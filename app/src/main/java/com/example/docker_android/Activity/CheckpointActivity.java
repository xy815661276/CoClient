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
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Adapter.CheckpointsAdapter;
import com.example.docker_android.Adapter.ContainerAdapter;
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.DockerAPI.DockerTerminalService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class CheckpointActivity extends BaseActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<String> list = new ArrayList<>();
    /**
     * 活动跳转接口
     * @param context 上下文
     * @param id 容器id
     * @param data2 暂定
     */
    public static void actionStart(Context context, String id, String data2) {
        Intent intent = new Intent(context, CheckpointActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("data2", data2);
        context.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_checkpoint;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        swipeRefreshLayout = findViewById(R.id.checkpoint_srl);
        recyclerView = findViewById(R.id.checkpoint_RecyclerView);
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
        loadData();
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void loadData() {
        list.clear();
        String id = getIntent().getExtras().getString("id");
        list = DockerTerminalService.GetCheckpoint(id);
        CheckpointsAdapter adapter = new CheckpointsAdapter(list,CheckpointActivity.this,id);
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(CheckpointActivity.this,1);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout.setRefreshing(false);
    }
}