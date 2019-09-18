package com.guolaiwan.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.cgx.library.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.AddressBean;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.ui.activity.MyAddressActivity;

import java.util.List;

import app.guolaiwan.com.guolaiwan.R;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/26
 * 描述:
 */

public class MyAddressAdapter extends BaseQuickAdapter<AddressBean,BaseViewHolder> {

    private MyAddressActivity mMyAddressActivity;
    private boolean mIsForChoose;


    public MyAddressAdapter(MyAddressActivity myAddressActivity,boolean isForChoose) {
        super(R.layout.item_address);
        this.mMyAddressActivity = myAddressActivity;
        this.mIsForChoose = isForChoose;
    }


    @Override
    protected void convert(BaseViewHolder helper, AddressBean item) {

        helper.setText(R.id.tvName,item.getConsigneeName());
        helper.setText(R.id.tvPhone,item.getConsigneePhone());
        helper.setText(R.id.tvAddress,item.getProvince()+" "+item.getCity()+" "+item.getDistrict()+ " " + item.getConsigneeAddress());

        if(mIsForChoose == true){
            helper.setGone(R.id.ll_delate,false);
            helper.getView(R.id.ll_info).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mIsForChoose == true){
                        mMyAddressActivity.setResultForTargetActivity(item);
                    }
                }
            });
        }else {
            helper.setGone(R.id.ll_delate,true);
            helper.getView(R.id.ll_delate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallBack != null){
                        mCallBack.onDelete(item.getId() +"");
                    }
                }
            });
        }
    }

    private CallBack mCallBack;
    public void setDelCallBack(CallBack callBack) {
        this.mCallBack = callBack;
    }

    public  interface CallBack{
        void onDelete(String addressId);
    }


}
