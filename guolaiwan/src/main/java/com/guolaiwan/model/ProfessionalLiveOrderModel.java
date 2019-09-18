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

public class ProfessionalLiveOrderModel {

    /*专业直播申请:获取支付信息*/
    public void getPayInfo(String startTime, String endTime, String cameraCount, String isSave, String saveHour, String isMatPlay, String totalPrice, HttpObserver<ProfessionalLiveOrderBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String, String> params = new HashMap<>();
        params.put("userId", CommonUtils.getUserId());
        params.put("userType",CommonUtils.isMerchant()?"MERCHANT":"USER");
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        params.put("cameraCount",cameraCount);
        params.put("isSave",isSave);
        params.put("saveHour",saveHour);
        params.put("isMatPlay",isMatPlay);
        params.put("totalPrice",totalPrice);
        Observable<HttpResult<ProfessionalLiveOrderBean>> observable = HttpClient.getApiService().getProfessionalLivePayInfo(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

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
