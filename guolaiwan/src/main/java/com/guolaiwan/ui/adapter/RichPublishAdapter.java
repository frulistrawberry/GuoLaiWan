package com.guolaiwan.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cgx.library.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guolaiwan.bean.RichUpload;

import app.guolaiwan.com.guolaiwan.R;

public class RichPublishAdapter extends BaseQuickAdapter<RichUpload.Content,BaseViewHolder> {
    Listener listener;
    public RichPublishAdapter() {
        super(R.layout.item_add_rich);
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, RichUpload.Content item) {
        if (item.type == 1){
            //纯文字
            helper.setGone(R.id.add_img_layout,false);
        }else {
            helper.setGone(R.id.add_img_layout,true);
            if (StringUtils.isEmpty(item.img)){
                helper.setText(R.id.tv_tip,"点击添加图片");
                ((ImageView)helper.getView(R.id.pic)).setImageResource(R.drawable.item_add_icon);
            }
            else{
                helper.setText(R.id.tv_tip,"点击修改图片");
                Glide.with(mContext).load(item.img).centerCrop().into((ImageView) helper.getView(R.id.pic));
            }
        }
        helper.setText(R.id.text, StringUtils.isEmpty(item.text)?"点击添加文字":item.text);
        helper.getView(R.id.pic).setOnClickListener(v -> listener.clickImg(helper.getAdapterPosition()));
        helper.getView(R.id.text).setOnClickListener(v -> listener.clickText(helper.getAdapterPosition()));

    }

    public interface Listener{
        void clickImg(int position);
        void clickText(int position);
    }
}
