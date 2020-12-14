package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.R;

public class StartActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        //展示3s后前往主页
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @Override
    public void initToolbar() {

    }
    @Override
    //设置透明状态栏
    public void setTranslucentStatus() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.pure_white));
    }
}