package com.guolaiwan.bean;

import java.io.Serializable;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/1/22.
 * 说明:
 */

public class ProfessionalLiveOrderBean implements Serializable {
    private String id;
    private String liveId;
    private String startTime;
    private String endTime;
    private String count;
    private String recordSize;
    private String status;
    private String isMatPlay;
    private String totalFee;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getRecordSize() {
        return recordSize;
    }

    public void setRecordSize(String recordSize) {
        this.recordSize = recordSize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsMatPlay() {
        return isMatPlay;
    }

    public void setIsMatPlay(String isMatPlay) {
        this.isMatPlay = isMatPlay;
    }
}
