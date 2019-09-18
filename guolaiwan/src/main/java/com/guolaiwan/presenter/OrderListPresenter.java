package com.guolaiwan.presenter;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.utils.CollectionUtils;
import com.guolaiwan.bean.OrderBean;
import com.guolaiwan.model.OrderListModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.OrderListView;

import java.util.List;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/27
 * 描述:
 */

public class OrderListPresenter extends BasePresenter<OrderListView> {
    OrderListModel mModel;
    public OrderListPresenter(OrderListView mIView) {
        super(mIView);
        mModel = new OrderListModel();
    }

    public void refresh(String type,String uType){
        mModel.getOrderList(type, uType, new HttpObserver<List<OrderBean>>() {
            @Override
            public void onNext(String message, List<OrderBean> data) {
                if (data != null){
                    if (CollectionUtils.isEmpty(data)){
                        mIView.showEmpty();
                    }else {
                        mIView.loadOrderList(data);
                        mIView.showContent();
                    }
                }else {
                    mIView.showEmpty();
                }

            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.showError();
            }

            @Override
            public void onComplete() {}
        },mIView.getLifeSubject());
    }
}
