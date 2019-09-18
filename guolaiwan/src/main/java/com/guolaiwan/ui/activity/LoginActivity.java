package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.IBaseVIew;
import com.cgx.library.utils.RegexUtils;
import com.cgx.library.utils.StringUtils;
import com.cgx.library.utils.log.LogUtil;
import app.guolaiwan.com.guolaiwan.R;
import com.guolaiwan.presenter.LoginPresenter;
import com.guolaiwan.utils.CommonUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import mabeijianxi.camera.util.Log;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/18
 * 描述:
 */

public class LoginActivity extends BaseActivity implements IBaseVIew, UMAuthListener {

    @BindView(R.id.et_phone)
    EditText mPhoneEt;
    @BindView(R.id.et_password)
    EditText mPasswordEt;

    LoginPresenter mPresenter;

    public static void launch(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.btn_login,R.id.iv_wx_login,R.id.btn_login_back,R.id.btn_forget_pwd,R.id.btn_register})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_login:
                String phone = mPhoneEt.getText().toString().trim();
                String password = mPasswordEt.getText().toString().trim();
                if (!RegexUtils.isMobileExact(phone)){
                    CommonUtils.showMsg("请输入正确的手机号");
                    return;
                }
                if (StringUtils.isEmpty(password)){
                    CommonUtils.showMsg("请输入密码");
                    return;
                }
                mPresenter.phoneLogin(phone,password);
                break;
            case R.id.iv_wx_login:
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, this);
                break;
            case R.id.btn_register:
                RegisterActivity.launch(this);
                break;
            case R.id.btn_forget_pwd:
                ResetPasswordActivity.launch(this);
                break;
            case R.id.btn_login_back:
                finish();
                break;
        }
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_login);

    }

    @Override
    protected void initData() {
        mPresenter = new LoginPresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        showLoadingDialog();
    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
        dismissLoadingDialog();
        mPresenter.weChatLogin(map.get("openid"),map.get("name"),map.get("iconurl"));
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        CommonUtils.error(i,throwable.getMessage());
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        CommonUtils.showMsg("取消登录");
    }
}
