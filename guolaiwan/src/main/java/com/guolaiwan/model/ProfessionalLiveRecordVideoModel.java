package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.ProfessionalLiveApplyCheckPriceBean;
import com.guolaiwan.bean.ProfessionalLiveWatcherCheckLiveStateBean;
import com.guolaiwan.bean.RecordVideoBean;
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
 * 作者: 蔡朝阳
 * 日期: 2019/1/21
 * 描述:
 */

public class ProfessionalLiveRecordVideoModel {
    /*专业直播:获取录制视频列表*/
    public void professionalLiveGetRecordVideoList(int currentPage,HttpObserver<List<RecordVideoBean>> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("userId", CommonUtils.getUserId());
        params.put("currentPage", currentPage + "");
        Observable<HttpResult<List<RecordVideoBean>>>observable = HttpClient.getApiService().professionalLiveGetRecordVideoList(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    /*专业直播:删除视频列表数据*/
    public void professionalLiveDeleteRecordVideoListItem(String id,HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        Observable<HttpResult>observable = HttpClient.getApiService().professionalLiveDeleteRecordVideoListItem(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

}
