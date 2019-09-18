package com.guolaiwan.presenter;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.utils.CollectionUtils;
import com.guolaiwan.bean.LiveListBean;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.model.LiveFragmentModel;
import com.guolaiwan.model.LiveListModel;
import com.guolaiwan.model.ProfessionalLiveStartModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.LiveFragmentView;
import com.guolaiwan.ui.iview.LiveListView;
import com.guolaiwan.ui.iview.ProfessionalLiveStartView;
import com.guolaiwan.utils.CommonUtils;

import java.util.List;


public class LiveFragmentPresenter extends BasePresenter<LiveFragmentView> {

    private LiveFragmentModel mModel;

    public LiveFragmentPresenter(LiveFragmentView mIView) {
        super(mIView);
        mModel = new LiveFragmentModel();
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
