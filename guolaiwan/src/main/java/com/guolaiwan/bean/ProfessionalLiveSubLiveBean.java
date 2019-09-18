package com.guolaiwan.bean;

import java.io.Serializable;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/1/26.
 * 说明:
 */

public class ProfessionalLiveSubLiveBean implements Serializable{

    private String id;
    private String liveId;
    private String cameraNumber;
    private String inUse;
    private String recordState;
    private String liveName;
    private String pubName;
    private String status;
    //是否录制可用
    private String isRecord;
    //是否垫播
    private String isMatPlay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getCameraNumber() {
        return cameraNumber;
    }

    public void setCameraNumber(String cameraNumber) {
        this.cameraNumber = cameraNumber;
    }

    public String getInUse() {
        return inUse;
    }

    public void setInUse(String inUse) {
        this.inUse = inUse;
    }

    public String getRecordState() {
        return recordState;
    }

    public void setRecordState(String recordState) {
        this.recordState = recordState;
    }

    public String getLiveName() {
        return liveName;
    }

    public void setLiveName(String liveName) {
        this.liveName = liveName;
    }

    public String getPubName() {
        return pubName;
    }

    public void setPubName(String pubName) {
        this.pubName = pubName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsRecord() {
        return isRecord;
    }

    public void setIsRecord(String isRecord) {
        this.isRecord = isRecord;
    }

    public String getIsMatPlay() {
        return isMatPlay;
    }

    public void setIsMatPlay(String isMatPlay) {
        this.isMatPlay = isMatPlay;
    }
}
