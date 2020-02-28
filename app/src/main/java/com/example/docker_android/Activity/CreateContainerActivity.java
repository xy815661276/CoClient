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
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.R;
import com.wang.avi.AVLoadingIndicatorView;


import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class CreateContainerActivity extends AppCompatActivity {
    @BindView(R.id.container_name)
    EditText Container_name;
    @BindView(R.id.image_name)
    EditText Image_name;
    @BindView(R.id.host_port)
    EditText Host_port;
    @BindView(R.id.container_port)
    EditText Container_port;
    @BindView(R.id.host_volume)
    EditText Host_volume;
    @BindView(R.id.container_volume)
    EditText Container_volume;
    @BindView(R.id.submit)
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_container);
        ButterKnife.bind(this);  //使用BindView必须，不然会崩溃
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
                    Toast.makeText(CreateContainerActivity.this,"输入有空，请重新输入",Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(CreateContainerActivity.this,"创建成功",Toast.LENGTH_LONG).show();
                                            else if(response.code()==400)
                                                Toast.makeText(CreateContainerActivity.this,"创建失败，错误的参数:"+result.getString("message"),Toast.LENGTH_LONG).show();
                                            else if(response.code()==404)
                                                Toast.makeText(CreateContainerActivity.this,"创建失败，没有该容器",Toast.LENGTH_LONG).show();
                                            else if(response.code()==409)
                                                Toast.makeText(CreateContainerActivity.this,"创建失败，参数冲突:"+result.getString("message"),Toast.LENGTH_LONG).show();
                                            else if(response.code()==500)
                                                Toast.makeText(CreateContainerActivity.this,"创建失败，服务器错误",Toast.LENGTH_LONG).show();
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
}
