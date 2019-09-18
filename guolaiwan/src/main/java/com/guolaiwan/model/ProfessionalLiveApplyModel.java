package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.OderInfoBean;
import com.guolaiwan.bean.ProfessionalLiveApplyCheckPriceBean;
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

public class ProfessionalLiveApplyModel {

    /*专业直播申请:核算价格*/
    public void checkPrice(String startTime, String endTime, String cameraCount,String isSave, String saveHour, String isMatPlay, HttpObserver<ProfessionalLiveApplyCheckPriceBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        params.put("cameraCount",cameraCount);
        params.put("isSave",isSave);
        params.put("saveHour",saveHour);
        params.put("isMatPlay",isMatPlay);
        Observable<HttpResult<ProfessionalLiveApplyCheckPriceBean>> observable = HttpClient.getApiService().checkPrice(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

}
