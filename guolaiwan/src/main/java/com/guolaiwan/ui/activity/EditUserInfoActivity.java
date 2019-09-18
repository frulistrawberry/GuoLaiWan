package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.FrescoUtil;
import com.cgx.library.utils.SPUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.utils.CommonUtils;

import java.io.File;
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

/**
 * 作者: 陈冠希
 * 日期: 2018/4/27
 * 描述:
 */

public class EditUserInfoActivity extends BaseActivity{

    @BindView(R.id.iv_img)
    SimpleDraweeView imageView;
    String headImage;
    @BindView(R.id.nickNameEt)
    EditText nickNameEt;
    @BindView(R.id.phoneEt)
    EditText phoneEt;
    @BindView(R.id.realNameEt)
    EditText realNameEt;
    @BindView(R.id.companyEt)
    EditText companyEt;
    String nickName;
    String realName;
    String phone;
    String company;

    public static void launch(Context context){
        context.startActivity(new Intent(context,EditUserInfoActivity.class));
    }

    @OnClick({R.id.iv_img})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_img:
                PhotoPickerActivity.launcher(this,1,1);
                break;
        }
    }

    @OnClick(R.id.btn_edit_user_info_save)
    public void save(View v){
        if (TextUtils.isEmpty(nickNameEt.getText())){
            CommonUtils.showMsg("请填写昵称");
            return;
        }
        if (TextUtils.isEmpty(phoneEt.getText())){
            CommonUtils.showMsg("请填写联系方式");
            return;
        }
        if (TextUtils.isEmpty(realNameEt.getText())){
            CommonUtils.showMsg("请填写真实姓名");
            return;
        }
        if (TextUtils.isEmpty(companyEt.getText())){
            CommonUtils.showMsg("请填写工作单位");
            return;
        }
        String nickName = nickNameEt.getText().toString();
        String phone = phoneEt.getText().toString();
        String realName = realNameEt.getText().toString();
        String company = companyEt.getText().toString();
        Map<String,String> body = new HashMap<>();
        body.put("userPhone",phone);
        body.put("userHeadimg",headImage);
        body.put("userNickname",nickName);
        body.put("trueName",realName);
        body.put("companyName",company);
        body.put("userId",CommonUtils.getUserId());
        Observable<HttpResult> observable = HttpClient.getApiService().editUser(body);
        showLoadingDialog();
        RetrofitUtil.composeToSubscribe(observable, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                SPUtils.putString("phone",phone);
                SPUtils.putString("nickName",nickName);
                SPUtils.putString("realName",realName);
                SPUtils.putString("company",company);
                SPUtils.putString("headImage",headImage);
                CommonUtils.showMsg(message);
                finish();
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.showMsg(errMessage);
            }

            @Override
            public void onComplete() {
                dismissLoadingDialog();
            }
        },getLifeSubject());
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setTitle("完善个人信息").showBack().show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_edit_user_info);
    }

    @Override
    protected void initData() {
        phone = SPUtils.getString("phone","");
        nickName = SPUtils.getString("nickName","");
        realName = SPUtils.getString("realName","");
        company = SPUtils.getString("company","");
        headImage = SPUtils.getString("headImage","");
    }

    @Override
    protected void initEvent() {
        phoneEt.setText(phone);
        nickNameEt.setText(nickName);
        realNameEt.setText(realName);
        companyEt.setText(company);
        FrescoUtil.getInstance().loadNetImage(imageView,headImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;

        if (requestCode == 1){
            List<String> result = null;
            try {
                result = data.getStringArrayListExtra("result");
            }catch (Exception e){
                result = null;
            }
            if (result != null) {
                String path = result.get(0);
                File file = new File(path);
                showLoadingDialog();
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                Observable<HttpResult<String>> observable = HttpClient.getApiService().uploadAvatar(part);
                RetrofitUtil.composeToSubscribe(observable, new HttpObserver<String>() {
                    @Override
                    public void onNext(String message, String data) {
                        FrescoUtil.getInstance().loadNetImage(imageView,data);
                        headImage = data;
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
        }
    }
}
