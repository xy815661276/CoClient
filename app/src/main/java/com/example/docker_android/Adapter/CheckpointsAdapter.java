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
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * RecyclerView Adapter用于适配checkpoints
 */
public class CheckpointsAdapter extends RecyclerView.Adapter<CheckpointsAdapter.ViewHolder> {
    //收集容器对象
    private List<String> mClassList;
    private Context mContext;
    private String container_id;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View classView;
        //检查点名称
        TextView CheckpointName;

        private ViewHolder(View view) {//绑定控件
            super(view);
            classView = view;
            CheckpointName = view.findViewById(R.id.checkpoint_name);
        }
    }

    public CheckpointsAdapter(List<String> classList, Context context,String id) {
        mContext = context;
        mClassList = classList;
        container_id = id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_checkpoints, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.classView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                String checkpoint  = mClassList.get(position);
                showSingDialog(container_id,checkpoint);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = mClassList.get(position);
        holder.CheckpointName.setText(name);
    }

    @Override
    public int getItemCount() {
        return mClassList.size();
    }
    /**
     * 开启对话框，用于对检查点的操作
     */
    int choice;
    private void showSingDialog(String id,String name){
        final String[] items = {"Delete"};
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
                if(choice == 0) {
                    LoadingDialog.showDialogForLoading(mContext);
                    DockerTerminalService.DeleteCheckpoint(id,name);
                    LoadingDialog.hideDialogForLoading();
                    ((CheckpointActivity) mContext).loadData();
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
