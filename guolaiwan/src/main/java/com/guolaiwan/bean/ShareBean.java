package com.guolaiwan.bean;

/**
 * 创建者: 蔡朝阳
 * 日期:  2018/12/24.
 * 说明:  用于区分在哪分享
 */

public class ShareBean {
    private String shareType;
    private String url;
    private String title;
    private String description;

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
