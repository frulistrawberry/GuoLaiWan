package com.guolaiwan.model;


import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.LiveListBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class LiveListModel {

    public void getLiveList(String type,int page, HttpObserver<List<LiveListBean>> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<List<LiveListBean>>> observable = HttpClient.getApiService().getLiveList(type,page);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }
}
