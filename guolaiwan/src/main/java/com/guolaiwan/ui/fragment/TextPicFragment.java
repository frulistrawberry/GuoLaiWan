package com.guolaiwan.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgx.library.base.BaseFragment;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.BarUtils;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.widget.dialog.AlertDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.FriendBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.ui.adapter.FriendCircleAdapter;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.Observable;

public class TextPicFragment extends BaseFragment{

    @BindView(R.id.layout_ptr)
    public PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    public int mCurrentPage=1;
    public FriendCircleAdapter mAdapter;
    private boolean isMy = false;

    String vType;
    String userId;
    public static TextPicFragment newInstance(String type){
        TextPicFragment fragment = new TextPicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static TextPicFragment newInstance(String type,boolean isMy){
        TextPicFragment fragment = new TextPicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        bundle.putBoolean("isMy",isMy);
        bundle.putString("userId",CommonUtils.getUserId());
        fragment.setArguments(bundle);
        return fragment;
    }

    public static TextPicFragment newInstance(String type,String userId){
        TextPicFragment fragment = new TextPicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        bundle.putString("userId",userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void showContent() {
        super.showContent();
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
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
        if (mCurrentPage == 1){
            mAdapter.setNewData(null);
            mAdapter.setEmptyView(ViewUtils
                    .getEmptyView(getContext(),R.mipmap.no_project,"暂无数据"));
        }

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
    protected View createView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.include_ptr_recycler,parent,false);
    }



    @Override
    protected void initData() {
        mAdapter = new FriendCircleAdapter();
        mAdapter.setEnableLoadMore(true);
        EventBus.getDefault().register(this);
        vType = getArguments().getString("type","");
        isMy = getArguments().getBoolean("isMy",false);
        userId = getArguments().getString("userId","");
    }

    @Subscribe
    public void onEventMainThread(String str){
        if (str.equals("refresh_friend")){
            mCurrentPage = 1;
            request();
        }
    }
    @Override
    protected void initView() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        request();
    }

    @Override
    protected void initEvent() {
            mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    mCurrentPage = 1;
                    request();
                }
            });

            mAdapter.setOnLoadMoreListener(() -> {
                mCurrentPage++;
                request();
            });
            mAdapter.setMy(isMy);
            mAdapter.setCallBack(new FriendCircleAdapter.CallBack() {
                @Override
                public void onDelete(FriendBean item) {
                    new  AlertDialog.Builder(getActivity(),getChildFragmentManager()).setTitle("提示").setMessage("确认删除商品？")
                            .setCancelable(true).setPositiveText("确认删除").setOnPositiveListener(v -> {
                                RetrofitUtil.composeToSubscribe(HttpClient.getApiService().delPic(item.getId()+"",CommonUtils.getUserId()), new HttpObserver() {
                                    @Override
                                    public void onComplete() {

                                    }

                                    @Override
                                    public void onNext(String message, Object data) {
                                        CommonUtils.showMsg(message);
                                        mCurrentPage = 1;
                                        request();
                                    }

                                    @Override
                                    public void onError(int errCode, String errMessage) {
                                        CommonUtils.showMsg(errMessage);
                                    }
                                },getLifeSubject());
                            }).setNegativeText("我再想想").build().show();
                }
            });
    }

    private void request(){
                    showLoading();
                    Observable<HttpResult<List<FriendBean>>> observable = HttpClient.getApiService().getVideoPic(mCurrentPage,10, userId,vType,"");
                    RetrofitUtil.composeToSubscribe(observable, new HttpObserver<List<FriendBean>>() {
                        @Override
                        public void onNext(String message, List<FriendBean> data) {
                            if (CollectionUtils.isEmpty(data)){
                                showEmpty();
                                return;
                            }
                            if (mCurrentPage == 1){
                                mAdapter.setNewData(data);
                            }else
                                mAdapter.addData(data);
                showContent();
            }

            @Override
            public void onError(int errCode, String errMessage) {
                showError();
            }

            @Override
            public void onComplete() {

            }
        },getLifeSubject());
    }
}
