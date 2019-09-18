package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.OderInfoBean;
import com.guolaiwan.bean.OrderBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.utils.CommonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;


/**
 * 作者: 陈冠希
 * 日期: 2018/4/27
 * 描述:
 */

public class OrderInfoModel {
    public void getOrderInfo(String orderId, HttpObserver<OderInfoBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<OderInfoBean>> observable = HttpClient.getApiService().getOrderInfo(orderId);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void commentProduct(String productId,String content,int star,HttpObserver observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params =new HashMap<>();
        params.put("userId", CommonUtils.getUserId());
        params.put("productId",productId);
        params.put("content",content);
        params.put("start",star+"");
        Observable<HttpResult> observable = HttpClient.getApiService().commentProduct(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void updateStatus(String orderNo, String orderStatus, String reason,HttpObserver observer,PublishSubject<LifeCycleEvent> lifeCycleSubject) {
        Map<String,String> params =new HashMap<>();
        params.put("orderNO",orderNo);
        params.put("orderStatus",orderStatus);
        params.put("refundReason",reason);
        Observable<HttpResult> observable = HttpClient.getApiService().updateOrderStatus(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }
}
