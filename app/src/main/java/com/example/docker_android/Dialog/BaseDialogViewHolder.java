package com.example.docker_android.Dialog;

import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.IdRes;

/**
 * by xavier
 */
public class BaseDialogViewHolder {

    private SparseArray<View> views;
    private View convertView;

    private BaseDialogViewHolder(View view) {
        convertView = view;
        views = new SparseArray<>();
    }

    public static BaseDialogViewHolder create(View view) {
        return new BaseDialogViewHolder(view);
    }

    /**
     * 获取view
     * @param viewId
     * @param <T>指定泛型
     * @return
     */
    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置文本
     * @param viewId
     * @param text
     */
    public void setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
    }
    public void setHint(int viewId, String text) {
        EditText editText = getView(viewId);
        editText.setHint(text);
    }
    public void setTextColor(int viewId, int colorId) {
        TextView textView = getView(viewId);
        textView.setTextColor(colorId);
    }

    public void setBackgroundResource(int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
    }

    public void setBackgroundColor(int viewId, int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(colorId);
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
    }

    public View getConvertView() {
        return convertView;
    }
}
