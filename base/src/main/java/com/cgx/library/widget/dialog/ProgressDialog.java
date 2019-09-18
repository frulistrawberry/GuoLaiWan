package com.cgx.library.widget.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgx.library.R;
import com.cgx.library.utils.ScreenUtils;
import com.cgx.library.utils.SizeUtils;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnBindViewListener;

/**
 * author：   tc
 * date：     2015/10/5 & 14:03
 * version    1.0
 * description 加载中的进度条对话框
 * modify by  ljy
 */
public class ProgressDialog  {
    private TDialog mDialog;

    public void show(){
        mDialog.show();
    }

    public void dismiss(){
        mDialog.dismiss();
    }

    private  ProgressDialog(TDialog.Builder builder){
        mDialog = builder.create();
    }
    public static class Builder{
        private Activity mActivity;
        private FragmentManager mFragmentManager;
        private String mMessage = "加载中...";

        public Builder(Activity activity, FragmentManager fm) {
            this.mActivity = activity;
            this.mFragmentManager = fm;
        }

        public Builder setMessage(String message){
            this.mMessage = message;
            return this;
        }

        public ProgressDialog build(){
            TDialog.Builder builder = new TDialog.Builder(mFragmentManager)
                    .setLayoutRes(R.layout.dialog_progress)
                    .setOnBindViewListener(new OnBindViewListener() {
                        @Override
                        public void bindView(BindViewHolder viewHolder) {
                            viewHolder.setText(R.id.tv_dialog_message,mMessage);
                        }
                    })
                    .setGravity(Gravity.CENTER)
                    .setCancelableOutside(false)
                    .setWidth(ScreenUtils.getScreenWidth(mActivity)/3)
                    .setHeight(ScreenUtils.getScreenWidth(mActivity)/3)
                    .setDimAmount(0.6f).setCancelable(false);
            return new ProgressDialog(builder);
        }
    }
}
