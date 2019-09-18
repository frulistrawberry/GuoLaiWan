package com.guolaiwan.net;

import com.guolaiwan.bean.AddressBean;
import com.guolaiwan.bean.AlipayBean;
import com.guolaiwan.bean.ArticleBean;
import com.guolaiwan.bean.Child;
import com.guolaiwan.bean.CompanyBean;
import com.guolaiwan.bean.DistributorListBean;
import com.guolaiwan.bean.FilerBean;
import com.guolaiwan.bean.FriendBean;
import com.guolaiwan.bean.GuideBean;
import com.guolaiwan.bean.GuideSpotContentAndImageBean;
import com.guolaiwan.bean.HomeList;
import com.guolaiwan.bean.HomeMerchantsBean;
import com.guolaiwan.bean.LiveListBean;
import com.guolaiwan.bean.LoginBean;
import com.guolaiwan.bean.LookLiveData;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.MerchantInfoBean;
import com.guolaiwan.bean.MerchantListBean;
import com.guolaiwan.bean.OderInfoBean;
import com.guolaiwan.bean.OrderBean;
import com.guolaiwan.bean.Paise;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.ProductInfoBean;
import com.guolaiwan.bean.ProductListBean;
import com.guolaiwan.bean.ProfessionalLiveApplyCheckPriceBean;
import com.guolaiwan.bean.ProfessionalLiveCameraInfoBean;
import com.guolaiwan.bean.ProfessionalLiveDirectorBean;
import com.guolaiwan.bean.ProfessionalLiveOrderBean;
import com.guolaiwan.bean.ProfessionalLiveWatcherCheckLiveStateBean;
import com.guolaiwan.bean.RecommendBean;
import com.guolaiwan.bean.RecordVideoBean;
import com.guolaiwan.bean.ShareContentBean;
import com.guolaiwan.bean.ShopChartBean;
import com.guolaiwan.bean.ProfessionalLiveSubLiveBean;
import com.guolaiwan.bean.UpdateBean;
import com.guolaiwan.bean.UserBean;
import com.guolaiwan.bean.VoiceBean;
import com.guolaiwan.bean.VpCommentBean;
import com.guolaiwan.bean.WeChatpayBean;
import com.guolaiwan.constant.UrlConstant;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/20
 * 描述: 请求接口定义
 */

public interface ApiService {

    @GET(UrlConstant.GET_RECOMMEND)
    Observable<HttpResult<List<RecommendBean>>> getRecommend(@Query("comCode") String comCode);

    @GET(UrlConstant.GET_MODULARS)
    Observable<HttpResult<HomeList>> getModulars(@Query("comCode") String comCode);

    @GET(UrlConstant.GET_COMPANY)
    Observable<HttpResult<List<CompanyBean>>> getCompany();

    @GET(UrlConstant.GET_RETRIEVAL)
    Observable<HttpResult<List<FilerBean>>> getRetrieval(@Query("modularCode") String modularCode);

    @GET(UrlConstant.GET_RECOMMEND_BY_MODU)
    Observable<HttpResult<List<MerchantBean>>> getRecommendByModu(@Query("modularCode") String modularCode);

    @POST(UrlConstant.GET_MERCHANTS_BY_MODU)
    Observable<HttpResult<MerchantListBean>> getMerchantsByModu(@Body Map<String,Object> map);

    @GET(UrlConstant.GET_RECOMMEND_BY_ACT)
    Observable<HttpResult<List<ProductBean>>> getRecommendByAct(@Query("actId") String actId);

    @GET(UrlConstant.GET_PRODUCTS_BY_ACT)
    Observable<HttpResult<ProductListBean>> getProductsByAct(@Query("page") int page, @Query("actId") String actId);

    @GET(UrlConstant.GET_MERCHANT_INFO)
    Observable<HttpResult<MerchantInfoBean>> getMerchantInfo(@Query("merchantID")String merchantId);

    @GET(UrlConstant.GET_PRODUCTS_BY_MER)
    Observable<HttpResult<ProductListBean>> getProductsByMer(@Query("page") int page, @Query("merchantID") String merchantId);

    @GET(UrlConstant.GET_DISTRIBUTOR_INFO)
    Observable<HttpResult<MerchantInfoBean>> getDistributorInfo(@Query("distributorId")String distributorId);

