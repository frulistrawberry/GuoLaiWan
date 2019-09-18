package com.guolaiwan.model;


import com.cgx.library.net.ExceptionHandler;
import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.LookLiveData;
import com.guolaiwan.bean.StartLiveBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.subjects.PublishSubject;

public class CameraModel {

    public void startLive( String liveName, HttpObserver<LookLiveData> observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("anchorId", CommonUtils.getUserId());
        params.put("liveName",liveName);
        params.put("liveType",CommonUtils.isMerchant()?"MERCHANT":"USER");
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().startLive(params),observer,lifeCycleSubject);

    }

    public void stopLive(HttpObserver observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("anchorId", CommonUtils.getUserId());
        params.put("liveType",CommonUtils.isMerchant()?"MERCHANT":"USER");
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().stopLive(params),observer,lifeCycleSubject);
    }

    public void addProduct(String merchantId,String liveId,String productId, String price,HttpObserver observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("liveId",liveId);
        params.put("productId",productId);
        params.put("price",price);
        params.put("liveProductType","AUCTIONPRICE");
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().addProduct(params),observer,lifeCycleSubject);
    }

    public void confirmAuctionPrice(String liveProductId,HttpObserver observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("liveProductId",liveProductId);
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().confirmPrice(params),observer,lifeCycleSubject);
    }
    public void deleteProduct(String liveProductId,String liveId,HttpObserver observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("liveType","MERCHANT");
        params.put("liveProductID",liveProductId);
        params.put("anchorId",CommonUtils.getUserId());
        params.put("liveId",liveId);
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().delProduct(params),observer,lifeCycleSubject);
    }

    public void sendMessage(String message,String liveId ,HttpObserver observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("message", message);
        params.put("liveId",liveId);
        params.put("userId",CommonUtils.getUserId());
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().addMessage(params),observer,lifeCycleSubject);

    }

    public void getLiveInfo(String liveId, HttpObserver<LookLiveData> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().getLiveInfo(liveId),observer,lifeCycleSubject);
    }

    public void sendPrice(String liveId,String productId,String price,String addressId,HttpObserver observer,PublishSubject<LifeCycleEvent> lifeCycleEventPublishSubject){
        Map<String,String> params = new HashMap<>();
        params.put("userId",CommonUtils.getUserId());
        params.put("liveId",liveId);
        params.put("liveProductId",productId);
        params.put("price",price);
        params.put("addressId",addressId);
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().sendPrice(params),observer,lifeCycleEventPublishSubject);
    }


}
