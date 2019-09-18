package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.LoginBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/18
 * 描述:
 */

public class LoginModel {
    public void phoneLogin(String phone, String password, HttpObserver<LoginBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> body = new HashMap<>();
        body.put("phone",phone);
        body.put("password",password);
        Observable<HttpResult<LoginBean>> observable = HttpClient.getApiService().login(body);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void weChatLogin(String openId,String nickName,String headUrl,HttpObserver<LoginBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> body = new HashMap<>();
        body.put("openId",openId);
        body.put("userNickname",nickName);
        body.put("userHeadimg",headUrl);
        Observable<HttpResult<LoginBean>> observable = HttpClient.getApiService().weChatLogin(body);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }
}
