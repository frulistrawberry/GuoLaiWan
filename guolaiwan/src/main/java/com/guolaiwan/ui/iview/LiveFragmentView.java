package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.LiveListBean;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;

import java.util.List;


public interface LiveFragmentView extends IBaseVIew{
    void setProgfessionalLiveOrderInfo(ProfessionalLiveOrderBean data);
}
