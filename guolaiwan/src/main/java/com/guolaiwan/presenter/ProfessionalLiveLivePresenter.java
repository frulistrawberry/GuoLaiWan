package com.guolaiwan.presenter;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.BasePresenter;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.JsonValidator;
import com.cgx.library.utils.StringUtils;
import com.cgx.library.utils.log.LogUtil;
import com.google.gson.Gson;
import com.guolaiwan.bean.AuctionBean;
import com.guolaiwan.bean.MessageBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.ProfessionalLiveSubLiveBean;
import com.guolaiwan.bean.SocketBean;
import com.guolaiwan.constant.UrlConstant;
import com.guolaiwan.model.ProfessionalLiveCameraChoiceModel;
import com.guolaiwan.model.ProfessionalLiveLiveModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.ProfessionalLiveCameraChoiceView;
import com.guolaiwan.ui.iview.ProfessionalLiveLiveView;
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

import io.reactivex.annotations.NonNull;

public class ProfessionalLiveLivePresenter extends BasePresenter<ProfessionalLiveLiveView> {

    private ProfessionalLiveLiveModel mModel;
    private WebSocketClient mSocketClient;

    public ProfessionalLiveLivePresenter(ProfessionalLiveLiveView mIView) {
        super(mIView);
        mModel = new ProfessionalLiveLiveModel();
        EventBus.getDefault().register(this);
    }

    /*专业直播:直播机位开始直播*/
    public void professionalLiveStartSubLive(String liveId,String cameraNumber,String liveName){
        mIView.dismissLoadingDialog();
        mModel.professionalLiveStartSubLive(liveId, cameraNumber,liveName,new HttpObserver<ProfessionalLiveSubLiveBean>() {
            @Override
            public void onNext(String message, ProfessionalLiveSubLiveBean data) {
                mIView.dismissLoadingDialog();
                mIView.setProfessionalLiveDate(data);
                initSocket(data.getLiveId());
                professionalLiveStartSubLiveService(data.getLiveId(),data.getId());
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

    /*专业直播:机位停止直播*/
    public void professionalLiveStopSubLive(String liveId,String subLiveId){
        mIView.dismissLoadingDialog();
        mModel.professionalLiveStopSubLive(liveId, subLiveId, new HttpObserver<String>() {
            @Override
            public void onNext(String message, String data) {
                mIView.dismissLoadingDialog();
                if(data.equals("YES")){
                    professionalLiveStopSubLiveService(liveId,subLiveId);
                    ((BaseActivity)mIView).finish();
                }else {
                    CommonUtils.showMsg("温馨提示:当前直播不可关闭");
                }
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

    /*专业直播:直播机位开始直播服务*/
    public void professionalLiveStartSubLiveService(String liveId,String subLiveId){
        mModel.professionalLiveStartSubLiveService(liveId, subLiveId, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {}

            @Override
            public void onError(int errCode, String errMessage) {}

            @Override
            public void onComplete() {}
        },mIView.getLifeSubject());
    }

    /*专业直播:直播机位停止直播服务*/
    public void professionalLiveStopSubLiveService(String liveId,String subLiveId){
        mModel.professionalLiveStopSubLiveService(liveId, subLiveId, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {}

            @Override
            public void onError(int errCode, String errMessage) {}

            @Override
            public void onComplete() {}
        },mIView.getLifeSubject());
    }

    /*专业直播:发表评论*/
    public void professionalLiveSendCommentMessage(String commentMessage,String liveId){
        mIView.showLoadingDialog();
        mModel.professionLiveSendMessage(commentMessage,liveId,new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
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

    /*专业直播websocket请求*/
    private void initSocket(final String liveId) {
        new Thread(() -> {
            try {
                mSocketClient = new WebSocketClient(new URI(UrlConstant.PROFESSIONAL_LIVE_SOCKET_HOST + liveId), new Draft_6455()) {

                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        LogUtil.i("ProfessionalLiveWebSocketLog", "打开通道" + handshakedata.getHttpStatus());
                    }

                    @Override
                    public void onMessage(String message) {
                        LogUtil.i("ProfessionalLiveWebSocketLog", "接收消息");
                        if (new JsonValidator().validate(message)){
                            LogUtil.json("ProfessionalLiveWebSocketLog", message);
                        }else {
                            LogUtil.i("ProfessionalLiveWebSocketLog", message);
                        }
                        EventBus.getDefault().post(message);
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        LogUtil.i("ProfessionalLiveWebSocketLog", "通道关闭");
                        LogUtil.i("ProfessionalLiveWebSocketLog", "code="+code);
                        LogUtil.i("ProfessionalLiveWebSocketLog", "reason="+reason);
                        LogUtil.i("ProfessionalLiveWebSocketLog", "remote="+remote);
                    }

                    @Override
                    public void onError(Exception ex) {
                        LogUtil.i("ProfessionalLiveWebSocketLog", "链接错误");
                        mSocketClient.connect();
                    }
                };
                mSocketClient.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /*评论消息刷新*/
    @Subscribe
    public void onEventMainThread(String commentMessage){
        if (new JsonValidator().validate(commentMessage)){
            SocketBean data = new Gson().fromJson(commentMessage,SocketBean.class);
            if (data !=null){
                List<MessageBean> messages = data.getLiveMessages();
                if (!CollectionUtils.isEmpty(messages)){
                    for (MessageBean messageBean : messages) {
                        mIView.refreshCommentMessage(messageBean);
                    }
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
