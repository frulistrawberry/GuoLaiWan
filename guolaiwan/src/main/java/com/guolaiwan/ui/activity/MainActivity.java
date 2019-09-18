package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.AppUtils;
import com.cgx.library.utils.SPUtils;
import com.cgx.library.utils.ToastUtils;
import com.cgx.library.utils.log.LogUtil;
import com.cgx.library.widget.TabBar;
import app.guolaiwan.com.guolaiwan.R;

import com.cgx.library.widget.dialog.AlertDialog;
import com.github.faucamp.simplertmp.packets.Data;
import com.guolaiwan.bean.GuideBean;
import com.guolaiwan.bean.UpdateBean;
import com.guolaiwan.constant.PreferenceConstant;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.presenter.MainPresenter;
import com.guolaiwan.ui.fragment.FriendCircleFragment;
import com.guolaiwan.ui.fragment.GuideFragment;
import com.guolaiwan.ui.fragment.HomeFragment;
import com.guolaiwan.ui.fragment.LiveFragment;
import com.guolaiwan.ui.fragment.MeFragment;
import com.guolaiwan.ui.iview.MainView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import io.reactivex.subjects.PublishSubject;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/20
 * 描述: 应用主界面
 */

public class MainActivity extends BaseActivity implements TabBar.OnTabCheckListener, AMapLocationListener, BaseActivity.OnKeyClickListener, MainView {
    public static final int PAGE_HOME = 0;
    public static final int PAGE_LIVE = 1;
    public static final int PAGE_GUIDE = 2;
    public static final int PAGE_ME = 4;
    public static final int PAGE_TOPIC = 3;

    @BindView(R.id.tabBar)
    public TabBar mTabBar;

    private HomeFragment mHomeFragment;
    private LiveFragment mLiveFragment;
    private GuideFragment mGuideFragment;
    private MeFragment mMeFragment;
    private FriendCircleFragment mFriendFragment;
    private Fragment mCurrentFragment;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private MainPresenter mPresenter;

    public static void launch(Context context){
        context.startActivity(new Intent(context,MainActivity.class));
    }

    public static void launch(Context context,boolean toGuide){
        Intent intent = new Intent(context,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("toGuide",toGuide);
        intent.putExtras(bundle);
        context.startActivity(intent);
        context.startActivity(new Intent(context,MainActivity.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            boolean toGuide = bundle.getBoolean("toGuide",false);
            if (toGuide){
                selectFragment(PAGE_GUIDE);
                mTabBar.setCurrentItem(PAGE_GUIDE);
            }
        }

        if(intent.getAction() != null && intent.getAction().equals(Intent.ACTION_VIEW)){
            Uri uri = intent.getData();
            if(uri != null){
                mPresenter.getGuideInfo();
            }
        }
    }

    @Override
    public void onTabSelected(View v, int position) {
        selectFragment(position);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        mTabBar.addTab(new TabBar.Tab().setCheckedColor(getResourceColor(R.color.green))
                        .setColor(getResourceColor(R.color.gray)).setText("首页")
                        .setNormalIcon(R.mipmap.ic_tab_home).setPressedIcon(R.mipmap.ic_tab_home_p))
                .addTab(new TabBar.Tab().setCheckedColor(getResourceColor(R.color.green))
                        .setColor(getResourceColor(R.color.gray)).setText("直播")
                        .setNormalIcon(R.mipmap.ic_tab_live).setPressedIcon(R.mipmap.ic_tab_live_p))
                .addTab(new TabBar.Tab().setCheckedColor(getResourceColor(R.color.green))
                        .setColor(getResourceColor(R.color.gray)).setText("导览")
                        .setNormalIcon(R.mipmap.ic_tab_guide).setPressedIcon(R.mipmap.ic_tab_guide_p))
                .addTab(new TabBar.Tab().setCheckedColor(getResourceColor(R.color.green))
                        .setColor(getResourceColor(R.color.gray)).setText("我发布")
                        .setNormalIcon(R.mipmap.ic_friend_un).setPressedIcon(R.mipmap.ic_friend))
                .addTab(new TabBar.Tab().setCheckedColor(getResourceColor(R.color.green))
                        .setColor(getResourceColor(R.color.gray)).setText("我的")
                        .setNormalIcon(R.mipmap.ic_tab_me).setPressedIcon(R.mipmap.ic_tab_me_p))
                .setOnTabCheckListener(this)
                .setCurrentItem(PAGE_HOME);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            boolean toGuide = bundle.getBoolean("toGuide",false);
            if (toGuide){
                selectFragment(PAGE_GUIDE);
                mTabBar.setCurrentItem(PAGE_GUIDE);
            }
        }

        Intent intent = getIntent();

        if(intent.getAction() != null && intent.getAction().equals(Intent.ACTION_VIEW)){
            Uri uri = intent.getData();
            if(uri != null){
                mPresenter.getGuideInfo();
            }
        }
    }

    @Override
    protected void initData() {

        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(this);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setInterval(10000);
        mLocationOption.setHttpTimeOut(20000);
        mLocationClient.setLocationOption(mLocationOption);
        EventBus.getDefault().register(this);
        update();
        mPresenter = new MainPresenter(this);
    }

    @Override
    protected void initEvent() {
        setOnKeyListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.startLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stopLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();
    }

    private void selectFragment(int position) {
        switch (position){
            case PAGE_HOME:
                if (mHomeFragment == null){
                    mHomeFragment = HomeFragment.newInstance();
                }
                selectFragment(mHomeFragment);
                break;
            case PAGE_LIVE:
                if (mLiveFragment == null){
                    mLiveFragment = LiveFragment.newInstance();
                }
                selectFragment(mLiveFragment);
                break;
            case PAGE_GUIDE:
                if (mGuideFragment == null){
                    mGuideFragment = GuideFragment.newInstance();
                }
                selectFragment(mGuideFragment);
                break;
            case PAGE_ME:
                if (mMeFragment == null){
                    mMeFragment = MeFragment.newInstance();
                }
                selectFragment(mMeFragment);
                break;
            case PAGE_TOPIC:
                if (mFriendFragment == null){
                    mFriendFragment = new FriendCircleFragment();
                }
                selectFragment(mFriendFragment);
                break;

        }

    }

    private void selectFragment(@NonNull Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (mCurrentFragment != fragment) {
            if (mCurrentFragment != null && mCurrentFragment.isAdded()) {
                transaction.hide(mCurrentFragment);
            }
            if (!fragment.isAdded()) {
                transaction.add(R.id.layout_container, fragment).commitAllowingStateLoss();
            } else {
                transaction.show(fragment).commitAllowingStateLoss();
            }
            mCurrentFragment = fragment;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                double latitude = aMapLocation.getLatitude();
                double longitude =  aMapLocation.getLongitude();
                LogUtil.json(TAG,aMapLocation.toStr());
                SPUtils.putFloat(PreferenceConstant.LATITUDE, (float) latitude);
                SPUtils.putFloat(PreferenceConstant.LONGITUDE, (float) longitude);
                SPUtils.putString("cityCode",aMapLocation.getAdCode());
                LatLng latLng = new LatLng(latitude,latitude);
                EventBus.getDefault().post(latLng);
            }else {
//                System.exit(0);
            }
        }else {
        }
    }

