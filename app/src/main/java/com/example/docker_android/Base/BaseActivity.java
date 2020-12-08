package com.example.docker_android.Base;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;



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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}
