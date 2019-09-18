package com.guolaiwan.presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cgx.library.base.BasePresenter;;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.ToastUtils;
import com.guolaiwan.bean.RecordVideoBean;
import com.guolaiwan.model.ProfessionalLivePlayerModel;
import com.guolaiwan.model.ProfessionalLiveRecordVideoModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.activity.ProfessionalLiveRecordVideoActivity;
import com.guolaiwan.ui.adapter.RecordVideoListAdapter;
import com.guolaiwan.ui.iview.ProfessionalLiveRecordVideoView;
import com.guolaiwan.utils.CommonUtils;

import java.util.List;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/3/7.
 * 说明:
 */
public class ProfessionalLiveRecordVideoPresenter extends BasePresenter<ProfessionalLiveRecordVideoView> {

    private ProfessionalLiveRecordVideoModel mModel;

    public ProfessionalLiveRecordVideoPresenter(ProfessionalLiveRecordVideoView mIView) {
        super(mIView);
        mModel = new ProfessionalLiveRecordVideoModel();
    }

    /*专业直播:获取录制视频列表*/
    public void professionalLiveGetRecordVideoList(int currentPage){
        mIView.showLoading();
        mModel.professionalLiveGetRecordVideoList(currentPage, new HttpObserver<List<RecordVideoBean>>() {
            @Override
            public void onNext(String message, List<RecordVideoBean> data) {
                if (CollectionUtils.isEmpty(data)){
                    mIView.showEmpty();
                }else {
                    mIView.loadRecordVideoList(data);
                }
                mIView.showContent();
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.showError();
            }

            @Override
            public void onComplete() {}
        },mIView.getLifeSubject());
    }

    /*专业直播:删除视频列表数据*/
    public void professionalLiveDeleteRecordVideoListItem(String id,int itemLayoutPosition){
        mIView.showLoadingDialog();
        mModel.professionalLiveDeleteRecordVideoListItem(id, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                mIView.dismissLoadingDialog();
                mIView.setDeleteVideoResult(itemLayoutPosition);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                CommonUtils.showMsg("温馨提示:删除失败" + errCode);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }


}
