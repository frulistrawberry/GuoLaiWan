package com.guolaiwan.presenter;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.BasePresenter;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.log.LogUtil;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.ProductListBean;
import com.guolaiwan.model.MerchantInfoModel;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.ui.iview.MerchantInfoView;
import com.guolaiwan.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import mabeijianxi.camera.util.Log;


public class MerchantInfoPresenter extends BasePresenter<MerchantInfoView> {
    MerchantInfoModel mModel;

    public MerchantInfoPresenter(MerchantInfoView mIView) {
        super(mIView);
        mModel = new MerchantInfoModel();
    }

    public void refreshData(int type,String merchantId){
        mIView.showLoading();
        mModel.getMerchantInfoData(type,merchantId, new HttpObserver<MerchantInfoModel.MerchantInfoData>() {
            @Override
            public void onNext(String message, MerchantInfoModel.MerchantInfoData data) {
                if (data!=null){
                    mIView.loadMerchantInfo(data.getMerchantInfo());
                    if (CollectionUtils.isEmpty(data.getProductList())){
                        mIView.showEmpty();
                    }else {
                        mIView.loadProductList(data.getProductList());

                    }
                    mIView.showContent();
                }else {
                    mIView.showEmpty();
                }
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

    public void loadProductList(int type,int page,String merchantId){
        mIView.showLoading();
        mModel.getProductList(type,page, merchantId, new HttpObserver<ProductListBean>() {
            @Override
            public void onNext(String message, ProductListBean data) {
                if (data == null){
                    mIView.showEmpty();
                }else {
                    if (CollectionUtils.isEmpty(data.getProducts())){
                        mIView.showEmpty();
                    }else {
                        List<ProductBean> dataList = new ArrayList<>();
                        dataList.addAll(data.getProducts());
                        mIView.loadProductList(dataList);
                    }
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
    //收藏
    public void collectMerchant(String merchantId){
        Map<String,String> params =new HashMap<>();
        params.put("userId", CommonUtils.getUserId());
        params.put("merId",merchantId);
        Observable<HttpResult> observable = HttpClient.getApiService().collectMerchant(params);
        RetrofitUtil.composeToSubscribe(observable,new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
                mIView.setCollectImageState();
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.error(errCode,errMessage);
            }
            @Override
            public void onComplete() {}
        },mIView.getLifeSubject());
    }
}
