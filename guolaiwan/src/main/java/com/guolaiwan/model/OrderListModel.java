package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.OrderBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.utils.CommonUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;


/**
 * 作者: 陈冠希
 * 日期: 2018/4/27
 * 描述:
 */

public class OrderListModel  {
    public void getOrderList(String type,String uType ,HttpObserver<List<OrderBean>> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<List<OrderBean>>> observable = HttpClient.getApiService().getOrderList(CommonUtils.getUserId(),type,uType,"true");
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }
}
