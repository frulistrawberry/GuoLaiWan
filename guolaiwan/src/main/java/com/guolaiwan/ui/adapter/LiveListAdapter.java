package com.guolaiwan.ui.adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.cgx.library.base.BaseViewHolder;
import com.cgx.library.utils.AppUtils;
import com.cgx.library.utils.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import app.guolaiwan.com.guolaiwan.R;
import com.guolaiwan.bean.LiveListBean;
import com.guolaiwan.ui.activity.PlayerActivity;
import com.guolaiwan.ui.activity.ProfessionalLivePlayerActivity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/16/016.
 */

public class LiveListAdapter extends BaseQuickAdapter<LiveListBean,BaseViewHolder> {


    public LiveListAdapter() {
        super(R.layout.item_live_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, final LiveListBean item) {
        RelativeLayout parent = helper.getView(R.id.layout_parent);
        if (helper.getAdapterPosition() % 2 == 0){
            parent.setPadding(SizeUtils.dp2px(AppUtils.getAppContext(),5),0,SizeUtils.dp2px(AppUtils.getAppContext(),15),SizeUtils.dp2px(AppUtils.getAppContext(),10));
        }else {
            parent.setPadding(SizeUtils.dp2px(AppUtils.getAppContext(),15),0,SizeUtils.dp2px(AppUtils.getAppContext(),5),SizeUtils.dp2px(AppUtils.getAppContext(),10));
        }
        if(item.getLiveType().equals("PROFESSIONAL_LIVE")){
            //专业直播
            helper.setText(R.id.tv_live_name,item.getLiveName());
            if (item.getLiveStatusType().equals("LIVING")){
                helper.setText(R.id.tv_live_status,"直播中");
            } else{
                helper.setText(R.id.tv_live_status,"已结束");
            }
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProfessionalLivePlayerActivity.launch(mContext,item.getLiveId());
                }
            });

        }else {
            //普通直播
            helper.setText(R.id.tv_live_name,item.getLiveName());
            if (item.getLiveStatusType().equals("LIVING")){
                helper.setText(R.id.tv_live_status,"直播中");
            } else{
                helper.setText(R.id.tv_live_status,"已结束");
            }
            helper.setImageUrl(R.id.iv_img,item.getCover());
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlayerActivity.launch(mContext,item.getId()+"");
                }
            });
        }
    }
}
