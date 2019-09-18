package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.ToastUtils;
import com.guolaiwan.bean.ProfessionalLiveDirectorBean;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.bean.ProfessionalLiveSubLiveBean;
import com.guolaiwan.presenter.ProfessionalLiveCameraChoicePresenter;
import com.guolaiwan.ui.iview.ProfessionalLiveCameraChoiceView;
import com.guolaiwan.utils.CommonUtils;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;

public class ProfessionalLiveCameraChoiceActivity extends BaseActivity implements ProfessionalLiveCameraChoiceView,View.OnClickListener {


    //机位选择控件
    @BindView(R.id.ll_camera_choice)
    LinearLayout mCameraChoiceLl;
    //机位一
    @BindView(R.id.tv_camera_1)
    TextView mCamera1Tv;
    //机位二
    @BindView(R.id.tv_camera_2)
    TextView mCamera2Tv;
    //机位三
    @BindView(R.id.tv_camera_3)
    TextView mCamera3Tv;
    //机位四
    @BindView(R.id.tv_camera_4)
    TextView mCamera4Tv;
    //机位五
    @BindView(R.id.tv_camera_5)
    TextView mCamera5Tv;
    //机位六
    @BindView(R.id.tv_camera_6)
    TextView mCamera6Tv;
    //导播
    @BindView(R.id.tv_director)
    TextView mDirectorTv;

    private ProfessionalLiveCameraChoicePresenter mProfessionalLiveCameraChoicePresenter;
    private ProfessionalLiveOrderBean mProfessionalLiveOrderBean;
    private String mLiveId;
    private int mCameraCount;

    public static void launch(Context context,ProfessionalLiveOrderBean professionalLiveOrderBean){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
            return;
        }
        Intent intent = new Intent(context, ProfessionalLiveCameraChoiceActivity.class);
        intent.putExtra("professionalLiveOrderBean",professionalLiveOrderBean);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_professional_live_camera_choice);
        mProfessionalLiveOrderBean = (ProfessionalLiveOrderBean) getIntent().getSerializableExtra("professionalLiveOrderBean");
        mLiveId = mProfessionalLiveOrderBean.getLiveId();
        mCameraCount = Integer.parseInt(mProfessionalLiveOrderBean.getCount());
        for(int i = 0;i < mCameraCount;i++){
            View childAt = mCameraChoiceLl.getChildAt(i);
            childAt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        mProfessionalLiveCameraChoicePresenter = new ProfessionalLiveCameraChoicePresenter(this);
    }

    @Override
    protected void initEvent() {
        mCamera1Tv.setOnClickListener(this);
        mCamera2Tv.setOnClickListener(this);
        mCamera3Tv.setOnClickListener(this);
        mCamera4Tv.setOnClickListener(this);
        mCamera5Tv.setOnClickListener(this);
        mCamera6Tv.setOnClickListener(this);
        mDirectorTv.setOnClickListener(this);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().showBack().setTitle("机位选择").show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_camera_1:
                //调用接口判断机位是否可用
                mProfessionalLiveCameraChoicePresenter.professionalLiveCameraUseable(mLiveId,1 + "");
                break;
            case R.id.tv_camera_2:
                //调用接口判断机位是否可用
                mProfessionalLiveCameraChoicePresenter.professionalLiveCameraUseable(mLiveId,2 + "");
                break;
            case R.id.tv_camera_3:
                //调用接口判断机位是否可用
                mProfessionalLiveCameraChoicePresenter.professionalLiveCameraUseable(mLiveId,3 + "");
                break;
            case R.id.tv_camera_4:
                //调用接口判断机位是否可用
                mProfessionalLiveCameraChoicePresenter.professionalLiveCameraUseable(mLiveId,4 + "");
                break;
            case R.id.tv_camera_5:
                //调用接口判断机位是否可用
                mProfessionalLiveCameraChoicePresenter.professionalLiveCameraUseable(mLiveId,5 + "");
                break;
            case R.id.tv_camera_6:
                //调用接口判断机位是否可用
                mProfessionalLiveCameraChoicePresenter.professionalLiveCameraUseable(mLiveId,6 + "");
                break;
            case R.id.tv_director:
                //进入导播界面
                mProfessionalLiveCameraChoicePresenter.professionalLiveDirectorUseable(mLiveId);
                break;
        }
    }

    @Override
    public void setCameraUsableCheckResult(ProfessionalLiveSubLiveBean professionalLiveSubLiveBean) {
        String cameraInUse = professionalLiveSubLiveBean.getInUse();
        if(cameraInUse.equals("1")){
            //机位已被占用
            ToastUtils.showToast(getContext(),"温馨提示:该机位已被占用，选择其他机位试试吧。");
        }else {
            //机位可用
            //进入直播界面
            ProfessionalLiveLiveActivity.launch(getContext(),professionalLiveSubLiveBean);
            finish();
        }
    }

    @Override
    public void setDirectorUsableCheckedResult(ProfessionalLiveDirectorBean professionalLiveDirectorBean) {
        String directorInUse = professionalLiveDirectorBean.getInUse();
        if(directorInUse.equals("1")){
            //导播位已被占用
            ToastUtils.showToast(getContext(),"温馨提示:导播位已被占用。");
        }else {
            //机位可用
            //进入导播界面
            ProfessionalLiveDirectorActivity.launch(getContext(),professionalLiveDirectorBean);
            finish();
        }
    }
}
