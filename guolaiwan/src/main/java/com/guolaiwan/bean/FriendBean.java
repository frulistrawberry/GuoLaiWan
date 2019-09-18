package com.guolaiwan.bean;

import java.io.Serializable;

public class FriendBean implements Serializable {
    private int id;
    private String uuid;
    private String updateTime;
    private String name;
    private String fUrl;
    private String content;
    private String type;
    private int visitNum;
    private int userId;
    private User user;
    private String comments;
    private int commentCount;
    private String praises;
    private int praiseCount;
    private int isDelete;
    private int isPraise;
    private String headPic;

    public String getfUrl() {
        return fUrl;
    }

    public void setfUrl(String fUrl) {
        this.fUrl = fUrl;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

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

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setFUrl(String fUrl) {
        this.fUrl = fUrl;
    }
    public String getFUrl() {
        return fUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setVisitNum(int visitNum) {
        this.visitNum = visitNum;
    }
    public int getVisitNum() {
        return visitNum;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserId() {
        return userId;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getComments() {
        return comments;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    public int getCommentCount() {
        return commentCount;
    }

    public void setPraises(String praises) {
        this.praises = praises;
    }
    public String getPraises() {
        return praises;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }
    public int getPraiseCount() {
        return praiseCount;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
    public int getIsDelete() {
        return isDelete;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }
    public int getIsPraise() {
        return isPraise;
    }

    public static class User {

        private int id;
        private String uuid;
        private String updateTime;
        private String userPhone;
        private String userPassword;
        private String userOpenID;
        private int userIntegral;
        private String userHeadimg;
        private String userNickname;

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

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserOpenID(String userOpenID) {
            this.userOpenID = userOpenID;
        }

        public String getUserOpenID() {
            return userOpenID;
        }

        public void setUserIntegral(int userIntegral) {
            this.userIntegral = userIntegral;
        }

        public int getUserIntegral() {
            return userIntegral;
        }

        public void setUserHeadimg(String userHeadimg) {
            this.userHeadimg = userHeadimg;
        }

        public String getUserHeadimg() {
            return userHeadimg;
        }

        public void setUserNickname(String userNickname) {
            this.userNickname = userNickname;
        }

        public String getUserNickname() {
            return userNickname;
        }
    }

    }
