package com.guolaiwan.bean;

import com.google.gson.annotations.SerializedName;



public class MessageBean {
    int userId;
    String message;
    @SerializedName("userName")
    String nickName;
    boolean isSystem;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public String getUserId() {
        return userId+"";
    }
}
