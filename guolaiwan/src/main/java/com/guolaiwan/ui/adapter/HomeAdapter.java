package com.guolaiwan.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.holder.Holder;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.BaseFragment;
import com.cgx.library.base.BaseViewHolder;
import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.AppUtils;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.FrescoUtil;
import com.cgx.library.utils.StringUtils;
import com.cgx.library.utils.ToastUtils;
import com.cgx.library.utils.log.LogUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import app.guolaiwan.com.guolaiwan.R;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import mabeijianxi.camera.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.guolaiwan.App;
import com.guolaiwan.bean.ActivityBean;
import com.guolaiwan.bean.ClassificationBean;
import com.guolaiwan.bean.ClassificationListBean;
import com.guolaiwan.bean.DistributorBean;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.MikyouCountDownTimer;
import com.guolaiwan.bean.ModularBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.SpecialEventsBean;
import com.guolaiwan.bean.TodayHotSearchBean;
import com.guolaiwan.bean.TodayHotSearchBeanListBean;
import com.guolaiwan.constant.Constant;
import com.guolaiwan.constant.ItemType;
import com.guolaiwan.constant.UrlConstant;
import com.guolaiwan.model.MerchantInfoModel;
import com.guolaiwan.model.ProductInfoModel;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.presenter.HomePresenter;
import com.guolaiwan.ui.activity.CategoryListActivity;
import com.guolaiwan.ui.activity.ImageBrowserActivity;
import com.guolaiwan.ui.activity.LoginActivity;
import com.guolaiwan.ui.activity.MainActivity;
import com.guolaiwan.ui.activity.MerchantInfoActivity;
import com.guolaiwan.ui.activity.ProductInfoActivity;
import com.guolaiwan.ui.iview.CategoryView;
import com.guolaiwan.ui.widget.AutoGridLayoutManager;
import com.guolaiwan.ui.widget.HomeClassificationGridView;
import com.guolaiwan.ui.widget.PageRecyclerView;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.TimerUtils;
import com.sina.weibo.sdk.constant.WBConstants;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/9
 * 描述:
 */

public class HomeAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder> {

    private FragmentActivity mContext;
    private String mType;
    public static String TYPE_FRAGMENT = "FRAGMENT";
    public static String TYPE_ACTIVITY = "ACTIVITY";
    private PageRecyclerView.PageAdapter mAdapter;

    public HomeAdapter(FragmentActivity context,List<MultiItemEntity> data,String type) {
        super(data);
        this.mContext = context;
        this.mType = type;
        addItemType(ItemType.ITEM_TYPE_CLASSIFICATION,R.layout.item_home_classification);
        addItemType(ItemType.ITEM_TYPE_MERCHANT_REC,R.layout.item_category_recommand);
        addItemType(ItemType.ITEM_TYPE_MERCHANT,R.layout.item_category_list);
        addItemType(ItemType.ITEM_TYPE_PRODUCT_REC,R.layout.item_category_recommand);
        addItemType(ItemType.ITEM_TYPE_PRODUCT,R.layout.item_category_list);
        addItemType(ItemType.ITEM_TYPE_DISTRIBUTOR_REC,R.layout.item_category_recommand);
        addItemType(ItemType.ITEM_TYPE_DISTRIBUTOR,R.layout.item_category_list);
        addItemType(ItemType.ITEM_TYPE_SPECIAL_EVENT,R.layout.item_home_special_events);
        addItemType(ItemType.ITEM_TYPE_TODAY_HOT_SEARCH,R.layout.item_today_hot_search);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MultiItemEntity item) {
        switch (item.getItemType()){
            case ItemType.ITEM_TYPE_CLASSIFICATION:
                if(item instanceof ClassificationListBean){
                    List<ClassificationBean> classificationBeanList = ((ClassificationListBean) item).getClassificationBeanList();
                    PageRecyclerView pageRecyclerView = helper.getView(R.id.prv_home_class);
                    pageRecyclerView.setPageSize(2,5);
                    mAdapter = pageRecyclerView.new PageAdapter(classificationBeanList, new PageRecyclerView.CallBack() {
                        @Override
                        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_modular, parent, false);
                            return new MyHolder(view);
                        }

                        @Override
                        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                            ClassificationBean classificationBean = classificationBeanList.get(position);

                            if(classificationBean.getType().equals("MODULAR")){
                                ((MyHolder)holder).mHomeClassLl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CategoryListActivity.launch(mContext, CategoryView.TYPE_MODU, classificationBean.getModularCode(),classificationBean.getModularName());
                                    }
                                });
                                ((MyHolder)holder).mImgSdv.setImageURI(classificationBean.getModularPic());
                                ((MyHolder)holder).mTextTv.setText(classificationBean.getModularName());
                            }

