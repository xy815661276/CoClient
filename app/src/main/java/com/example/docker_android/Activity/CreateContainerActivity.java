package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.Fragment.ContainerFragment;
import com.example.docker_android.R;
import com.wang.avi.AVLoadingIndicatorView;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class CreateContainerActivity extends BaseActivity {
    EditText Container_name;
    EditText Image_name;
    EditText Host_port;
    EditText Container_port;
    EditText Host_volume;
    EditText Container_volume;
    TextView submit;
    /**
     * 活动跳转接口
     * @param context
     * @param data1
     * @param data2
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, CreateContainerActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_container;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        Container_name = findViewById(R.id.container_name);
        Image_name = findViewById(R.id.image_name);
        Host_port = findViewById(R.id.host_port);
        Container_port = findViewById(R.id.container_port);
        Host_volume = findViewById(R.id.host_volume);
        Container_volume = findViewById(R.id.container_volume);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadingDialog.showDialogForLoading(CreateContainerActivity.this);
                String container_name = Container_name.getText().toString();
                String image_name = Image_name.getText().toString();
                String container_port = Container_port.getText().toString();
                String host_port = Host_port.getText().toString();
                String host_volume = Host_volume.getText().toString();
                String container_volume = Container_volume.getText().toString();
                if(container_name.equals("")||image_name.equals("")||container_port.equals("")||host_port.equals("")||host_volume.equals("")||container_volume.equals("")){
                    Toast.makeText(CreateContainerActivity.this,"Input available",Toast.LENGTH_SHORT).show();
                    LoadingDialog.hideDialogForLoading();
                }
                else {
                    new Handler().postDelayed(new Runnable() {
                        //加载动画太快，延时几秒
                        @Override
                        public void run() {
                            DockerService.createContainer(container_name,image_name,host_port,container_port,host_volume,container_volume,new okhttp3.Callback(){
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    JSONObject result = JSON.parseObject(response.body().string());
                                    Log.d("create container",result.toJSONString());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(response.code()==201)
                                                Toast.makeText(CreateContainerActivity.this,"Created successfully",Toast.LENGTH_LONG).show();
                                            else if(response.code()==400)
                                                Toast.makeText(CreateContainerActivity.this,"Creation failed, wrong parameters:"+result.getString("message"),Toast.LENGTH_LONG).show();
                                            else if(response.code()==404)
                                                Toast.makeText(CreateContainerActivity.this,"Creation failed, there is no such container",Toast.LENGTH_LONG).show();
                                            else if(response.code()==409)
                                                Toast.makeText(CreateContainerActivity.this,"Creation failed, parameter conflict:"+result.getString("message"),Toast.LENGTH_LONG).show();
                                            else if(response.code()==500)
                                                Toast.makeText(CreateContainerActivity.this,"Creation failed, server error",Toast.LENGTH_LONG).show();
                                            LoadingDialog.hideDialogForLoading();
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    },1000);
                }
            }
        });
    }

    @Override
    public void initToolbar() {

    }
}
