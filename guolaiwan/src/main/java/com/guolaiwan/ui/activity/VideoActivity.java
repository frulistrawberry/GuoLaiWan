package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.load.model.ImageVideoWrapperEncoder;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.FrescoUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.guolaiwan.bean.ShareBean;
import com.guolaiwan.constant.UrlConstant;
import com.guolaiwan.ui.widget.ShareDialog;
import com.guolaiwan.utils.CommonUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.w3c.dom.Text;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by Administrator on 2018/5/25/025.
 */

public class VideoActivity extends BaseActivity {

    @BindView(R.id.surface_view)
    VideoView mVideoView;
    @BindView(R.id.tv_loading)
    TextView loadingTv;
    @BindView(R.id.iv_head)
    SimpleDraweeView headIv;
    @BindView(R.id.tv_author_name)
    TextView nickTv;
    @BindView(R.id.tv_content)
    TextView contentTv;
    @BindView(R.id.tv_live_name)
    TextView titleTV;

    private ShareDialog mShareDialog;

    String filePath;
    String nickName;
    String headUrl;
    String content;
    String title;
    String userId;

    public static void launch(Context context, String path, String nickName, String headUrl, String content, String title,String userId){
        Intent intent = new Intent(context,VideoActivity.class);
        intent.putExtra("path",path);
        intent.putExtra("nickName",nickName);
        intent.putExtra("title",title);
        intent.putExtra("headUrl",headUrl);
        intent.putExtra("content",content);
        intent.putExtra("userId",userId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        if (!LibsChecker.checkVitamioLibs(this)){
            return;
        }
        setContentView(R.layout.activity_video);
        mShareDialog = new ShareDialog(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        filePath = getIntent().getStringExtra("path");
        nickName = getIntent().getStringExtra("nickName");
        title = getIntent().getStringExtra("title");
        headUrl = getIntent().getStringExtra("headUrl");
        content = getIntent().getStringExtra("content");
        userId = getIntent().getStringExtra("userId");
        FrescoUtil.getInstance().loadNetImage(headIv,headUrl);
        nickTv.setText(nickName);
        titleTV.setText(title);
        contentTv.setText(content);
        mVideoView.setBufferSize(1024*1024*2);
        mVideoView.setVideoURI(Uri.parse(filePath));
        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
        mVideoView.start();
    }

    @OnClick({R.id.iv_back,R.id.userLayout,R.id.shareBtn})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.userLayout:
                Intent intent = new Intent();
                intent.setClass(this,UserVideoPicActivity.class);
                intent.putExtra("userId",userId);
                intent.putExtra("nickName",nickName);
                startActivity(intent);
                break;
            case R.id.shareBtn:
                //TODO 暂时
                ShareBean shareBean = new ShareBean();
                shareBean.setUrl("http://www.guolaiwan.net/download/download.html");
                shareBean.setTitle("畅游华夏,尽在过来玩");
                shareBean.setDescription("联系电话" + "\n" + "0315-6681288/6686299");
                mShareDialog.setShareDate(shareBean);
                mShareDialog.showShareDialog();
                break;
        }
    }



    @Override
    protected void initData() {}

    @Override
    protected void initEvent() {

        mVideoView.setOnBufferingUpdateListener((mp, percent) -> loadingTv.setText("视频较大请稍后"+percent+"%"));
        mVideoView.setOnInfoListener((mp, what, extra) -> {
            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START){ //缓冲开始
                loadingTv.setVisibility(View.VISIBLE);
                mVideoView.pause();

            }else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END){//缓冲结束
                loadingTv.setVisibility(View.GONE);
                mVideoView.start();

            }else if (what == MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED){ //下载速率变化
            }
            return false;
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);
            }
        });

        //进度条
        mVideoView.setOnCompletionListener(mp -> {
            //播放视频完成回调
            mVideoView.stopPlayback();
            mVideoView.setVideoURI(Uri.parse(filePath));
            mVideoView.start();
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView.isPlaying())
            mVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mVideoView.isPlaying()&&!mVideoView.isBuffering())
            mVideoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }
}
