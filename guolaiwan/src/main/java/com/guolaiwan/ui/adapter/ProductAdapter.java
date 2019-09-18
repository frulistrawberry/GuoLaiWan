package com.guolaiwan.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.cgx.library.base.BaseViewHolder;
import com.cgx.library.utils.AppUtils;
import com.cgx.library.utils.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.ui.activity.ProductInfoActivity;

import app.guolaiwan.com.guolaiwan.R;
import mabeijianxi.camera.util.Log;


public class ProductAdapter extends BaseQuickAdapter<ProductBean,BaseViewHolder> {

    private Context mContext;
    private boolean isChoose;
    private boolean isCollect;
    private boolean isJiFen;
    CallBack callBack;

    public void setJiFen(boolean jiFen) {
        isJiFen = jiFen;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public ProductAdapter(Context context) {
        super(R.layout.item_merchant_product);
        this.mContext = context;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ProductBean item) {
        helper.setImageUrl(R.id.iv_img,item.getProductShowPic());
        Log.i("CAI",item.getProductShowPic());
        helper.setText(R.id.tv_name,item.getProductName());
        helper.setText(R.id.tv_price,"￥" + item.getProductPrice());
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


        if (isChoose){
            helper.getConvertView().setOnClickListener(v -> {

                for (int i = 0; i < mData.size(); i++) {
                    if (i == helper.getAdapterPosition()){
                        mData.get(i).setCheck(true);
                    }else {
                        mData.get(i).setCheck(false);
                    }
                }
                notifyDataSetChanged();
            });
        }else if (isCollect){
            helper.getConvertView().setOnClickListener(v -> ProductInfoActivity.launch(mContext,item.getId()+""));
        }else if (isJiFen){
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callBack != null) {
                        callBack.onDelete(item);
                    }
                }
            });

        }
        if (isChoose&&item.isCheck()){
            helper.setGone(R.id.iv_check,true);
        }else {
            helper.setGone(R.id.iv_check,false);
        }
        if (isCollect){
            helper.setGone(R.id.iv_delete,true);
        }else {
            helper.setGone(R.id.iv_delete,false);
        }
        helper.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack!=null){
                    callBack.onDelete(item);
                }
            }
        });

    }

    public  interface CallBack{
        void onDelete(ProductBean item);
    }

}
