package com.guolaiwan.presenter;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.utils.CollectionUtils;
import com.guolaiwan.bean.LiveListBean;
import com.guolaiwan.model.LiveListModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.LiveListView;

import java.util.List;



public class LiveListPresenter extends BasePresenter<LiveListView> {
    private LiveListModel mModel;


    public LiveListPresenter(LiveListView mIView) {
        super(mIView);
        mModel = new LiveListModel();
    }

    public void refresh(String type,int page){
        mIView.showLoading();
        mModel.getLiveList(type, page, new HttpObserver<List<LiveListBean>>() {
            @Override
            public void onNext(String message, List<LiveListBean> data) {
                    if (CollectionUtils.isEmpty(data)){
                        mIView.showEmpty();
                    }else {
                        mIView.loadLiveList(data);
                    }
                mIView.showContent();
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.showError();
            }

            @Override
            public void onComplete() {

            }
        },mIView.getLifeSubject());
    }
}
