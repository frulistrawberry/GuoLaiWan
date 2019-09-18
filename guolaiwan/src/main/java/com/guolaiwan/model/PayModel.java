package com.guolaiwan.model;

import android.util.Log;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.EncryptUtils;
import com.cgx.library.utils.StringUtils;
import com.cgx.library.utils.log.LogUtil;
import com.guolaiwan.bean.AlipayBean;
import com.guolaiwan.bean.WeChatpayBean;
import com.guolaiwan.constant.KeyConstant;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.net.WXApiService;
import com.guolaiwan.utils.CommonUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/25
 * 描述:
 */

public class PayModel {

    private PublishSubject<LifeCycleEvent> mLifecycleSubject = PublishSubject.create();

    public void aliPayProduct(String productId, String addressId, int productNum,HttpObserver<AlipayBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,Object> params = new HashMap<>();
        params.put("productId",productId);
        params.put("productNum",productNum);
        params.put("userId", CommonUtils.getUserId());
        params.put("paytype","ALIPAY");
        if (!StringUtils.isEmpty(addressId)){
            params.put("addressId",addressId);
        }
        Observable<HttpResult<AlipayBean>> observable = HttpClient.getApiService().aliPayOrder(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void alipayAllOrders(List<Map<String,Object>> orders,HttpObserver<String> observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,Object> params = new HashMap<>();
        params.put("orders",orders);
        params.put("userId", CommonUtils.getUserId());
        params.put("paytype","ALIPAY");
        Observable<HttpResult<String>> observable = HttpClient.getApiService().aliPayAllOrders(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void alipayOrder(String orders,HttpObserver<String> observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,Object> params = new HashMap<>();
        params.put("orderId",orders);
        params.put("userId", CommonUtils.getUserId());
        params.put("paytype","ALIPAY");
        Observable<HttpResult<String>> observable = HttpClient.getApiService().aliPayOrderType(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void weChatPayProduct(String productId, String addressId,int productNum,HttpObserver<WeChatpayBean> observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,Object> params = new HashMap<>();
        params.put("productId",productId);
        params.put("productNum",productNum);
        params.put("userId", CommonUtils.getUserId());
        params.put("paytype","WEICHAT");
        if (!StringUtils.isEmpty(addressId)){
            params.put("addressId",addressId);
        }
        Observable<HttpResult<WeChatpayBean>> observable = HttpClient.getApiService().weChatPayOrder(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }


    public void weChatPayAllOrders(List<Map<String,Object>> orders, HttpObserver<WeChatpayBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,Object> params = new HashMap<>();
        params.put("orders",orders);
        params.put("userId", CommonUtils.getUserId());
        params.put("paytype","WEICHAT");
        Observable<HttpResult<WeChatpayBean>> observable = HttpClient.getApiService().weChatPayAllOrders(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }


    public void weChatPayOrder(String orderId, HttpObserver<WeChatpayBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,Object> params = new HashMap<>();
        params.put("orderId",orderId);
        params.put("userId", CommonUtils.getUserId());
        params.put("paytype","WEICHAT");
        Observable<HttpResult<WeChatpayBean>> observable = HttpClient.getApiService().weChatPayOrderType(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*微信结算:新增联系人*/
    public void weChatPayAllOrders(String addressId,List<Map<String,Object>> orders, HttpObserver<WeChatpayBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,Object> params = new HashMap<>();
        params.put("addressId",addressId);
        params.put("orders",orders);
        params.put("userId", CommonUtils.getUserId());
        params.put("paytype","WEICHAT");
        Observable<HttpResult<WeChatpayBean>> observable = HttpClient.getApiService().weChatPayAllOrders(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播微信支付*/
    public void professionalWechatPay(String orderId,String totalFee,String payType,HttpObserver<WeChatpayBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("orderId",orderId);
        params.put("totalFee",totalFee);
        params.put("payType",payType);
        Observable<HttpResult<WeChatpayBean>> observable = HttpClient.getApiService().professionalLiveWeiChatPay(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播ALI支付*/
    public void professionalALiPay(String orderId,String totalFee,String payType,HttpObserver<String> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("orderId",orderId);
        params.put("totalFee",totalFee);
        params.put("payType",payType);
        Observable<HttpResult<String>> observable = HttpClient.getApiService().professionalLiveALiPay(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播修改订单状态*/
    public void professionalLiveUpdateOrderState(String userId,String orderStatus,HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("userId",userId);
        params.put("orderStatus",orderStatus);
        Observable<HttpResult> observable = HttpClient.getApiService().professionalLiveUpdateOrderState(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }
}
