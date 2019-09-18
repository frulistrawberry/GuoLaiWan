package com.guolaiwan.bean;

import java.util.List;

public class HomeMerchantsBean {
    private String multiple;
    private List<MerchantBean> merchants;

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public List<MerchantBean> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<MerchantBean> merchants) {
        this.merchants = merchants;
    }
}
