package com.guolaiwan.bean;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/2/14.
 * 说明:
 */

public class ProfessionalLiveWatcherCheckLiveStateBean {

    private String liveName;
    private String vedioUrl;
    private String broadCastCamera;
    private String liveStatus;
    //机位是否可以关闭
    private String cameraCanClose;


    public String getLiveName() {
        return liveName;
    }

    public void setLiveName(String liveName) {
        this.liveName = liveName;
    }

    public String getVedioUrl() {
        return vedioUrl;
    }

    public void setVedioUrl(String vedioUrl) {
        this.vedioUrl = vedioUrl;
    }

    public String getBroadCastCamera() {
        return broadCastCamera;
    }

    public void setBroadCastCamera(String broadCastCamera) {
        this.broadCastCamera = broadCastCamera;
    }

    public String getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getCameraCanClose() {
        return cameraCanClose;
    }

    public void setCameraCanClose(String cameraCanClose) {
        this.cameraCanClose = cameraCanClose;
    }
}
