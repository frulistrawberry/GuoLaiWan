package com.cgx.library.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.widget.TitleBar;
import com.fingdo.statelayout.StateLayout;

import butterknife.ButterKnife;
import io.reactivex.subjects.PublishSubject;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/20
 * 描述: Fragment的基类，包含Activity栈管理，状态栏颜色设置，销毁时取消网络请求等
 * 子类需要进行ButterKnife绑定
 */

public abstract class BaseFragment extends Fragment {
    protected  final String TAG = getClass().getSimpleName();
    protected BaseActivity mActivity;

    //用于控制retrofit的生命周期，以便在destroy或其他状态时终止网络请求
    private PublishSubject<LifeCycleEvent> mLifecycleSubject = PublishSubject.create();

    //根布局
    private ViewGroup mRoot;

    //标题栏
    private TitleBar mTitleBar;

    //是否第一次打开fragment
    //实现懒加载使用
    private boolean isFirstLoad = false;


    //该方法用于提供lifecycleSubject（相当于实现了IBaseView中的getLifeSubject抽象方法）。
    //方便Presenter中直接通过IBaseView获取lifecycleSubject，而不用每次都作为参数传递过去
    public PublishSubject<LifeCycleEvent> getLifeSubject() {
        return mLifecycleSubject;
    }

    public TitleBar getTitleBar(){
        return mTitleBar;
    }

    /**
     * 显示圆形进度对话框
     */
    public void showLoadingDialog() {
        mActivity.showLoadingDialog();
    }

    /**
     * 关闭进度对话框
     */
    public void dismissLoadingDialog() {
        mActivity.dismissLoadingDialog();
    }

    public void showContent(){

    }

    public void showLoading(){

    }

    public void showEmpty(){

    }

    public void showError(){

    }

    public int getResourceColor(@ColorRes int colorId) {
        return mActivity.getResourceColor(colorId);
    }

    public String getResourceString(@StringRes int stringId) {
        return mActivity.getResourceString(stringId);
    }

    public String getResourceString(@StringRes int id, Object... formatArgs) {
        return mActivity.getResourceString(id,formatArgs);
    }

    public Drawable getResourceDrawable(@DrawableRes int id) {
        return mActivity.getResourceDrawable(id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = genRootView();
        mTitleBar = new TitleBar(getContext());
        mTitleBar.setVisibility(View.GONE);
        mRoot.addView(mTitleBar);
        View contentView = createView(inflater,mRoot);
        if (contentView != null) {
            mRoot.addView(contentView);
        }
        ButterKnife.bind(this,mRoot);
        return mRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initTitle();
        initView();
        initEvent();

        //视图创建完成，将变量置为true
        isFirstLoad = true;
        //如果Fragment可见进行数据加载
        if (getUserVisibleHint()) {
            onLazyLoad();
            isFirstLoad = false;
        }
    }

    @Override
    public void onPause() {
        mLifecycleSubject.onNext(LifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        mLifecycleSubject.onNext(LifeCycleEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //视图销毁将变量置为false
        isFirstLoad = false;
    }

    @Override
    public void onDestroy() {
        mLifecycleSubject.onNext(LifeCycleEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirstLoad && isVisibleToUser) {
            //视图变为可见并且是第一次加载
            onLazyLoad();
            isFirstLoad = false;
        }
    }


    protected void initTitle(){}

    protected abstract View createView(LayoutInflater inflater,ViewGroup parent);

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initEvent();

    protected void onLazyLoad(){}

    private ViewGroup genRootView() {
        final LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.
                LayoutParams(LinearLayout.
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(params);
        return linearLayout;
    }
}
