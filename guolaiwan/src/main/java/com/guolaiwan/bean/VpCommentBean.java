package com.guolaiwan.bean;

/**
 * Created by Administrator on 2018/5/26/026.
 */

public class VpCommentBean {
    private int id;
    private String uuid;
    private String updateTime;
    private int userId;
    private int auserId;
    private String commentText;
    private FriendBean.User user;
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getUuid() {
        return uuid;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateTime() {
        return updateTime;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserId() {
        return userId;
    }

    public void setAuserId(int auserId) {
        this.auserId = auserId;
    }
    public int getAuserId() {
        return auserId;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
    public String getCommentText() {
        return commentText;
    }

    public void setUser(FriendBean.User user) {
        this.user = user;
    }
    public FriendBean.User getUser() {
        return user;
    }

}
