package com.guolaiwan.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.guolaiwan.constant.ItemType;

/**
 * 创建者: 蔡朝阳
 * 日期:  2018/12/2.
 * 说明:  首页今日热搜bean
 */

public class TodayHotSearchBean{

    private String id;
    private String uuid;
    private String updatetime;
    private String title;
    private String pic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

}
