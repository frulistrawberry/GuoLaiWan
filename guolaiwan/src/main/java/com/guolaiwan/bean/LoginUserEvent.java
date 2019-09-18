package com.guolaiwan.bean;

import java.util.List;

/**
 * 蔡朝阳:刷新服务器已登录用户游览景点
 */

public class LoginUserEvent {

    public String action;
    public List<String> childIdList;


    public LoginUserEvent(String action,List<String> childList){
        this.action = action;
        this.childIdList = childList;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getChildIdList() {
        return childIdList;
    }

    public void setChildIdList(List<String> childIdList) {
        this.childIdList = childIdList;
    }
}
