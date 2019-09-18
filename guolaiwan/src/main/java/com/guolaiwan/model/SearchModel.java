package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.MerchantListBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.ProductListBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.subjects.PublishSubject;

public class SearchModel {
    public void searchProduct(String keywords, String comId, int page, HttpObserver<ProductListBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("name",keywords);
        params.put("comId",comId);
        params.put("page",page+"");
        params.put("type","PRODUCT");
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().searchProduct(params),observer,lifeCycleSubject);
    }

    public void searchMerchant(String keywords, String comId, int page, HttpObserver<MerchantListBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("name",keywords);
        params.put("comId",comId);
        params.put("page",page+"");
        params.put("type","MERCHANT");
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().searchMerchant(params),observer,lifeCycleSubject);
    }
}
