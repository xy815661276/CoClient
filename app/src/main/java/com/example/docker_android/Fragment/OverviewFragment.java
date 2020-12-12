package com.example.docker_android.Fragment;

import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Base.BaseLazyFragment;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class OverviewFragment extends BaseLazyFragment {

    private TextView dockerTV;
    private TextView apiTV;
    private TextView osTV;
    private TextView archTV;
    private TextView kernelTV;
    private TextView runtimeTV;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_overview;
    }

    @Override
    public void initFragments(View view) {
        dockerTV = view.findViewById(R.id.docker);
        apiTV = view.findViewById(R.id.api);
        osTV = view.findViewById(R.id.os);
        archTV = view.findViewById(R.id.arch);
        kernelTV = view.findViewById(R.id.kernel);
        runtimeTV = view.findViewById(R.id.runtime);
        swipeRefreshLayout = view.findViewById(R.id.overview_srl);
    }

    @Override
    public void finishCreateView(Bundle state) {
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
    public void loadData(){
        DockerService.getVersion(new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject tmp = JSON.parseObject(responseData);
                            String docker = tmp.getString("Version");
                            String api = tmp.getString("ApiVersion");
                            String os = tmp.getString("Os");
                            String arch = tmp.getString("Arch");
                            os = "Android";
                            String runtime = tmp.getString("DefaultRuntime");
                            String kernel = tmp.getString("KernelVersion");
                            kernel = kernel.substring(0,kernel.indexOf("/"));
                            dockerTV.setText(docker);
                            osTV.setText(os);
                            apiTV.setText(api);
                            archTV.setText(arch);
                            runtimeTV.setText(runtime);
                            kernelTV.setText(kernel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed to get data, please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        DockerService.getInfo(new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                swipeRefreshLayout.setRefreshing(false);
                final String responseData = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject tmp = JSON.parseObject(responseData);
                            String runtime = tmp.getString("DefaultRuntime");
                            runtimeTV.setText(runtime);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed to get data, please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}