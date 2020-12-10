package com.example.docker_android.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Activity.OSInfoActivity;
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
    }

    @Override
    public void finishCreateView(Bundle state) {
        loadData();
    }
    @Override
    public void loadData(){
        DockerService.getInfo(new okhttp3.Callback(){
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
                            String docker = tmp.getString("ServerVersion");
                            String os = tmp.getString("OSType");
                            String arch = tmp.getString("Architecture");
                            String runtime = tmp.getString("DefaultRuntime");
                            String kernel = tmp.getString("KernelVersion");
                            kernel = kernel.substring(0,kernel.indexOf("/"));
                            dockerTV.setText(docker);
                            osTV.setText(os);
                            archTV.setText(arch);
                            runtimeTV.setText(runtime);
                            kernelTV.setText(kernel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}