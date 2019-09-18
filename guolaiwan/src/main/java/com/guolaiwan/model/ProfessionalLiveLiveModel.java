package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.ProfessionalLiveSubLiveBean;
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

public class ProfessionalLiveLiveModel {
    /*专业直播:机位开始直播*/
    public void professionalLiveStartSubLive(String liveId,String cameraNumber,String liveName,HttpObserver<ProfessionalLiveSubLiveBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        params.put("cameraNumber", cameraNumber);
        params.put("liveName", liveName);
        Observable<HttpResult<ProfessionalLiveSubLiveBean>> observable = HttpClient.getApiService().professionalLiveStartSubLive(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:机位停止直播*/
    public void professionalLiveStopSubLive(String liveId,String subLiveId,HttpObserver<String> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        params.put("subLiveId", subLiveId);
        Observable<HttpResult<String>> observable = HttpClient.getApiService().professionalLiveStopSubLive(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:机位开始直播开启服务*/
    public void professionalLiveStartSubLiveService(String liveId,String subLiveId,HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        params.put("subLiveId", subLiveId);
        Observable<HttpResult> observable = HttpClient.getApiService().professionalLiveStartSubLiveService(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:机位开始直播关闭服务*/
    public void professionalLiveStopSubLiveService(String liveId,String subLiveId,HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        params.put("subLiveId", subLiveId);
        Observable<HttpResult> observable = HttpClient.getApiService().professionalLiveStopSubLiveService(params);
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
