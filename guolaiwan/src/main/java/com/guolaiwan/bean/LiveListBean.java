package com.guolaiwan.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/16/016.
 */

public class LiveListBean implements Serializable {
    private int id;
    private String uuid;
    private String updateTime;
    private int userId;
    private int merchantId;
    private String pubName;
    private String cover;
    //专业直播也用到liveName
    private String liveName;
    //专业直播也用到liveStatusType
    private String liveStatusType;
    //专业直播也用到liveType
    private String liveType;
    //专业直播特有字段
    private String liveId;
    private String broadcastCamera;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getLiveName() {
        return liveName;
    }

    public void setLiveName(String liveName) {
        this.liveName = liveName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getPubName() {
        return pubName;
    }

    public void setPubName(String pubName) {
        this.pubName = pubName;
    }

    public String getLiveStatusType() {
        return liveStatusType;
    }

    public void setLiveStatusType(String liveStatusType) {
        this.liveStatusType = liveStatusType;
    }

    public String getLiveType() {
        return liveType;
    }

    public void setLiveType(String liveType) {
        this.liveType = liveType;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getBroadcastCamera() {
        return broadcastCamera;
    }

    public void setBroadcastCamera(String broadcastCamera) {
        this.broadcastCamera = broadcastCamera;
    }
}
