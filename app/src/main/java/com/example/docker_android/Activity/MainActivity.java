package com.example.docker_android.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.docker_android.Adapter.ViewPagerAdapter;
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Fragment.ContainerFragment;
import com.example.docker_android.Fragment.HomeFragment;
import com.example.docker_android.Fragment.ImageFragment;
import com.example.docker_android.Fragment.OverviewFragment;
import com.example.docker_android.Fragment.TestFragment;
import com.example.docker_android.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private MenuItem menuItem;
    //点击两次返回退出时间
    private long exitTime = 0;
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.vp);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        List<Fragment> list = new ArrayList<>();
        list.add(new OverviewFragment());
        list.add(new ContainerFragment());
        list.add(new ImageFragment());
        viewPagerAdapter.setList(list);

    }

    @Override
    public void initToolbar() {

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.page_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.page_container:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.page_image:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    /**
     * 检测返回键，实现按下两次返回退出程序
     * @param keyCode code
     * @param event event
     * @return boolean
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void exit() {
        if ((SystemClock.elapsedRealtime() - exitTime) > 2000){
            Toast.makeText(MainActivity.this, "Press again to exit", Toast.LENGTH_SHORT).show();
            exitTime = SystemClock.elapsedRealtime();
        } else {
            finish();
        }
    }
}