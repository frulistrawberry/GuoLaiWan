package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.ShopChartBean;

import java.util.List;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/24
 * 描述:
 */

public interface ShopCartView extends IBaseVIew {
    void loadShopCart(List<ShopChartBean> shopChartList);
    void reload();
}
