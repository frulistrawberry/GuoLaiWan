package com.guolaiwan.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zyyoona7.lib.BaseCustomPopup;

import java.text.MessageFormat;

import app.guolaiwan.com.guolaiwan.R;


public class CmmtPopup extends BaseCustomPopup {

    private View.OnClickListener mCancelListener;
    private View.OnClickListener mOkListener;
    AppCompatTextView mCancelTv;
    AppCompatTextView mOkTv;
    EditText mContentEt;
    RatingBar ratingBar;
    TextView countTv;

    private int count = 5;

    public CmmtPopup(Context context) {
        super(context);
    }

    @Override
    protected void initAttributes() {
        setContentView(R.layout.layout_cmmt, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusAndOutsideEnable(false)
                .setBackgroundDimEnable(true)
                .setDimValue(0.5f);
        getPopupWindow().setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        getPopupWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected void initViews(View view) {

        mCancelTv = getView(R.id.tv_cancel);
        mOkTv = getView(R.id.tv_ok);
        mContentEt = getView(R.id.et_cmmt);
        ratingBar = getView(R.id.star);
        countTv = getView(R.id.tv_count);
        ratingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                count = (int) (ratingCount*2);
                countTv.setText(MessageFormat.format("{0}", count));
            }
        });
    }

    public void setOnCancelClickListener(View.OnClickListener listener) {
        if (mCancelTv == null) {
            return;
        }
        mCancelTv.setOnClickListener(listener);
    }

    public void setOnOkClickListener(View.OnClickListener listener) {
        if (mOkTv == null) {
            return;
        }
        mOkTv.setOnClickListener(listener);
    }

    public boolean isShowing(){
        return  getPopupWindow()!=null && getPopupWindow().isShowing();
    }

    public String getCmmtContent(){
        return mContentEt.getText().toString().trim();
    }

    public int getCount(){
        return count;
    }
}


