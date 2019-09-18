package com.guolaiwan.bean;

import java.io.Serializable;



public class LoginBean implements Serializable {
    private String merchant;
    private String phone;
    private String userId;
    private String merchantId;


    public boolean isMerchant() {
        return merchant.equals("yes");
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchantId() {
        return merchantId;
    }
}
