package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.GuideSpotContentAndImageBean;
import com.guolaiwan.bean.MerchantBean;

import java.util.List;

/**
 * 创建者: 蔡朝阳
 * 日期:  2018/11/25.
 * 说明:  导览点详情界面View接口
 */

public interface GuideSpotView extends IBaseVIew {
    void setContentAndImage(List<GuideSpotContentAndImageBean> guideSpotContentAndImageBeanList);
}
