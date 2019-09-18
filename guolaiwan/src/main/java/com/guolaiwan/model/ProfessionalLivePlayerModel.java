package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.ProfessionalLiveWatcherCheckLiveStateBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;


/**
 * 作者: 蔡朝阳
 * 日期: 2019/1/21
 * 描述:
 */

public class ProfessionalLivePlayerModel {

    /*专业直播：观看用户获取当前主播机位URL*/
    public void professionalLiveWatcherGetLiveState(String liveId, HttpObserver<ProfessionalLiveWatcherCheckLiveStateBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        Observable<HttpResult<ProfessionalLiveWatcherCheckLiveStateBean>> observable = HttpClient.getApiService().professionalLiveWatcherGetLiveState(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:发送评论信息*/
    public void professionLiveSendMessage(String commentMessage,String liveId ,HttpObserver observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("commentMessage", commentMessage);
        params.put("liveId",liveId);
        params.put("userId", CommonUtils.getUserId());
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().professionalLiveSendMessage(params),observer,lifeCycleSubject);
    }



}
