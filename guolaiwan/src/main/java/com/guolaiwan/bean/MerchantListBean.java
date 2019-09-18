package com.guolaiwan.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/13
 * 描述:
 */

public class MerchantListBean implements Serializable {
    private int count;
    private List<MerchantBean> merchants;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MerchantBean> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<MerchantBean> merchants) {
        this.merchants = merchants;
    }
}
