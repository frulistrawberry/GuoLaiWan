package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.MessageBean;
import com.guolaiwan.bean.ProfessionalLiveSubLiveBean;

public interface ProfessionalLiveLiveView extends IBaseVIew {
    void setProfessionalLiveDate(ProfessionalLiveSubLiveBean professionalLiveSubLiveBean);
    void refreshCommentMessage(MessageBean messageBean);
}
