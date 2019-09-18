package com.cgx.library.widget.dialog;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.cgx.library.R;
import com.cgx.library.utils.StringUtils;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnBindViewListener;
import com.timmy.tdialog.listener.OnViewClickListener;


/**
 * 作者: 陈冠希
 * 日期: 2018/3/21
 * 描述:
 */

public class AlertDialog  {
   private TDialog mDialog;

    public void show(){
        mDialog.show();
    }

    public void dismiss(){
        mDialog.dismiss();
    }

   private  AlertDialog(TDialog.Builder builder){
       mDialog = builder.create();
   }

   public static class Builder{

       private FragmentManager mFragmentManager;
       private Activity mActivity;
       private String mTitle = "提示";
       private String mMessage;
       private boolean mCancelable;
       private boolean mCenterBtnVisiable = false;
       private String mPositiveBtnText = "确定";
       private String mCenterBtnText = "导航";
       private String mNegativeBtnText = "取消";
       private View.OnClickListener mOnPositiveListener;
       private View.OnClickListener mOnCenterListener;
       private View.OnClickListener mOnNegativeListener;

       public Builder(Activity activity, FragmentManager fm) {
           this.mActivity = activity;
           this.mFragmentManager = fm;
       }

       public Builder setTitle(String title){
           this.mTitle = title;
           return this;
       }

       public Builder setMessage(String message){
           this.mMessage = message;
           return this;
       }


       public Builder setCancelable(boolean isCancelable){
           this.mCancelable = isCancelable;
           return this;
       }

       public Builder setPositiveText(String text){
           this.mPositiveBtnText = text;
           return this;
       }

       public Builder setOnPositiveListener(View.OnClickListener listener){
           this.mOnPositiveListener = listener;
           return this;
       }

       public Builder setCenterText(String text){
           this.mCenterBtnText = text;
           return this;
       }

       public Builder setOnCenterListener(View.OnClickListener listener){
           this.mOnCenterListener = listener;
           return this;
       }

       public Builder setCenterVisiable(boolean visiable){
           this.mCenterBtnVisiable = visiable;
            return this;
       }

       public Builder setNegativeText(String text){
           this.mNegativeBtnText = text;
           return this;
       }

       public Builder setNegativeListener(View.OnClickListener listener){
           this.mOnNegativeListener = listener;
           return this;
       }

       public AlertDialog build(){
            TDialog.Builder builder = new TDialog
                    .Builder(mFragmentManager)
                    .setLayoutRes(R.layout.dialog_alert)
                    .setScreenWidthAspect(mActivity,0.7f)
                    .setGravity(Gravity.CENTER)
                    .setDimAmount(0.6f)
                    .setCancelableOutside(mCancelable)
                    .setOnBindViewListener(new OnBindViewListener() {
                        @Override
                        public void bindView(BindViewHolder viewHolder) {
                            if (StringUtils.isEmpty(mTitle)){
                                viewHolder.setVisibility(R.id.tv_dialog_title,View.GONE);
                            }else {
                                viewHolder.setVisibility(R.id.tv_dialog_title,View.VISIBLE);
                                viewHolder.setText(R.id.tv_dialog_title,mTitle);
                            }
                            viewHolder.setText(R.id.tv_dialog_message,mMessage);
                            viewHolder.setText(R.id.tv_dialog_cancel,mNegativeBtnText);

                            if(mCenterBtnVisiable == true){
                                viewHolder.setVisibility(R.id.tv_dialog_center,View.VISIBLE);
                            }else {
                                viewHolder.setVisibility(R.id.tv_dialog_center,View.GONE);
                            }

                            viewHolder.setText(R.id.tv_dialog_center,mCenterBtnText);

                            viewHolder.setText(R.id.tv_dialog_confirm,mPositiveBtnText);
                        }
                    })
                    .addOnClickListener(R.id.tv_dialog_confirm,R.id.tv_dialog_center,R.id.tv_dialog_cancel)
                    .setOnViewClickListener(new OnViewClickListener() {
                        @Override
                        public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                            int i = view.getId();
                            if (i == R.id.tv_dialog_confirm) {
                                if (mOnPositiveListener != null) {
                                    mOnPositiveListener.onClick(view);
                                }
                                tDialog.dismiss();
                            }

                            if (i == R.id.tv_dialog_center){
                                if (mOnCenterListener != null) {
                                    mOnCenterListener.onClick(view);
                                }
                                tDialog.dismiss();
                            }

                            if (i == R.id.tv_dialog_cancel){
                                if (mOnNegativeListener != null) {
                                    mOnNegativeListener.onClick(view);
                                }
                                tDialog.dismiss();
                            }
                        }
                    });
           return new AlertDialog(builder);
       }
   }
}
