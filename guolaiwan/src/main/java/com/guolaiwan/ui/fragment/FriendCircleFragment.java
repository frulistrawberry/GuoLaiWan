package com.guolaiwan.ui.fragment;

import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgx.library.base.BaseFragment;
import com.cgx.library.base.PagerAdapter;
import com.cgx.library.utils.SDCardUtils;
import com.cgx.library.utils.SPUtils;
import com.guolaiwan.ui.activity.EditUserInfoActivity;
import com.guolaiwan.ui.activity.LoginActivity;
import com.guolaiwan.ui.activity.PicSearchActivity;
import com.guolaiwan.ui.activity.PublishVideoActivity;
import com.guolaiwan.ui.activity.RichPublicActivity;
import com.guolaiwan.ui.activity.UpLoadActivity;
import com.guolaiwan.ui.adapter.FriendCircleAdapter;
import com.guolaiwan.ui.widget.DialogOnItemClickListener;
import com.guolaiwan.ui.widget.NormalSelectDialog;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import mabeijianxi.camera.MediaRecorderActivity;
import mabeijianxi.camera.model.AutoVBRMode;
import mabeijianxi.camera.model.MediaRecorderConfig;


/**
 * Created by Administrator on 2018/5/22/022.
 */

public class FriendCircleFragment extends BaseFragment implements DialogOnItemClickListener {



    private NormalSelectDialog selectDialog;
    private NormalSelectDialog selectDialog1;

    @BindView(R.id.tab_layout)
    public TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    public ViewPager mViewPager;

    private PagerAdapter mAdapter;


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.fragment_live,parent,false);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setTitle("我发布").setRightText("发布", v -> {
            if (CommonUtils.isLogin()){
                String phone = SPUtils.getString("phone","");
                String nickName = SPUtils.getString("nickName","");
                String realName = SPUtils.getString("realName","");
                String company = SPUtils.getString("company","");
                boolean isCanPublish = !TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(nickName)&&!TextUtils.isEmpty(realName)&&!TextUtils.isEmpty(company);
                if (isCanPublish)
                    selectDialog.show();
                else
                    EditUserInfoActivity.launch(getContext());
            }
            else
                LoginActivity.launch(getContext());
        }).setLeftText("搜索", v -> PicSearchActivity.launch(getContext())).show();
    }

        @Override
    protected void initData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(TextPicFragment.newInstance("PICTURE"));
        fragments.add(TextPicFragment.newInstance("LITTLEVEDIO"));
        String[] titles = new String[]{"图文","小视频"};
        mAdapter = new PagerAdapter(getChildFragmentManager(),fragments,titles);
    }


    @Override
    protected void initView() {
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        selectDialog = new NormalSelectDialog.Builder(getContext()).setOnItemClickListener(this).build();
        selectDialog1 = new NormalSelectDialog.Builder(getContext()).setOnItemClickListener(new DialogOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0:
                        MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
                                .setMediaBitrateConfig(new AutoVBRMode())
                                .smallVideoWidth(480)
                                .smallVideoHeight(360)
                                .recordTimeMax(180 * 1000)
                                .maxFrameRate(20)
                                .captureThumbnailsTime(1)
                                .recordTimeMin((int) (1.5 * 1000))
                                .build();
                        MediaRecorderActivity.goSmallVideoRecorder(getActivity(), PublishVideoActivity.class.getName(), config);
                        break;
                    case 1:
                        startActivity(new Intent(getContext(), UpLoadActivity.class));
                        break;
                }
            }
        }).build();
        selectDialog1.setDataList(Arrays.asList("录制","上传"));
        selectDialog.setDataList(Arrays.asList("图文","小视频"));

    }

    @Override
    protected void initEvent() {
    }


    @Override
    public void onItemClick(View view, int position) {
        switch (position){
            case 0:
                startActivity(new Intent(getContext(),RichPublicActivity.class));
                selectDialog.dismiss();
                break;
            case 1:
                selectDialog.dismiss();
                selectDialog1.show();
                break;
        }
    }



}
