package com.guolaiwan.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.PagerAdapter;
import com.guolaiwan.ui.fragment.TextPicFragment;

import java.util.ArrayList;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;

public class UserVideoPicActivity extends BaseActivity{

    @BindView(R.id.tab_layout)
    public TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    public ViewPager mViewPager;

    private PagerAdapter mAdapter;
    String userId;
    String nickName;
    @Override
    protected void initTitle() {
        super.initTitle();

    }

    @Override
    protected void initData() {
        userId = getIntent().getStringExtra("userId");
        nickName = getIntent().getStringExtra("nickName");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(TextPicFragment.newInstance("PICTURE",userId));
        fragments.add(TextPicFragment.newInstance("LITTLEVEDIO",userId));
        String[] titles = new String[]{"图文","小视频"};
        mAdapter = new PagerAdapter(getSupportFragmentManager(),fragments,titles);
    }


    @Override
    protected void initView() {
        setContentView(R.layout.fragment_live);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        getTitleBar().setTitle(nickName).showBack().show();


    }

    @Override
    protected void initEvent() {
    }

}
