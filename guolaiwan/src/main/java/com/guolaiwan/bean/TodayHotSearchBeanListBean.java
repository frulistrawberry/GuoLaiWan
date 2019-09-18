package com.guolaiwan.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.guolaiwan.constant.ItemType;

import java.util.List;

/**
 * 创建者: 蔡朝阳
 * 日期:  2018/12/4.
 * 说明:
 */

public class TodayHotSearchBeanListBean implements MultiItemEntity{
    private List<TodayHotSearchBean> list;

    public List<TodayHotSearchBean> getList() {
        return list;
    }

    public void setList(List<TodayHotSearchBean> list) {
        this.list = list;
    }

    @Override
    public int getItemType() {
        return ItemType.ITEM_TYPE_TODAY_HOT_SEARCH;
    }
}
