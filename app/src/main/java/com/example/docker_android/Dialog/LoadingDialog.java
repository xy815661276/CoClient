package com.example.docker_android.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.docker_android.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * 加载中Dialog
 *
 */
public class LoadingDialog{
//
//    private static AVLoadingIndicatorView avi;
//    private static Context mContext;
//    public static LoadingDialog getInstance(Context context) {
//            LoadingDialog loadingDialog;
//            loadingDialog = new LoadingDialog(context, R.style.TransparentDialog); //设置AlertDialog背景透明
//            loadingDialog.setCancelable(false);
//            loadingDialog.setCanceledOnTouchOutside(false);
//            loadingDialog.mContext = context;
//        return loadingDialog;
//    }
//    private LoadingDialog(Context context, int themeResId) {
//        super(context,themeResId);
//    }
//
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        this.setContentView(R.layout.dialog_loading);
////        avi = this.findViewById(R.id.avi);
////    }
////    @Override
//    protected void onStart() {
//        super.onStart();
//        this.setContentView(R.layout.dialog_loading);
//        avi = this.findViewById(R.id.avi);
//    }
//    @Override
//    public void show() {
//        //在dialog show之前判断一下
//        if (!getActivity(LoadingDialog.mContext).isFinishing()) {
//            super.show();
//        }
//        avi.show();
//    }
//
//    @Override
//    public void dismiss() {
//        super.dismiss();
//        avi.hide();
//    }
//
//    private Activity getActivity(Context context) {
//        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
//            context = ((ContextWrapper) context).getBaseContext();
//        }
//        if (context instanceof Activity) {
//            return (Activity) context;
//        }else
//            return null;
//    }
    private static Dialog mLoadingDialog;
    /**
     * 显示加载对话框
     *
     * @param context    上下文
     */
    public static void showDialogForLoading(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        TextView loadingText = view.findViewById(R.id.id_tv_loading_dialog_text);
        AVLoadingIndicatorView avLoadingIndicatorView = view.findViewById(R.id.AVLoadingIndicatorView);
        loadingText.setText("加载中,请稍后...");
        mLoadingDialog = new Dialog(context, R.style.loading_dialog_style);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
        avLoadingIndicatorView.smoothToShow();
        mLoadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mLoadingDialog.hide();
                    return true;
                }
                return false;
            }
        });
    }
    public static void hideDialogForLoading(){
        mLoadingDialog.dismiss();
    }
}
