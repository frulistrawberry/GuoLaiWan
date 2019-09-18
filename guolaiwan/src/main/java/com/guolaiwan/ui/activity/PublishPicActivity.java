package com.guolaiwan.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.EventLog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.CameraUtils;
import com.cgx.library.utils.ImageUtils;
import com.cgx.library.utils.log.LogUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.App;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.ui.adapter.AddImageAdapter;
import com.guolaiwan.ui.widget.DialogOnItemClickListener;
import com.guolaiwan.ui.widget.NormalSelectDialog;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ImageFactory;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class PublishPicActivity extends BaseActivity implements DialogOnItemClickListener, AddImageAdapter.OnDeleteListener, BaseQuickAdapter.OnItemClickListener {

    private final int REQUEST_CAMERA = 0x00;
    private final int REQUEST_PICTURE = 0xa1;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_content)
    EditText mContentEt;
    @BindView(R.id.tv_publish)
    TextView mPublishBtn;
    AddImageAdapter mAdapter;
    private List<String> result;
    private int selectedCount;
    private NormalSelectDialog selectDialog;
    private String fUrl = "";
    private int count = 0;
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

    boolean uploading = false;

    @OnClick({R.id.tv_publish,R.id.tv_cancel})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_publish:
                if (uploading)
                    return;
                uploading = true;
                showLoadingDialog();
                fUrl = "";
                count = 0;
                if (result.size()>1){
                    for (int i = 0; i < result.size(); i++) {
                        if (result.get(i).equals("add"))
                            continue;
                        int finalI = i;
                        new Thread(() -> {
                            ImageFactory factory = new ImageFactory();
                            File tumb = new File(App.APP_PIC_PATH+System.currentTimeMillis()+".jpg");
                            compressImageToFile(factory.getBitmap(result.get(finalI)),tumb);
                            uploadAvatar(tumb.getAbsolutePath());
                        }).run();
                    }
                }else {
                    Map<String,String> body = new HashMap<>();
                    body.put("type","PICTURE");
                    body.put("context",mContentEt.getText().toString().trim());
                    body.put("userId",CommonUtils.getUserId());
                    LogUtil.d("params",body);
                    Observable<HttpResult> observable1 = HttpClient.getApiService().videoPic(body);
                    RetrofitUtil.composeToSubscribe(observable1, new HttpObserver() {
                        @Override
                        public void onNext(String message, Object data) {
                            CommonUtils.showMsg(message);
                            uploading = false;
                            finish();
                            EventBus.getDefault().post("refresh_friend");
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
                }
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_pic_public);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        selectDialog = new NormalSelectDialog.Builder(this).setOnItemClickListener(this).build();
        selectDialog.setDataList(Arrays.asList("拍照","选择图片"));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mAdapter = new AddImageAdapter();
        result = new ArrayList<>();
        result.add("add");
        mAdapter.setNewData(result);
        mAdapter.setOnDeleteListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initEvent() {
        mContentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0){
                    mPublishBtn.setClickable(true);
                    mPublishBtn.setTextColor(getResourceColor(R.color.text_black));
                }else {
                    if (result.size()>1){
                        mPublishBtn.setClickable(true);
                        mPublishBtn.setTextColor(getResourceColor(R.color.text_black));
                    }else {
                        mPublishBtn.setClickable(false);
                        mPublishBtn.setTextColor(getResourceColor(R.color.gray_light));
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position){
            case 0:
                startActivityForResult(CameraUtils.getOpenCameraIntent(), REQUEST_CAMERA);
                selectDialog.dismiss();
                break;
            case 1:
                PhotoPickerActivity.launcher(this,REQUEST_PICTURE,selectedCount);
                selectDialog.dismiss();
                break;
        }
    }

    @Override
    public void onDelete(RecyclerView.ViewHolder holder, String s) {
        result.remove(holder.getAdapterPosition());
        List<String> newImages = result;
        selectedCount = 0;
        for (String str:newImages) {
            if (!str.equals("add")){
                selectedCount++;
            }
        }
        if (selectedCount<9 && !newImages.contains("add"))
            newImages.add("add");
        mAdapter.setNewData(newImages);
        if (result.size()>1){
            mPublishBtn.setClickable(true);
            mPublishBtn.setTextColor(getResourceColor(R.color.text_black));
        }else {
            if (TextUtils.isEmpty(mContentEt.getText().toString().trim())){
                mPublishBtn.setClickable(false);
                mPublishBtn.setTextColor(getResourceColor(R.color.gray_light));
            }else {
                mPublishBtn.setClickable(true);
                mPublishBtn.setTextColor(getResourceColor(R.color.text_black));
            }
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter.getData().get(position).equals("add")){
            selectDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        selectedCount = 0;
        List<String> selectedImages = new ArrayList<>();
        for (String s :result) {
            if (!s.equals("add")){
                selectedImages.add(s);
                selectedCount++;
            }
        }
        if (requestCode == REQUEST_PICTURE){
            List<String> result = null;
            try {
                result = data.getStringArrayListExtra("result");
            }catch (Exception e){
                result = null;
            }
            if (result != null) {
                result.addAll(0, selectedImages);
                selectedCount = result.size();
                if (selectedCount<9)
                    result.add("add");
                this.result = result;
                mAdapter.setNewData(result);
            }
        }else if (requestCode == REQUEST_CAMERA){
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                String filePath = App.APP_PIC_PATH+System.currentTimeMillis()+".jpeg";
                ImageUtils.save(photo,filePath, Bitmap.CompressFormat.JPEG);
                List<String> result = new ArrayList<>();
                result.add(filePath);
                result.addAll(0, selectedImages);
                selectedCount = result.size();
                if (selectedCount<9)
                    result.add("add");
                this.result = result;
                mAdapter.setNewData(result);
            }

        }

        if (result.size()>1){
            mPublishBtn.setClickable(true);
            mPublishBtn.setTextColor(getResourceColor(R.color.text_black));
        }

    }




    private  void  uploadAvatar(String path) {

        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Observable<HttpResult<String>> observable = HttpClient.getApiService().uploadAvatar(part);
        RetrofitUtil.composeToSubscribe(observable, new HttpObserver<String>() {

            @Override
            public void onComplete() {

            }

            @Override
            public void onNext(String message, String data) {
                count++;
                if (TextUtils.isEmpty(fUrl)){
                    fUrl = data;
                }else
                    fUrl = fUrl + ","+data;
                if (count == result.size()-1){
                    Map<String,String> body = new HashMap<>();
                    body.put("fUrl",fUrl);
                    body.put("type","PICTURE");
                    body.put("context",mContentEt.getText().toString().trim());
                    body.put("userId",CommonUtils.getUserId());
                    LogUtil.d("params",body);
                    Observable<HttpResult> observable1 = HttpClient.getApiService().videoPic(body);
                    RetrofitUtil.composeToSubscribe(observable1, new HttpObserver() {
                        @Override
                        public void onNext(String message, Object data) {
                            CommonUtils.showMsg(message);
                            finish();
                            EventBus.getDefault().post("refresh_friend");
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
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {

            }
        },getLifeSubject());
    }

}
