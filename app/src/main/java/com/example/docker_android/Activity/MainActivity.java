package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
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
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.Entity.Image.Image;
import com.example.docker_android.R;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import com.wangjie.rapidfloatingactionbutton.util.RFABShape;
import com.wangjie.rapidfloatingactionbutton.util.RFABTextUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;
public class MainActivity extends AppCompatActivity implements View.OnClickListener,RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener{
    @BindView(R.id.runningHeader)
    RelativeLayout containerRun;
    @BindView(R.id.stoppedHeader)
    RelativeLayout containerStop;
    @BindView(R.id.imagesHeader)
    RelativeLayout image;
    @BindView(R.id.osHeader)
    RelativeLayout os;
    @BindView(R.id.mainpage_toolbar)
    Toolbar toolbar;
    @BindView(R.id.running)
    TextView running;
    @BindView(R.id.stopped)
    TextView stopped;
    @BindView(R.id.images)
    TextView images;
    @BindView(R.id.activity_main_rfal)
    RapidFloatingActionLayout rfaLayout;
    @BindView(R.id.activity_main_rfab)
    RapidFloatingActionButton rfaBtn;
    @BindView(R.id.container_main_srl)
    SwipeRefreshLayout swipeRefreshLayout;
    private RapidFloatingActionHelper rfabHelper;
    private long exitTime = 0;//点击两次返回退出时间
    private List<Container> list_run = new ArrayList<>();
    public static List<Container> list_stop = new ArrayList<>();
    private List<Image> list_image = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);  //使用BindView必须，不然会崩溃
        initButton();
        containerRun.setOnClickListener(this);
        containerStop.setOnClickListener(this);
        image.setOnClickListener(this);
        os.setOnClickListener(this);
        initToolbar();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 启动刷新的控件
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        // 设置是否开始刷新,true为刷新，false为停止刷新
                        swipeRefreshLayout.setRefreshing(true);
                        LoadData();
                    }
                });
            }
        });
        LoadData();
    }
    /**
     *点击事件处理
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.runningHeader:
                ContainerRunningActivity.actionStart(MainActivity.this,"","");
                break;
            case R.id.stoppedHeader:
                ContainerStoppedActivity.actionStart(MainActivity.this,"","");
                break;
            case R.id.imagesHeader:
                ImageActivity.actionStart(MainActivity.this,"","");
                break;
            case R.id.osHeader:
                OSInfoActivity.actionStart(MainActivity.this,"","");
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
    /**
     * 加载数据
     */
    private void LoadData() {
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
                final String responseData = response.body().string(); //toString方法未重写，这里使用string()方法 //string不能调用两次 被调用一次就关闭了，这里调用两次会报异常
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
                            Toast.makeText(MainActivity.this, "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(MainActivity.this, "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 初始化floatButton，添加弹出菜单
     */
    private void initButton(){
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(MainActivity.this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("创建容器")
                .setResId(R.drawable.container)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("创建镜像")
                .setResId(R.drawable.image)
                .setIconNormalColor(0xff4e342e)
                .setIconPressedColor(0xff3e2723)
                .setLabelColor(Color.WHITE)
                .setLabelSizeSp(14)
                .setLabelBackgroundDrawable(RFABShape.generateCornerShapeDrawable(0xaa000000, RFABTextUtil.dip2px(MainActivity.this, 4)))
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("CRUN")
                .setResId(R.drawable.image)
                .setIconNormalColor(0xff4e342e)
                .setIconPressedColor(0xff3e2723)
                .setLabelColor(Color.WHITE)
                .setLabelSizeSp(14)
                .setLabelBackgroundDrawable(RFABShape.generateCornerShapeDrawable(0xaa000000, RFABTextUtil.dip2px(MainActivity.this, 4)))
                .setWrapper(1)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(RFABTextUtil.dip2px(MainActivity.this, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(RFABTextUtil.dip2px(MainActivity.this, 5))
        ;
        rfabHelper = new RapidFloatingActionHelper(
                MainActivity.this,
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();
    }
    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        rfabHelper.toggleContent();
        switch (position){
            case 0:
                CreateContainerActivity.actionStart(MainActivity.this,"","");
                break;
            case 1:
                CreateImageActivity.actionStart(MainActivity.this,"","");
                break;
            case 2:
                CrunActivity.actionStart(MainActivity.this,"","");
                break;
            default:
                break;
        }
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        rfabHelper.toggleContent();
        switch (position){
            case 0:
                CreateContainerActivity.actionStart(MainActivity.this,"","");
                break;
            case 1:
                CreateImageActivity.actionStart(MainActivity.this,"","");
                break;
            case 2:
                CrunActivity.actionStart(MainActivity.this,"","");
                break;
            default:
                break;
        }
    }
}
