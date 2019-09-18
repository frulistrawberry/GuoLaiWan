package com.guolaiwan.ui.adapter;

import android.view.View;

import com.cgx.library.base.BaseViewHolder;
import com.cgx.library.utils.FrescoUtil;
import com.cgx.library.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.guolaiwan.bean.GuideSpotContentAndImageBean;
import com.guolaiwan.bean.RichUpload;
import com.guolaiwan.ui.activity.ImageBrowserActivity;

import java.util.ArrayList;

import app.guolaiwan.com.guolaiwan.R;

public class GuideImageAdapter extends BaseQuickAdapter<GuideSpotContentAndImageBean,BaseViewHolder> {

    public GuideImageAdapter() {
        super(R.layout.item_guide_text_image);
    }

    @Override
    protected void convert(BaseViewHolder helper, GuideSpotContentAndImageBean item) {
        String childContent = item.getChildContent();
        String childPic = item.getChildPic();

        SimpleDraweeView simpleDraweeView = helper.getView(R.id.iv_img);

        if (StringUtils.isEmpty(childContent)){
            helper.setGone(R.id.text,false);
        }else {
            helper.setGone(R.id.text,true);
            helper.setText(R.id.text,childContent);
        }

        if (StringUtils.isEmpty(childPic)){
            simpleDraweeView.setVisibility(View.GONE);
        }else {
            simpleDraweeView.setVisibility(View.VISIBLE);
            FrescoUtil.getInstance().loadImageWrapContent(simpleDraweeView,childPic);
            simpleDraweeView.setOnClickListener(v -> {
                ArrayList<String> images = new ArrayList<>();
                images.add(childPic);
                ImageBrowserActivity.launcher(mContext,images);
            });
        }
    }
}
