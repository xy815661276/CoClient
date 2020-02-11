package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.docker_android.Adapter.MainPagerAdapter;
import com.example.docker_android.R;
import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_tabLayout)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.main_viewPager)
    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);  //使用BindView必须，不然会崩溃
        MainPagerAdapter mFragmentAdapter = new MainPagerAdapter(getSupportFragmentManager(),this);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mFragmentAdapter);
        mSlidingTabLayout.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }
}
