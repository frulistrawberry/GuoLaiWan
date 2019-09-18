package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.AddressBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.ProductInfoBean;
import com.guolaiwan.bean.ProductListBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.utils.CommonUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/25
 * 描述:
 */

public class MyCollectionModel {
    public void getMyCollection(HttpObserver<List<ProductBean>> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<List<ProductBean>>> observable = HttpClient.getApiService().getMyCollect(CommonUtils.getUserId());
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void getGlod(String comId, int page, HttpObserver<ProductListBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<ProductListBean>> observable = HttpClient.getApiService().getGoldPruducts(CommonUtils.getUserId(),comId,page);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }
}
