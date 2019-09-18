package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.StringUtils;
import com.cgx.library.utils.log.LogUtil;
import com.cgx.library.widget.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import app.guolaiwan.com.guolaiwan.R;
import com.guolaiwan.bean.DistributorBean;
import com.guolaiwan.bean.FilerBean;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.RecommendBean;
import com.guolaiwan.constant.ItemType;
import com.guolaiwan.presenter.CategoryPresenter;
import com.guolaiwan.ui.adapter.CategoryAdapter;
import com.guolaiwan.ui.adapter.HomeAdapter;
import com.guolaiwan.ui.iview.CategoryView;
import com.guolaiwan.ui.widget.SpannerGroup;
import com.guolaiwan.ui.widget.SpannerPop;
import com.guolaiwan.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/11
 * 描述: 分类页
 */

public class CategoryListActivity extends BaseActivity implements CategoryView {
    @BindView(R.id.layout_ptr)
    public PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;
    @BindView(R.id.spanner)
    public SpannerGroup mSpanner;
    public View mHeaderBannerView;
    public View mHeaderSpannerView;
    public SpannerGroup mHeaderSpanner;
    public ConvenientBanner<ProductBean> mProductBannerView;
    public ConvenientBanner<MerchantBean> mMerchantBannerView;

    private HomeAdapter mAdapter;
    private int mCurrentPage = 1;
    private int mType;
    private String mId;
    private List<Map<String,String>> mRetrievals;
    private String mTitleStr;
    private CategoryPresenter mPresenter;
    private List<MultiItemEntity> mData;

    public static void launch(Context context,int type,String id,String title){
        Intent intent = new Intent(context, CategoryListActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("type",type);
        intent.putExtra("title",title);
        context.startActivity(intent);
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
        if (!StringUtils.isEmpty(mTitleStr)){
            getTitleBar().setTitle(mTitleStr);
        }
        getTitleBar().showBack().setRightImage(R.mipmap.icon_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.launch(getContext());
            }
        });
        getTitleBar().show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_category);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mHeaderBannerView = LayoutInflater.from(this).inflate(R.layout.item_home_banner,mRecyclerView,false);
        mHeaderSpannerView = LayoutInflater.from(this).inflate(R.layout.layout_spanner_header,mRecyclerView,false);
        mHeaderSpanner = mHeaderSpannerView.findViewById(R.id.spanner);
        mHeaderBannerView = LayoutInflater.from(this).inflate(R.layout.item_home_banner,mRecyclerView,false);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.addHeaderView(mHeaderBannerView);
        mAdapter.addHeaderView(mHeaderSpannerView);
        mHeaderBannerView.setVisibility(View.GONE);
        mHeaderSpannerView.setVisibility(View.GONE);
        if (mType == CategoryView.TYPE_MODU){
            mMerchantBannerView = mHeaderBannerView.findViewById(R.id.banner_home);
        }else {
            mProductBannerView = mHeaderBannerView.findViewById(R.id.banner_home);
        }
        mAdapter.setHeaderAndEmpty(true);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(true);
        mPresenter.refresh(mType,mCurrentPage,mId,mId,mRetrievals);


    }

    @Override
    protected void initData() {
        mType = getIntent().getIntExtra("type",1);
        mId = getIntent().getStringExtra("id");
        mTitleStr = getIntent().getStringExtra("title");
        mPresenter = new CategoryPresenter(this);
        mData = new ArrayList<>();
        mAdapter = new HomeAdapter(this,mData,HomeAdapter.TYPE_ACTIVITY);

    }

    @Override
    protected void initEvent() {
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.refresh(mType,mCurrentPage,mId,mId,mRetrievals);
                mAdapter.setEnableLoadMore(true);
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mCurrentPage++;
               if (mType == CategoryView.TYPE_MODU){
                   mPresenter.loadModuList(mCurrentPage,mId,mRetrievals);
               }else if (mType == CategoryView.TYPE_ACT){
                   mPresenter.loadProductList(mCurrentPage,mId);
               }
            }
        }, mRecyclerView);

        mSpanner.setOnItemClickListener(new SpannerPop.OnItemClickListener() {
            @Override
            public void onItemClick(List<Map<String,String>> retrievals,String currentClassName,int index) {
                mRetrievals = retrievals;
                mHeaderSpanner.setCategoryNames(currentClassName,index);
                mCurrentPage = 1;
                mAdapter.setNewData(null);
                mPresenter.refresh(mType,mCurrentPage,mId,mId,mRetrievals);
            }
        });
        mHeaderSpanner.setOnItemClickListener(new SpannerPop.OnItemClickListener() {
            @Override
            public void onItemClick(List<Map<String,String>> retrievals,String currentClassName,int index) {
                mRetrievals = retrievals;
                mSpanner.setCategoryNames(currentClassName,index);
                mCurrentPage = 1;
                mAdapter.setNewData(null);
                mPresenter.refresh(mType,mCurrentPage,mId,mId,mRetrievals);
            }
        });
        if (mType == CategoryView.TYPE_MODU){
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int scrollY = ViewUtils.getRecyclerViewScrollY(recyclerView);
                    if (mHeaderBannerView.getBottom()-scrollY<0){
                        mSpanner.setVisibility(View.VISIBLE);
                    }else {
                        mSpanner.setVisibility(View.GONE);
                    }
                    LogUtil.d(TAG,"mHeaderSpannerView.getTop()="+mHeaderSpannerView.getTop());
                    LogUtil.d(TAG,"scrollY="+scrollY);
                    LogUtil.d(TAG,"mHeaderSpannerView.getTop()-scrollY="+(mHeaderSpannerView.getTop()-scrollY));
                }
            });
        }
    }

    @Override
    public void loadModuBanner(List<MerchantBean> bannerData) {
        mHeaderBannerView.setVisibility(View.VISIBLE);
        ViewUtils.loadMerchantBanner(mMerchantBannerView,bannerData);
    }

    @Override
    public void loadModuRetrieval(List<FilerBean> retrievalData) {
        if (CollectionUtils.isEmpty(retrievalData)){
            mHeaderSpannerView.setVisibility(View.GONE);
            mSpanner.setVisibility(View.GONE);
        }else {
            mHeaderSpannerView.setVisibility(View.VISIBLE);
            mSpanner.setData(retrievalData);
            mHeaderSpanner.setData(retrievalData);

        }
    }

    @Override
    public void loadMerchantList(List<MultiItemEntity> merchantListData) {
        if (mCurrentPage == 1){
            mAdapter.setNewData(merchantListData);
        }else {
            mAdapter.addData(merchantListData);
        }

    }

    @Override
    public void loadActBanner(List<ProductBean> bannerData) {
        mHeaderBannerView.setVisibility(View.VISIBLE);
        ViewUtils.loadProductBanner(mProductBannerView,bannerData);
    }

    @Override
    public void loadActProductList(List<MultiItemEntity> productListData) {
        if (mCurrentPage == 1){
            mAdapter.setNewData(productListData);
        }else {
            mAdapter.addData(productListData);
        }
    }


}
