package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.ProfessionalLiveDirectorBean;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.bean.ProfessionalLiveSubLiveBean;

public interface ProfessionalLiveCameraChoiceView extends IBaseVIew {
    void setCameraUsableCheckResult(ProfessionalLiveSubLiveBean professionalLiveSubLiveBean);
    void setDirectorUsableCheckedResult(ProfessionalLiveDirectorBean professionalLiveDirectorBean);
}
