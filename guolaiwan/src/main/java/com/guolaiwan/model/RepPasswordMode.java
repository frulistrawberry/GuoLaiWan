package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/18
 * 描述:
 */

public class RepPasswordMode {
    public void repPassword(String phone, String password, String code,HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> body = new HashMap<>();
        body.put("phone",phone);
        body.put("password",password);
        body.put("code",code);
        Observable<HttpResult> observable = HttpClient.getApiService().repPassword(body);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void getRepPasswordCode(String phone,HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> body = new HashMap<>();
        body.put("phone",phone);
        Observable<HttpResult> observable = HttpClient.getApiService().getRepPasswordCode(body);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }
}
