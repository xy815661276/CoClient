package com.example.docker_android.Activity;

import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.Entity.Image.Image;
import com.example.docker_android.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
public class HomeActivity extends BaseActivity{

    private RelativeLayout containerRun;
    private RelativeLayout containerStop;
    private RelativeLayout image;
    private RelativeLayout os;
    private Toolbar toolbar;
    private TextView running;
    private TextView stopped;
    private TextView images;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BottomNavigationView bottomNavigationView;


    private long exitTime = 0;//点击两次返回退出时间
    private final List<Container> list_run = new ArrayList<>();
    public static List<Container> list_stop = new ArrayList<>();
    private final List<Image> list_image = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        containerRun = findViewById(R.id.runningHeader);
        containerStop = findViewById(R.id.stoppedHeader);
        image = findViewById(R.id.imagesHeader);
        os = findViewById(R.id.osHeader);
        toolbar = findViewById(R.id.mainpage_toolbar);
        running = findViewById(R.id.running);
        stopped = findViewById(R.id.stopped);
        images = findViewById(R.id.images);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        swipeRefreshLayout = findViewById(R.id.container_main_srl);
        //监听跳转
        containerRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContainerRunningActivity.actionStart(HomeActivity.this,"","");
            }
        });
        containerStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContainerStoppedActivity.actionStart(HomeActivity.this,"","");
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageActivity.actionStart(HomeActivity.this,"","");
            }
        });
        os.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OSInfoActivity.actionStart(HomeActivity.this,"","");
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 启动刷新的控件
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        // 设置是否开始刷新,true为刷新，false为停止刷新
                        swipeRefreshLayout.setRefreshing(true);
                        loadData();
                    }
                });
            }
        });
        loadData();
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
            Toast.makeText(HomeActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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
    @Override
    public void initToolbar() {

    }
    /**
     * 加载数据
     */
    @Override
    public void loadData() {
        //先清除原来的数据
        list_run.clear();
        list_stop.clear();
        list_image.clear();

        DockerService.getContainers(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //toString方法未重写，这里使用string()方法
                //string不能调用两次 被调用一次就关闭了，这里调用两次会报异常
                final String responseData = response.body().string();
                Log.d("Component", "onResponse: " + responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            JSONArray jsonArray = JSONArray.parseArray(responseData);
                            for (int i= 0;i < jsonArray.size();i++){
                                Log.d("container", "onResponse: " + jsonArray.getString(i));
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Log.d("container",jsonObject.getString("Names"));
                                Container container = JSON.parseObject(jsonArray.getString(i), Container.class);
                                if(container.getState().equals("running"))
                                    list_run.add(container);
                                else
                                    list_stop.add(container);
                            }
                            running.setText(String.valueOf(list_run.size()));
                            stopped.setText(String.valueOf(list_stop.size()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HomeActivity.this, "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        DockerService.getImages(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string(); //toString方法未重写，这里使用string()方法 //string不能调用两次 被调用一次就关闭了，这里调用两次会报异常
                Log.d("Component", "onResponse: " + responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = JSONArray.parseArray(responseData);
                            for (int i= 0;i < jsonArray.size();i++){
                                Log.d("container", "onResponse: " + jsonArray.getString(i));
                                Image image = JSON.parseObject(jsonArray.getString(i), Image.class);
                                list_image.add(image);
                            }
                            images.setText(String.valueOf(list_image.size()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HomeActivity.this, "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
