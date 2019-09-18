package com.guolaiwan.bean;

import java.util.List;

/**
 * 蔡朝阳:获取分享URL向数据库提交数据bean
 */

public class ShareContentBean {
    //分享时先向后台提交:UserId,景区Id,游览的景点,未游览的景点,步数,公里数,卡路里,天气
    private String userId;//用户id
    private String productId;//景区id
    private String childIds;//浏览过的导览点id
    private String step;//步数
    private String km;//公里数
    private String calorie;//卡路里
    private String weather;//天气

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getChildIds() {
        return childIds;
    }

    public void setChildIds(String childIds) {
        this.childIds = childIds;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }
}
