package com.guolaiwan.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;

import com.cgx.library.base.BaseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.MerchantInfoBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.presenter.ChooseProductPresenter;
import com.guolaiwan.presenter.MerchantInfoPresenter;
import com.guolaiwan.presenter.MyCollectPresenter;
import com.guolaiwan.ui.adapter.ProductAdapter;
import com.guolaiwan.ui.iview.MerchantInfoView;
import com.guolaiwan.ui.iview.MyCollectView;
import com.guolaiwan.ui.widget.RefundPopup;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ViewUtils;

import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/20
 * 描述: 商家详情页
 */
public class ChooseProductActivity extends BaseActivity implements MyCollectView, MerchantInfoView {

    @BindView(R.id.layout_ptr)
    public PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    private ProductAdapter mAdapter;
    private MerchantInfoPresenter mPresenter;
    private int mCurrentPage = 1;
    RefundPopup mCommentPop;

    public static void launch(Activity context){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
        }else {
            Intent intent = new Intent(context,ChooseProductActivity.class);
            context.startActivityForResult(intent,1);
        }

    }


    @OnClick(R.id.tv_navigation)
    public void addProject(View view){

        mCommentPop.show(view);

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
        if (!mPtrLayout.isRefreshing()){
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
        getTitleBar().showBack().setTitle("拍卖商品").show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_product_list);
        mRecyclerView.setBackgroundColor(Color.parseColor("#f4f4f4"));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.loadProductList(1,mCurrentPage,CommonUtils.getMerchantId());
        mCommentPop=new RefundPopup(this)
                .createPopup();
        mCommentPop.setOnCancelClickListener(v -> {
            if (mCommentPop.isShowing()) {
                mCommentPop.dismiss();
            }
        });

        mCommentPop.setOnOkClickListener(v -> {
            if (mCommentPop.isShowing()) {
                String reason = mCommentPop.getContentEt().getText().toString().trim();
                if (com.cgx.library.utils.StringUtils.isEmpty(reason)){
                    CommonUtils.showMsg("请输入起拍价格");
                    return;
                }
                for (ProductBean productBean : mAdapter.getData()) {
                    if (productBean.isCheck()){

                        Intent intent = new Intent();
                        intent.putExtra("productId",productBean.getId()+"");
                        intent.putExtra("price",reason);
                        setResult(1,intent);
                        finish();
                    }
                }
                mCommentPop.dismiss();
            }
        });
        mCommentPop.setHinit("请输入起拍价格");
        mCommentPop.setOkText("发布");
        mCommentPop.getContentEt().setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    protected void initData() {
        mPresenter = new MerchantInfoPresenter(this);
        mAdapter = new ProductAdapter(this);
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setChoose(true);
    }

    @Override
    protected void initEvent() {
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.loadProductList(1,mCurrentPage,CommonUtils.getMerchantId());
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mCurrentPage++;
                mPresenter.loadProductList(1,mCurrentPage,CommonUtils.getMerchantId());
            }
        }, mRecyclerView);
    }


    @Override
    public void loadCollectList(List<ProductBean> productList) {}

    @Override
    public void loadMerchantInfo(MerchantInfoBean merchantInfo) {}

    @Override
    public void loadProductList(List<ProductBean> productList) {
        if (mCurrentPage == 1){
            mAdapter.setNewData(productList);
        }else {
            mAdapter.addData(productList);
        }
    }

    @Override
    public void setCollectImageState() {

    }
}