    @GET(UrlConstant.GET_PRODUCTS_BY_DIS)
    Observable<HttpResult<ProductListBean>> getProductsByDis(@Query("page") int page, @Query("distributorId") String distributorId);

    @GET(UrlConstant.GET_PRODUCT_INFO)
    Observable<HttpResult<ProductInfoBean>> getProductInfo(@Query("productId") String productId,@Query("userId")String userId);

    @GET(UrlConstant.GET_DISTRIBUTOR_BY_PRO)
    Observable<HttpResult<DistributorListBean>> getDistributorByPro(@Query("productId") String productId);

    @GET(UrlConstant.GET_LIVE_LIST)
    Observable<HttpResult<List<LiveListBean>>> getLiveList(@Query("liveType") String liveType,@Query("page") int page);

    @POST(UrlConstant.GET_REGIST_CODE)
    Observable<HttpResult> getRegistCode(@Body Map<String,String> body);

    @POST(UrlConstant.GET_BIND_CODE)
    Observable<HttpResult> getBindCode(@Body Map<String,String> body);

    @POST(UrlConstant.REGIST)
    Observable<HttpResult> regist(@Body Map<String,String> body);

    @POST(UrlConstant.BIND_PHONE)
    Observable<HttpResult> bindPhone(@Body Map<String,String> body);

    @POST(UrlConstant.LOGIN)
    Observable<HttpResult<LoginBean>> login(@Body Map<String,String> body);

    @POST(UrlConstant.WECHAT_LOGIN)
    Observable<HttpResult<LoginBean>> weChatLogin(@Body Map<String,String> body);

    @POST(UrlConstant.GET_REP_PASSWORD_CODE)
    Observable<HttpResult> getRepPasswordCode(@Body Map<String,String> body);

    @POST(UrlConstant.REP_PASSWORD)
    Observable<HttpResult> repPassword(@Body Map<String,String> body);

    @POST(UrlConstant.JOIN_BASKET)
    Observable<HttpResult> joinBasket(@Body Map<String,String> body);


    @POST(UrlConstant.COLLECT_PRODUCT)
    Observable<HttpResult> collectProduct(@Body Map<String,String> body);

    @POST(UrlConstant.COMMENT_PRODUCT)
    Observable<HttpResult> commentProduct(@Body Map<String,String> body);

    @GET(UrlConstant.GET_BASKET)
    Observable<HttpResult<List<ShopChartBean>>> getBasket(@Query("userId")String userId,@Query("page")int page);

    @POST(UrlConstant.EDIT_BASKET)
    Observable<HttpResult> editBasket(@Body Map<String,String> body);

    @POST(UrlConstant.DEL_BASKET)
    Observable<HttpResult> delBasket(@Body Map<String,String> body);

    @GET(UrlConstant.GET_ADDRESS_LIST)
    Observable<HttpResult<List<AddressBean>>> getAddressList(@Query("userId")String userId);

    @POST(UrlConstant.ADD_ADDRESS)
    Observable<HttpResult> addAddress(@Body Map<String,Object> body);

    @POST(UrlConstant.ADD_ORDER)
    Observable<HttpResult<AlipayBean>> aliPayOrder(@Body Map<String,Object> body);

    @POST(UrlConstant.ADD_ORDER)
    Observable<HttpResult<WeChatpayBean>> weChatPayOrder(@Body Map<String,Object> body);

    @POST(UrlConstant.PAY_ALL_ORDER)
    Observable<HttpResult<String>> aliPayAllOrders(@Body Map<String,Object> body);

    @POST(UrlConstant.PAY_ALL_ORDER)
    Observable<HttpResult<WeChatpayBean>> weChatPayAllOrders(@Body Map<String,Object> body);

    @POST(UrlConstant.PAY_ORDER)
    Observable<HttpResult<String>> aliPayOrderType(@Body Map<String,Object> body);

    @POST(UrlConstant.PAY_ORDER)
    Observable<HttpResult<WeChatpayBean>> weChatPayOrderType(@Body Map<String,Object> body);

    @GET(UrlConstant.GET_MY_COLLECT)
    Observable<HttpResult<List<ProductBean>>> getMyCollect(@Query("userId")String userId);

