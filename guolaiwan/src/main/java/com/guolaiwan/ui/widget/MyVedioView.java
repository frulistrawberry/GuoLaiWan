package com.guolaiwan.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import io.vov.vitamio.widget.VideoView;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/2/20.
 * 说明:  处理观看直播时，出现白边的问题
 */

public class MyVedioView extends VideoView {

    public MyVedioView(Context context) {
        super(context);
    }

    public MyVedioView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVedioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if(widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSize,heightSize);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
