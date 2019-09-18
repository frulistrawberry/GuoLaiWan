package com.guolaiwan.presenter;

import com.cgx.library.base.BasePresenter;
import com.guolaiwan.bean.ProfessionalLiveApplyCheckPriceBean;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.model.ProfessionalLiveOrderModel;
import com.guolaiwan.model.ProfessionalLiveStartModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.ProfessionalLiveOrderView;
import com.guolaiwan.ui.iview.ProfessionalLiveStartView;
import com.guolaiwan.utils.CommonUtils;

public class ProfessionalLiveStartPresenter extends BasePresenter<ProfessionalLiveStartView> {

    private ProfessionalLiveStartModel mModel;

    public ProfessionalLiveStartPresenter(ProfessionalLiveStartView mIView) {
        super(mIView);
        mModel = new ProfessionalLiveStartModel();
    }


    /*获取专业直播直播订单信息*/
    public void getProfessionalLiveOrderInfo(){
        mIView.showLoadingDialog();
        String userId = CommonUtils.getUserId();
        mModel.getProfessionalLiveOrderInfo(userId, new HttpObserver<ProfessionalLiveOrderBean>() {
            @Override
            public void onNext(String message, ProfessionalLiveOrderBean data) {
                mIView.dismissLoadingDialog();
                mIView.setProgfessionalLiveOrderInfo(data);
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
