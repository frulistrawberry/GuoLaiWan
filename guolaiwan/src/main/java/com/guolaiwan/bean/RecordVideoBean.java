package com.guolaiwan.bean;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/3/7.
 * 说明:
 */
public class RecordVideoBean {
    private String id;
    private String liveId;
    private String subLiveId;
    private String localPath;
    private String pubUrl;
    private String recordName;
    private String subLiveName;

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

    public String getSubLiveId() {
        return subLiveId;
    }

    public void setSubLiveId(String subLiveId) {
        this.subLiveId = subLiveId;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getPubUrl() {
        return pubUrl;
    }

    public void setPubUrl(String pubUrl) {
        this.pubUrl = pubUrl;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getSubLiveName() {
        return subLiveName;
    }

    public void setSubLiveName(String subLiveName) {
        this.subLiveName = subLiveName;
    }

}
