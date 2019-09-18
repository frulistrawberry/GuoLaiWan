package com.guolaiwan.presenter;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.BasePresenter;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.JsonValidator;
import com.cgx.library.utils.StringUtils;
import com.cgx.library.utils.log.LogUtil;
import com.google.gson.Gson;
import com.guolaiwan.bean.AuctionBean;
import com.guolaiwan.bean.LookLiveData;
import com.guolaiwan.bean.MessageBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.SocketBean;
import com.guolaiwan.bean.StartLiveBean;
import com.guolaiwan.constant.UrlConstant;
import com.guolaiwan.model.CameraModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.CameraView;
import com.guolaiwan.utils.CommonUtils;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CameraPresenter extends BasePresenter<CameraView>{

    CameraModel mModel;
    private WebSocketClient mSocketClient;


    public CameraPresenter(CameraView mIView) {
        super(mIView);
        mModel = new CameraModel();
        EventBus.getDefault().register(this);
    }

    public void startLive(String liveName){
        mModel.startLive(liveName,new HttpObserver<LookLiveData>() {
            @Override
            public void onNext(String message, LookLiveData data) {
                initSocket(data.getLiveInfo().getId());
                mIView.setLiveData(data);
            }

            @Override
            public void onError(int errCode, String errMessage) {}

            @Override
            public void onComplete() {}
        },mIView.getLifeSubject());
    }

    public void addProduct(String productId,String liveId,String price){
        mIView.showLoadingDialog();
        mModel.addProduct(CommonUtils.getMerchantId(), liveId, productId, price, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
//                Map<String,String> params = new HashMap<>();
//                params.put("key","live-"+liveId+"-"+CommonUtils.getUserId()+"-A");
                mSocketClient.send("A");
            }

            @Override
            public void onError(int errCode, String errMessage) {

            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    public void addMessage(String message,String liveId){
        mIView.showLoadingDialog();
        mModel.sendMessage(message, liveId, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                Map<String,String> params = new HashMap<>();
                params.put("key","live-" + liveId + "-" + CommonUtils.getUserId() + "-M");
                LogUtil.d("KEY","live-"+liveId + "-" + CommonUtils.getUserId()+"-M");
                mSocketClient.send("M");
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

    public void stopLive(){
        mModel.stopLive(new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {}

            @Override
            public void onError(int errCode, String errMessage) {}

            @Override
            public void onComplete() {
                ((BaseActivity)mIView).finish();
            }
        },mIView.getLifeSubject());
    }

    public void confirm(String productId,String liveId){
        mIView.showLoadingDialog();
        mModel.confirmAuctionPrice(productId, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
                Map<String,String> params = new HashMap<>();
                params.put("key","live-"+liveId+"-"+CommonUtils.getUserId()+"-A");
                mSocketClient.send("A");
            }

            @Override
            public void onError(int errCode, String errMessage) {

            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    public void delete(String productId,String liveId){
        mIView.showLoadingDialog();
        mModel.deleteProduct(productId,liveId, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
                Map<String,String> params = new HashMap<>();
                params.put("key","live-"+liveId+"-"+CommonUtils.getUserId()+"-A");
                mSocketClient.send("A");
            }

            @Override
            public void onError(int errCode, String errMessage) {

            }

            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    private void initSocket(final String liveId) {
        new Thread(() -> {
            try {
                mSocketClient = new WebSocketClient(new URI(UrlConstant.SOCKET_HOST + liveId), new Draft_6455()) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        LogUtil.d("WebSocketLog", "打开通道" + handshakedata.getHttpStatus());
                    }

                    @Override
                    public void onMessage(String message) {
                        LogUtil.d("WebSocketLog", "接收消息");
                        if (new JsonValidator().validate(message))
                            LogUtil.json("WebSocketLog", message);
                        else
                            LogUtil.d("WebSocketLog", message);
                        EventBus.getDefault().post(message);
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        LogUtil.d("WebSocketLog", "通道关闭");
                        LogUtil.d("WebSocketLog", "code="+code);
                        LogUtil.d("WebSocketLog", "reason="+reason);
                        LogUtil.d("WebSocketLog", "remote="+remote);
                    }

                    @Override
                    public void onError(Exception ex) {
                        LogUtil.d("WebSocketLog", "链接错误");
                        mSocketClient.connect();
                    }
                };
                mSocketClient.connect();

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void getLiveInfo(String liveId){
        mIView.showLoadingDialog();
        mModel.getLiveInfo(liveId, new HttpObserver<LookLiveData>() {
            @Override
            public void onNext(String message, LookLiveData data) {
                StartLiveBean liveData = data.getLiveInfo();
                initSocket(liveData.getId());
                mIView.setLookLiveData(data);
            }

            @Override
            public void onError(int errCode, String errMessage) {

            }


            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();

            }
        },mIView.getLifeSubject());
    }

    public void sendPrice(String liveId,String productId,String price,String addressId){
        mIView.showLoadingDialog();
        mModel.sendPrice(liveId, productId, price,addressId, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
                CommonUtils.showMsg(message);
                Map<String,String> params = new HashMap<>();
                params.put("key", "live-" + liveId + "-" + CommonUtils.getUserId() + "-P");
                mSocketClient.send("P");
            }

            @Override
            public void onError(int errCode, String errMessage) {}


            @Override
            public void onComplete() {
                mIView.dismissLoadingDialog();
            }
        },mIView.getLifeSubject());
    }

    @Subscribe
    public void onEventMainThread(String message){
        if (new JsonValidator().validate(message)){
            SocketBean data = new Gson().fromJson(message,SocketBean.class);
            if (data !=null){
                boolean isDel = !StringUtils.isEmpty(data.getPL())&&data.getPL().equals("404");
                if (isDel){
                    mIView.delePruduct();
                    return;
                }
                List<MessageBean> messages = data.getLiveMessages();
                if (!CollectionUtils.isEmpty(messages)){
                    for (MessageBean messageBean : messages) {
                        mIView.addMessage(messageBean);
                    }
                }
                if (!CollectionUtils.isEmpty(data.getLiveProducts())){
                    for (ProductBean productBean : data.getLiveProducts()) {
                        mIView.showProduct(productBean);
                    }
                }

                if (!CollectionUtils.isEmpty(data.getAuctions())){
                    for (AuctionBean auctionBean : data.getAuctions()) {
                        mIView.changePrice(auctionBean.getPrice());
                    }
                }
                if (data.getCJ()!=null){
                    mIView.showProduct(data.getCJ());
                }


            }
        }
    }

    @Override
    public void destroy()  {
        if (mSocketClient != null){
            try {
                mSocketClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EventBus.getDefault().unregister(this);
        super.destroy();
    }
}