package com.guolaiwan.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.cgx.library.base.BaseFragment;
import com.cgx.library.utils.SPUtils;
import com.cgx.library.utils.log.LogUtil;
import com.guolaiwan.bean.Child;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.UserBean;
import com.guolaiwan.bean.VoiceBean;
import com.guolaiwan.constant.Constant;
import com.guolaiwan.presenter.GuidePresenter;
import com.guolaiwan.ui.activity.GuideActivity;
import com.guolaiwan.ui.activity.LoginActivity;
import com.guolaiwan.ui.activity.SearchActivity;
import com.guolaiwan.ui.iview.GuideView;
import com.guolaiwan.utils.CommonUtils;

import app.guolaiwan.com.guolaiwan.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.guolaiwan.ui.activity.GuideActivity.convertViewToBitmap;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/26
 * 描述: 导览
 */

public class GuideFragment extends BaseFragment implements GuideView, DistrictSearch.OnDistrictSearchListener, AMap.OnMarkerClickListener, INaviInfoCallback {

    @BindView(R.id.map_view)
    public TextureMapView mMapView;
    @BindView(R.id.shopNameTv)
    TextView mShopNameTv;
    @BindView(R.id.tv_distance)
    TextView mDistanceTv;
    @BindView(R.id.tv_location)
    TextView mLocationTv;
    @BindView(R.id.layout_level)
    LinearLayout mLevelLayout;
    @BindView(R.id.tv_category)
    TextView mCategoryTv;
    @BindView(R.id.tv_open_time)
    TextView mOpenTimeTv;
    @BindView(R.id.layout_info)
    LinearLayout mInfoView;
    @BindView(R.id.guide_layout)
    LinearLayout mGuideTv;
    @BindView(R.id.tab_layout)
    TabLayout mTablayout;
    private AMap mAMap;
    private GuidePresenter mPresenter;
    private List<Marker> mMarkerList;
    MerchantBean merchantBean;

    public static GuideFragment newInstance() {
        return new GuideFragment();
    }

