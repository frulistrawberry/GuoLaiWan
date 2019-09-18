package com.guolaiwan.presenter;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.utils.CollectionUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.guolaiwan.bean.MerchantListBean;
import com.guolaiwan.bean.ProductListBean;
import com.guolaiwan.model.CategoryModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.CategoryView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/13
 * 描述:
 */

public class CategoryPresenter extends BasePresenter<CategoryView> {

    private CategoryModel mModel;

    public CategoryPresenter(CategoryView mIView) {
        super(mIView);
        mModel = new CategoryModel();
    }

    public void refresh(int type,int page,String modularCode,String actId,List<Map<String,String>> retrievals){
        if (type == CategoryView.TYPE_MODU){
            refreshModuData(page,modularCode,retrievals);
        }else if (type == CategoryView.TYPE_ACT){
            refreshActData(page,actId);
        }
    }

    private void refreshActData(int page, String actId) {
        mIView.showLoading();
        mModel.getActData(page, actId, new HttpObserver<CategoryModel.ActData>() {
            @Override
            public void onNext(String message, CategoryModel.ActData data) {
                if (data!=null){
                    mIView.loadActBanner(data.getBannerData());
                    if (CollectionUtils.isEmpty(data.getProductData())){
                        mIView.showEmpty();
                    }else {
                        List<MultiItemEntity> dataList = new ArrayList<>();
                        dataList.addAll(data.getProductData());
                        mIView.loadMerchantList(dataList);
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

    private void refreshModuData(int page, String modularCode, List<Map<String,String>> retrievals) {
        mIView.showLoading();
        mModel.getModularData(page, modularCode, retrievals, new HttpObserver<CategoryModel.ModularData>() {
            @Override
            public void onNext(String message, CategoryModel.ModularData data) {
                if (data!=null){
                    mIView.loadModuBanner(data.getBannerData());
                    mIView.loadModuRetrieval(data.getRetrievalData());
                    if (CollectionUtils.isEmpty(data.getMerchantData())){
                        mIView.showEmpty();
                    }else {
                        List<MultiItemEntity> dataList = new ArrayList<>();
                        dataList.addAll(data.getMerchantData());
                        mIView.loadMerchantList(dataList);
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

    public void loadProductList(int page,String actId){
        mModel.getProductList(page, actId, new HttpObserver<ProductListBean>() {
            @Override
            public void onNext(String message, ProductListBean data) {
                if (data == null){
                    mIView.showEmpty();
                }else {
                    if (CollectionUtils.isEmpty(data.getProducts())){
                        mIView.showEmpty();
                    }else {
                        List<MultiItemEntity> dataList = new ArrayList<>();
                        dataList.addAll(data.getProducts());
                        mIView.loadMerchantList(dataList);
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

    public void loadModuList(int page,String modularCode,List<Map<String,String>> retrievals){
        mModel.getMerchantList(page, modularCode, retrievals, new HttpObserver<MerchantListBean>() {
            @Override
            public void onNext(String message, MerchantListBean data) {
                if (data == null){
                    mIView.showEmpty();
                }else {
                    if (CollectionUtils.isEmpty(data.getMerchants())){
                        mIView.showEmpty();
                    }else {
                        List<MultiItemEntity> dataList = new ArrayList<>();
                        dataList.addAll(data.getMerchants());
                        mIView.loadMerchantList(dataList);
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

}
