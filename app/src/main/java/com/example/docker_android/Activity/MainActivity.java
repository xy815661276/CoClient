package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.docker_android.R;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.card_view_container)
    CardView container;
    @BindView(R.id.card_view_Image)
    CardView image;
    @BindView(R.id.mainpage_toolbar)
    Toolbar toolbar;
    private long exitTime = 0;//点击两次返回退出时间
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);  //使用BindView必须，不然会崩溃
        container.setOnClickListener(this);
        image.setOnClickListener(this);
        initToolbar();
    }

    /**
     *点击事件处理
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.card_view_container:
                ContainerActivity.actionStart(MainActivity.this,"","");
                break;
            case R.id.card_view_Image:
                ImageActivity.actionStart(MainActivity.this,"","");
                break;
            default:
                break;
        }
    }

    /**
     * 检测返回键，实现按下两次返回退出程序
     * @param keyCode
     * @param event
     * @return
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
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = SystemClock.elapsedRealtime();
        } else {
            finish();
        }
    }
    /**
     * 如果是继承的AppCompatActivity,取代了原有的actionbar,需要手动的将toolbar中的菜单设置上来
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu,menu);//将toolbar中的菜单添加上来
        return true;
    }
    /**
     * 初始化Toolbar的数据
     */
    private void initToolbar() {
        //也可以在布局中设置这些属性,具体见布局文件
//        toolbar.setNavigationIcon(R.mipmap.ic_launcher);//设置最左侧图标
        //toolbar.setLogo(R.mipmap.ic_launcher);//设置程序logo图标
        toolbar.setTitle("toolbar标题");
        toolbar.setSubtitle("子标题");
        toolbar.setTitleTextColor(Color.parseColor("#ff0000"));//设置标题的字体颜色
        toolbar.setSubtitleTextColor(Color.parseColor("#00ff00"));//设置子标题的字体颜色
        setSupportActionBar(toolbar); //取代原本的actionbar,继承activity可以不设置,不替换显示不了菜单
        //单击事件需要在setSupportActionBar方法后,因为原本的actionbar也有这个单击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了最左边的图标", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
