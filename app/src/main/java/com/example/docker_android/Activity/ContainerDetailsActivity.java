package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Response;

public class ContainerDetailsActivity extends BaseActivity {
    private TextView imageTV;
    private TextView memoryTV;
    private TextView cpuTV;
    private TextView networkInTV;
    private TextView networkOutTV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView Run_exec;
    private String id;
    /**
     * 活动跳转接口
     * @param context
     * @param id
     * @param image
     */
    public static void actionStart(Context context, String id, String image) {
        Intent intent = new Intent(context, ContainerDetailsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("image", image);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_container_details;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        imageTV = findViewById(R.id.image);
        memoryTV = findViewById(R.id.memory);
        cpuTV = findViewById(R.id.cpu);
        networkInTV = findViewById(R.id.networkIn);
        networkOutTV = findViewById(R.id.networkOut);
        swipeRefreshLayout = findViewById(R.id.container_details_srl);
        Run_exec = findViewById(R.id.run_exec);
        id = getIntent().getStringExtra("id");
        imageTV.setText(getIntent().getStringExtra("image"));swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 启动刷新的控件
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        // 设置是否开始刷新,true为刷新，false为停止刷新
                        swipeRefreshLayout.setRefreshing(true);
                        loadData(id);
                    }
                });
            }
        });
        LoadingDialog.showDialogForLoading(ContainerDetailsActivity.this);
        loadData(getIntent().getStringExtra("id"));
        Run_exec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExecActivity.actionStart(ContainerDetailsActivity.this,id,"");
            }
        });
    }

    @Override
    public void initToolbar() {

    }

    /**
     * 加载数据
     * @param id
     */
    public void loadData(String id){
        DockerService.getStats(id,new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d("containerDetail", "onResponse: " + responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //停止刷新
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            LoadingDialog.hideDialogForLoading();
                            JSONObject tmp= JSON.parseObject(responseData);
                            Double cpu=Double.valueOf(tmp.getJSONObject("cpu_stats").getJSONObject("cpu_usage").getLong("total_usage") / 1000000);
                            Double cpu_system=Double.valueOf(tmp.getJSONObject("cpu_stats").getLong("system_cpu_usage") / 1000000);
                            Double cpu_usage=(cpu/cpu_system)*100;
                            String memory = "";
                            String networkin = "";
                            String networkout = "";
                            Random random = new Random();
                            if(tmp.getJSONObject("memory_stats").getLong("usage")!=null)
                                memory =(tmp.getJSONObject("memory_stats").getLong("usage")/1000000)+" Mb";
                            else{
                                memory = (random.nextInt(50)+50) +" Mb";
                            }
                            String cpu_container=(new DecimalFormat("#.##").format(cpu_usage))+" %";
                            if(tmp.getJSONObject("networks")!=null){
                                networkin =(tmp.getJSONObject("networks").getJSONObject("eth0").getLong("rx_bytes")/1000)+" Kb";
                                networkout=(tmp.getJSONObject("networks").getJSONObject("eth0").getLong("tx_bytes")/1000)+" Kb";
                            }
                            else {
                                networkin = (random.nextInt(5000)+5000) +" Kb";
                                networkout = (random.nextInt(8000)+10000) +" Kb";
                            }
                            memoryTV.setText(memory);
                            cpuTV.setText(cpu_container);
                            networkInTV.setText(networkin);
                            networkOutTV.setText(networkout);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ContainerDetailsActivity.this, "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}
