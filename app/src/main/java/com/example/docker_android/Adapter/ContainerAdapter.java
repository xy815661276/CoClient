package com.example.docker_android.Adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docker_android.Activity.CheckpointActivity;
import com.example.docker_android.Activity.ContainerDetailsActivity;
import com.example.docker_android.Activity.ContainerDetailsTextActivity;
import com.example.docker_android.Activity.ContainerRunningActivity;
import com.example.docker_android.Activity.ContainerStoppedActivity;
import com.example.docker_android.Activity.ExecActivity;
import com.example.docker_android.Activity.LogsActivity;
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.EditDialog;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.Fragment.ContainerFragment_new;
import com.example.docker_android.R;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * RecyclerView Adapter 用于适配停止的容器列表
 */
public class ContainerAdapter extends RecyclerView.Adapter<ContainerAdapter.ViewHolder> {

    private List<Container> mClassList;//收集容器对象
    private Context mContext;
    private ContainerFragment_new mFragment;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View classView;//cardView
        TextView ContainerName;//容器名
        TextView ContainerTime;//创建时间
        TextView ContainerImage;//镜像名
        ImageView menu;//菜单
        CardView delete;//删除按钮
        TextView dot;//圆点
        LinearLayout about;//关于

        private ViewHolder(View view) {//绑定控件
            super(view);
            classView = view;
            ContainerName = view.findViewById(R.id.containerTV);
            ContainerTime = view.findViewById(R.id.timeTV);
            ContainerImage = view.findViewById(R.id.imageTV);
            menu = view.findViewById(R.id.menu);
            delete = view.findViewById(R.id.delete);
            dot = view.findViewById(R.id.dot);
            about = view.findViewById(R.id.about);
        }
    }

    public ContainerAdapter(List<Container> classList, Context context, ContainerFragment_new fragment) {
        mContext = context;
        mClassList = classList;
        mFragment = fragment;
    }

    public ContainerAdapter(List<Container> classList, Context context) {
        mContext = context;
        mClassList = classList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_container_new, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.classView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Container container = mClassList.get(position);
                if(container.getState().equals("exited") || container.getState().equals("created")){
                    showSingStopDialog(container.getId());
                }
                else {
                    showSingStartDialog(container.getId(),container.getImage());
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Container container = mClassList.get(position);
                deleteDialog(container.getId());
            }
        });
        holder.about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Container container = mClassList.get(position);
                ContainerDetailsTextActivity.actionStart(mContext,container.getId(),"");
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
        GradientDrawable mm= (GradientDrawable)holder.dot.getBackground();
        if(container.getState().equals("exited") || container.getState().equals("created") ){
            mm.setColor(Color.RED);
        }
        else if(container.getState().equals("paused")){
            mm.setColor(Color.YELLOW);
        }
        else {
            mm.setColor(Color.GREEN);
        }
    }
    @Override
    public int getItemCount() {
        return mClassList.size();
    }

    /**
     * 开启对话框，用于对容器的操作
     */
    int choice;
    private void showSingStopDialog(String id){
        final String[] items = {"Start","Delete","Checkpoint Start","View logs","View checkpoints"};
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
                if (choice == 0){
                    LoadingDialog.showDialogForLoading(mContext);
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
                                    LoadingDialog.hideDialogForLoading();
                                    ((ContainerStoppedActivity)mContext).loadData();
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
                    LoadingDialog.showDialogForLoading(mContext);
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
                                    LoadingDialog.hideDialogForLoading();
                                    ((ContainerStoppedActivity)mContext).loadData();
                                }
                            });
                        }
                    });
                }
                else if(choice == 2){
                    CheckpointActivity.actionStart(mContext,id,"");
                    ((ContainerStoppedActivity)mContext).loadData();
                }
                else if(choice == 3){
                    LogsActivity.actionStart(mContext,id,"");
                }
                else if(choice == 4){
                    CheckpointActivity.actionStart(mContext,id,"");
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


    private void showSingStartDialog(String id,String image){
        final String[] items = {"Command","Stop","View logs","Checkpoint","View info(JSON)","View checkpoints"};
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
                String action = "";
                if(choice == 0)
                    ExecActivity.actionStart(mContext,id,image);
                else if(choice == 2){
                    LogsActivity.actionStart(mContext,id,"");
                }
                else if(choice == 3){
                    EditDialog searchDialog = EditDialog.newInstance("input checkpoint name",1,id);
                    BaseActivity activity = (BaseActivity) mContext;
                    searchDialog.setMargin(60)
                            .setClickOutCancel(true)
                            .show(activity.getSupportFragmentManager());
                    ((ContainerStoppedActivity)mContext).loadData();
                }
                else if(choice == 4){
                    ContainerDetailsTextActivity.actionStart(mContext,id,"");
                }
                else if(choice == 5){
                    CheckpointActivity.actionStart(mContext,id,"");
                }
                else if (choice==1){
                    LoadingDialog.showDialogForLoading(mContext);
                    action = "stop";
                    DockerService.ContainerAction(id,action,new Callback(){
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            ((AppCompatActivity)mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(response.code() == 204){
                                        Toast.makeText(mContext,"Successful operation",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(mContext,"Operation failed, please try again later",Toast.LENGTH_SHORT).show();
                                    }
                                    LoadingDialog.hideDialogForLoading();
                                    ((ContainerStoppedActivity)mContext).loadData();
                                }
                            });

                        }
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
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

    private void deleteDialog(String id){
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(mContext);
        singleChoiceDialog.setIcon(R.drawable.docker);
        singleChoiceDialog.setTitle("Prompt");
        singleChoiceDialog.setMessage("You sure you want to delete it?");
        singleChoiceDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                LoadingDialog.showDialogForLoading(mContext);
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
                                LoadingDialog.hideDialogForLoading();
                                ((ContainerStoppedActivity)mContext).loadData();
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
    }
}
