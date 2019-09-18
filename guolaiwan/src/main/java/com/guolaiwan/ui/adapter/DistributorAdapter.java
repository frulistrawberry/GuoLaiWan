package com.guolaiwan.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.cgx.library.base.BaseViewHolder;
import com.cgx.library.utils.AppUtils;
import com.cgx.library.utils.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import app.guolaiwan.com.guolaiwan.R;
import com.guolaiwan.bean.DistributorBean;
import com.guolaiwan.ui.activity.ProductInfoActivity;

import java.util.List;


public class DistributorAdapter extends BaseQuickAdapter<DistributorBean,BaseViewHolder> {
    public DistributorAdapter() {
        super(R.layout.item_merchant_product);
    }

    @Override
    protected void convert(BaseViewHolder helper, final DistributorBean item) {
        helper.setImageUrl(R.id.iv_img,item.getShopPic());
        helper.setText(R.id.tv_name,item.getShopName());
        helper.setText(R.id.tv_price,"￥" + item.getDistributorPrice());
        RelativeLayout parent = helper.getView(R.id.layout_parent);
        if (helper.getAdapterPosition()%2 == 0){
            parent.setPadding(SizeUtils.dp2px(AppUtils.getAppContext(),4),0,SizeUtils.dp2px(AppUtils.getAppContext(),8),SizeUtils.dp2px(AppUtils.getAppContext(),8));
        }else {
            parent.setPadding(SizeUtils.dp2px(AppUtils.getAppContext(),8),0,SizeUtils.dp2px(AppUtils.getAppContext(),4),SizeUtils.dp2px(AppUtils.getAppContext(),8));
        }

        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductInfoActivity.launch(mContext,item.getId()+"");
            }
        });
    }
}
