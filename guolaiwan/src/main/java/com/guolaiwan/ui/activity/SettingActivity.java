package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.AppUtils;
import com.cgx.library.utils.DeviceUtils;
import com.cgx.library.utils.PhoneUtils;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.DataCleanManager;

import java.text.MessageFormat;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/9/009.
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.text_setting_version)
    TextView versionTv;
    @BindView(R.id.tv_cache)
    TextView cacheTv;
    @BindView(R.id.btn_setting_logout)
    Button logoutBtn;


    public static void launch(Context context){
        context.startActivity(new Intent(context,SettingActivity.class));
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setTitle("设置").showBack().show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_setting);
        versionTv.setText(MessageFormat.format("v{0}", AppUtils.getAppInfo(this).getVersionName()));
        try {
            cacheTv.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            cacheTv.setText("");
            e.printStackTrace();
        }
        if (CommonUtils.isLogin()){
            logoutBtn.setVisibility(View.VISIBLE);
        }else
            logoutBtn.setVisibility(View.GONE);
    }

    @OnClick({R.id.btn_setting_clear,R.id.btn_setting_logout})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_setting_clear:
                DataCleanManager.clearAllCache(this);
                cacheTv.setText("");
                CommonUtils.showMsg("清除成功");
                break;
            case R.id.btn_setting_logout:
                CommonUtils.clearUser();
                finish();
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }
}
