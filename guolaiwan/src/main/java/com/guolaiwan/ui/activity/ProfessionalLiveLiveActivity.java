package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.widget.dialog.AlertDialog;
import com.github.faucamp.simplertmp.RtmpHandler;
import com.guolaiwan.bean.MessageBean;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.bean.ProfessionalLiveSubLiveBean;
import com.guolaiwan.constant.UrlConstant;
import com.guolaiwan.presenter.ProfessionalLiveLivePresenter;
import com.guolaiwan.ui.adapter.MessageAdapter;
import com.guolaiwan.ui.iview.ProfessionalLiveLiveView;
import com.guolaiwan.ui.widget.RefundPopup;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.EmojiUtil;
import com.seu.magicfilter.utils.MagicFilterType;

import net.ossrs.yasea.SrsCameraView;
import net.ossrs.yasea.SrsEncodeHandler;
import net.ossrs.yasea.SrsPublisher;
import net.ossrs.yasea.SrsRecordHandler;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;

public class ProfessionalLiveLiveActivity extends BaseActivity implements ProfessionalLiveLiveView, View.OnClickListener, SrsEncodeHandler.SrsEncodeListener, SrsRecordHandler.SrsRecordListener, RtmpHandler.RtmpListener, BaseActivity.OnKeyClickListener {

    //开始直播布局
    @BindView(R.id.rl_start_live)
    RelativeLayout mStartLiveRl;
    //开始直播
    @BindView(R.id.tv_start)
    TextView mStartTv;
    //编辑直播名称
    @BindView(R.id.et_live_name)
    EditText mLiveNameEt;
    //直播中布局
    @BindView(R.id.rl_living)
    RelativeLayout mLivingRl;
    //直播名称
    @BindView(R.id.tv_live_name)
    TextView mLiveNameTv;
    //直播控件
    @BindView(R.id.scv_live)
    SrsCameraView mCameraView;
    //评论显示控件
    @BindView(R.id.rv_comment)
    RecyclerView mCommentRv;
    //发送消息按钮
    @BindView(R.id.iv_send_message)
    ImageView mSendMessageIv;
    //切换摄像头按钮
    @BindView(R.id.iv_change_camera)
    ImageView mChangeCameraIv;
    //停止直播按钮
    @BindView(R.id.iv_stop_live)
    ImageView mStopLiveIv;
    //返回按钮
    @BindView(R.id.iv_back)
    ImageView mBackIv;
    //评论输入控件
    RefundPopup mCommentPop;

    //推流工具
    private SrsPublisher mPublisher;
    //评论输出控件Adapter
    private MessageAdapter mAdapter;

    private ProfessionalLiveSubLiveBean mProfessionalLiveSubLiveBean;
    private String mLiveId;
    private String mCameraNumber;

    private ProfessionalLiveLivePresenter mProfessionalLiveLivePresenter;



