package com.guolaiwan.ui.widget;

import android.content.Context;
import android.graphics.Point;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgx.library.utils.ToastUtils;
import com.guolaiwan.ui.activity.ProfessionalLiveDirectorActivity;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoViewBridge;
import app.guolaiwan.com.guolaiwan.R;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/2/24.
 * 说明:  导播多视频播放播放器
 */
public class MultiSampleVideo extends StandardGSYVideoPlayer{

    private final static String TAG = "MultiSampleVideo";

    //标题
    private TextView mLiveTitleTv;
    //信号机位标识
    private TextView mSignalCameraFlagTv;
    //信号加载按钮
    private RelativeLayout mLoadSignalRl;
    private ImageView mLoadOrAddVideoIv;
    private TextView mLoadOrAddVideoTv;
    //信号加载界面机位名称
    private TextView mCameraNameInSignalLoadingTv;
    //声音按钮
    private ImageView mVoiceIv;
    //录制按钮
    private ImageView mRecordIv;
    //设为信号流按钮
    private ImageView mGoToShowIv;

    private Context mContext;
    private String mFullKey;
    private ProfessionalLiveDirectorActivity mActivity;


    public MultiSampleVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MultiSampleVideo(Context context) {
        super(context);
    }

    public MultiSampleVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        this.mContext = context;
        mLoadSignalRl = findViewById(R.id.rl_load_signal);
        mLoadOrAddVideoIv = findViewById(R.id.iv_load_or_add_video);
        mLoadOrAddVideoTv = findViewById(R.id.tv_load_or_add_video);
        mCameraNameInSignalLoadingTv = findViewById(R.id.tv_camera_name);
        mSignalCameraFlagTv = findViewById(R.id.tv_signal_camera);
        mLiveTitleTv = findViewById(R.id.tv_live_name);
        mVoiceIv = findViewById(R.id.iv_voice);
        mRecordIv = findViewById(R.id.iv_record);
        mGoToShowIv = findViewById(R.id.iv_goto_show);

        //进入全屏时控件状态设置
//        if(mIfCurrentIsFullscreen == true){
//            mLoadSignalRl.setVisibility(View.GONE);
//        }
        //初始化监听
        mLoadSignalRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mActivity != null){
                    switch (mType){
                        case "camera":
                            mActivity.getPresenter().professionalLiveDirectorGetCameraLiveState(mSubLiveId);
                            break;
                        case "matplay":
                            mActivity.addMatPlayVideo();
                            break;
                    }
                }
            }
        });

        mRecordIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecordIv.getTag().equals("stop_record")){
                    if(mActivity != null){
                        mActivity.getPresenter().professionalLiveStartRecord(mLiveId,mSubLiveId,mCameraPosition);
                    }
                }else {
                    if(mActivity != null){
                        mActivity.getPresenter().professionalLiveStopRecord(mLiveId,mSubLiveId,mCameraPosition);
                    }
                }
            }
        });

        mGoToShowIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mActivity != null){
                    if(mActivity.getmBroadCastCamera().equals(mCameraPosition)){
                        //当前机位是信号机位，就别调接口了呗
                        mActivity.setBroadCastCamera(mCameraPosition);
                        ToastUtils.showToast(getContext(),"温馨提示:修改信号流机位成功");
                    }else{
                        mActivity.getPresenter().professionalLiveDirectorChangeBroadCastCamera(mLiveId,mCameraPosition);
                    }
                }
            }
        });
        mVoiceIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVoiceIv.getTag().equals("close_voice")){
                    //打开声音
                    setIsMute(false);
                }else {
                    //关闭声音
                    setIsMute(true);
                }
            }
        });
        onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        break;
                }
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_pro_live_director_video;
    }

    @Override
    public GSYVideoViewBridge getGSYVideoManager() {
        CustomManager.getCustomManager(getKey()).initContext(getContext().getApplicationContext());
        return CustomManager.getCustomManager(getKey());
    }

    @Override
    protected boolean backFromFull(Context context) {
        return CustomManager.backFromWindowFull(context, getKey());
    }

    @Override
    protected void releaseVideos() {
        CustomManager.releaseAllVideos(getKey());
    }


    @Override
    protected int getFullId() {
        return CustomManager.FULLSCREEN_ID;
    }

    @Override
    protected int getSmallId() {
        return CustomManager.SMALL_ID;
    }


    //    @Override
