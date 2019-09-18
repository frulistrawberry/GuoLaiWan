package com.guolaiwan.presenter;

import android.util.Log;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.JsonValidator;
import com.cgx.library.utils.log.LogUtil;
import com.google.gson.Gson;
import com.guolaiwan.bean.MessageBean;
import com.guolaiwan.bean.ProfessionalLiveWatcherCheckLiveStateBean;
import com.guolaiwan.bean.SocketBean;
import com.guolaiwan.constant.UrlConstant;
import com.guolaiwan.model.ProfessionalLivePlayerModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.ProfessionalLivePlayerView;
import com.guolaiwan.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ProfessionalLivePlayerPresenter extends BasePresenter<ProfessionalLivePlayerView> {

    private ProfessionalLivePlayerModel mModel;
    private WebSocketClient mSocketClient;
    public ProfessionalLivePlayerPresenter(ProfessionalLivePlayerView mIView) {
        super(mIView);
        mModel = new ProfessionalLivePlayerModel();
        EventBus.getDefault().register(this);
    }


    /*专业直播：观看用户获取当前主播机位URL*/
    public void professionalLiveWatcherGetLiveState(String liveId){
        mModel.professionalLiveWatcherGetLiveState(liveId, new HttpObserver<ProfessionalLiveWatcherCheckLiveStateBean>() {
            @Override
            public void onNext(String message, ProfessionalLiveWatcherCheckLiveStateBean data) {
                String liveName = data.getLiveName();
                String vedioUrl = data.getVedioUrl();
                mIView.setVedioUrl(liveName,vedioUrl);
                initSocket(liveId);
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.error(errCode,errMessage);
                mIView.showsNoSignalNotice();
            }

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
        if(mSocketClient == null){
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
