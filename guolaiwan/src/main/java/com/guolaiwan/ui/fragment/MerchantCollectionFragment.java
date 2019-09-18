package com.guolaiwan.ui.fragment;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgx.library.base.BaseFragment;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.presenter.MerchantCollectionPresenter;
import com.guolaiwan.presenter.ProductCollectionPresenter;
import com.guolaiwan.ui.adapter.MerchantAdapter;
import com.guolaiwan.ui.adapter.ProductAdapter;
import com.guolaiwan.ui.iview.MerchantCollectionView;
import com.guolaiwan.ui.iview.ProductCollectionView;
import com.guolaiwan.utils.ViewUtils;

import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 创建者: 蔡朝阳
 * 日期:  2018/12/3.
 * 说明:
 */

public class MerchantCollectionFragment extends BaseFragment implements MerchantCollectionView {

    @BindView(R.id.layout_ptr)
    public PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    private MerchantAdapter mAdapter;
    private MerchantCollectionPresenter mPresenter;

    public static MerchantCollectionFragment newInstance() {
        MerchantCollectionFragment fragment = new MerchantCollectionFragment();
        return fragment;
    }


    @Override
    public void showContent() {
        super.showContent();
        if (mPtrLayout.isRefreshing()){
            mPtrLayout.refreshComplete();
        }
    }

    @Override
    public void showLoading() {
        if (!mPtrLayout.isRefreshing()){
            mAdapter.setEmptyView(ViewUtils.getLoadingView(getContext()));
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
    protected View createView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.include_ptr_recycler,parent,false);
    }

    @Override
    protected void initData() {
        mPresenter = new MerchantCollectionPresenter(this);
        mAdapter = new MerchantAdapter(getContext());
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.setCallBack(new MerchantAdapter.CallBack() {
            @Override
            public void onDelete(MerchantBean item) {
                mPresenter.delete(item.getId()+"");
            }
        });
    }

    @Override
    protected void initView() {
        mRecyclerView.setBackgroundColor(Color.parseColor("#f4f4f4"));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.refresh();
    }

    @Override
    protected void initEvent() {
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.refresh();
            }
        });
    }

    @Override
    public void loadCollectList(List<MerchantBean> data) {
        mAdapter.setNewData(data);
    }
}
