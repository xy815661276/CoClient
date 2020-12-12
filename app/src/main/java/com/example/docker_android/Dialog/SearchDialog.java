package com.example.docker_android.Dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.docker_android.Activity.ExecActivity;
import com.example.docker_android.Activity.ImageSearchActivity;
import com.example.docker_android.Base.BaseDialogFragment;
import com.example.docker_android.R;


/**
 * by xavier
 */
public class SearchDialog extends BaseDialogFragment {
    public static SearchDialog newInstance() {
        Bundle bundle = new Bundle();
        SearchDialog dialog = new SearchDialog();
        dialog.setArguments(bundle);
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
        final EditText search = holder.getView(R.id.search_content);
        holder.setOnClickListener(R.id.btn_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        holder.setOnClickListener(R.id.btn_ok, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search_content = search.getText().toString().trim();
                if ("".equals(search_content)) {
                    Toast.makeText(holder.getConvertView().getContext(), "Some input is empty",Toast.LENGTH_SHORT).show();
                } else {
                    ImageSearchActivity.actionStart(getActivity(),search_content,"");
                    dialog.dismiss();
                }
            }
        });
    }

}
