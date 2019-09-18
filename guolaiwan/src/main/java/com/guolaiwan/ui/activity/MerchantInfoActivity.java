package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.ActivityUtils;
import com.cgx.library.utils.IntentUtils;
import com.cgx.library.utils.PhoneUtils;
import com.cgx.library.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import app.guolaiwan.com.guolaiwan.R;

import com.guolaiwan.bean.LoginUserEvent;
import com.guolaiwan.bean.MerchantInfoBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.constant.Constant;
import com.guolaiwan.presenter.MerchantInfoPresenter;
import com.guolaiwan.ui.adapter.MerchantProductAdapter;
import com.guolaiwan.ui.iview.MerchantInfoView;
import com.guolaiwan.utils.AMapUtils;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/20
 * 描述: 商家详情页
 */
public class MerchantInfoActivity extends BaseActivity implements MerchantInfoView, INaviInfoCallback {

    @BindView(R.id.layout_ptr)
    public PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;
    @BindView(R.id.layout_book_table)
    public LinearLayout mBookTableLayout;
    @BindView(R.id.layout_guide)
    public LinearLayout mGuideLayout;
    @BindView(R.id.layout_navigation)
    public LinearLayout mNavigationLayout;
    @BindView(R.id.iv_collect)
    public ImageView mCollectIv;
    public View mHeaderView;
    public ConvenientBanner<String> mBannerView;
    public TextView mShopNameTv;
    public TextView mLocationTv;
    public LinearLayout mLevelLayout;
    public TextView mPriceTv;

    private MerchantProductAdapter mAdapter;
    private String mMerchantId;
    private int mCurrentPage = 1;
    private MerchantInfoPresenter mPresenter;
    private int mType;
    MerchantInfoBean mProductInfo;
    private String phoneNum;

