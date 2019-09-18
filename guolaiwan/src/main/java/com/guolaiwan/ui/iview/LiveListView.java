package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.LiveListBean;

import java.util.List;


public interface LiveListView extends IBaseVIew{
    void loadLiveList(List<LiveListBean> liveList);
}
