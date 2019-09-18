package com.guolaiwan.presenter;


import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.BasePresenter;
import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.LoginBean;
import com.guolaiwan.model.LoginModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.utils.CommonUtils;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/18
 * 描述:
 */

public class LoginPresenter extends BasePresenter<IBaseVIew> {
    private LoginModel mModel;

    public LoginPresenter(IBaseVIew mIView) {
        super(mIView);
        mModel = new LoginModel();
    }

    public void phoneLogin(String phone,String password){
        mIView.showLoadingDialog();
        mModel.phoneLogin(phone, password, new HttpObserver<LoginBean>() {
            @Override
            public void onNext(String message, LoginBean data) {
                if (mIView instanceof BaseActivity){
                    CommonUtils.showMsg(message);
                    CommonUtils.saveUserInfo(data);
                    ((BaseActivity) mIView).finish();
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                //CommonUtils.error(errCode,errMessage);
                CommonUtils.showMsg("温馨提示:手机号或密码错误");
            }


            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    public void weChatLogin(String openId,String nickName,String headUrl){
        mModel.weChatLogin(openId, nickName, headUrl, new HttpObserver<LoginBean>() {
            @Override
            public void onNext(String message, LoginBean data) {
                if (mIView instanceof BaseActivity){
                    CommonUtils.showMsg(message);
                    CommonUtils.saveUserInfo(data);
                    ((BaseActivity) mIView).finish();

                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.error(errCode,errMessage);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }
}
