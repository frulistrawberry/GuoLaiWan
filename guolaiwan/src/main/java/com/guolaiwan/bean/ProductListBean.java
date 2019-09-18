package com.guolaiwan.bean;

import org.w3c.dom.ProcessingInstruction;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/13
 * 描述:
 */

public class ProductListBean implements Serializable {
    private int count;
    private List<ProductBean> products;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ProductBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductBean> products) {
        this.products = products;
    }
}
