package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/25
 * 描述:
 */

public class AddAddressModel {
    public void addAddress(String province, String city, String district, String consigneeAddress,String consigneeName
            , String phone, HttpObserver observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,Object> params = new HashMap<>();
        params.put("province",province);
        params.put("city",city);
        params.put("district",district);
        params.put("consigneeAddress",consigneeAddress);
        params.put("addressphone",phone);
        params.put("consigneeName",consigneeName);
        params.put("userId", CommonUtils.getUserId());
        Observable<HttpResult> observable = HttpClient.getApiService().addAddress(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);

    }
}
