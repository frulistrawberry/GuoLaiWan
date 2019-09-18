package com.guolaiwan.ui.widget;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.widget.dialog.NomalProgressDialog;
import com.guolaiwan.bean.ShareBean;
import com.guolaiwan.ui.activity.GuideActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import app.guolaiwan.com.guolaiwan.R;

/**
 * 创建者: 蔡朝阳
 * 日期:  2018/12/24.
 * 说明:  分享dialog
 */

public class ShareDialog implements View.OnClickListener {

    private BaseActivity mContext;
    private View mView;
    private Dialog mDialog;
    private LinearLayout mWeiXinFriendLl;
    private LinearLayout mWeiXinCircleLl;
    private LinearLayout mCancelLl;
    private String mShareUrl;
    private String mShareTitle;
    private String mDescription;
    //加载中弹窗
    private NomalProgressDialog mProgressDialog;

    public ShareDialog(BaseActivity context) {
        this.mContext = context;
        initShareDialog();
    }

    private void initShareDialog(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_share_dialog, null);
        mWeiXinFriendLl = mView.findViewById(R.id.ll_weixin_friend);
        mWeiXinCircleLl = mView.findViewById(R.id.ll_weixin_circle);
        mCancelLl = mView.findViewById(R.id.ll_cancel);

        mWeiXinFriendLl.setOnClickListener(this);
        mWeiXinCircleLl.setOnClickListener(this);
        mCancelLl.setOnClickListener(this);

        mDialog = new Dialog(mContext, R.style.NormalSelectDialogStyle);
        mDialog.setContentView(mView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_weixin_friend:
                dimissShareDialog();
                shareWinXinFriend();

                break;
            case R.id.ll_weixin_circle:
                dimissShareDialog();
                shareWinXinCircle();
                break;
            case R.id.ll_cancel:
                dimissShareDialog();
                break;
        }
    }


    /*微信好友分享*/
    private void shareWinXinFriend(){
        showLoadingDialog();
        UMImage image = new UMImage(mContext, R.mipmap.ic_launcher);
        UMWeb umWeb = new UMWeb(mShareUrl);
        umWeb.setTitle(mShareTitle);
        umWeb.setThumb(image);
        umWeb.setDescription(mDescription);
        new ShareAction(mContext)
                .withMedia(umWeb)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        dimissLoadingDialog();
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        //CommonUtils.showMsg("分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        //CommonUtils.showMsg("分享失败");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        //CommonUtils.showMsg("分享取消");
                    }
                }).share();
    }

    /*微信朋友圈分享*/
    private void shareWinXinCircle(){
        showLoadingDialog();
        UMImage image = new UMImage(mContext, R.mipmap.ic_launcher);
        UMWeb umWeb = new UMWeb(mShareUrl);
        umWeb.setTitle(mShareTitle);
        umWeb.setThumb(image);
        umWeb.setDescription(mDescription);
        new ShareAction(mContext)
                .withMedia(umWeb)
                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        dimissLoadingDialog();
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        //CommonUtils.showMsg("分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        //CommonUtils.showMsg("分享失败");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        //CommonUtils.showMsg("分享取消");
                    }
                }).share();
    }

    public ShareDialog setShareDate(ShareBean shareBean){
        mShareTitle = shareBean.getTitle();
        mShareUrl = shareBean.getUrl();
        mDescription = shareBean.getDescription();
        return this;
    }

    public ShareDialog showShareDialog(){
        mDialog.show();
        Window window = mDialog.getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        return this;
    }

    public ShareDialog dimissShareDialog(){
        mDialog.dismiss();
        return this;
    }

    private void showLoadingDialog(){
        if (mProgressDialog == null) {
            mProgressDialog = new NomalProgressDialog.Builder(mContext).build();
        }
        mProgressDialog.show();
    }

    private void dimissLoadingDialog(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
