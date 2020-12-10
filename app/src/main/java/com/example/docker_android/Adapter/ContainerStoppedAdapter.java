package com.example.docker_android.Adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Activity.ContainerDetailsActivity;
import com.example.docker_android.Activity.ContainerStoppedActivity;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * RecyclerView Adapter
 */
public class ContainerStoppedAdapter extends RecyclerView.Adapter<ContainerStoppedAdapter.ViewHolder> {

    private List<Container> mClassList;//收集容器对象
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View classView;//cardView
        TextView ContainerName;//容器名
        TextView ContainerTime;//创建时间
        TextView ContainerImage;//镜像名

        private ViewHolder(View view) {//绑定控件
            super(view);
            classView = view;
            ContainerName = view.findViewById(R.id.containerTV);
            ContainerTime = view.findViewById(R.id.timeTV);
            ContainerImage = view.findViewById(R.id.imageTV);
        }
    }

    public ContainerStoppedAdapter(List<Container> classList, Context context) {
        mContext = context;
        mClassList = classList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_container, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.classView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Container container = mClassList.get(position);
                showSingDialog(container.getId());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Container container = mClassList.get(position);
        holder.ContainerName.setText(container.getNames().get(0));
        holder.ContainerTime.setText(container.getStatus());
        holder.ContainerImage.setText(container.getImage());
    }
    @Override
    public int getItemCount() {
        return mClassList.size();
    }

    /**
     * 开启对话框，用于对容器的操作
     */
    int choice;
    private void showSingDialog(String id){
        final String[] items = {"Start","Delete"};
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(mContext);
        singleChoiceDialog.setIcon(R.drawable.docker);
        singleChoiceDialog.setTitle("Please Select");
        //第二个参数是默认的选项
        singleChoiceDialog.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choice= which;
            }
        });
        singleChoiceDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoadingDialog.showDialogForLoading(mContext);
                if (choice == 0){
                    DockerService.ContainerAction(id,"start",new okhttp3.Callback(){
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.d("start container",response.body().string());
                            ((AppCompatActivity)mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(response.code() == 204)
                                        Toast.makeText(mContext,"Start successfully",Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(mContext,"Start failed, please try again later",Toast.LENGTH_SHORT).show();
                                    ((ContainerStoppedActivity)mContext).loadData();
                                    LoadingDialog.hideDialogForLoading();
                                }
                            });
                        }
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                else if(choice == 1){
                    DockerService.DeleteContainer(id,new okhttp3.Callback(){
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            ((AppCompatActivity)mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(response.code() == 204)
                                        Toast.makeText(mContext,"Successfully deleted",Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(mContext,"Delete failed, please try again later",Toast.LENGTH_SHORT).show();
                                    ((ContainerStoppedActivity)mContext).loadData();
                                    LoadingDialog.hideDialogForLoading();
                                }
                            });
                        }
                    });
                }
            }
        });
        singleChoiceDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        singleChoiceDialog.show();
    }
}
