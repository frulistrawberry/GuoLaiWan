package com.guolaiwan.ui.activity;



import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.ActivityEvent;
import com.guolaiwan.bean.Child;
import com.guolaiwan.bean.GuideSearchEvent;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.presenter.PayPresent;
import com.guolaiwan.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;

public class ProfessionalLivePayActivity extends BaseActivity implements IBaseVIew,View.OnClickListener {

    @BindView(R.id.rl_ali_pay)
    RelativeLayout mAliPayRl;
    @BindView(R.id.rl_wechart_pay)
    RelativeLayout mWeChatPayRl;

    private ProfessionalLiveOrderBean mProfessionalLiveOrderBean;
    private String mOrderId;
    private String mTotalFee;
    private PayPresent mPresenter;

    public static void launch(Context context,ProfessionalLiveOrderBean professionalLiveOrderBean){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
            return;
        }
        Intent intent = new Intent(context, ProfessionalLivePayActivity.class);
        intent.putExtra("professionalLiveOrderBean",professionalLiveOrderBean);
        context.startActivity(intent);
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_professional_live_pay);
        Intent intent = getIntent();
        mProfessionalLiveOrderBean = (ProfessionalLiveOrderBean) intent.getSerializableExtra("professionalLiveOrderBean");
        mOrderId = mProfessionalLiveOrderBean.getId();
        mTotalFee = mProfessionalLiveOrderBean.getTotalFee();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        mPresenter = new PayPresent(this);
    }

    @Override
    protected void initEvent() {
        mAliPayRl.setOnClickListener(this);
        mWeChatPayRl.setOnClickListener(this);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().showBack().setTitle("专业直播支付").show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_ali_pay:
                //mPresenter.professionalLivePay(mOrderId,mTotalFee,"ALIPAY");
                //TODO 支付暂时不做先调用修改订单状态接口，模拟支付成功后修改订单状态
                mPresenter.professionalLiveUpdateOrderState("PAYSUCCESS");
                break;
            case R.id.rl_wechart_pay:
                //mPresenter.professionalLivePay(mOrderId,mTotalFee,"WEICHAT");
                //TODO 支付暂时不做先调用修改订单状态接口，模拟支付成功后修改订单状态
                mPresenter.professionalLiveUpdateOrderState("PAYSUCCESS");
                break;
        }
    }

    /*支付成功后关闭界面*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActivityEvent(ActivityEvent activityEvent) {
        String action = activityEvent.action;
        if(action.equals("close")){
            finish();
        }
    }
}
