package com.guolaiwan.presenter;

import android.content.Context;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.base.IBaseVIew;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.ToastUtils;
import com.guolaiwan.bean.OrderBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.activity.PayActivity;
import com.guolaiwan.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/27
 * 描述:
 */

public class CustomPayPresenter extends BasePresenter<IBaseVIew> {
    public CustomPayPresenter(IBaseVIew mIView) {
        super(mIView);
    }

    public void customPay(String merchantId,String price){
        mIView.showLoadingDialog();
        Map<String,Object> body = new HashMap<>();
        body.put("payMoney",price);
        body.put("userId", CommonUtils.getUserId());
        body.put("merchantId",merchantId);
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().goToPay(body), new HttpObserver<OrderBean>() {
            @Override
            public void onNext(String message, OrderBean data) {
                PayActivity.launch((Context) mIView,"order",data.getId()+"",null);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.showMsg(errMessage);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }
}
