package com.guolaiwan.ui.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.SPUtils;
import com.cgx.library.utils.ToastUtils;
import com.cgx.library.widget.TitleBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.guolaiwan.App;
import com.guolaiwan.bean.ProfessionalLiveCameraInfoBean;
import com.guolaiwan.bean.ProfessionalLiveDirectorBean;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.bean.ProfessionalLiveSubLiveBean;
import com.guolaiwan.bean.ProfessionalLiveWatcherCheckLiveStateBean;
import com.guolaiwan.constant.UrlConstant;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.presenter.ProfessionalLiveDirectorPresenter;
import com.guolaiwan.ui.iview.ProfessionalLiveDirectorView;
import com.guolaiwan.ui.widget.CustomManager;
import com.guolaiwan.ui.widget.MultiSampleVideo;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.DimenUtil;
import com.guolaiwan.utils.ImageFactory;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.io.File;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import io.reactivex.Observable;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.ThumbnailUtils;
import io.vov.vitamio.widget.VideoView;
import mabeijianxi.camera.LocalMediaCompress;
import mabeijianxi.camera.model.AutoVBRMode;
import mabeijianxi.camera.model.LocalMediaConfig;
import mabeijianxi.camera.model.OnlyCompressOverBean;
import mabeijianxi.camera.model.VBRMode;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfessionalLiveDirectorActivity extends BaseActivity implements ProfessionalLiveDirectorView,View.OnClickListener, BaseActivity.OnKeyClickListener, RadioGroup.OnCheckedChangeListener {

    private String TAG = "ProfessionalLiveDirectorActivity";

    //返回按钮
    @BindView(R.id.iv_back)
    ImageView mBackIv;
    //ScrollView
    @BindView(R.id.sc_director)
    ScrollView mDirectorSv;
    //导播界面ScrollView第一个子控件
    @BindView(R.id.ll_director)
    LinearLayout mDirectorLl;
    //机位一
    @BindView(R.id.vedio_camera_1)
    MultiSampleVideo mVideoCamera1;
    //机位二
    @BindView(R.id.vedio_camera_2)
    MultiSampleVideo mVideoCamera2;
    //机位三
    @BindView(R.id.vedio_camera_3)
    MultiSampleVideo mVideoCamera3;
    //机位四
    @BindView(R.id.vedio_camera_4)
    MultiSampleVideo mVideoCamera4;
    //机位五
    @BindView(R.id.vedio_camera_5)
    MultiSampleVideo mVideoCamera5;
    //机位六
    @BindView(R.id.vedio_camera_6)
    MultiSampleVideo mVideoCamera6;
    //垫播
    @BindView(R.id.vedio_mat_play)
    MultiSampleVideo mMatPlayVideo;
    //机位可关闭直播按钮
    @BindView(R.id.rg_camera_can_close)
    RadioGroup mCamearCanCloseRg;
    @BindView(R.id.rb_camera_can_close_yes)
    RadioButton mCameraCanCloseYesRb;
    @BindView(R.id.rb_camera_can_close_no)
    RadioButton mCameraCanCloseNoRb;
    //开启/关闭直播按钮
    @BindView(R.id.tv_live)
    TextView mLiveTv;
    //直播名称编辑
    @BindView(R.id.et_live_name)
    EditText mLiveNameEt;
    //直播名称显示
    @BindView(R.id.tv_name_live)
    TextView mLiveNameTv;
    //修改直播按钮
    @BindView(R.id.tv_change_live_name)
    TextView mChangeLiveNameTv;

    //成员变量
    //当前主播机位
    private String mBroadCastCamera = "0";
    //可用的机位集合
    private List<ProfessionalLiveSubLiveBean> mUsableCameraList;
    //直播标题：直播列表显示
    private String mLiveName;
    //跳转视频选择界面请求码
    private int GOTO_SYSTEM_VEDIO_ACTIVITY = 1;
    //本地垫播视频路径
    private String mMatPlayVedioPath = "";


    private ProfessionalLiveDirectorPresenter mPresenter;
    private ProfessionalLiveDirectorBean mProfessionalLiveDirectorBean;
    private String mDirectorId;
    private String mLiveId;


    public static void launch(Context context,ProfessionalLiveDirectorBean professionalLiveDirectorBean){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
            return;
        }
        Intent intent = new Intent(context, ProfessionalLiveDirectorActivity.class);
        intent.putExtra("ProfessionalLiveDirectorBean",professionalLiveDirectorBean);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_professional_live_director);
        setOnKeyListener(this);
        Intent intent = getIntent();
        mProfessionalLiveDirectorBean = (ProfessionalLiveDirectorBean) intent.getSerializableExtra("ProfessionalLiveDirectorBean");
        mLiveId = mProfessionalLiveDirectorBean.getLiveId();
        mDirectorId = mProfessionalLiveDirectorBean.getId();
        mMatPlayVedioPath = SPUtils.getString("MAT_PLAY_VEDIO_PATH");
        //防止EditText自动获取焦点
        mDirectorSv.setFocusable(true);
        mDirectorSv.setFocusableInTouchMode(true);
        mDirectorSv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        mPresenter.professionalLiveCheckDirectorLiveState(mLiveId);
    }


    @Override
    protected void initData() {
        mPresenter = new ProfessionalLiveDirectorPresenter(this);
    }

    @Override
    protected void initEvent() {
        //返回键
        mBackIv.setOnClickListener(this);
        //开启/关闭 直播按钮
        mLiveTv.setOnClickListener(this);
        //直播名称显示
        mLiveNameTv.setOnClickListener(this);
        //修改直播名称
        mChangeLiveNameTv.setOnClickListener(this);
        mDirectorSv.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //toolbar返回键
            case R.id.iv_back:
                mPresenter.professionalLiveUpdateDirectorUsable(mLiveId,mDirectorId,"NO");
                finish();
                break;

            //直播名称TextView点击可修改名称
            case R.id.tv_name_live:
                mLiveNameTv.setVisibility(View.GONE);
                mLiveNameEt.setVisibility(View.VISIBLE);
                mChangeLiveNameTv.setVisibility(View.VISIBLE);
                break;

            //开启关闭直播按钮
            case R.id.tv_live:
                if(mLiveTv.getText().toString().equals("开启直播")){
                    if(mBroadCastCamera.equals("0")){
                        ToastUtils.showToast(getContext(),"温馨提示:请设置播出机位");
                        return;
                    }
                    if(mCameraCanCloseYesRb.isChecked()){
                        ToastUtils.showToast(getContext(),"温馨提示:请将机位可关闭直播设置为否");
                        return;
                    }
                    String liveName = mLiveNameEt.getText().toString().trim();
                    if(liveName == null || liveName.equals("")){
                        ToastUtils.showToast(getContext(),"温馨提示:请输入直播名称");
                        return;
                    }
                    mLiveName = liveName;
                    mPresenter.professionalLiveDirectorUpdateLiveState(mLiveId,mLiveName,"LIVING");
                }else {
                    if(!mCameraCanCloseYesRb.isChecked()){
                        mCameraCanCloseYesRb.setChecked(true);
                    }
                    mPresenter.professionalLiveDirectorUpdateLiveState(mLiveId,"","STOP");
                }
                break;

            //修改直播名称按钮
            case R.id.tv_change_live_name:
                String liveName = mLiveNameEt.getText().toString().trim();
                if(liveName == null || liveName.equals("")){
                    ToastUtils.showToast(getContext(),"温馨提示:请输入直播名称");
                }else {
                    mLiveName = liveName;
                    mPresenter.professionalLiveDirectorUpdateLiveName(mLiveId,mLiveName);
                }
                break;

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
            case R.id.rb_camera_can_close_yes:
                mPresenter.professionalLiveDirectorUpdateCameraCanClose(mLiveId,"YES");
                break;
            case R.id.rb_camera_can_close_no:
                mPresenter.professionalLiveDirectorUpdateCameraCanClose(mLiveId,"NO");
                break;
        }
    }

    /*设置导播界面状态:开启关闭按钮，直播名控件是TextView还是EditText*/
    @Override
    public void setDirectorState(ProfessionalLiveWatcherCheckLiveStateBean professionalLiveWatcherCheckLiveStateBean) {
        if(professionalLiveWatcherCheckLiveStateBean.getBroadCastCamera().equals("") && professionalLiveWatcherCheckLiveStateBean.getLiveStatus().equals("") && professionalLiveWatcherCheckLiveStateBean.getLiveName().equals("")){
            String cameraCanClose = professionalLiveWatcherCheckLiveStateBean.getCameraCanClose();
            //机位可关闭直播按钮
            if(cameraCanClose.equals("YES")){
                mCameraCanCloseYesRb.setChecked(true);
                mCameraCanCloseNoRb.setChecked(false);
            }else {
                mCameraCanCloseYesRb.setChecked(false);
                mCameraCanCloseNoRb.setChecked(true);
            }
            //数据库中professionallive表没有这个liveId的数据，说明第一次使用
            mPresenter.professionalLiveUpdateDirectorUsable(mLiveId,mDirectorId,"YES");
        }else {
            mBroadCastCamera = professionalLiveWatcherCheckLiveStateBean.getBroadCastCamera();
            String liveStatus = professionalLiveWatcherCheckLiveStateBean.getLiveStatus();
            String liveName = professionalLiveWatcherCheckLiveStateBean.getLiveName();
            String cameraCanClose = professionalLiveWatcherCheckLiveStateBean.getCameraCanClose();
            //机位可关闭直播按钮
            if(cameraCanClose.equals("YES")){
                mCameraCanCloseYesRb.setChecked(true);
                mCameraCanCloseNoRb.setChecked(false);
            }else {
                mCameraCanCloseYesRb.setChecked(false);
                mCameraCanCloseNoRb.setChecked(true);
            }
            if(liveStatus.equals("STOP")){
                //没开播还扯啥蛋
                mPresenter.professionalLiveUpdateDirectorUsable(mLiveId,mDirectorId,"YES");
            }else {
                //直播名
                mLiveNameEt.setVisibility(View.GONE);
                mLiveNameTv.setVisibility(View.VISIBLE);
                mLiveNameTv.setText("直播名称:" + liveName);
                //开启关闭直播按钮
                mLiveTv.setText("关闭直播");
                mPresenter.professionalLiveUpdateDirectorUsable(mLiveId,mDirectorId,"YES");
            }
        }
    }

    /*初始化各机位*/
    @Override
    public void initEveryCamera(ProfessionalLiveCameraInfoBean cameraInfo) {
        //机位可关闭直播按钮
        mCamearCanCloseRg.setOnCheckedChangeListener(this);
        String isMatPlay = cameraInfo.getIsMatPlay();
        String isRecord = cameraInfo.getIsRecord();
        mUsableCameraList = cameraInfo.getSubLives();
        //设置机位显示
        for(int i = 0;i < mUsableCameraList.size();i++){
            ProfessionalLiveSubLiveBean professionalLiveSubLiveBeanI = mUsableCameraList.get(i);
            String id = professionalLiveSubLiveBeanI.getId();
            String recordState = professionalLiveSubLiveBeanI.getRecordState();
            MultiSampleVideo cameraI = (MultiSampleVideo) mDirectorLl.getChildAt(i);
            cameraI.setVisibility(View.VISIBLE);
            //设置录制是否可用
            cameraI.setRecordUsable(isRecord);
            //设置Activity属性
            cameraI.setActivity(this);
            //设置liveId
            cameraI.setLiveId(mLiveId);
            //设置sunLiveId
            cameraI.setSubLiveId(id);
            //设置加载按钮样式
            cameraI.setLoadOrAddImageRes("camera");
            //设置静音
            cameraI.setIsMute(true);
            //设置录制按钮状态
            if(recordState.equals("STOP")){
                //停止录制状态
                cameraI.setRecordIvState(false);
            }else {
                //正在录制状态
                cameraI.setRecordIvState(true);
            }
            //设置信号加载界面机位名称
            switch (i){
                case 0:
                    cameraI.setCameraName("机位一");
                    cameraI.setCameraPosition("1");
                    break;
                case 1:
                    cameraI.setCameraName("机位二");
                    cameraI.setCameraPosition("2");
                    break;
                case 2:
                    cameraI.setCameraName("机位三");
                    cameraI.setCameraPosition("3");
                    break;
                case 3:
                    cameraI.setCameraName("机位四");
                    cameraI.setCameraPosition("4");
                    break;
                case 4:
                    cameraI.setCameraName("机位五");
                    cameraI.setCameraPosition("5");
                    break;
                case 5:
                    cameraI.setCameraName("机位六");
                    cameraI.setCameraPosition("6");
                    break;
            }
        }
        //初始化垫播
        if(isMatPlay.equals("1")){
            //设置是否显示
            mMatPlayVideo.setVisibility(View.VISIBLE);
            //设置是否录制:垫播不录制
            mMatPlayVideo.setRecordUsable("0");
            //设置Activity属性
            mMatPlayVideo.setActivity(this);
            //设置liveId
            mMatPlayVideo.setLiveId(mLiveId);
            //设置加载按钮样式
            mMatPlayVideo.setLoadOrAddImageRes("matplay");
            //设置机位位置
            mMatPlayVideo.setCameraPosition("7");
            //设置机位名称
            mMatPlayVideo.setCameraName("垫播");
            //设置直播名称
            mMatPlayVideo.setLiveName("垫播");
            //是否静音
            mMatPlayVideo.setIsMute(true);
        }else {
            mMatPlayVideo.setVisibility(View.GONE);
        }
    }

    /*设置机位直播状态*/
    @Override
    public void setCameraLiveState(ProfessionalLiveSubLiveBean professionalLiveSubLiveBean) {
        String status = professionalLiveSubLiveBean.getStatus();
        String subLiveId = professionalLiveSubLiveBean.getId();
        String liveName = professionalLiveSubLiveBean.getLiveName();
        String cameraNumber = professionalLiveSubLiveBean.getCameraNumber();
        String pubName = professionalLiveSubLiveBean.getPubName();

        if(status.equals("STOP")){
            //信号未接入提醒
            switch (cameraNumber){
                case "1":
                    ToastUtils.showToast(getContext(),"温馨提示:机位一暂无信号接入");
                    return;
                case "2":
                    ToastUtils.showToast(getContext(),"温馨提示:机位二暂无信号接入");
                    return;
                case "3":
                    ToastUtils.showToast(getContext(),"温馨提示:机位三暂无信号接入");
                    return;
                case "4":
                    ToastUtils.showToast(getContext(),"温馨提示:机位四暂无信号接入");
                    break;
                case "5":
                    ToastUtils.showToast(getContext(),"温馨提示:机位五暂无信号接入");
                    return;
                case "6":
                    ToastUtils.showToast(getContext(),"温馨提示:机位六暂无信号接入");
                    return;

            }
        }
        if(status.equals("LIVING")){
            //机位正在直播中接入信号
            //switch区分机位
            switch (cameraNumber){
                case "1" :
                    mVideoCamera1.setLiveName(liveName);
                    mVideoCamera1.setUpPlayer(TAG,1,UrlConstant.RTMP_HOST + pubName,liveName);
                    Log.i("CAI","机位一视频URL:" + UrlConstant.RTMP_HOST + pubName);
                    break;
                case "2":
                    mVideoCamera2.setLiveName(liveName);
                    mVideoCamera2.setUpPlayer(TAG,2,UrlConstant.RTMP_HOST + pubName,liveName);
                    Log.i("CAI","机位二视频URL:" + UrlConstant.RTMP_HOST + pubName);
                    break;
                case "3":
                    mVideoCamera3.setLiveName(liveName);
                    mVideoCamera3.setUpPlayer(TAG,3,UrlConstant.RTMP_HOST + pubName,liveName);
                    Log.i("CAI","机位三视频URL:" + UrlConstant.RTMP_HOST + pubName);
                    break;
                case "4":
                    mVideoCamera4.setLiveName(liveName);
                    mVideoCamera4.setUpPlayer(TAG,4,UrlConstant.RTMP_HOST + pubName,liveName);
                    Log.i("CAI","机位四视频URL:" + UrlConstant.RTMP_HOST + pubName);
                    break;
                case "5":
                    mVideoCamera5.setLiveName(liveName);
                    mVideoCamera5.setUpPlayer(TAG,5,UrlConstant.RTMP_HOST + pubName,liveName);
                    Log.i("CAI","机位五视频URL:" + UrlConstant.RTMP_HOST + pubName);
                    break;
                case "6":
                    mVideoCamera6.setLiveName(liveName);
                    mVideoCamera6.setUpPlayer(TAG,6,UrlConstant.RTMP_HOST + pubName,liveName);
                    Log.i("CAI","机位六视频URL:" + UrlConstant.RTMP_HOST + pubName);
                    break;
            }
            //点击信号加载时设置主播机位标签显示
            switch (mBroadCastCamera){
                //1-6机位
                case "1":
                    if(mLiveTv.getText().equals("关闭直播")){
                        mVideoCamera1.setSignalCameraFlagVisiable(true);
                    }
                    break;
                case "2":
                    if(mLiveTv.getText().equals("关闭直播")){
                        mVideoCamera2.setSignalCameraFlagVisiable(true);
                    }
                    break;
                case "3":
                    if(mLiveTv.getText().equals("关闭直播")){
                        mVideoCamera3.setSignalCameraFlagVisiable(true);
                    }
                    break;
                case "4":
                    if(mLiveTv.getText().equals("关闭直播")){
                        mVideoCamera4.setSignalCameraFlagVisiable(true);
                    }
                    break;
                case "5":
                    if(mLiveTv.getText().equals("关闭直播")){
                        mVideoCamera5.setSignalCameraFlagVisiable(true);
                    }
                    break;
                case "6":
                    if(mLiveTv.getText().equals("关闭直播")){
                        mVideoCamera6.setSignalCameraFlagVisiable(true);
                    }
                    break;
            }
        }
    }

    /*导播开启/关闭直播结果*/
    @Override
    public void openOrCloseDirectorLiveResult(String liveStatus) {
        if(liveStatus.equals("LIVING")){
            //导播开直播操作
            mLiveNameEt.setText("");
            mLiveNameEt.setVisibility(View.GONE);
            mLiveNameTv.setVisibility(View.VISIBLE);
            mLiveNameTv.setText("直播名称:" + mLiveName);
            mLiveTv.setText("关闭直播");
            ToastUtils.showToast(getContext(),"温馨提示:开启直播成功");
        }else {
            //机位停止录制提醒:必须一个一个关别废话
            if(mVideoCamera1.getRecordState() == true){
                ToastUtils.showToast(getContext(),"温馨提示:请停止机位一录制");
                return;
            }
            if(mVideoCamera2.getRecordState() == true){
                ToastUtils.showToast(getContext(),"温馨提示:请停止机位二录制");
                return;
            }
            if(mVideoCamera3.getRecordState() == true){
                ToastUtils.showToast(getContext(),"温馨提示:请停止机位三录制");
                return;
            }
            if(mVideoCamera4.getRecordState() == true){
                ToastUtils.showToast(getContext(),"温馨提示:请停止机位四录制");
                return;
            }
            if(mVideoCamera5.getRecordState() == true){
                ToastUtils.showToast(getContext(),"温馨提示:请停止机位五录制");
                return;
            }if(mVideoCamera6.getRecordState() == true){
                ToastUtils.showToast(getContext(),"温馨提示:请停止机位六录制");
                return;
            }


            //导播关直播操作
            mBroadCastCamera = "0";
            mLiveNameTv.setText("");
            mLiveNameEt.setText("");
            mLiveNameTv.setVisibility(View.GONE);
            mLiveNameEt.setVisibility(View.VISIBLE);
            if(SPUtils.contains("MAT_PLAY_VEDIO_PATH")){
                SPUtils.remove("MAT_PLAY_VEDIO_PATH");
            }
            mLiveTv.setText("开启直播");
            ToastUtils.showToast(getContext(),"温馨提示:关闭直播成功");
            //恢复播放器状态
            //释放所有Video
            CustomManager.clearAllVideo();
            //播放器显示信号加载按钮
            mVideoCamera1.setLoadSignalVisiable(true);
            mVideoCamera2.setLoadSignalVisiable(true);
            mVideoCamera3.setLoadSignalVisiable(true);
            mVideoCamera4.setLoadSignalVisiable(true);
            mVideoCamera5.setLoadSignalVisiable(true);
            mVideoCamera6.setLoadSignalVisiable(true);
            mMatPlayVideo.setLoadSignalVisiable(true);
            //隐藏信号机位标签
            mVideoCamera1.setSignalCameraFlagVisiable(false);
            mVideoCamera2.setSignalCameraFlagVisiable(false);
            mVideoCamera3.setSignalCameraFlagVisiable(false);
            mVideoCamera4.setSignalCameraFlagVisiable(false);
            mVideoCamera5.setSignalCameraFlagVisiable(false);
            mVideoCamera6.setSignalCameraFlagVisiable(false);
            mMatPlayVideo.setSignalCameraFlagVisiable(false);

        }
    }

    /*导播修改直播名称结果*/
    @Override
    public void updateLiveNameResult() {
        mChangeLiveNameTv.setVisibility(View.GONE);
        mLiveNameEt.setVisibility(View.GONE);
        mLiveNameEt.setText("");
        mLiveNameTv.setVisibility(View.VISIBLE);
        mLiveNameTv.setText("直播名称:" + mLiveName);
        ToastUtils.showToast(getContext(),"温馨提示:直播名称修改成功");
    }

    /*设置信号流机位结果*/
    @Override
    public void setBroadCastCamera(String broadCastCamera) {
        mBroadCastCamera = broadCastCamera;
        //重置信号机位标签
        //先全部隐藏
        mVideoCamera1.setSignalCameraFlagVisiable(false);
        mVideoCamera2.setSignalCameraFlagVisiable(false);;
        mVideoCamera3.setSignalCameraFlagVisiable(false);
        mVideoCamera4.setSignalCameraFlagVisiable(false);
        mVideoCamera5.setSignalCameraFlagVisiable(false);
        mVideoCamera6.setSignalCameraFlagVisiable(false);
        mMatPlayVideo.setSignalCameraFlagVisiable(false);
        //设置显示
        switch (broadCastCamera){
            case"1":
                mVideoCamera1.setSignalCameraFlagVisiable(true);
                break;
            case"2":
                mVideoCamera2.setSignalCameraFlagVisiable(true);
                break;
            case"3":
                mVideoCamera3.setSignalCameraFlagVisiable(true);
                break;
            case"4":
                mVideoCamera4.setSignalCameraFlagVisiable(true);
                break;
            case"5":
                mVideoCamera5.setSignalCameraFlagVisiable(true);
                break;
            case"6":
                mVideoCamera6.setSignalCameraFlagVisiable(true);
                break;
            case"7":
                mMatPlayVideo.setSignalCameraFlagVisiable(true);
                break;
        }
        ToastUtils.showToast(getContext(),"温馨提示:修改信号流机位成功");
    }

    @Override
    public void startRecordResult(String cameraPosition) {
        switch (cameraPosition){
            case "1":
                mVideoCamera1.setRecordIvState(true);
                ToastUtils.showToast(getContext(),"温馨提示:机位一开始录制");
                break;
            case "2":
                mVideoCamera2.setRecordIvState(true);
                ToastUtils.showToast(getContext(),"温馨提示:机位二开始录制");
                break;
            case "3":
                mVideoCamera3.setRecordIvState(true);
                ToastUtils.showToast(getContext(),"温馨提示:机位三开始录制");
                break;
            case "4":
                mVideoCamera4.setRecordIvState(true);
                ToastUtils.showToast(getContext(),"温馨提示:机位四开始录制");
                break;
            case "5":
                mVideoCamera5.setRecordIvState(true);
                ToastUtils.showToast(getContext(),"温馨提示:机位五开始录制");
                break;
            case "6":
                mVideoCamera6.setRecordIvState(true);
                ToastUtils.showToast(getContext(),"温馨提示:机位六开始录制");
                break;
        }
    }

    @Override
    public void stopRecordResult(String cameraPosition) {
        switch (cameraPosition){
            case "1":
                mVideoCamera1.setRecordIvState(false);
                ToastUtils.showToast(getContext(),"温馨提示:机位一停止录制");
                break;
            case "2":
                mVideoCamera2.setRecordIvState(false);
                ToastUtils.showToast(getContext(),"温馨提示:机位二停止录制");
                break;
            case "3":
                mVideoCamera3.setRecordIvState(false);
                ToastUtils.showToast(getContext(),"温馨提示:机位三停止录制");
                break;
            case "4":
                mVideoCamera4.setRecordIvState(false);
                ToastUtils.showToast(getContext(),"温馨提示:机位四停止录制");
                break;
            case "5":
                mVideoCamera5.setRecordIvState(false);
                ToastUtils.showToast(getContext(),"温馨提示:机位五停止录制");
                break;
            case "6":
                mVideoCamera6.setRecordIvState(false);
                ToastUtils.showToast(getContext(),"温馨提示:机位六停止录制");
                break;
        }
    }

    /*对播放器提供方法*/
    /*提供Presenter对象，方便播放器调用接口*/
    public ProfessionalLiveDirectorPresenter getPresenter(){
        if(mPresenter != null){
            return mPresenter;
        }else {
            return null;
        }
    }

    /*提供当前直播机位,切流时调用*/
    //用于判断信号流机位是否为与当前机位一致
    public String getmBroadCastCamera(){
        return mBroadCastCamera;
    }

    /*上传垫播视频*/
    public void addMatPlayVideo(){
        if(mMatPlayVedioPath.equals("") || mLiveTv.getText().equals("开启直播")){
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            intent.setType("video/*");
            startActivityForResult(intent,GOTO_SYSTEM_VEDIO_ACTIVITY);
        }else {
            mMatPlayVideo.setUpPlayer(TAG,7,mMatPlayVedioPath,"垫播");
            ToastUtils.showToast(getContext(),"温馨提示:视频上传成功");
        }
        if(mBroadCastCamera.equals("7") && mLiveTv.getText().equals("关闭直播")){
            mMatPlayVideo.setSignalCameraFlagVisiable(true);
        }
    }

    @Override
    public void clickBack() {
//        if (CustomManager.backFromWindowFull(this, mVideoCamera1.getFullKey())) {
//            return;
//        }
//
//        if (CustomManager.backFromWindowFull(this, mVideoCamera2.getFullKey())) {
//            return;
//        }
//
//        if (CustomManager.backFromWindowFull(this, mVideoCamera3.getFullKey())) {
//            return;
//        }
//
//        if (CustomManager.backFromWindowFull(this, mVideoCamera4.getFullKey())) {
//            return;
//        }
//
//        if (CustomManager.backFromWindowFull(this, mVideoCamera5.getFullKey())) {
//            return;
//        }
//
//        if (CustomManager.backFromWindowFull(this, mVideoCamera6.getFullKey())) {
//            return;
//        }

        mPresenter.professionalLiveUpdateDirectorUsable(mLiveId,mDirectorId,"NO");
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOTO_SYSTEM_VEDIO_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                //视频路径
                Uri vedioUri = data.getData();
                ContentResolver contentResolver = this.getContentResolver();
                Cursor cursor = contentResolver.query(vedioUri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()){
                        //视频路径
                        String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        mMatPlayVedioPath = "file:/" + videoPath;
                        //sp记录当前垫播视频本地路径
                        SPUtils.putString("MAT_PLAY_VEDIO_PATH",mMatPlayVedioPath);
                        //垫播视频上传服务器
                        // 第一步压缩视频
                        compressMatPlayVedio(videoPath);
                    }
                    cursor.close();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomManager.onPauseAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CustomManager.onResumeAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomManager.clearAllVideo();
    }


    /*垫播视频处理相关*/
    /*第一步:压缩垫播视频*/
    OnlyCompressOverBean mOnlyCompressOverBean;
    public void compressMatPlayVedio(String vedioPath){
        LocalMediaConfig.Buidler buidler = new LocalMediaConfig.Buidler();
        final LocalMediaConfig localMediaConfig = buidler
                .setVideoPath(vedioPath)
                .captureThumbnailsTime(1)
                .doH264Compress(new VBRMode(4096,2048))
                .setFramerate(15)
                .build();
        showLoadingDialog();
        new Thread(() -> {
            mOnlyCompressOverBean = new LocalMediaCompress(localMediaConfig).startCompress();
            handler.sendEmptyMessage(1);
        }).start();
    }

    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String vedioCompressedPath = mOnlyCompressOverBean.getVideoPath();
                    uploadMatPlayVedio(vedioCompressedPath);
                    break;
            }
        }
    };

    /*第二步:压缩完成上传服务器*/
    private void uploadMatPlayVedio(String path) { ;
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("vedioFile", mLiveId, requestFile);
        Observable<HttpResult<String>> observable = HttpClient.getApiService().uploadMatPlayVedio(part);
        RetrofitUtil.composeToSubscribe(observable, new HttpObserver<String>() {

            @Override
            public void onComplete() {
                dismissLoadingDialog();
            }

            @Override
            public void onNext(String message, String data) {
                dismissLoadingDialog();
                mMatPlayVideo.setUpPlayer(TAG,7,mMatPlayVedioPath,"垫播");
                mPresenter.professionalLiveStartMatPlayService(mLiveId);
            }
            @Override
            public void onError(int errCode, String errMessage) {
                dismissLoadingDialog();
                ToastUtils.showToast(getContext(),"温馨提示:视频上传失败" + errCode);
            }
        },getLifeSubject());
    }

}
