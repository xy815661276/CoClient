package com.example.docker_android.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Activity.ContainerMigrateActivity;
import com.example.docker_android.Activity.ContainerRunningActivity;
import com.example.docker_android.Activity.ContainerStoppedActivity;
import com.example.docker_android.Activity.CreateContainerActivity;
import com.example.docker_android.Base.BaseLazyFragment;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.Dialog.PromptDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ContainerFragment extends BaseLazyFragment {

    private CardView containerStop;
    private CardView containerCreate;
    private CardView containerDelete;
    private CardView containerMigrate;
    private TextView stopped;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static final int REQUEST=1;


    private final List<Container> list = new ArrayList<>();
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_container;
    }

    @Override
    public void initFragments(View view) {
        containerStop = view.findViewById(R.id.home_container_stop_card);
        containerCreate = view.findViewById(R.id.home_container_create_card);
        containerDelete = view.findViewById(R.id.home_container_delete_card);
        stopped = view.findViewById(R.id.stopped);
        swipeRefreshLayout = view.findViewById(R.id.container_srl);
        containerMigrate = view.findViewById(R.id.home_container_migrate_card);
    }

    @Override
    public void finishCreateView(Bundle state) {
        containerStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContainerStoppedActivity.actionStart(getActivity(),"","");
            }
        });
        containerCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateContainerActivity.actionStart(getActivity(),"","");
            }
        });
        containerDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PromptDialog promptDialog = PromptDialog.newInstance("Are you sure to delete the stopped containers?");
                promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                    @Override
                    public boolean onOKClick(View view, Intent data) {
                        LoadingDialog.showDialogForLoading(getActivity());
                        DockerService.DeleteStopContainer(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                LoadingDialog.hideDialogForLoading();
                                final String responseData = response.body().string();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if(response.code() == 200){
                                                Toast.makeText(getActivity(),"Successfully deleted",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getActivity(),"Delete failed,try again later",Toast.LENGTH_SHORT).show();
                                            }
                                            loadData();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getActivity(), "Delete failed,try again later", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                        return true;
                    }
                });
                promptDialog.setMargin(30)
                        .setClickOutCancel(true)
                        .show(getActivity().getSupportFragmentManager());
            }
        });
        containerMigrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContainerMigrateActivity.actionStart(getActivity(),"","");
            }
        });
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
    public void loadData() {
        //先清除原来的数据
        list.clear();

        DockerService.getContainers(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //toString方法未重写，这里使用string()方法
                //string不能调用两次 被调用一次就关闭了，这里调用两次会报异常
                final String responseData = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            JSONArray jsonArray = JSONArray.parseArray(responseData);
                            for (int i= 0;i < jsonArray.size();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Container container = JSON.parseObject(jsonArray.getString(i), Container.class);
                                list.add(container);
                            }
                            stopped.setText(String.valueOf(list.size()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed,please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}