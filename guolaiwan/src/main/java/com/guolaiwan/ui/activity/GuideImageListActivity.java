package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.BaseViewHolder;
import com.cgx.library.utils.log.LogUtil;
import com.guolaiwan.bean.GuideSpotContentAndImageBean;
import com.guolaiwan.bean.RichUpload;
import com.guolaiwan.constant.Constant;
import com.guolaiwan.constant.UrlConstant;
import com.guolaiwan.presenter.GuideImageListPresenter;
import com.guolaiwan.ui.adapter.GuideImageAdapter;
import com.guolaiwan.ui.iview.GuideSpotView;
import com.guolaiwan.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import mabeijianxi.camera.util.Log;


public class GuideImageListActivity extends BaseActivity implements GuideSpotView{

    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    private String mChildId ;
    private String mVoiceId;
    private GuideImageAdapter mAdapter;
    private GuideImageListPresenter mPresenter;


    public static void launch(Context context, String id,String voiceId){
        Intent intent = new Intent(context,GuideImageListActivity.class);
        intent.putExtra("childId",id);
        intent.putExtra("voiceId",voiceId);
        context.startActivity(intent);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setTitle("景点详情").showBack().show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_guide_image);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    protected void initData() {
        mChildId = getIntent().getStringExtra("childId");
        mVoiceId = getIntent().getStringExtra("voiceId");
        mPresenter = new GuideImageListPresenter(this);
        mAdapter = new GuideImageAdapter();
        mPresenter.getSpotImageAndContent(mVoiceId,mChildId);
    }

    @Override
    protected void initEvent() {}

    @Override
    public void setContentAndImage(List<GuideSpotContentAndImageBean> guideSpotContentAndImageBeanList) {
        if(guideSpotContentAndImageBeanList == null || guideSpotContentAndImageBeanList.size() == 0){
            mAdapter.setEmptyView(ViewUtils.getEmptyView(getContext(),R.mipmap.no_project,"暂无数据"));
        }else {
            List<GuideSpotContentAndImageBean> dataList = new ArrayList<>();
            for(int i = 0;i < guideSpotContentAndImageBeanList.size();i++){
                GuideSpotContentAndImageBean guideSpotContentAndImageBean = guideSpotContentAndImageBeanList.get(i);
                String childContent = guideSpotContentAndImageBean.getChildContent();
                String[] splitChildContent = childContent.split("&");
                for(int j = 0;j < splitChildContent.length;j++){
                    GuideSpotContentAndImageBean beanWithText = new GuideSpotContentAndImageBean();
                    beanWithText.setChildContent(splitChildContent[j]);
                    dataList.add(beanWithText);
                }
                String childPic = guideSpotContentAndImageBean.getChildPic();

                String[] splitChildPic = childPic.split(",");
                for(int n = 0;n < splitChildPic.length;n++){
                    GuideSpotContentAndImageBean beanWithImg = new GuideSpotContentAndImageBean();
                    beanWithImg.setChildPic(UrlConstant.GUIDE_SPOT_IMAGE_URL + splitChildPic[n]);
                    dataList.add(beanWithImg);
                }
            }
            mAdapter.setNewData(dataList);
        }

    }
}
