package com.guolaiwan.presenter;

import com.cgx.library.base.BasePresenter;
import com.guolaiwan.bean.GuideBean;
import com.guolaiwan.model.MainModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.MainView;

public class MainPresenter extends BasePresenter<MainView> {
    MainModel mModel;
    public MainPresenter(MainView mIView) {
        super(mIView);
        mModel = new MainModel();
    }

    public void getGuideInfo() {
        mIView.showLoadingDialog();
        mModel.getGuideInfo( new HttpObserver<GuideBean>() {
            @Override
            public void onNext(String message, GuideBean data) {
                mIView.dismissLoadingDialog();
                if (data!=null){
                    mIView.showGuideInfo(data);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
            }

            @Override
            public void onComplete() {

            }
        },mIView.getLifeSubject());
    }
}
