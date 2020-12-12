package com.example.docker_android.Adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docker_android.Activity.ImageActivity;
import com.example.docker_android.Activity.ImageDetailsActivity;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Image.Image;
import com.example.docker_android.Entity.Image.ImageSearch;
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
public class ImageSearchAdapter extends RecyclerView.Adapter<ImageSearchAdapter.ViewHolder> {


    private final List<ImageSearch> mClassList;
    private final Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View classView;//cardView
        TextView ImageName;
        TextView is_official;
        TextView is_automated;
        TextView description;
        TextView stars;

        //绑定控件
        private ViewHolder(View view) {
            super(view);
            classView = view;
            ImageName = view.findViewById(R.id.image_search_name);
            description = view.findViewById(R.id.imageDescription);
            is_official = view.findViewById(R.id.is_official);
            is_automated = view.findViewById(R.id.is_automated);
            stars = view.findViewById(R.id.stars);
        }
    }

    public ImageSearchAdapter(List<ImageSearch> classList, Context context) {
        mContext = context;
        mClassList = classList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_searched_image, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.classView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ImageSearch image_search = mClassList.get(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageSearch image_search = mClassList.get(position);
        holder.ImageName.setText(image_search.getName());
        String description = "\u3000\u3000" + image_search.getDescription();
        holder.description.setText(description);
        String is_official = "is_official: " + image_search.is_official();
        holder.is_official.setText(is_official);
        String is_automated = "is_automated: " + image_search.is_automated();
        holder.is_automated.setText(is_automated);
        holder.stars.setText(NumChange(image_search.getStar_count()));
    }

    @Override
    public int getItemCount() {
        return mClassList.size();
    }

    /*
     * 将数字转化，大于1000的用k表示，大于100k的用100k+表示
     */
    private static String NumChange(int num){
        String res = "";
        if(num >= 0 && num < 1000){
            res = String.valueOf(num);
        }
        else if(num >= 1000 && num <= 100000){
            res = num/1000 + "k";
        }
        else {
            res = "100k+";
        }
        return res;
    }
}
