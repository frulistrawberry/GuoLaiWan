package com.guolaiwan.net;

import com.cgx.library.net.RetrofitUtil;
import com.guolaiwan.constant.UrlConstant;

import java.util.IllegalFormatCodePointException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/20
 * 描述:
 */
public class HttpClient {
    private static ApiService mApiService;//提供各种具体的网络请求
    private static WXApiService mWXApiService;
    private static final int DEFAULT_TIMEOUT = 15;//请求超时时长，单位秒

    public static ApiService getApiService() {
        if (mApiService == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            //设置请求超时时长
            okHttpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            //启用Log日志
            okHttpClientBuilder.addInterceptor(RetrofitUtil.getHttpLoggingInterceptor());
            //设置缓存方式、时长、地址
            okHttpClientBuilder.addNetworkInterceptor(RetrofitUtil.getCacheInterceptor());
//            okHttpClientBuilder.addInterceptor(RetrofitUtil.getCacheInterceptor());
//            okHttpClientBuilder.cache(RetrofitUtil.getCache());
            //设置https访问(验证证书)
//            okHttpClientBuilder.sslSocketFactory(getSSLSocketFactory(mContext, new int[]{R.raw.tomcat}));//请把服务器给的证书文件放在R.raw文件夹下
//            okHttpClientBuilder.hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            //设置统一的header
            okHttpClientBuilder.addInterceptor(RetrofitUtil.getHeaderInterceptor());

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(UrlConstant.HOST)
                    //配置转化库，采用GsonHttpObserver
                    .addConverterFactory(GsonConverterFactory.create())
                    //配置回调库，采用RxJava
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    //设置OKHttpClient为网络客户端
                    .client(okHttpClientBuilder.build()).build();

            mApiService = retrofit.create(ApiService.class);
            return mApiService;
        } else {
            return mApiService;
        }
    }
    public static WXApiService getWXApiService(){
        if (mWXApiService == null){
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        //设置请求超时时长
        okHttpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //启用Log日志
        okHttpClientBuilder.addInterceptor(RetrofitUtil.getHttpLoggingInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlConstant.WX_HOST)
                //配置转化库，采用XmlHttpObserver
                .addConverterFactory(SimpleXmlConverterFactory.create())
                //配置回调库，采用RxJava
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //设置OKHttpClient为网络客户端
                .client(okHttpClientBuilder.build()).build();

        mWXApiService = retrofit.create(WXApiService.class);
        return mWXApiService;
    } else {
        return mWXApiService;
    }
    }
}
