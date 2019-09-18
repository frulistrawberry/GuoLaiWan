package com.guolaiwan.presenter;

import android.util.Log;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.BasePresenter;
import com.cgx.library.base.IBaseVIew;
import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.utils.AppUtils;
import com.cgx.library.utils.EncryptUtils;
import com.cgx.library.utils.StringUtils;
import com.cgx.library.utils.log.LogUtil;
import com.guolaiwan.aliapi.AliApi;
import com.guolaiwan.bean.ActivityEvent;
import com.guolaiwan.bean.AlipayBean;
import com.guolaiwan.bean.WeChatpayBean;
import com.guolaiwan.constant.KeyConstant;
import com.guolaiwan.model.PayModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.activity.ProfessionalLiveStartActivity;
import com.guolaiwan.utils.CommonUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.PublishSubject;


/**
 * 作者: 陈冠希
 * 日期: 2018/4/25
 * 描述:
 */

public class PayPresent extends BasePresenter<IBaseVIew>{

    private PayModel mModel;

    private IWXAPI mWXApi;
    private PublishSubject<LifeCycleEvent> mLifecycleSubject = PublishSubject.create();

    public PayPresent(IBaseVIew mIView) {
        super(mIView);
        mModel = new PayModel();
        if (this.mIView instanceof BaseActivity){
            mWXApi =  WXAPIFactory.createWXAPI((BaseActivity)this.mIView, KeyConstant.WX_APP_ID);
        }
    }

    public void payProduct(String productId,String addressId,String payType,int productNum){
        if (payType.equals("ALIPAY")){
            mModel.aliPayProduct(productId,addressId,productNum, new HttpObserver<AlipayBean>() {

                @Override
                public void onNext(String message, AlipayBean data) {
                    AliApi.getInstance().pay((BaseActivity) mIView,data.getOrderInfo());
                    EventBus.getDefault().post(data.getOrderId());
                }

                @Override
                public void onError(int errCode, String errMessage) {
                    CommonUtils.error(errCode,errMessage);
                }

                @Override
                public void onComplete() {
                }

            },mIView.getLifeSubject());
        }else if (payType.equals("WECHAT")){
            mModel.weChatPayProduct(productId, addressId,productNum,new HttpObserver<WeChatpayBean>() {
                @Override
                public void onNext(String message, WeChatpayBean data) {
                    PayReq req = new PayReq();
                    req.appId = data.getAppid();
                    req.partnerId = data.getPartnerid();
                    req.prepayId = data.getPrepayid();
                    req.nonceStr = data.getNoncestr();
                    req.timeStamp = data.getTimestamp();
                    req.packageValue = data.getPackageStr();
                    req.sign = data.getSign();
                    mWXApi.sendReq(req);
                    EventBus.getDefault().post(data.getOrderId());
                }

                @Override
                public void onError(int errCode, String errMessage) {
                    CommonUtils.error(errCode,errMessage);
                }

                @Override
                public void onComplete() {
                }
            },mIView.getLifeSubject());
        }
    }

    public void payAllOrders(List<Map<String,Object>> orders,String payType){
        if (payType.equals("ALIPAY")){
            mModel.alipayAllOrders(orders, new HttpObserver<String>() {
                @Override
                public void onNext(String message, String data) {
                    AliApi.getInstance().pay((BaseActivity) mIView,data);
                }

                @Override
                public void onError(int errCode, String errMessage) {
                    CommonUtils.error(errCode,errMessage);
                }

                @Override
                public void onComplete() {
                }
            },mIView.getLifeSubject());
        }else if (payType.equals("WECHAT")){
            mModel.weChatPayAllOrders(orders, new HttpObserver<WeChatpayBean>() {
                @Override
                public void onNext(String message, WeChatpayBean data) {
                    PayReq req = new PayReq();
                    req.appId = data.getAppid();
                    req.partnerId = data.getPartnerid();
                    req.prepayId = data.getPrepayid();
                    req.nonceStr = data.getNoncestr();
                    req.timeStamp = data.getTimestamp()+"";
                    req.packageValue = data.getPackageStr();
                    req.sign = data.getSign();
                    mWXApi.sendReq(req);
                }

                @Override
                public void onError(int errCode, String errMessage) {
                    CommonUtils.error(errCode,errMessage);
                }

                @Override
                public void onComplete() {
                }
            },mIView.getLifeSubject());
        }
    }

