package com.guolaiwan.utils;

import android.text.TextUtils;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.cgx.library.utils.AppUtils;
import com.cgx.library.utils.SPUtils;
import com.cgx.library.utils.StringUtils;
import com.cgx.library.utils.ToastUtils;
import com.guolaiwan.bean.DistributorBean;
import com.guolaiwan.bean.LoginBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.constant.PreferenceConstant;

import java.io.BufferedOutputStream;
import java.util.List;

import mabeijianxi.camera.util.Log;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/20
 * 描述:
 */

public class CommonUtils {
    public static void error(int status,String messgage){
        showMsg(messgage);
    }

    public static void showMsg(String msg){
        ToastUtils.showToast(AppUtils.getAppContext(),msg);
    }

    public static String calculateLineDistance(String shopLatitudeStr,String shopLongitudeStr){
        if (StringUtils.isEmpty(shopLatitudeStr)||StringUtils.isEmpty(shopLongitudeStr)){
            return "";
        }
        String distance = "";
        float latitude = SPUtils.getFloat(PreferenceConstant.LATITUDE);
        float longitude = SPUtils.getFloat(PreferenceConstant.LONGITUDE);
        if (latitude != -1 && longitude !=-1){
            float shopLatitude = Float.valueOf(shopLatitudeStr);
            float shopLongitude = Float.valueOf(shopLongitudeStr);
            LatLng latLng1 = new LatLng(shopLatitude,shopLongitude);
            LatLng latLng2 = new LatLng(latitude,longitude);
            float lineDistance = AMapUtils.calculateLineDistance(latLng1,latLng2);
            if (lineDistance > 1000){
                distance = StringUtils.getStringWithRound(String.valueOf(lineDistance / 1000)) + "km";
            }else {
                distance = StringUtils.getStringWithRound(String.valueOf(lineDistance)) + "m";
            }
        }else {
            distance = "";
        }

        return distance;
    }

    public static boolean isLogin(){
        return !StringUtils.isEmpty(getUserId());
    }

    public static String getUserId(){
        return SPUtils.getString("userId");
    }

    public static boolean isMerchant(){
        return SPUtils.getBoolean("isMerchant");
    }

    public static void  saveUserInfo(LoginBean login){
        SPUtils.putString("userId",login.getUserId());
        SPUtils.putString("phone",login.getPhone());
        SPUtils.putBoolean("isMerchant",login.isMerchant());
        SPUtils.putString("merchantId",login.getMerchantId());
    }

    public static String getPhone(){
        return  SPUtils.getString("phone");
    }

    public static String getMerchantId(){
        return SPUtils.getString("merchantId");
    }

    public static boolean isBindedPhone(){
        return !TextUtils.isEmpty(getPhone());
    }

    public static void clearUser(){
        SPUtils.clear();
    }


}
