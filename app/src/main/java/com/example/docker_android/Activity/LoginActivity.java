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
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
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
}