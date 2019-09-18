package com.guolaiwan.presenter;

import android.app.Activity;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.model.RegistMode;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.utils.CommonUtils;


public class RegisterPresenter extends BasePresenter<IBaseVIew>{
    RegistMode mModel;

    public RegisterPresenter(IBaseVIew mIView) {
        super(mIView);
        mModel = new RegistMode();
    }


    public void getVerifyCode(String phone){
        mIView.showLoadingDialog();
        mModel.getRegistCode(phone, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);

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

    public void getBindCode(String phone){
        mIView.showLoadingDialog();
        mModel.getBindPhoneCode(phone, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);

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

    public void register(String phone,String password,String code){
        mModel.regist(phone, password, code, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
                if (mIView instanceof Activity){
                    ((Activity) mIView).finish();
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

    public void bindPhone(String phone,String code){
        mModel.bindPhone(phone, code, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
                if (mIView instanceof Activity){
                    ((Activity) mIView).finish();
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
