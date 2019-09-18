package com.guolaiwan.presenter;

import com.cgx.library.base.BasePresenter;
import com.guolaiwan.bean.GuideSpotContentAndImageBean;
import com.guolaiwan.model.GuideImageListModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.GuideSpotView;
import com.guolaiwan.utils.CommonUtils;
import java.util.List;
import java.util.Map;

/**
 * 创建者: 蔡朝阳
 * 日期:  2018/11/25.
 * 说明:
 */

public class GuideImageListPresenter extends BasePresenter<GuideSpotView> {

    GuideImageListModel mGuideImageListModel;

    public GuideImageListPresenter(GuideSpotView mIView) {
        super(mIView);
        mGuideImageListModel = new GuideImageListModel();
    }

    public void getSpotImageAndContent(String voice,String childId){
        mIView.showLoading();
        mGuideImageListModel.getSpotImageAndContent(voice, childId, new HttpObserver<Map<String,List<GuideSpotContentAndImageBean>>>() {
            @Override
            public void onNext(String message,Map<String,List<GuideSpotContentAndImageBean>> data) {
                List<GuideSpotContentAndImageBean> dataList = data.get("data");
                mIView.setContentAndImage(dataList);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.showMsg("获取景点详情失败" + errCode);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }



}
