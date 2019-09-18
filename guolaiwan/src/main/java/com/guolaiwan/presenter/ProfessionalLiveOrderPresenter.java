package com.guolaiwan.presenter;

import com.cgx.library.base.BasePresenter;
import com.guolaiwan.bean.ProfessionalLiveApplyCheckPriceBean;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.model.ProfessionalLiveApplyModel;
import com.guolaiwan.model.ProfessionalLiveOrderModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.ProfessionalLiveApplyView;
import com.guolaiwan.ui.iview.ProfessionalLiveOrderView;
import com.guolaiwan.utils.CommonUtils;

import io.reactivex.annotations.NonNull;

public class ProfessionalLiveOrderPresenter extends BasePresenter<ProfessionalLiveOrderView> {

    private ProfessionalLiveOrderModel mModel;

    public ProfessionalLiveOrderPresenter(ProfessionalLiveOrderView mIView) {
        super(mIView);
        mModel = new ProfessionalLiveOrderModel();
    }


    /*专业直播申请:获取支付信息*/
    public void getPayInfo(String startTime,String endTime,String cameraCount,String isSave,String saveHour,String isMatPlay,String totalPrice){
        mIView.showLoadingDialog();
        mModel.getPayInfo(startTime,endTime,cameraCount,isSave,saveHour,isMatPlay,totalPrice,new HttpObserver<ProfessionalLiveOrderBean>(){

            @Override
            public void onNext(String message, ProfessionalLiveOrderBean data) {
                mIView.dismissLoadingDialog();
                mIView.gotoPay(data);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                CommonUtils.error(errCode,errMessage);
            }

            @Override
            public void onComplete() {}
        },mIView.getLifeSubject());
    }


    /*专业直播申请:核算价格*/
    public void checkPrice(String startTime,String endTime,String cameraCount,String isSave,String saveHour,String isMatPlay){
        mIView.showLoadingDialog();
        mModel.checkPrice(startTime,endTime,cameraCount,isSave,saveHour,isMatPlay,new HttpObserver<ProfessionalLiveApplyCheckPriceBean>(){
            @Override
            public void onNext(String message, ProfessionalLiveApplyCheckPriceBean data) {
                mIView.dismissLoadingDialog();
                mIView.setCheckedPrice(data.getCheckPrice());
            }
            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                CommonUtils.error(errCode,errMessage);
            }

            @Override
            public void onComplete() {}

        },mIView.getLifeSubject());
    }
}
