package com.cgx.library.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cgx.library.R;
import com.cgx.library.utils.ScreenUtils;



/**
 * 作者: chengx
 * 日期: 2016/9/29.
 * 描述: 仿IOS ProgressDialog
 */
public class NomalProgressDialog {

    private static Context context;
    private TextView mTextTv;
    private View dialogView;
    private Dialog dialog;
    private Builder builder;

    public NomalProgressDialog(Builder builder) {
        this.builder = builder;
        dialog = new Dialog(context, R.style.NormalProgressDialogStyle);
        dialogView = View.inflate(context, R.layout.dialog_progress,null);
        mTextTv = dialogView.findViewById(R.id.tv_message);
        dialogView.setMinimumHeight((int) (ScreenUtils.getScreenWidth(context) * builder.getHeight()));
        dialogView.setMinimumWidth((int) (ScreenUtils.getScreenWidth(context) * builder.getWidth()));
        dialog.setContentView(dialogView);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width = (int) (ScreenUtils.getScreenWidth(context) * builder.getWidth());
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(context) * builder.getHeight());
        layoutParams.gravity = Gravity.CENTER;
        dialog.setCancelable(builder.isCancleable());
        dialog.setCanceledOnTouchOutside(false);
    }

    public NomalProgressDialog(Builder builder,String labelText) {
        this.builder = builder;
        dialog = new Dialog(context, R.style.NormalProgressDialogStyle);
        dialogView = View.inflate(context, R.layout.dialog_progress,null);
        mTextTv = dialogView.findViewById(R.id.tv_message);
        mTextTv.setText(labelText);
        dialogView.setMinimumHeight((int) (ScreenUtils.getScreenWidth(context) * builder.getHeight()));
        dialogView.setMinimumWidth((int) (ScreenUtils.getScreenWidth(context) * builder.getWidth()));
        dialog.setContentView(dialogView);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width = (int) (ScreenUtils.getScreenWidth(context) * builder.getWidth());
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(context) * builder.getHeight());
        layoutParams.gravity = Gravity.CENTER;
        dialog.setCancelable(builder.isCancleable());
        dialog.setCanceledOnTouchOutside(false);
    }

    public NomalProgressDialog setLableText(String lableText){
        mTextTv.setText(lableText);
        return this;
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public boolean isShowing(){
        return dialog.isShowing();
    }

    public static class Builder{

        private final String DEFAULT_LABEL = "加载中...";
        private final int DEFAULT_TEXT_SIZE = 11;
        private final float DEFAULT_HEIGHT = 0.30f;
        private final float DEFAULT_WIDTH = 0.30f;

        private String labelText;
        private int labelTextSize;
        private boolean cancleable;
        private float height;
        private float width;

        public Builder(Context context){
            NomalProgressDialog.context = context;
            labelText = DEFAULT_LABEL;
            labelTextSize = DEFAULT_TEXT_SIZE;
            cancleable = false;
            height = DEFAULT_HEIGHT;
            width = DEFAULT_WIDTH;
        }

        public String getLabelText() {
            return labelText;
        }

        public Builder setLabelText(String labelText) {
            this.labelText = labelText;
            return this;
        }

        public int getLabelTextSize() {
            return labelTextSize;
        }

        public Builder setLabelTextSize(int labelTextSize) {
            this.labelTextSize = labelTextSize;
            return this;
        }

        public boolean isCancleable() {
            return cancleable;
        }

        public Builder setCancleable(boolean cancleable) {
            this.cancleable = cancleable;
            return this;
        }

        public float getHeight() {
            return height;
        }

        public Builder setHeight(float height) {
            this.height = height;
            return this;
        }

        public float getWidth() {
            return width;
        }

        public Builder setWidth(float width) {
            this.width = width;
            return this;
        }

        public NomalProgressDialog build(){
            return new NomalProgressDialog(this);
        }

        public NomalProgressDialog build(String labelText){
            return new NomalProgressDialog(this,labelText);
        }
    }
}
