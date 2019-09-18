package com.guolaiwan.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cgx.library.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.ImageFloder;

import app.guolaiwan.com.guolaiwan.R;

public class ImageDirListAdapter extends BaseQuickAdapter<ImageFloder,BaseViewHolder> {
    public ImageDirListAdapter() {
        super(R.layout.item_photo_picker_pop);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageFloder imageFloder) {
        helper.setText(R.id.text_photo_picker_dir_name,imageFloder.getDirName());
        helper.setText(R.id.text_photo_picker_dir_count,imageFloder.getImageCount()+"张");
        ImageView imageView = helper.getView(R.id.image_photo_picker_dir_icon);
        Glide.with(mContext).load(imageFloder.getFirstImagePath()).into(imageView);
    }
}
