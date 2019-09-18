package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.guolaiwan.bean.AddressBean;
import com.guolaiwan.bean.OrderParam;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.ShopChartBean;
import com.guolaiwan.utils.CommonUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;

public class BuyNowActivity extends BaseActivity {

    @BindView(R.id.ll_no_cancat)
    public LinearLayout mNoCancatLl;
    @BindView(R.id.ll_cancat_info)
    public LinearLayout mCancatInfoLl;
    @BindView(R.id.tv_name)
    public TextView mNameTv;
    @BindView(R.id.tv_phone)
    public TextView mPhoneTv;
    @BindView(R.id.tv_address)
    public TextView mAddressTv;
    @BindView(R.id.pic_sdv)
    SimpleDraweeView mPicSdv;
    @BindView(R.id.tv_product_name)
    public TextView mProductNameTv;
    @BindView(R.id.tv_price)
    public TextView mPriceTv;
    @BindView(R.id.tv_money)
    public TextView mMoneyTv;
    @BindView(R.id.tv_add)
    public TextView mAddTv;
    @BindView(R.id.tv_sub)
    public TextView mSubTv;
    @BindView(R.id.tv_count)
    public TextView mCountTv;
    @BindView(R.id.tv_pay)
    public TextView mPayTv;

    private int mProductCount = 1;
    private ProductBean mProductBean;
    private String mDistributorId;

    private BigDecimal mTotalMoney;
    private BigDecimal mSingleProductMoney;

    public static void launch(Context context,String distributorId,int buyCount,ProductBean productBean){
        Intent intent = new Intent(context,BuyNowActivity.class);
        intent.putExtra("distributorId",distributorId);
        intent.putExtra("productBean",productBean);
        intent.putExtra("buyCount",buyCount);
        context.startActivity(intent);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().showBack().setTitle("立即购买").show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_buy_now);
        if(mProductBean != null){
            mPicSdv.setImageURI(mProductBean.getProductShowPic());
            mProductNameTv.setText(mProductBean.getProductName());
            mPriceTv.setText("￥" + mProductBean.getProductPrice());
            mTotalMoney = new BigDecimal(Float.toString(mProductBean.getProductPrice()));
            mSingleProductMoney = new BigDecimal(Float.toString(mProductBean.getProductPrice()));
            mCountTv.setText(mProductCount + "");
            mTotalMoney = mSingleProductMoney.multiply(new BigDecimal(mProductCount));
            mMoneyTv.setText("合计：￥" + mTotalMoney);
        }
    }

    @Override
    protected void initData() {
        mDistributorId = getIntent().getStringExtra("distributorId");
        mProductBean = (ProductBean) getIntent().getSerializableExtra("productBean");
        mProductCount = getIntent().getIntExtra("buyCount",1);
    }

    @Override
    protected void initEvent() {}

    @OnClick({R.id.ll_no_cancat,R.id.tv_change_cancat_info,R.id.tv_add,R.id.tv_sub,R.id.tv_pay})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_no_cancat:
                MyAddressActivity.launch(this,1,true);
                break;

            case R.id.tv_change_cancat_info:
                MyAddressActivity.launch(this,1,true);
                break;

            case R.id.tv_add:
                mProductCount = mProductCount + 1;
                mCountTv.setText(mProductCount + "");
                mTotalMoney = mTotalMoney.add(mSingleProductMoney);
                mMoneyTv.setText("合计：￥" + mTotalMoney);
                break;

            case R.id.tv_sub:
                if(mProductCount > 1){
                    mProductCount = mProductCount - 1;
                    mCountTv.setText(mProductCount + "");
                    mTotalMoney = mTotalMoney.subtract(mSingleProductMoney);
                    mMoneyTv.setText("合计：￥" + mTotalMoney);
                }
                break;

            case R.id.tv_pay:
                if(mNameTv.getText().equals("") || mPhoneTv.getText().equals("") || mAddressTv.getText().equals("")){
                    ToastUtils.showToast(getContext(),"温馨提示:请添加或选择联系人。");
                    return;
                }
                PayActivity.launch(this,"product",mDistributorId,mAddressBean.getId()+"",mProductCount);
                finish();
                break;
        }
    }

    private AddressBean mAddressBean;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            mAddressBean = (AddressBean) data.getSerializableExtra("addressBean");
            if(mNoCancatLl.getVisibility() == View.VISIBLE){
                mNoCancatLl.setVisibility(View.GONE);
            }

            if(mCancatInfoLl.getVisibility() == View.GONE){
                mCancatInfoLl.setVisibility(View.VISIBLE);
            }
            mNameTv.setText(mAddressBean.getConsigneeName());
            mPhoneTv.setText(mAddressBean.getConsigneePhone());
            mAddressTv.setText(mAddressBean.getProvince() + " " + mAddressBean.getCity() + " " + mAddressBean.getDistrict() + " " + mAddressBean.getConsigneeAddress());
        }
    }
}
