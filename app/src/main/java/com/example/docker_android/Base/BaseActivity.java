package com.example.docker_android.Base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docker_android.R;


/**
 * by xavier
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置所有只能竖屏
        beforeSetView();
        setContentView(getLayoutId());
        initToolbar();
        initViews(savedInstanceState);
        initFragment();
        setTranslucentStatus();
    }

    public abstract int getLayoutId();

    public abstract void initViews(Bundle savedInstanceState);

    public abstract void initToolbar();

    public void beforeSetView() {
    }

    public void initFragment() {

    }

    public void loadData() {

    }

    public void showProgressBar() {

    }

    public void hideProgressBar() {

    }

    public void initRecyclerView() {

    }

    protected void initRefreshLayout() {
    }

    public void finishTask() {

    }

    //设置透明状态栏
    public void setTranslucentStatus() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}
