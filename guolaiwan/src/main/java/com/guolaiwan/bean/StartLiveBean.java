package com.guolaiwan.bean;



public class StartLiveBean {
    String id;
    String liveName;
    String nickName;
    String headImg;
    String userId;

    FriendBean.User user;

    public FriendBean.User getUser() {
        return user;
    }

    public void setUser(FriendBean.User user) {
        this.user = user;
    }

    public String getLiveName() {
        return liveName;
    }

    public void setLiveName(String liveName) {
        this.liveName = liveName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }
}
