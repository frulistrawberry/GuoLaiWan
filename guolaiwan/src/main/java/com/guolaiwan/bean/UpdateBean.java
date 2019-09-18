package com.guolaiwan.bean;

/**
 * Created by Administrator on 2018/5/27/027.
 */

public class UpdateBean {
    private boolean forceUpdate;
    private int versionName;
    private int versionCode;
    private String content;
    private String url;
    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
    public boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setVersionName(int versionName) {
        this.versionName = versionName;
    }
    public int getVersionName() {
        return versionName;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
    public int getVersionCode() {
        return versionCode;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
