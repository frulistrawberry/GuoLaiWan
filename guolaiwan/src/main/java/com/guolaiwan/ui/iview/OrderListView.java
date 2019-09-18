package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.OrderBean;

import java.util.List;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/27
 * 描述:
 */

public interface OrderListView extends IBaseVIew {
    void loadOrderList(List<OrderBean> data);
}
