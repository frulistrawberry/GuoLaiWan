package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.StringUtils;
import com.guolaiwan.bean.DistributorBean;
import com.guolaiwan.bean.DistributorListBean;
import com.guolaiwan.bean.ProductInfoBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.utils.CommonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.PublishSubject;
import retrofit2.http.PATCH;


public class ProductInfoModel {
    public void getProductInfoData(String productId, HttpObserver<ProductInfoData> observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){

        Observable<HttpResult<ProductInfoBean>> productObs = HttpClient.getApiService().getProductInfo(productId,CommonUtils.getUserId());
        Observable<HttpResult<DistributorListBean>> distributorsObs = HttpClient.getApiService().getDistributorByPro(productId);
        Observable<HttpResult<ProductInfoData>> observable = Observable.zip(productObs, distributorsObs, new BiFunction<HttpResult<ProductInfoBean>, HttpResult<DistributorListBean>, HttpResult<ProductInfoData>>() {
            @Override
            public HttpResult<ProductInfoData> apply(HttpResult<ProductInfoBean> productInfoBeanHttpResult, HttpResult<DistributorListBean> distributorListBeanHttpResult) throws Exception {
                HttpResult<ProductInfoData> result = new HttpResult<>();
                ProductInfoData productInfoData = new ProductInfoData();
                if (productInfoBeanHttpResult.getData() != null) {
                    productInfoData.setProductInfo(productInfoBeanHttpResult.getData());
                }

                if (distributorListBeanHttpResult.getData() != null) {
                    if (!CollectionUtils.isEmpty(distributorListBeanHttpResult.getData().getDistributors())) {
                        productInfoData.setDistributors(distributorListBeanHttpResult.getData().getDistributors());
                    }
                }
                result.setStatus(200);
                result.setMessage("请求成功！");
                result.setData(productInfoData);

                return result;
            }
        });

        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);

    }


    public void joinBasket(String productId,String addressId,HttpObserver observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params =new HashMap<>();
        params.put("userId", CommonUtils.getUserId());
        params.put("productId",productId);
        if (!StringUtils.isEmpty(addressId))
            params.put("addressId",addressId);
        Observable<HttpResult> observable = HttpClient.getApiService().joinBasket(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void collectProduct(String productId,HttpObserver observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params =new HashMap<>();
        params.put("userId", CommonUtils.getUserId());
        params.put("productId",productId);
        Observable<HttpResult> observable = HttpClient.getApiService().collectProduct(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void commentProduct(String productId,String content,int star,HttpObserver observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params =new HashMap<>();
        params.put("userId", CommonUtils.getUserId());
        params.put("productId",productId);
        params.put("content",content);
        params.put("start",star+"");
        Observable<HttpResult> observable = HttpClient.getApiService().commentProduct(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public static class ProductInfoData{
        private ProductInfoBean productInfo;
        private List<DistributorBean> distributors;

        public ProductInfoBean getProductInfo() {
            return productInfo;
        }

        public void setProductInfo(ProductInfoBean productInfo) {
            this.productInfo = productInfo;
        }

        public List<DistributorBean> getDistributors() {
            return distributors;
        }

        public void setDistributors(List<DistributorBean> distributors) {
            this.distributors = distributors;
        }
    }

}
