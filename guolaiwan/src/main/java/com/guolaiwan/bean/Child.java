package com.guolaiwan.bean;


import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/12/012.
 */

public class Child implements Serializable {

    String id;
    String childName;
    String childPic;
    String chineseGirl;
    String chineseBoy;
    String englishGirl;
    String englishBoy;
    String childLongitude;
    String childLatitude;
    String lanId;
    String content;
    String childScale;
    String childRoad;
    String isCen;
    String chineseContent;
    String englishContent;
    //是否讲解
    String isTaught;
    //讲解范围
    String scope;
    //路线指引方向
    String guideDirection;
    //路线指引提示语
    String guideText;

    public String getIsTaught() {
        return isTaught;
    }

    public void setIsTaught(String isTaught) {
        this.isTaught = isTaught;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getEnglishContent() {
        return englishContent;
    }

    public void setEnglishContent(String englishContent) {
        this.englishContent = englishContent;
    }

    public String getChineseContent() {
        return chineseContent;
    }

    public void setChineseContent(String chineseContent) {
        this.chineseContent = chineseContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildPic() {
        return childPic;
    }

    public void setChildPic(String childPic) {
        this.childPic = childPic;
    }

    public String getChildLongitude() {
        return childLongitude;
    }

    public void setChildLongitude(String childLongitude) {
        this.childLongitude = childLongitude;
    }

    public String getChildLatitude() {
        return childLatitude;
    }

    public void setChildLatitude(String childLatitude) {
        this.childLatitude = childLatitude;
    }

    public String getLanId() {
        return lanId;
    }

    public void setLanId(String lanId) {
        this.lanId = lanId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChildScale() {
        return childScale;
    }

    public void setChildScale(String childScale) {
        this.childScale = childScale;
    }

    public String getChildRoad() {
        return childRoad;
    }

    public void setChildRoad(String childRoad) {
        this.childRoad = childRoad;
    }

    public String getIsCen() {
        return isCen;
    }

    public void setIsCen(String isCen) {
        this.isCen = isCen;
    }

    public String getShopLatitude() {
        return childLatitude;
    }

    public String getShopLongitude() {
        return childLongitude;
    }

    public String getChineseGirl() {
        return chineseGirl;
    }

    public void setChineseGirl(String chineseGirl) {
        this.chineseGirl = chineseGirl;
    }

    public String getChineseBoy() {
        return chineseBoy;
    }

    public void setChineseBoy(String chineseBoy) {
        this.chineseBoy = chineseBoy;
    }

    public String getEnglishGirl() {
        return englishGirl;
    }

    public void setEnglishGirl(String englishGirl) {
        this.englishGirl = englishGirl;
    }

    public String getEnglishBoy() {
        return englishBoy;
    }

    public void setEnglishBoy(String englishBoy) {
        this.englishBoy = englishBoy;
    }

    public String getGuideDirection() {
        return guideDirection;
    }

    public void setGuideDirection(String guideDirection) {
        this.guideDirection = guideDirection;
    }

    public String getGuideText() {
        return guideText;
    }

    public void setGuideText(String guideText) {
        this.guideText = guideText;
    }
}
