package com.cgx.library.base;

import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;

import com.cgx.library.utils.FrescoUtil;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * date 2018/2/6
 * version 1.0
 * description ViewHolder基类
 * create by chengx
 */

public class BaseViewHolder extends com.chad.library.adapter.base.BaseViewHolder {
    public BaseViewHolder(View view) {
        super(view);
    }

    public BaseViewHolder setImageUrl(@IdRes int viewId, String url){
        SimpleDraweeView simpleDraweeView = getView(viewId);
        FrescoUtil.getInstance().loadNetImage(simpleDraweeView,url);
        return this;
    }

    public BaseViewHolder setNetImageWrapContent(@IdRes int viewId, String url){
        SimpleDraweeView simpleDraweeView = getView(viewId);
        FrescoUtil.getInstance().loadImageWrapContent(simpleDraweeView,url);
        return this;
    }

    @Override
    public com.chad.library.adapter.base.BaseViewHolder setText(int viewId, CharSequence value) {
        if (TextUtils.isEmpty(value)){
            return super.setText(viewId,"");
        }
        return super.setText(viewId, value);
    }
}
