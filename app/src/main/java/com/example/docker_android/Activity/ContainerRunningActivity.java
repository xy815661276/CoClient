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
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Adapter.ContainerRunningAdapter;
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ContainerRunningActivity extends BaseActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private final List<Container> list = new ArrayList<>();
    /**
     * 活动跳转接口
     * @param context
     * @param data1
     * @param data2
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, ContainerRunningActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_container_running;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        swipeRefreshLayout = findViewById(R.id.container_running_srl);
        recyclerView = findViewById(R.id.running_RecycleView);
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
        LoadingDialog.showDialogForLoading(ContainerRunningActivity.this);
        loadData();
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void loadData() {
        list.clear();
        DockerService.getContainers(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string(); //toString方法未重写，这里使用string()方法 //string不能调用两次 被调用一次就关闭了，这里调用两次会报异常
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //停止刷新
                        swipeRefreshLayout.setRefreshing(false);
                        LoadingDialog.hideDialogForLoading();
                        try {
                            JSONArray jsonArray = JSONArray.parseArray(responseData);
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Container container = JSON.parseObject(jsonArray.getString(i), Container.class);
                                if (container.getState().equals("running"))
                                    list.add(container);
                            }
                            ContainerRunningAdapter adapter = new ContainerRunningAdapter(list,ContainerRunningActivity.this);
                            recyclerView.setAdapter(adapter);
                            GridLayoutManager layoutManager = new GridLayoutManager(ContainerRunningActivity.this,1);
                            recyclerView.setLayoutManager(layoutManager);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ContainerRunningActivity.this, "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
