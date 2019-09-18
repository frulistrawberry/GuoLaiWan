package com.guolaiwan.model;

import com.amap.api.maps.model.LatLng;
import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.Child;
import com.guolaiwan.bean.HomeMerchantsBean;
import com.guolaiwan.bean.ShareContentBean;
import com.guolaiwan.bean.ShareUrlBean;
import com.guolaiwan.bean.UserBean;
import com.guolaiwan.bean.VoiceBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import mabeijianxi.camera.util.Log;

public class GuideModel {
    public void guideHome(String comId,String modular ,HttpObserver<HomeMerchantsBean> observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<HomeMerchantsBean>> observable = HttpClient.getApiService().getHomeMerchants(comId,modular);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void guideMarker(String comId, HttpObserver<List<Child>> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<List<Child>>> observable = HttpClient.getApiService().getChilds(comId);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void getLoginUserSpotMarkerState(String userId,HttpObserver<UserBean> observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<UserBean>> observable = HttpClient.getApiService().getUser(userId);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void guideVoice(String productId,HttpObserver<List<VoiceBean>> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<List<VoiceBean>>> observable = HttpClient.getApiService().getVoice(productId);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void getShareUrl(ShareContentBean shareContentBean, HttpObserver<Map<String,String>> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<Map<String,String>>> observable = HttpClient.getApiService().getShareUrl(shareContentBean);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void getRoad(String currentChildId,String targetChildId ,HttpObserver<List<Child>> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("startId",currentChildId);
        params.put("childId",targetChildId);
        Observable<HttpResult<List<Child>>> observable = HttpClient.getApiService().getRoad(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void refreshLoginUserVisitedSpotOnServer(String UserId,List<String>childIdList,String action,HttpObserver<Map<String,String>> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("userId",UserId);
        if(childIdList!= null && childIdList.size() != 0){
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < childIdList.size(); i++) {
                if (i < childIdList.size() - 1) {
                    sb.append(childIdList.get(i) + ",");
                } else {
                    sb.append(childIdList.get(i));
                }
            }
            params.put("childIdStr",sb.toString());
        }else {
            params.put("childIdStr","");
        }
        params.put("action",action);
        Observable<HttpResult<Map<String,String>>> observable = HttpClient.getApiService().refreshLoginUserVisitedSpotOnServer(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }
}
