package com.example.docker_android.Adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docker_android.Activity.ContainerRunningActivity;
import com.example.docker_android.Activity.ImageActivity;
import com.example.docker_android.Activity.ImageDetailsActivity;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.Entity.Image.Image;
import com.example.docker_android.Fragment.ImageFragment_new;
import com.example.docker_android.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * RecyclerView Adapter 用于适配镜像列表
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final List<Image> mClassList;//收集容器对象
    private final Context mContext;
    private ImageFragment_new mFragment;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View classView;//cardView
        TextView ImageName;//镜像名
        TextView ImageTag;//标签
        TextView ImageSize;//大小
        LinearLayout about;
        CardView delete;
        ImageView menu;

        private ViewHolder(View view) {//绑定控件
            super(view);
            classView = view;
            ImageName = view.findViewById(R.id.imageTV);
            ImageTag = view.findViewById(R.id.tagTV);
            ImageSize = view.findViewById(R.id.sizeTV);
            about = view.findViewById(R.id.about);
            delete = view.findViewById(R.id.delete);
            menu = view.findViewById(R.id.menu);
        }
    }

    public ImageAdapter(List<Image> classList, Context context) {
        mContext = context;
        mClassList = classList;
    }

    public ImageAdapter(List<Image> classList, Context context,ImageFragment_new fragment) {
        mContext = context;
        mClassList = classList;
        mFragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image_new, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.classView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Image image = mClassList.get(position);
                ImageDetailsActivity.actionStart(mContext,image.getId(),"");
            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Image image = mClassList.get(position);
                showSingDialog(image.getId());
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Image image = mClassList.get(position);
                LoadingDialog.showDialogForLoading(mContext);
                deleteDialog(image.getId());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image image = mClassList.get(position);
        String tag = image.getRepoTags().get(0);
        holder.ImageName.setText(tag.substring(0,tag.indexOf(":")));
        String time = "Created: " + stampToDate(image.getCreated());
        holder.ImageTag.setText(time);
        holder.ImageSize.setText(getPrintSize(image.getSize()));
    }

    @Override
    public int getItemCount() {
        return mClassList.size();
    }
    /*

     * 将UNIX时间戳转换为时间

     */

    private static String stampToDate(long l){

        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = l*1000;//毫秒转成秒
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


    /**
     * 开启对话框，用于对容器的操作
     */
    int choice;
    private void showSingDialog(String id){
        final String[] items = {"Delete","View Info"};
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
                if (choice == 0) {
                    LoadingDialog.showDialogForLoading(mContext);
                    DockerService.DeleteImage(id,new okhttp3.Callback(){
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            ((AppCompatActivity)mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(response.code() == 200){
                                        Toast.makeText(mContext,"successfully deleted",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(mContext,"Delete failed, please try again later",Toast.LENGTH_SHORT).show();
                                    }
                                    ((ImageActivity)mContext).loadData();
                                    LoadingDialog.hideDialogForLoading();
                                }
                            });
                        }
                    });
                }
                else if(choice == 1){
                    ImageDetailsActivity.actionStart(mContext,id,"");
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

    private void deleteDialog(String id){
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(mContext);
        singleChoiceDialog.setIcon(R.drawable.docker);
        singleChoiceDialog.setTitle("Prompt");
        singleChoiceDialog.setMessage("You sure you want to delete it?");
        singleChoiceDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                LoadingDialog.showDialogForLoading(mContext);

                DockerService.DeleteImage(id,new okhttp3.Callback(){
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ((AppCompatActivity)mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(response.code() == 200){
                                    Toast.makeText(mContext,"successfully deleted",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(mContext,"Delete failed, please try again later",Toast.LENGTH_SHORT).show();
                                }
                                mFragment.loadData();
                                LoadingDialog.hideDialogForLoading();
                            }
                        });
                    }
                });

            }
        });
        singleChoiceDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LoadingDialog.hideDialogForLoading();
            }
        });
        singleChoiceDialog.show();
    }
}
