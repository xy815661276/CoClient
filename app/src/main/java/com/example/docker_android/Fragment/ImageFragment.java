package com.example.docker_android.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Activity.ContainerRunningActivity;
import com.example.docker_android.Activity.ContainerStoppedActivity;
import com.example.docker_android.Activity.ImageActivity;
import com.example.docker_android.Activity.OSInfoActivity;
import com.example.docker_android.Base.BaseLazyFragment;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.Entity.Image.Image;
import com.example.docker_android.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ImageFragment extends BaseLazyFragment {
    private RelativeLayout image;
    private TextView images;
    private SwipeRefreshLayout swipeRefreshLayout;


    private final List<Container> list_run = new ArrayList<>();
    public static List<Container> list_stop = new ArrayList<>();
    private final List<Image> list_image = new ArrayList<>();
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_image;
    }

    @Override
    public void initFragments(View view) {
        image = view.findViewById(R.id.imagesHeader);
        images = view.findViewById(R.id.images);
        swipeRefreshLayout = view.findViewById(R.id.image_srl);
    }

    @Override
    public void finishCreateView(Bundle state) {
        //监听跳转
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageActivity.actionStart(getActivity(),"","");
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
    @Override
    public void loadData() {
        //先清除原来的数据
        list_image.clear();

        DockerService.getImages(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string(); //toString方法未重写，这里使用string()方法 //string不能调用两次 被调用一次就关闭了，这里调用两次会报异常
                Log.d("Component", "onResponse: " + responseData);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
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
                            Toast.makeText(getActivity(), "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}