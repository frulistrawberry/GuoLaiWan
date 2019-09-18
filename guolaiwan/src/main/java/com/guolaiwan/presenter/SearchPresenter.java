package com.guolaiwan.presenter;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.SPUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.guolaiwan.bean.MerchantListBean;
import com.guolaiwan.bean.ProductListBean;
import com.guolaiwan.model.SearchModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.HomeView;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter extends BasePresenter<HomeView> {
    private final String mComCode;
    private SearchModel mModel;

    public SearchPresenter(HomeView mIView) {
        super(mIView);
        mComCode = SPUtils.getString("comCode","0001");
        mModel = new SearchModel();
    }

    public void search(String keywords,String type,int page){
        if (type.equals("MERCHANT")){
            mModel.searchMerchant(keywords, mComCode, page, new HttpObserver<MerchantListBean>() {
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
                            mIView.loadData(dataList);
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
        }else if (type.equals("PRODUCT")){
            mModel.searchProduct(keywords, mComCode, page, new HttpObserver<ProductListBean>() {
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
                            mIView.loadData(dataList);
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
}
