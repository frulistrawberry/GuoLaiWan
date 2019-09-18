package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.FrescoUtil;
import com.cgx.library.utils.SPUtils;
import com.cgx.library.widget.dialog.AlertDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.faucamp.simplertmp.RtmpHandler;
import com.guolaiwan.bean.LookLiveData;
import com.guolaiwan.bean.MessageBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.StartLiveBean;
import com.guolaiwan.presenter.CameraPresenter;
import com.guolaiwan.ui.adapter.MessageAdapter;
import com.guolaiwan.ui.iview.CameraView;
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
import butterknife.OnClick;

import static com.cgx.library.net.ExceptionHandler.handleException;

public class LiveActivity extends BaseActivity implements SrsEncodeHandler.SrsEncodeListener, SrsRecordHandler.SrsRecordListener, RtmpHandler.RtmpListener, CameraView, BaseActivity.OnKeyClickListener {

    @BindView(R.id.view_live)
    SrsCameraView mCameraView;
    @BindView(R.id.et_live_name)
    EditText mLiveNameEt;
    @BindView(R.id.btn_start_live)
    TextView mStartLiveTv;
    @BindView(R.id.iv_back)
    ImageView mBackTv;
    @BindView(R.id.layout_pre)
    RelativeLayout mPreLayout;
    @BindView(R.id.layout_living)
    RelativeLayout mLivingLayout;
    @BindView(R.id.iv_img)
    SimpleDraweeView mImageTv;
    @BindView(R.id.tv_product_name)
    TextView mProductNameTv;
    @BindView(R.id.tv_price)
    TextView mPriceTv;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.product_layout)
    LinearLayout mProductLayout;
    @BindView(R.id.add_product)
    ImageView addProductIv;
    @BindView(R.id.btn_confirm)
    TextView confirmBtn;
    @BindView(R.id.tv_live_name)
    TextView liveNameTv;
    @BindView(R.id.layout_del)
    LinearLayout delBtn;
    @BindView(R.id.add_product_tv)
    TextView addProductTv;


    RefundPopup mCommentPop;
    private String liveId;
    private boolean isConfirm = true;
    SrsPublisher mPublisher;
    CameraPresenter mPresenter;
    MessageAdapter mAdapter;
    private String productId;

    public static void launch(Context context){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
            return;
        }
        context.startActivity(new Intent(context,LiveActivity.class));
    }

    @OnClick({R.id.btn_start_live,R.id.send_message,R.id.add_product,R.id.stop_live,R.id.change_camera,R.id.iv_back,R.id.btn_confirm,R.id.btn_delete})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_start_live:
                String liveName = mLiveNameEt.getText().toString().trim();
                if (TextUtils.isEmpty(liveName)){
                    CommonUtils.showMsg("温馨提示:请输入直播名称");
                    return;
                }
                SPUtils.putString("liveName",liveName);
                liveNameTv.setText(liveName+"-直播中");
                mPresenter.startLive(liveName);
                break;
            case R.id.send_message:
                mCommentPop.show(view);
                break;
            case R.id.add_product:
                if (productBean==null)
                    ChooseProductActivity.launch(this);
                else {
                  CommonUtils.showMsg("温馨提示:当前商品成交或取消后才可以发布商品");
                }
                break;

            case R.id.stop_live:
                if (productBean!=null){
                    new AlertDialog.Builder(this,getSupportFragmentManager())
                            .setCancelable(true)
                            .setTitle("提示")
                            .setMessage("温馨提示:当前有拍卖正在进行，退出直播后下次直播将继续拍卖")
                            .setNegativeText("取消")
                            .setPositiveText("退出")
                            .setOnPositiveListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mPresenter.stopLive();
                                }
                            }).build().show();
                }else
                    mPresenter.stopLive();
                break;

            case R.id.iv_back:
                finish();
                break;

            case R.id.change_camera:
                mPublisher.switchCameraFace((mPublisher.getCamraId() + 1) % Camera.getNumberOfCameras());
                break;

            case R.id.btn_confirm:
                if (! isConfirm)
                mPresenter.confirm(productId,liveId);
                break;

            case R.id.btn_delete:
                mPresenter.delete(productId,liveId);
                break;
        }
    }

    @Override
    protected void initView() {
        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_live);
        setOnKeyListener(this);
        mLiveNameEt.setText(SPUtils.getString("liveName",""));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mPublisher = new SrsPublisher(mCameraView);
        //初始化直播插件
        //编码状态回调
        mPublisher.setEncodeHandler(new SrsEncodeHandler(this));
        mPublisher.setRecordHandler(new SrsRecordHandler(this));
        //rtmp推流状态回调
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        //预览分辨率
        mPublisher.setPreviewResolution(360,640);
        //推流分辨率
        mPublisher.setOutputResolution(360,640);
        //传输率：高清
        //mPublisher.setVideoHDMode();
        //传输率：流畅
        mPublisher.setVideoSmoothMode();
        //开启美颜
        mPublisher.switchCameraFilter(MagicFilterType.BEAUTY);
       //打开摄像头
        mPublisher.startCamera();
        initRefondPop();
        if (CommonUtils.isMerchant()){
            addProductIv.setVisibility(View.VISIBLE);
            addProductTv.setVisibility(View.VISIBLE);

        }else {
            addProductIv.setVisibility(View.GONE);
            addProductTv.setVisibility(View.GONE);
        }
    }

    private void initRefondPop(){
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
                    String reason = mCommentPop.getContentEt().getText().toString().trim();
                    if (com.cgx.library.utils.StringUtils.isEmpty(reason)){
                        CommonUtils.showMsg("请输入消息内容");
                        return;
                    }
                    String msgStr = EmojiUtil.emojiToString(reason);
                    mCommentPop.getContentEt().setText("");
                    mPresenter.addMessage(msgStr,liveId);
                    mCommentPop.dismiss();
                }
            }
        });
        mCommentPop.setHinit("说点儿什么吧");
        mCommentPop.setOkText("发送");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
        mPublisher.stopPublish();
    }


    @Override
    protected void initData() {
        mPresenter = new CameraPresenter(this);
        mAdapter = new MessageAdapter();
    }

    @Override
    protected void initEvent() {}


    @Override
    public void onRtmpConnecting(String msg) {}

    @Override
    public void onRtmpConnected(String msg) {}

    @Override
    public void onRtmpVideoStreaming() {}

    @Override
    public void onRtmpAudioStreaming() {}

    @Override
    public void onRtmpStopped() {}

    @Override
    public void onRtmpDisconnected() {}

    @Override
    public void onRtmpVideoFpsChanged(double fps) {
        Log.i(TAG, String.format("Output Fps: %f", fps));
    }

    @Override
    public void onRtmpVideoBitrateChanged(double bitrate) {
        int rate = (int) bitrate;
        if (rate / 1000 > 0) {
            Log.i(TAG, String.format("Video bitrate: %f kbps", bitrate / 1000));
        } else {
            Log.i(TAG, String.format("Video bitrate: %d bps", rate));
        }
    }

    @Override
    public void onRtmpAudioBitrateChanged(double bitrate) {
        int rate = (int) bitrate;
        if (rate / 1000 > 0) {
            Log.i(TAG, String.format("Audio bitrate: %f kbps", bitrate / 1000));
        } else {
            Log.i(TAG, String.format("Audio bitrate: %d bps", rate));
        }
    }

    @Override
    public void onRtmpSocketException(SocketException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIOException(IOException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {
        handleException(e);
    }

    // Implementation of SrsRecordHandler.
    @Override
    public void onRecordPause() {
        Toast.makeText(getApplicationContext(), "录制暂停", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordResume() {
        Toast.makeText(getApplicationContext(), "录制恢复", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordStarted(String msg) {
        Toast.makeText(getApplicationContext(), "录制文件: " + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordFinished(String msg) {
        Toast.makeText(getApplicationContext(), "MP4 文件保存: " + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordIOException(IOException e) {
        handleException(e);
    }

    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    // Implementation of SrsEncodeHandler.
    @Override
    public void onNetworkWeak() {
        Toast.makeText(getApplicationContext(), "温馨提示:网络出现波动,请耐心等待。", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkResume() {
        Toast.makeText(getApplicationContext(), "温馨提示:网络恢复。", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    @Override
    public void setLiveData(LookLiveData data) {
        StartLiveBean liveData = data.getLiveInfo();
        String rtmpUrl = "rtmp://www.guolaiwan.net/live/" + CommonUtils.getUserId() + "A" + liveData.getId();
        liveId = liveData.getId();
        mStartLiveTv.setVisibility(View.GONE);
        mPreLayout.setVisibility(View.GONE);
        mLivingLayout.setVisibility(View.VISIBLE);
        mProductLayout.setVisibility(View.GONE);
        //选择硬编码
        mPublisher.switchToHardEncoder();
        //开始推流
        mPublisher.startPublish(rtmpUrl);
        mPublisher.startCamera();
        ProductBean productData = data.getLiveProductInfo();
        if (productData != null) {
            showProduct(productData);
        }
    }

    public void addMessage(MessageBean message){
        runOnUiThread(() -> {
            List<MessageBean> data = new ArrayList<>();
            if (CollectionUtils.isEmpty(mAdapter.getData())){
                data.add(message);
                mAdapter.setNewData(data);
            }else
                mAdapter.addData(message);
            mRecyclerView.smoothScrollToPosition(mAdapter.getData().size()-1);
            mAdapter.notifyDataSetChanged();
        });
    }
    ProductBean productBean;

    public void showProduct(ProductBean product){
        productBean = product;
        runOnUiThread(() -> {
            productId = product.getId()+"";
            mProductNameTv.setText(product.getProductName());
            mPriceTv.setText("当前出价:"+(product.getPrice()/100)+"元");
            FrescoUtil.getInstance().loadNetImage(mImageTv,product.getHeadPic());
            addProductIv.setImageResource(R.mipmap.ic_product_list_press);
            mProductLayout.setVisibility(View.VISIBLE);
            confirmBtn.setClickable(!product.isLocked());
            isConfirm = product.isLocked();
            if (product.isLocked()){
                confirmPrice(product.getPrice());
                delBtn.setVisibility(View.GONE);
            }else {
                confirmBtn.setText("成交");
                confirmBtn.setBackgroundDrawable(getResourceDrawable(R.drawable.shap_btn_green));
                delBtn.setVisibility(View.VISIBLE);
            }
        });

    }

    public void changePrice(int price){
        runOnUiThread(() -> mPriceTv.setText("当前出价:"+price/100+"元"));
    }

    public void confirmPrice(float price){
        productBean = null;
        runOnUiThread(() -> {
            mPriceTv.setText("成交价:"+(price/100)+"元");
            confirmBtn.setText("已成交");
            confirmBtn.setBackgroundDrawable(getResourceDrawable(R.drawable.shap_btn_gray));
            delBtn.setVisibility(View.GONE);

        });
    }

    @Override
    public void setLookLiveData(LookLiveData data) {}

    @Override
    public void delePruduct() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                productBean = null;
                mProductLayout.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1){
            productId = data.getStringExtra("productId");
            mPresenter.addProduct(data.getStringExtra("productId"),liveId,data.getStringExtra("price"));
        }
    }

    /*返回键退出*/
    @Override
    public void clickBack() {
        if (productBean == null){
            mPresenter.stopLive();
            return;
        }
        new AlertDialog.Builder(this,getSupportFragmentManager())
                .setCancelable(true)
                .setTitle("提示")
                .setMessage("当前有拍卖正在进行，退出直播后下次直播将继续拍卖")
                .setNegativeText("取消")
                .setPositiveText("退出")
                .setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.stopLive();
                    }
                }).build().show();
    }


}
