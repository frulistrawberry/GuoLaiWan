package com.guolaiwan.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cgx.library.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;

import app.guolaiwan.com.guolaiwan.R;

public class AddImageAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

    private OnDeleteListener onDeleteListener;

    public interface OnDeleteListener{
        void onDelete(RecyclerView.ViewHolder holder, String s);
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public AddImageAdapter() {
        super(R.layout.item_pic);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (item.equals("add")){
            helper.setImageResource(R.id.btn_initiate_topic_add,R.mipmap.initiate_topic_add_image);
            helper.setGone(R.id.btn_initiate_topic_delete,false);
        }else {
            Glide.with(mContext).load(item).centerCrop().into((ImageView) helper.getView(R.id.btn_initiate_topic_add));
            helper.setGone(R.id.btn_initiate_topic_delete,true);
        }
        helper.getView(R.id.btn_initiate_topic_delete).setOnClickListener(v -> {
            if (onDeleteListener != null) {
                onDeleteListener.onDelete(helper,item);
            }
        });
    }
}
