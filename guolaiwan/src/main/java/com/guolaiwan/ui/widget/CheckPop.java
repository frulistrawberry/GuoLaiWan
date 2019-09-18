package com.guolaiwan.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.cgx.library.utils.SizeUtils;
import com.zyyoona7.lib.BaseCustomPopup;

import app.guolaiwan.com.guolaiwan.R;

/**
 * Created by zyyoona7 on 2018/3/12.
 */

public class CheckPop extends BaseCustomPopup {

    private View.OnClickListener mCancelListener;
    private View.OnClickListener mOkListener;
    AppCompatTextView mCancelTv;
    AppCompatTextView mOkTv;
    EditText contentEt;

    public CheckPop(Context context) {
        super(context);
    }

    @Override
    protected void initAttributes() {
        setContentView(R.layout.layout_refund, ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(getContext(),150));
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
        contentEt = getView(R.id.et_cmmt);
        mOkTv.setText("验单");
        contentEt.setHint("请输入订单编号");

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
        return getPopupWindow() != null && getPopupWindow().isShowing();
    }

    public EditText getContentEt() {
        return contentEt;
    }
}


