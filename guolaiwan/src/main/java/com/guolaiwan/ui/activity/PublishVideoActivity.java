package com.guolaiwan.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.log.LogUtil;
import com.guolaiwan.App;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ImageFactory;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import mabeijianxi.camera.LocalMediaCompress;
import mabeijianxi.camera.MediaRecorderActivity;
import mabeijianxi.camera.model.LocalMediaConfig;
import mabeijianxi.camera.model.OnlyCompressOverBean;
import mabeijianxi.camera.model.VBRMode;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.guolaiwan.ui.activity.UpLoadActivity.getRealPathFromUri;

/**
 * Created by Administrator on 2018/5/26/026.
 */

public class PublishVideoActivity extends BaseActivity {
    @BindView(R.id.et_content)
    EditText mContentEt;
    @BindView(R.id.tv_publish)
    TextView mPublishBtn;
    @BindView(R.id.iv_video)
    ImageView videoIv;
    @BindView(R.id.et_title)
    EditText titleET;

    String path = "";
    String fUrl = "";
    String headPic = "";
    String picUrl = "";

    @OnClick({R.id.tv_publish,R.id.tv_cancel,R.id.add_music})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_publish:
                if (TextUtils.isEmpty(fUrl)){
                    CommonUtils.showMsg("视频未上传");
                    return;
                }
                if (TextUtils.isEmpty(headPic)){
                    CommonUtils.showMsg("封面未上传");
                    return;
                }
                if (TextUtils.isEmpty(mContentEt.getText())){
                    CommonUtils.showMsg("请输入视频描述");
                    return;
                }
                if (TextUtils.isEmpty(titleET.getText())){
                    CommonUtils.showMsg("请输入视频标题");
                    return;
                }
                showLoadingDialog();


                Map<String,String> body = new HashMap<>();
                body.put("fUrl",fUrl);
                body.put("type","LITTLEVEDIO");
                body.put("context",mContentEt.getText().toString().trim());
                body.put("userId",CommonUtils.getUserId());
                body.put("headPic",headPic);
                body.put("sName",titleET.getText().toString().trim());
                if (!TextUtils.isEmpty(musicPath)){
                    body.put("murl",musicPath);
                }
                LogUtil.d("params",body);
                Observable<HttpResult> observable1 = HttpClient.getApiService().videoPic(body);
                RetrofitUtil.composeToSubscribe(observable1, new HttpObserver() {
                    @Override
                    public void onNext(String message, Object data) {
                        CommonUtils.showMsg(message);
                        EventBus.getDefault().post("refresh_friend");
                        finish();
                    }

                    @Override
                    public void onError(int errCode, String errMessage) {
                        CommonUtils.error(errCode,errMessage);
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                    }
                },getLifeSubject());
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.add_music:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,3);
                break;
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_publish_video);
        Intent intent = getIntent();
        path = intent.getStringExtra(MediaRecorderActivity.VIDEO_URI);
        Log.i("CAI","压缩前的路径:"  + path);
        picUrl = intent.getStringExtra(MediaRecorderActivity.VIDEO_SCREENSHOT);
        Bitmap bitmap = BitmapFactory.decodeFile( picUrl);
        videoIv.setImageBitmap(bitmap);
        LocalMediaConfig.Buidler buidler = new LocalMediaConfig.Buidler();
        final LocalMediaConfig config = buidler
                .setVideoPath(path)
                .captureThumbnailsTime(1)
                .doH264Compress(new VBRMode(4096,2048))
                .setFramerate(15)
                .build();
       compress(config);
    }

    OnlyCompressOverBean onlyCompressOverBean;
    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    dismissLoadingDialog();
                    path = onlyCompressOverBean.getVideoPath();
                    Log.i("CAI","压缩后的路径:"  + path);
                    uploadAvatar(path,"video");
                    break;
            }
        }
    };


    public static void compressImageToFile(Bitmap bmp,File file) {
        // 0-100 100为不压缩
        int options = 60;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void compress(LocalMediaConfig config){
        showLoadingDialog();
        new Thread(() -> {
            onlyCompressOverBean = new LocalMediaCompress(config).startCompress();
            handler.sendEmptyMessage(1);
        }).start();
    }

    private String videoUri;
    @Override
    protected void initData() {}

    @Override
    protected void initEvent() {}

    private void uploadAvatar(String path,String type) {
        showLoadingDialog();
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Observable<HttpResult<String>> observable = HttpClient.getApiService().uploadAvatar(part);
        RetrofitUtil.composeToSubscribe(observable, new HttpObserver<String>() {

            @Override
            public void onComplete() {
                dismissLoadingDialog();
            }

            @Override
            public void onNext(String message, String data) {
                if (!TextUtils.isEmpty(data)){
                    if (type.equals("video")){
                        fUrl = data;
                        new Thread(() -> {
                            ImageFactory factory = new ImageFactory();
                            File tumb = new File(App.APP_PIC_PATH + System.currentTimeMillis()+".jpg");
                            compressImageToFile(factory.getBitmap(picUrl),tumb);
                            uploadAvatar(tumb.getAbsolutePath(),"pic");
                        }).run();
                    }else if (type.equals("music")){
                        musicPath = data;
                    }
                    else {
                        headPic = data;
                    }
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                dismissLoadingDialog();
                CommonUtils.error(errCode,errMessage);
            }
        },getLifeSubject());
    }

    String musicPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null && data.getData()!=null){

            Uri uri = data.getData();
            //视频文件路径
            musicPath = getRealPathFromUri(this,uri);
            uploadAvatar(musicPath,"music");
        }


    }
}
