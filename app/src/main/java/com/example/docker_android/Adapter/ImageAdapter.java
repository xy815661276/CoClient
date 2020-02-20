package com.example.docker_android.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.Entity.Image.Image;
import com.example.docker_android.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * RecyclerView Adapter用于适配飞机的列表，包含飞机图片，飞机名称以及简介
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<Image> mClassList;//收集容器对象
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View classView;//cardView
        TextView ImageName;//镜像名
//        TextView ImageID;//ID
        TextView ImageTag;//标签
//        TextView ImageCreateTime;//创建时间
        TextView ImageSize;//大小

        private ViewHolder(View view) {//绑定控件
            super(view);
            classView = view;
            ImageName = view.findViewById(R.id.imageTV);
//            ImageID = view.findViewById(R.id.image_id);
            ImageTag = view.findViewById(R.id.tagTV);
//            ImageCreateTime = view.findViewById(R.id.image_create_time);
            ImageSize = view.findViewById(R.id.sizeTV);
        }
    }

    public ImageAdapter(List<Image> classList, Context context) {
        mContext = context;
        mClassList = classList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image, parent, false);
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
        Image image = mClassList.get(position);
        String tag = image.getRepoTags().get(0);
        holder.ImageName.setText(tag.substring(0,tag.indexOf(":")));
//        holder.ImageID.setText(image.getId());
        holder.ImageTag.setText(tag.substring(tag.indexOf(":")+1));
//        holder.ImageCreateTime.setText(stampToDate(String.valueOf(image.getCreated())));
        holder.ImageSize.setText(getPrintSize(image.getSize()));
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

    private String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return size + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return size + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return (size / 100)+ "."
                    + (size % 100) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return (size / 100) + "."
                    + (size % 100) + "GB";
        }
    }
}
