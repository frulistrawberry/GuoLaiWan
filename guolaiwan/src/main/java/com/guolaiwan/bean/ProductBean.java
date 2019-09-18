package com.guolaiwan.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.guolaiwan.constant.ItemType;

import java.io.Serializable;
import java.util.Date;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/11
 * 描述: 商品
 */

public class ProductBean implements Serializable,MultiItemEntity {

    private int id;
    private String uuid;
    private String updateTime;
    private String productModularCode;
    private String productModularCodeName;
    private String productClassCode;
    private String productClassName;
    private int productMerchantID;
    private String productMerchantName;
    private String productName;
    private String productSubtitle;
    private String productBeginDate;
    private String productEnddate;
    private String nowDate;
    private String productEctivedate;
    private float productOldPrice;
    private float productPrice;
    private String productOldPriceStr;
    private String productPricesStr;
    private String productCommissionPriceStr;
    private String productShowPic;
    private String productMorePic;
    private int productIndexRecommend;
    private int productListRecommend;
    private int productShowNum;
    private int productSaleNum;
    private int productSort;
    private String productAuditstatus;
    private String productAuditAdvice;
    private String productMerchantJson;
    private int productStock;
    private int productLimitType;
    private int productLimitNum;
    private String productIntroduce;
    private int productntegral;
    private int productIsShow;
    private String productCityCode;
    private String productCityName;
    private int merMId;
    private String merMName;
    private int comId;
    private String comName;
    private String psType;
    private int goldNum;
    private int sent;
    private String shopLongitude;
    private String shopLatitude;
    private String productScore;
    private int productType;
    private int ifcollection;
    private boolean isCheck;
    private String auctionId;
    private String dealPrice;
    private boolean flag;
    private String liveId;
    private String liveProductType;
    private boolean locked;
    private int merchantId;
    private String merchantName;
    private boolean paid;
    private int price;
    private int productId;
    private String headPic;
    private String productIsDel;
    private String userId;
    private String orderId;
    private String activityName;
    private String activityId;
    private boolean isYiYuanGou;
    private String cutDownTimeStartDate;
    private String cutDownTimeEndDate;


    public String getCutDownTimeStartDate() {
        return cutDownTimeStartDate;
    }

    public void setCutDownTimeStartDate(String cutDownTimeStartDate) {
        this.cutDownTimeStartDate = cutDownTimeStartDate;
    }

    public String getCutDownTimeEndDate() {
        return cutDownTimeEndDate;
    }