    @GET(UrlConstant.GET_ORDER_LIST)
    Observable<HttpResult<List<OrderBean>>> getOrderList(@Query("userId")String userId, @Query("type") String type, @Query("uType") String uType,@Query("ifpay") String ifpay);

    @GET(UrlConstant.GET_ORDER_INFO)
    Observable<HttpResult<OderInfoBean>> getOrderInfo(@Query("orderId")String orderId);

    @GET(UrlConstant.GUIDE_HOME)
    Observable<HttpResult<HomeMerchantsBean>> getHomeMerchants(@Query("comId") String comId,@Query("modular")String modular);

    @GET(UrlConstant.GET_CHILD_BY_PRO)
    Observable<HttpResult<List<Child>>> getChilds(@Query("merchantId") String comId);

    @GET(UrlConstant.GET_VOICE)
    Observable<HttpResult<List<VoiceBean>>> getVoice(@Query("merchantId") String merchantId);

    @POST(UrlConstant.START_LIVE)
    Observable<HttpResult<LookLiveData>> startLive(@Body Map<String,String> body);

     @POST(UrlConstant.STOP_LIVE)
     Observable<HttpResult> stopLive(@Body Map<String,String> body);

    @POST(UrlConstant.ADD_PRODUCT)
    Observable<HttpResult> addProduct(@Body Map<String,String> body);

    @POST(UrlConstant.ADD_MESSAGE)
    Observable<HttpResult> addMessage(@Body Map<String,String> body);

    @GET(UrlConstant.GET_LIVE_INFO)
    Observable<HttpResult<LookLiveData>> getLiveInfo(@Query("liveId") String liveId);

    @POST(UrlConstant.CONFIRM_PRICE)
    Observable<HttpResult> confirmPrice(@Body Map<String,String> body);

    @POST(UrlConstant.DELETE_PRODUCT)
    Observable<HttpResult> delProduct(@Body Map<String,String> body);


    @GET(UrlConstant.GET_USER)
    Observable<HttpResult<UserBean>> getUser(@Query("userId")String userId);

    @POST(UrlConstant.UPDATE_ORDER_STATUS)
    Observable<HttpResult> updateOrderStatus(@Body Map<String,String> body);


    @POST(UrlConstant.CHECK_ORDER)
    Observable<HttpResult> checkOder(@Body Map<String,String> body);

    @POST(UrlConstant.SEND_PRICE)
    Observable<HttpResult> sendPrice(@Body Map<String,String> body);

    @POST(UrlConstant.SEARCH)
    Observable<HttpResult<ProductListBean>> searchProduct(@Body Map<String,String> body);

    @POST(UrlConstant.SEARCH)
    Observable<HttpResult<MerchantListBean>> searchMerchant(@Body Map<String,String> body);

    @GET(UrlConstant.GET_VIDEO_PIC)
    Observable<HttpResult<List<FriendBean>>> getVideoPic(@Query("page") int page,@Query("pageSize") int pageNum,@Query("userId")String userId ,@Query("vType")String vType,@Query("sName")String sName);

    @Multipart
    @POST(UrlConstant.UPLOAD)
    Observable<HttpResult<String>> uploadAvatar(@Part MultipartBody.Part imgs);

    @POST(UrlConstant.VIDEO_PIC)
    Observable<HttpResult> videoPic(@Body Map<String,String> body);

    @POST(UrlConstant.COMMENT_PIC)
    Observable<HttpResult> commentPic(@Body Map<String,String> body);

    @POST(UrlConstant.PRAISE_PIC)
    Observable<HttpResult<Paise>> praisePic(@Body Map<String,String> body);

    @GET(UrlConstant.COMMENT_LIST)
    Observable<HttpResult<List<VpCommentBean>>> getCommentList(@Query("userId") String userId, @Query("vpId")String id, @Query("page") int page, @Query("pageSize")int pageSize);

    @GET(UrlConstant.GET_CHILD_PIC)
    Observable<HttpResult<List<String>>> getChildPic(@Query("childId") String userId, @Query("page") int page);

    @GET("updateVersion")
    Observable<HttpResult<UpdateBean>> update();

