package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.FileUtils;
import com.cgx.library.utils.ToastUtils;
import com.cgx.library.widget.TitleBar;
import com.cgx.library.widget.dialog.AlertDialog;
import com.google.gson.Gson;
import com.guolaiwan.App;
import com.guolaiwan.bean.Child;
import com.guolaiwan.bean.GuideSearchEvent;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.ShareBean;
import com.guolaiwan.bean.ShareContentBean;
import com.guolaiwan.bean.UserBean;
import com.guolaiwan.bean.VoiceBean;
import com.guolaiwan.bean.WeatherFree;
import com.guolaiwan.constant.Constant;
import com.guolaiwan.constant.UrlConstant;
import com.guolaiwan.presenter.GuidePresenter;
import com.guolaiwan.ui.iview.GuideView;
import com.guolaiwan.ui.widget.ListPop;
import com.guolaiwan.ui.widget.MyPleayer;
import com.guolaiwan.ui.widget.ShareDialog;
import com.guolaiwan.ui.widget.SingleChoiceDialog;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.FrameAnimation;
import com.leng.jbq.SportStepJsonUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.DoubleConsumer;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import cn.jzvd.JZMediaManager;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

import static com.guolaiwan.App.APP_FILE_PATH;

public class GuideActivity extends BaseActivity implements GuideView, AMap.OnMarkerClickListener, View.OnClickListener, AMap.InfoWindowAdapter, AMap.OnMyLocationChangeListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnSeekCompleteListener {

    //图例
    @BindView(R.id.ll_legend)
    LinearLayout mLegendLl;
    //人物和天气、文字详情、健康指数
    @BindView(R.id.iv_people)
    public ImageView mPeopleIv;
    @BindView(R.id.tv_weather)
    public TextView mWeatherTv;
    @BindView(R.id.sv_content)
    public ScrollView mContentSv;
    @BindView(R.id.tv_content)
    public TextView mContentTv;
    @BindView(R.id.rl_health_index)
    public RelativeLayout mHealthIndexRl;
    @BindView(R.id.tv_health)
    public TextView mHealthTv;
    //语言选择对话框
    @BindView(R.id.scd_voice)
    public SingleChoiceDialog mSingleChoiceDialog;
    //功能按钮
    @BindView(R.id.iv_voice)
    public ImageView mVoiceIv;
    @BindView(R.id.iv_remove_road)
    public ImageView mRemoveRoadIv;
    @BindView(R.id.iv_remove_record)
    public ImageView mRemoveRecordIv;
    @BindView(R.id.iv_health_index)
    public ImageView mHealthIndexIv;
    //地图相关
    @BindView(R.id.map_view)
    public TextureMapView mMapView;
    //语音插件
    public MyPleayer mJzvdStd;
    //语音插件控制器
    @BindView(R.id.ll_player_controller)
    public LinearLayout mPlayerControllerLl;
    @BindView(R.id.rl_player_controller_text_detail)
    public RelativeLayout mPlayerControllerTextDetailRl;
    @BindView(R.id.tv_player_controller_text_detail)
    public TextView mPlayerControllerTextDetailTv;
    @BindView(R.id.rl_player_controller_bt)
    public RelativeLayout mPlayerControllerRl;
    @BindView(R.id.iv_player_controller_bt)
    public ImageView mPlayerControllerBt;
    @BindView(R.id.tv_distination)
    public TextView mDistinationTv;
    @BindView(R.id.sb_progress)
    public SeekBar mProgressBarSb;
    //语音加载界面
    @BindView(R.id.rl_voice_loading)
    public RelativeLayout mVoiceLoadingRl;
    //分享Dialog
    ShareDialog mShareDialog;
    //第一次进入界面
    private static boolean mIsLaunchActivity = false;
    //播放讲解语音
    public static boolean mIsPlayingVoice = false;
    //切换人物
    private boolean mIsPeopleChanging = false;

    private AMap mAMap;
    private List<Marker> mMarkerList;
    private List<Child> mChildList;
    private List<String> mThisTimeVisitedChildIdList;
    private Child child;
    private GuidePresenter mPresenter;
    private String merchantId;
    private String longitude;
    private String latitude;
    //当前景点经纬度
    private LatLng latLng;
    private CameraUpdate mCameraUpdate;
    private File file;
    //mark状态
    private static final float HAVE_VISITED = 60.0F;
    private static final float HAVE_NOT_VISITED = 0.0F;
    private static final float SEARCH_TARGET = 240.0F;
    private App mApp;
    private int mStep;
    private String mKm;
    private String mCalorie;
    //是否在景区范围
    private boolean mIsInSpot;
    private String mUserId;

    public static void launch(Context context, String merchantId, String longitude, String latitude) {
        mIsLaunchActivity = true;
        Intent intent = new Intent();
        intent.setClass(context, GuideActivity.class);
        intent.putExtra("merchantId", merchantId);
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
        context.startActivity(intent);
    }