    public void payOrder(String orderId,String payType){
        if (payType.equals("ALIPAY")){
            mModel.alipayOrder(orderId, new HttpObserver<String>() {

                @Override
                public void onNext(String message, String data) {
                    AliApi.getInstance().pay((BaseActivity) mIView,data);
                }

                @Override
                public void onError(int errCode, String errMessage) {
                    CommonUtils.error(errCode,errMessage);
                }

                @Override
                public void onComplete() {
                }

            },mIView.getLifeSubject());
        }else if (payType.equals("WECHAT")){
            mModel.weChatPayOrder(orderId, new HttpObserver<WeChatpayBean>() {
                @Override
                public void onNext(String message, WeChatpayBean data) {
                    PayReq req = new PayReq();
                    req.appId = data.getAppid();
                    req.partnerId = data.getPartnerid();
                    req.prepayId = data.getPrepayid();
                    req.nonceStr = data.getNoncestr();
                    req.timeStamp = data.getTimestamp();
                    req.packageValue = data.getPackageStr();
                    req.sign = data.getSign();
                    mWXApi.sendReq(req);
                }

                @Override
                public void onError(int errCode, String errMessage) {
                    CommonUtils.error(errCode,errMessage);
                }

                @Override
                public void onComplete() {
                }
            },mIView.getLifeSubject());
        }
    }

    /*购物车结算:结算新增联系人*/
    public void payAllOrders(List<Map<String,Object>> orders,String payType,String addressId){
        if (payType.equals("ALIPAY")){
            mModel.alipayAllOrders(orders, new HttpObserver<String>() {
                @Override
                public void onNext(String message, String data) {
                    AliApi.getInstance().pay((BaseActivity) mIView,data);
                }

                @Override
                public void onError(int errCode, String errMessage) {
                    CommonUtils.error(errCode,errMessage);
                }

                @Override
                public void onComplete() {
                }
            },mIView.getLifeSubject());
        }else if (payType.equals("WECHAT")){
            //使用新增收货地址结算
            mModel.weChatPayAllOrders(addressId,orders, new HttpObserver<WeChatpayBean>() {
                @Override
                public void onNext(String message, WeChatpayBean data) {
                    PayReq req = new PayReq();
                    req.appId = data.getAppid();
                    req.partnerId = data.getPartnerid();
                    req.prepayId = data.getPrepayid();
                    req.nonceStr = data.getNoncestr();
                    req.timeStamp = data.getTimestamp()+"";
                    req.packageValue = data.getPackageStr();
                    req.sign = data.getSign();
                    mWXApi.sendReq(req);
                }

                @Override
                public void onError(int errCode, String errMessage) {
                    CommonUtils.error(errCode,errMessage);
                }

                @Override
                public void onComplete() {
                }
            },mIView.getLifeSubject());
        }
    }


    /*专业直播支付*/
    public void professionalLivePay(String orderId,String totalFee,String payType){
        //TODO 支付暂时不做，模拟支付成功后的逻辑
        if(payType.equals("ALIPAY")){
            mModel.professionalALiPay(orderId, totalFee, payType, new HttpObserver<String>() {
                @Override
                public void onNext(String message, String data) {
                    AliApi.getInstance().pay((BaseActivity) mIView,data);
                }

                @Override
                public void onError(int errCode, String errMessage) {
                    CommonUtils.error(errCode,errMessage);
                }

                @Override
                public void onComplete() {}
            },mIView.getLifeSubject());
        }

        if(payType.equals("WEICHAT")){
            mModel.professionalWechatPay(orderId,totalFee,payType,new HttpObserver<WeChatpayBean>(){

                @Override
                public void onNext(String message, WeChatpayBean data) {
                    PayReq req = new PayReq();
                    req.appId = data.getAppid();
                    req.partnerId = data.getPartnerid();
                    req.prepayId = data.getPrepayid();
                    req.nonceStr = data.getNoncestr();
                    req.timeStamp = data.getTimestamp()+"";
                    req.packageValue = data.getPackageStr();
                    req.sign = data.getSign();
                    mWXApi.sendReq(req);
                }

                @Override
                public void onError(int errCode, String errMessage) {
                    CommonUtils.error(errCode,errMessage);
                }

                @Override
                public void onComplete() {}
            },mIView.getLifeSubject());
        }
    }

    /*修改订单状态*/
    public void professionalLiveUpdateOrderState(String orderStatus){
        mIView.showLoadingDialog();
        String userId = CommonUtils.getUserId();
        mModel.professionalLiveUpdateOrderState(userId, orderStatus, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                mIView.dismissLoadingDialog();
                CommonUtils.showMsg(message);
                //关闭当前界面和申请界面
                ActivityEvent activityEvent = new ActivityEvent();
                activityEvent.action = "close";
                EventBus.getDefault().post(activityEvent);
                //打开开启直播界面
                ProfessionalLiveStartActivity.launch((BaseActivity)mIView);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                mIView.dismissLoadingDialog();
                CommonUtils.error(errCode,errMessage);
            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }
}
