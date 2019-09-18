package com.guolaiwan.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/11
 * 描述:
 */

public class HomeList implements Serializable {
    private List<ModularBean> modulars;
    private List<DistributorBean> distributors;
    private List<ActivityBean> activitys;
    private List<SpecialEventsBean> SpecialEventsBean;
    private TodayHotSearchBeanListBean todayhotsearch;

    public TodayHotSearchBeanListBean getTodayhotsearch() {
        return todayhotsearch;
    }

    public void setTodayhotsearch(TodayHotSearchBeanListBean todayhotsearch) {
        this.todayhotsearch = todayhotsearch;
    }

    public List<com.guolaiwan.bean.SpecialEventsBean> getSpecialEventsBean() {
        return SpecialEventsBean;
    }

    public void setSpecialEventsBean(List<com.guolaiwan.bean.SpecialEventsBean> specialEventsBean) {
        SpecialEventsBean = specialEventsBean;
    }

    public List<ModularBean> getModulars() {
        return modulars;
    }

    public void setModulars(List<ModularBean> modulars) {
        this.modulars = modulars;
    }

    public List<DistributorBean> getDistributors() {
        return distributors;
    }

    public void setDistributors(List<DistributorBean> distributors) {
        this.distributors = distributors;
    }

    public List<ActivityBean> getActivitys() {
        return activitys;
    }

    public void setActivitys(List<ActivityBean> activitys) {
        this.activitys = activitys;
    }
}
