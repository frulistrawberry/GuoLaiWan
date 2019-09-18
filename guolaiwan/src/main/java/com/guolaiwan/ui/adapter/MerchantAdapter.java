package com.guolaiwan.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.cgx.library.base.BaseViewHolder;
import com.cgx.library.utils.AppUtils;
import com.cgx.library.utils.FrescoUtil;
import com.cgx.library.utils.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.SpecialEventsBean;
import com.guolaiwan.constant.UrlConstant;
import com.guolaiwan.model.MerchantInfoModel;
import com.guolaiwan.ui.activity.MerchantInfoActivity;
import com.guolaiwan.ui.activity.ProductInfoActivity;

import app.guolaiwan.com.guolaiwan.R;
import mabeijianxi.camera.util.Log;


public class MerchantAdapter extends BaseQuickAdapter<MerchantBean,BaseViewHolder> {

    private Context mContext;
    CallBack callBack;

    public MerchantAdapter(Context context) {
        super(R.layout.item_merchant_product);
        this.mContext = context;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MerchantBean item) {
        helper.setImageUrl(R.id.iv_img,item.getShopPic());
        helper.setText(R.id.tv_name,item.getShopName());
        RelativeLayout parent = helper.getView(R.id.layout_parent);
        int top;
        if (helper.getAdapterPosition() == 0 || helper.getAdapterPosition() == 1){
            top = SizeUtils.dp2px(AppUtils.getAppContext(),8);
        }else {
            top = 0;
        }
        if (helper.getAdapterPosition()%2 == 1){
            parent.setPadding(SizeUtils.dp2px(AppUtils.getAppContext(),4),top,SizeUtils.dp2px(AppUtils.getAppContext(),8),SizeUtils.dp2px(AppUtils.getAppContext(),8));
        }else {
            parent.setPadding(SizeUtils.dp2px(AppUtils.getAppContext(),8),top,SizeUtils.dp2px(AppUtils.getAppContext(),4),SizeUtils.dp2px(AppUtils.getAppContext(),8));
        }

        helper.getConvertView().setOnClickListener(v -> MerchantInfoActivity.launch(mContext,item.getId()+"", MerchantInfoModel.TYPE_MERCHANT));

        helper.setGone(R.id.iv_delete,true);
        helper.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack != null){
                    callBack.onDelete(item);
                }
            }
        });

    }

    public  interface CallBack{
        void onDelete(MerchantBean item);
    }

}
