package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.IBaseVIew;
import com.cgx.library.utils.RegexUtils;
import com.cgx.library.utils.StringUtils;
import app.guolaiwan.com.guolaiwan.R;
import com.guolaiwan.presenter.RegisterPresenter;
import com.guolaiwan.presenter.RepPasswordPresenter;
import com.guolaiwan.utils.CommonUtils;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/21/021.
 */

public class ResetPasswordActivity extends BaseActivity implements IBaseVIew{

    @BindView(R.id.tv_code)
    TextView mVerifyCodeTv;
    @BindView(R.id.et_phone)
    EditText mPhoneEt;
    @BindView(R.id.et_password)
    EditText mPasswordEt;
    @BindView(R.id.et_code)
    EditText mVerifyCodeEt;

    RepPasswordPresenter mPresenter;


    public static void launch(Context context){
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.btn_login,R.id.btn_register_back,R.id.tv_code})
    public void onClick(View v){
        String phone = mPhoneEt.getText().toString().trim();
        String password = mPasswordEt.getText().toString().trim();
        String code = mVerifyCodeEt.getText().toString().trim();
        switch (v.getId()){
            case R.id.btn_login:

                if (!RegexUtils.isMobileExact(phone)){
                    CommonUtils.showMsg("请输入正确的手机号");
                    return;
                }
                if (StringUtils.isEmpty(password)){
                    CommonUtils.showMsg("请输入密码");
                    return;
                }
                if (code.length()!=6){
                    CommonUtils.showMsg("请输入正确的验证码");
                    return;
                }
                mPresenter.repPassword(phone,password,code);
                break;
            case R.id.tv_code:
                if (!RegexUtils.isMobileExact(phone)){
                    CommonUtils.showMsg("请输入正确的手机号");
                    return;
                }
                new CountDownTimer(60000,1000).start();
                mPresenter.getRepPasswordCode(phone);
                break;
            case R.id.btn_login_back:
                finish();
                break;
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_reset_password);
    }

    @Override
    protected void initData() {
        mPresenter = new RepPasswordPresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    public class CountDownTimer extends android.os.CountDownTimer{

        public CountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mVerifyCodeTv.setTextColor(Color.parseColor("#3a3a3a"));
            mVerifyCodeTv.setText(MessageFormat.format("{0}s", millisUntilFinished / 1000));

        }

        @Override
        public void onFinish() {
            mVerifyCodeTv.setTextColor(Color.parseColor("#01cb9e"));
            mVerifyCodeTv.setText("获取");
        }
    }
}
