package com.example.docker_android.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.ExecDialog;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LogsActivity extends BaseActivity {
    private TextView log;

    /**
     * 活动跳转接口
     * @param context
     * @param id
     * @param data2
     */
    public static void actionStart(Context context, String id, String data2) {
        Intent intent = new Intent(context, LogsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_logs;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        log = findViewById(R.id.logs_result);
        loadData();
    }

    @Override
    public void initToolbar() {

    }
    @Override
    public void loadData() {
        LoadingDialog.showDialogForLoading(LogsActivity.this);
        DockerService.getLogs(getIntent().getStringExtra("id"), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.code() == 200){
                            log.setText(responseData);
                        }
                        else{
                            Toast.makeText(LogsActivity.this,"Failed,try again later",Toast.LENGTH_SHORT).show();
                        }
                        LoadingDialog.hideDialogForLoading();
                    }
                });
            }
        });
    }

}
