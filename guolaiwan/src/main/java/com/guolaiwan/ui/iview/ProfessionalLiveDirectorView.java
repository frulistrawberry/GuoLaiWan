package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.ProfessionalLiveCameraInfoBean;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.bean.ProfessionalLiveSubLiveBean;
import com.guolaiwan.bean.ProfessionalLiveWatcherCheckLiveStateBean;

import java.util.List;

public interface ProfessionalLiveDirectorView extends IBaseVIew {
    void setDirectorState(ProfessionalLiveWatcherCheckLiveStateBean professionalLiveWatcherCheckLiveStateBean);
    void initEveryCamera(ProfessionalLiveCameraInfoBean cameraInfo);
    void setCameraLiveState(ProfessionalLiveSubLiveBean professionalLiveSubLiveBean);
    void openOrCloseDirectorLiveResult(String liveStatus);
    void updateLiveNameResult();
    void setBroadCastCamera(String broadCastCamera);
    void startRecordResult(String cameraPosition);
    void stopRecordResult(String cameraPosition);
}
