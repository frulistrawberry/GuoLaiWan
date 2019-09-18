package com.guolaiwan.aliapi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.guolaiwan.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class AliApi {

    private static final int SDK_PAY_FLAG = 1;

    private static AliApi mInstance;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case SDK_PAY_FLAG: {
                        @SuppressWarnings("unchecked")
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        String resultInfo = payResult.getResult();
                        String resultStatus = payResult.getResultStatus();
                        if (TextUtils.equals(resultStatus, "9000")) {
                            CommonUtils.showMsg("支付成功");
                            EventBus.getDefault().post("closePayActivity");
                        } else {
                            CommonUtils.showMsg("支付失败");
                        }
                        break;
                    }
                }
                return false;
            }
        }
    );


    public static synchronized AliApi getInstance(){
        if (mInstance == null)
            mInstance = new AliApi();
        return mInstance;
    }


    public void pay(final Activity context, final String orderInfo){

        if (!checkAliPayInstalled(context)){
            CommonUtils.showMsg("请先安装支付宝");
            return;
        }
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                Map<String, String> result = alipay.payV2(orderInfo, false);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public  boolean checkAliPayInstalled(Context context) {

        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }
}