//    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
//        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
//        MultiSampleVideo multiSampleVideo = (MultiSampleVideo) gsyBaseVideoPlayer;
//        //设置Activity
//        multiSampleVideo.setActivity(mActivity);
//        //设置是否录制
//        multiSampleVideo.setRecordUsable(mIsRecord);
//        //设置机位名称
//        multiSampleVideo.setCameraName(mCameraName);
//        //设置liveId
//        multiSampleVideo.setLiveId(mLiveId);
//        //设置机位序号
//        multiSampleVideo.setCameraPosition(mCameraPosition);
//        //设置subLiveId
//        multiSampleVideo.setSubLiveId(mSubLiveId);
//        //设置直播名称
//        multiSampleVideo.setLiveName(mLiveName);
//        //设置直播机位信号流显示
//        multiSampleVideo.setSignalCameraFlagVisiable(mIsShow);
//        //设置加载图片
//        multiSampleVideo.setLoadOrAddImageRes(mType);
//        //设置是否静音
//        multiSampleVideo.setIsMute(mIsMute);
//        return multiSampleVideo;
//    }

//    @Override
//    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
//        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
//        if (gsyVideoPlayer != null) {
//            MultiSampleVideo multiSampleVideo = (MultiSampleVideo) gsyVideoPlayer;
//            //设置Activity
//            multiSampleVideo.setActivity(mActivity);
//            //设置是否录制
//            multiSampleVideo.setRecordUsable(mIsRecord);
//            //设置机位名称
//            multiSampleVideo.setCameraName(mCameraName);
//            //设置liveId
//            multiSampleVideo.setLiveId(mLiveId);
//            //设置机位序号
//            multiSampleVideo.setCameraPosition(mCameraPosition);
//            //设置subLiveId
//            multiSampleVideo.setSubLiveId(mSubLiveId);
//            //设置直播名称
//            multiSampleVideo.setLiveName(mLiveName);
//            //设置直播机位信号流显示
//            multiSampleVideo.setSignalCameraFlagVisiable(mIsShow);
//            //设置加载图片
//            multiSampleVideo.setLoadOrAddImageRes(mType);
//            //设置是否静音
//            multiSampleVideo.setIsMute(mIsMute);
//        }
//    }

    public String getKey() {
        if (mPlayPosition == -22) {
            Debuger.printfError(getClass().getSimpleName() + " used getKey() " + "******* PlayPosition never set. ********");
        }
        if (TextUtils.isEmpty(mPlayTag)) {
            Debuger.printfError(getClass().getSimpleName() + " used getKey() " + "******* PlayTag never set. ********");
        }
        return TAG + mPlayPosition + mPlayTag;
    }

    /*对外提供控制方法*/
    //设置界面对象
    public MultiSampleVideo setActivity(ProfessionalLiveDirectorActivity professionalLiveDirectorActivity){
        this.mActivity = professionalLiveDirectorActivity;
        return this;
    }


    //设置是否显示录制是否可用按钮
    private String mIsRecordable = "0";
    public MultiSampleVideo setRecordUsable(String isRecord){
        this.mIsRecordable = isRecord;
        if(mIsRecordable.equals("1")){
            mRecordIv.setVisibility(View.VISIBLE);
        }else {
            mRecordIv.setVisibility(View.GONE);
        }
        return this;
    }

    //设置机位名称
    private String mCameraName = "";
    public MultiSampleVideo setCameraName(String cameraName){
        this.mCameraName = cameraName;
        mCameraNameInSignalLoadingTv.setText(mCameraName);
        return this;
    }


    //设置liveId
    private String mLiveId;
    public MultiSampleVideo setLiveId(String liveId){
        this.mLiveId = liveId;
        return this;
    }

    //设置机位序号
    private String mCameraPosition = "";
    public MultiSampleVideo setCameraPosition(String cameraPosition){
        this.mCameraPosition = cameraPosition;
        return this;
    }

    //设置SubLiveId
    private String mSubLiveId;
    public MultiSampleVideo setSubLiveId(String subLiveId){
        this.mSubLiveId = subLiveId;
        return this;
    }

    //设置直播名称
    private String mLiveName = "";
    public MultiSampleVideo setLiveName(String liveName){
        this.mLiveName = liveName;
        mLiveTitleTv.setText(mLiveName);
        return this;
    }

    private boolean mIsShow = false;
    //设置信号流标签显示
    public MultiSampleVideo setSignalCameraFlagVisiable(boolean isShow){
        this.mIsShow = isShow;
        if(mIsShow == true){
            mSignalCameraFlagTv.setVisibility(View.VISIBLE);
        }else {
            mSignalCameraFlagTv.setVisibility(View.GONE);
        }
        return this;
    }

    //设置加载按钮资源:区分垫播还是直播机位
    //使用mType区分机位或者垫播的行为
    private String mType;
    public MultiSampleVideo setLoadOrAddImageRes(String type){
        this.mType = type;
        if(type.equals("camera")){
            mLoadOrAddVideoIv.setImageResource(R.mipmap.signal_in);
            mLoadOrAddVideoTv.setText("点击加载信号");
        }else {
            mLoadOrAddVideoIv.setImageResource(R.mipmap.add_mat_play_vedio);
            mLoadOrAddVideoTv.setText("上传垫播视频");
        }
        return this;
    }

    //设置是否静音
    private boolean mIsMute = true;
    public MultiSampleVideo setIsMute(boolean isMute){
        this.mIsMute = isMute;
        if(mIsMute == true){
            mVoiceIv.setTag("close_voice");
            mVoiceIv.setImageResource(R.drawable.bt_pro_live_voice_close);
        }else {
            mVoiceIv.setTag("open_voice");
            mVoiceIv.setImageResource(R.drawable.bt_pro_live_voice_open);
        }
        CustomManager.getCustomManager(getKey()).setNeedMute(mIsMute);
        return this;
    }

    //设置是否录制
    private boolean mIsRecord = false;
    public MultiSampleVideo setRecordIvState(boolean isRecord){
        this.mIsRecord = isRecord;
        if(mIsRecord == true){
            mRecordIv.setImageResource(R.drawable.bt_pro_live_record_pause);
            mRecordIv.setTag("start_record");
        }else {
            mRecordIv.setImageResource(R.drawable.bt_pro_live_record_start);
            mRecordIv.setTag("stop_record");
        }
        return this;
    }

    //设置是否显示信号加载按钮
    public MultiSampleVideo setLoadSignalVisiable(boolean visiable){
        if(visiable == true){
            if(mLoadSignalRl.getVisibility() == View.GONE){
                mLoadSignalRl.setVisibility(View.VISIBLE);
            }
        }else {
            if(mLoadSignalRl.getVisibility() == View.VISIBLE){
                mLoadSignalRl.setVisibility(View.GONE);
            }
        }
        return this;
    }

    //开始播放调用
    public void setUpPlayer(String tag,int cameraPosition,String url,String liveName){
        mLoadSignalRl.setVisibility(View.GONE);
        if(mType.equals("camera")){
            ToastUtils.showToast(mContext,"温馨提示:信号加载成功");
            //垫播加载成功在Activity中
        }
        setPlayTag(tag);
        setPlayPosition(cameraPosition);
        boolean isPlayIng = getCurrentPlayer().isInPlayingState();
        if(isPlayIng == false){
            setUpLazy(url,false,null,null,liveName);
        }
//        getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startWindowFullscreen(mContext,false,true);
//            }
//        });
        //一些配置
        setIsTouchWigetFull(false);
        setRotateViewAuto(false);
        setAutoFullWithSize(true);
        setNeedShowWifiTip(false);
        setLockLand(true);
        setReleaseWhenLossAudio(false);
        setShowFullAnimation(true);
        setIsTouchWiget(false);
        setNeedLockFull(true);
        setVideoAllCallBack(new GSYSampleCallBack() {

            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                setIsMute(mIsMute);
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                mFullKey = "null";
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                mFullKey = getKey();
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }
        });
    }

    //判断是否为录制状态
    public boolean getRecordState(){
        return mIsRecord;
    }

    //界面返回键退出全屏使用
    public String getFullKey() {
        return mFullKey;
    }

}
