package com.example.docker_android.Base;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * by xavier
 */
public abstract class BaseLazyFragment extends Fragment {

    private View parentView;
    private FragmentActivity activity;
    protected boolean isPrepared;
    protected boolean isVisible;

    /**
     * Fragment继承并适配对应的Layout
     * @return
     */
    public abstract
    @LayoutRes
    //安卓注解，表明该参数、变量或者函数返回值应该是一个 layout 布局文件类型的资源
    int getLayoutResId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(getLayoutResId(), container, false);
        activity = getSupportActivity();
        return parentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFragments(view);
        finishCreateView(savedInstanceState);
    }

    public abstract void initFragments(View view);

    public abstract void finishCreateView(Bundle state);

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (FragmentActivity) activity; //绑定Activity
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    public FragmentActivity getSupportActivity() {
        return super.getActivity();
    }

    public ActionBar getSupportActionBar() {
        return getSupportActivity().getActionBar();
    }

    public Context getApplicationContext() {
        return this.activity == null ? (getActivity() == null ?
                null : getActivity().getApplicationContext()) : this.activity.getApplicationContext();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    //Fragment方法规范：

    /**
     * fragment显示时才加载数据
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * fragment懒加载方法
     */
    protected void lazyLoad() {
    }

    /**
     * fragment隐藏
     */
    protected void onInvisible() {
    }

    /**
     * 加载数据
     */
    protected void loadData() {
    }

    /**
     * 显示进度条
     */
    protected void showProgressBar() {
    }

    /**
     * 隐藏进度条
     */
    protected void hideProgressBar() {
    }

    /**
     * 初始化recyclerView
     */
    protected void initRecyclerView() {
    }

    /**
     * 初始化refreshLayout
     */
    protected void initRefreshLayout() {
    }

    /**
     * 设置数据显示
     */
    protected void finishTask() {
    }

}
