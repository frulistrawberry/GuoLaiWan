package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.PagerAdapter;
import com.cgx.library.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.MerchantInfoBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.presenter.MerchantInfoPresenter;
import com.guolaiwan.presenter.MyCollectPresenter;
import com.guolaiwan.ui.adapter.MerchantProductAdapter;
import com.guolaiwan.ui.adapter.ProductAdapter;
import com.guolaiwan.ui.fragment.LiveListFragment;
import com.guolaiwan.ui.fragment.MerchantCollectionFragment;
import com.guolaiwan.ui.fragment.ProductCollectionFragment;
import com.guolaiwan.ui.iview.MerchantInfoView;
import com.guolaiwan.ui.iview.MyCollectView;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ViewUtils;

import java.util.ArrayList;
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
public class MyCollectActivity extends BaseActivity{

    @BindView(R.id.tab_layout)
    public TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    public ViewPager mViewPager;
    private PagerAdapter mAdapter;


    public static void launch(Context context){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
        }else {
            Intent intent = new Intent(context,MyCollectActivity.class);
            context.startActivity(intent);
        }

    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().showBack().setTitle("我的收藏").show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.layout_activity_my_collection);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ProductCollectionFragment.newInstance());
        fragments.add(MerchantCollectionFragment.newInstance());
        String[] titles = new String[]{"商品","商家"};
        mAdapter = new PagerAdapter(getSupportFragmentManager(),fragments,titles);
    }

    @Override
    protected void initEvent() {}

}
