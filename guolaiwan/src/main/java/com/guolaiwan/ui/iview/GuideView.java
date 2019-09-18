package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.Child;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.UserBean;
import com.guolaiwan.bean.VoiceBean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/22/022.
 */

public interface GuideView extends IBaseVIew {
    void addMarkers(List<MerchantBean> merchantList);
    void addChildren(List<Child> children);
    void setSpotMarkerState(UserBean data);
    void setVoice(List<VoiceBean> voiceBeanList);
    void shareMyTravel(String url,String merchantName);
    void showRoad(List<Child> childOnRoad);
}