                            if(classificationBean.getType().equals("ACTIVITY")){
                                ((MyHolder)holder).mHomeClassLl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CategoryListActivity.launch(mContext, CategoryView.TYPE_ACT, classificationBean.getActivityId() + "",classificationBean.getActivityName());
                                    }
                                });
                                ((MyHolder)holder).mImgSdv.setImageURI(classificationBean.getPic());
                                ((MyHolder)holder).mTextTv.setText(classificationBean.getActivityName());
                            }
                        }
                    });
                    pageRecyclerView.setAdapter(mAdapter);
                    helper.setIsRecyclable(false);
                }
                break;

            case ItemType.ITEM_TYPE_MERCHANT_REC:
                if (item instanceof MerchantBean){
                    if(mType.equals(TYPE_FRAGMENT)){
                        helper.setVisible(R.id.class_title_rl,true);
                        helper.setText(R.id.moduler_name_tv,"—" + ((MerchantBean) item).getModularName() + "—");
                        helper.getView(R.id.class_title_rl).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CategoryListActivity.launch(mContext, CategoryView.TYPE_MODU, ((MerchantBean) item).getModularCode(),((MerchantBean) item).getModularName());
                            }
                        });

                        helper.getView(R.id.content_ll).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MerchantInfoActivity.launch(mContext,((MerchantBean) item).getId()+"",MerchantInfoModel.TYPE_MERCHANT);
                            }
                        });

                    }

                    if(mType.equals(TYPE_ACTIVITY)){
                        helper.setGone(R.id.class_title_rl,false);
                    }

                    helper.getView(R.id.colection_tv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(CommonUtils.isLogin()){
                                //已登录直接收藏
                                collectMerchant(((MerchantBean) item).getId()+"");
                            }else {
                                //未登录跳转登录界面
                                LoginActivity.launch(mContext);
                            }
                        }
                    });
                    helper.setText(R.id.tv_name,((MerchantBean) item).getShopName());
                    String address = ((MerchantBean) item).getShopAddress();
                    String distance = CommonUtils.calculateLineDistance(((MerchantBean) item).getShopLatitude(),((MerchantBean) item).getShopLongitude());
                    helper.setText(R.id.tv_location, address);
                    helper.setText(R.id.tv_distance, distance);
                    String pics = ((MerchantBean) item).getShopMpic();
                    String[] picArray = pics.split(",");
                    if (picArray.length>=3){
                        helper.setImageUrl(R.id.iv_img0,picArray[0]);
                        helper.setImageUrl(R.id.iv_img1,picArray[1]);
                        helper.setImageUrl(R.id.iv_img2,picArray[2]);
                    }else {
                        helper.setImageResource(R.id.iv_img0,R.mipmap.ic_image_load);
                        helper.setImageResource(R.id.iv_img1,R.mipmap.ic_image_load);
                        helper.setImageResource(R.id.iv_img2,R.mipmap.ic_image_load);
                    }
                    helper.setText(R.id.tv_desc,"评分："+ ((MerchantBean) item).getShopScore());
                    helper.setGone(R.id.price_ll,false);
                    //helper.setText(R.id.tv_price,((MerchantBean) item).getAveragePrice()+"/人");
                }
                break;
            case ItemType.ITEM_TYPE_MERCHANT:
                if (item instanceof MerchantBean){
                    helper.setText(R.id.tv_name,((MerchantBean) item).getShopName());
                    String address = ((MerchantBean) item).getShopAddress();
                    String distance = CommonUtils.calculateLineDistance(((MerchantBean) item).getShopLatitude(),((MerchantBean) item).getShopLongitude());
                    helper.setText(R.id.tv_location,address);
                    helper.setText(R.id.tv_distance,distance);
                    //helper.setImageUrl(R.id.iv_img,((MerchantBean) item).getShopPic());
                    helper.setImageUrl(R.id.iv_img,((MerchantBean) item).getShopHeading());
                    helper.setText(R.id.tv_desc,"评分："+ ((MerchantBean) item).getShopScore());
                    helper.setGone(R.id.price_ll,false);
                    //helper.setText(R.id.tv_price,((MerchantBean) item).getAveragePrice()+"/人");
                }
                break;
            case ItemType.ITEM_TYPE_PRODUCT_REC:
                if (item instanceof ProductBean){
                    if(mType.equals(TYPE_FRAGMENT)){
                        if(((ProductBean) item).isYiYuanGou() == true){
                            String timeOnServer = ((ProductBean) item).getNowDate();
                            String cutDownTimeStartDate = ((ProductBean) item).getCutDownTimeStartDate();
                            String cutDownTimeEndDate = ((ProductBean) item).getCutDownTimeEndDate();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date currentDate = null;
                            Date startDate = null;
                            Date endDate = null;
                            try {
                                currentDate = sdf.parse(timeOnServer);
                                startDate = sdf.parse(cutDownTimeStartDate);
                                endDate = sdf.parse(cutDownTimeEndDate);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //当前时间和活动开始时间时间差
                            long timePoorForStart = currentDate.getTime() - startDate.getTime();
                            //当前时间和活动结束时间时间差
                            long timePoorForEnd = currentDate.getTime() - endDate.getTime();

                            if(timePoorForStart < 0){
                                    //一元购活动还未开始,进行倒计时
                                    helper.setText(R.id.daijishi_title,"距离开始还有:");
                                    TextView textView = TimerUtils.getTimer(TimerUtils.DEFAULT_STYLE, mContext, -timePoorForStart, TimerUtils.TIME_STYLE_TWO, 0).setCountDownTimerCallBack(new MikyouCountDownTimer.CountDownTimerCallBack() {
                                        @Override
                                        public void onTimeCountDownFinish() {
                                        }
                                    }).getmDateTv();
                                    RelativeLayout mTimerCountRl = helper.getView(R.id.daojishi_rl);
                                    mTimerCountRl.removeAllViews();
                                    mTimerCountRl.addView(textView);
                            }

                            if(timePoorForStart > 0 && timePoorForEnd < 0){
                                //一元购活动正在进行中
                                helper.setText(R.id.daijishi_title,"活动正在进行中");
                            }

                            if(timePoorForEnd > 0){
                                //一元购活动正在进行中
                                helper.setText(R.id.daijishi_title,"活动已结束");
                            }

                            helper.setVisible(R.id.class_yiyuangou_title_rl,true);
                            helper.getView(R.id.class_yiyuangou_title_rl).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CategoryListActivity.launch(mContext, CategoryView.TYPE_ACT, ((ProductBean) item).getActivityId() + "",((ProductBean) item).getActivityName());
                                }
                            });

                            helper.getView(R.id.content_ll).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ProductInfoActivity.launch(mContext,((ProductBean) item).getId()+"", Constant.ACTIVITY_ONE_YAUN_BUY);
                                }
                            });
                        }else {
                            helper.setVisible(R.id.class_title_rl,true);
                            helper.setText(R.id.moduler_name_tv,"—" + ((ProductBean) item).getActivityName()+ "—");
                            helper.getView(R.id.class_title_rl).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CategoryListActivity.launch(mContext, CategoryView.TYPE_ACT, ((ProductBean) item).getActivityId() + "",((ProductBean) item).getActivityName());
                                }
                            });
                            helper.getView(R.id.content_ll).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ProductInfoActivity.launch(mContext,((ProductBean) item).getId()+"");
                                }
                            });
                        }
                    }


                    if(mType.equals(TYPE_ACTIVITY)){
                        helper.setGone(R.id.class_title_rl,false);
                        helper.setGone(R.id.class_yiyuangou_title_rl,false);
                    }

                    helper.getView(R.id.colection_tv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(CommonUtils.isLogin()){
                                //已登录直接收藏
                                collectProduct(((ProductBean) item).getId() + "");
                            }else {
                                //未登录跳转登录界面
                                LoginActivity.launch(mContext);
                            }
                        }
                    });
                    helper.setText(R.id.tv_name,((ProductBean) item).getProductName());
                    String address = ((ProductBean) item).getProductMerchantName();
                    String distance = CommonUtils.calculateLineDistance(((ProductBean) item).getShopLatitude(),((ProductBean) item).getShopLongitude());
                    helper.setText(R.id.tv_location, address);
                    helper.setText(R.id.tv_distance, distance);
                    helper.setText(R.id.tv_price,((ProductBean) item).getProductPricesStr());
                    String pics = ((ProductBean) item).getProductMorePic();
                    String[] picArray = pics.split(",");
                    if (picArray.length>=3){
                        helper.setImageUrl(R.id.iv_img0,picArray[0]);
                        helper.setImageUrl(R.id.iv_img1,picArray[1]);
                        helper.setImageUrl(R.id.iv_img2,picArray[2]);
                    }else {
                        helper.setImageResource(R.id.iv_img0,R.mipmap.ic_image_load);
                        helper.setImageResource(R.id.iv_img1,R.mipmap.ic_image_load);
                        helper.setImageResource(R.id.iv_img2,R.mipmap.ic_image_load);
                    }
                    helper.setText(R.id.tv_desc,"评分："+ ((ProductBean) item).getProductScore());
                }
                break;
            case ItemType.ITEM_TYPE_PRODUCT:
                if (item instanceof ProductBean){
                    helper.setText(R.id.tv_name,((ProductBean) item).getProductName());
                    helper.setText(R.id.tv_location,((ProductBean) item).getProductMerchantName());
                    helper.setImageUrl(R.id.iv_img,((ProductBean) item).getProductShowPic());
                    String distance = CommonUtils.calculateLineDistance(((ProductBean) item).getShopLatitude(),((ProductBean) item).getShopLongitude());
                    helper.setText(R.id.tv_distance,distance);
                    helper.setText(R.id.tv_desc,"评分："+ ((ProductBean) item).getProductScore());
                    helper.setText(R.id.tv_price,((ProductBean) item).getProductPricesStr());
                    helper.setGone(R.id.tv_location,false);
                    helper.setGone(R.id.tv_distance,false);
                }
                break;
            case ItemType.ITEM_TYPE_DISTRIBUTOR_REC:
                if (item instanceof DistributorBean){
                    helper.setText(R.id.tv_name,((DistributorBean) item).getShopName());
                    String address = ((DistributorBean) item).getShopAddress();
                    String distance = CommonUtils.calculateLineDistance(((DistributorBean) item).getShopLatitude(),((DistributorBean) item).getShopLongitude());
                    helper.setText(R.id.tv_location, address);
                    helper.setText(R.id.tv_distance, distance);
                    String pics = ((DistributorBean) item).getShopMpic();
                    String[] picArray = pics.split(",");
                    if (picArray.length>=3){
                        helper.setImageUrl(R.id.iv_img0,picArray[0]);
                        helper.setImageUrl(R.id.iv_img1,picArray[1]);
                        helper.setImageUrl(R.id.iv_img2,picArray[2]);
                    }else {
                        helper.setImageResource(R.id.iv_img0,R.mipmap.ic_image_load);
                        helper.setImageResource(R.id.iv_img1,R.mipmap.ic_image_load);
                        helper.setImageResource(R.id.iv_img2,R.mipmap.ic_image_load);
                    }
                    helper.setText(R.id.tv_desc,"评分："+ ((DistributorBean) item).getDistributorScore());
                    helper.setText(R.id.tv_price,((DistributorBean) item).getAveragePrice()+"/人");
                }
                break;
            case ItemType.ITEM_TYPE_DISTRIBUTOR:
                if (item instanceof DistributorBean){
                    helper.setText(R.id.tv_name,((DistributorBean) item).getShopName());
                    String address = ((DistributorBean) item).getShopAddress();
                    String distance = CommonUtils.calculateLineDistance(((DistributorBean) item).getShopLatitude(),((DistributorBean) item).getShopLongitude());
                    helper.setText(R.id.tv_location,address);
                    helper.setText(R.id.tv_distance,distance);
                    helper.setImageUrl(R.id.iv_img,((DistributorBean) item).getShopPic());
                    helper.setText(R.id.tv_desc,"评分："+ ((DistributorBean) item).getDistributorScore());
                    helper.setText(R.id.tv_price,((DistributorBean) item).getAveragePrice()+"/人");
                }
                break;

            case ItemType.ITEM_TYPE_SPECIAL_EVENT:
                if(item instanceof SpecialEventsBean){
                    SimpleDraweeView simpleDraweeView = helper.getView(R.id.iv_img);
                    if(!StringUtils.isEmpty(((SpecialEventsBean) item).getId())){
                        simpleDraweeView.setVisibility(View.VISIBLE);
                        simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //由于SPECIAL_EVENT接口未完成暂时写死
                                //点击首页动图进入年货节
                                CategoryListActivity.launch(mContext, CategoryView.TYPE_ACT,((SpecialEventsBean) item).getId(),((SpecialEventsBean) item).getTitle());
                            }
                        });
                        FrescoUtil.getInstance().loadNetImage(simpleDraweeView,UrlConstant.GUIDE_SPOT_IMAGE_URL + ((SpecialEventsBean) item).getPic());
                        helper.setText(R.id.guolaiwan_toutiao_title_tv,((SpecialEventsBean) item).getTitle());
                        helper.getView(R.id.guolaiwan_toutiao_watch_more_tv).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ToastUtils.showToast(mContext,"温馨提示:程序猿拼命开发中，敬请期待。");
                            }
                        });
                    }else {
                        simpleDraweeView.setVisibility(View.GONE);
                        helper.setGone(R.id.guolaiwan_toutiao_ll,false);
                    }
                }
                break;

            case ItemType.ITEM_TYPE_TODAY_HOT_SEARCH:
                if(item instanceof TodayHotSearchBeanListBean){
                    List<TodayHotSearchBean> todayHotSearchBeanList = ((TodayHotSearchBeanListBean) item).getList();
                    TodayHotSearchBean todayHotSearchBean1 = todayHotSearchBeanList.get(0);
                    TodayHotSearchBean todayHotSearchBean2 = todayHotSearchBeanList.get(1);
                    TodayHotSearchBean todayHotSearchBean3 = todayHotSearchBeanList.get(2);
                    TodayHotSearchBean todayHotSearchBean4 = todayHotSearchBeanList.get(3);
                    TodayHotSearchBean todayHotSearchBean5 = todayHotSearchBeanList.get(4);

                    helper.setText(R.id.tv_title1,todayHotSearchBean1.getTitle());
                    helper.setText(R.id.tv_title2,todayHotSearchBean2.getTitle());
                    helper.setText(R.id.tv_title3,todayHotSearchBean3.getTitle());
                    helper.setText(R.id.tv_title4,todayHotSearchBean4.getTitle());
                    helper.setText(R.id.tv_title5,todayHotSearchBean5.getTitle());

                    helper.setImageUrl(R.id.iv_img1,todayHotSearchBean1.getPic());
                    helper.setImageUrl(R.id.iv_img2,todayHotSearchBean2.getPic());
                    helper.setImageUrl(R.id.iv_img3,todayHotSearchBean3.getPic());
                    helper.setImageUrl(R.id.iv_img4,todayHotSearchBean4.getPic());
                    helper.setImageUrl(R.id.iv_img5,todayHotSearchBean5.getPic());
                }
                break;
        }
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (item.getItemType() == ItemType.ITEM_TYPE_MERCHANT || item.getItemType() == ItemType.ITEM_TYPE_MERCHANT_REC ){
                    if (item instanceof MerchantBean){
                        MerchantInfoActivity.launch(mContext,((MerchantBean) item).getId()+"",MerchantInfoModel.TYPE_MERCHANT);
                    }
                } else if (item.getItemType() == ItemType.ITEM_TYPE_DISTRIBUTOR || item.getItemType() == ItemType.ITEM_TYPE_DISTRIBUTOR_REC ){
                    if (item instanceof DistributorBean){
                        MerchantInfoActivity.launch(mContext,((DistributorBean) item).getId()+"",MerchantInfoModel.TYPE_DISTRIBUTOR);
                    }
                } else if (item.getItemType() == ItemType.ITEM_TYPE_PRODUCT || item.getItemType() == ItemType.ITEM_TYPE_PRODUCT_REC ){
                    if (item instanceof ProductBean){
                        ProductInfoActivity.launch(mContext,((ProductBean) item).getId()+"");
                    }
                }
            }
        });
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public LinearLayout mHomeClassLl;
        public SimpleDraweeView mImgSdv;
        public TextView mTextTv;

        public MyHolder(View itemView) {
            super(itemView);
            mHomeClassLl = itemView.findViewById(R.id.ll_home_class);
            mImgSdv = itemView.findViewById(R.id.iv_img);
            mTextTv = itemView.findViewById(R.id.tv_text);
        }
    }

    /*收藏商品*/
    public void collectProduct(String productId){
        Map<String,String> params =new HashMap<>();
        params.put("userId", CommonUtils.getUserId());
        params.put("productId",productId);
        Observable<HttpResult> observable = HttpClient.getApiService().collectProduct(params);
        RetrofitUtil.composeToSubscribe(observable,new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.error(errCode,errMessage);
            }
            @Override
            public void onComplete() {}
        },((BaseActivity)mContext).getLifeSubject());
    }

    /*收藏店铺*/
    public void collectMerchant(String merchantId){
        Map<String,String> params =new HashMap<>();
        params.put("userId", CommonUtils.getUserId());
        params.put("merId",merchantId);
        Observable<HttpResult> observable = HttpClient.getApiService().collectMerchant(params);
        RetrofitUtil.composeToSubscribe(observable,new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.error(errCode,errMessage);
            }
            @Override
            public void onComplete() {}
        },((BaseActivity)mContext).getLifeSubject());
    }

}
