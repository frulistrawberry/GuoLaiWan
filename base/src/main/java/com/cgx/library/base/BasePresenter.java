package com.cgx.library.base;


/**
 * date 2018/2/6
 * version 1.0
 * description Presenter基类
 * create by chengx
 */

public class BasePresenter<V extends IBaseVIew> {

    protected V mIView;

    public BasePresenter(V mIView) {
        this.mIView = mIView;
    }

    /**
     * 释放内存,防止内存泄漏
     */
    public void destroy() {
        mIView = null;
    }
}