    @OnClick({R.id.tv_guide, R.id.tv_navigation, R.id.tv_search})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_guide:
                if(CommonUtils.isLogin()){
                    GuideActivity.launch(getContext(), merchantBean.getId() + "", merchantBean.getShopLongitude(), merchantBean.getShopLatitude());
                }else {
                    LoginActivity.launch(getContext());
                }
                break;
            case R.id.tv_navigation:
                double latitude = Double.valueOf(merchantBean.getShopLatitude());
                double longitude = Double.valueOf(merchantBean.getShopLongitude());
                Poi end = new Poi(merchantBean.getShopName(), new LatLng(latitude, longitude), "");
                AmapNaviPage.getInstance().showRouteActivity(getContext(), new AmapNaviParams(null, null, end, AmapNaviType.DRIVER), this);
                break;
            case R.id.tv_search:
                SearchActivity.launch(getContext());
                break;
        }
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.fragment_guide, parent, false);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
    }

    @Override
    protected void initData() {
        mPresenter = new GuidePresenter(this);
        mMarkerList = new ArrayList<>();
    }

    @Override
    protected void initView() {
        mTablayout.addTab(mTablayout.newTab().setText("景点").setTag("0001"));
        mTablayout.addTab(mTablayout.newTab().setText("酒店").setTag("0002"));
        mTablayout.addTab(mTablayout.newTab().setText("特产").setTag("01"));
        mTablayout.addTab(mTablayout.newTab().setText("商城").setTag("0004"));
        mTablayout.addTab(mTablayout.newTab().setText("美食").setTag("0003"));
        mTablayout.addTab(mTablayout.newTab().setText("一元购").setTag("01112"));
        mTablayout.addTab(mTablayout.newTab().setText("生活服务").setTag("005"));
        mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(mInfoView != null){
                    if(mInfoView.getVisibility() == View.VISIBLE){
                        mInfoView.setVisibility(View.GONE);
                    }
                }
                mPresenter.getMerchants(SPUtils.getString("comCode", "0001"), (String) tab.getTag());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mPresenter.getMerchants(SPUtils.getString("comCode", "0001"), "0001");

    }

    @Override
    protected void initEvent() {}


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        DistrictSearch search = new DistrictSearch(getContext());
        DistrictSearchQuery query = new DistrictSearchQuery();
        String cityName = SPUtils.getString("cityName", "遵化市");
        query.setKeywords(cityName);//传入关键字
        query.setShowBoundary(true);//是否返回边界值
        search.setQuery(query);
        search.setOnDistrictSearchListener(this);//绑定监听器
        search.searchDistrictAnsy();//开始搜索
        mAMap.getUiSettings().setLogoBottomMargin(-100);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void addMarkers(List<MerchantBean> merchantList) {
        for (Marker marker : mMarkerList) {
            marker.remove();

        }
        mMarkerList.clear();

        View v = LayoutInflater.from(getContext()).inflate(R.layout.marker_layout, null);
        TextView textView = v.findViewById(R.id.text);
        ImageView simpleDraweeView = v.findViewById(R.id.iv_img);
        MerchantBean lastMerchantBean = null;
        for (MerchantBean merchantBean : merchantList) {
            if ((merchantBean.getShopLatitude() != null && !merchantBean.getShopLatitude().equals("")) && (merchantBean.getShopLongitude() != null && !merchantBean.getShopLongitude().equals(""))) {
                double latitude = Double.valueOf(merchantBean.getShopLatitude());
                double longitude = Double.valueOf(merchantBean.getShopLongitude());
                textView.setText(merchantBean.getShopName());
                int resId = R.mipmap.ic_launcher;
                switch (merchantBean.getModularCode()) {
                    case "01":
                        resId = R.mipmap.ic_launcher;
                        break;
                    case "01112":
                        resId = R.mipmap.ic_yiyuan;
                        break;
                    case "005":
                        resId = R.mipmap.ic_shenghuo;
                        break;
                    case "0004":
                        resId = R.mipmap.ic_shangcheng;
                        break;
                    case "0003":
                        resId = R.mipmap.ic_canyin;
                        break;
                    case "0002":
                        resId = R.mipmap.ic_jiudian;
                        break;
                    case "0001":
                        resId = R.mipmap.ic_jingqu;
                        break;

                }
                simpleDraweeView.setImageResource(resId);
                MarkerOptions options = new MarkerOptions();
                if (lastMerchantBean != null) {
                    if (merchantBean.getShopName().length() == lastMerchantBean.getShopName().length()) {
                        textView.setText(merchantBean.getShopName() + " ");
                        convertViewToBitmap(v);
                    }
                }
                textView.setText(merchantBean.getShopName());
                Bitmap bitmap = convertViewToBitmap(v);
                options.position(new LatLng(latitude, longitude)).draggable(false)
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                Marker marker = mAMap.addMarker(options);
                marker.setObject(merchantBean);
                mMarkerList.add(marker);
                lastMerchantBean = merchantBean;
            }
        }
        mAMap.setOnMarkerClickListener(this);
    }

    @Override
    public void addChildren(List<Child> children) {

    }


    @Override
    public void setSpotMarkerState(UserBean data) {
    }


    @Override
    public void setVoice(List<VoiceBean> voiceBeanList) {

    }

    @Override
    public void shareMyTravel(String url, String merchantName) {

    }

    @Override
    public void showRoad(List<Child> childOnRoad) {

    }

    @Override
    public void onDistrictSearched(DistrictResult districtResult) {
        LogUtil.d(TAG, districtResult);
        LatLonPoint latLongPoint = districtResult.getDistrict().get(0).getCenter();
        LatLng latLng = new LatLng(latLongPoint.getLatitude(), latLongPoint.getLongitude());
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 10, 0, 0));
        mAMap.moveCamera(mCameraUpdate);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (Marker marker1 : mMarkerList) {
            if (marker.equals(marker1)) {
                merchantBean = (MerchantBean) marker.getObject();
                mShopNameTv.setText(merchantBean.getShopName());
                mLocationTv.setText(merchantBean.getShopAddress());
                mDistanceTv.setText(CommonUtils.calculateLineDistance(merchantBean.getShopLatitude(), merchantBean.getShopLongitude()));
                mOpenTimeTv.setVisibility(View.GONE);
                for (int i = 0; i < merchantBean.getShopScore() / 2; i++) {
                    ((ImageView) mLevelLayout.getChildAt(i)).setImageResource(R.mipmap.star);
                }
                for (int i = 4 - merchantBean.getShopScore() / 2; i >= merchantBean.getShopScore() / 2; i--) {
                    ((ImageView) mLevelLayout.getChildAt(i)).setImageResource(R.mipmap.star_gray);
                }
                mCategoryTv.setVisibility(View.GONE);
                mInfoView.setVisibility(View.VISIBLE);
                if (merchantBean.getModularName().equals("景区景点") && merchantBean.getIsGuide().equals(Constant.SPOT_CAN_GUIDE)) {
                    mGuideTv.setVisibility(View.VISIBLE);
                } else {
                    mGuideTv.setVisibility(View.GONE);
                }

            }
        }
        return false;
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

}
