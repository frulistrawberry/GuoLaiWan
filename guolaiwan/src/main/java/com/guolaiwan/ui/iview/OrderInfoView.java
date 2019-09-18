package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.OderInfoBean;

public interface OrderInfoView extends IBaseVIew {
    void closeActivity();
    void loadOrderInfo(OderInfoBean data);
}
