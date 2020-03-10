package com.example.docker_android.Dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.docker_android.Activity.ExecActivity;
import com.example.docker_android.Fragment.BaseDialogFragment;
import com.example.docker_android.R;


/**
 * by Pcd at 19/04/23 22:33
 */
public class ExecDialog extends BaseDialogFragment {
    private OnItemClickListener mOnItemClickListener;
    public static ExecDialog newInstance() {
        Bundle bundle = new Bundle();
        ExecDialog dialog = new ExecDialog();
        dialog.setArguments(bundle);
        return dialog;
    }
    @Override
    public int setUpLayoutId() {
        return R.layout.dialog_exec;
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
        final EditText cmd = holder.getView(R.id.exec_cmd);
        final EditText working_dir = holder.getView(R.id.exec_working_dir);
        holder.setOnClickListener(R.id.btn_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        holder.setOnClickListener(R.id.btn_ok, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    String Command = cmd.getText().toString().trim();
                    String Working_dir = working_dir.getText().toString().trim();
                    if ("".equals(Command)||"".equals(Working_dir)) {
                        Toast.makeText(holder.getConvertView().getContext(), "有输入为空，请重新输入",Toast.LENGTH_SHORT).show();
                    } else {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("Command", Command);
                            resultIntent.putExtra("Working_dir", Working_dir);
                            onActivityResult(ExecActivity.REQUEST, Activity.RESULT_OK,resultIntent);
                            if (mOnItemClickListener.onOKClick(holder.getConvertView(), resultIntent)) {
                                dialog.dismiss();
                            }
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
    }
    public interface OnItemClickListener {
            boolean onOKClick(View view, Intent data);
    }

    public void setOnItemClickListener(ExecDialog.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}
