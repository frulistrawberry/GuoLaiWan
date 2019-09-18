package com.guolaiwan.net;

import android.os.Handler;
import android.os.Message;

import com.guolaiwan.constant.Constant;

/**
 * Created by Administrator on 2018/5/26/026.
 */

public class DefaultProgressListener implements UploadFileRequestBody.ProgressListener {

    private Handler mHandler;
    private int size;

    //多文件上传时，index作为上传的位置的标志
    private int mIndex;

    public DefaultProgressListener(Handler mHandler, int mIndex, int size) {
        this.mHandler = mHandler;
        this.mIndex = mIndex;
        this.size = size;
    }

    @Override
    public void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish) {
        System.out.println("----the current " + hasWrittenLen + "----" + totalLen + "-----" + (hasWrittenLen * 100 / totalLen));
        int percent = (int) (hasWrittenLen * 100 / totalLen);
        if (percent > 100) percent = 100;
        if (percent < 0) percent = 0;
//        if (percent == 100) {
//            Constant.LASTSIZE += percent / size;
//        }
//        Message msg = Message.obtain();
//        if ((Constant.LASTSIZE + percent / size) > 100) {
//            msg.what = 100;
//        } else {
//            msg.what = Constant.LASTSIZE + percent / size;
//        }
//        msg.arg1 = mIndex;
//        //这里因为有多张图片，二一开始进度只是单张的，所以根据图片的size计算总的进度；
//        if (Constant.LOADSIZE < msg.what) {
//            Constant.LOADSIZE = msg.what;
//            mHandler.sendMessage(msg);
//        }
    }
}
