package com.guolaiwan.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/13/013.
 */

public class SocketBean {
    List<MessageBean> liveMessages;
    List<AuctionBean> auctions;
    List<ProductBean> liveProducts;
    ProductBean CJ;
    String PL;

    public String getPL() {
        return PL;
    }

    public void setPL(String PL) {
        this.PL = PL;
    }

    public ProductBean getCJ() {
        return CJ;
    }

    public void setCJ(ProductBean CJ) {
        this.CJ = CJ;
    }

    public List<MessageBean> getLiveMessages() {
        return liveMessages;
    }

    public void setLiveMessages(List<MessageBean> liveMessages) {
        this.liveMessages = liveMessages;
    }

    public List<AuctionBean> getAuctions() {
        return auctions;
    }

    public void setAuctions(List<AuctionBean> auctions) {
        this.auctions = auctions;
    }

    public List<ProductBean> getLiveProducts() {
        return liveProducts;
    }

    public void setLiveProducts(List<ProductBean> liveProducts) {
        this.liveProducts = liveProducts;
    }
}
