package com.guolaiwan.presenter;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.utils.ToastUtils;
import com.guolaiwan.bean.ProfessionalLiveDirectorBean;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.bean.ProfessionalLiveSubLiveBean;
import com.guolaiwan.model.ProfessionalLiveCameraChoiceModel;
import com.guolaiwan.model.ProfessionalLiveStartModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.ProfessionalLiveCameraChoiceView;
import com.guolaiwan.ui.iview.ProfessionalLiveStartView;
import com.guolaiwan.utils.CommonUtils;

public class ProfessionalLiveCameraChoicePresenter extends BasePresenter<ProfessionalLiveCameraChoiceView> {

    private ProfessionalLiveCameraChoiceModel mModel;

    public ProfessionalLiveCameraChoicePresenter(ProfessionalLiveCameraChoiceView mIView) {
        super(mIView);
        mModel = new ProfessionalLiveCameraChoiceModel();
    }

    /*专业直播:直播机位是否可用*/
    public void professionalLiveCameraUseable(String liveId,String cameraNumber){
        mIView.showLoadingDialog();
        mModel.professionalLiveCameraUseable(liveId, cameraNumber, new HttpObserver<ProfessionalLiveSubLiveBean>() {
            @Override
            public void onNext(String message, ProfessionalLiveSubLiveBean data) {
                mIView.dismissLoadingDialog();
                mIView.setCameraUsableCheckResult(data);
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

    /*专业直播:导播机位是否可用*/
    public void professionalLiveDirectorUseable(String liveId){
        mIView.showLoadingDialog();
        mModel.professionalLiveDirectorUseable(liveId, new HttpObserver<ProfessionalLiveDirectorBean>() {
            @Override
            public void onNext(String message, ProfessionalLiveDirectorBean data) {
                mIView.showLoadingDialog();
                mIView.setDirectorUsableCheckedResult(data);
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
