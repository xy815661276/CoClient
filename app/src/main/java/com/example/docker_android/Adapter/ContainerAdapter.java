package com.example.docker_android.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * RecyclerView Adapter用于适配飞机的列表，包含飞机图片，飞机名称以及简介
 */
public class ContainerAdapter extends RecyclerView.Adapter<ContainerAdapter.ViewHolder> {

    private List<Container> mClassList;//收集容器对象
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View classView;//cardView
        TextView ContainerName;//容器名
        TextView ContainerID;//ID
        TextView ContainerState;//状态
        TextView ContainerCreateTime;//创建时间
        TextView ContainerPorts;//端口映射
        TextView ContainerLabel;//标签

        private ViewHolder(View view) {//绑定控件
            super(view);
            classView = view;
            ContainerName = view.findViewById(R.id.container_name);
            ContainerID = view.findViewById(R.id.container_id);
            ContainerState = view.findViewById(R.id.container_state);
            ContainerCreateTime = view.findViewById(R.id.container_create_time);
            ContainerPorts = view.findViewById(R.id.container_ports);
            ContainerLabel = view.findViewById(R.id.container_label);
        }
    }

    public ContainerAdapter(List<Container> classList, Context context) {
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
        holder.ContainerName.setText(container.getNames().toString());
        holder.ContainerID.setText(String.valueOf(container.getId()));
        holder.ContainerState.setText(String.valueOf(container.getState()));
        holder.ContainerCreateTime.setText(stampToDate(String.valueOf(container.getCreated())));
        holder.ContainerPorts.setText(container.getPorts().toString());
        holder.ContainerLabel.setText(container.getLabels().toString());
    }

    @Override
    public int getItemCount() {
        return mClassList.size();
    }
    /*

     * 将UNIX时间戳转换为时间

     */

    private static String stampToDate(String s){

        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = Long.valueOf(s)*1000;//毫秒转成秒
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
