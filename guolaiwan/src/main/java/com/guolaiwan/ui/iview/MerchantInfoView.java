package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.MerchantInfoBean;
import com.guolaiwan.bean.ProductBean;

import java.util.List;


public interface MerchantInfoView extends IBaseVIew{
    void loadMerchantInfo(MerchantInfoBean merchantInfo);
    void loadProductList(List<ProductBean> productList);
    void setCollectImageState();
}
