package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.guolaiwan.bean.FilerBean;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.ProductBean;

import java.util.List;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/13
 * 描述:
 */

public interface CategoryView extends IBaseVIew {
   public static final int TYPE_MODU = 1;
   public static final int TYPE_ACT = 2;


    void loadModuBanner(List<MerchantBean> bannerData);
    void loadModuRetrieval(List<FilerBean> retrievalData);
    void loadMerchantList(List<MultiItemEntity> merchantListData);

    void loadActBanner(List<ProductBean> bannerData);
    void loadActProductList(List<MultiItemEntity> productListData);
}