    @Subscribe
    public void onEventMainThread(String action){
        switch (action){
            case "page_guide":
                selectFragment(PAGE_GUIDE);
                mTabBar.setCurrentItem(PAGE_GUIDE);
                break;
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (mHomeFragment == null && fragment instanceof HomeFragment){
            mHomeFragment = (HomeFragment) fragment;
        }

        if (mLiveFragment == null && fragment instanceof LiveFragment){
            mLiveFragment = (LiveFragment) fragment;
        }

        if (mGuideFragment == null && fragment instanceof GuideFragment){
            mGuideFragment = (GuideFragment) fragment;
        }

        if (mMeFragment == null && fragment instanceof MeFragment){
            mMeFragment = (MeFragment) fragment;
        }
    }

    @Override
    public void clickBack() {
        new AlertDialog.Builder(this,getSupportFragmentManager())
                .setCancelable(true)
                .setTitle("提示")
                .setMessage("是否退出过来玩")
                .setNegativeText("取消")
                .setPositiveText("退出")
                .setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.exit(0);
                    }
                }).build().show();
    }

    private void update(){
        int versonCode = AppUtils.getAppInfo(getContext()).getVersionCode();
        RetrofitUtil.composeToSubscribe(HttpClient.getApiService().update(), new HttpObserver<UpdateBean>() {
            @Override
            public void onNext(String message, UpdateBean data) {
                if (versonCode < data.getVersionCode()){
                    android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(getContext())
                            .setTitle("更新提示")
                            .setMessage(data.getContent())
                            .setCancelable(!data.getForceUpdate())
                            .setPositiveButton("更新", (dialog1, which) -> {
                                Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getUrl()));
                                startActivity(viewIntent);
                                dialog1.dismiss();
                            })
                            .setNegativeButton("取消", (dialog12, which) -> {
                                if (data.getForceUpdate()){
                                    finish();
                                }else {
                                    dialog12.dismiss();
                                }
                            }).create();
                    dialog.show();
                }

            }

            @Override
            public void onError(int errCode, String errMessage) {

            }
            @Override
            public void onComplete() {

            }
        },getLifeSubject());
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().post("refresh_live");
    }

    @Override
    public void showGuideInfo(GuideBean data) {
        GuideActivity.launch(MainActivity.this,data.getMerchantId(),data.getLongitude(),data.getLatitude(),data.getUserId());
    }
}
