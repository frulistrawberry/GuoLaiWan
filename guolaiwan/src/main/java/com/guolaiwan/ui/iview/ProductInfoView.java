package com.guolaiwan.ui.iview;


import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.CommentBean;
import com.guolaiwan.bean.DistributorBean;
import com.guolaiwan.bean.ProductInfoBean;

import java.util.List;


public interface ProductInfoView extends IBaseVIew{

    void loadProductInfo(ProductInfoBean productInfo);

    void loadComments(List<CommentBean> comments);

    void loadDistributor(List<DistributorBean> distributors);

}
