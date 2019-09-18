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

public class ClassificationListBean implements MultiItemEntity, Serializable {


    List<ClassificationBean> classificationBeanList;

    public List<ClassificationBean> getClassificationBeanList() {
        return classificationBeanList;
    }

    public void setClassificationBeanList(List<ClassificationBean> classificationBeanList) {
        this.classificationBeanList = classificationBeanList;
    }

    @Override
    public int getItemType() {
        return ItemType.ITEM_TYPE_CLASSIFICATION;
    }
}
