package com.guolaiwan.presenter;


import com.cgx.library.base.BasePresenter;
import com.cgx.library.utils.CollectionUtils;
import com.guolaiwan.model.ProductInfoModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.ProductInfoView;
import com.guolaiwan.utils.CommonUtils;


public class ProductInfoPresenter extends BasePresenter<ProductInfoView> {

    private ProductInfoModel mModel;

    public ProductInfoPresenter(ProductInfoView mIView) {
        super(mIView);
        mModel = new ProductInfoModel();
    }

    public void refreshData(String productId){
        mIView.showLoading();
        mModel.getProductInfoData(productId, new HttpObserver<ProductInfoModel.ProductInfoData>() {
            @Override
            public void onNext(String message, ProductInfoModel.ProductInfoData data) {
                if (data == null) {
                    mIView.showEmpty();
                    return;
                }
                if (data.getProductInfo() != null){
                   mIView.loadProductInfo(data.getProductInfo());
                   mIView.loadComments(data.getProductInfo().getComments());
                }

                if (!CollectionUtils.isEmpty(data.getDistributors())){
                   mIView.loadDistributor(data.getDistributors());
                }else {
                    mIView.showEmpty();
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

    public void addToShopCart(String productId,String addressId){
        mIView.showLoadingDialog();
        mModel.joinBasket(productId,addressId, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
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

    public void collectProduct(final String productId){
        mIView.showLoadingDialog();
        mModel.collectProduct(productId, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
                refreshData(productId);
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

    public void commentProduct(final String productId, String content, int star){
        mIView.showLoadingDialog();
        mModel.commentProduct(productId,content,star, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
                refreshData(productId);
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
