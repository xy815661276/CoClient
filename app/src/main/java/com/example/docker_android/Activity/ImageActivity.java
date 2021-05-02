package com.example.docker_android.Activity;

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
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Image.Image;
import com.example.docker_android.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ImageActivity extends BaseActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    List<Image> list = new ArrayList<>();
    /**
     * 活动跳转接口
     * @param context
     * @param data1
     * @param data2
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        swipeRefreshLayout = findViewById(R.id.image_srl);
        recyclerView = findViewById(R.id.image_recycler_View);
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
        LoadingDialog.showDialogForLoading(ImageActivity.this);
        loadData();
    }

    @Override
    public void initToolbar() {
        //TODO
    }

    @Override
    public void loadData() {
        list.clear();
        DockerService.getImages(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string(); //toString方法未重写，这里使用string()方法 //string不能调用两次 被调用一次就关闭了，这里调用两次会报异常
                Log.d("Component", "onResponse: " + responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //停止刷新
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            JSONArray jsonArray = JSONArray.parseArray(responseData);
                            for (int i= 0;i < jsonArray.size();i++){
                                Log.d("container", "onResponse: " + jsonArray.getString(i));
                                Image image = JSON.parseObject(jsonArray.getString(i), Image.class);
                                list.add(image);
                            }
                            ImageAdapter adapter = new ImageAdapter(list,ImageActivity.this);
                            recyclerView.setAdapter(adapter);
                            GridLayoutManager layoutManager = new GridLayoutManager(ImageActivity.this,1);
                            recyclerView.setLayoutManager(layoutManager);
                            LoadingDialog.hideDialogForLoading();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ImageActivity.this, "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
                            LoadingDialog.hideDialogForLoading();
                        }
                    }
                });
            }
        });
    }
}
