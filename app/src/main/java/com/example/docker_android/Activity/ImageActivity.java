package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Adapter.ContainerAdapter;
import com.example.docker_android.Adapter.ImageAdapter;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.Entity.Image.Image;
import com.example.docker_android.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class ImageActivity extends AppCompatActivity {
    @BindView(R.id.image_recycler_View)
    RecyclerView recyclerView;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    private List<Image> list = new ArrayList<>();
    /**
     * 活动跳转接口
     * @param context
     * @param data1
     * @param data2
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);  //使用BindView必须，不然会崩溃
        startAnim();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载动画太快，延时几秒
                LoadData();
            }
        },1000);
    }

    private void LoadData() {
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
                                    list.add(image);
                            }
                            ImageAdapter adapter = new ImageAdapter(list,ImageActivity.this);
                            recyclerView.setAdapter(adapter);
                            GridLayoutManager layoutManager = new GridLayoutManager(ImageActivity.this,1);
                            recyclerView.setLayoutManager(layoutManager);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ImageActivity.this, "刷新失败，请稍后再试或联系管理员", Toast.LENGTH_SHORT).show();
                        }
                        stopAnim();
                    }
                });
            }
        });
    }



    private void startAnim(){
        avi.show();
        //avi.smoothToShow();
    }

    private void stopAnim(){
        avi.hide();
        //avi.smoothToHide();
    }
}
