package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.utils.CommonUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * 创建者: 蔡朝阳
 * 日期:  2018/12/3.
 * 说明:
 */

public class MerchantCollectionModel {
    public void getMerchantCollection(HttpObserver<List<MerchantBean>> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<List<MerchantBean>>> observable = HttpClient.getApiService().getCollectedMerchant(CommonUtils.getUserId());
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }
}
