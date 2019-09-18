package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cgx.library.base.BaseActivity;
import com.guolaiwan.utils.CommonUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;

public class PrefessionalLiveRecordVideoPlayActivity extends BaseActivity implements BaseActivity.OnKeyClickListener {

    //播放器
    @BindView(R.id.video_player)
    StandardGSYVideoPlayer mVideoPlayer;

    private String mSubLiveName;
    private String mVideoUrl;
    private OrientationUtils mOrientationUtils;

    public static void launch(Context context,String subLiveName,String videoUrl){
        Intent intent = new Intent(context,PrefessionalLiveRecordVideoPlayActivity.class);
        intent.putExtra("subLiveName",subLiveName);
        intent.putExtra("videoUrl",videoUrl);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_prefessional_live_record_video_play);
        setOnKeyListener(this);
        Intent intent = getIntent();
        mSubLiveName = intent.getStringExtra("subLiveName");
        mVideoUrl = intent.getStringExtra("videoUrl");
        mVideoPlayer.setUp(mVideoUrl,true,mSubLiveName);
        mVideoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        mVideoPlayer.getBackButton().setVisibility(View.GONE);
        mVideoPlayer.getFullscreenButton().setVisibility(View.GONE);
        mOrientationUtils = new OrientationUtils(this, mVideoPlayer);
        mOrientationUtils.setEnable(false);
        mVideoPlayer.setNeedShowWifiTip(false);
        mVideoPlayer.startPlayLogic();
    }

    @Override
    protected void initData() {}

    @Override
    protected void initEvent() {}

    @Override
    protected void onPause() {
        super.onPause();
        mVideoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (mOrientationUtils != null)
            mOrientationUtils.releaseListener();
    }


    @Override
    public void clickBack() {
        if (mOrientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mVideoPlayer.getFullscreenButton().performClick();
            return;
        }
        mVideoPlayer.setVideoAllCallBack(null);
        finish();
    }
}
