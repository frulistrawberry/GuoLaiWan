package com.guolaiwan.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.autonavi.rtbt.IFrameForRTBT;
import com.bumptech.glide.Glide;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.CameraUtils;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.FileUtils;
import com.cgx.library.utils.ImageUtils;
import com.cgx.library.utils.StringUtils;
import com.cgx.library.utils.log.LogUtil;
import com.google.gson.Gson;
import com.guolaiwan.App;
import com.guolaiwan.bean.RichUpload;
import com.guolaiwan.bean.SelectPicEvent;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.ui.adapter.RichPublishAdapter;
import com.guolaiwan.ui.widget.DialogOnItemClickListener;
import com.guolaiwan.ui.widget.NormalSelectDialog;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ImageFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.guolaiwan.ui.activity.UpLoadActivity.getRealPathFromUri;

/**
 * Created by Administrator on 2018/5/28/028.
 */

public class RichPublicActivity extends BaseActivity implements DialogOnItemClickListener, RichPublishAdapter.Listener {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_add_title)
    TextView mAddTitleTv;
    @BindView(R.id.iv_cover)
    ImageView mCoverIv;
    private NormalSelectDialog selectDialog;
    private NormalSelectDialog selectDialog1;
    SelectPicEvent event;
    RichPublishAdapter adapter;
    View footer;
    private RichUpload context = new RichUpload();
    private List<RichUpload.Content> contents = new ArrayList<>();

    @OnClick({R.id.iv_add_cover,R.id.tv_add_title,R.id.tv_publish,R.id.add_music})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_add_cover:
                event = new SelectPicEvent();
                event.tag = "cover";
                selectDialog.show();
                break;
            case R.id.tv_add_title:
                event = new SelectPicEvent();
                event.tag = "cover";
                AddTextActivity.launcher(this,event);
                break;
            case R.id.add_music:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/x-mpeg");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,3);
                break;
            case R.id.tv_publish:
                if (StringUtils.isEmpty(context.title)){
                    CommonUtils.showMsg("请输入标题");
                    return;
                }
                if (StringUtils.isEmpty(context.cover)){
                    CommonUtils.showMsg("请上传封面");
                    return;
                }
                if (CollectionUtils.isEmpty(contents)){
                    CommonUtils.showMsg("请添加内容");
                    return;
                }
                List<RichUpload.Content> realContent = new ArrayList<>();
                for (RichUpload.Content content : contents) {
                    if (!StringUtils.isEmpty(content.img)||!StringUtils.isEmpty(content.text)){
                        realContent.add(content);
                    }
                }
                context.content = realContent;
                String context = new Gson().toJson(this.context);
                showLoadingDialog();
                Map<String,String> body = new HashMap<>();
                body.put("fUrl","");
                body.put("type","PICTURE");
                body.put("context",context);
                body.put("sName",this.context.title);
                body.put("headPic",this.context.cover);
                body.put("userId",CommonUtils.getUserId());
                if (!TextUtils.isEmpty(this.context.music)){
                    body.put("murl",this.context.music);
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
        }
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setLeftText("取消", v -> finish()).setTitle("发布图文").show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_rich_public);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        footer = LayoutInflater.from(this).inflate(R.layout.footer_add,mRecyclerView,false);
        footer.setOnClickListener(v -> selectDialog1.show());
        adapter.addFooterView(footer);
        adapter.setListener(this);
        mRecyclerView.setAdapter(adapter);

        selectDialog = new NormalSelectDialog.Builder(this).setOnItemClickListener(this).build();
        selectDialog1 = new NormalSelectDialog.Builder(this).setOnItemClickListener(new DialogOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RichUpload.Content content = new RichUpload.Content();
                switch (position){
                    case 0:
                        content.type = 1;
                        contents.add(content);
                        adapter.setNewData(contents);
                        break;
                    case 1:
                        content.type = 2;
                        contents.add(content);
                        adapter.setNewData(contents);
                        break;
                }
                selectDialog1.dismiss();
            }
        }).build();
        selectDialog.setDataList(Arrays.asList("拍照","选择图片"));
        selectDialog1.setDataList(Arrays.asList("文字","图片"));
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        adapter = new RichPublishAdapter();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position){
            case 0:
                CameraActivity.launcher(this,event);
                selectDialog.dismiss();
                break;
            case 1:
                PhotoPickerActivity.launcher(this,event);
                selectDialog.dismiss();
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(SelectPicEvent event){
        runOnUiThread(() -> {
            if (event.tag.equals("cover")){
                if (!StringUtils.isEmpty(event.img))
                    uploadAvatar(event);
                String title = event.text;
                if (!StringUtils.isEmpty(title)){
                    context.title = title;
                }
                if (StringUtils.isEmpty(context.title)){
                    mAddTitleTv.setText("点击添加标题");
                }else {
                    mAddTitleTv.setText(context.title);
                }
            }else if(event.tag.equals("other")){
                if (!StringUtils.isEmpty(event.img))
                    uploadAvatar(event);
                if (!StringUtils.isEmpty(event.text)){
                    if (contents.size()>event.position){
                        contents.get(event.position).text = event.text;
                        adapter.notifyDataSetChanged();
                    }
                }

            }else if (event.tag.equals("music")){
                if (!StringUtils.isEmpty(event.img))
                    uploadAvatar(event);
            }
        });

    }

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


    private  void  uploadAvatar(SelectPicEvent path) {
        showLoadingDialog();
        new Thread(() -> {
            File tumb =null;

            if (!path.tag.equals("music")){
                ImageFactory factory = new ImageFactory();
                tumb = new File(App.APP_PIC_PATH+System.currentTimeMillis()+".jpg");
                compressImageToFile(factory.getBitmap(path.img),tumb);
            }else
                tumb = new File(path.img);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), tumb);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", tumb.getName(), requestFile);
            Observable<HttpResult<String>> observable = HttpClient.getApiService().uploadAvatar(part);
            File finalTumb = tumb;
            RetrofitUtil.composeToSubscribe(observable, new HttpObserver<String>() {

                @Override
                public void onComplete() {
                    dismissLoadingDialog();
                    if (!path.tag.equals("music"))
                        FileUtils.deleteFile(finalTumb);
                }

                @Override
                public void onNext(String message, String data) {
                    if (path.tag.equals("cover")){
                        context.cover = data;
                        runOnUiThread(() -> Glide.with(RichPublicActivity.this).load(context.cover).centerCrop().into(mCoverIv));
                    }else if (path.tag.equals("music")){
                        context.music = data;
                    } else {
                        if (path.position<contents.size()){
                            contents.get(path.position).img = data;
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onError(int errCode, String errMessage) {
                    CommonUtils.showMsg("上传失败请重新选择图片");
                }
            },getLifeSubject());

        }).run();

    }


    @Override
    public void clickImg(int position) {
        event = new SelectPicEvent();
        event.tag = "other";
        event.position = position;
        selectDialog.show();
    }

    @Override
    public void clickText(int position) {
        event = new SelectPicEvent();
        event.tag = "other";
        event.position = position;
        event.text = contents.get(position).text;
        AddTextActivity.launcher(this,event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null && data.getData()!=null){

            Uri uri = data.getData();
            //视频文件路径
            String path = getRealPathFromUri(this,uri);
            event = new SelectPicEvent();
            event.tag = "music";
            event.img = path;
            EventBus.getDefault().post(event);

        }
    }
}
