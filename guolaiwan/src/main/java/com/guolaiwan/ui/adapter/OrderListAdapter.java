package com.guolaiwan.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.autonavi.ae.dice.InitConfig;
import com.autonavi.rtbt.IFrameForRTBT;
import com.cgx.library.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.OrderBean;
import com.guolaiwan.ui.activity.MerchantOrderInfoActivity;
import com.guolaiwan.ui.activity.OrderInfoActivity;
import com.guolaiwan.utils.ViewUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/27
 * 描述:
 */

public class OrderListAdapter extends BaseQuickAdapter<OrderBean,BaseViewHolder> {
    String uType = "USER";
    public OrderListAdapter() {
        super(R.layout.item_order_list);
    }

    public String getuType() {
        return uType;
    }

    public void setuType(String uType) {
        this.uType = uType;
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderBean item) {
        helper.setImageUrl(R.id.iv_img,item.getProductPic());
        helper.setText(R.id.tv_product_name,item.getProductName());
        helper.setText(R.id.tv_count,"数量x"+item.getProductNum());
        helper.setText(R.id.tv_price,"￥"+item.getProductPrice());
        helper.setText(R.id.tv_product_info,"共"+item.getProductNum()+"件商品"+"  "+"合计:￥"+item.getPayMoney());
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uType.equals("USER"))
                    OrderInfoActivity.launch(mContext,item.getId()+"");
                else
                    MerchantOrderInfoActivity.launch(mContext, item.getId()+"");
            }
        });
    }
}
