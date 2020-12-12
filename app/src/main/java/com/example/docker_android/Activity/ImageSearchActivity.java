package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.example.docker_android.Adapter.ImageAdapter;
import com.example.docker_android.Adapter.ImageSearchAdapter;
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Image.Image;
import com.example.docker_android.Entity.Image.ImageSearch;
import com.example.docker_android.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ImageSearchActivity extends BaseActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    List<ImageSearch> list = new ArrayList<>();
    /**
     * 活动跳转接口
     * @param context 上下文
     * @param term 搜索内容
     * @param data2 数据2
     */
    public static void actionStart(Context context, String term, String data2) {
        Intent intent = new Intent(context, ImageSearchActivity.class);
        intent.putExtra("term", term);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_image_search;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
            swipeRefreshLayout = findViewById(R.id.image_search_srl);
            recyclerView = findViewById(R.id.image_search_RecycleView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 启动刷新的控件
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        // 设置是否开始刷新,true为刷新，false为停止刷新
                        swipeRefreshLayout.setRefreshing(true);
                        loadData();
                    }
                });
            }
        });
        LoadingDialog.showDialogForLoading(ImageSearchActivity.this);
        loadData();
    }

    @Override
    public void initToolbar() {

    }
    @Override
    public void loadData() {
        list.clear();
        DockerService.getSearchImages(getIntent().getStringExtra("term"),new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d("search image", "onResponse: " + responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //停止刷新
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            JSONArray jsonArray = JSONArray.parseArray(responseData);
                            for (int i= 0;i < jsonArray.size();i++){
                                ImageSearch image_search = JSON.parseObject(jsonArray.getString(i), ImageSearch.class);
                                list.add(image_search);
                            }
                            ImageSearchAdapter adapter = new ImageSearchAdapter(list,ImageSearchActivity.this);
                            recyclerView.setAdapter(adapter);
                            GridLayoutManager layoutManager = new GridLayoutManager(ImageSearchActivity.this,1);
                            recyclerView.setLayoutManager(layoutManager);
                            LoadingDialog.hideDialogForLoading();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ImageSearchActivity.this, "Failed to get data, please try again later", Toast.LENGTH_SHORT).show();
                            LoadingDialog.hideDialogForLoading();
                        }
                    }
                });
            }
        });
    }
}