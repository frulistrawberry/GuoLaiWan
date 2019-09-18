package com.guolaiwan.presenter;

import android.util.Log;

import com.cgx.library.base.BasePresenter;
import com.guolaiwan.bean.OderInfoBean;
import com.guolaiwan.model.OrderInfoModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.activity.OrderInfoActivity;
import com.guolaiwan.ui.iview.OrderInfoView;
import com.guolaiwan.utils.CommonUtils;

public class OrderInfoPresenter extends BasePresenter<OrderInfoView> {

    private OrderInfoModel mModel;

    public OrderInfoPresenter( OrderInfoView mIView) {
        super(mIView);
        mModel = new OrderInfoModel();
    }

    public void loadOrderInfo(String orderId){
        mIView.showLoading();
        mModel.getOrderInfo(orderId, new HttpObserver<OderInfoBean>() {
            @Override
            public void onNext(String message, OderInfoBean data) {
                mIView.loadOrderInfo(data);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.error(errCode,errMessage);
            }

            @Override
            public void onComplete() {
                mIView.showContent();
            }
        },mIView.getLifeSubject());
    }

    public void updateStatus(String orderNo,String orderStatus,String reason){
        mIView.showLoadingDialog();
        mModel.updateStatus(orderNo, orderStatus, reason, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
                //评价后关闭Activity
                if(orderStatus.equals("COMMENTED")){
                    mIView.closeActivity();
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                CommonUtils.showMsg("温馨提示:" + errMessage + errCode);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    public void commentProduct(final String orderNo,final String orderId, final String productId, String content, int star){
        mIView.showLoadingDialog();
        mModel.commentProduct(productId,content,star, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                //CommonUtils.showMsg(message);
                //loadOrderInfo(orderId);
                //评论完成更新订单状态为已评论
                updateStatus(orderNo,"COMMENTED",null);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.error(errCode,errMessage);
            }


            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }


}
