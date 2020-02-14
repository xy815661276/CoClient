package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.docker_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.card_view_container)
    CardView container;
    @BindView(R.id.card_view_Image)
    CardView image;
    private long exitTime = 0;//点击两次返回退出时间
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);  //使用BindView必须，不然会崩溃
        container.setOnClickListener(this);
        image.setOnClickListener(this);
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
}
