package com.guolaiwan.presenter;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.utils.CollectionUtils;
import com.guolaiwan.bean.AddressBean;
import com.guolaiwan.model.MyAddressModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.MyAddressView;
import com.guolaiwan.utils.CommonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.NonNull;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/26
 * 描述:
 */

public class MyAddressPresenter extends BasePresenter<MyAddressView> {
    MyAddressModel mModel;
    public MyAddressPresenter(MyAddressView mIView) {
        super(mIView);
        mModel = new MyAddressModel();
    }

    public void refresh(){
        mModel.getMyAddressList(new HttpObserver<List<AddressBean>>() {
            @Override
            public void onNext(String message, List<AddressBean> data) {
//                if (CollectionUtils.isEmpty(data))
//                    return;
//                mIView.loadAddressList(data);
                mIView.dismissLoadingDialog();
                if (data != null){
                    if (CollectionUtils.isEmpty(data)){
                        mIView.showEmpty();
                    }else {
                        mIView.loadAddressList(data);
                        mIView.showContent();
                    }
                }else {
                    mIView.showEmpty();
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.error(errCode,errMessage);
            }

            @Override
            public void onComplete() {
                mIView.showContent();
            }
        },mIView.getLifeSubject());
    }

    public void delAddressByAddressId(String addressId){
        mIView.showLoadingDialog();
        Map<String,String> body = new HashMap<>();
        body.put("userId", CommonUtils.getUserId());
        body.put("addressId",addressId);
        mModel.delAddressByAddressId(body,new HttpObserver(){
            @Override
            public void onNext(String message, Object data) {
                mIView.dismissLoadingDialog();
                CommonUtils.showMsg(message);
                refresh();
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                CommonUtils.showMsg(errMessage);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }
}