    public static void launch(Context context, String merchantId, String longitude, String latitude,String userId) {
        mIsLaunchActivity = true;
        Intent intent = new Intent();
        intent.setClass(context, GuideActivity.class);
        intent.putExtra("merchantId", merchantId);
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    private String temMin;
    private String temMax;
    private String mWeatherStr;
    private String weatherCode;
    private String mCurrentTem;
    private String precipitation;
    private String windDir;
    private String windSc;
    private String prompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
        //初始化和风天气
        HeConfig.init(Constant.HEFENG_ID, Constant.HEFENG_KEY);
        HeConfig.switchToFreeServerNode();
        //初始化分享Dialog
        mShareDialog = new ShareDialog(this);
        mApp = (App) getApplication();
        mAMap = mMapView.getMap();
        mAMap.setInfoWindowAdapter(this);
        mJzvdStd = (MyPleayer) findViewById(R.id.videoplayer);
        mJzvdStd.setActivity(this);
        Jzvd.WIFI_TIP_DIALOG_SHOWED = true;
        Intent intent = getIntent();
        merchantId = intent.getStringExtra("merchantId");
        longitude = intent.getStringExtra("longitude");
        latitude = intent.getStringExtra("latitude");
        mUserId = intent.getStringExtra("userId");
        if (TextUtils.isEmpty(mUserId)) {
            mUserId = CommonUtils.getUserId();
        }

        //初始化数据集合
        mMarkerList = new ArrayList<>();
        mThisTimeVisitedChildIdList = new ArrayList<>();
        mChildList = new ArrayList<>();
        //初始化地图
        latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.interval(1000);
        mAMap.setOnMyLocationChangeListener(this);
        mAMap.setMyLocationStyle(myLocationStyle);
        mAMap.setMyLocationEnabled(true);
        file = new File(APP_FILE_PATH, "style.data");
        if (!file.exists()) {
            try {
                FileUtils.copyFileFromAssets(this, file, "style.data");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mAMap.getUiSettings().setZoomControlsEnabled(false);
        mAMap.getUiSettings().setAllGesturesEnabled(false);
        mAMap.setCustomMapStylePath(file.getAbsolutePath());
        mAMap.setMapCustomEnable(true);
        mAMap.getUiSettings().setLogoBottomMargin(-100);
        mAMap.setOnMarkerClickListener(this);
        addOverlayToMap();
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)), 17, 0, 0));
        mAMap.moveCamera(mCameraUpdate);
        mAMap.setMinZoomLevel(16);
        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {}

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                LatLng target = cameraPosition.target;
                if (!judgeMyLcationIsInAreaBounds(mAMap, mAreaBounds, target)) {
                    LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 17, 0, 0));
                    mAMap.moveCamera(mCameraUpdate);
                }
            }
        });
        PolygonOptions polygonOptions = new PolygonOptions();
        for(LatLng latLng : mAreaBounds){
            polygonOptions.add(latLng);
        }
        polygonOptions.visible(false);
        Polygon polygon = mAMap.addPolygon(polygonOptions);
        mIsInSpot = polygon.contains(mCurrentLatLng);
        polygon.remove();


        //获取数据
        mPresenter = new GuidePresenter(this);
        mPresenter.getMarkers(merchantId);
        mPresenter.getVoice(merchantId);
        HeWeather.getWeather(this, longitude + "," + latitude, Lang.CHINESE_SIMPLIFIED, Unit.METRIC,
                new HeWeather.OnResultWeatherDataListBeansListener() {
                    @Override
                    public void onError(Throwable e) {
                        //天气预报获取失败
                        mWeatherStr = "未知";
                        mCurrentTem = "未知";
                    }

                    @Override
                    public void onSuccess(List dataObject) {
                        if(mIsInSpot == false){
                            //用户不在景区范围内播放欢迎动画
                            playWelcomeVoice();
                        }
                        //天气预报获取成功
                        Gson gson = new Gson();
                        String jsonString = gson.toJson(dataObject);
                        //去除首尾"[]"字符
                        String substringJson = jsonString.substring(1, jsonString.length() - 1);
                        WeatherFree weatherFree = gson.fromJson(substringJson, WeatherFree.class);
                        temMin = weatherFree.getDaily_forecast().get(0).getTmp_min();
                        temMax = weatherFree.getDaily_forecast().get(0).getTmp_max();
                        mWeatherStr = weatherFree.getNow().getCond_txt();
                        weatherCode = weatherFree.getNow().getCond_code();
                        mCurrentTem = weatherFree.getNow().getTmp();
                        precipitation = weatherFree.getNow().getPcpn();
                        windDir = weatherFree.getNow().getWind_dir();
                        windSc = weatherFree.getNow().getWind_sc();
                        prompt = weatherFree.getLifestyle().get(1).getTxt();
                        showWeatherInfo(temMin, temMax, mWeatherStr, weatherCode, mCurrentTem, precipitation, windDir, windSc, prompt);
                    }
                });
        //展示人物进场动画
        peopleIn(mCheckedVoiceId);
        //设置点击事件
        mVoiceIv.setOnClickListener(this);
        mRemoveRecordIv.setOnClickListener(this);
        mRemoveRoadIv.setOnClickListener(this);
        mHealthIndexIv.setOnClickListener(this);
        mPlayerControllerTextDetailRl.setOnClickListener(this);
        //mPlayerControllerTextDetailTv.setOnClickListener(this);
        mPlayerControllerRl.setOnClickListener(this);
        //mPlayerControllerBt.setOnClickListener(this);
        //设置SeekBar拖动事件
        mProgressBarSb.setOnSeekBarChangeListener(this);
        EventBus.getDefault().register(this);
    }

    private TitleBar mTitleBar;
    @Override
    protected void initTitle() {
        super.initTitle();
        mTitleBar = getTitleBar().showBack().setRightText("分享游记", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsLaunchActivity == true) {
                    return;
                }

                if (mSingleChoiceDialog.getVisibility() == View.VISIBLE) {
                    return;
                }

                if (mIsPeopleChanging == true) {
                    return;
                }

                //分享时先向后台提交:UserId,景区Id,游览的景点,未游览的景点,步数,公里数,卡路里,天气
                //提交后后台返回生成的页面和景区名称
                ShareContentBean shareContentBean = new ShareContentBean();
                try {
                    mStep = mApp.getCurrentStepCurrent();
                    mKm = SportStepJsonUtils.getDistanceByStep(mStep);
                    mCalorie = SportStepJsonUtils.getCalorieByStep(mStep);
                    shareContentBean.setProductId(merchantId);
                    shareContentBean.setCalorie(mCalorie);
                    shareContentBean.setKm(mKm);
                    shareContentBean.setStep(mStep + "");
                    if (mWeatherStr == null || mWeatherStr.equals("") || mCurrentTem == null || mCurrentTem.equals("")) {
                        mWeatherStr = "未知";
                        mCurrentTem = "未知";
                    }
                    shareContentBean.setWeather(mWeatherStr + ":" + mCurrentTem);
                    String visitedChildIds = "";
                    if (mUserVisitedChildIdMap != null && mUserVisitedChildIdMap.size() > 0) {
                        for (int i = 0; i < mUserVisitedChildIdMap.size(); i++) {
                            if (i != mUserVisitedChildIdMap.size() - 1) {
                                visitedChildIds = visitedChildIds + mUserVisitedChildIdMap.get(i) + ",";
                            } else {
                                visitedChildIds = visitedChildIds + mUserVisitedChildIdMap.get(i);
                            }
                        }
                    }
                    shareContentBean.setChildIds(visitedChildIds);
                    shareContentBean.setUserId(mUserId);
                    mPresenter.getShareUrl(shareContentBean);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).showSearchView("景点搜索", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mIsLaunchActivity == true) {
                    return;
                }

                if (mSingleChoiceDialog.getVisibility() == View.VISIBLE) {
                    return;
                }

                if (mIsPeopleChanging == true) {
                    return;
                }

                if (mChildList != null) {
                    GuideSearchActivity.launch(GuideActivity.this, mChildList);
                }
            }
        });
        mTitleBar.show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_guide);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {
    }

    @Override
    public void addMarkers(List<MerchantBean> merchantList) {
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        if (mIsLaunchActivity == true) {
            return true;
        }

        if (mSingleChoiceDialog.getVisibility() == View.VISIBLE) {
            return false;
        }

        if (mIsPeopleChanging == true) {
            return false;
        }
        if (!marker.isInfoWindowShown()) {
            marker.setInfoWindowEnable(false);
        }
        for (Marker marker1 : mMarkerList) {
            if (marker.equals(marker1)) {
                child = (Child) marker.getObject();
                new AlertDialog.Builder(this, getSupportFragmentManager())
                        .setTitle("提示")
                        .setMessage(child.getChildName())
                        .setPositiveText("导航")
                        .setNegativeText("取消")
                        .setOnPositiveListener(this).build().show();
            }
        }
        return false;
    }

    private TextView mInfoTitleTv;
    private TextView mInfoSnippetTv;

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(R.layout.marker_info_layout, null);
        mInfoTitleTv = infoWindow.findViewById(R.id.tv_title);
        mInfoSnippetTv = infoWindow.findViewById(R.id.tv_snippet);
        mInfoTitleTv.setText(marker.getTitle());
        mInfoSnippetTv.setText(marker.getSnippet());
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onClick(View view) {
        if (mIsLaunchActivity == true) {
            return;
        }

        if (mSingleChoiceDialog.getVisibility() == View.VISIBLE) {
            return;
        }

        if (mIsPeopleChanging == true) {
            return;
        }

        switch (view.getId()) {
            case R.id.iv_voice:
                if (mIsPlayingVoice == true) {
                    ToastUtils.showToast(getContext(), "温馨提示:切换语音前要停止讲解哦");
                    return;
                } else {
                    if (mSingleChoiceDialog.getVisibility() == View.GONE) {
                        mSingleChoiceDialog.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.iv_remove_record:
                //清除游览记录
                if (mUserVisitedChildIdMap != null && mUserVisitedChildIdMap.size() > 0) {
                    Set<String> keys = mUserVisitedChildIdMap.keySet();
                    for (String childId : keys) {
                        if (!mThisTimeVisitedChildIdList.contains(childId)) {
                            mThisTimeVisitedChildIdList.add(mUserVisitedChildIdMap.get(childId));
                        }
                    }
                }
                if (mThisTimeVisitedChildIdList != null && mThisTimeVisitedChildIdList.size() > 0) {
                    mPresenter.refreshLoginUserVisitedSpotOnServer(mUserId, mThisTimeVisitedChildIdList, "remove");
                    mThisTimeVisitedChildIdList.clear();
                    mUserVisitedChildIdMap.clear();
                    return;
                }
                //清除搜索结果
                setSpotMarkerState(mUserBean);
                break;
            case R.id.iv_remove_road:
                if(mPolyline == null){
                    //mPolyline == null说明并未进行路线规划
                    //因此直接返回
                    return;
                }else {
                    mPolyline = null;
                    setSpotMarkerState(mUserBean);
                }
                break;

            case R.id.iv_health_index:
                showAndDimissHealthIndex();
                break;

            case R.id.rl_player_controller_text_detail:
                //显示图例
                if(mLegendLl.getVisibility() == View.GONE){
                    mLegendLl.setVisibility(View.VISIBLE);
                }
                if (mContentSv.getVisibility() == View.VISIBLE) {
                    mContentSv.setVisibility(View.GONE);
                    mPlayerControllerTextDetailTv.setText("显示详情");
                } else {
                    //隐藏图例
                    if(mLegendLl.getVisibility() == View.VISIBLE){
                        mLegendLl.setVisibility(View.GONE);
                    }
                    if(mHealthIndexRl.getVisibility() == View.VISIBLE){
                        mHealthIndexRl.setVisibility(View.GONE);
                    }
                    mContentSv.setVisibility(View.VISIBLE);
                    mPlayerControllerTextDetailTv.setText("隐藏详情");
                }
                break;

            case R.id.rl_player_controller_bt:
                mJzvdStd.setUp(mCurrentVoiceUrl, "", JzvdStd.SCREEN_WINDOW_NORMAL);
                mJzvdStd.startButton.performClick();
                break;

            default:
                //点击Marker不进入详情，没有管理端图片没法维护
                //先做成导航
                if (mCurrentLatLng != null) {
                    //测试路线:大门口
                    //LatLng latLng = new LatLng(Double.parseDouble("40.18707573784722"), Double.parseDouble("117.62696261935764"));
                    float leastDistance = 10000;
                    int mClosedChildIndex = -1;
                    for (int i = 0; i < mChildList.size(); i++) {
                        float distanceFromMCurrentLatLngFromChild = AMapUtils.calculateLineDistance(mCurrentLatLng, new LatLng(Double.parseDouble(mChildList.get(i).getChildLatitude()), Double.parseDouble(mChildList.get(i).getShopLongitude())));
                        if (leastDistance > distanceFromMCurrentLatLngFromChild) {
                            leastDistance = distanceFromMCurrentLatLngFromChild;
                            mClosedChildIndex = i;
                        }
                    }
                    if(mClosedChildIndex == -1){
                        ToastUtils.showToast(this,"温馨提示:导航功能须在景区内使用");
                    }else{
                        Child child = mChildList.get(mClosedChildIndex);
                        mPresenter.getRoad(child.getId(), this.child.getId());
                    }
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(!mIsPlayingVoice){
            seekBar.setProgress(0);
            return;
        }
        int progress = seekBar.getProgress();
        JZMediaManager.seekTo(mJzvdStd.getDuration() * progress / 100);
    }


    /*展示健康指数*/
    private void showAndDimissHealthIndex() {
        if(mHealthIndexRl.getVisibility() == View.VISIBLE){
            mHealthIndexRl.setVisibility(View.GONE);
            if(mLegendLl.getVisibility() == View.GONE){
                mLegendLl.setVisibility(View.VISIBLE);
            }
        }else {
            try {
                //隐藏图例
                if(mLegendLl.getVisibility() ==View.VISIBLE){
                    mLegendLl.setVisibility(View.GONE);
                }
                if(mContentSv.getVisibility() == View.VISIBLE){
                    mContentSv.setVisibility(View.GONE);
                    mPlayerControllerTextDetailTv.setText("显示详情");
                }
                mStep = mApp.getCurrentStepCurrent();
                mKm = SportStepJsonUtils.getDistanceByStep(mStep);
                mCalorie = SportStepJsonUtils.getCalorieByStep(mStep);
                mHealthTv.setText("健康指数: " + "\n"
                        + "您已行走: " + mStep + "步" + "\n"
                        + "消耗卡路里: " + mCalorie + "大卡" + "\n"
                        + "行走距离: " + mKm + "KM" + "\n");
                mHealthTv.setBackgroundResource(R.drawable.health_notice);
                mHealthIndexRl.setVisibility(View.VISIBLE);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private LatLng mCurrentLatLng;
    private String mCurrentVoiceUrl = "";
    private Child mClosedChild = null;
    private Marker mClosedMarker = null;
    private int mClosedChildIndex = 0;

    @Override
    public void onMyLocationChange(Location location) {
        if (mIsLaunchActivity == true) {
            return;
        }
        if (mIsPlayingVoice == true) {
            return;
        }
        mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        //TODO 先判断是否在景区内 根据坐标点经纬度
        //先选取距离最近的景点坐标
        if (mChildList != null && mChildList.size() > 0) {
            float leastDistance = 10000;
            //for循环找到离当前位置最近的child
            for (int i = 0; i < mChildList.size(); i++) {
                float distanceFromMCurrentLatLngFromChild = AMapUtils.calculateLineDistance(mCurrentLatLng, new LatLng(Double.parseDouble(mChildList.get(i).getChildLatitude()), Double.parseDouble(mChildList.get(i).getShopLongitude())));
                if (leastDistance > distanceFromMCurrentLatLngFromChild) {
                    leastDistance = distanceFromMCurrentLatLngFromChild;
                    mClosedChildIndex = i;
                }
            }

            mClosedChild = mChildList.get(mClosedChildIndex);
            mClosedMarker = mMarkerList.get(mClosedChildIndex);
            if(mUserVisitedChildIdMap != null && !mUserVisitedChildIdMap.containsKey(mClosedChild.getId())){
                Animation animation = new ScaleAnimation(0, 1, 0, 1);
                animation.setDuration(500);
                animation.setInterpolator(new LinearInterpolator());
                mClosedMarker.setAnimation(animation);
                mClosedMarker.startAnimation();
            }


            mDistinationTv.setText(mClosedChild.getChildName());
            mContentTv.setText(mClosedChild.getChineseContent());

            switch (mCheckedVoiceId) {
                case Constant.CHINESE_WOMAN:
                    mCurrentVoiceUrl = UrlConstant.GUIDE_SPOT_IMAGE_URL + mClosedChild.getChineseGirl();
                    mContentTv.setText(mClosedChild.getChineseContent());
                    break;
                case Constant.CHINESE_MAN:
                    mCurrentVoiceUrl = UrlConstant.GUIDE_SPOT_IMAGE_URL + mClosedChild.getChineseBoy();
                    mContentTv.setText(mClosedChild.getChineseContent());
                    break;
            }

            //显示控件
            if (mPlayerControllerLl.getVisibility() == View.GONE) {
                mPlayerControllerLl.setVisibility(View.VISIBLE);
                if (leastDistance >= Float.parseFloat(mClosedChild.getScope())){
                    CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(Double.parseDouble(mClosedChild.getShopLatitude()), Double.parseDouble(mClosedChild.getShopLongitude())), 17, 0, 0));
                    mAMap.moveCamera(mCameraUpdate);
                    mJzvdStd.setCurrentIntroduceChild(mClosedChild);
                    mJzvdStd.setUp(mCurrentVoiceUrl, "", JzvdStd.SCREEN_WINDOW_NORMAL);
                    mJzvdStd.startButton.performClick();
                }
            }



            //判断是否在讲解范围
            if(leastDistance < Float.parseFloat(mClosedChild.getScope())){
                //如果mUserVisitedChildIdMap中没有当前最近child的if那么自动讲解
                //如果有那么用户可以手动点击播放按钮进行讲解
                if(!mUserVisitedChildIdMap.containsKey(mClosedChild.getId())){
                    mJzvdStd.setCurrentIntroduceChild(mClosedChild);
                    mJzvdStd.setUp(mCurrentVoiceUrl, "", JzvdStd.SCREEN_WINDOW_NORMAL);
                    mJzvdStd.startButton.performClick();
                }
            }
        }
    }

    /*更新SeekBar进度,在MyPlayer中调用*/
    public void updateSeekBarProgress(int persent){
        mProgressBarSb.setProgress(persent);
    }

    /*暂停时将当前将节点加入mUserVisitedChildIdMap中不在进行自动讲解
    * 用户可手动点击播放按钮进行讲解*/
    public void setLoginUserVisitedSpot(){
        if(mUserVisitedChildIdMap != null){
            if(!mUserVisitedChildIdMap.containsKey(mClosedChild.getId())){
                mUserVisitedChildIdMap.put(mClosedChild.getId(),mClosedChild.getId());
            }
        }
    }

    private HashMap<String, String> mUserVisitedChildIdMap;
    private UserBean mUserBean;
    @Override
    public void setSpotMarkerState(UserBean data) {
        mUserBean = data;
        if (mUserVisitedChildIdMap == null) {
            mUserVisitedChildIdMap = new HashMap<>();
        } else {
            mUserVisitedChildIdMap.clear();
        }
        if (data != null) {
            String visitedChildIds = data.getChildId();
            if (visitedChildIds != null && !visitedChildIds.equals("")) {
                String[] visitedChildIdsSplit = visitedChildIds.split(",");
                for (int i = 0; i < visitedChildIdsSplit.length; i++) {
                    mUserVisitedChildIdMap.put(visitedChildIdsSplit[i], visitedChildIdsSplit[i]);
                }
            }
        }
        if (mChildList != null && mChildList.size() > 0) {
            if (mIsLaunchActivity == false) {
                mAMap.clear();
                addOverlayToMap();
                mMarkerList.clear();
            }
            Marker marker = null;
            for (Child child : mChildList) {
                double latitude = Double.valueOf(child.getShopLatitude());
                double longitude = Double.valueOf(child.getShopLongitude());
                MarkerOptions options = new MarkerOptions();
                if (mGuideTargetChild != null) {
                    if (child.getId().equals(mGuideTargetChild.getId())) {
                        options.position(new LatLng(latitude, longitude)).draggable(false)
                                .icon(BitmapDescriptorFactory.defaultMarker(SEARCH_TARGET));
                        mGuideTargetChild = null;
                        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latitude, longitude), 17, 0, 0));
                        mAMap.moveCamera(mCameraUpdate);
                        marker = mAMap.addMarker(options);
                        marker.setObject(child);
                        mMarkerList.add(marker);

                        if (mIsPlayingVoice) {
                            Child childForClosedMarker = (Child) mClosedMarker.getObject();
                            if (childForClosedMarker.getId().equals(child.getId())) {
                                mClosedMarker = marker;
                                mClosedMarker.setTitle("正在讲解");
                                mClosedMarker.setSnippet(child.getChildName());
                                mClosedMarker.showInfoWindow();
                            }
                        }
                        continue;
                    }
                }
                //按照是否游览过的点显示不同的marker颜色
                //是否游览过根据mUserVisitedChildIdMap是否含有该child进行判断
                //注意mUserVisitedChildIdMap可能为空
                if (mUserVisitedChildIdMap != null) {
                    if (mUserVisitedChildIdMap.containsKey(child.getId())) {
                        options.position(new LatLng(latitude, longitude)).draggable(false)
                                .icon(BitmapDescriptorFactory.defaultMarker(HAVE_VISITED));
                    } else {
                        options.position(new LatLng(latitude, longitude)).draggable(false)
                                .icon(BitmapDescriptorFactory.defaultMarker(HAVE_NOT_VISITED));
                    }
                } else {
                    options.position(new LatLng(latitude, longitude)).draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(HAVE_NOT_VISITED));
                }
                marker = mAMap.addMarker(options);
                marker.setObject(child);
                mMarkerList.add(marker);

                //刷新地图marker状态后讲解时应继续显示继续讲解window
                if (mIsPlayingVoice) {
                    Child childForClosedMarker = (Child) mClosedMarker.getObject();
                    if (childForClosedMarker.getId().equals(child.getId())) {
                        mClosedMarker = marker;
                        mClosedMarker.setTitle("正在讲解");
                        mClosedMarker.setSnippet(child.getChildName());
                        mClosedMarker.showInfoWindow();
                    }
                }
            }
        }
    }

    @Override
    public void addChildren(List<Child> children) {
        this.mChildList = children;
        mPresenter.getLoginUserSpotMarkerState(mUserId);
    }

    //搜索事件
    private Child mGuideTargetChild;
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onGuideSearchEvent(GuideSearchEvent guideSearchEvent) {
        //按照搜索结果刷新界面
        mGuideTargetChild = guideSearchEvent.getmChild();
        setSpotMarkerState(mUserBean);

    }

    private String mCheckedVoiceId = "1";
    private int mCheckedPosition = 0;
    private List<VoiceBean> mVoiceBeanList;

    @Override
    public void setVoice(List<VoiceBean> voiceBeanList) {
        if (voiceBeanList == null || voiceBeanList.size() == 0) {
            mVoiceBeanList = new ArrayList<>();
            //语音数据获取错误，默认创建一个集合放中文女生VoiceBean
            VoiceBean voiceBean = new VoiceBean();
            voiceBean.setId("1");
            voiceBean.setName("中文男声");
            VoiceBean voiceBean2 = new VoiceBean();
            voiceBean2.setId("2");
            voiceBean2.setName("中文女声");

            VoiceBean voiceBean3 = new VoiceBean();
            voiceBean3.setId("3");
            voiceBean3.setName("唐山方言");

            VoiceBean voiceBean4 = new VoiceBean();
            voiceBean4.setId("4");
            voiceBean4.setName("四川方言");
            mVoiceBeanList.add(voiceBean);
            mVoiceBeanList.add(voiceBean2);
            mVoiceBeanList.add(voiceBean3);
            mVoiceBeanList.add(voiceBean4);
        } else {
            mVoiceBeanList = voiceBeanList;
            VoiceBean voiceBean2 = new VoiceBean();
            voiceBean2.setId("2");
            voiceBean2.setName("中文男声");

            VoiceBean voiceBean3 = new VoiceBean();
            voiceBean3.setId("3");
            voiceBean3.setName("英文女声");

            VoiceBean voiceBean4 = new VoiceBean();
            voiceBean4.setId("4");
            voiceBean4.setName("英文男声");

            mVoiceBeanList.add(voiceBean2);
            mVoiceBeanList.add(voiceBean3);
            mVoiceBeanList.add(voiceBean4);

        }
        mSingleChoiceDialog.setRadio(mVoiceBeanList, mVoiceBeanList.get(0).getId());
        mSingleChoiceDialog.setOnCheckedChangeListerner(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                mCheckedPosition = i;
            }
        });
        mSingleChoiceDialog.setOnClickListernerForConfirm(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mVoiceBeanList.get(mCheckedPosition).getId().equals("1")){
                    ToastUtils.showToast(getContext(),"温馨提示:暂未提供该讲解语音。");
                }

               // if (!mVoiceBeanList.get(mCheckedPosition).getId().equals(mCheckedVoiceId)) {

