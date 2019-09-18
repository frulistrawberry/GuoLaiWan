package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.CollectionUtils;
import com.guolaiwan.bean.FilerBean;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.MerchantListBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.ProductListBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function3;
import io.reactivex.subjects.PublishSubject;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/12
 * 描述:
 */

public class CategoryModel {
    public void getModularData(int page, final String modularCode, @Nullable List<Map<String,String>> retrievals, HttpObserver<ModularData> observer, final PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<List<FilerBean>>> filerObservable = HttpClient.getApiService().getRetrieval(modularCode);
        Observable<HttpResult<List<MerchantBean>>> recommendObservable = HttpClient.getApiService().getRecommendByModu(modularCode);
        Map<String,Object> params = new HashMap<>();
        params.put("page",page);
        params.put("modularCode",modularCode);
        params.put("retrievals",retrievals);
        Observable<HttpResult<MerchantListBean>> merchantObservable = HttpClient.getApiService().getMerchantsByModu(params);
        Observable<HttpResult<ModularData>> observable = Observable.zip(filerObservable, recommendObservable, merchantObservable, new Function3<HttpResult<List<FilerBean>>, HttpResult<List<MerchantBean>>, HttpResult<MerchantListBean>, HttpResult<ModularData>>() {
            @Override
            public HttpResult<ModularData> apply(HttpResult<List<FilerBean>> listHttpResult, HttpResult<List<MerchantBean>> listHttpResult2, HttpResult<MerchantListBean> listHttpResult3) throws Exception {
                HttpResult<ModularData> result = new HttpResult<>();
                ModularData modularData = new ModularData();
                if (!CollectionUtils.isEmpty(listHttpResult.getData())){
                    modularData.setRetrievalData(listHttpResult.getData());
                }
                if (!CollectionUtils.isEmpty(listHttpResult2.getData())){
                    modularData.setBannerData(listHttpResult2.getData());
                }
                if (listHttpResult3.getData() != null && !CollectionUtils.isEmpty(listHttpResult3.getData().getMerchants())){
                    for (int i = 0; i < listHttpResult3.getData().getMerchants().size(); i++) {
                        MerchantBean productBean = listHttpResult3.getData().getMerchants().get(i);
                        productBean.setRec(i == 0);
                    }
                    modularData.setMerchantData(listHttpResult3.getData().getMerchants());
                }
                result.setData(modularData);
                result.setStatus(200);
                result.setMessage("请求成功！");
                return result;
            }
        });
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void getActData(int page,String actId,HttpObserver<ActData> observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<List<ProductBean>>> bannerObservable = HttpClient.getApiService().getRecommendByAct(actId);
        Observable<HttpResult<ProductListBean>> productObservable = HttpClient.getApiService().getProductsByAct(page,actId);
        Observable<HttpResult<ActData>> observable = Observable.zip(bannerObservable, productObservable, new BiFunction<HttpResult<List<ProductBean>>, HttpResult<ProductListBean>, HttpResult<ActData>>() {
            @Override
            public HttpResult<ActData> apply(HttpResult<List<ProductBean>> listHttpResult, HttpResult<ProductListBean> productListBeanHttpResult) throws Exception {
                HttpResult<ActData> result = new HttpResult<>();
                ActData actData = new ActData();
                if (!CollectionUtils.isEmpty(listHttpResult.getData())){
                    actData.setBannerData(listHttpResult.getData());
                }
                if (productListBeanHttpResult.getData() != null && !CollectionUtils.isEmpty(productListBeanHttpResult.getData().getProducts())){
                    for (int i = 0; i < productListBeanHttpResult.getData().getProducts().size(); i++) {
                        ProductBean productBean = productListBeanHttpResult.getData().getProducts().get(i);
                        productBean.setRec(i == 0);
                    }
                    actData.setProductData(productListBeanHttpResult.getData().getProducts());
                }
                result.setData(actData);
                result.setStatus(200);
                result.setMessage("请求成功！");
                return result;
            }
        });
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void getMerchantList(int page, String modularCode, @Nullable List<Map<String,String>> retrievals, HttpObserver<MerchantListBean> observer, final PublishSubject<LifeCycleEvent> lifeCycleSubject){

        Map<String,Object> params = new HashMap<>();
        params.put("page",page);
        params.put("modularCode",modularCode);
        params.put("retrievals",retrievals);
        Observable<HttpResult<MerchantListBean>> observable = HttpClient.getApiService().getMerchantsByModu(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void getProductList(int page,String actId,HttpObserver<ProductListBean> observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<ProductListBean>> observable = HttpClient.getApiService().getProductsByAct(page,actId);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public static class ModularData{
        private List<FilerBean> retrievalData;
        private List<MerchantBean> bannerData;
        private List<MerchantBean> merchantData;

        public List<FilerBean> getRetrievalData() {
            return retrievalData;
        }

        public void setRetrievalData(List<FilerBean> retrievalData) {
            this.retrievalData = retrievalData;
        }

        public List<MerchantBean> getBannerData() {
            return bannerData;
        }

        public void setBannerData(List<MerchantBean> bannerData) {
            this.bannerData = bannerData;
        }

        public List<MerchantBean> getMerchantData() {
            return merchantData;
        }

        public void setMerchantData(List<MerchantBean> merchantData) {
            this.merchantData = merchantData;
        }
    }

    public static class ActData{
        private List<ProductBean> bannerData;
        private List<ProductBean> productData;

        public List<ProductBean> getBannerData() {
            return bannerData;
        }

        public void setBannerData(List<ProductBean> bannerData) {
            this.bannerData = bannerData;
        }

        public List<ProductBean> getProductData() {
            return productData;
        }

        public void setProductData(List<ProductBean> productData) {
            this.productData = productData;
        }
    }
}
