package com.guolaiwan.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgx.library.base.BaseFragment;
import com.cgx.library.base.PagerAdapter;
import com.cgx.library.widget.dialog.AlertDialog;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.presenter.LiveFragmentPresenter;
import com.guolaiwan.ui.activity.LiveActivity;
import com.guolaiwan.ui.activity.LoginActivity;
import com.guolaiwan.ui.activity.ProfessionalLiveApplyActivity;
import com.guolaiwan.ui.activity.ProfessionalLiveStartActivity;
import com.guolaiwan.ui.iview.LiveFragmentView;
import com.guolaiwan.utils.CommonUtils;

import app.guolaiwan.com.guolaiwan.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/26
 * 描述: 直播
 */

public class LiveFragment extends BaseFragment implements LiveFragmentView {

    @BindView(R.id.tab_layout)
    public TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    public ViewPager mViewPager;

    private LiveFragmentPresenter mLiveFragmentPresenter;
    private PagerAdapter mAdapter;

    public static LiveFragment newInstance() {
        return new LiveFragment();
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.fragment_live,parent,false);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setTitle("直播").setRightText("开直播", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出选择框:选择普通直播还是专业直播
                new AlertDialog.Builder(getActivity(),getFragmentManager())
                        .setTitle("提示")
                        .setMessage("请选择直播方式")
                        .setPositiveText("专业直播")
                        .setNegativeText("普通直播")
                        .setOnPositiveListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //判断是否登录直播导播必须登录
                                if(!CommonUtils.isLogin()){
                                    LoginActivity.launch(getContext());
                                }else {
                                    mLiveFragmentPresenter.getProfessionalLiveOrderInfo();
                                }
                            }
                        })
                        .setNegativeListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //进入普通直播界面
                                //判断是否登录直播导播必须登录
                                if(!CommonUtils.isLogin()){
                                    LoginActivity.launch(getContext());
                                }else {
                                    LiveActivity.launch(getContext());
                                }
                            }
                        })
                        .build().show();
            }
        }).show();
    }

    @Override
    protected void initData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(LiveListFragment.newInstance("MERCHANT"));
        fragments.add(LiveListFragment.newInstance("USER"));
        fragments.add(LiveListFragment.newInstance("PROFESSIONAL_LIVE"));
        String[] titles = new String[]{"商家","个人","专业直播"};
        mAdapter = new PagerAdapter(getChildFragmentManager(),fragments,titles);
        mLiveFragmentPresenter = new LiveFragmentPresenter(this);
    }

    @Override
    protected void initView() {
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initEvent() {}

    @Override
    public void setProgfessionalLiveOrderInfo(ProfessionalLiveOrderBean data) {
        if(data.getId() == null || !data.getStatus().equals("PAYSUCCESS")){
            //进入专业直播申请界面
            ProfessionalLiveApplyActivity.launch(getContext());
        }else {
            //进入专业直播开始直播界面
            ProfessionalLiveStartActivity.launch(getContext());
        }
    }

}
