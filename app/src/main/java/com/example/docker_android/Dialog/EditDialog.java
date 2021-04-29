package com.example.docker_android.Dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.docker_android.Activity.ContainerRunningActivity;
import com.example.docker_android.Activity.ContainerStoppedActivity;
import com.example.docker_android.Activity.ImageSearchActivity;
import com.example.docker_android.Base.BaseDialogFragment;
import com.example.docker_android.DockerAPI.DockerTerminalService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.R;

import java.util.List;


/**
 * by xavier
 */
public class EditDialog extends BaseDialogFragment {
    private static String edit_hint = "input search content";
    private static int edit_action = 0;
    private static String edit_data = "";
    public static EditDialog newInstance(String hint, int action,String data) {
        Bundle bundle = new Bundle();
        EditDialog dialog = new EditDialog();
        dialog.setArguments(bundle);
        edit_hint = hint;
        edit_action = action;
        edit_data = data;
        return dialog;
    }
    @Override
    public int setUpLayoutId() {
        return R.layout.dialog_search;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
    }

    @Override
    public void convertView(final BaseDialogViewHolder holder, final BaseDialogFragment dialog) {
        final EditText edit = holder.getView(R.id.edit_content);
        edit.setHint(edit_hint);
        holder.setOnClickListener(R.id.btn_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        holder.setOnClickListener(R.id.btn_ok, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edit_content = edit.getText().toString().trim();
                if ("".equals(edit_content)) {
                    Toast.makeText(holder.getConvertView().getContext(), "Some input is empty",Toast.LENGTH_SHORT).show();
                } else {
                    if(edit_action == 0){
                        ImageSearchActivity.actionStart(getActivity(),edit_content,"");
                    }
                    else if(edit_action == 1){
                        List<String> list = DockerTerminalService.GetCheckpoint(edit_data);
                        if(list.contains(edit_content)){
                            Toast.makeText(holder.getConvertView().getContext(), "Checkpoint already exists",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            DockerTerminalService.DockerCheckpoint(edit_data,edit_content);
                            Toast.makeText(holder.getConvertView().getContext(), "Checkpoint Successfully",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(edit_action == 2){
                        LoadingDialog.showDialogForLoading(getActivity());
                        long start = System.currentTimeMillis();
                        List<String> list = DockerTerminalService.GetCheckpoint(edit_data);
                        if(!list.contains(edit_content)){
                            Toast.makeText(holder.getConvertView().getContext(), "Checkpoint does not exists",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            DockerTerminalService.StartCheckpoint(edit_data,edit_content);
                            Toast.makeText(holder.getConvertView().getContext(), "Checkpoint Start Successfully",Toast.LENGTH_SHORT).show();
                            long end = System.currentTimeMillis();
                            Log.d("Start Time:", String.valueOf(end - start));
                            ContainerStoppedActivity containerStoppedActivity = (ContainerStoppedActivity) getActivity();
                            containerStoppedActivity.loadData();
                        }
                    }
                    LoadingDialog.hideDialogForLoading();
                    dialog.dismiss();
                }
            }
        });
    }

}
