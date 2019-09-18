package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.StringUtils;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.ui.widget.CheckPop;
import com.guolaiwan.ui.widget.RefundPopup;
import com.guolaiwan.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.OnClick;
import cn.simonlee.xcodescanner.core.CameraScanner;
import cn.simonlee.xcodescanner.core.GraphicDecoder;
import cn.simonlee.xcodescanner.core.NewCameraScanner;
import cn.simonlee.xcodescanner.core.OldCameraScanner;
import cn.simonlee.xcodescanner.core.ZBarDecoder;
import cn.simonlee.xcodescanner.view.AdjustTextureView;
import cn.simonlee.xcodescanner.view.ScannerFrameView;

public class CheckActivity extends BaseActivity implements CameraScanner.CameraListener, TextureView.SurfaceTextureListener, GraphicDecoder.DecodeListener {

    private AdjustTextureView mTextureView;
    private ScannerFrameView mScannerFrameView;

    private CameraScanner mCameraScanner;
    protected GraphicDecoder mGraphicDecoder;

    private CheckPop refundPopup;
    boolean getedOrderCode;


    public static void launch(Context context){
        if (CommonUtils.isLogin()){
            context.startActivity(new Intent(context,CheckActivity.class));
        }else {
            LoginActivity.launch(context);
        }
    }

    @OnClick(R.id.input_button)
    public void onClick(View v){
        showCmmtPop(v);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().showBack().setTitle("验单").show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_check);
        mTextureView = findViewById(R.id.textureview);
        mScannerFrameView = findViewById(R.id.scannerframe);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCameraScanner = NewCameraScanner.getInstance();
        } else {
            mCameraScanner = OldCameraScanner.getInstance();
        }

        mCameraScanner.setCameraListener(this);
        mTextureView.setSurfaceTextureListener(this);
        initRefondPop();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void onRestart() {
        Log.d(TAG, getClass().getName() + ".onRestart()");
        if (mTextureView.isAvailable()) {
            //部分机型转到后台不会走onSurfaceTextureDestroyed()，因此isAvailable()一直为true，转到前台后不会再调用onSurfaceTextureAvailable()
            //因此需要手动开启相机
            mCameraScanner.setSurfaceTexture(mTextureView.getSurfaceTexture());
            mCameraScanner.setPreviewSize(mTextureView.getWidth(), mTextureView.getHeight());
            mCameraScanner.openCamera(this.getApplicationContext());
        }
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, getClass().getName() + ".onPause()");
        mCameraScanner.closeCamera();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, getClass().getName() + ".onDestroy()");
        mCameraScanner.setGraphicDecoder(null);
        if (mGraphicDecoder != null) {
            mGraphicDecoder.setDecodeListener(null);
            mGraphicDecoder.detach();
        }
        mCameraScanner.detach();
        super.onDestroy();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.e(TAG, getClass().getName() + ".onSurfaceTextureAvailable() width = " + width + " , height = " + height);
        mCameraScanner.setSurfaceTexture(surface);
        mCameraScanner.setPreviewSize(width, height);
        mCameraScanner.openCamera(this.getApplicationContext());
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.e(TAG, getClass().getName() + ".onSurfaceTextureSizeChanged() width = " + width + " , height = " + height);
        // TODO 当View大小发生变化时，要进行调整。
//        mTextureView.setImageFrameMatrix();
//        mCameraScanner.setPreviewSize(width, height);
//        mCameraScanner.setFrameRect(mScannerFrameView.getLeft(), mScannerFrameView.getTop(), mScannerFrameView.getRight(), mScannerFrameView.getBottom());
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.e(TAG, getClass().getName() + ".onSurfaceTextureDestroyed()");
        return true;
    }

    @Override// 每有一帧画面，都会回调一次此方法
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {}

    @Override
    public void openCameraSuccess(int frameWidth, int frameHeight, int frameDegree) {
        Log.e(TAG, getClass().getName() + ".openCameraSuccess() frameWidth = " + frameWidth + " , frameHeight = " + frameHeight + " , frameDegree = " + frameDegree);
        mTextureView.setImageFrameMatrix(frameWidth, frameHeight, frameDegree);
        if(mGraphicDecoder == null){
            mGraphicDecoder = new ZBarDecoder();//使用带参构造方法可指定条码识别的格式
            mGraphicDecoder.setDecodeListener(this);
        }
        //该区域坐标为相对于父容器的左上角顶点。
        //TODO 应考虑TextureView与ScannerFrameView的Margin与padding的情况
        mCameraScanner.setFrameRect(mScannerFrameView.getLeft(), mScannerFrameView.getTop(), mScannerFrameView.getRight(), mScannerFrameView.getBottom());
        mCameraScanner.setGraphicDecoder(mGraphicDecoder);
    }

    @Override
    public void openCameraError() {
        CommonUtils.showMsg("出错了");
    }

    @Override
    public void noCameraPermission() {
        CommonUtils.showMsg("没有权限");
    }

    @Override
    public void cameraDisconnected() {
        CommonUtils.showMsg("断开了连接");
    }
    String result = "";

    @Override
    public void decodeSuccess(int type, int quality, String result) {
        if (!TextUtils.isEmpty(this.result))
            return;
        this.result = result;
        Map<String,String> params = new HashMap<>();
        params.put("userId",CommonUtils.getUserId());
        params.put("orderNo",result);
        io.reactivex.Observable< HttpResult> observable = HttpClient.getApiService().checkOder(params);
        showLoadingDialog();
        RetrofitUtil.composeToSubscribe(observable, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
                finish();
            }

            @Override
            public void onError(int errCode, String errMessage) {

            }

            @Override
            public void onComplete() {
                dismissLoadingDialog();
            }
        },getLifeSubject());
    }

    private void initRefondPop(){
        refundPopup=new CheckPop(this)
                .createPopup();
        refundPopup.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (refundPopup.isShowing()) {
                    refundPopup.dismiss();
                }
            }
        });

        refundPopup.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (refundPopup.isShowing()) {
                    String reason = refundPopup.getContentEt().getText().toString().trim();
                    if (StringUtils.isEmpty(reason)){
                        CommonUtils.showMsg("请输入订单编号");
                        return;
                    }
                    Map<String,String> params = new HashMap<>();
                    params.put("userId",CommonUtils.getUserId());
                    params.put("orderNo",reason);
                    io.reactivex.Observable< HttpResult> observable = HttpClient.getApiService().checkOder(params);
                    showLoadingDialog();
                    RetrofitUtil.composeToSubscribe(observable, new HttpObserver() {
                        @Override
                        public void onNext(String message, Object data) {
                            CommonUtils.showMsg(message);
                            finish();
                        }

                        @Override
                        public void onError(int errCode, String errMessage) {

                        }

                        @Override
                        public void onComplete() {
                            dismissLoadingDialog();
                        }
                    },getLifeSubject());
                    refundPopup.dismiss();
                }
            }
        });
    }

    private void showCmmtPop(View view){
        refundPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
}