    @GET("videoPicInfo")
    Observable<HttpResult<ArticleBean>> getPicInfo(@Query("vpId")String vpId);

    @GET(UrlConstant.DEL_PIC)
    Observable<HttpResult> delPic(@Query("vpid")String vpId,@Query("userId")String userId);

    @POST("editUser")
    Observable<HttpResult> editUser(@Body Map<String,String> body);

    @POST("goToPay")
    Observable<HttpResult<OrderBean>> goToPay(@Body Map<String,Object> body);

    @POST("delCollectionPro")
    Observable<HttpResult> delCollect(@Body Map<String,Object> body);

    @POST("convert/goldProduct")
    Observable<HttpResult<OderInfoBean>> duihuan(@Body Map<String,Object> body);

    @GET("gold/products")
    Observable<HttpResult<ProductListBean>> getGoldPruducts(@Query("userId") String userId, @Query("comId")String id, @Query("page") int page);

    @POST("order/delbyID")
    Observable<HttpResult> delById(@Body Map<String,String> body);

    @POST("deleteChildsByIds")
    Observable<HttpResult<Map<String,String>>> refreshLoginUserVisitedSpotOnServer(@Body Map<String,String> body);

    @POST("toShare")
    Observable<HttpResult<Map<String,String>>> getShareUrl(@Body ShareContentBean shareContentBean);

    //导览路线规划
    @POST("getRoad")
    Observable<HttpResult<List<Child>>> getRoad(@Body Map<String,String> params);

    //导览点详情图文数据
    @GET("getChildPicConById")
    Observable<HttpResult<Map<String,List<GuideSpotContentAndImageBean>>>> getSpotImageAndContent(@Query("voiceId") String voiceId, @Query("childId") String child);

    //收藏店铺
    @POST("mercollectionPro")
    Observable<HttpResult> collectMerchant(@Body Map<String,String> body);

    //获取店铺收藏列表
    @GET("getmerCollection")
    Observable<HttpResult<List<MerchantBean>>> getCollectedMerchant(@Query("userId")String userId);

    //删除店铺收藏
    @POST("delmerCollectionPro")
    Observable<HttpResult> delCollectedMerchant(@Body Map<String,Object> body);

    //删除收货地址
    @POST("address/delete")
    Observable<HttpResult> delAddressByAddressId(@Body Map<String,String> body);

    //专业直播核算价格
    @POST("checkPrice")
    Observable<HttpResult<ProfessionalLiveApplyCheckPriceBean>> checkPrice(@Body Map<String,String> body);

    //专业直播获取支付信息
    @POST("apply")
    Observable<HttpResult<ProfessionalLiveOrderBean>> getProfessionalLivePayInfo(@Body Map<String,String> body);

    //专业直播微信支付
    @POST("professionalLivePay")
    Observable<HttpResult<WeChatpayBean>> professionalLiveWeiChatPay(@Body Map<String,String> body);

    //专业直播ALI支付
    @POST("professionalLivePay")
    Observable<HttpResult<String>> professionalLiveALiPay(@Body Map<String,String> body);

    //专业直播修改订单状态
    @POST("professionalLiveUpdateOrderState")
    Observable<HttpResult> professionalLiveUpdateOrderState(@Body Map<String,String> body);

    //专业直播获取订单信息
    @POST("professionalLiveGetOrderInfo")
    Observable<HttpResult<ProfessionalLiveOrderBean>> professionalLiveGetOrderInfo(@Body Map<String,String> body);

    //专业直播获取机位是否可用
    @POST("professionalLivegetCameraUsable")
    Observable<HttpResult<ProfessionalLiveSubLiveBean>> professionalLiveCameraUseable(@Body Map<String,String> body);

    //专业直播机位开始直播
    @POST("professionalLiveStartSubLive")
    Observable<HttpResult<ProfessionalLiveSubLiveBean>> professionalLiveStartSubLive(@Body Map<String,String> body);

    //专业直播机位停止直播
    @POST("professionalLiveStopSubLive")
    Observable<HttpResult<String>> professionalLiveStopSubLive(@Body Map<String,String> body);

    //专业直播机位开始直播开启服务
    @POST("professionalLiveStartSubLiveService")
    Observable<HttpResult> professionalLiveStartSubLiveService(@Body Map<String,String> body);

