package com.guolaiwan.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.guolaiwan.constant.ItemType;

import java.io.Serializable;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/11
 * 描述:
 */

public class MerchantBean implements Serializable,MultiItemEntity {
    private int id;
    private String uuid;
    private String updateTime;
    private String shopName;
    private String shopLoginName;
    private String shopLoginPwd;
    private String shopHeading;
    private String shopQualifications;
    private String shopQQ;
    private String shopTel;
    private String shopAddress;
    private String shopBankId;
    private String shopOpenBank;
    private String shopLinkperson;
    private String shopPic;
    private String shopMpic;
    private String shopIntroduction;
    private String shopLongitude;
    private String shopLatitude;
    private String shopAllMoney;
    private String shopActualMoney;
    private String shopAuditState;
    private String shopAuditopinion;
    private String modularName;
    private String modularCode;
    private String modularClass;
    private String modularClassId;
    private String cityCode;
    private String cityName;
    private int productCount;
    private int comId;
    private String comName;
    private int shopScore;
    private String averagePrice;
    private boolean isRec;
    private String isGuide;




    public String getIsGuide() {
        return isGuide;
    }

    public void setIsGuide(String isGuide) {
        this.isGuide = isGuide;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }

    public int getShopScore() {
        return shopScore;
    }

    public void setShopScore(int shopScore) {
        this.shopScore = shopScore;
    }

    public boolean isRec() {
        return isRec;
    }

    public void setRec(boolean rec) {
        isRec = rec;
    }

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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopLoginName() {
        return shopLoginName;
    }

    public void setShopLoginName(String shopLoginName) {
        this.shopLoginName = shopLoginName;
    }

    public String getShopLoginPwd() {
        return shopLoginPwd;
    }

    public void setShopLoginPwd(String shopLoginPwd) {
        this.shopLoginPwd = shopLoginPwd;
    }

    public String getShopHeading() {
        return shopHeading;
    }

    public void setShopHeading(String shopHeading) {
        this.shopHeading = shopHeading;
    }

    public String getShopQualifications() {
        return shopQualifications;
    }

    public void setShopQualifications(String shopQualifications) {
        this.shopQualifications = shopQualifications;
    }

    public String getShopQQ() {
        return shopQQ;
    }

    public void setShopQQ(String shopQQ) {
        this.shopQQ = shopQQ;
    }

    public String getShopTel() {
        return shopTel;
    }

    public void setShopTel(String shopTel) {
        this.shopTel = shopTel;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopBankId() {
        return shopBankId;
    }

    public void setShopBankId(String shopBankId) {
        this.shopBankId = shopBankId;
    }

    public String getShopOpenBank() {
        return shopOpenBank;
    }

    public void setShopOpenBank(String shopOpenBank) {
        this.shopOpenBank = shopOpenBank;
    }

    public String getShopLinkperson() {
        return shopLinkperson;
    }

    public void setShopLinkperson(String shopLinkperson) {
        this.shopLinkperson = shopLinkperson;
    }

    public String getShopPic() {
        return shopPic;
    }

    public void setShopPic(String shopPic) {
        this.shopPic = shopPic;
    }

    public String getShopMpic() {
        return shopMpic;
    }

    public void setShopMpic(String shopMpic) {
        this.shopMpic = shopMpic;
    }

    public String getShopIntroduction() {
        return shopIntroduction;
    }

    public void setShopIntroduction(String shopIntroduction) {
        this.shopIntroduction = shopIntroduction;
    }

    public String getShopLongitude() {
        return shopLongitude;
    }

    public void setShopLongitude(String shopLongitude) {
        this.shopLongitude = shopLongitude;
    }

    public String getShopLatitude() {
        return shopLatitude;
    }

    public void setShopLatitude(String shopLatitude) {
        this.shopLatitude = shopLatitude;
    }

    public String getShopAllMoney() {
        return shopAllMoney;
    }

    public void setShopAllMoney(String shopAllMoney) {
        this.shopAllMoney = shopAllMoney;
    }

    public String getShopActualMoney() {
        return shopActualMoney;
    }

    public void setShopActualMoney(String shopActualMoney) {
        this.shopActualMoney = shopActualMoney;
    }

    public String getShopAuditState() {
        return shopAuditState;
    }

    public void setShopAuditState(String shopAuditState) {
        this.shopAuditState = shopAuditState;
    }

    public String getShopAuditopinion() {
        return shopAuditopinion;
    }

    public void setShopAuditopinion(String shopAuditopinion) {
        this.shopAuditopinion = shopAuditopinion;
    }

    public String getModularName() {
        return modularName;
    }

    public void setModularName(String modularName) {
        this.modularName = modularName;
    }

    public String getModularCode() {
        return modularCode;
    }

    public void setModularCode(String modularCode) {
        this.modularCode = modularCode;
    }

    public String getModularClass() {
        return modularClass;
    }

    public void setModularClass(String modularClass) {
        this.modularClass = modularClass;
    }

    public String getModularClassId() {
        return modularClassId;
    }

    public void setModularClassId(String modularClassId) {
        this.modularClassId = modularClassId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
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

    @Override
    public int getItemType() {
        return isRec? ItemType.ITEM_TYPE_MERCHANT_REC:ItemType.ITEM_TYPE_MERCHANT;
    }
}
