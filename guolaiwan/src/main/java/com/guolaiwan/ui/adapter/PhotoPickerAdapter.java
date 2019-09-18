package com.guolaiwan.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cgx.library.base.BaseViewHolder;
import com.cgx.library.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;

public class PhotoPickerAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    private ArrayList<String> selectedImage = new ArrayList<>();

    private int selectedCount = 0;
    public void setSelectedCount(int selectedCount) {
        this.selectedCount = selectedCount;
    }

    public ArrayList<String> getSelectedImage() {
        return selectedImage;
    }
    public PhotoPickerAdapter() {
        super(R.layout.item_photo_picker);
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        holder.setImageResource(R.id.image_photo_picker, R.mipmap.pictures_no);
        holder.setImageResource(R.id.btn_photo_picker_selected, R.mipmap.check_false);
        ImageView imageView = holder.getView(R.id.image_photo_picker);
        Glide.with(mContext).load(item).centerCrop().into(imageView);
        final ImageView imgs = holder.getView(R.id.image_photo_picker);
        imgs.setColorFilter(null);
        holder.getView(R.id.image_photo_picker).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImage.clear();
                selectedImage.add(item);
                notifyDataSetChanged();
            }
        });
        if (selectedImage.contains(item)){
            holder.setImageResource(R.id.btn_photo_picker_selected,R.mipmap.check_true);
            imageView.setColorFilter(Color.parseColor("#88000000"));
        }
    }
}
