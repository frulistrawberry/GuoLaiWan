package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;

public interface ProfessionalLiveOrderView extends IBaseVIew {
    void gotoPay(ProfessionalLiveOrderBean professionalLiveOrderBean);
    void setCheckedPrice(String checkedPrice);
}
