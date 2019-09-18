package com.guolaiwan.bean;

import java.io.Serializable;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/1/26.
 * 说明:
 */

public class ProfessionalLiveDirectorBean implements Serializable{

    private String id;
    private String liveId;
    private String inUse;

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

    public String getInUse() {
        return inUse;
    }

    public void setInUse(String inUse) {
        this.inUse = inUse;
    }
}