    //专业直播机位开始直播开启服务
    @POST("professionalLiveStopSubLiveService")
    Observable<HttpResult> professionalLiveStopSubLiveService(@Body Map<String,String> body);

    //专业直播发送评论信息
    @POST("professionalLiveSendMessage")
    Observable<HttpResult> professionalLiveSendMessage(@Body Map<String,String> body);

    //专业直播导播位是否可用
    @POST("professionalLiveDirectorUsable")
    Observable<HttpResult<ProfessionalLiveDirectorBean>> professionalLiveDirectorUsable(@Body Map<String,String> body);

    //专业直播修改导播位使用状态
    @POST("professionalLiveUpdateDirectorUsable")
    Observable<HttpResult> professionalLiveUpdateDirectorUsable(@Body Map<String,String> body);

    //专业直播导播界面获取各机位信息
    @POST("professionalLiveDirectorGetEveryCameraInfo")
    Observable<HttpResult<ProfessionalLiveCameraInfoBean>> professionalLiveDirectorGetEveryCameraInfo(@Body Map<String,String> body);

    //专业直播导播界面获取各机位信息
    @POST("professionalLiveDirectorGetCameraLiveState")
    Observable<HttpResult<ProfessionalLiveSubLiveBean>> professionalLiveDirectorGetCameraLiveState(@Body Map<String,String> body);

    //专业直播上传垫播视频
    @Multipart
    @POST("uploadMatPlayVedio.do")
    Observable<HttpResult<String>> uploadMatPlayVedio(@Part MultipartBody.Part vedio);

    //专业直播导播开始/关闭直播
    @POST("professionalLiveDirectorUpdateLiveState")
    Observable<HttpResult> professionalLiveDirectorUpdateLiveState(@Body Map<String,String> body);

    //专业直播导播修改直播名称
    @POST("professionalLiveDirectorUpdateLiveName")
    Observable<HttpResult> professionalLiveDirectorUpdateLiveName(@Body Map<String,String> body);

    //专业直播观看用户获取当前主播机位URL
    @POST("professionalLiveWatcherGetLiveState")
    Observable<HttpResult<ProfessionalLiveWatcherCheckLiveStateBean>> professionalLiveWatcherGetLiveState(@Body Map<String,String> body);

    //专业直播核对导播直播状态:用于导播打开是的场景控制，导播界面最先调用的接口
    @POST("professionalLiveCheckDirectorLiveState")
    Observable<HttpResult<ProfessionalLiveWatcherCheckLiveStateBean>> professionalLiveCheckDirectorLiveState(@Body Map<String,String> body);

    //专业直播导播界面设置信号流机位接口
    @POST("professionalLiveDirectorChangeBroadCastCamera")
    Observable<HttpResult> professionalLiveDirectorChangeBroadCastCamera(@Body Map<String,String> body);

    //专业直播导播界面开启垫播服务接口
    @POST("professionalLiveStartMatPlayService")
    Observable<HttpResult> professionalLiveStartMatPlayService(@Body Map<String,String> body);

    //专业直播导播界面开始录制
    @POST("professionalLiveStartRecord")
    Observable<HttpResult> professionalLiveStartRecord(@Body Map<String,String> body);

    //专业直播导播界面停止录制
    @POST("professionalLiveStopRecord")
    Observable<HttpResult> professionalLiveStopRecord(@Body Map<String,String> body);

    //专业直播获取录制视频列表
    @POST("professionalLiveGetRecordVideoList")
    Observable<HttpResult<List<RecordVideoBean>>> professionalLiveGetRecordVideoList(@Body Map<String,String> body);

    //专业直播删除录制视频列表数据
    @POST("professionalLiveDeleteRecordVideoListItem")
    Observable<HttpResult> professionalLiveDeleteRecordVideoListItem(@Body Map<String,String> body);

    //专业直播导播更新直播机位是否可关闭状态
    @POST("professionalLiveDirectorUpdateCameraCanClose")
    Observable<HttpResult> professionalLiveDirectorUpdateCameraCanClose(@Body Map<String,String> body);

    @GET("https://www.guolaiwan.net/guolaiwan/guide/getNeededMessage")
    Observable<HttpResult<GuideBean>> getGuideInfo();
}
