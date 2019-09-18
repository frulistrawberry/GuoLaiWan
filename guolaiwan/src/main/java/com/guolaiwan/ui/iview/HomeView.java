package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.guolaiwan.bean.CompanyBean;
import com.guolaiwan.bean.RecommendBean;
import com.guolaiwan.model.HomeModel;

import java.util.List;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/10
 * 描述:
 */

public interface HomeView extends IBaseVIew{
    void loadData(List<MultiItemEntity> data);
    void loadBanner(List<RecommendBean> bannerData);
    void loadPop(List<CompanyBean> popData);
}
