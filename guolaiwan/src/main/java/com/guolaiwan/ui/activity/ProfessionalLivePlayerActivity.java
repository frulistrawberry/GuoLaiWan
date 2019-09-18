package com.guolaiwan.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.CollectionUtils;
import com.guolaiwan.bean.MessageBean;
import com.guolaiwan.presenter.ProfessionalLivePlayerPresenter;
import com.guolaiwan.ui.adapter.MessageAdapter;
import com.guolaiwan.ui.iview.ProfessionalLivePlayerView;
import com.guolaiwan.ui.widget.MyVedioView;
import com.guolaiwan.ui.widget.RefundPopup;
import com.guolaiwan.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;

public class ProfessionalLivePlayerActivity extends BaseActivity implements ProfessionalLivePlayerView, View.OnClickListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    //播放器
    @BindView(R.id.mvv_live_vedio)
    VideoView mLiveVedioVv;
    //直播名称:显示当前机位直播名称
    //若无机位信号，什么也不显示
    @BindView(R.id.tv_live_name)
    TextView mLiveNameTv;
    //消息按钮
    @BindView(R.id.iv_send_message)
    ImageView mSendMessageIv;
    //退出按钮
    @BindView(R.id.iv_exit)
    ImageView mExitIv;
    //显示消息控件
    @BindView(R.id.rv_message)
    RecyclerView mMessageRv;
    //无信号刷新按钮
    @BindView(R.id.rl_refresh_notice)
    RelativeLayout mRefreshNoticeRl;
    //资源加载控件
    @BindView(R.id.rl_loading)
    RelativeLayout mLoadingRl;
    //评论输入控件
    RefundPopup mCommentPop;
    //评论输出控件Adapter
    private MessageAdapter mAdapter;

    private String mLiveId;
    private ProfessionalLivePlayerPresenter mPresenter;

    public static void launch(Context context,String liveId){
        Intent intent = new Intent(context,ProfessionalLivePlayerActivity.class);
        intent.putExtra("liveId",liveId);
        context.startActivity(intent);
    }


    @Override
    protected void initView() {
        if (!LibsChecker.checkVitamioLibs(this)){
            return;
        }
        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_professional_live_player);
        Intent intent = getIntent();
        mLiveId = intent.getStringExtra("liveId");
        mLiveVedioVv.setBufferSize(1024 * 1024 * 2);
        mLiveVedioVv.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
        //设置播放器监听
        mLiveVedioVv.setOnBufferingUpdateListener(this);
        mLiveVedioVv.setOnInfoListener(this);
        mLiveVedioVv.setOnPreparedListener(this);
        mLiveVedioVv.setOnErrorListener(this);
        //获取直播信息
        mPresenter.professionalLiveWatcherGetLiveState(mLiveId);
        //初始化评论显示控件
        mMessageRv.setLayoutManager(new LinearLayoutManager(this));
        mMessageRv.setAdapter(mAdapter);
        //初始化评论输入控件
        initCommentPop();
    }

    @Override
    protected void initData() {
        mPresenter = new ProfessionalLivePlayerPresenter(this);
        mAdapter = new MessageAdapter();
    }

    @Override
    protected void initEvent() {
        mRefreshNoticeRl.setOnClickListener(this);
        mSendMessageIv.setOnClickListener(this);
        mExitIv.setOnClickListener(this);
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
                    if(!CommonUtils.isLogin()){
                        LoginActivity.launch(getContext());
                        return;
                    }else {
                        String commentMessage = mCommentPop.getContentEt().getText().toString().trim();
                        if (com.cgx.library.utils.StringUtils.isEmpty(commentMessage)){
                            CommonUtils.showMsg("请输入消息内容");
                            return;
                        }
                        mPresenter.professionalLiveSendCommentMessage(commentMessage,mLiveId);
                        mCommentPop.getContentEt().setText("");
                        mCommentPop.dismiss();
                    }
                }
            }
        });
        mCommentPop.setHinit("说点儿什么吧");
        mCommentPop.setOkText("发送");
    }

    /*获取直播状态无机位信号接入处理*/
    @Override
    public void showsNoSignalNotice() {
        mLiveNameTv.setText("暂无信号接入");
        mRefreshNoticeRl.setVisibility(View.VISIBLE);
    }


    /*设置播放流URL*/
    @Override
    public void setVedioUrl(String liveName,String vedioUrl) {
        //显示加载界面
        mLoadingRl.setVisibility(View.VISIBLE);
        mLiveNameTv.setText(liveName);
        //给Vedio设置播放流URL
        Log.i("CAI","专业直播观看界面视频URL:" + vedioUrl);
        mLiveVedioVv.setVideoPath(vedioUrl);
    }

    /*刷新评论*/
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
            mMessageRv.smoothScrollToPosition(mAdapter.getData().size() - 1);
            mAdapter.notifyDataSetChanged();
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //无信号提醒
            case R.id.rl_refresh_notice:
                mRefreshNoticeRl.setVisibility(View.GONE);
                mPresenter.professionalLiveWatcherGetLiveState(mLiveId);
                break;
            //评论
            case R.id.iv_send_message:
                mCommentPop.show(view);
                break;
            //退出界面
            case R.id.iv_exit:
                finish();
                break;
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
       Log.i("CAI","缓冲进度:" + percent);
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what){
            //缓冲开始
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if(mLoadingRl.getVisibility() == View.GONE){
                    mLoadingRl.setVisibility(View.VISIBLE);
                }
                Log.i("CAI","缓冲开始");
                mLiveVedioVv.pause();
                break;
            //缓冲结束
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                if(mLoadingRl.getVisibility() == View.VISIBLE){
                    mLoadingRl.setVisibility(View.GONE);
                }
                Log.i("CAI","缓冲结束");
                mLiveVedioVv.start();
                break;
            //下载速率变化
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setPlaybackSpeed(1.0f);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if(mLoadingRl.getVisibility() == View.VISIBLE){
            mLoadingRl.setVisibility(View.GONE);
        }
        mLiveVedioVv.stopPlayback();
        showsNoSignalNotice();
        return true;
    }

    /*Activity声明周期方法*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLiveVedioVv.stopPlayback();
        mPresenter.destroy();
    }
}
