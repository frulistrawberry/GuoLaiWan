package com.guolaiwan.presenter;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.utils.CollectionUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.guolaiwan.bean.ActivityBean;
import com.guolaiwan.bean.ClassificationBean;
import com.guolaiwan.bean.ClassificationListBean;
import com.guolaiwan.bean.CompanyBean;
import com.guolaiwan.bean.DistributorBean;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.ModularBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.SpecialEventsBean;
import com.guolaiwan.bean.TodayHotSearchBean;
import com.guolaiwan.bean.TodayHotSearchBeanListBean;
import com.guolaiwan.model.HomeModel;
import com.guolaiwan.model.ProductInfoModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.HomeView;
import com.guolaiwan.utils.CommonUtils;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/10
 * 描述:
 */

public class HomePresenter extends BasePresenter<HomeView> {
    private HomeModel mModel;
    public HomePresenter(HomeView mIView) {
        super(mIView);
        mModel = new HomeModel();
    }

    public void loadData(String comCode){
        mIView.showLoading();
        mModel.getHomeData(comCode,new HttpObserver<HomeModel.HomeData>() {
            @Override
            public void onNext(String message, HomeModel.HomeData data) {
                if (data == null) {
                    mIView.showEmpty();
                    return;
                }
                if (!CollectionUtils.isEmpty(data.getBannerData())){
                    mIView.loadBanner(data.getBannerData());
                }
                List<MultiItemEntity> dataList = new ArrayList<>();
                //首页分类改为两行数据集
                ClassificationListBean classificationData = data.getClassificationData();
                List<ModularBean> modularData = data.getModularData();
                List<ActivityBean> activityData = data.getActivityData();
                TodayHotSearchBeanListBean todayHotSearchBeanListBean = data.getTodayHotSearchBeanListBean();
                List<SpecialEventsBean> specialEventsBean = data.getSpecialEventsBean();


                if(classificationData != null){
                    dataList.add(classificationData);
                }

                if (!CollectionUtils.isEmpty(specialEventsBean)){
                    dataList.addAll(specialEventsBean);
                }

                //各分类只多个活动只显示一元购
                if (!CollectionUtils.isEmpty(activityData)){
                    for (ActivityBean activity : activityData) {
                        List<ProductBean> productList = activity.getProducts();
                        if (!CollectionUtils.isEmpty(productList)) {
                            for (int i = 0; i < productList.size(); i++) {
                                ProductBean product = productList.get(i);
                                if(product.getActivityName() != null && !product.getActivityName().equals("") && product.getActivityName().equals("一元购")){
                                    //此处区分活动类型
                                    product.setYiYuanGou(true);
                                    product.setRec(i == 0);
                                    dataList.add(product);
                                }else {
                                    //目前只有一元购、集赞和春节活动
                                    //集赞接口未完成不显示、春节活动放到首页动图中
                                    //因此这里只显示一元购
                                    if(!product.getActivityName().equals("一元购")){
                                        continue;
                                    }else {
                                        product.setRec(i == 0);
                                        dataList.add(product);
                                    }
                                }
                            }
                        }
                    }
                }

                //数据接口没有暂时不显示
                //if (todayHotSearchBeanListBean != null){
                  //  dataList.add(todayHotSearchBeanListBean);
                //}

                if (!CollectionUtils.isEmpty(modularData)){
                    for (ModularBean modular : modularData) {
                        List<MerchantBean> merchantList = modular.getMerchants();
                        if (!CollectionUtils.isEmpty(merchantList)) {
                            for (int i = 0; i < merchantList.size(); i++) {
                                MerchantBean merchant = merchantList.get(i);
                                merchant.setRec(i == 0);
                                dataList.add(merchant);
                            }
                        }
                    }
                }

                List<DistributorBean> distributorData = data.getDistributorData();
                if (!CollectionUtils.isEmpty(distributorData)){
                    for (int i = 0; i < distributorData.size(); i++) {
                        DistributorBean distributor = distributorData.get(i);
                        distributor.setRec(i == 0);
                        dataList.add(distributor);
                    }
                }

                if (CollectionUtils.isEmpty(dataList)){
                    mIView.showEmpty();
                } else {
                    mIView.loadData(dataList);
                }
                mIView.showContent();
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.showError();
            }

            @Override
            public void onComplete() {}

        },mIView.getLifeSubject());
    }

    public void loadCompany(){
        mModel.getCompanyData(new HttpObserver<List<CompanyBean>>() {
            @Override
            public void onNext(String message, List<CompanyBean> data) {
                mIView.loadPop(data);
            }

            @Override
            public void onError(int errCode, String errMessage) {

            }

            @Override
            public void onComplete() {

            }
        },mIView.getLifeSubject());
    }

}
