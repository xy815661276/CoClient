package com.example.docker_android.Dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.docker_android.Activity.ExecActivity;
import com.example.docker_android.Base.BaseDialogFragment;
import com.example.docker_android.Fragment.ContainerFragment;
import com.example.docker_android.R;

import org.w3c.dom.Text;


/**
 * by xavier
 */
public class PromptDialog extends BaseDialogFragment {
    private OnItemClickListener mOnItemClickListener;

    public static PromptDialog newInstance() {
        Bundle bundle = new Bundle();
        PromptDialog dialog = new PromptDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public int setUpLayoutId() {
        return R.layout.dialog_prompt;
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
                    Intent resultIntent = new Intent();
                    onActivityResult(ContainerFragment.REQUEST, Activity.RESULT_OK,resultIntent);
                    if (mOnItemClickListener.onOKClick(holder.getConvertView(), resultIntent)) {
                        dialog.dismiss();
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

    public void setOnItemClickListener(PromptDialog.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}
