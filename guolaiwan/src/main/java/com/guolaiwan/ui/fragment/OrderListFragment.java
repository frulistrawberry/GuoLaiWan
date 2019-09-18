package com.guolaiwan.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cgx.library.base.BaseFragment;
import com.cgx.library.utils.log.LogUtil;
import com.guolaiwan.bean.OrderBean;
import com.guolaiwan.presenter.OrderListPresenter;
import com.guolaiwan.ui.adapter.OrderListAdapter;
import com.guolaiwan.ui.iview.OrderListView;
import com.guolaiwan.utils.ViewUtils;
import java.util.List;
import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.vov.vitamio.utils.Log;

public class OrderListFragment extends BaseFragment implements OrderListView {

    @BindView(R.id.layout_ptr)
    public PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    private OrderListAdapter mAdapter;
    private OrderListPresenter mPresenter;
    private String mType;
    private String uType;

    public static OrderListFragment newInstance(String type,String uType) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        bundle.putString("uType",uType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void showContent() {
        super.showContent();
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
    }

    @Override
    public void showLoading() {
        if (!mPtrLayout.isRefreshing()){
            mAdapter.setEmptyView(ViewUtils.getLoadingView(getContext()));
        }
    }

    @Override
    public void showEmpty() {
        mAdapter.setEmptyView(ViewUtils
                .getEmptyView(getContext(),R.mipmap.no_project,"暂无数据"));
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
    }

    @Override
    public void showError() {
        mAdapter.setEmptyView(ViewUtils.getErrorView(getContext()));
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.include_ptr_recycler,parent,false);
    }

    @Override
    protected void initData() {
        mType = getArguments().getString("type","");
        uType = getArguments().getString("uType","");
        mAdapter = new OrderListAdapter();
        mAdapter.setuType(uType);
        mPresenter = new OrderListPresenter(this);
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.refresh(mType,uType);
    }

    @Override
    protected void initEvent() {
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.refresh(mType,uType);
            }
        });
    }

    @Override
    public void loadOrderList(List<OrderBean> data) {
        mAdapter.setNewData(data);
    }

    @Override
    public void onResume() {
        super.onResume();
        //mAdapter.setNewData(null)为了用户点击退款返回列表界面时
        //当没有数据时无法刷新列表，因此选用这种方式强制刷新视图
        mAdapter.setNewData(null);
        mPresenter.refresh(mType,uType);
    }
}
