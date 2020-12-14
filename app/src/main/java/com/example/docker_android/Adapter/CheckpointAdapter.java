package com.example.docker_android.Adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docker_android.Activity.CheckpointActivity;
import com.example.docker_android.Activity.ContainerDetailsActivity;
import com.example.docker_android.Activity.ContainerDetailsTextActivity;
import com.example.docker_android.Activity.ContainerRunningActivity;
import com.example.docker_android.Activity.LogsActivity;
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.EditDialog;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.DockerAPI.DockerTerminalService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * RecyclerView Adapter用于适配飞机的列表，包含飞机图片，飞机名称以及简介
 */
public class CheckpointAdapter extends RecyclerView.Adapter<CheckpointAdapter.ViewHolder> {
    //收集容器对象
    private List<Container> mClassList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View classView;
        //容器名
        TextView ContainerName;
        //创建时间
        TextView CheckpointNum;

        private ViewHolder(View view) {//绑定控件
            super(view);
            classView = view;
            ContainerName = view.findViewById(R.id.checkpoint_container_name);
            CheckpointNum = view.findViewById(R.id.checkpoint_num);
        }
    }

    public CheckpointAdapter(List<Container> classList, Context context) {
        mContext = context;
        mClassList = classList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_checkpoint, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.classView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Container container = mClassList.get(position);
                CheckpointActivity.actionStart(mContext,container.getId(),"");
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Container container = mClassList.get(position);
        holder.ContainerName.setText(container.getNames().get(0));
        List<String> checkpoints = DockerTerminalService.GetCheckpoint(container.getId());
        holder.CheckpointNum.setText(String.valueOf(checkpoints.size()));
    }

    @Override
    public int getItemCount() {
        return mClassList.size();
    }
}
