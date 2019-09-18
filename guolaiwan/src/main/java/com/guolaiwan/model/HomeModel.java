package com.guolaiwan.model;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.CollectionUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.guolaiwan.bean.ActivityBean;
import com.guolaiwan.bean.ClassificationBean;
import com.guolaiwan.bean.ClassificationListBean;
import com.guolaiwan.bean.CompanyBean;
import com.guolaiwan.bean.DistributorBean;
import com.guolaiwan.bean.DistributorListBean;
import com.guolaiwan.bean.HomeList;
import com.guolaiwan.bean.ModularBean;
import com.guolaiwan.bean.ProductInfoBean;
import com.guolaiwan.bean.RecommendBean;
import com.guolaiwan.bean.SpecialEventsBean;
import com.guolaiwan.bean.TodayHotSearchBean;
import com.guolaiwan.bean.TodayHotSearchBeanListBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.PublishSubject;


/**
 * 作者: 陈冠希
 * 日期: 2018/3/20
 * 描述:
 */

public class HomeModel {
    public void getHomeData(String comCode,HttpObserver<HomeData> observer, PublishSubject<LifeCycleEvent> lifeCycleSubject){
        final Observable<HttpResult<List<RecommendBean>>> bannerObservable = HttpClient
                .getApiService()
                .getRecommend(comCode);

        Observable<HttpResult<HomeList>> listObservable = HttpClient
                .getApiService()
                .getModulars(comCode);

        Observable<HttpResult<HomeData>> observable = Observable.zip(bannerObservable, listObservable, new BiFunction<HttpResult<List<RecommendBean>>, HttpResult<HomeList>, HttpResult<HomeData>>() {
            @Override
            public HttpResult<HomeData> apply(HttpResult<List<RecommendBean>> bannerResult, HttpResult<HomeList> homeListResult) throws Exception {
                HttpResult<HomeData> result = new HttpResult<>();
                HomeData homeData = new HomeData();

                //首页分类改为两行数据集
                List<ClassificationBean> classificationBeanList = new ArrayList<>();
                ClassificationListBean classificationListBean = new ClassificationListBean();

                if (!CollectionUtils.isEmpty(bannerResult.getData())){
                    homeData.setBannerData(bannerResult.getData());
                }
                if (homeListResult.getData()!=null){
                    HomeList homeList = homeListResult.getData();

                    if (!CollectionUtils.isEmpty(homeList.getModulars())){
                        List<ModularBean> modularBeanList = homeList.getModulars();
                        homeData.setModularData(modularBeanList);
                        for(ModularBean modularBean : modularBeanList){
                            //提出活动中的一元购:无数据
                            //暂时将集赞剔除
                            if(modularBean.getModularName().equals("一元购")){
                                continue;
                            }
                            ClassificationBean classificationBean = new ClassificationBean();
                            classificationBean.setModularCode(modularBean.getModularCode());
                            classificationBean.setModularName(modularBean.getModularName());
                            classificationBean.setModularPic(modularBean.getModularPic());
                            classificationBean.setType("MODULAR");
                            classificationBeanList.add(classificationBean);
                        }
                    }

                    if (!CollectionUtils.isEmpty(homeList.getActivitys())){
                        List<ActivityBean> activityBeanList =  homeList.getActivitys();
                        homeData.setActivityData(activityBeanList);
                        for(ActivityBean activityBean: activityBeanList){
                            //暂时将集赞剔除
                            if(activityBean.getName().equals("金泽生态园—当草莓遇上蘑菇") || activityBean.getName().equals("春节特惠")){
                                continue;
                            }
                            ClassificationBean classificationBean = new ClassificationBean();
                            classificationBean.setActivityId(activityBean.getId()+"");
                            classificationBean.setActivityName(activityBean.getName());
                            classificationBean.setPic(activityBean.getPic());
                            classificationBean.setType("ACTIVITY");
                            classificationBeanList.add(classificationBean);
                        }
                    }



                    if(!CollectionUtils.isEmpty(classificationBeanList)){
                        classificationListBean.setClassificationBeanList(classificationBeanList);
                        homeData.setClassificationData(classificationListBean);
                    }

                    if (!CollectionUtils.isEmpty(homeList.getDistributors())){
                        homeData.setDistributorData(homeList.getDistributors());
                    }
                    if(!CollectionUtils.isEmpty(homeList.getSpecialEventsBean())){
                        homeData.setSpecialEventsBean(homeList.getSpecialEventsBean());
                    }
                    if(homeList.getTodayhotsearch() != null){
                        if(!CollectionUtils.isEmpty(homeList.getTodayhotsearch().getList())){
                            homeData.setTodayHotSearchBeanListBean(homeList.getTodayhotsearch());
                        }

                    }
                }
                result.setData(homeData);
                result.setStatus(200);
                result.setMessage("请求成功！");
                return result;
            }
        });

        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public void getCompanyData(HttpObserver<List<CompanyBean>> observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Observable<HttpResult<List<CompanyBean>>> observable = HttpClient.getApiService().getCompany();
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }

    public static class HomeData{
        //首页分类,两行横向滑动
        private ClassificationListBean classificationData;
        private List<RecommendBean> bannerData;
        private List<SpecialEventsBean> specialEventsBean;
        private List<ModularBean> modularData;
        private List<ActivityBean> activityData;
        private List<DistributorBean> distributorData;
        private TodayHotSearchBeanListBean todayHotSearchBeanListBean;

        public ClassificationListBean getClassificationData() {
            return classificationData;
        }

        public void setClassificationData(ClassificationListBean classificationData) {
            this.classificationData = classificationData;
        }

        public TodayHotSearchBeanListBean getTodayHotSearchBeanListBean() {
            return todayHotSearchBeanListBean;
        }

        public void setTodayHotSearchBeanListBean(TodayHotSearchBeanListBean todayHotSearchBeanListBean) {
            this.todayHotSearchBeanListBean = todayHotSearchBeanListBean;
        }

        public List<RecommendBean> getBannerData() {
            return bannerData;
        }

        public void setBannerData(List<RecommendBean> bannerData) {
            this.bannerData = bannerData;
        }

        public List<ModularBean> getModularData() {
            return modularData;
        }

        public List<SpecialEventsBean> getSpecialEventsBean() {
            return specialEventsBean;
        }

        public void setSpecialEventsBean(List<SpecialEventsBean> specialEventsBean) {
            this.specialEventsBean = specialEventsBean;
        }

        public void setModularData(List<ModularBean> modularData) {
            this.modularData = modularData;
        }

        public List<ActivityBean> getActivityData() {
            return activityData;
        }

        public void setActivityData(List<ActivityBean> activityData) {
            this.activityData = activityData;
        }

        public List<DistributorBean> getDistributorData() {
            return distributorData;
        }

        public void setDistributorData(List<DistributorBean> distributorData) {
            this.distributorData = distributorData;
        }
    }

    public void collectProduct(String productId,HttpObserver observer,PublishSubject<LifeCycleEvent> lifeCycleSubject){
        Map<String,String> params =new HashMap<>();
        params.put("userId", CommonUtils.getUserId());
        params.put("productId",productId);
        Observable<HttpResult> observable = HttpClient.getApiService().collectProduct(params);
        RetrofitUtil.composeToSubscribe(observable,observer,lifeCycleSubject);
    }
}
