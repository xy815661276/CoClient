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
import com.example.docker_android.Adapter.ContainerAdapter;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class ContainerRunningActivity extends AppCompatActivity {
    @BindView(R.id.container_running_srl)
    SwipeRefreshLayout swipeRefreshLayout;
    List<Container> list = new ArrayList<>();
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
    @BindView(R.id.running_RecycleView)
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_running);
        ButterKnife.bind(this);  //使用BindView必须，不然会崩溃
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 启动刷新的控件
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        // 设置是否开始刷新,true为刷新，false为停止刷新
                        swipeRefreshLayout.setRefreshing(true);
                        LoadData();
                    }
                });
            }
        });
        LoadingDialog.showDialogForLoading(ContainerRunningActivity.this);
        LoadData();
    }

    public void LoadData() {
        list.clear();
        DockerService.getContainers(new okhttp3.Callback() {
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
                        LoadingDialog.hideDialogForLoading();
                        try {
                            JSONArray jsonArray = JSONArray.parseArray(responseData);
                            for (int i = 0; i < jsonArray.size(); i++) {
                                Log.d("container", "onResponse: " + jsonArray.getString(i));
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Log.d("container", jsonObject.getString("Names"));
                                Container container = JSON.parseObject(jsonArray.getString(i), Container.class);
                                if (container.getState().equals("running"))
                                    list.add(container);
                            }
                            ContainerAdapter adapter = new ContainerAdapter(list,ContainerRunningActivity.this);
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
