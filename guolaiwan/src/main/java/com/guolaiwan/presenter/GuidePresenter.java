package com.guolaiwan.presenter;


import com.amap.api.maps.model.LatLng;
import com.cgx.library.base.BasePresenter;
import com.guolaiwan.bean.Child;
import com.guolaiwan.bean.HomeMerchantsBean;
import com.guolaiwan.bean.ShareContentBean;
import com.guolaiwan.bean.UserBean;
import com.guolaiwan.bean.VoiceBean;
import com.guolaiwan.model.GuideModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.GuideView;
import com.guolaiwan.utils.CommonUtils;
import java.util.List;
import java.util.Map;

import mabeijianxi.camera.util.Log;

/**
 * Created by Administrator on 2018/4/22/022.
 */

public class GuidePresenter extends BasePresenter<GuideView>{
    GuideModel mModel;

    public GuidePresenter(GuideView mIView) {
        super(mIView);
        mModel = new GuideModel();
    }

    public void getCategorys(){

    }

    public void getMerchants(String comId,String modular){
        mModel.guideHome(comId,modular, new HttpObserver<HomeMerchantsBean>() {
            @Override
            public void onNext(String message, HomeMerchantsBean data) {
                mIView.addMarkers(data.getMerchants());
            }

            @Override
            public void onError(int errCode, String errMessage) {

            }

            @Override
            public void onComplete() {

            }
        },mIView.getLifeSubject());
    }

    public void getMarkers(String merchantId){
        mModel.guideMarker( merchantId, new HttpObserver<List<Child>>() {
            @Override
            public void onNext(String message, List<Child> data) {
                mIView.addChildren(data);
            }

            @Override
            public void onError(int errCode, String errMessage) {}

            @Override
            public void onComplete() {}
        },mIView.getLifeSubject());
    }


    public void getLoginUserSpotMarkerState(String userId){
        mModel.getLoginUserSpotMarkerState(userId,new HttpObserver<UserBean>() {
            @Override
            public void onNext(String message, UserBean data) {
                mIView.setSpotMarkerState(data);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.showMsg("温馨提示:获取景点状态出错" + errCode);
            }

            @Override
            public void onComplete() {
            }
        },mIView.getLifeSubject());
    }

    public void getVoice(String merchantId){
        mModel.guideVoice(merchantId,new HttpObserver<List<VoiceBean>>() {
            @Override
            public void onNext(String message, List<VoiceBean> data) {
                mIView.setVoice(data);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.showMsg("温馨提示:语音数据获取出错" + errCode);
            }

            @Override
            public void onComplete() {
            }
        },mIView.getLifeSubject());
    }

    public void getShareUrl(ShareContentBean shareContentBean){
        mModel.getShareUrl(shareContentBean,new HttpObserver<Map<String,String>>(){
            @Override
            public void onNext(String message, Map<String,String> data) {
                mIView.shareMyTravel(data.get("url"),data.get("childName"));
            }

            @Override
            public void onComplete() {}

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.showMsg("温馨提示:分享失败" + errCode);
            }
        },mIView.getLifeSubject());

    }

    public void getRoad(String currentChildId,String targetChildId){
        mModel.getRoad(currentChildId,targetChildId,new HttpObserver<List<Child>>(){
            @Override
            public void onNext(String message,List<Child> data) {
                mIView.showRoad(data);
            }

            @Override
            public void onComplete() {}


            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.showMsg("温馨提示:路线规划错误" + errCode);
            }
        },mIView.getLifeSubject());
    }


    public void refreshLoginUserVisitedSpotOnServer(String UserId,List<String>childIdList,String action){
        mModel.refreshLoginUserVisitedSpotOnServer(UserId,childIdList,action,new HttpObserver<Map<String,String>>() {

            @Override
            public void onNext(String message, Map<String,String> data) {
                getLoginUserSpotMarkerState(CommonUtils.getUserId());
            }

            @Override
            public void onComplete() {}

            @Override
            public void onError(int errCode, String errMessage) {
                if(errCode != 404){
                    CommonUtils.showMsg("温馨提示:更新游览记录出错" + errCode);
                }
            }
        },mIView.getLifeSubject());
    }
}
