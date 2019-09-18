package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.KeyboardUtils;
import com.cgx.library.widget.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.guolaiwan.bean.CompanyBean;
import com.guolaiwan.bean.RecommendBean;
import com.guolaiwan.presenter.SearchPresenter;
import com.guolaiwan.ui.adapter.HomeAdapter;
import com.guolaiwan.ui.iview.HomeView;
import com.guolaiwan.ui.widget.ListPop;
import com.guolaiwan.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements HomeView {

    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.btn_mer)
    TextView merBtn;
    @BindView(R.id.btn_pro)
    TextView proBtn;
    private HomeAdapter mAdapter;
    private int mCurrentPage = 1;
    private List<MultiItemEntity> mData;
    private SearchPresenter mPresenter;
    private String type = "MERCHANT";
    String keywords;

    public static void launch(Context context){
        context.startActivity(new Intent(context,SearchActivity.class));
    }

    @OnClick({R.id.btnSearchRight,R.id.btn_mer,R.id.btn_pro,R.id.btnSearch})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnSearchRight:
                finish();
                break;
            case R.id.btn_mer:
               type = "MERCHANT";
               merBtn.setTextColor(Color.parseColor("#ca4000"));
               proBtn.setTextColor(getResourceColor(R.color.gray_light));
                break;
            case R.id.btn_pro:
                type = "PRODUCT";
                proBtn.setTextColor(Color.parseColor("#ca4000"));
                merBtn.setTextColor(getResourceColor(R.color.gray_light));
                break;
            case R.id.btnSearch:
                mCurrentPage = 1;
                KeyboardUtils.hideSoftInput(SearchActivity.this);
                keywords = etSearch.getText().toString().trim();
                if (TextUtils.isEmpty(keywords)){
                    return;
                }
                mPresenter.search(keywords,type,mCurrentPage);
                break;
        }
    }



    @Override
    public void showContent() {
        super.showContent();
        if (mAdapter.isLoading())
            mAdapter.loadMoreComplete();
    }

    @Override
    public void showLoading() {
        if ( mCurrentPage == 1){
            mAdapter.setEmptyView(ViewUtils.getLoadingView(getContext()));
        }
    }

    @Override
    public void showEmpty() {
        if (mCurrentPage == 1)
            mAdapter.setEmptyView(ViewUtils
                    .getEmptyView(getContext(), R.mipmap.no_project,"暂无数据"));
        else {
            mAdapter.loadMoreEnd(false);
        }
    }

    @Override
    public void showError() {
        mAdapter.setEmptyView(ViewUtils.getErrorView(getContext()));
        if (mCurrentPage>1){
            mAdapter.loadMoreFail();
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setHeaderAndEmpty(true);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(true);
    }

    @Override
    protected void initData() {
        mData = new ArrayList<>();
        mAdapter = new HomeAdapter(this,mData,HomeAdapter.TYPE_ACTIVITY);
        mPresenter = new SearchPresenter(this);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mCurrentPage++;
                mPresenter.search(keywords,type,mCurrentPage);

            }
        }, mRecyclerView);

    }

    @Override
    public void loadData(List<MultiItemEntity> data) {
        if (mCurrentPage == 1){
            mAdapter.setNewData(data);
        }else {
            mAdapter.addData(data);
        }
    }

    @Override
    public void loadBanner(List<RecommendBean> bannerData) {

    }

    @Override
    public void loadPop(List<CompanyBean> popData) {

    }
}