    public void setCutDownTimeEndDate(String cutDownTimeEndDate) {
        this.cutDownTimeEndDate = cutDownTimeEndDate;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public boolean isYiYuanGou() {
        return isYiYuanGou;
    }

    public void setYiYuanGou(boolean yiYuanGou) {
        isYiYuanGou = yiYuanGou;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductIsDel() {
        return productIsDel;
    }

    public void setProductIsDel(String productIsDel) {
        this.productIsDel = productIsDel;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(String dealPrice) {
        this.dealPrice = dealPrice;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getLiveProductType() {
        return liveProductType;
    }

    public void setLiveProductType(String liveProductType) {
        this.liveProductType = liveProductType;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public int getPrice() {
        return price;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getIfcollection() {
        return ifcollection;
    }

    public void setIfcollection(int ifcollection) {
        this.ifcollection = ifcollection;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getProductScore() {
        return productScore;
    }

    public void setProductScore(String productScore) {
        this.productScore = productScore;
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

    private boolean isRec;

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

    public String getProductModularCode() {
        return productModularCode;
    }

    public void setProductModularCode(String productModularCode) {
        this.productModularCode = productModularCode;
    }

    public String getProductModularCodeName() {
        return productModularCodeName;
    }

    public void setProductModularCodeName(String productModularCodeName) {
        this.productModularCodeName = productModularCodeName;
    }

    public String getProductClassCode() {
        return productClassCode;
    }

    public void setProductClassCode(String productClassCode) {
        this.productClassCode = productClassCode;
    }

    public String getProductClassName() {
        return productClassName;
    }

    public void setProductClassName(String productClassName) {
        this.productClassName = productClassName;
    }

    public int getProductMerchantID() {
        return productMerchantID;
    }

    public void setProductMerchantID(int productMerchantID) {
        this.productMerchantID = productMerchantID;
    }

    public String getProductMerchantName() {
        return productMerchantName;
    }

    public void setProductMerchantName(String productMerchantName) {
        this.productMerchantName = productMerchantName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSubtitle() {
        return productSubtitle;
    }

    public void setProductSubtitle(String productSubtitle) {
        this.productSubtitle = productSubtitle;
    }

    public String getProductBeginDate() {
        return productBeginDate;
    }

    public void setProductBeginDate(String productBeginDate) {
        this.productBeginDate = productBeginDate;
    }

    public String getProductEnddate() {
        return productEnddate;
    }

    public void setProductEnddate(String productEnddate) {
        this.productEnddate = productEnddate;
    }

    public String getProductEctivedate() {
        return productEctivedate;
    }

    public void setProductEctivedate(String productEctivedate) {
        this.productEctivedate = productEctivedate;
    }

    public float getProductOldPrice() {
        return productOldPrice;
    }

    public void setProductOldPrice(float productOldPrice) {
        this.productOldPrice = productOldPrice;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductOldPriceStr() {
        return productOldPriceStr;
    }

    public void setProductOldPriceStr(String productOldPriceStr) {
        this.productOldPriceStr = productOldPriceStr;
    }

    public String getProductPricesStr() {
        return productPricesStr;
    }

    public void setProductPricesStr(String productPricesStr) {
        this.productPricesStr = productPricesStr;
    }

    public String getProductCommissionPriceStr() {
        return productCommissionPriceStr;
    }

    public void setProductCommissionPriceStr(String productCommissionPriceStr) {
        this.productCommissionPriceStr = productCommissionPriceStr;
    }

    public String getProductShowPic() {
        return productShowPic;
    }

    public void setProductShowPic(String productShowPic) {
        this.productShowPic = productShowPic;
    }

    public String getProductMorePic() {
        return productMorePic;
    }

    public void setProductMorePic(String productMorePic) {
        this.productMorePic = productMorePic;
    }

    public int getProductIndexRecommend() {
        return productIndexRecommend;
    }

    public void setProductIndexRecommend(int productIndexRecommend) {
        this.productIndexRecommend = productIndexRecommend;
    }

    public int getProductListRecommend() {
        return productListRecommend;
    }

    public void setProductListRecommend(int productListRecommend) {
        this.productListRecommend = productListRecommend;
    }

    public int getProductShowNum() {
        return productShowNum;
    }

    public void setProductShowNum(int productShowNum) {
        this.productShowNum = productShowNum;
    }

    public int getProductSaleNum() {
        return productSaleNum;
    }

    public void setProductSaleNum(int productSaleNum) {
        this.productSaleNum = productSaleNum;
    }

    public int getProductSort() {
        return productSort;
    }

    public void setProductSort(int productSort) {
        this.productSort = productSort;
    }

    public String getProductAuditstatus() {
        return productAuditstatus;
    }

    public void setProductAuditstatus(String productAuditstatus) {
        this.productAuditstatus = productAuditstatus;
    }

    public String getProductAuditAdvice() {
        return productAuditAdvice;
    }

    public void setProductAuditAdvice(String productAuditAdvice) {
        this.productAuditAdvice = productAuditAdvice;
    }

    public String getProductMerchantJson() {
        return productMerchantJson;
    }

    public void setProductMerchantJson(String productMerchantJson) {
        this.productMerchantJson = productMerchantJson;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public int getProductLimitType() {
        return productLimitType;
    }

    public void setProductLimitType(int productLimitType) {
        this.productLimitType = productLimitType;
    }

    public int getProductLimitNum() {
        return productLimitNum;
    }

    public void setProductLimitNum(int productLimitNum) {
        this.productLimitNum = productLimitNum;
    }

    public String getProductIntroduce() {
        return productIntroduce;
    }

    public void setProductIntroduce(String productIntroduce) {
        this.productIntroduce = productIntroduce;
    }

    public int getProductntegral() {
        return productntegral;
    }

    public void setProductntegral(int productntegral) {
        this.productntegral = productntegral;
    }

    public int getProductIsShow() {
        return productIsShow;
    }

    public void setProductIsShow(int productIsShow) {
        this.productIsShow = productIsShow;
    }

    public String getProductCityCode() {
        return productCityCode;
    }

    public void setProductCityCode(String productCityCode) {
        this.productCityCode = productCityCode;
    }

    public String getProductCityName() {
        return productCityName;
    }

    public void setProductCityName(String productCityName) {
        this.productCityName = productCityName;
    }

    public int getMerMId() {
        return merMId;
    }

    public void setMerMId(int merMId) {
        this.merMId = merMId;
    }

    public String getMerMName() {
        return merMName;
    }

    public void setMerMName(String merMName) {
        this.merMName = merMName;
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

    public String getPsType() {
        return psType;
    }

    public void setPsType(String psType) {
        this.psType = psType;
    }

    public int getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(int goldNum) {
        this.goldNum = goldNum;
    }

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    @Override
    public int getItemType() {
        return isRec? ItemType.ITEM_TYPE_PRODUCT_REC:ItemType.ITEM_TYPE_PRODUCT;
    }
}