    public static void launch(Context context, String merchantId, int type) {
        Intent intent = new Intent(context, MerchantInfoActivity.class);
        intent.putExtra("merchantId", merchantId);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @OnClick({R.id.tv_guide, R.id.tv_navigation,R.id.tv_booktable,R.id.iv_collect})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_guide:
                if(CommonUtils.isLogin()){
                    GuideActivity.launch(this, mMerchantId, merchantInfo.getShopLongitude(), merchantInfo.getShopLatitude());
                }else {
                    LoginActivity.launch(getContext());
                }
                break;
            case R.id.tv_navigation:
                double latitude = Double.valueOf(mProductInfo.getShopLatitude());
                double longitude = Double.valueOf(mProductInfo.getShopLongitude());
                Poi end = new Poi(mProductInfo.getShopName(), new LatLng(latitude, longitude), "");
                AmapNaviPage.getInstance().showRouteActivity(this, new AmapNaviParams(null, null, end, AmapNaviType.DRIVER), this);
                break;

            case R.id.tv_booktable:
                break;
            case R.id.iv_collect:
                //调用收藏方法
                if(!CommonUtils.isLogin()){
                    LoginActivity.launch(this);
                }else {
                    mPresenter.collectMerchant(mProductInfo.getId()+"");
                }
                break;
        }
    }

    @Override
    public void showContent() {
        super.showContent();
        if (mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
        }
        if (mAdapter.isLoading())
            mAdapter.loadMoreComplete();
    }

    @Override
    public void showLoading() {
        if (!mPtrLayout.isRefreshing() && mCurrentPage == 1) {
            mAdapter.setEmptyView(ViewUtils.getLoadingView(getContext()));
        }
    }

    @Override
    public void showEmpty() {
        if (mCurrentPage == 1)
            mAdapter.setEmptyView(ViewUtils
                    .getEmptyView(getContext(), R.mipmap.no_project, "暂无数据"));
        else {
            mAdapter.loadMoreEnd(false);
        }
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
    }

    @Override
    public void showError() {
        mAdapter.setEmptyView(ViewUtils.getErrorView(getContext()));
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
        if (mCurrentPage > 1) {
            mAdapter.loadMoreFail();
        }
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().showBack().setRightImage(R.mipmap.icon_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.launch(getContext());
            }
        }).show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_merchant_info);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.layout_merchant_info, mRecyclerView, false);
        mBannerView = mHeaderView.findViewById(R.id.banner);
        mShopNameTv = mHeaderView.findViewById(R.id.tv_shop_name);
        mLocationTv = mHeaderView.findViewById(R.id.tv_location);
        mHeaderView.findViewById(R.id.iv_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(phoneNum))
                    return;
                PhoneUtils.call(MerchantInfoActivity.this, phoneNum);
            }
        });
        mPriceTv = mHeaderView.findViewById(R.id.tv_price);
        mLevelLayout = mHeaderView.findViewById(R.id.layout_level);
        mBannerView = mHeaderView.findViewById(R.id.banner);
        mHeaderView.findViewById(R.id.btn_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomPayActivity.launch(getContext(), mMerchantId);
            }
        });
        mAdapter.addHeaderView(mHeaderView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(true);
        mPresenter.refreshData(mType, mMerchantId);
    }

    @Override
    protected void initData() {
        mMerchantId = getIntent().getStringExtra("merchantId");
        mType = getIntent().getIntExtra("type", 1);
        mPresenter = new MerchantInfoPresenter(this);
        mAdapter = new MerchantProductAdapter(this);
        mAdapter.setHeaderAndEmpty(true);
    }

    @Override
    protected void initEvent() {
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.refreshData(mType, mMerchantId);
                mAdapter.setEnableLoadMore(true);
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mCurrentPage++;
                mPresenter.loadProductList(mType, mCurrentPage, mMerchantId);
            }
        }, mRecyclerView);
    }

    MerchantInfoBean merchantInfo;

    @Override
    public void loadMerchantInfo(MerchantInfoBean merchantInfo) {
        this.merchantInfo = merchantInfo;
        if(merchantInfo.getShopTel() != null){
            phoneNum = merchantInfo.getShopTel();
        }
        String shopMpic = merchantInfo.getShopMpic();
        if (StringUtils.isEmpty(shopMpic)) {
            mBannerView.setVisibility(View.INVISIBLE);
        } else {
            String[] pics = shopMpic.split(",");
            ViewUtils.loadMerchantInfoBanner(mBannerView, pics);
        }
        mShopNameTv.setText(merchantInfo.getShopName());
        mPriceTv.setText(String.format("￥%s/人", merchantInfo.getAveragePrice()));
        mLocationTv.setText(merchantInfo.getShopAddress());
        for (int i = 0; i < merchantInfo.getShopScore() / 2; i++) {
            ((ImageView) mLevelLayout.getChildAt(i)).setImageResource(R.mipmap.star);
        }
        for (int i = 4 - merchantInfo.getShopScore() / 2; i >= merchantInfo.getShopScore() / 2; i--) {
            ((ImageView) mLevelLayout.getChildAt(i)).setImageResource(R.mipmap.star_gray);
        }
        getTitleBar().setTitle(merchantInfo.getShopName());
        if (!StringUtils.isEmpty(merchantInfo.getBusiness()) && merchantInfo.getBusiness().equals("RESTAURANT")) {
            mBookTableLayout.setVisibility(View.VISIBLE);
        } else {
            mBookTableLayout.setVisibility(View.GONE);
        }

        if (!StringUtils.isEmpty(merchantInfo.getBusiness()) && merchantInfo.getBusiness().equals("SCENICSPOT") && merchantInfo.getIsGuide().equals(Constant.SPOT_CAN_GUIDE)) {
            mGuideLayout.setVisibility(View.VISIBLE);
        } else {
            mGuideLayout.setVisibility(View.GONE);
        }
        mNavigationLayout.setVisibility(View.VISIBLE);
        mProductInfo = merchantInfo;
    }

    @Override
    public void loadProductList(List<ProductBean> productList) {
        if (mCurrentPage == 1) {
            mAdapter.setNewData(productList);
        } else {
            mAdapter.addData(productList);
        }
    }

    @Override
    public void setCollectImageState() {
        mCollectIv.setImageResource(R.mipmap.ic_collect_white);
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    @Override
    public void onStrategyChanged(int i) {

    }

    @Override
    public View getCustomNaviBottomView() {
        return null;
    }

    @Override
    public View getCustomNaviView() {
        return null;
    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public View getCustomMiddleView() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
