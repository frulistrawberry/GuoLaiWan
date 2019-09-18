package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.ProfessionalLiveCameraInfoBean;
import com.guolaiwan.bean.ProfessionalLiveDirectorBean;
import com.guolaiwan.bean.ProfessionalLiveSubLiveBean;
import com.guolaiwan.bean.ProfessionalLiveWatcherCheckLiveStateBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;


/**
 * 作者: 蔡朝阳
 * 日期: 2019/1/21
 * 描述:
 */

public class ProfessionalLiveDirectorModel {

    /*专业直播:修改导播位使用状态*/
    public void professionalLiveUpdateDirectorUsable(String id,String isUsed, HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("isUsed", isUsed);
        Observable<HttpResult> observable = HttpClient.getApiService().professionalLiveUpdateDirectorUsable(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:导播界面获取机位信息*/
    public void professionalLiveDirectorGetEveryCameraInfo(String liveId, HttpObserver<ProfessionalLiveCameraInfoBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        Observable<HttpResult<ProfessionalLiveCameraInfoBean>> observable = HttpClient.getApiService().professionalLiveDirectorGetEveryCameraInfo(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:导播界面获取机位直播状态*/
    public void professionalLiveDirectorGetCameraLiveState(String subLiveId, HttpObserver<ProfessionalLiveSubLiveBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("id", subLiveId);
        Observable<HttpResult<ProfessionalLiveSubLiveBean>> observable = HttpClient.getApiService().professionalLiveDirectorGetCameraLiveState(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:导播开启/关闭直播*/
    public void professionalLiveDirectorUpdateLiveState(String liveId,String liveName,String liveStatus ,HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        params.put("liveName", liveName);
        params.put("liveStatusType", liveStatus);
        Observable<HttpResult> observable = HttpClient.getApiService().professionalLiveDirectorUpdateLiveState(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:导播修改直播名称*/
    public void professionalLiveDirectorUpdateLiveName(String liveId,String liveName,HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        params.put("liveName", liveName);
        Observable<HttpResult> observable = HttpClient.getApiService().professionalLiveDirectorUpdateLiveName(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:核对导播直播状态,用于导播打开是的场景控制，导播界面最先调用的接口*/
    public void professionalLiveCheckDirectorLiveState(String liveId,HttpObserver<ProfessionalLiveWatcherCheckLiveStateBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        Observable<HttpResult<ProfessionalLiveWatcherCheckLiveStateBean>> observable = HttpClient.getApiService().professionalLiveCheckDirectorLiveState(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:导播界面设置信号流机位*/
    public void professionalLiveDirectorChangeBroadCastCamera(String liveId,String broadCastCamera,HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        params.put("broadCastCamera", broadCastCamera);
        Observable<HttpResult> observable = HttpClient.getApiService().professionalLiveDirectorChangeBroadCastCamera(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:开启垫播服务*/
    public void professionalLiveStartMatPlayService(String liveId,HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        Observable<HttpResult> observable = HttpClient.getApiService().professionalLiveStartMatPlayService(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:导播界面开始录制*/
    public void professionalLiveStartRecord(String liveId,String subLiveId,HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        params.put("subLiveId", subLiveId);
        Observable<HttpResult> observable = HttpClient.getApiService().professionalLiveStartRecord(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:导播界面停止录制*/
    public void professionalLiveStopRecord(String liveId,String subLiveId,HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        params.put("subLiveId", subLiveId);
        Observable<HttpResult> observable = HttpClient.getApiService().professionalLiveStopRecord(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:导播更新直播机位是否可关闭状态*/
    public void professionalLiveDirectorUpdateCameraCanClose(String liveId,String cameraCanClose,HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("liveId", liveId);
        params.put("cameraCanClose", cameraCanClose);
        Observable<HttpResult> observable = HttpClient.getApiService().professionalLiveDirectorUpdateCameraCanClose(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }
}
