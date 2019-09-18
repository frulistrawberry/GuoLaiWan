package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.ToastUtils;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.presenter.ProfessionalLiveOrderPresenter;
import com.guolaiwan.ui.iview.ProfessionalLiveOrderView;
import com.guolaiwan.utils.CommonUtils;

import java.util.Date;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;

public class ProfessionalLiveOrderActivity extends BaseActivity implements ProfessionalLiveOrderView, View.OnClickListener {


    //开始时间
    @BindView(R.id.tv_date_start)
    TextView mStartDateTv;
    //结束时间
    @BindView(R.id.tv_date_end)
    TextView mEndDateTv;
    //机位数量
    @BindView(R.id.tv_camera_count)
    TextView mCameraCountTv;
    //是否录制
    @BindView(R.id.tv_save)
    TextView mSaveTv;
    //录制时长
    @BindView(R.id.ll_save_hour)
    LinearLayout mSaveHourLl;
    @BindView(R.id.tv_save_hour)
    TextView mSaveHourTv;
    //是否垫播
    @BindView(R.id.tv_mat_play)
    TextView mMatPlayTv;
    //总计价格
    @BindView(R.id.tv_total_price)
    TextView mTotalPriceTv;
    //确认付款
    @BindView(R.id.tv_pay)
    TextView mPayText;
    //各数据值
    private String mStartTime;
    private String mEndTime;
    private String mCameraCount;
    private String mIsSave;
    private String mSaveTime;
    private String mIsMatPlay;
    private String mTotalPrice;

    private ProfessionalLiveOrderPresenter mPresenter;


    public static void launch(Context context,String startTime,String endTime,String cameraCount,String isSave,String saveTime,String isMatPlay){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
            return;
        }
        Intent intent = new Intent(context, ProfessionalLiveOrderActivity.class);
        intent.putExtra("startTime",startTime);
        intent.putExtra("endTime",endTime);
        intent.putExtra("cameraCount",cameraCount);
        intent.putExtra("isSave",isSave);
        intent.putExtra("saveTime",saveTime);
        intent.putExtra("isMatPlay",isMatPlay);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_professional_live_order);
        Intent intent = getIntent();
        mStartTime = intent.getStringExtra("startTime");
        mEndTime = intent.getStringExtra("endTime");
        mCameraCount = intent.getStringExtra("cameraCount");
        mIsSave = intent.getStringExtra("isSave");
        mSaveTime = intent.getStringExtra("saveTime");
        mIsMatPlay = intent.getStringExtra("isMatPlay");

        mStartDateTv.setText(mStartTime);
        mEndDateTv.setText(mEndTime);
        mCameraCountTv.setText(mCameraCount + "台");

        if(mIsSave.equals("YES")){
            mSaveTv.setText("是");
            mSaveHourLl.setVisibility(View.VISIBLE);
            mSaveHourTv.setText(mSaveTime + "小时");
        }

        if(mIsSave.equals("NO")){
            mSaveTv.setText("否");
        }

        if(mIsMatPlay.equals("YES")){
            mMatPlayTv.setText("是");
        }

        if(mIsMatPlay.equals("NO")){
            mMatPlayTv.setText("否");
        }
        mPresenter.checkPrice(mStartTime,mEndTime,mCameraCount,mIsSave,mSaveTime,mIsMatPlay);
    }

    @Override
    protected void initData() {
        mPresenter = new ProfessionalLiveOrderPresenter(this);
    }

    @Override
    protected void initEvent() {
        mPayText.setOnClickListener(this);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().showBack().setTitle("支付确认").show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_pay:
                if(mTotalPrice == null || mTotalPrice.equals("")){
                    ToastUtils.showToast(getContext(),"温馨提示:获取总计价格失败请重试。");
                    return;
                }
                mPresenter.getPayInfo(mStartTime,mEndTime,mCameraCount,mIsSave,mSaveTime,mIsMatPlay,mTotalPrice);
                break;
        }
    }

    @Override
    public void gotoPay(ProfessionalLiveOrderBean professionalLiveOrderBean) {
        ProfessionalLivePayActivity.launch(this,professionalLiveOrderBean);
        finish();
    }

    @Override
    public void setCheckedPrice(String checkedPrice) {
        mTotalPrice = checkedPrice;
        mTotalPriceTv.setText(checkedPrice + "元");
    }
}
