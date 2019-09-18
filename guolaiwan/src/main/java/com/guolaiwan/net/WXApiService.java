package com.guolaiwan.net;

import com.guolaiwan.bean.WXResp;
import com.guolaiwan.constant.UrlConstant;


import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/27
 * 描述:
 */

public interface WXApiService {
    @POST(UrlConstant.UNIFIED_ORDER)
    Observable<WXResp> unifiedOrder(@Body Map<String,Object> params);
}
