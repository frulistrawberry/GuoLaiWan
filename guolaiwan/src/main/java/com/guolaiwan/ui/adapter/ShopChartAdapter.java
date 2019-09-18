package com.guolaiwan.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cgx.library.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.ShopChartBean;

import app.guolaiwan.com.guolaiwan.R;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/24
 * 描述:
 */

public class ShopChartAdapter extends BaseQuickAdapter<ShopChartBean,BaseViewHolder> {
    private Callback mCallback;

    public interface Callback{
        void onDelete(ShopChartBean item);
        void onEdit(ShopChartBean item);
        void onCheck();
    }

    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public ShopChartAdapter() {
        super(R.layout.item_shop_cart);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ShopChartBean item) {
        CheckBox checkBox = helper.getView(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setChecked(isChecked);
                mCallback.onCheck();
            }
        });
        TextView addTv = helper.getView(R.id.tv_add);
        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = item.getProductNum()+1;
                item.setProductNum(count);
                helper.setText(R.id.tv_count,count+"");
                mCallback.onEdit(item);
            }
        });
        TextView delTv = helper.getView(R.id.tv_del);
        delTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = item.getProductNum();
                if (count ==1)
                    return;
                count--;
                item.setProductNum(count);
                helper.setText(R.id.tv_count,count+"");
                mCallback.onEdit(item);
            }
        });
        helper.setText(R.id.tv_count,item.getProductNum()+"");
        helper.setText(R.id.tv_name,item.getProductName());
        helper.setText(R.id.tv_price,"￥"+item.getProductPrice());
        helper.setImageUrl(R.id.iv_img,item.getProductPic());
        helper.setChecked(R.id.checkbox,item.isChecked());
        if (helper.getAdapterPosition() == 0)
            helper.setGone(R.id.v_divider,false);
        else
            helper.setGone(R.id.v_divider,true);
        helper.setOnClickListener(R.id.tv_del_product, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onDelete(item);
            }
        });

    }
}
