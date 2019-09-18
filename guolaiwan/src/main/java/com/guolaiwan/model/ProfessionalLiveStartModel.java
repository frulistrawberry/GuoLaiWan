package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.ProfessionalLiveApplyCheckPriceBean;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
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

public class ProfessionalLiveStartModel {

    /*专业直播:获取订单信息*/
    public void getProfessionalLiveOrderInfo(String userId,HttpObserver<ProfessionalLiveOrderBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        Observable<HttpResult<ProfessionalLiveOrderBean>> observable = HttpClient.getApiService().professionalLiveGetOrderInfo(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }
}
