package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.IBaseVIew;
import com.cgx.library.utils.StringUtils;
import com.guolaiwan.presenter.CustomPayPresenter;
import com.guolaiwan.utils.CommonUtils;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/27
 * 描述:
 */

public class CustomPayActivity extends BaseActivity implements IBaseVIew{
    @BindView(R.id.et_price)
    EditText priceEt;
    @BindView(R.id.tv_merchant_name)
    TextView mMerchantNameTv;
    private CustomPayPresenter mPresenter;
    private String merchantId;

    public static void launch(Context context,String merchantId){
        Intent intent = new Intent(context,CustomPayActivity.class);
        intent.putExtra("merchantId",merchantId);
        context.startActivity(intent);
    }

    @OnClick(R.id.btn_pay)
    public void onClick(View v){
        String value = priceEt.getText().toString().trim();
        if (StringUtils.isEmpty(value)){
            CommonUtils.showMsg("请输入支付金额");
            return;
        }
        mPresenter.customPay(merchantId,value);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setTitle("到店支付").showBack().show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_custom_pay);
        mMerchantNameTv.setText("过来玩");
    }

    @Override
    protected void initData() {
        merchantId = getIntent().getStringExtra("merchantId");
        mPresenter = new CustomPayPresenter(this);
    }

    @Override
    protected void initEvent() {

    }
}
