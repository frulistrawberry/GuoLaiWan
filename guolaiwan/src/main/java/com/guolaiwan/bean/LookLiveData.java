package com.guolaiwan.bean;

import java.io.Serializable;

public class LookLiveData implements Serializable {
    StartLiveBean liveInfo;
    ProductBean liveProductInfo;

    public StartLiveBean getLiveInfo() {
        return liveInfo;
    }

    public void setLiveInfo(StartLiveBean liveInfo) {
        this.liveInfo = liveInfo;
    }

    public ProductBean getLiveProductInfo() {
        return liveProductInfo;
    }

    public void setLiveProductInfo(ProductBean liveProductInfo) {
        this.liveProductInfo = liveProductInfo;
    }
}