    public static void launch(Context context, ProfessionalLiveSubLiveBean professionalLiveSubLiveBean){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
            return;
        }
        Intent intent = new Intent(context, ProfessionalLiveLiveActivity.class);
        intent.putExtra("professionalLiveSubLiveBean",professionalLiveSubLiveBean);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_professional_live_live);
        setOnKeyListener(this);
        mProfessionalLiveSubLiveBean = (ProfessionalLiveSubLiveBean) getIntent().getSerializableExtra("professionalLiveSubLiveBean");
        mLiveId = mProfessionalLiveSubLiveBean.getLiveId();
        mCameraNumber = mProfessionalLiveSubLiveBean.getCameraNumber();
        //直播控件相关
        mPublisher = new SrsPublisher(mCameraView);
        //初始化直播插件
        //编码状态回调
        mPublisher.setEncodeHandler(new SrsEncodeHandler(this));
        mPublisher.setRecordHandler(new SrsRecordHandler(this));
        //rtmp推流状态回调
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        //预览分辨率
        mPublisher.setPreviewResolution(640,360);
        //推流分辨率
        mPublisher.setOutputResolution(640,360);
        //传输率：高清
        //mPublisher.setVideoHDMode();
        //传输率：流畅
        mPublisher.setVideoSmoothMode();
        //开启美颜
        mPublisher.switchCameraFilter(MagicFilterType.BEAUTY);
        //设置横屏
        mPublisher.setScreenOrientation(2);
        //打开摄像头
        mPublisher.startCamera();

        //初始化评论显示控件
        mCommentRv.setLayoutManager(new LinearLayoutManager(this));
        mCommentRv.setAdapter(mAdapter);
        //初始化评论输入控件
        initCommentPop();
    }

    @Override
    protected void initData() {
        mProfessionalLiveLivePresenter = new ProfessionalLiveLivePresenter(this);
        mAdapter = new MessageAdapter();
    }

    @Override
    protected void initEvent() {
        mStartTv.setOnClickListener(this);
        mSendMessageIv.setOnClickListener(this);
        mChangeCameraIv.setOnClickListener(this);
        mStopLiveIv.setOnClickListener(this);
        mBackIv.setOnClickListener(this);
    }

    /*初始化评论控件*/
    private void initCommentPop(){
        mCommentPop=new RefundPopup(this)
                .createPopup();
        mCommentPop.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCommentPop.isShowing()) {
                    mCommentPop.dismiss();
                }
            }
        });

        mCommentPop.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCommentPop.isShowing()) {
                    String commentMessage = mCommentPop.getContentEt().getText().toString().trim();
                    if (com.cgx.library.utils.StringUtils.isEmpty(commentMessage)){
                        CommonUtils.showMsg("请输入消息内容");
                        return;
                    }
                    String msgStr = EmojiUtil.emojiToString(commentMessage);
                    mProfessionalLiveLivePresenter.professionalLiveSendCommentMessage(msgStr,mLiveId);
                    mCommentPop.getContentEt().setText("");
                    mCommentPop.dismiss();
                }
            }
        });
        mCommentPop.setHinit("说点儿什么吧");
        mCommentPop.setOkText("发送");
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_start:
                String liveName = mLiveNameEt.getText().toString().trim();
                if (TextUtils.isEmpty(liveName)){
                    CommonUtils.showMsg("温馨提示:请输入直播名称");
                    return;
                }
                //调用机位开直播接口
                mProfessionalLiveLivePresenter.professionalLiveStartSubLive(mLiveId,mCameraNumber,liveName);
                break;

            case R.id.iv_send_message:
                mCommentPop.show(view);
                break;

            case R.id.iv_change_camera:
                mPublisher.switchCameraFace((mPublisher.getCamraId() + 1) % Camera.getNumberOfCameras());
                break;

            case R.id.iv_stop_live:
                //调用机位关闭直播接口
                mProfessionalLiveLivePresenter.professionalLiveStopSubLive(mLiveId,mSubLiveId);
                break;

            case R.id.iv_back:
                if(mLivingRl.getVisibility() == View.VISIBLE){
                    mProfessionalLiveLivePresenter.professionalLiveStopSubLive(mLiveId,mSubLiveId);
                }else {
                    finish();
                }
                break;

        }
    }

    private String mSubLiveId;
    private String mPubName;
    private String mLiveName;
    @Override
    public void setProfessionalLiveDate(ProfessionalLiveSubLiveBean professionalLiveSubLiveBean) {
        mSubLiveId = professionalLiveSubLiveBean.getId();
        mLiveName = professionalLiveSubLiveBean.getLiveName();
        mPubName = professionalLiveSubLiveBean.getPubName();

        mLiveNameTv.setText(mLiveName);
        mStartLiveRl.setVisibility(View.GONE);
        mLivingRl.setVisibility(View.VISIBLE);
        //直播相关
        String rtmpUrl = UrlConstant.RTMP_HOST + mPubName;
        //选择硬编码
        mPublisher.switchToHardEncoder();
        //开始推流
        mPublisher.startPublish(rtmpUrl);
        mPublisher.startCamera();
    }

    @Override
    public void refreshCommentMessage(MessageBean messageBean) {
        runOnUiThread(() -> {
            List<MessageBean> data = new ArrayList<>();
            if (CollectionUtils.isEmpty(mAdapter.getData())){
                data.add(messageBean);
                mAdapter.setNewData(data);
            }else{
                mAdapter.addData(messageBean);
            }
            mCommentRv.smoothScrollToPosition(mAdapter.getData().size() - 1);
            mAdapter.notifyDataSetChanged();
        });
    }

    /*返回键退出*/
    @Override
    public void clickBack() {
        if(mLivingRl.getVisibility() == View.VISIBLE){
            mProfessionalLiveLivePresenter.professionalLiveStopSubLive(mLiveId,mSubLiveId);
        }else {
            finish();
        }
    }

    /*Activity声明周期方法*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProfessionalLiveLivePresenter.destroy();
        if(mLivingRl.getVisibility() == View.VISIBLE){
            mPublisher.stopPublish();
        }
    }

    /*以下三个方法时编码回调*/
    @Override
    public void onNetworkWeak() {

    }

    @Override
    public void onNetworkResume() {

    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {

    }

    /*以下六个方法为录制回调*/
    @Override
    public void onRecordPause() {

    }

    @Override
    public void onRecordResume() {

    }

    @Override
    public void onRecordStarted(String msg) {

    }

    @Override
    public void onRecordFinished(String msg) {

    }

    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {

    }

    @Override
    public void onRecordIOException(IOException e) {

    }

    /*以下方法为推流状态回调*/
    @Override
    public void onRtmpConnecting(String msg) {

    }

    @Override
    public void onRtmpConnected(String msg) {

    }

    @Override
    public void onRtmpVideoStreaming() {

    }

    @Override
    public void onRtmpAudioStreaming() {

    }

    @Override
    public void onRtmpStopped() {

    }

    @Override
    public void onRtmpDisconnected() {

    }

    @Override
    public void onRtmpVideoFpsChanged(double fps) {

    }

    @Override
    public void onRtmpVideoBitrateChanged(double bitrate) {

    }

    @Override
    public void onRtmpAudioBitrateChanged(double bitrate) {

    }

    @Override
    public void onRtmpSocketException(SocketException e) {

    }

    @Override
    public void onRtmpIOException(IOException e) {

    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {

    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
