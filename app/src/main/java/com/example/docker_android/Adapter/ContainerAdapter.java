package com.example.docker_android.Adapter;


import android.app.Activity;
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

import com.example.docker_android.Activity.ContainerDetailsActivity;
import com.example.docker_android.Activity.ContainerDetailsTextActivity;
import com.example.docker_android.Activity.ContainerRunningActivity;
import com.example.docker_android.Activity.LogsActivity;
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.EditDialog;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
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
public class ContainerAdapter extends RecyclerView.Adapter<ContainerAdapter.ViewHolder> {

    private List<Container> mClassList;//收集容器对象
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View classView;//cardView
        TextView ContainerName;//容器名
//        TextView ContainerID;//ID
//        TextView ContainerState;//状态
        TextView ContainerTime;//创建时间
//        TextView ContainerPorts;//端口映射
//        TextView ContainerLabel;//标签
        TextView ContainerImage;//镜像名

        private ViewHolder(View view) {//绑定控件
            super(view);
            classView = view;
            ContainerName = view.findViewById(R.id.containerTV);
//            ContainerID = view.findViewById(R.id.container_id);
//            ContainerState = view.findViewById(R.id.container_state);
            ContainerTime = view.findViewById(R.id.timeTV);
//            ContainerPorts = view.findViewById(R.id.container_ports);
//            ContainerLabel = view.findViewById(R.id.container_label);
            ContainerImage = view.findViewById(R.id.imageTV);
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
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Container container = mClassList.get(position);
                showSingDialog(container.getId(),container.getImage());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Container container = mClassList.get(position);
        holder.ContainerName.setText(container.getNames().get(0));
//        holder.ContainerID.setText(String.valueOf(container.getId()));
//        holder.ContainerState.setText(String.valueOf(container.getState()));
        holder.ContainerTime.setText(container.getStatus());
//        holder.ContainerPorts.setText(container.getPorts().toString());
//        holder.ContainerLabel.setText(container.getLabels().toString());
        holder.ContainerImage.setText(container.getImage());
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
    /**
     * 开启对话框，用于对容器的操作
     */
    int choice;
    private void showSingDialog(String id,String image){
        final String[] items = {"View info","Stop","Start","View logs","Checkpoint","View info(JSON)"};
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
                    ContainerDetailsActivity.actionStart(mContext,id,image);
                else if(choice == 3){
                    LogsActivity.actionStart(mContext,id,"");
                }
                else if(choice == 4){
                    EditDialog searchDialog = EditDialog.newInstance("input checkpoint name",1,id);
                    BaseActivity activity = (BaseActivity) mContext;
                    searchDialog.setMargin(60)
                            .setClickOutCancel(true)
                            .show(activity.getSupportFragmentManager());
                    ((ContainerRunningActivity)mContext).loadData();
                }
                else if(choice == 5){
                    ContainerDetailsTextActivity.actionStart(mContext,id,"");
                }
                else {
                    LoadingDialog.showDialogForLoading(mContext);
                    if (choice==1) action = "stop";
                    else if(choice==2) action = "start";
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
                                    ((ContainerRunningActivity)mContext).loadData();
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
