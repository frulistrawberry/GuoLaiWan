package com.guolaiwan.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autonavi.ae.dice.InitConfig;
import com.autonavi.rtbt.IFrameForRTBT;
import com.cgx.library.base.BaseFragment;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.FrescoUtil;
import com.cgx.library.utils.SPUtils;
import com.cgx.library.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import app.guolaiwan.com.guolaiwan.R;

import com.guolaiwan.bean.UserBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.activity.CheckActivity;
import com.guolaiwan.ui.activity.EditUserInfoActivity;
import com.guolaiwan.ui.activity.JiFenListActivity;
import com.guolaiwan.ui.activity.LoginActivity;
import com.guolaiwan.ui.activity.MyAddressActivity;
import com.guolaiwan.ui.activity.MyCollectActivity;
import com.guolaiwan.ui.activity.MyVideoPicActivity;
import com.guolaiwan.ui.activity.OrderListActivity;
import com.guolaiwan.ui.activity.ProfessionalLiveRecordVideoActivity;
import com.guolaiwan.ui.activity.SettingActivity;
import com.guolaiwan.ui.activity.ShopCartActivity;
import com.guolaiwan.utils.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/26
 * 描述: 我的
 */

public class MeFragment extends BaseFragment {


    @BindView(R.id.tv_nick_name)
    public TextView mNickNameTv;
    @BindView(R.id.iv_img)
    public SimpleDraweeView mHeadIv;
    @BindView(R.id.tv_integral)
    public TextView mIntegralTv;
    @BindView(R.id.btn_merchant_order)
    public LinearLayout merchantOderBtn;
    @BindView(R.id.btn_check)
    public LinearLayout checkBtn;

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @OnClick({R.id.btn_login,R.id.btn_shop_chart,R.id.btn_address,R.id.btn_collect,R.id.btn_user_order,R.id.btn_merchant_order
            ,R.id.btn_check,R.id.btn_circle,R.id.btn_jifen,R.id.btn_video})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_login:
                if (!CommonUtils.isLogin())
                    LoginActivity.launch(getContext());
                else {
                    // 编辑个人信息
                    EditUserInfoActivity.launch(getActivity());
                }
                break;
            case R.id.btn_shop_chart:
                ShopCartActivity.launch(getContext());
                break;
            case R.id.btn_address:
                MyAddressActivity.launch(getActivity());
                break;
            case R.id.btn_collect:
                MyCollectActivity.launch(getActivity());
                break;
            case R.id.btn_user_order:
                OrderListActivity.launch(getContext(),"USER");
                break;
            case R.id.btn_merchant_order:
                OrderListActivity.launch(getContext(),"MERCHANT");
                break;
            case R.id.btn_check:
                CheckActivity.launch(getContext());
                break;
            case R.id.btn_circle:
                startActivity(new Intent(getContext(),MyVideoPicActivity.class));
                break;
            case R.id.btn_jifen:
                JiFenListActivity.launch(getContext());
                break;

            case R.id.btn_video:
                ProfessionalLiveRecordVideoActivity.launch(getContext());
                break;
        }
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.fragment_me,parent,false);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setTitle("我的").setRightImage(R.mipmap.ic_setting, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.launch(getContext());
            }
        }).show();
    }

    @Override
    protected void initData() {}

    @Override
    protected void initView() {}

    @Override
    protected void initEvent() {}


    @Override
    public void onResume() {
        super.onResume();
        if (!CommonUtils.isLogin()){
            mNickNameTv.setText("立即登录");
            FrescoUtil.getInstance().loadResourceImage(mHeadIv,R.mipmap.ic_launcher);
            mIntegralTv.setVisibility(View.GONE);
            merchantOderBtn.setVisibility(View.GONE);
            checkBtn.setVisibility(View.GONE);
        }else {
            if (CommonUtils.isMerchant()){
                merchantOderBtn.setVisibility(View.VISIBLE);
                checkBtn.setVisibility(View.VISIBLE);
            }else {
                merchantOderBtn.setVisibility(View.GONE);
                checkBtn.setVisibility(View.GONE);
            }
            RetrofitUtil.composeToSubscribe(HttpClient.getApiService().getUser(CommonUtils.getUserId()), new HttpObserver<UserBean>() {

                @Override
                public void onComplete() {

                }

                @Override
                public void onNext(String message, UserBean data) {
                    if (TextUtils.isEmpty(data.getUserNickname())){
                        mNickNameTv.setText("过来玩会员_"+data.getId());
                    }else {
                        mNickNameTv.setText(data.getUserNickname());
                        SPUtils.putString("nickName",data.getUserNickname());
                    }

                    if (TextUtils.isEmpty(data.getUserIntegral())){
                        mIntegralTv.setText("积分:0");
                    }else {
                        mIntegralTv.setText("积分:"+data.getUserIntegral());
                    }

                    if (TextUtils.isEmpty(data.getUserHeadimg())){
                        FrescoUtil.getInstance().loadResourceImage(mHeadIv,R.mipmap.ic_launcher);
                    }else {
                        FrescoUtil.getInstance().loadNetImage(mHeadIv,data.getUserHeadimg());
                        SPUtils.putString("headImage",data.getUserHeadimg());

                    }
                    if (!TextUtils.isEmpty(data.getUserPhone()))
                    SPUtils.putString("phone",data.getUserPhone());
                    if (!TextUtils.isEmpty(data.getTrueName()))
                    SPUtils.putString("realName",data.getTrueName());
                    if (!TextUtils.isEmpty(data.getCompanyName()))
                    SPUtils.putString("company",data.getCompanyName());
                }

                @Override
                public void onError(int errCode, String errMessage) {}
            },getLifeSubject());
        }
    }
}
