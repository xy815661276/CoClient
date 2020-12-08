package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.ExecDialog;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class ExecActivity extends BaseActivity {
    private TextView Response;
    private TextView Command;

    public static final int REQUEST=1;
    private final boolean CREATED = false;
    private String exec_id;
    /**
     * 活动跳转接口
     * @param context
     * @param id
     * @param data2
     */
    public static void actionStart(Context context, String id, String data2) {
        Intent intent = new Intent(context, ExecActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_exec;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        Response = findViewById(R.id.cmd_result);
        Command = findViewById(R.id.run_command);
        Command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExecDialog execDialog = ExecDialog.newInstance();
                execDialog.setOnItemClickListener(new ExecDialog.OnItemClickListener() {
                    @Override
                    public boolean onOKClick(View view, Intent data) {
                        LoadingDialog.showDialogForLoading(ExecActivity.this);
                        String cmd = data.getStringExtra("Command");
                        String working_dir = data.getStringExtra("Working_dir");
                        DockerService.CreateExec(getIntent().getStringExtra("id"),cmd,working_dir,new okhttp3.Callback(){
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseData = response.body().string();
                                Log.d("exec",responseData);
                                JSONObject result = JSON.parseObject(responseData);
                                exec_id = result.getString("Id");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(response.code() == 201){
                                            DockerService.StartExec(exec_id, new okhttp3.Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {
                                                    e.printStackTrace();
                                                }
                                                @Override
                                                public void onResponse(Call call, Response response) throws IOException {
                                                    String responseData = response.body().string();
                                                    Log.d("start exec",responseData);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Response.setText(responseData);
                                                            LoadingDialog.hideDialogForLoading();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                        else{
                                            LoadingDialog.hideDialogForLoading();
                                            Toast.makeText(ExecActivity.this,"执行错误，请稍后再试。",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                        return true;
                    }
                });
                execDialog.setMargin(60)
                        .setClickOutCancel(true)
                        .show(getSupportFragmentManager());
            }
        });
    }

    @Override
    public void initToolbar() {

    }

}
