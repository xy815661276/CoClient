package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.R;
import com.yuyh.jsonviewer.library.JsonRecyclerView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ContainerDetailsTextActivity extends BaseActivity {
    private JsonRecyclerView container_info;

    /**
     * 活动跳转接口
     * @param context 上下文
     * @param id container id
     * @param data2 暂时未用到
     */
    public static void actionStart(Context context, String id, String data2) {
        Intent intent = new Intent(context, ContainerDetailsTextActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_container_details_text;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        container_info = findViewById(R.id.container_info);
        loadData();
    }

    @Override
    public void initToolbar() {

    }
    @Override
    public void loadData() {
        LoadingDialog.showDialogForLoading(ContainerDetailsTextActivity.this);
        DockerService.getContainer(getIntent().getStringExtra("id"), new Callback() {
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
                            container_info.bindJson(responseData);
                        }
                        else{
                            Toast.makeText(ContainerDetailsTextActivity.this,"Failed,try again later",Toast.LENGTH_SHORT).show();
                        }
                        LoadingDialog.hideDialogForLoading();
                    }
                });
            }
        });
    }
}