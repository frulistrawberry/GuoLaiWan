package com.guolaiwan.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.cgx.library.utils.ScreenUtils;
import com.cgx.library.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;

/**
 * 作者: chengx
 * 日期: 2016/11/17.
 * 描述:
 */
public class NormalSelectDialog {
    private static Context context;
    private Dialog dialog;
    private View dialogView;
    private TextView title;
    private Button cancelBtn;
    private LinearLayout containerLayout;

    private Builder builder;
    private List<String> data;
    private int selectPosition;

    private NormalSelectDialog(Builder builder){
        this.builder = builder;
        dialog = new Dialog(context, R.style.NormalSelectDialogStyle);
        dialogView = View.inflate(context,R.layout.widget_select_dialog,null);
        dialog.setContentView(dialogView);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenUtils.getScreenWidth(context) * builder.getItemWidth());
        lp.gravity = Gravity.BOTTOM;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        containerLayout = (LinearLayout) dialogView.findViewById(R.id.action_dialog_linearlayout);
        cancelBtn = (Button) dialogView.findViewById(R.id.action_dialog_botbtn);
        cancelBtn.setText(builder.getCancelButtonText());
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(builder.isTouchOutside());
    }

    private void loadItem(){
        cancelBtn.setTextColor(builder.getItemTextColor());
        cancelBtn.setTextSize(builder.getItemTextSize());
        LinearLayout.LayoutParams btnlp = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,builder.getItemHeight());
        btnlp.topMargin = 10;
        cancelBtn.setLayoutParams(btnlp);

        if (data.size() == 1){
            Button button = getButton(data.get(0),0);
            button.setBackgroundResource(R.drawable.selector_widget_actiondialog_single);
            containerLayout.addView(button);
        }else if (data.size()>1){
            for (int i = 0; i < data.size(); i++) {
                Button button = getButton(data.get(i), i);
                if (i == 0){
                    button.setBackgroundResource(R.drawable
                            .selector_widget_actiondialog_top);
                }else if (i != data.size() - 1){
                    button.setBackgroundResource(R.drawable
                            .selector_widget_actiondialog_middle);
                } else {
                    button.setBackgroundResource(R.drawable
                            .selector_widget_actiondialog_bottom);
                }
                containerLayout.addView(button);
            }

        }
    }

    private Button getButton(String text, int position){
        final Button button = new Button(context);
        button.setText(text);
        button.setTag(position);
        button.setTextColor(builder.getItemTextColor());
        button.setTextSize(builder.getItemTextSize());
        button.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, builder.getItemHeight()));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (builder.getOnItemClickListener() != null) {
                    selectPosition = Integer.parseInt(button.getTag().toString());
                    builder.getOnItemClickListener().onItemClick(button, selectPosition);
                }
            }
        });
        return button;
    }

    public void setDataList(List<String> datas) {
        int count = containerLayout.getChildCount();
        if(count>1){
            containerLayout.removeViewsInLayout(1,count-1);
        }
        this.data = (datas == null ? new ArrayList<String>() : datas);
        loadItem();
    }

    public boolean isShowing() {

        return dialog.isShowing();
    }

    public void show() {

        dialog.show();

    }

    public void dismiss() {

        dialog.dismiss();
    }


    public static class Builder{

        private DialogOnItemClickListener onItemClickListener;
        private int itemHeight;
        private float itemWidth;
        private int itemTextColor;
        private float itemTextSize;

        private String cancelButtonText;
        private boolean isTouchOutside;

        public Builder(Context context){
            NormalSelectDialog.context = context;

            onItemClickListener = null;
            itemHeight = SizeUtils.dp2px(context,45);
            itemWidth = 0.92f;
            itemTextColor = ContextCompat.getColor(context,R.color.black_light);
            itemTextSize = 14;

            cancelButtonText = "取消";
            isTouchOutside = true;
        }

        public DialogOnItemClickListener getOnItemClickListener() {
            return onItemClickListener;
        }

        public Builder setOnItemClickListener(DialogOnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }

        public int getItemHeight() {
            return itemHeight;
        }

        public Builder setItemHeight(int itemHeight) {
            this.itemHeight = itemHeight;
            return this;
        }

        public float getItemWidth() {
            return itemWidth;
        }

        public Builder setItemWidth(float itemWidth) {
            this.itemWidth = itemWidth;
            return this;
        }

        public int getItemTextColor() {
            return itemTextColor;
        }

        public Builder setItemTextColor(int itemTextColor) {
            this.itemTextColor = itemTextColor;
            return this;
        }

        public float getItemTextSize() {
            return itemTextSize;
        }

        public Builder setItemTextSize(float itemTextSize) {
            this.itemTextSize = itemTextSize;
            return this;
        }

        public String getCancelButtonText() {
            return cancelButtonText;
        }

        public Builder setCancelButtonText(String cancelButtonText) {
            this.cancelButtonText = cancelButtonText;
            return this;
        }

        public boolean isTouchOutside() {
            return isTouchOutside;
        }

        public Builder setIsTouchOutside(boolean isTouchOutside) {
            this.isTouchOutside = isTouchOutside;
            return this;
        }

        public NormalSelectDialog build(){
            return new NormalSelectDialog(this);
        }
    }
}
