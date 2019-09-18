package com.cgx.library.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.cgx.library.R;
import com.cgx.library.utils.KeyboardUtils;
import com.cgx.library.widget.dialog.NomalProgressDialog;
import com.fingdo.statelayout.StateLayout;

import butterknife.ButterKnife;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.utils.FrescoUtil;
import com.cgx.library.utils.log.LogUtil;
import com.cgx.library.widget.TitleBar;
import com.cgx.library.widget.dialog.ProgressDialog;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.subjects.PublishSubject;


/**
 * 作者: 陈冠希
 * 日期: 2018/3/20
 * 描述: Activity的基类，包含Activity栈管理，状态栏/导航栏颜色设置，销毁时取消网络请求等
 * 子类需要进行ButterKnife绑定
 */

public abstract class BaseActivity extends AppCompatActivity{

    protected final String TAG = getClass().getSimpleName();

    //用于控制retrofit的生命周期，以便在destroy或其他状态时终止网络请求
    private PublishSubject<LifeCycleEvent> mLifecycleSubject = PublishSubject.create();

    //返回键监听器
    private OnKeyClickListener mOnKeyClickListener;

    //"加载中"的弹窗
    private NomalProgressDialog mProgressDialog;

    //根布局
    private ViewGroup mRoot;

    //标题栏
    private TitleBar mTitleBar;


    //该方法用于提供lifecycleSubject（相当于实现了IBaseView中的getLifeSubject抽象方法）。
    //方便Presenter中直接通过IBaseView获取lifecycleSubject，而不用每次都作为参数传递过去
    public PublishSubject<LifeCycleEvent> getLifeSubject() {
        return mLifecycleSubject;
    }

    public void setOnKeyListener(OnKeyClickListener onKeyClickListener) {
        this.mOnKeyClickListener = onKeyClickListener;
    }

    public TitleBar getTitleBar(){
        return mTitleBar;
    }


    /**
     * 显示圆形进度对话框
     */
    public void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new NomalProgressDialog.Builder(this).build();
        }
        mProgressDialog.show();
    }

    /**
     * 显示圆形进度对话框:可自定义文字
     */
    public void showLoadingDialog(String text) {
        if (mProgressDialog == null) {
            mProgressDialog = new NomalProgressDialog.Builder(this).build();
        }
        mProgressDialog.setLableText(text).show();
    }

    /**
     * 关闭进度对话框
     */
    public void dismissLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
        }
    }

    public void showContent(){}

    public void showLoading(){}

    public void showEmpty(){}

    public void showError(){}

    public Context getContext() {
        return this;
    }

    public int getResourceColor(@ColorRes int colorId) {
        return ResourcesCompat.getColor(getResources(), colorId, null);
    }

    public String getResourceString(@StringRes int stringId) {
        return getResources().getString(stringId);
    }

    public String getResourceString(@StringRes int id, Object... formatArgs) {
        return getResources().getString(id, formatArgs);
    }

    public Drawable getResourceDrawable(@DrawableRes int id) {
        return ResourcesCompat.getDrawable(getResources(), id, null);
    }

    @Override
    public void setContentView(int layoutResID) {
        final View content = getLayoutInflater().inflate(layoutResID,null);
        setContentView(content,true);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view,true);
    }

    @Override
    public void onLowMemory() {
        LogUtil.e("内存不足");
        //清空图片内存缓存（包括Bitmap缓存和未解码图片的缓存）
        FrescoUtil.clearMemoryCaches();
        super.onLowMemory();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mOnKeyClickListener != null) {//如果没有设置返回事件的监听，则默认finish页面。
                    mOnKeyClickListener.clickBack();
                } else {
                    finish();
                }
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    protected void initTitle(){

    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        initTitle();
        initEvent();
    }


    @Override
    protected void onPause() {
        mLifecycleSubject.onNext(LifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        mLifecycleSubject.onNext(LifeCycleEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifecycleSubject.onNext(LifeCycleEvent.DESTROY);
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }



    private void setContentView(View contentView,boolean autoInject){
        mRoot = genRootView();
        mTitleBar = new TitleBar(this);
        mTitleBar.hide();
        mRoot.addView(mTitleBar,-1,-2);
        mRoot.addView(contentView,-1,-1);
        super.setContentView(mRoot);
        if (autoInject){
            ButterKnife.bind(this);
        }


    }


    private ViewGroup genRootView() {
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.
                LayoutParams(LinearLayout.
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(params);
        return linearLayout;
    }





    /**
     * 按键的监听，供页面设置自定义的按键行为
     */
    public interface OnKeyClickListener {
        /**
         * 点击了返回键
         */
        void clickBack();

        //可加入其它按键事件
    }
}
