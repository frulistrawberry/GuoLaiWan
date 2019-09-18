package com.guolaiwan.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgx.library.base.BaseFragment;
import app.guolaiwan.com.guolaiwan.R;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.LiveListBean;
import com.guolaiwan.presenter.LiveListPresenter;
import com.guolaiwan.ui.activity.ProductInfoActivity;
import com.guolaiwan.ui.adapter.LiveListAdapter;
import com.guolaiwan.ui.iview.CategoryView;
import com.guolaiwan.ui.iview.LiveListView;
import com.guolaiwan.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;


public class LiveListFragment extends BaseFragment implements LiveListView {


    @BindView(R.id.layout_ptr)
    public PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    private LiveListAdapter mAdapter;
    private LiveListPresenter mPresenter;
    private String mType;
    private int mCurrentPage = 1;

    public static LiveListFragment newInstance(String type) {
        LiveListFragment fragment = new LiveListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void showContent() {
        super.showContent();
        if (mPtrLayout.isRefreshing()){
            mPtrLayout.refreshComplete();
        }
        if (mAdapter.isLoading()){
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void showLoading() {
        if (!mPtrLayout.isRefreshing() && mCurrentPage == 1){
            mAdapter.setEmptyView(ViewUtils.getLoadingView(getContext()));
        }
    }

    @Override
    public void showEmpty() {
        if (mCurrentPage == 1){
            mAdapter.setNewData(null);
            mAdapter.setEmptyView(ViewUtils
                    .getEmptyView(getContext(),R.mipmap.no_project,"暂无数据"));
        } else {
            mCurrentPage = 1;
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
    protected View createView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.include_ptr_recycler,parent,false);
    }

    @Override
    protected void initData() {
        mType = getArguments().getString("type","");
        mAdapter = new LiveListAdapter();
        mAdapter.setEnableLoadMore(true);
        mPresenter = new LiveListPresenter(this);
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mRecyclerView.setAdapter(mAdapter);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initEvent() {
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mCurrentPage = 1;
                mPresenter.refresh(mType,mCurrentPage);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mCurrentPage++;
                mPresenter.refresh(mType,mCurrentPage);
            }
        }, mRecyclerView);
    }


    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
        mPresenter.refresh(mType,mCurrentPage);
    }

    @Override
    public void loadLiveList(List<LiveListBean> liveList) {
        if (mCurrentPage == 1){
            mAdapter.setNewData(liveList);
        }else {
            mAdapter.addData(liveList);
        }
    }

    @Subscribe
    public void onEventMainThread(String str){
        if (str.equals("refresh_live")){
            mCurrentPage = 1;
            mPresenter.refresh(mType,mCurrentPage);
        }
    }

}
