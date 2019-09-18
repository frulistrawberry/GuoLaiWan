package com.guolaiwan.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/1/6.
 * 说明:  修改下拉刷新控件与轮播图和首页分类有滑动冲突的bug
 */

public class FixScrollerPtrFrameLayout extends PtrClassicFrameLayout {


    public FixScrollerPtrFrameLayout(Context context) {
        super(context);
    }

    public FixScrollerPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FixScrollerPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private boolean disallowInterceptTouchEvent = false;
    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        disallowInterceptTouchEvent = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                this.requestDisallowInterceptTouchEvent(false);
                disableWhenHorizontalMove(true);
                break;
        }
        if (disallowInterceptTouchEvent) {
            return dispatchTouchEventSupper(e);
        }
        return super.dispatchTouchEvent(e);
    }
}
