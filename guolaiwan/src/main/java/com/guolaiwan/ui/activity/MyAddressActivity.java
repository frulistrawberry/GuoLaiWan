package com.guolaiwan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.cgx.library.base.BaseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.AddressBean;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.presenter.MyAddressPresenter;
import com.guolaiwan.ui.adapter.MerchantAdapter;
import com.guolaiwan.ui.adapter.MyAddressAdapter;
import com.guolaiwan.ui.iview.MyAddressView;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ViewUtils;

import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;


public class MyAddressActivity extends BaseActivity implements MyAddressView{
    @BindView(R.id.layout_ptr)
    public PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;
    private MyAddressAdapter mAdapter;
    private boolean isForChoose = false;
    private MyAddressPresenter mPresenter;
    public static int mRequestCode = 0;

    public static void launch(Activity activity,int requestCode,boolean isForChoose){
        mRequestCode = requestCode;
        if (CommonUtils.isLogin()){
            Intent intent = new Intent(activity,MyAddressActivity.class);
            intent.putExtra("isForChoose",isForChoose);
            activity.startActivityForResult(intent,requestCode);
        }else {
            LoginActivity.launch(activity);
        }

    }

    public static void launch(Activity activity){
        if (CommonUtils.isLogin()){
            Intent intent = new Intent(activity,MyAddressActivity.class);
            activity.startActivity(intent);
        }else {
            LoginActivity.launch(activity);
        }

    }

    @Override
    public void showContent() {
        super.showContent();
        if (mPtrLayout.isRefreshing()){
            mPtrLayout.refreshComplete();
        }
    }

    @Override
    public void showEmpty() {
        mAdapter.setNewData(null);
        mAdapter.setEmptyView(ViewUtils
                .getEmptyView(getContext(),R.mipmap.no_project,"暂无数据"));
        if (mPtrLayout.isRefreshing()){
            mPtrLayout.refreshComplete();
        }
    }

    @Override
    public void showError() {
        mAdapter.setEmptyView(ViewUtils.getErrorView(getContext()));
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        if (isForChoose)
            getTitleBar().setTitle("选择收货地址");
        else
            getTitleBar().setTitle("我的收货地址");
        getTitleBar().showBack().show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_address);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAddressAdapter(this,isForChoose);
        if(isForChoose == false){
            mAdapter.setDelCallBack(new MyAddressAdapter.CallBack() {
                @Override
                public void onDelete(String addressId) {
                    mPresenter.delAddressByAddressId(addressId);
                }
            });
        }
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.refresh();
    }

    @Override
    protected void initData() {
        mPresenter = new MyAddressPresenter(this);
        isForChoose = getIntent().getBooleanExtra("isForChoose",false);
    }

    @Override
    protected void initEvent() {
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.refresh();
                mAdapter.setEnableLoadMore(true);
            }
        });

    }

    public void setResultForTargetActivity(AddressBean item){
        Intent intent = new Intent();
        intent.putExtra("addressBean",item);
        setResult(mRequestCode,intent);
        finish();
    }

    @Override
    public void loadAddressList(List<AddressBean> data) {
        mAdapter.setNewData(data);
    }

    @OnClick(R.id.tv_add_address)
    public void onClick(View v){
        AddAddressActivity.launch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mPresenter != null){
            mPresenter.refresh();
        }
    }
}
