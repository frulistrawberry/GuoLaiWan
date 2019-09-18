package com.guolaiwan.bean;

import java.io.Serializable;

public class CommentBean implements Serializable {
    private String CommentMPic;
    private String userHeadimg;
    private String userName;
    private String content;
    private String userDate;
    private int start;
    private String merContent;
    private String merDate;

    public String getCommentMPic() {
        return CommentMPic;
    }

    public void setCommentMPic(String commentMPic) {
        CommentMPic = commentMPic;
    }

    public String getUserHeadimg() {
        return userHeadimg;
    }

    public void setUserHeadimg(String userHeadimg) {
        this.userHeadimg = userHeadimg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getMerContent() {
        return merContent;
    }

    public void setMerContent(String merContent) {
        this.merContent = merContent;
    }

    public String getMerDate() {
        return merDate;
    }

    public void setMerDate(String merDate) {
        this.merDate = merDate;
    }
}
