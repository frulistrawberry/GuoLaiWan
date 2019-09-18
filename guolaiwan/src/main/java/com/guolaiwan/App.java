package com.guolaiwan;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.cgx.library.utils.AppUtils;
import com.cgx.library.utils.FileUtils;
import com.cgx.library.utils.FrescoUtil;
import com.cgx.library.utils.SDCardUtils;
import com.cgx.library.utils.SPUtils;
import com.cgx.library.utils.log.LogUtil;
import com.guolaiwan.constant.Constant;
import com.guolaiwan.constant.KeyConstant;
import com.guolaiwan.db.DBHelper;
import com.leng.jbq.ISportStepInterface;
import com.leng.jbq.TodayStepManager;
import com.leng.jbq.TodayStepService;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

import java.io.File;

import app.guolaiwan.com.guolaiwan.BuildConfig;
import mabeijianxi.camera.VCamera;
import mabeijianxi.camera.util.DeviceUtils;
import mabeijianxi.camera.util.Log;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/20
 * 描述:
 */

public class App extends MultiDexApplication {

    private static String TAG = "App";

    public static String APP_PIC_PATH = SDCardUtils.getSDCardPath() + File.separator + "过来玩" + File.separator + "images" + File.separator;

    public static String APP_FILE_PATH = SDCardUtils.getSDCardPath() + File.separator + "过来玩" + File.separator + "files" + File.separator;

    public static String APP_VOICE_PATH = SDCardUtils.getSDCardPath() + File.separator + "过来玩" + File.separator + "voices" + File.separator;

    public static String APP_VIDEO_PATH = SDCardUtils.getSDCardPath() + File.separator + "过来玩" + File.separator + "videos" + File.separator;


    private ISportStepInterface iSportStepInterface;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //Activity和Service通过aidl进行通信
            iSportStepInterface = ISportStepInterface.Stub.asInterface(service);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Config.DEBUG = true;
        MultiDex.install(this);
        AppUtils.init(this);
        SPUtils.init(Constant.SP_NAME);
        LogUtil.init(BuildConfig.DEBUG);
        FrescoUtil.init(this);
        //if (!BuildConfig.DEBUG) {
            CrashHandler handler = CrashHandler.getInstance();
            handler.init(this);
        //}

        // SPUtils.putString("userId","40");
        // 设置拍摄视频缓存路径
        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                VCamera.setVideoCachePath(dcim + "/mabeijianxi/");
            } else {
                VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/mabeijianxi/");
            }
        } else {
            VCamera.setVideoCachePath(dcim + "/mabeijianxi/");
        }
        // 开启log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(true);
        // 初始化拍摄SDK，必须
        VCamera.initialize(this);

        //初始化计步模块
        TodayStepManager.init(this);
        //开启计步Service，同时绑定Activity进行aidl通信
        Intent intent = new Intent(this, TodayStepService.class);
        startService(intent);
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
        //初始化FileDownloader
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000)
                        .readTimeout(15_000)
                ))
                .commit();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        FileUtils.createOrExistsDir(APP_FILE_PATH);
        FileUtils.createOrExistsDir(APP_PIC_PATH);
        FileUtils.createOrExistsDir(APP_VOICE_PATH);
    }

    {
        Config.DEBUG =true;
        PlatformConfig.setWeixin(KeyConstant.WX_APP_ID,KeyConstant.WX_APP_SECRET);
    }


    /*获取当前步数*/
    public int getCurrentStepCurrent() throws RemoteException {
        int step = 0;
        if(iSportStepInterface != null){
            step = iSportStepInterface.getCurrentTimeSportStep();
        }
        return step;
    }
}
