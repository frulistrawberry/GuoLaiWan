package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.PagerAdapter;
import com.guolaiwan.constant.Constant;
import com.guolaiwan.ui.fragment.OrderListFragment;
import com.guolaiwan.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;

public class OrderListActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    public TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    public ViewPager mViewPager;

    private PagerAdapter mAdapter;
    private String uType;

    public static void launch(Context context,String uType){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
            return;
        }
        Intent intent = new Intent(context,OrderListActivity.class);
        intent.putExtra("uType",uType);
        context.startActivity(intent);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setTitle("订单").showBack().show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_order_list);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {
        uType = getIntent().getStringExtra("uType");
        List<Fragment> fragments = new ArrayList<>();
        String[] titles = new String[]{"已支付","已发货","待退款","已退款","已收货","已验单"};
        fragments.add(OrderListFragment.newInstance(Constant.ORDER_HAVE_PAY,uType));
        fragments.add(OrderListFragment.newInstance(Constant.ORDER_HAVE_SEND,uType));
        fragments.add(OrderListFragment.newInstance(Constant.ORDER_NOT_RETURN_MONEY,uType));
        fragments.add(OrderListFragment.newInstance(Constant.ORDER_HAVE_RETURN_MONEY,uType));
        fragments.add(OrderListFragment.newInstance(Constant.ORDER_HAVE_SIGN,uType));
        fragments.add(OrderListFragment.newInstance(Constant.ORDER_HAVE_CHECK,uType));
        mAdapter = new PagerAdapter(getSupportFragmentManager(),fragments,titles);
    }

    @Override
    protected void initEvent() {}
}
