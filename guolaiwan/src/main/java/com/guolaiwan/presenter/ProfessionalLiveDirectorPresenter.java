package com.guolaiwan.presenter;

import android.app.Activity;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.utils.ToastUtils;
import com.guolaiwan.bean.ProfessionalLiveCameraInfoBean;
import com.guolaiwan.bean.ProfessionalLiveDirectorBean;
import com.guolaiwan.bean.ProfessionalLiveSubLiveBean;
import com.guolaiwan.bean.ProfessionalLiveWatcherCheckLiveStateBean;
import com.guolaiwan.model.ProfessionalLiveCameraChoiceModel;
import com.guolaiwan.model.ProfessionalLiveDirectorModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.activity.ProfessionalLiveDirectorActivity;
import com.guolaiwan.ui.iview.ProfessionalLiveCameraChoiceView;
import com.guolaiwan.ui.iview.ProfessionalLiveDirectorView;
import com.guolaiwan.utils.CommonUtils;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class ProfessionalLiveDirectorPresenter extends BasePresenter<ProfessionalLiveDirectorView> {

    private ProfessionalLiveDirectorModel mModel;

    public ProfessionalLiveDirectorPresenter(ProfessionalLiveDirectorView mIView) {
        super(mIView);
        mModel = new ProfessionalLiveDirectorModel();
    }

    public void professionalLiveCheckDirectorLiveState(String liveId){
        mIView.showLoadingDialog();
        mModel.professionalLiveCheckDirectorLiveState(liveId, new HttpObserver<ProfessionalLiveWatcherCheckLiveStateBean>() {
            @Override
            public void onNext(String message, ProfessionalLiveWatcherCheckLiveStateBean data) {
                mIView.dismissLoadingDialog();
                mIView.setDirectorState(data);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.error(errCode,errMessage);
                mIView.dismissLoadingDialog();
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }


    /*专业直播:修改导播机位使用状态*/
    public void professionalLiveUpdateDirectorUsable(String liveId,String directorId,String isUsed){
        mIView.showLoadingDialog();
        mModel.professionalLiveUpdateDirectorUsable(directorId,isUsed ,new HttpObserver() {

            @Override
            public void onNext(String message, Object data) {
                mIView.dismissLoadingDialog();
                if(isUsed.equals("YES")){
                    professionalLiveDirectorGetEveryCameraInfo(liveId);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.error(errCode,errMessage);
                mIView.dismissLoadingDialog();
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());

    }

    /*专业直播:导播获取机位信息*/
    public void professionalLiveDirectorGetEveryCameraInfo(String liveId){
        mIView.showLoadingDialog();
        mModel.professionalLiveDirectorGetEveryCameraInfo(liveId, new HttpObserver<ProfessionalLiveCameraInfoBean>() {
            @Override
            public void onNext(String message, ProfessionalLiveCameraInfoBean data) {
                mIView.initEveryCamera(data);
                mIView.dismissLoadingDialog();
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.error(errCode,errMessage);
                mIView.dismissLoadingDialog();
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    /*专业直播:导播获取机位直播状态*/
    public void professionalLiveDirectorGetCameraLiveState(String subLiveId){
        mIView.showLoadingDialog();
        mModel.professionalLiveDirectorGetCameraLiveState(subLiveId, new HttpObserver<ProfessionalLiveSubLiveBean>() {
            @Override
            public void onNext(String message, ProfessionalLiveSubLiveBean data) {
                mIView.dismissLoadingDialog();
                mIView.setCameraLiveState(data);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                CommonUtils.error(errCode,errMessage);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    /*专业直播:导播开启/关闭直播*/
    public void professionalLiveDirectorUpdateLiveState(String liveId,String liveName,String liveStatus){
        mIView.showLoadingDialog();
        mModel.professionalLiveDirectorUpdateLiveState(liveId, liveName, liveStatus, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                mIView.dismissLoadingDialog();
                mIView.openOrCloseDirectorLiveResult(liveStatus);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                CommonUtils.error(errCode,errMessage);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    /*专业直播:导播修改直播名称*/
    public void professionalLiveDirectorUpdateLiveName(String liveId,String liveName){
        mIView.showLoadingDialog();
        mModel.professionalLiveDirectorUpdateLiveName(liveId, liveName, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                mIView.dismissLoadingDialog();
                mIView.updateLiveNameResult();
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                CommonUtils.error(errCode,errMessage);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    /*专业直播:导播设置信号流机位*/
    public void professionalLiveDirectorChangeBroadCastCamera(String liveId,String broadCastCamera){
        mIView.showLoadingDialog();
        mModel.professionalLiveDirectorChangeBroadCastCamera(liveId,broadCastCamera,new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                mIView.dismissLoadingDialog();
                mIView.setBroadCastCamera(broadCastCamera);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                CommonUtils.error(errCode,errMessage);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    /*专业直播:开启导播服务*/
    public void professionalLiveStartMatPlayService(String liveId){
        mIView.showLoadingDialog();
        mModel.professionalLiveStartMatPlayService(liveId, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                mIView.dismissLoadingDialog();
                ToastUtils.showToast((ProfessionalLiveDirectorActivity)mIView,"温馨提示:视频上传成功");
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                CommonUtils.error(errCode,errMessage);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    /*专业直播:导播界面开始录制*/
    public void professionalLiveStartRecord(String liveId,String subLiveId,String cameraPosition){
        mIView.showLoadingDialog();
        mModel.professionalLiveStartRecord(liveId, subLiveId, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                mIView.dismissLoadingDialog();
                mIView.startRecordResult(cameraPosition);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                ToastUtils.showToast((ProfessionalLiveDirectorActivity)mIView,"温馨提示:开启录制失败" + errCode);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    /*专业直播:导播界面停止录制*/
    public void professionalLiveStopRecord(String liveId,String subLiveId,String cameraPosition){
        mIView.showLoadingDialog();
        mModel.professionalLiveStopRecord(liveId, subLiveId, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                mIView.dismissLoadingDialog();
                mIView.stopRecordResult(cameraPosition);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                ToastUtils.showToast((ProfessionalLiveDirectorActivity)mIView,"温馨提示:停止录制失败" + errCode);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }


    /*专业直播:导播更新直播机位是否可关闭状态*/
    public void professionalLiveDirectorUpdateCameraCanClose(String liveId,String cameraCanClose){
        mIView.showLoadingDialog();
        mModel.professionalLiveDirectorUpdateCameraCanClose(liveId, cameraCanClose, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                mIView.dismissLoadingDialog();
                CommonUtils.showMsg("温馨提示:设置成功");
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                CommonUtils.error(errCode,errMessage);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }


}
