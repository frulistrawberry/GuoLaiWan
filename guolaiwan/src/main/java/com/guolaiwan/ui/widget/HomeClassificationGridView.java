package com.guolaiwan.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/1/5.
 * 说明:
 */

public class HomeClassificationGridView extends GridView {

    public HomeClassificationGridView(Context context) {
        super(context);
    }

    public HomeClassificationGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeClassificationGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
