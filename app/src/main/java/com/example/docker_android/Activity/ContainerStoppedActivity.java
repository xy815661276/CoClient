package com.example.docker_android.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.example.docker_android.Adapter.ContainerAdapter;
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.Fragment.ContainerFragment_new;
import com.example.docker_android.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ContainerStoppedActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private final List<Container> list_container = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * 活动跳转接口
     * @param context
     * @param data1
     * @param data2
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, ContainerStoppedActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_container_stopped;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        swipeRefreshLayout = findViewById(R.id.container_stopped_srl);
        recyclerView = findViewById(R.id.stopped_RecycleView);
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
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void loadData(){
        list_container.clear();
        DockerService.getContainers(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadingDialog.hideDialogForLoading();
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            JSONArray jsonArray = JSONArray.parseArray(responseData);
                            for (int i= 0;i < jsonArray.size();i++) {
                                Container container = JSON.parseObject(jsonArray.getString(i), Container.class);
                                list_container.add(container);
                            }
                            ContainerAdapter adapter = new ContainerAdapter(list_container, ContainerStoppedActivity.this);
                            recyclerView.setAdapter(adapter);
                            GridLayoutManager layoutManager = new GridLayoutManager(ContainerStoppedActivity.this,1);
                            recyclerView.setLayoutManager(layoutManager);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ContainerStoppedActivity.this, "Failed, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onStart() {
        loadData();
        super.onStart();
    }

}
