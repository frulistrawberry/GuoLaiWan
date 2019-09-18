package com.guolaiwan.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.guolaiwan.constant.ItemType;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者: 蔡朝阳
 * 日期:  2018/12/17.
 * 说明:  首页分类使用的数据实体
 *        将ModularBean的集合和ActivityBean的集合封装在一个实体类中
 *        以进行首页分类的两行滑动展示
 */

public class ClassificationBean implements Serializable {

    private String activityId;
    private String activityName;
    private String pic;
    private String modularCode;
    private String modularName;
    private String modularPic;
    private String type;


    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getModularCode() {
        return modularCode;
    }

    public void setModularCode(String modularCode) {
        this.modularCode = modularCode;
    }

    public String getModularName() {
        return modularName;
    }

    public void setModularName(String modularName) {
        this.modularName = modularName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getModularPic() {
        return modularPic;
    }

    public void setModularPic(String modularPic) {
        this.modularPic = modularPic;
    }

}
