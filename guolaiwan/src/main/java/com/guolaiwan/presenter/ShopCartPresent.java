package com.guolaiwan.presenter;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.utils.CollectionUtils;
import com.guolaiwan.bean.ShopChartBean;
import com.guolaiwan.model.ShopChartModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.ShopCartView;
import com.guolaiwan.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/24
 * 描述:
 */

public class ShopCartPresent extends BasePresenter<ShopCartView> {
    ShopChartModel mModel;
    public ShopCartPresent(ShopCartView mIView) {
        super(mIView);
        mModel = new ShopChartModel();
    }

    public void refresh(final int page){
        mIView.showLoading();
        mModel.getBasket(page, new HttpObserver<List<ShopChartBean>>() {
            @Override
            public void onNext(String message, List<ShopChartBean> data) {
                if (CollectionUtils.isEmpty(data)){
                    if (page == 1)
                        mIView.loadShopCart(data);
                    mIView.showEmpty();
                }else {
                    mIView.loadShopCart(data);
                }
                mIView.showContent();
            }

            @Override
            public void onError(int errCode, String errMessage) {
                if (page == 1){
                    List<ShopChartBean> data = new ArrayList<>();
                    mIView.loadShopCart(data);
                }
                mIView.showError();
            }

            @Override
            public void onComplete() {

            }
        },mIView.getLifeSubject());
    }

    public void editShopCart(String orderId,String productId,String count){
        mIView.showLoadingDialog();
        mModel.eidtBasket(orderId, productId, count, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
                mIView.reload();
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

    public void delShopCart(String orderId){
        mIView.showLoadingDialog();
        mModel.delBasket(orderId, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
                mIView.reload();
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.error(errCode,errMessage);
            }


            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        }, mIView.getLifeSubject());
    }
}


