package com.guolaiwan.bean;

/**
 * 创建者: 蔡朝阳
 * 日期:  2018/11/25.
 * 说明:  讲解点详情数据bean
 */

public class GuideSpotContentAndImageBean {

    private String id;
    private String uuid;
    private String updateTime;
    private String childPic;
    private String childContent;
    private String childId;
    private String voiceId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getChildPic() {
        return childPic;
    }

    public void setChildPic(String childPic) {
        this.childPic = childPic;
    }

    public String getChildContent() {
        return childContent;
    }

    public void setChildContent(String childContent) {
        this.childContent = childContent;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getVoiceId() {
        return voiceId;
    }

    public void setVoiceId(String voiceId) {
        this.voiceId = voiceId;
    }
}
