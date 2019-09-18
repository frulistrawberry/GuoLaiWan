package com.guolaiwan.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.guolaiwan.constant.ItemType;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/10
 * 描述:
 */

public class ModularBean implements Serializable,MultiItemEntity {
    private int id;
    private String uuid;
    private String updateTime;
    private String modularCode;
    private String modularName;
    private int modularIsv;
    private String modularPic;
    private int sort;
    private int comId;
    private String comName;
    private String type;
    private String modularClasses;
    private String products;
    private List<MerchantBean> merchants;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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

    public int getModularIsv() {
        return modularIsv;
    }

    public void setModularIsv(int modularIsv) {
        this.modularIsv = modularIsv;
    }

    public String getModularPic() {
        return modularPic;
    }

    public void setModularPic(String modularPic) {
        this.modularPic = modularPic;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getComId() {
        return comId;
    }

    public void setComId(int comId) {
        this.comId = comId;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getModularClasses() {
        return modularClasses;
    }

    public void setModularClasses(String modularClasses) {
        this.modularClasses = modularClasses;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public List<MerchantBean> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<MerchantBean> merchants) {
        this.merchants = merchants;
    }

    @Override
    public int getItemType() {
        return ItemType.ITEM_TYPE_HOME_MODULAR;
    }
}
