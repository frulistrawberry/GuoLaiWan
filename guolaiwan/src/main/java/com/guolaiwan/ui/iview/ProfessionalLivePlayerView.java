package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.MessageBean;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/2/14.
 * 说明:
 */

public interface ProfessionalLivePlayerView extends IBaseVIew {
    void showsNoSignalNotice();
    void setVedioUrl(String liveName,String vedioUrl);
    void refreshCommentMessage(MessageBean messageBean);
}
