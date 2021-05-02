package com.example.docker_android.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.example.docker_android.Activity.CreateImageActivity;
import com.example.docker_android.Activity.ImageActivity;
import com.example.docker_android.Activity.ImageMigrateActivity;
import com.example.docker_android.Base.BaseLazyFragment;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.Dialog.PromptDialog;
import com.example.docker_android.Dialog.EditDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.Entity.Image.Image;
import com.example.docker_android.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ImageFragment extends BaseLazyFragment {
    private RelativeLayout image;
    private RelativeLayout create_image;
    private RelativeLayout delete_unused_image;
    private RelativeLayout image_search;
    private RelativeLayout image_migrate;
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
        create_image = view.findViewById(R.id.create_image);
        delete_unused_image = view.findViewById(R.id.delete_image);
        image_search = view.findViewById(R.id.image_search_Header);
        image_migrate = view.findViewById(R.id.image_migrateHeader);
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
        create_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateImageActivity.actionStart(getActivity(),"","");
            }
        });
        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDialog searchDialog = EditDialog.newInstance("input search content",0,"");
                searchDialog.setMargin(60)
                        .setClickOutCancel(true)
                        .show(getActivity().getSupportFragmentManager());
            }
        });
        delete_unused_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final PromptDialog promptDialog = PromptDialog.newInstance("Are you sure to delete the unused images?");
                promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                    @Override
                    public boolean onOKClick(View view, Intent data) {
                        LoadingDialog.showDialogForLoading(getActivity());
                        DockerService.DeleteUnusedImage(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                LoadingDialog.hideDialogForLoading();
                                final String responseData = response.body().string();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if(response.code() == 200){
                                                Toast.makeText(getActivity(),"Successfully deleted",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getActivity(),"Delete failed,try again later",Toast.LENGTH_SHORT).show();
                                            }
                                            loadData();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getActivity(), "Delete failed,try again later", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                        return true;
                    }
                });
                promptDialog.setMargin(30)
                        .setClickOutCancel(true)
                        .show(getActivity().getSupportFragmentManager());
            }
        });
        image_migrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageMigrateActivity.actionStart(getActivity(),"","");
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            JSONArray jsonArray = JSONArray.parseArray(responseData);
                            for (int i= 0;i < jsonArray.size();i++){
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