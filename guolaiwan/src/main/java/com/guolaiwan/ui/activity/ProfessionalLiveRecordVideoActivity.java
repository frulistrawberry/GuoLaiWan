package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.LiveListBean;
import com.guolaiwan.bean.RecordVideoBean;
import com.guolaiwan.presenter.LiveListPresenter;
import com.guolaiwan.presenter.ProfessionalLiveRecordVideoPresenter;
import com.guolaiwan.ui.adapter.LiveListAdapter;
import com.guolaiwan.ui.adapter.RecordVideoListAdapter;
import com.guolaiwan.ui.iview.ProfessionalLiveRecordVideoView;
import com.guolaiwan.ui.widget.RecycleViewDivider;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ViewUtils;

import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class ProfessionalLiveRecordVideoActivity extends BaseActivity implements ProfessionalLiveRecordVideoView {


    @BindView(R.id.layout_ptr)
    public PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    //当前页数
    private int mCurrentPage = 1;
    private RecordVideoListAdapter mAdapter;
    private ProfessionalLiveRecordVideoPresenter mPresenter;

    public static void launch(Context context){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
        }else {
            Intent intent = new Intent(context,ProfessionalLiveRecordVideoActivity.class);
            context.startActivity(intent);
        }
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
        if (mCurrentPage > 1){
            mAdapter.loadMoreFail();
        }
    }


    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().showBack().setTitle("我的视频").show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_professional_live_record_video);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.professionalLiveGetRecordVideoList(mCurrentPage);
    }

    @Override
    protected void initData() {
        mAdapter = new RecordVideoListAdapter(getContext());
        mAdapter.setEnableLoadMore(true);
        mPresenter = new ProfessionalLiveRecordVideoPresenter(this);
        mAdapter.setPresenter(mPresenter);
    }

    @Override
    protected void initEvent() {
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mCurrentPage = 1;
                mPresenter.professionalLiveGetRecordVideoList(mCurrentPage);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mCurrentPage++;
                mPresenter.professionalLiveGetRecordVideoList(mCurrentPage);
            }
        }, mRecyclerView);
    }

    private List<RecordVideoBean> mRecordVideoBeanList;
    @Override
    public void loadRecordVideoList(List<RecordVideoBean> recordVideoBeanList) {
        this.mRecordVideoBeanList = recordVideoBeanList;
        if (mCurrentPage == 1){
            mAdapter.setNewData(recordVideoBeanList);
        }else {
            mAdapter.addData(recordVideoBeanList);
        }
    }

    @Override
    public void setDeleteVideoResult(int itemLayoutPosition) {
        mRecordVideoBeanList.remove(itemLayoutPosition);
        mAdapter.notifyItemRemoved(itemLayoutPosition);
        if (itemLayoutPosition != mRecordVideoBeanList.size()) {
            mAdapter.notifyItemRangeChanged(itemLayoutPosition, mRecordVideoBeanList.size() - itemLayoutPosition);
        }
        ToastUtils.showToast(getContext(),"温馨提示:删除成功");
        if(mRecordVideoBeanList.size() == 0){
            mCurrentPage = 1;
            mPresenter.professionalLiveGetRecordVideoList(mCurrentPage);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAdapter != null){
            mAdapter.releaseAllDownLoadTask();
        }
    }
}
