package com.guolaiwan.presenter;

import android.app.Activity;
import android.content.Context;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.BasePresenter;
import com.cgx.library.base.IBaseVIew;
import com.cgx.library.utils.ToastUtils;
import com.guolaiwan.model.RepPasswordMode;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.utils.CommonUtils;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/18
 * 描述:
 */

public class RepPasswordPresenter extends BasePresenter<IBaseVIew> {
    private RepPasswordMode mModel;
    public RepPasswordPresenter(IBaseVIew mIView) {
        super(mIView);
    }

    public void repPassword(String phone,String password,String code){
        mIView.showLoadingDialog();
        mModel.repPassword(phone, password, code, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                if (mIView instanceof Activity){
                    CommonUtils.showMsg(message);
                    ((BaseActivity) mIView).finish();
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {

            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    public void getRepPasswordCode(String phone){
        mIView.showLoadingDialog();
        mModel.getRepPasswordCode(phone, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                if (mIView instanceof Activity){
                    CommonUtils.showMsg(message);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {

            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }
}
