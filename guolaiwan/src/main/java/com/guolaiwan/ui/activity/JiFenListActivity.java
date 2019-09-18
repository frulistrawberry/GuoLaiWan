package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.autonavi.ae.dice.InitConfig;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.widget.dialog.AlertDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.AddressBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.presenter.MyCollectPresenter;
import com.guolaiwan.ui.adapter.ProductAdapter;
import com.guolaiwan.ui.iview.MyCollectView;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ViewUtils;

import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/20
 * 描述: 商家详情页
 */
public class JiFenListActivity extends BaseActivity implements MyCollectView{

    @BindView(R.id.layout_ptr)
    public PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    private ProductAdapter mAdapter;
    private MyCollectPresenter mPresenter;
    int mCurrentPage = 1;

    public static void launch(Context context){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
        }else {
            Intent intent = new Intent(context,JiFenListActivity.class);
            context.startActivity(intent);
        }

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
        getTitleBar().showBack().setTitle("积分商城").show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.include_ptr_recycler);
        mRecyclerView.setBackgroundColor(Color.parseColor("#f4f4f4"));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.getGlodList(mCurrentPage);
    }

    String productId = "";

    @Override
    protected void initData() {
        mPresenter = new MyCollectPresenter(this);
        mAdapter = new ProductAdapter(this);
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.setJiFen(true);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setCallBack(new ProductAdapter.CallBack() {
            @Override
            public void onDelete(ProductBean item) {
                new AlertDialog.Builder(JiFenListActivity.this,getSupportFragmentManager())
                        .setCancelable(true)
                        .setTitle("提示")
                        .setMessage("花费"+item.getGoldNum()+"积分"+"兑换"+item.getProductName()+"?")
                        .setNegativeText("取消")
                        .setPositiveText("兑换")
                        .setOnPositiveListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                productId = item.getId()+"";
                                MyAddressActivity.launch(JiFenListActivity.this,1,true);
                            }
                        }).build().show();
            }
        });
    }

    @Override
    protected void initEvent() {
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mCurrentPage = 1;
                mPresenter.getGlodList(mCurrentPage);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mCurrentPage++;
                mPresenter.getGlodList(mCurrentPage);
            }
        }, mRecyclerView);
    }


    @Override
    public void loadCollectList(List<ProductBean> productList) {
        if (mCurrentPage == 1)
        mAdapter.setNewData(productList);
        else
            mAdapter.addData(productList);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            AddressBean addressBean = (AddressBean) data.getSerializableExtra("addressBean");
            mPresenter.duihuan(productId,addressBean.getId()+"");
        }
    }

}
