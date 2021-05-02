package com.example.docker_android.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.example.docker_android.Activity.ContainerMigrateActivity;
import com.example.docker_android.Activity.CreateContainerActivity;
import com.example.docker_android.Activity.CreateImageActivity;
import com.example.docker_android.Activity.ImageActivity;
import com.example.docker_android.Activity.ImageMigrateActivity;
import com.example.docker_android.Activity.ImageSearchActivity;
import com.example.docker_android.Adapter.ImageAdapter;
import com.example.docker_android.Base.BaseLazyFragment;
import com.example.docker_android.Dialog.EditDialog;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.Dialog.PromptDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.Entity.Image.Image;
import com.example.docker_android.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ImageFragment_new extends BaseLazyFragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    List<Image> list = new ArrayList<>();
    private FloatingActionMenu menu;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab4;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_image_new;
    }

    @Override
    public void initFragments(View view) {
        recyclerView = view.findViewById(R.id.image_recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.image_srl);
        menu = view.findViewById(R.id.fab_menu);
        fab1 = view.findViewById(R.id.create_image);
        fab2 = view.findViewById(R.id.delete_image);
        fab3 = view.findViewById(R.id.migrate_image);
        fab4 = view.findViewById(R.id.search_images);
    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.create_image:
                    CreateImageActivity.actionStart(getActivity(),"","");
                    menu.close(true);
                    break;
                case R.id.delete_image:
                    AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(getActivity());
                    singleChoiceDialog.setIcon(R.drawable.docker);
                    singleChoiceDialog.setTitle("Prompt");
                    singleChoiceDialog.setMessage("Are you sure to delete the unused images?");
                    singleChoiceDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

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
                        }
                    });
                    singleChoiceDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    singleChoiceDialog.show();
                    menu.close(true);
                    break;
                case R.id.migrate_image:
                    ImageMigrateActivity.actionStart(getActivity(),"","");
                    menu.close(true);
                    break;
                case R.id.search_images:
                    EditDialog searchDialog = EditDialog.newInstance("Input search content",0,"");
                    searchDialog.setMargin(60)
                            .setClickOutCancel(true)
                            .show(getActivity().getSupportFragmentManager());
                    menu.close(true);
                    break;
            }
        }
    };
    @Override
    public void finishCreateView(Bundle state) {
        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);
        fab4.setOnClickListener(clickListener);
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
        list.clear();
        DockerService.getImages(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //停止刷新
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            JSONArray jsonArray = JSONArray.parseArray(responseData);
                            for (int i= 0;i < jsonArray.size();i++){
                                Image image = JSON.parseObject(jsonArray.getString(i), Image.class);
                                list.add(image);
                            }
                            ImageAdapter adapter = new ImageAdapter(list,getActivity(),ImageFragment_new.this);
                            recyclerView.setAdapter(adapter);
                            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
                            recyclerView.setLayoutManager(layoutManager);
                            LoadingDialog.hideDialogForLoading();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed, try again.", Toast.LENGTH_SHORT).show();
                            LoadingDialog.hideDialogForLoading();
                        }
                    }
                });
            }
        });
    }



}