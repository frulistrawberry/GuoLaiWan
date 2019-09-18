package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.ShopChartBean;
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
 * 日期: 2018/4/24
 * 描述:
 */

public class ShopChartModel {
    public void getBasket(int page, HttpObserver<List<ShopChartBean>> observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<List<ShopChartBean>>> observable = HttpClient.getApiService().getBasket(CommonUtils.getUserId(),page);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void eidtBasket(String orderId,String productId,String productNum,HttpObserver observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("orderId",orderId);
        params.put("productId",productId);
        params.put("productNum",productNum);
        Observable<HttpResult> observable = HttpClient.getApiService().editBasket(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void delBasket(String orderId,HttpObserver observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("orderNO",orderId);
        Observable<HttpResult> observable = HttpClient.getApiService().delBasket(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

//    public void pay
}
