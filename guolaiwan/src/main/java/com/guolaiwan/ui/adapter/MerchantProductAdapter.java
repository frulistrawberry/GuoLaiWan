package com.guolaiwan.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgx.library.base.BaseViewHolder;
import com.cgx.library.utils.AppUtils;
import com.cgx.library.utils.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import app.guolaiwan.com.guolaiwan.R;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.ui.activity.ProductInfoActivity;


public class MerchantProductAdapter extends BaseQuickAdapter<ProductBean,BaseViewHolder> {

    private Context mContext;

    public MerchantProductAdapter(Context context) {
        super(R.layout.item_merchant_product);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ProductBean item) {
        helper.setImageUrl(R.id.iv_img,item.getProductShowPic());
        helper.setText(R.id.tv_name,item.getProductName());
        helper.setText(R.id.tv_price,"￥" + item.getProductPrice());
        if(item.getProductPrice() < item.getProductOldPrice()){
            helper.setGone(R.id.tv_original_price,true);
            TextView originalPriceTv = helper.getView(R.id.tv_original_price);
            originalPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            originalPriceTv.getPaint().setAntiAlias(true);
            originalPriceTv.setText("￥" + item.getProductOldPrice());
        }
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
