package com.guolaiwan.ui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.cgx.library.base.BaseFragment;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.SPUtils;
import com.cgx.library.utils.SizeUtils;
import com.cgx.library.utils.StringUtils;
import com.cgx.library.widget.TitleBar;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import app.guolaiwan.com.guolaiwan.R;

import com.guolaiwan.bean.CompanyBean;
import com.guolaiwan.bean.RecommendBean;
import com.guolaiwan.bean.ShareBean;
import com.guolaiwan.constant.UrlConstant;
import com.guolaiwan.presenter.HomePresenter;
import com.guolaiwan.ui.activity.MainActivity;
import com.guolaiwan.ui.activity.SearchActivity;
import com.guolaiwan.ui.adapter.HomeAdapter;
import com.guolaiwan.ui.iview.HomeView;
import com.guolaiwan.ui.widget.FixScrollerPtrFrameLayout;
import com.guolaiwan.ui.widget.ListPop;
import com.guolaiwan.ui.widget.ShareDialog;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ViewUtils;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/26
 * 描述: 主页
 */

public class HomeFragment extends BaseFragment implements HomeView, View.OnClickListener {

    @BindView(R.id.layout_ptr)
    public FixScrollerPtrFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;
    public LinearLayout mQrCodeLl;
    public LinearLayout mSearchLl;
    public LinearLayout mCustomerServiceLl;


    private TitleBar mTitleBar;
    public View mHeaderView;
    public View mSearchView;
    public ConvenientBanner<RecommendBean> mBannerView;
    public ListPop mListPop;
    //分享Diaolog
    private ShareDialog mShareDialog;

    private HomeAdapter mAdapter;
    private List<MultiItemEntity> mData;
    private HomePresenter mPresenter;
    private String mComCode = "0001";
    private String mComPhoneNum = "0315-6686299";
    private String mComName = "遵化公司+";
    private List<CompanyBean> mCompanyData;
    private boolean isChoose = false;

    public static HomeFragment newInstance() {
        return new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home,parent,false);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        mTitleBar = getTitleBar().setCenterTitleWithLogo(R.mipmap.logo, "过来玩")
                .setLeftText(mComName,this)
                .setRightImggeForHomeShare(R.mipmap.share_icon, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShareBean shareBean = new ShareBean();
                        shareBean.setUrl(UrlConstant.WECHAT_WEBSITE_URL);
                        shareBean.setTitle("畅游华夏,尽在过来玩");
                        shareBean.setDescription("联系电话" + "\n" + "0315-6681288/6686299");
                        mShareDialog.setShareDate(shareBean);
                        mShareDialog.showShareDialog();
                    }
                });
        mTitleBar.show();
    }

    @Override
    protected void initData() {
        if(SPUtils.contains("comCode")){
            mComCode = SPUtils.getString("comCode");
        }

        if(SPUtils.contains("cityName")){
            mComName = SPUtils.getString("cityName") + "公司+";
        }

        if(SPUtils.contains("comPhoneNum")){
            mComPhoneNum = SPUtils.getString("comPhoneNum",mComPhoneNum);
        }

        mData = new ArrayList<>();
        mAdapter = new HomeAdapter(getActivity(),mData,HomeAdapter.TYPE_FRAGMENT);
        mAdapter.setHeaderAndEmpty(true);
        mPresenter = new HomePresenter(this);
    }

    @Override
    protected void initView() {
        //初始化分享Dialog
        mShareDialog = new ShareDialog((MainActivity)getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.item_home_banner,mRecyclerView,false);
        mSearchView = LayoutInflater.from(getContext()).inflate(R.layout.layout_home_search,mRecyclerView,false);
        mBannerView = mHeaderView.findViewById(R.id.banner_home);
        mQrCodeLl = mSearchView.findViewById(R.id.ll_qrcode);
        mSearchLl = mSearchView.findViewById(R.id.ll_search);
        mCustomerServiceLl = mSearchView.findViewById(R.id.ll_custom_service);
        mAdapter.addHeaderView(mSearchView);
        mAdapter.addHeaderView(mHeaderView);
        mHeaderView.setVisibility(View.GONE);
        mRecyclerView.setAdapter(mAdapter);
        mListPop = new ListPop(getContext()).createPopup();
        mPresenter.loadCompany();
    }

    @Override
    protected void initEvent() {
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.loadData(mComCode);
                mPresenter.loadCompany();
            }
        });
        mListPop.setOnItemClickListener(new ListPop.OnItemClickListener() {
            @Override
            public void onItemClick( int position) {
                if (CollectionUtils.isEmpty(mCompanyData)){
                    return;
                }
                SPUtils.putString("comCode",mCompanyData.get(position).getComCode());
                SPUtils.putString("cityCode",mCompanyData.get(position).getCityCode());
                SPUtils.putString("cityName",mCompanyData.get(position).getCityName());
                SPUtils.putString("comPhoneNum",mCompanyData.get(position).getCompanyPhone());
                mComCode = SPUtils.getString("comCode",mComCode);
                mComPhoneNum = SPUtils.getString("comPhoneNum",mComPhoneNum);
                mComName = SPUtils.getString("cityName") + "公司+";
                mPresenter.loadData(mComCode);
                mTitleBar.setLeftText(mComName,HomeFragment.this);
                mListPop.dismiss();
                isChoose = true;
            }
        });
        mQrCodeLl.setOnClickListener(this);
        mSearchLl.setOnClickListener(this);
        mCustomerServiceLl.setOnClickListener(this);
    }


    @Override
    public void loadData(List<MultiItemEntity> data) {
        mData.clear();
        mData.addAll(data);
        mAdapter.setNewData(mData);
    }

    @Override
    public void loadBanner(List<RecommendBean> bannerData) {
        mHeaderView.setVisibility(View.VISIBLE);
        ViewUtils.loadHomeBanner(mBannerView,bannerData);
    }

    @Override
    public void loadPop(List<CompanyBean> popData) {
        this.mCompanyData = popData;
        List<String> stringList = new ArrayList<>();
        for (CompanyBean popDatum : popData) {
            stringList.add(popDatum.getComName());
            String cityCode = SPUtils.getString("cityCode");
            if (StringUtils.isEmpty(popDatum.getCityName())||StringUtils.isEmpty(popDatum.getCityCode())||StringUtils.isEmpty(popDatum.getComCode())){
                continue;
            }
            if (popDatum.getCityCode().equals(cityCode)){
                SPUtils.putString("comCode",popDatum.getComCode());
                SPUtils.putString("cityName",popDatum.getCityName());
            }
        }
        if (!isChoose){
            mComCode = SPUtils.getString("comCode","0001");
        }
        mListPop.setData(stringList);
        mPresenter.loadData(mComCode);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_qrcode:
                break;
            case R.id.ll_search:
                SearchActivity.launch(getContext());
                break;
            case R.id.ll_custom_service:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + mComPhoneNum);
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.tv_title_left:
                if (CollectionUtils.isEmpty(mCompanyData)) {
                    CommonUtils.error(-1, "公司信息获取失败");
                    return;
                }
                mListPop.showAtAnchorView(view, VerticalGravity.BELOW, HorizontalGravity.CENTER, 0, SizeUtils.dp2px(getContext(), -15));
                break;
        }
    }
}
