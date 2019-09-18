package com.guolaiwan.presenter;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.SPUtils;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.OderInfoBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.ProductListBean;
import com.guolaiwan.model.MerchantCollectionModel;
import com.guolaiwan.model.MyCollectionModel;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.MerchantCollectionView;
import com.guolaiwan.ui.iview.ProductCollectionView;
import com.guolaiwan.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/13
 * 描述:
 */

public class MerchantCollectionPresenter extends BasePresenter<MerchantCollectionView> {

    private MerchantCollectionModel mModel;

    public MerchantCollectionPresenter(MerchantCollectionView mIView) {
        super(mIView);
        mModel = new MerchantCollectionModel();
    }

    public void refresh(){
        mIView.showLoadingDialog();
        mModel.getMerchantCollection( new HttpObserver<List<MerchantBean>>() {
            @Override
            public void onNext(String message, List<MerchantBean> data) {
                mIView.dismissLoadingDialog();
                if (data != null){
                    if (CollectionUtils.isEmpty(data)){
                        mIView.showEmpty();
                    }else {
                        mIView.loadCollectList(data);
                        mIView.showContent();
                    }
                }else {
                    mIView.showEmpty();
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                mIView.showError();
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    public void delete(String merchantId){
        mIView.showLoadingDialog();
        Map<String,Object> body = new HashMap<>();
        body.put("userId", CommonUtils.getUserId());
        body.put("merId",merchantId);
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().delCollectedMerchant(body), new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                mIView.dismissLoadingDialog();
                CommonUtils.showMsg(message);
                refresh();
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                CommonUtils.showMsg(errMessage);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }
}
