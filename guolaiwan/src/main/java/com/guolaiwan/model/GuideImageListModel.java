package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.GuideSpotContentAndImageBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * 创建者: 蔡朝阳
 * 日期:  2018/11/25.
 * 说明:
 */

public class GuideImageListModel {

    public void getSpotImageAndContent(String voiceId, String childId , HttpObserver<Map<String,List<GuideSpotContentAndImageBean>>> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<Map<String,List<GuideSpotContentAndImageBean>>>> observable = HttpClient.getApiService().getSpotImageAndContent(voiceId,childId);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }
}
