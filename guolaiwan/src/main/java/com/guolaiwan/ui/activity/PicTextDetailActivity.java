package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.BaseViewHolder;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.FrescoUtil;
import com.cgx.library.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.guolaiwan.bean.ArticleBean;
import com.guolaiwan.bean.FriendBean;
import com.guolaiwan.bean.RichUpload;
import com.guolaiwan.bean.ShareBean;
import com.guolaiwan.constant.UrlConstant;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.ui.widget.ShareDialog;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ViewUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.Observable;
import mabeijianxi.camera.util.Log;

public class PicTextDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.layout_ptr)
    public PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;
    @BindView(R.id.surface_view)
    SurfaceView surfaceView;
    @BindView(R.id.playbtn)
    ImageView playbtn;
    //分享Dialog
    private ShareDialog mShareDialog;
    BaseQuickAdapter<RichUpload.Content,BaseViewHolder> mAdapter;
    public int mCurrentPage=1;
    private String id;
    TextView nickTv;
    SimpleDraweeView headIv;
    TextView titleTv;
    View headerView;
    String userId;
    String title;
    String cover;
    MediaPlayer mMediaPlayer;
    private boolean isloaded;
    private boolean haveMusic;
    private boolean isPrepared;


    public void onClick(View v){
        if (v.getId()==R.id.userLayout){
            Intent intent = new Intent();
            intent.setClass(this,UserVideoPicActivity.class);
            intent.putExtra("userId",userId);
            intent.putExtra("nickName",nickTv.getText().toString());
            startActivity(intent);
        }else if (v.getId() == R.id.playbtn){
            if (mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
                playbtn.setImageResource(R.mipmap.ic_play_music);
            }else {
                mMediaPlayer.start();
                playbtn.setImageResource(R.mipmap.ic_pause_music);
            }
        }
    }

    public static void launch(Context context, String id){
        Intent intent = new Intent(context,PicTextDetailActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    public void showContent() {
        super.showContent();
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
        if (mAdapter.isLoading())
            mAdapter.loadMoreComplete();
    }

    @Override
    public void showLoading() {
        if (!mPtrLayout.isRefreshing() && mCurrentPage == 1){
            mAdapter.setEmptyView(ViewUtils.getLoadingView(getContext()));
        }
    }

    @Override
    public void showEmpty() {
        if (mCurrentPage == 1){
            mAdapter.setNewData(null);
            mAdapter.setEmptyView(ViewUtils
                    .getEmptyView(getContext(),R.mipmap.no_project,"暂无数据"));
        }

        else {
            mAdapter.loadMoreEnd(false);
        }
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
    }

    @Override
    public void showError() {
        mAdapter.setEmptyView(ViewUtils.getErrorView(getContext()));
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
        if (mCurrentPage>1){
            mAdapter.loadMoreFail();
        }
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setTitle("图文详情").setLeftImage(R.mipmap.back_black, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveMusic){
                    if (isPrepared)
                        finish();
                    else
                        return;
                }else {
                    finish();
                }
            }
        }).setRightText("分享", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 暂时
                ShareBean shareBean = new ShareBean();
                shareBean.setUrl("http://www.guolaiwan.net/download/download.html");
                shareBean.setTitle("畅游华夏,尽在过来玩");
                shareBean.setDescription("联系电话" + "\n" + "0315-6681288/6686299");
                mShareDialog.setShareDate(shareBean);
                mShareDialog.showShareDialog();
            }
        }).show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_pic_detail);
        mShareDialog = new ShareDialog(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setOnKeyListener(new OnKeyClickListener() {
            @Override
            public void clickBack() {
                if (haveMusic){
                    if (isPrepared)
                        finish();
                    else
                        return;
                }else {
                    finish();
                }
            }
        });
        headerView = LayoutInflater.from(this).inflate(R.layout.acticle_detail_header,mRecyclerView,false);
        titleTv = headerView.findViewById(R.id.titleTv);
        headIv = headerView.findViewById(R.id.iv_img);
        nickTv = headerView.findViewById(R.id.tv_nick_name);
        headerView.findViewById(R.id.userLayout).setOnClickListener(this);
        mAdapter.addHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(mp -> {
            isPrepared = true;
            playbtn.setVisibility(View.VISIBLE);
            playbtn.setImageResource(R.mipmap.ic_pause_music);
            mMediaPlayer.start();
        });
        playbtn.setOnClickListener(this);
        request();
    }


    @Override
    protected void initData() {

        id = getIntent().getStringExtra("id");
        mAdapter = new BaseQuickAdapter<RichUpload.Content, BaseViewHolder>(R.layout.item_pic_text) {
            @Override
            protected void convert(BaseViewHolder helper, RichUpload.Content item) {
                SimpleDraweeView simpleDraweeView = helper.getView(R.id.iv_img);
                if (!StringUtils.isEmpty(item.img)){
                    simpleDraweeView.setVisibility(View.VISIBLE);
                    FrescoUtil.getInstance().loadImageWrapContent(simpleDraweeView,item.img);
                    simpleDraweeView.setOnClickListener(v -> {
                        ArrayList<String> images = new ArrayList<>();
                        images.add(item.img);
                        ImageBrowserActivity.launcher(mContext,images);
                    });
                }else {
                    simpleDraweeView.setVisibility(View.GONE);
                }

                if (StringUtils.isEmpty(item.text)){
                    helper.setGone(R.id.text,false);
                }else {
                    helper.setGone(R.id.text,true);
                    helper.setText(R.id.text,item.text);
                }
            }
        };
    }


    @Override
    protected void initEvent() {
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                request();
            }
        });


    }

    private void request(){
        showLoading();
        Observable<HttpResult<ArticleBean>> observable = HttpClient.getApiService().getPicInfo(id);
        RetrofitUtil.composeToSubscribe(observable, new HttpObserver<ArticleBean>() {
            @Override
            public void onNext(String message, ArticleBean respones) {
                userId = respones.videoPic.getUserId()+"";
                nickTv.setText(respones.videoPic.getUser().getUserNickname());
                FrescoUtil.getInstance().loadNetImage(headIv,respones.videoPic.getUser().getUserHeadimg());
                String context = respones.videoPic.getContent();
                RichUpload upload = new Gson().fromJson(context,RichUpload.class);
                titleTv.setText(upload.title);
                title = upload.title;
                cover = upload.cover;
                List<RichUpload.Content> data = upload.content;
                if (CollectionUtils.isEmpty(data)){
                    showEmpty();
                    return;
                }
                mAdapter.setNewData(data);
                if (!TextUtils.isEmpty(upload.music)){
                    haveMusic = true;
                    try {
                        if (!isloaded){
                            isloaded = true;
                            Uri uri = Uri.parse(upload.music);
                            mMediaPlayer.setDataSource(PicTextDetailActivity.this,uri);
                            mMediaPlayer.prepareAsync();
                        }
                    }catch (Exception e){

                    }

                }

                showContent();
            }

            @Override
            public void onError(int errCode, String errMessage) {
                showError();
            }

            @Override
            public void onComplete() {

            }
        },getLifeSubject());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer!=null&& mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }
}
