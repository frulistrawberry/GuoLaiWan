package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.AddressBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.utils.CommonUtils;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import retrofit2.http.Body;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/25
 * 描述:
 */

public class MyAddressModel {
    public void getMyAddressList(HttpObserver<List<AddressBean>> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<List<AddressBean>>> observable = HttpClient.getApiService().getAddressList(CommonUtils.getUserId());
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void delAddressByAddressId(Map<String,String> paramsBody, HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult> observable = HttpClient.getApiService().delAddressByAddressId(paramsBody);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }
}
