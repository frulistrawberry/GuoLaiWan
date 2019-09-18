package com.guolaiwan.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.ToastUtils;
import com.cgx.library.widget.CustomLoadMoreView;
import com.cgx.library.widget.dialog.AlertDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.AddressBean;
import com.guolaiwan.bean.OrderParam;
import com.guolaiwan.bean.ShopChartBean;
import com.guolaiwan.presenter.ShopCartPresent;
import com.guolaiwan.ui.adapter.ShopChartAdapter;
import com.guolaiwan.ui.iview.ShopCartView;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import mabeijianxi.camera.util.Log;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/24
 * 描述:
 */

public class ShopCartActivity extends BaseActivity implements ShopCartView, ShopChartAdapter.Callback, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.layout_ptr)
    public PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;
    @BindView(R.id.checkbox)
    public CheckBox mCheckBox;
    @BindView(R.id.tv_money)
    public TextView mMoneyTv;
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

    private ShopChartAdapter mAdapter;
    private ShopCartPresent mPresenter;
    private int mCurrentPage = 1;

    public static void launch(Context context){
        if (CommonUtils.isLogin()){
            Intent intent = new Intent(context,ShopCartActivity.class);
            context.startActivity(intent);
        }else {
            LoginActivity.launch(context);
        }

    }

    @OnClick({R.id.tv_pay,R.id.ll_no_cancat,R.id.tv_change_cancat_info})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_pay:
                if(mNameTv.getText().equals("") || mPhoneTv.getText().equals("") || mAddressTv.getText().equals("")){
                    ToastUtils.showToast(getContext(),"温馨提示:请添加或选择联系人。");
                    return;
                }
                List<ShopChartBean> products = mAdapter.getData();
                if (CollectionUtils.isEmpty(products)){
                    CommonUtils.showMsg("温馨提示:请选择要结算的商品。");
                    return;
                }
                List<OrderParam> orders = new ArrayList<>();
                for (ShopChartBean product : products) {
                    OrderParam order = new OrderParam();
                    order.setOrderId(product.getId()+"");
                    order.setProductId(product.getProductId()+"");
                    order.setProductNum(product.getProductNum()+"");
                    orders.add(order);
                }
                //PayActivity.launch(this,"shopCart",orders);
                //购物车计算使用新增计算方式:带联系人结算
                PayActivity.launch(this,"shopCart",orders,mAddressBean.getId()+"");
                finish();
                break;
            case R.id.ll_no_cancat:
                MyAddressActivity.launch(this,1,true);
                break;

            case R.id.tv_change_cancat_info:
                MyAddressActivity.launch(this,1,true);
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

    @Override
    public void showContent() {
        super.showContent();
        if (mPtrLayout.isRefreshing()){
            mPtrLayout.refreshComplete();
        }
        if (mAdapter.isLoading())
            mAdapter.loadMoreComplete();
    }

    @Override
    public void showLoading() {
        if (!mPtrLayout.isRefreshing() && mCurrentPage == 1){
            mAdapter.setEmptyView(ViewUtils.getLoadingView(getContext()));
        }
    }

    @Override
    public void showEmpty() {
        if (mCurrentPage == 1)
            mAdapter.setEmptyView(ViewUtils
                    .getEmptyView(getContext(),R.mipmap.no_project,"暂无数据"));
        else {
            mAdapter.loadMoreEnd(false);
        }
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
    }

    @Override
    public void showError() {
        mAdapter.setEmptyView(ViewUtils.getErrorView(getContext()));
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
        if (mCurrentPage>1){
            mAdapter.loadMoreFail();
        }
    }


    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().showBack().setTitle("购物车").show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_shop_cart);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(false);
        mPresenter.refresh(mCurrentPage);
    }

    @Override
    protected void initData() {
        mPresenter = new ShopCartPresent(this);
        mAdapter = new ShopChartAdapter();
    }

    @Override
    protected void initEvent() {
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mCurrentPage = 1;
                mPresenter.refresh(mCurrentPage);
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mCurrentPage++;
                mPresenter.refresh(mCurrentPage);
            }
        }, mRecyclerView);
        mAdapter.setCallback(this);
        mCheckBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void loadShopCart(List<ShopChartBean> shopChartList) {
        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
        if (mCurrentPage == 1){
            mAdapter.setNewData(shopChartList);
        }else {
            mAdapter.addData(shopChartList);
        }
        calculateMoney();
    }

    @Override
    public void reload() {
        mCurrentPage = 1;
        mPresenter.refresh(mCurrentPage);
    }


    @SuppressLint("SetTextI18n")
    private void calculateMoney(){
        List<ShopChartBean> products = mAdapter.getData();
        if (CollectionUtils.isEmpty(products)){
            mMoneyTv.setText("合计：￥0.00");
            return;
        }
        double sum = 0.00f;
        for (ShopChartBean product : products) {
            if (product.isChecked())
                sum+= Double.valueOf(product.getPayMoney());
        }
        mMoneyTv.setText("合计：￥" + sum);
    }

    @Override
    public void onDelete(final ShopChartBean item) {
        new  AlertDialog.Builder(this,getSupportFragmentManager()).setTitle("提示").setMessage("确认删除商品？")
                .setCancelable(true).setPositiveText("确认删除").setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.delShopCart(item.getOrderNO());
            }
        }).setNegativeText("我再想想").build().show();

    }

    @Override
    public void onEdit(final ShopChartBean item) {
        mPresenter.editShopCart(item.getId()+"",item.getProductId()+"",item.getProductNum()+"");
    }

    @Override
    public void onCheck() {
        calculateMoney();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        List<ShopChartBean> data = mAdapter.getData();
        for (ShopChartBean datum : data) {
            datum.setChecked(isChecked);
        }
        mAdapter.setNewData(data);
        calculateMoney();
    }
}
