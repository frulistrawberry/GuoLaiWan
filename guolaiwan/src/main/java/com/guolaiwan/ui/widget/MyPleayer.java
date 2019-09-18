package com.guolaiwan.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;

import com.cgx.library.utils.ToastUtils;
import com.guolaiwan.bean.Child;
import com.guolaiwan.ui.activity.GuideActivity;

import app.guolaiwan.com.guolaiwan.R;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JzvdStd;

/**
 * 作者: 蔡朝阳
 * 日期: 2018/11/6
 * 描述: 音频播放器
 */

public class MyPleayer extends JzvdStd {


    private GuideActivity mGuideActivity;
    private Child mChild;

    public MyPleayer(Context context) {
        super(context);
    }

    public MyPleayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setActivity(GuideActivity guideActivity){
        this.mGuideActivity = guideActivity;
    }

    public GuideActivity getActivity(){
        return mGuideActivity;
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        mGuideActivity.showVoiceLoading();
        mGuideActivity.mIsPlayingVoice = true;
        getCurrentPositionWhenPlaying();
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        mGuideActivity.mIsPlayingVoice = true;
        mGuideActivity.dimissVoiceLoading();
        mGuideActivity.getPlayerControllerBt().setImageResource(R.drawable.bt_guide_pause);
        //展示markerinfo window
        mGuideActivity.showMarkerInfo();
        //展示人物讲解动画
        mGuideActivity.startSpeakAnim();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        mGuideActivity.mIsPlayingVoice = false;
        mGuideActivity.getPlayerControllerBt().setImageResource(R.drawable.bt_guide_play);
        //将当前讲解点添加到mUserVisitedChildIdMap中，不在自动讲解
        mGuideActivity.setLoginUserVisitedSpot();
        //隐藏markerinfo window
        mGuideActivity.dimissMarkerInfo();
        //停止人物讲解动画
        mGuideActivity.stopSpeakAnim();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        mGuideActivity.mIsPlayingVoice = false;
        mGuideActivity.getPlayerControllerBt().setImageResource(R.drawable.bt_guide_play);
        //防止网络不好时讲完一遍接着再讲一遍
        mGuideActivity.setLoginUserVisitedSpot();
        //与服务器交互已游览的景点刷新地图
        mGuideActivity.changeMarkerState();
        //清除缓存
        mGuideActivity.removeVoiceCache();
        //隐藏markerinfo window
        mGuideActivity.dimissMarkerInfo();
        //停止人物讲解动画
        mGuideActivity.stopSpeakAnim();
        //刷新SeekBar进度
        mGuideActivity.updateSeekBarProgress(0);
    }

    @Override
    public void onStateError() {
        super.onStateError();
        mGuideActivity.dismissLoadingDialog();
        ToastUtils.showToast(mGuideActivity,"温馨提示:语音加载出错");

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
        mGuideActivity.updateSeekBarProgress(progress);
    }

    //对Activity开放:设置当前讲解Child
    public void setCurrentIntroduceChild(Child Child){
        this.mChild = Child;
    }
}
