package com.guolaiwan.model;


import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.CollectionUtils;
import com.guolaiwan.bean.HomeMerchantsBean;
import com.guolaiwan.bean.MerchantInfoBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.ProductListBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.PublishSubject;
import mabeijianxi.camera.util.Log;


public class MerchantInfoModel {
    public static final int TYPE_MERCHANT = 1;
    public static final int TYPE_DISTRIBUTOR = 2;

    public void getMerchantInfoData(int type,String merchantId, HttpObserver<MerchantInfoData> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<MerchantInfoBean>> infoObservable = null;
        Observable<HttpResult<ProductListBean>> productsObservable = null;

        if (type == TYPE_MERCHANT){
            infoObservable = HttpClient.getApiService().getMerchantInfo(merchantId);
            productsObservable = HttpClient.getApiService().getProductsByMer(1,merchantId);
;
        }else if (type == TYPE_DISTRIBUTOR){
            infoObservable = HttpClient.getApiService().getDistributorInfo(merchantId);
            productsObservable = HttpClient.getApiService().getProductsByDis(1,merchantId);
        }

        Observable<HttpResult<MerchantInfoData>> resultObservable = Observable.zip(infoObservable, productsObservable, new BiFunction<HttpResult<MerchantInfoBean>, HttpResult<ProductListBean>, HttpResult<MerchantInfoData>>() {
            @Override
            public HttpResult<MerchantInfoData> apply(HttpResult<MerchantInfoBean> merchantInfoBeanHttpResult, HttpResult<ProductListBean> productListBeanHttpResult) throws Exception {
                HttpResult<MerchantInfoData> result = new HttpResult<>();
                MerchantInfoData merchantInfoData = new MerchantInfoData();
                MerchantInfoBean merchantInfo = merchantInfoBeanHttpResult.getData();
                if (merchantInfo != null) {
                    merchantInfoData.setMerchantInfo(merchantInfo);
                }

                ProductListBean products = productListBeanHttpResult.getData();
                if (products != null && !CollectionUtils.isEmpty(products.getProducts())) {
                    merchantInfoData.setProductList(products.getProducts());
                }

                result.setData(merchantInfoData);
                result.setMessage("请求成功！");
                result.setStatus(200);
                return result;
            }
        });
        RetrofitUtil.composeToSubscribe(resultObservable,observer,lifeCycleSubject);
    }


    public void getProductList(int type, int page, String merchantId, HttpObserver<ProductListBean> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<ProductListBean>> observable =  null;
        if (type == TYPE_MERCHANT){
            observable = HttpClient.getApiService().getProductsByMer(page,merchantId);
        }else if (type == TYPE_DISTRIBUTOR){
            observable = HttpClient.getApiService().getProductsByDis(page,merchantId);
        }

        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void refreshLoginUserVisitedSpotOnServer(String UserId,List<String>childIdList,String action,HttpObserver<Map<String,String>> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params = new HashMap<>();
        params.put("userId",UserId);
        if(childIdList!= null && childIdList.size() != 0){
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < childIdList.size(); i++) {
                if (i < childIdList.size() - 1) {
                    sb.append(childIdList.get(i) + ",");
                } else {
                    sb.append(childIdList.get(i));
                }
            }
            params.put("childIdStr",sb.toString());
        }else {
            params.put("childIdStr","");
        }
        params.put("action",action);

        Observable<HttpResult<Map<String,String>>> observable = HttpClient.getApiService().refreshLoginUserVisitedSpotOnServer(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public static class MerchantInfoData implements Serializable{
        private MerchantInfoBean merchantInfo;
        private List<ProductBean> productList;

        public MerchantInfoBean getMerchantInfo() {
            return merchantInfo;
        }

        public void setMerchantInfo(MerchantInfoBean merchantInfo) {
            this.merchantInfo = merchantInfo;
        }

        public List<ProductBean> getProductList() {
            return productList;
        }

        public void setProductList(List<ProductBean> productList) {
            this.productList = productList;
        }

    }

}
