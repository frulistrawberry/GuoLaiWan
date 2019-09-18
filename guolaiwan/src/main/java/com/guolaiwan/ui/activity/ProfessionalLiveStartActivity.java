package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.model.ProfessionalLiveStartModel;
import com.guolaiwan.presenter.ProfessionalLiveStartPresenter;
import com.guolaiwan.ui.iview.ProfessionalLiveOrderView;
import com.guolaiwan.ui.iview.ProfessionalLiveStartView;
import com.guolaiwan.utils.CommonUtils;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;

public class ProfessionalLiveStartActivity  extends BaseActivity implements ProfessionalLiveStartView, View.OnClickListener {


    //直播id
    @BindView(R.id.tv_live_id)
    TextView mLiveIdTv;
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
    //订单状态
    @BindView(R.id.tv_order_status)
    TextView mOrderStatusTv;
    //开始直播按钮
    @BindView(R.id.tv_start)
    TextView mStartTv;

    private ProfessionalLiveOrderBean mProfessionalLiveOrderBean;
    private String mLiveId;
    private String mStartTime;
    private String mEndTime;
    private String mCameraCount;
    private String mIsMatPlay;
    private String mSaveMemorySize;
    private String mStatus;

    private ProfessionalLiveStartPresenter mProfessionalLiveStartPresenter;



    public static void launch(Context context){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
            return;
        }
        Intent intent = new Intent(context, ProfessionalLiveStartActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_professional_live_start);
        mProfessionalLiveStartPresenter.getProfessionalLiveOrderInfo();
    }

    @Override
    protected void initData() {
        mProfessionalLiveStartPresenter = new ProfessionalLiveStartPresenter(this);
    }

    @Override
    protected void initEvent() {
        mStartTv.setOnClickListener(this);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().showBack().setTitle("专业直播").show();
    }

    @Override
    public void setProgfessionalLiveOrderInfo(ProfessionalLiveOrderBean professionalLiveOrderBean) {
        mProfessionalLiveOrderBean = professionalLiveOrderBean;
        mLiveId = professionalLiveOrderBean.getLiveId();
        mCameraCount = professionalLiveOrderBean.getCount();
        mStartTime = professionalLiveOrderBean.getStartTime();
        mEndTime = professionalLiveOrderBean.getEndTime();
        mIsMatPlay = professionalLiveOrderBean.getIsMatPlay();
        mSaveMemorySize = professionalLiveOrderBean.getRecordSize();
        mStatus = professionalLiveOrderBean.getStatus();

        mLiveIdTv.setText(mLiveId);
        mStartDateTv.setText(mStartTime);
        mEndDateTv.setText(mEndTime);
        mCameraCountTv.setText(mCameraCount + "台");
        if(Integer.parseInt(mSaveMemorySize) > 0){
            mSaveTv.setText("是");
            mSaveHourLl.setVisibility(View.VISIBLE);
            mSaveHourTv.setText(mSaveMemorySize + "小时");
        }else {
            mSaveTv.setText("否");
        }
        if(mIsMatPlay.equals("0")){
            mMatPlayTv.setText("否");
        }else {
            mMatPlayTv.setText("是");
        }
        if(mStatus.equals("PAYSUCCESS")){
            mOrderStatusTv.setText("已支付");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_start:
                ProfessionalLiveCameraChoiceActivity.launch(getContext(),mProfessionalLiveOrderBean);
                finish();
                break;
        }
    }
}
