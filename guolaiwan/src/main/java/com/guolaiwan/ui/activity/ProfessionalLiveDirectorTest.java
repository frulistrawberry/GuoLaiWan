package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.ToastUtils;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.utils.CommonUtils;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;

public class ProfessionalLiveDirectorTest extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.rl_load_signal)
    RelativeLayout mRl1;

    @BindView(R.id.rl_load_signal2)
    RelativeLayout mRl2;

    @BindView(R.id.rl_load_signal3)
    RelativeLayout mRl3;

    @BindView(R.id.rl_load_signal4)
    RelativeLayout mRl4;

    @BindView(R.id.rl_load_signal5)
    RelativeLayout mRl5;

    @BindView(R.id.rl_load_signal6)
    RelativeLayout mRl6;

    @BindView(R.id.rl_load_signal7)
    RelativeLayout mRl7;

    @BindView(R.id.tv_live)
    TextView mLiveTv;

    public static void launch(Context context){
        Intent intent = new Intent(context, ProfessionalLiveDirectorTest.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_professional_live_director_test);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mRl1.setOnClickListener(this);
        mRl2.setOnClickListener(this);
        mRl3.setOnClickListener(this);
        mRl4.setOnClickListener(this);
        mRl5.setOnClickListener(this);
        mRl6.setOnClickListener(this);
        mRl7.setOnClickListener(this);
        mLiveTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ToastUtils.showToast(getContext(),"温馨提示:程序猿正在努力开发，敬请期待...");
    }
}
