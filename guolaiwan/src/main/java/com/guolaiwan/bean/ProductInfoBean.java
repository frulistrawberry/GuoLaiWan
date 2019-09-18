package com.guolaiwan.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/15/015.
 */

public class ProductInfoBean {
    private ProductBean product;
    private List<CommentBean> comments;
    private int commentCount;
    private String shopIntroduction;

    public String getShopIntroduction() {
        return shopIntroduction;
    }

    public void setShopIntroduction(String shopIntroduction) {
        this.shopIntroduction = shopIntroduction;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public List<CommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
