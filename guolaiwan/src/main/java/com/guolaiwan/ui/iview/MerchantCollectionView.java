package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.ProductBean;

import java.util.List;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/26
 * 描述:
 */

public interface MerchantCollectionView extends IBaseVIew{
     void loadCollectList(List<MerchantBean> data);
}
