package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cgx.library.utils.CameraUtils;
import com.cgx.library.utils.ImageUtils;
import com.guolaiwan.App;
import com.guolaiwan.bean.SelectPicEvent;

import org.greenrobot.eventbus.EventBus;


public class CameraActivity extends AppCompatActivity {
    SelectPicEvent event;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = (SelectPicEvent) getIntent().getExtras().getSerializable("event");
        startActivityForResult(CameraUtils.getOpenCameraIntent(), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            String filePath = App.APP_PIC_PATH + System.currentTimeMillis()+".jpeg";
            ImageUtils.save(photo,filePath, Bitmap.CompressFormat.JPEG);
            event.img = filePath;
            EventBus.getDefault().post(event);
        }
        finish();
    }

    public static void launcher(Context context, SelectPicEvent event) {
        Intent intent = new Intent();
        intent.setClass(context,CameraActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("event",event);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
