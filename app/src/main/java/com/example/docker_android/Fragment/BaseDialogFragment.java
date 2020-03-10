package com.example.docker_android.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.FloatRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.docker_android.Dialog.BaseDialogViewHolder;
import com.example.docker_android.R;


/**
 * by Pcd at 19/04/23 22:28
 */
public abstract class BaseDialogFragment extends DialogFragment {


    protected @LayoutRes
    int mlayoutResId;
    private float mDimAmount = 0.5f; //背景昏暗度
    private boolean mShowAtBottom; //是否显示在底部
    private int mMargin = 0; //左右边距
    private int mAnimStyle = 0; //进入退出动画
    private boolean mClickOutCancel = true; //点击外部取消
    private Context mContext;
    private int mWidth;
    private int mHeight;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * 设定dialog布局
     * @return
     */
    public abstract int setUpLayoutId();

    /**
     * 操作dialog布局
     * @param holder
     * @param dialog
     */
    public abstract void convertView(BaseDialogViewHolder holder, BaseDialogFragment dialog);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.base_dialog); //theme为0可正常显示，但切换任务后返回会出错
        mlayoutResId = setUpLayoutId();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(mlayoutResId, container, false);
        convertView(BaseDialogViewHolder.create(view), this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    /**
     * 根据调用时的参数汇总，进行参数设定
     * 根据选用参数设定dialog窗口参数，其中宽高设定需使用px，因此对输入的dp进行转换
     */
    private void initParams() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = mDimAmount;

            if (mShowAtBottom) {
                params.gravity = Gravity.BOTTOM;
            }

            //设定宽度，输入0时默认根据左右间距判断。
            if (mWidth == 0) {
                params.width = getScreenWidth(getContext()) - 2 * dp2px(getContext(), mMargin);
            } else {
                params.width = dp2px(getContext(), mWidth);
            }

            //设定高度，未输入时根据内容自行适应
            if (mHeight == 0) {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                params.height = dp2px(getContext(), mHeight);
            }

            if (mAnimStyle != 0) {
                window.setWindowAnimations(mAnimStyle);
            }

            window.setAttributes(params);
        }
        setCancelable(mClickOutCancel);
    }

    /**
     * @FloatRange(from = 0, to = 1) 注解更改默认参数
     * @param dimAmount 设置背景昏暗度
     * @return this-->BaseDialogFragment利于链式设定，减少调用时的代码量
     */
    public BaseDialogFragment setDimAmount(@FloatRange(from = 0, to = 1) float dimAmount) {
        mDimAmount = dimAmount;
        return this;
    }

    /**
     * @param showBottom
     * @return 设置是否显示底部
     */
    public BaseDialogFragment setShowBottom(boolean showBottom) {
        mShowAtBottom = showBottom;
        return this;
    }

    /**
     * @param width
     * @param height
     * @return 设定宽高
     */
    public BaseDialogFragment setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
        return this;
    }

    /**
     * @param margin
     * @return 设定左右间距
     */
    public BaseDialogFragment setMargin(int margin) {
        mMargin = margin;
        return this;
    }

    /**
     * @StyleRes int animStyle 使用系统注解调用内含动画
     * @param animStyle
     * @return
     */
    public BaseDialogFragment setAnimStyle(@StyleRes int animStyle) {
        mAnimStyle = animStyle;
        return this;
    }

    /**
     * @param outCancel
     * @return 点击外部取消
     */
    public BaseDialogFragment setClickOutCancel(boolean outCancel) {
        mClickOutCancel = outCancel;
        return this;
    }

    /**
     * @param manager
     * @return 默认当前显示
     */
    public BaseDialogFragment show(FragmentManager manager) {
        super.show(manager, String.valueOf(System.currentTimeMillis()));
        return this;
    }

    /**
     * 获取屏幕参数和实用转换
     * @param
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics(); //获取屏幕信息
        return displayMetrics.widthPixels;
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
        // 在320x480分辨率，像素密度为160,1dp=1px ；density = dpi/160，即为和标准的比例；px = dp * (dpi/160),+0.5是为了向上取整，保证铺满
    }

}
