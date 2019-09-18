package com.guolaiwan.presenter;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.SPUtils;
import com.guolaiwan.bean.OderInfoBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.ProductListBean;
import com.guolaiwan.model.MyCollectionModel;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.MyCollectView;
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

public class ProductCollectionPresenter extends BasePresenter<ProductCollectionView> {

    private MyCollectionModel mModel;

    public ProductCollectionPresenter(ProductCollectionView mIView) {
        super(mIView);
        mModel = new MyCollectionModel();
    }

    public void refresh(){
        mIView.showLoading();
        mModel.getMyCollection( new HttpObserver<List<ProductBean>>() {
            @Override
            public void onNext(String message, List<ProductBean> data) {
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
                mIView.showError();
            }

            @Override
            public void onComplete() {}
        },mIView.getLifeSubject());
    }

    public void delete(String productId){
        mIView.showLoading();
        Map<String,Object> body = new HashMap<>();
        body.put("userId", CommonUtils.getUserId());
        body.put("collId",productId);
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().delCollect(body), new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
                refresh();
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.showMsg(errMessage);
            }

            @Override
            public void onComplete() {}
        },mIView.getLifeSubject());
    }

    public void duihuan(String productId,String addressId){
        mIView.showLoadingDialog();
        Map<String,Object> body = new HashMap<>();
        body.put("productId",productId);
        body.put("addressId",addressId);
        body.put("userId",CommonUtils.getUserId());
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().duihuan(body), new HttpObserver<OderInfoBean>() {
            @Override
            public void onNext(String message, OderInfoBean data) {
                CommonUtils.showMsg(message);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.showMsg(errMessage);
            }


            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    public void getGlodList(int page){
        mIView.showLoading();
        String comId = SPUtils.getString("comCode","0001");
        mModel.getGlod(comId, page, new HttpObserver<ProductListBean>() {
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
                                mIView.loadCollectList(dataList);
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
                }
                , mIView.getLifeSubject());
    }




}
