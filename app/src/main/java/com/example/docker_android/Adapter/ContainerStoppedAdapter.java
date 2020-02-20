package com.example.docker_android.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.docker_android.Activity.ContainerDetailsActivity;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * RecyclerView Adapter用于适配飞机的列表，包含飞机图片，飞机名称以及简介
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
}
