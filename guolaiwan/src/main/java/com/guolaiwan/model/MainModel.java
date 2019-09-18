package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.GuideBean;
import com.guolaiwan.bean.LookLiveData;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.subjects.PublishSubject;

public class MainModel {
    public void getGuideInfo(HttpObserver<GuideBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().getGuideInfo(),observer,lifeCycleSubject);
    }
}
