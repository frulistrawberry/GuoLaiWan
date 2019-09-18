package com.cgx.library.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgx.library.R;
import com.cgx.library.utils.SizeUtils;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/20
 * 描述: 标题栏
 */


public class TitleBar extends LinearLayout {

    private View mDivider;
    private ImageView mLeftTitleIv;
    private TextView mLeftTitleTv;
    private TextView mTitleTv;
    private ImageView mRightTitleIv;
    private TextView mRightTitleTv;
    private LinearLayout mSearchView;
    private TextView mSerachTv;
    //中间title带logo
    private ImageView mCenterTitleWithLogoIv;
    private TextView mCenterTitleWithLogoTv;
    private LinearLayout mCenterTitleWithLogoLl;
    //中间title带logo右侧text
    private TextView mRightOfCenterTitleWithLogoTextTv;
    //二维码
    private ImageView mQrCodeIv;
    //右侧图标
    private ImageView mRightIv;


    public TitleBar(Context context) {
        super(context);
        init();
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public TitleBar setTitle(CharSequence title){
        if (!TextUtils.isEmpty(title)){
            mTitleTv.setText(title);
        }
        return this;
    }

    public TitleBar setLeftText(CharSequence text, OnClickListener listener){
        mLeftTitleIv.setVisibility(GONE);
        mLeftTitleTv.setVisibility(VISIBLE);
        mLeftTitleTv.setText(text);
        mLeftTitleTv.setOnClickListener(listener);
        return this;
    }

    public TitleBar setLeftImage(int resId, OnClickListener listener){
        mLeftTitleTv.setVisibility(GONE);
        mLeftTitleIv.setVisibility(VISIBLE);
        mLeftTitleIv.setImageResource(resId);
        mLeftTitleIv.setOnClickListener(listener);
        return this;
    }

    public TitleBar setRightText(CharSequence text, OnClickListener listener){
        mRightTitleIv.setVisibility(GONE);
        mRightTitleTv.setVisibility(VISIBLE);
        mRightTitleTv.setText(text);
        mRightTitleTv.setOnClickListener(listener);
        return this;
    }

    public TitleBar setRightImage(int resId, OnClickListener listener){
        mRightTitleTv.setVisibility(GONE);
        mRightTitleIv.setVisibility(VISIBLE);
        mRightTitleIv.setImageResource(resId);
        mRightTitleIv.setOnClickListener(listener);
        return this;
    }

    public TitleBar setRightImage(int resId){
        mRightTitleTv.setVisibility(GONE);
        mRightTitleIv.setVisibility(VISIBLE);
        mRightTitleIv.setImageResource(resId);
        return this;
    }

    public TitleBar showBack(){
       return setLeftImage(R.mipmap.back_black,new OnClickListener() {
           @Override
           public void onClick(View v) {
               if (getContext() instanceof Activity){
                   ((Activity) getContext()).onBackPressed();
               }
           }
       });
    }

    public TitleBar showLogo(int resId){{
        ImageView logoIv = findViewById(R.id.iv_logo);
        logoIv.setImageResource(resId);
        logoIv.setVisibility(VISIBLE);
        return this;
    }

    }

    public TitleBar showBackWithText(String text){
        mLeftTitleIv.setVisibility(GONE);
        mLeftTitleTv.setVisibility(VISIBLE);
        mLeftTitleTv.setText(text);
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.icon_title_back);
        mLeftTitleTv.setCompoundDrawables(drawable,null,null,null);
        mLeftTitleTv.setCompoundDrawablePadding(SizeUtils.dp2px(getContext(),10));
        mLeftTitleTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity){
                    ((Activity) getContext()).onBackPressed();
                }
            }
        });
        return this;
    }

    public TitleBar showSearchView(String serachHint,OnClickListener onClickListener){
        mSearchView.setVisibility(VISIBLE);
        mSerachTv.setText(serachHint);
        mSearchView.setOnClickListener(onClickListener);
        return this;
    }

    public TitleBar showDivider(){
        mDivider.setVisibility(VISIBLE);
        return this;
    }

    public TitleBar hideDivider(){
        mDivider.setVisibility(GONE);
        return this;
    }

    public void show(){
        setVisibility(VISIBLE);
    }

    public void hide(){
        setVisibility(GONE);
    }

    //中间标题带logo
    public TitleBar setCenterTitleWithLogo(int resId,CharSequence title){
        mCenterTitleWithLogoLl.setVisibility(View.VISIBLE);
        mCenterTitleWithLogoTv.setText(title);
        mCenterTitleWithLogoIv.setImageResource(resId);
        return this;
    }

    //中间标题带logo,右侧text
    public TitleBar setTextRightOfCenterTitleWithLogo(CharSequence text,OnClickListener onClickListener){
        mRightOfCenterTitleWithLogoTextTv.setVisibility(View.VISIBLE);
        mRightOfCenterTitleWithLogoTextTv.setText(text);
        mRightOfCenterTitleWithLogoTextTv.setOnClickListener(onClickListener);
        return this;
    }

    //二维码
    public TitleBar setQrCodeIcon(int resId,OnClickListener onClickListener){
        mQrCodeIv.setVisibility(View.VISIBLE);
        mQrCodeIv.setImageResource(resId);
        mQrCodeIv.setOnClickListener(onClickListener);
        return this;
    }

    //右侧图标
    public TitleBar setRightImggeForHomeShare(int resId, OnClickListener listener){
        mRightIv.setVisibility(View.VISIBLE);
        mRightIv.setImageResource(resId);
        mRightIv.setOnClickListener(listener);
        return this;
    }

    private void init(){
        setOrientation(VERTICAL);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        inflater.inflate(R.layout.include_title_bar,this,true);
        mLeftTitleIv = findViewById(R.id.iv_title_left);
        mLeftTitleTv = findViewById(R.id.tv_title_left);
        mTitleTv = findViewById(R.id.tv_title);
        mRightTitleIv = findViewById(R.id.iv_title_right);
        mRightTitleTv = findViewById(R.id.tv_title_right);
        mDivider = findViewById(R.id.v_title_divider);
        mSearchView = findViewById(R.id.search_view);
        mSerachTv = findViewById(R.id.search_tv);
        mSearchView.setVisibility(GONE);
        //新布局，中间title带logo
        mCenterTitleWithLogoLl = findViewById(R.id.ll_title_logo_center);
        mCenterTitleWithLogoIv = findViewById(R.id.iv_logo_center);
        mCenterTitleWithLogoTv = findViewById(R.id.tv_title_center);
        //中间title带logo,右侧文本
        mRightOfCenterTitleWithLogoTextTv = findViewById(R.id.tv_to_right_of_center_title);
        //二维码
        mQrCodeIv = findViewById(R.id.iv_qrcode);
        //右侧图标
        mRightIv = findViewById(R.id.iv_right);
    }


}
