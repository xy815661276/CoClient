package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.R;
import com.example.docker_android.Utils.RootCmd;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    private EditText ip;
    private EditText port;
    private Button login;
    private Button exit;
    private Button start;
    private static boolean isDockerStart = false;
    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ip = findViewById(R.id.username);
        port = findViewById(R.id.password);
        login = findViewById(R.id.login);
        exit = findViewById(R.id.exit);
        start = findViewById(R.id.startDocker);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip_ = ip.getText().toString().trim();
                String port_ = port.getText().toString().trim();
                if(ip_.equals("") || port_.equals("")){
                    Toast.makeText(LoginActivity.this,"There are empty items",Toast.LENGTH_SHORT).show();
                }
                else if(!isIP(ip_)){
                    Toast.makeText(LoginActivity.this,"IP address format is incorrect",Toast.LENGTH_SHORT).show();
                }
                else {
                    LoadingDialog.showDialogForLoading(LoginActivity.this);
                    DockerService.setAddress(ip_,port_);
                    DockerService.getInfo(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this,"Login Failed,Check the input",Toast.LENGTH_SHORT).show();
                                    isDockerStart = false;
                                    LoadingDialog.hideDialogForLoading();
                                }
                            });
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(response.code() == 200){
                                            Toast.makeText(LoginActivity.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            isDockerStart = true;
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                                            isDockerStart = false;
                                        }
                                        LoadingDialog.hideDialogForLoading();
                                    }
                                });
                        }
                    });
                }
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDockerStart){
                    Toast.makeText(LoginActivity.this,"Docker has started!",Toast.LENGTH_SHORT).show();
                }
                else {
                    LoadingDialog.showDialogForLoading(LoginActivity.this);
                    DockerService.setAddress("127.0.0.1","2375");
                    DockerService.getInfo(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startDocker();
                                    isDockerStart = true;
                                    LoadingDialog.hideDialogForLoading();
                                }
                            });
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(response.code() == 200){
                                        Toast.makeText(LoginActivity.this,"Docker has started!",Toast.LENGTH_SHORT).show();
                                        isDockerStart = true;
                                        LoadingDialog.hideDialogForLoading();
                                    }
                                    else {
                                        startDocker();
                                        isDockerStart = true;
                                    }
                                    LoadingDialog.hideDialogForLoading();
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @Override
    public void initToolbar() {

    }
    public boolean isIP(String ip){
        String regex="\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        Pattern pattern= Pattern.compile(regex);
        Matcher matcher=pattern.matcher(ip);
        return matcher.matches();
    }
    @Override
    //设置透明状态栏
    public void setTranslucentStatus() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.mainPage_background));
    }

    private void startDocker(){
        // 运行启动Docker的脚本
        RootCmd.execRootCmd("dockerd.sh");
        try {
            Thread.currentThread().sleep(2000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}