//                    mIsPeopleChanging = true;
//                    //先执行人物退场动画
//                    switch (mCheckedVoiceId) {
//                        case Constant.CHINESE_WOMAN:
//                            //中文女声
//                            mAnimImages = getAnimRes(R.array.chinese_woman_out);
//                           break;
//                        case Constant.CHINESE_MAN:
//                            //中文男声
//                            mAnimImages = getAnimRes(R.array.chinese_man_out);
//                            break;
//
//                    }
//                    FrameAnimation frameAnimation = new FrameAnimation(mPeopleIv, mAnimImages, 100, false);
//                    frameAnimation.setAnimationListener(new FrameAnimation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart() {
//                        }
//
//                        @Override
//                        public void onAnimationEnd() {
//                            //人物退场动画执行完成
//                            //重新赋值mCheckedVoiceId
//                            mCheckedVoiceId = mVoiceBeanList.get(mCheckedPosition).getId();
//                            //按照新 mCheckedVoiceId 执行人物入场动画
//                            peopleIn(mCheckedVoiceId);
//                        }
//
//                        @Override
//                        public void onAnimationRepeat() {
//                        }
//                    });
                //}
                mSingleChoiceDialog.setVisibility(View.GONE);
            }
        });
        mSingleChoiceDialog.setOnClickListernerForCancel(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSingleChoiceDialog.setVisibility(View.GONE);
                    }
                }
        );
    }

    @Override
    public void shareMyTravel(String url, String merchantName) {
        ShareBean shareBean = new ShareBean();
        shareBean.setUrl(url);
        shareBean.setTitle(merchantName + "游记");
        shareBean.setDescription("畅游华夏,尽在过来玩" + "\n" + "联系电话" + "\n" + "0315-6681288/6686299");
        mShareDialog.setShareDate(shareBean);
        mShareDialog.showShareDialog();
    }

    private Polyline mPolyline;
    @Override
    public void showRoad(List<Child> childOnRoad) {
        if (childOnRoad == null || childOnRoad.size() == 0) {
            ToastUtils.showToast(GuideActivity.this, "温馨提示:没有路线可以到达");
        } else {
            List<LatLng> latLngs = new ArrayList<LatLng>();
            for (int i = 0; i < childOnRoad.size(); i++) {
                Child child = childOnRoad.get(i);
                LatLng latLng = new LatLng(Double.valueOf(child.getShopLatitude()), Double.valueOf(child.getShopLongitude()));
                latLngs.add(latLng);
            }
            BitmapDescriptor mRedTexture = BitmapDescriptorFactory
                    .fromAsset("road_pic.png");
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.setCustomTexture(mRedTexture);
            polylineOptions.setUseTexture(true);
            polylineOptions.width(30F);
            for (int i = latLngs.size() - 1; i > 0; i--) {
                mPolyline = mAMap.addPolyline(polylineOptions.add(latLngs.get(i), latLngs.get(i - 1)));
            }
        }
    }


    /*展示天气信息*/
    private void showWeatherInfo(String minTem, String maxTem, String weatherStr, String weatherCode, String currentTem, String precipitation, String windDir, String windSc, String prompt) {
        //隐藏图例
        if(mLegendLl.getVisibility() == View.VISIBLE){
            mLegendLl.setVisibility(View.GONE);
        }
        mWeatherTv.setVisibility(View.VISIBLE);
        mWeatherTv.setText("今日温度: " + minTem + " ~ " + maxTem + "℃" + " " + weatherStr + "\n"
                + "当前温度: " + currentTem + "℃" + "\n"
                + "降水量: " + precipitation + "%" + "\n"
                + "风向风力: " + windDir + " " + windSc + "级" + "\n"
                + "温馨提示: " + prompt);
        setWeatherImg(weatherCode);
    }

    private void setWeatherImg(String weatherCode) {
        switch (weatherCode) {
            case Constant.WEATHER_100:
                mWeatherTv.setBackgroundResource(R.drawable.weather_100);
                break;
            case Constant.WEATHER_101:
                mWeatherTv.setBackgroundResource(R.drawable.weather_101);
                break;
            case Constant.WEATHER_102:
                mWeatherTv.setBackgroundResource(R.drawable.weather_102);
                break;
            case Constant.WEATHER_103:
                mWeatherTv.setBackgroundResource(R.drawable.weather_103);
                break;
            case Constant.WEATHER_104:
                mWeatherTv.setBackgroundResource(R.drawable.weather_104);
                break;

            case Constant.WEATHER_200:
                mWeatherTv.setBackgroundResource(R.drawable.weather_200);
                break;
            case Constant.WEATHER_201:
                mWeatherTv.setBackgroundResource(R.drawable.weather_201);
                break;
            case Constant.WEATHER_202:
                mWeatherTv.setBackgroundResource(R.drawable.weather_202);
                break;
            case Constant.WEATHER_203:
                mWeatherTv.setBackgroundResource(R.drawable.weather_203);
                break;
            case Constant.WEATHER_204:
                mWeatherTv.setBackgroundResource(R.drawable.weather_204);
                break;
            case Constant.WEATHER_205:
                mWeatherTv.setBackgroundResource(R.drawable.weather_205);
                break;
            case Constant.WEATHER_206:
                mWeatherTv.setBackgroundResource(R.drawable.weather_206);
                break;
            case Constant.WEATHER_207:
                mWeatherTv.setBackgroundResource(R.drawable.weather_207);
                break;
            case Constant.WEATHER_208:
                mWeatherTv.setBackgroundResource(R.drawable.weather_208);
                break;
            case Constant.WEATHER_209:
                mWeatherTv.setBackgroundResource(R.drawable.weather_209);
                break;
            case Constant.WEATHER_210:
                mWeatherTv.setBackgroundResource(R.drawable.weather_210);
                break;
            case Constant.WEATHER_211:
                mWeatherTv.setBackgroundResource(R.drawable.weather_211);
                break;
            case Constant.WEATHER_212:
                mWeatherTv.setBackgroundResource(R.drawable.weather_212);
                break;
            case Constant.WEATHER_213:
                mWeatherTv.setBackgroundResource(R.drawable.weather_213);
                break;


            case Constant.WEATHER_300:
                mWeatherTv.setBackgroundResource(R.drawable.weather_300);
                break;
            case Constant.WEATHER_301:
                mWeatherTv.setBackgroundResource(R.drawable.weather_301);
                break;
            case Constant.WEATHER_302:
                mWeatherTv.setBackgroundResource(R.drawable.weather_302);
                break;
            case Constant.WEATHER_303:
                mWeatherTv.setBackgroundResource(R.drawable.weather_303);
                break;
            case Constant.WEATHER_304:
                mWeatherTv.setBackgroundResource(R.drawable.weather_304);
                break;
            case Constant.WEATHER_305:
                mWeatherTv.setBackgroundResource(R.drawable.weather_305);
                break;
            case Constant.WEATHER_306:
                mWeatherTv.setBackgroundResource(R.drawable.weather_306);
                break;
            case Constant.WEATHER_307:
                mWeatherTv.setBackgroundResource(R.drawable.weather_307);
                break;
            case Constant.WEATHER_308:
                mWeatherTv.setBackgroundResource(R.drawable.weather_308);
                break;
            case Constant.WEATHER_309:
                mWeatherTv.setBackgroundResource(R.drawable.weather_309);
                break;
            case Constant.WEATHER_310:
                mWeatherTv.setBackgroundResource(R.drawable.weather_310);
                break;
            case Constant.WEATHER_311:
                mWeatherTv.setBackgroundResource(R.drawable.weather_311);
                break;
            case Constant.WEATHER_312:
                mWeatherTv.setBackgroundResource(R.drawable.weather_312);
                break;
            case Constant.WEATHER_313:
                mWeatherTv.setBackgroundResource(R.drawable.weather_313);
                break;
            case Constant.WEATHER_314:
                mWeatherTv.setBackgroundResource(R.drawable.weather_314);
                break;
            case Constant.WEATHER_315:
                mWeatherTv.setBackgroundResource(R.drawable.weather_315);
                break;
            case Constant.WEATHER_316:
                mWeatherTv.setBackgroundResource(R.drawable.weather_316);
                break;
            case Constant.WEATHER_317:
                mWeatherTv.setBackgroundResource(R.drawable.weather_317);
                break;
            case Constant.WEATHER_318:
                mWeatherTv.setBackgroundResource(R.drawable.weather_318);
                break;
            case Constant.WEATHER_399:
                mWeatherTv.setBackgroundResource(R.drawable.weather_399);
                break;

            case Constant.WEATHER_400:
                mWeatherTv.setBackgroundResource(R.drawable.weather_400);
                break;
            case Constant.WEATHER_401:
                mWeatherTv.setBackgroundResource(R.drawable.weather_401);
                break;
            case Constant.WEATHER_402:
                mWeatherTv.setBackgroundResource(R.drawable.weather_302);
                break;
            case Constant.WEATHER_403:
                mWeatherTv.setBackgroundResource(R.drawable.weather_403);
                break;
            case Constant.WEATHER_404:
                mWeatherTv.setBackgroundResource(R.drawable.weather_404);
                break;
            case Constant.WEATHER_405:
                mWeatherTv.setBackgroundResource(R.drawable.weather_405);
                break;
            case Constant.WEATHER_406:
                mWeatherTv.setBackgroundResource(R.drawable.weather_406);
                break;
            case Constant.WEATHER_407:
                mWeatherTv.setBackgroundResource(R.drawable.weather_407);
                break;
            case Constant.WEATHER_408:
                mWeatherTv.setBackgroundResource(R.drawable.weather_408);
                break;
            case Constant.WEATHER_409:
                mWeatherTv.setBackgroundResource(R.drawable.weather_409);
                break;
            case Constant.WEATHER_410:
                mWeatherTv.setBackgroundResource(R.drawable.weather_410);
                break;
            case Constant.WEATHER_499:
                mWeatherTv.setBackgroundResource(R.drawable.weather_499);
                break;

            case Constant.WEATHER_500:
                mWeatherTv.setBackgroundResource(R.drawable.weather_500);
                break;
            case Constant.WEATHER_501:
                mWeatherTv.setBackgroundResource(R.drawable.weather_501);
                break;
            case Constant.WEATHER_502:
                mWeatherTv.setBackgroundResource(R.drawable.weather_502);
                break;
            case Constant.WEATHER_503:
                mWeatherTv.setBackgroundResource(R.drawable.weather_503);
                break;
            case Constant.WEATHER_504:
                mWeatherTv.setBackgroundResource(R.drawable.weather_504);
                break;
            case Constant.WEATHER_507:
                mWeatherTv.setBackgroundResource(R.drawable.weather_507);
                break;
            case Constant.WEATHER_508:
                mWeatherTv.setBackgroundResource(R.drawable.weather_508);
                break;
            case Constant.WEATHER_509:
                mWeatherTv.setBackgroundResource(R.drawable.weather_509);
                break;
            case Constant.WEATHER_510:
                mWeatherTv.setBackgroundResource(R.drawable.weather_510);
                break;
            case Constant.WEATHER_511:
                mWeatherTv.setBackgroundResource(R.drawable.weather_511);
                break;
            case Constant.WEATHER_512:
                mWeatherTv.setBackgroundResource(R.drawable.weather_512);
                break;
            case Constant.WEATHER_513:
                mWeatherTv.setBackgroundResource(R.drawable.weather_513);
                break;
            case Constant.WEATHER_514:
                mWeatherTv.setBackgroundResource(R.drawable.weather_514);
                break;
            case Constant.WEATHER_515:
                mWeatherTv.setBackgroundResource(R.drawable.weather_515);
                break;

            case Constant.WEATHER_900:
                mWeatherTv.setBackgroundResource(R.drawable.weather_900);
                break;
            case Constant.WEATHER_901:
                mWeatherTv.setBackgroundResource(R.drawable.weather_901);
                break;
            case Constant.WEATHER_999:
                mWeatherTv.setBackgroundResource(R.drawable.weather_999);
                break;
        }
    }

    /*播放欢迎语音*/
    private MediaPlayer mMediaPlayer;

    private void playWelcomeVoice() {
        try {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd("guide_welcome.mp3");
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mMediaPlayer.setOnSeekCompleteListener(this);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private int[] mAnimImages = null;
    /*人物进场*/
    private void peopleIn(String checkedVoiceId) {
        switch (checkedVoiceId) {
            case Constant.CHINESE_WOMAN:
                //中文女声
                //mAnimImages = getAnimRes(R.array.chinese_woman_in);
                mAnimImages = getAnimRes(R.array.chinese_man_in);
                break;
            case Constant.CHINESE_MAN:
                //中文男声
                mAnimImages = getAnimRes(R.array.chinese_man_in);
                break;
        }
        FrameAnimation frameAnimation = new FrameAnimation(mPeopleIv, mAnimImages, 100, false);
        frameAnimation.setAnimationListener(new FrameAnimation.AnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                if (mIsPeopleChanging == true) {
                    mIsPeopleChanging = false;
                }
                if (mIsLaunchActivity == true) {
                    if(mIsInSpot == true){
                        //在景区内，用户意图重新打开导览,因此进场动画时间减少
                        //天气信息
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mWeatherTv.setVisibility(View.GONE);
                                mAMap.getUiSettings().setAllGesturesEnabled(true);
                                mIsLaunchActivity = false;
                                //显示图例
                                if(mLegendLl.getVisibility() == View.GONE){
                                    mLegendLl.setVisibility(View.VISIBLE);
                                }
                            }
                        }, 3 * 1000);
                    }else {
                        //天气信息
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mWeatherTv.setVisibility(View.GONE);
                                mAMap.getUiSettings().setAllGesturesEnabled(true);
                                mIsLaunchActivity = false;
                                //显示图例
                                if(mLegendLl.getVisibility() == View.GONE){
                                    mLegendLl.setVisibility(View.VISIBLE);
                                }
                            }
                        }, 25 * 1000);
                    }
                }
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
    }


    /*人物讲话*/
    private FrameAnimation peopleSpeakFrameAnimation;

    public void peopleSpeak(String checkedVoiceId) {
        switch (checkedVoiceId) {
            case Constant.CHINESE_WOMAN:
                //中文女声
                //mAnimImages = getAnimRes(R.array.chinese_woman_speak);
                mAnimImages = getAnimRes(R.array.chinese_man_speak);
                break;
            case Constant.CHINESE_MAN:
                //中文男声
                mAnimImages = getAnimRes(R.array.chinese_man_speak);
                break;
        }
        peopleSpeakFrameAnimation = new FrameAnimation(mPeopleIv, mAnimImages, 100, true);
    }

    /*人物停止讲话*/
    public void peopleStopSpeak() {
        if (peopleSpeakFrameAnimation != null) {
            peopleSpeakFrameAnimation.release();
        }
    }


    /*获取动画资源图片*/
    private int[] getAnimRes(int resource) {
        TypedArray typedArray = getResources().obtainTypedArray(resource);
        int len = typedArray.length();
        int[] resId = new int[len];
        for (int i = 0; i < len; i++) {
            resId[i] = typedArray.getResourceId(i, -1);
        }
        typedArray.recycle();
        return resId;
    }


    public static Bitmap convertViewToBitmap(View view) {
        view.setDrawingCacheEnabled(true);

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0,
                view.getMeasuredWidth(),
                view.getMeasuredHeight());
        Bitmap bitmap = view.getDrawingCache(true);
        return Bitmap.createBitmap(bitmap);
    }

    private List<LatLng> mAreaBounds;
    private LatLngBounds mBounds;

    private void addOverlayToMap() {
        if (mAreaBounds == null) {
            mAreaBounds = new ArrayList<>();
            LatLng latlng1 = new LatLng(40.187254, 117.618897);
            LatLng latlng2 = new LatLng(40.186627, 117.619176);
            LatLng latlng3 = new LatLng(40.186397, 117.61946);
            LatLng latlng4 = new LatLng(40.186336, 117.619803);
            LatLng latlng5 = new LatLng(40.186008, 117.621262);
            LatLng latlng6 = new LatLng(40.186119, 117.621858);
            LatLng latlng7 = new LatLng(40.18616, 117.622566);
            LatLng latlng8 = new LatLng(40.186373, 117.626665);
            LatLng latlng9 = new LatLng(40.186463, 117.627748);
            LatLng latlng10 = new LatLng(40.187684, 117.629347);
            LatLng latlng11 = new LatLng(40.187758, 117.6294);
            LatLng latlng12 = new LatLng(40.1884, 117.628821);
            LatLng latlng13 = new LatLng(40.190996, 117.626471);
            LatLng latlng14 = new LatLng(40.190053, 117.623821);
            LatLng latlng15 = new LatLng(40.188389, 117.619659);
            LatLng latlng16 = new LatLng(40.187186, 117.618901);
            mAreaBounds.add(latlng1);
            mAreaBounds.add(latlng2);
            mAreaBounds.add(latlng3);
            mAreaBounds.add(latlng4);
            mAreaBounds.add(latlng5);
            mAreaBounds.add(latlng6);
            mAreaBounds.add(latlng7);
            mAreaBounds.add(latlng8);
            mAreaBounds.add(latlng9);
            mAreaBounds.add(latlng10);
            mAreaBounds.add(latlng11);
            mAreaBounds.add(latlng12);
            mAreaBounds.add(latlng13);
            mAreaBounds.add(latlng14);
            mAreaBounds.add(latlng15);
            mAreaBounds.add(latlng16);

            if (mBounds == null) {
                mBounds = new LatLngBounds.Builder()
                        .include(latlng1)
                        .include(latlng2)
                        .include(latlng3)
                        .include(latlng4)
                        .include(latlng5)
                        .include(latlng6)
                        .include(latlng7)
                        .include(latlng8)
                        .include(latlng9)
                        .include(latlng10)
                        .include(latlng11)
                        .include(latlng12)
                        .include(latlng12)
                        .include(latlng13)
                        .include(latlng14)
                        .include(latlng15)
                        .include(latlng16)
                        .build();
            }
        }
        mAMap.addGroundOverlay(new GroundOverlayOptions()
                .anchor(0.5f, 0.5f)
                .transparency(0f)
                .image(BitmapDescriptorFactory.fromResource(R.drawable.guide_map))
                .positionFromBounds(mBounds));
    }

    //判断是否在范围内
    private boolean judgeMyLcationIsInAreaBounds(AMap aMap, List<LatLng> latLngList, LatLng latLng) {
        PolygonOptions options = new PolygonOptions();
        for (LatLng i : latLngList) {
            options.add(i);
        }
        options.visible(true); //设置区域是否显示
        Polygon polygon = aMap.addPolygon(options);
        boolean contains = polygon.contains(latLng);
        polygon.remove();
        return contains;
    }


    /*显示语音加载界面*/
    public void showVoiceLoading() {
        if (mVoiceLoadingRl.getVisibility() == View.GONE) {
            mVoiceLoadingRl.setVisibility(View.VISIBLE);
        }
    }

    /*隐藏语音加载界面*/
    public void dimissVoiceLoading() {
        if (mVoiceLoadingRl.getVisibility() == View.VISIBLE) {
            mVoiceLoadingRl.setVisibility(View.GONE);
        }
    }

    /*提供语音控制器开始暂停按钮*/
    public ImageView getPlayerControllerBt() {
        if (mPlayerControllerBt != null) {
            return mPlayerControllerBt;
        }
        return null;
    }

    /*显示marker信息*/
    public void showMarkerInfo() {
        if (mClosedMarker != null) {
            mClosedMarker.setInfoWindowEnable(true);
            if (!mClosedMarker.isInfoWindowShown()) {
                mClosedMarker.setTitle("正在讲解");
                mClosedMarker.setSnippet(mClosedChild.getChildName());
                mClosedMarker.showInfoWindow();
            }
        }
    }

    /*隐藏marker信息*/
    public void dimissMarkerInfo() {
        if (mClosedMarker != null) {
            if (mClosedMarker.isInfoWindowShown()) {
                mClosedMarker.hideInfoWindow();
            }
        }
    }

    /*播放语音时人物讲解动画*/
    public void startSpeakAnim() {
        peopleSpeak(mCheckedVoiceId);
    }

    /*暂停人物讲解动画*/
    public void stopSpeakAnim() {
        peopleStopSpeak();
    }


    /*清除播放过的语音缓存*/
    public void removeVoiceCache() {
        Jzvd.clearSavedProgress(this, mCurrentVoiceUrl);
    }

    /*语音语音讲解完毕后与服务器交互刷新地图marker状态*/
    public void changeMarkerState() {
        mThisTimeVisitedChildIdList.add(mClosedChild.getId());
        mPresenter.refreshLoginUserVisitedSpotOnServer(mUserId, mThisTimeVisitedChildIdList, "add");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放欢迎语音博播放器
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        //释放播放器资源
        Jzvd.releaseAllVideos();
        if(mCurrentVoiceUrl != null || !mCurrentVoiceUrl.equals("")){
            Jzvd.clearSavedProgress(this,mCurrentVoiceUrl);
        }
        mIsPlayingVoice = false;
        if (mAMap != null) {
            mAMap.setMyLocationEnabled(false);
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }
}
