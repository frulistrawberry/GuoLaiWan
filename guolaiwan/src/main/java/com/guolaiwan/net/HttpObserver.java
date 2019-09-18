package com.guolaiwan.net;

import com.cgx.library.net.ExceptionHandler;
import com.guolaiwan.utils.CommonUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.vov.vitamio.utils.Log;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/20
 * 描述:
 */

public abstract class HttpObserver<T> implements Observer<HttpResult<T>> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof Exception) {
            //访问获得对应的Exception
            ExceptionHandler.ResponseThrowable responseThrowable = ExceptionHandler.handleException(e);
            CommonUtils.error(responseThrowable.code,responseThrowable.message);
            onError(responseThrowable.code, responseThrowable.message);
        } else {
            //将Throwable 和 未知错误的status code返回
            ExceptionHandler.ResponseThrowable responseThrowable = new ExceptionHandler.ResponseThrowable(e, ExceptionHandler.ERROR.UNKNOWN);
            CommonUtils.error(responseThrowable.code,responseThrowable.message);
            onError(responseThrowable.code, responseThrowable.message);
        }
    }

    @Override
    public void onNext(HttpResult<T> httpResult) {
        //如果没失效，则正常回调
        Log.d("result",httpResult);
        if (httpResult.isSuccess()) {
            onNext(httpResult.getMessage(), httpResult.getData());
        } else {
            CommonUtils.error(httpResult.getStatus(),httpResult.getMessage());
            onError(httpResult.getStatus(),httpResult.getMessage());
        }
    }

    //具体实现下面两个方法，便可从中得到更直接详细的信息
    public abstract void onNext(String message, T data);
    public abstract void onError(int errCode, String errMessage);
}
