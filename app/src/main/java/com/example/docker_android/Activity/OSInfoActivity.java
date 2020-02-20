package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class OSInfoActivity extends AppCompatActivity {

    @BindView(R.id.docker)
    TextView dockerTV;
    @BindView(R.id.api)
    TextView apiTV;
    @BindView(R.id.os)
    TextView osTV;
    @BindView(R.id.arch)
    TextView archTV;
    @BindView(R.id.kernel)
    TextView kernelTV;

    /**
     * 活动跳转接口
     * @param context
     * @param data1
     * @param data2
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, OSInfoActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osinfo);
        ButterKnife.bind(this);  //使用BindView必须，不然会崩溃
        LoadData();
    }

    private void LoadData(){
        DockerService.getInfo(new okhttp3.Callback(){
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
                        try {
                            JSONObject tmp = JSON.parseObject(responseData);
                            String docker = tmp.getString("ServerVersion");
                            String os = tmp.getString("OSType");
                            String arch = tmp.getString("Architecture");
                            String kernel = tmp.getString("KernelVersion");
                            dockerTV.setText(docker);
                            osTV.setText(os);
                            archTV.setText(arch);
                            kernelTV.setText(kernel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(OSInfoActivity.this, "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
