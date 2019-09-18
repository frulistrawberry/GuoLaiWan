package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.ProfessionalLiveDirectorBean;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.bean.ProfessionalLiveSubLiveBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;


/**
 * 作者: 蔡朝阳
 * 日期: 2019/1/21
 * 描述:
 */

public class ProfessionalLiveCameraChoiceModel {

    /*专业直播:直播机位是否可用*/
    public void professionalLiveCameraUseable(String liveId, String cameraNumber, HttpObserver<ProfessionalLiveSubLiveBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        params.put("cameraNumber", cameraNumber);
        Observable<HttpResult<ProfessionalLiveSubLiveBean>> observable = HttpClient.getApiService().professionalLiveCameraUseable(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:导播是否可用*/
    public void professionalLiveDirectorUseable(String liveId, HttpObserver<ProfessionalLiveDirectorBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        Observable<HttpResult<ProfessionalLiveDirectorBean>> observable = HttpClient.getApiService().professionalLiveDirectorUsable(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

}
