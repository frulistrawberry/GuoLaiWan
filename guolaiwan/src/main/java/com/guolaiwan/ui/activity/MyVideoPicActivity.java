package com.guolaiwan.ui.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.PagerAdapter;
import com.guolaiwan.ui.fragment.TextPicFragment;
import com.guolaiwan.ui.widget.DialogOnItemClickListener;
import com.guolaiwan.ui.widget.NormalSelectDialog;
import com.guolaiwan.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;

public class MyVideoPicActivity extends BaseActivity{

    @BindView(R.id.tab_layout)
    public TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    public ViewPager mViewPager;

    private PagerAdapter mAdapter;
    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setTitle("我的发布").showBack().show();
    }

    @Override
    protected void initData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(TextPicFragment.newInstance("PICTURE",true));
        fragments.add(TextPicFragment.newInstance("LITTLEVEDIO",true));
        String[] titles = new String[]{"图文","小视频"};
        mAdapter = new PagerAdapter(getSupportFragmentManager(),fragments,titles);
    }


    @Override
    protected void initView() {
        setContentView(R.layout.fragment_live);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    protected void initEvent() {
    }

}
