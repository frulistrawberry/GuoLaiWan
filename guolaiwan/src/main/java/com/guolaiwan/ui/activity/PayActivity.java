package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.OrderParam;
import com.guolaiwan.presenter.PayPresent;
import com.guolaiwan.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.OnClick;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/25
 * 描述:
 */

public class PayActivity extends BaseActivity implements IBaseVIew {

    private String mType;
    private PayPresent mPresenter;
    private List<Map<String,Object>> mOrders;
    private String mProductId;
    private String mAddressId;
    private int productNum = 1;

    public static void launch(Context context, String type, List<OrderParam> orders){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
            return;
        }
        Intent intent = new Intent(context,PayActivity.class);
        intent.putExtra("orders", (Serializable) orders);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    public static void launch(Context context,String type, String productId,String addressId){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
            return;
        }
        Intent intent = new Intent(context,PayActivity.class);
        intent.putExtra("productId",  productId);
        intent.putExtra("type",type);
        intent.putExtra("addressId",addressId);
        context.startActivity(intent);
    }

    public static void launch(Context context,String type, String productId,String addressId,int productNum){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
            return;
        }
        Intent intent = new Intent(context,PayActivity.class);
        intent.putExtra("productId",  productId);
        intent.putExtra("type",type);
        intent.putExtra("addressId",addressId);
        intent.putExtra("productNum",productNum);
        context.startActivity(intent);
    }

    /*新增打开支付界面方法:购物车支付带联系人*/
    public static void launch(Context context, String type, List<OrderParam> orders,String addressId){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
            return;
        }
        Intent intent = new Intent(context,PayActivity.class);
        intent.putExtra("orders", (Serializable) orders);
        intent.putExtra("type",type);
        intent.putExtra("addressId", addressId);
        context.startActivity(intent);
    }

    @OnClick({R.id.btn_recharge_wechat,R.id.btn_recharge_alipay})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_recharge_alipay:
                if (mType.equals("product")){
                    mPresenter.payProduct(mProductId,mAddressId,"ALIPAY",productNum);
                }else if (mType.equals("shopCart")){
                    mPresenter.payAllOrders(mOrders,"ALIPAY");
                }else if (mType.equals("order")){
                    mPresenter.payOrder(mProductId,"ALIPAY");
                }
                break;
            case R.id.btn_recharge_wechat:
                if (mType.equals("product")){
                    mPresenter.payProduct(mProductId,mAddressId,"WECHAT",productNum);
                }else if (mType.equals("shopCart")){
                    //mPresenter.payAllOrders(mOrders,"WECHAT");
                    //使用新增支付接口：微信支付带联系人
                    mPresenter.payAllOrders(mOrders,"WECHAT",mAddressId);
                }else if (mType.equals("order")){
                    mPresenter.payOrder(mProductId,"WECHAT");
                }
                break;
        }
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setTitle("选择支付方式").showBack().show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_pay);
    }

    @Override
    protected void initData() {
        mType = getIntent().getStringExtra("type");
        if (mType.equals("product")||mType.equals("order")){
            mProductId = getIntent().getStringExtra("productId");
            mAddressId = getIntent().getStringExtra("addressId");
            productNum = getIntent().getIntExtra("productNum",1);
        }else if (mType.equals("shopCart")){
            mAddressId = getIntent().getStringExtra("addressId");
            mOrders = new ArrayList<>();
            List<OrderParam> orders = (List<OrderParam>) getIntent().getSerializableExtra("orders");
            for (OrderParam order : orders) {
                Map<String,Object> param = new HashMap<>();
                param.put("productId",order.getProductId());
                param.put("orderId",order.getOrderId());
                param.put("productNum",order.getProductNum());
                mOrders.add(param);
            }
        }
        mPresenter = new PayPresent(this);
    }

    @Override
    protected void initEvent() {
        EventBus.getDefault().register(this);
    }


    String orderId;
    @Subscribe
    public void onEventMainThread(String str){
        if (str.equals("closePayActivity")){
            finish();
            if (orderId != null) {
                OrderInfoActivity.launch(this,orderId);
            }
        }else {
            orderId = str;
        }
    }


}
