package com.guolaiwan.presenter;

import android.util.Log;

import com.cgx.library.base.BasePresenter;
import com.guolaiwan.bean.OderInfoBean;
import com.guolaiwan.bean.ProfessionalLiveApplyCheckPriceBean;
import com.guolaiwan.model.OrderInfoModel;
import com.guolaiwan.model.ProfessionalLiveApplyModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.OrderInfoView;
import com.guolaiwan.ui.iview.ProfessionalLiveApplyView;
import com.guolaiwan.utils.CommonUtils;

import java.util.Map;

import io.reactivex.annotations.NonNull;

public class ProfessionalLiveApplyPresenter extends BasePresenter<ProfessionalLiveApplyView> {

    private ProfessionalLiveApplyModel mModel;

    public ProfessionalLiveApplyPresenter(ProfessionalLiveApplyView mIView) {
        super(mIView);
        mModel = new ProfessionalLiveApplyModel();
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
