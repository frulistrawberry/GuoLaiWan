package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.FrescoUtil;
import com.cgx.library.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.guolaiwan.bean.AddressBean;
import com.guolaiwan.bean.OderInfoBean;
import com.guolaiwan.bean.OrderBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.presenter.OrderInfoPresenter;
import com.guolaiwan.ui.iview.OrderInfoView;
import com.guolaiwan.ui.widget.CmmtPopup;
import com.guolaiwan.ui.widget.RefundPopup;
import com.guolaiwan.utils.CommonUtils;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class OrderInfoActivity extends BaseActivity implements OrderInfoView, INaviInfoCallback {

    @BindView(R.id.layout_ptr)
    PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.layout_address)
    RelativeLayout mAddressLayout;
    @BindView(R.id.tv_name)
    TextView mNameTv;
    @BindView(R.id.tv_phone)
    TextView mPhoneTv;
    @BindView(R.id.tv_address)
    TextView mAddressTv;
    @BindView(R.id.tv_shop_name)
    TextView mShopNameTv;
    @BindView(R.id.iv_img)
    SimpleDraweeView mImgIv;
    @BindView(R.id.tv_product_name)
    TextView mProductNameTv;
    @BindView(R.id.tv_price)
    TextView mPriceTv;
    @BindView(R.id.tv_count)
    TextView mProductCountTv;
    @BindView(R.id.tv_product_all_money)
    TextView mProductAllMoneyTv;
    @BindView(R.id.tv_order_all_money)
    TextView mOrdertAllMoneyTv;
    @BindView(R.id.tv_pay_money)
    TextView mPayMoneyTv;
    @BindView(R.id.layout_refund_reason)
    LinearLayout mRefundLayout;
    @BindView(R.id.tv_reasons)
    TextView mReasonTv;
    @BindView(R.id.iv_order)
    SimpleDraweeView mOrderIv;
    @BindView(R.id.layout_bottom)
    LinearLayout mBottomLayout;
    @BindView(R.id.tv_pay)
    TextView mPayTv;
    @BindView(R.id.tv_refund)
    TextView mRefundTv;
    @BindView(R.id.tv_receipt)
    TextView mReceiptTv;
    @BindView(R.id.tv_deliver)
    TextView mDeliverTv;
    @BindView(R.id.tv_comment)
    TextView mCommentTv;
    @BindView(R.id.layout_refund)
    LinearLayout refundLayout;
    @BindView(R.id.tv_order_no)
    TextView mOrderNoTv;
    @BindView(R.id.tv_create_time)
    TextView mCreateTimeTv;
    @BindView(R.id.tv_pay_time)
    TextView mPayTimeTv;
    @BindView(R.id.tv_send_time)
    TextView mSendTimeTv;
    @BindView(R.id.tv_complete_time)
    TextView mCompleteTimeTv;
    @BindView(R.id.tv_check_time)
    TextView mCheckTimeTv;
    @BindView(R.id.tv_guide)
    TextView mGuideTv;
    @BindView(R.id.tv_order_state)
    TextView mOrderStateTv;
    private CmmtPopup mCmmtPopup;
    private RefundPopup refundPopup;

    OrderInfoPresenter mPresenter;
    String orderId;
    private String productId;
    String orderNo;
    OrderBean order;

    public static void launch(Context context,String orderId){
        Intent intent = new Intent(context,OrderInfoActivity.class);
        intent.putExtra("orderId",orderId);
        context.startActivity(intent);
    }

    @OnClick({R.id.tv_pay,R.id.tv_refund,R.id.tv_receipt,R.id.tv_deliver,R.id.tv_comment,R.id.tv_refunded,R.id.tv_refundfail,R.id.tv_guide})
    public void onClick(View v){
        switch (v.getId()){

            case R.id.tv_pay:
                PayActivity.launch(this,"order",orderId,null);
                break;

            case R.id.tv_refund:
                showCmmtPop(v);
                break;

            case R.id.tv_receipt:
                mPresenter.updateStatus(orderNo,"RECEIPT",null);
                finish();
                break;

            case R.id.tv_comment:
                if (CommonUtils.isLogin())
                    mCmmtPopup.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                else
                    LoginActivity.launch(this);
                break;

            case R.id.tv_guide:
                double latitude = Double.valueOf(order.getShopLatitude());
                double longitude = Double.valueOf(order.getShopLongitude());
                Poi end = new Poi(order.getShopName(), new LatLng(latitude, longitude), "");
                AmapNaviPage.getInstance().showRouteActivity(this, new AmapNaviParams(null, null, end, AmapNaviType.DRIVER), this);
                break;
        }
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setTitle("订单详情").showBack().show();
    }

    @Override
    public void showContent() {
        super.showContent();
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_order_info);
        initCmmtPop();
        initRefondPop();
    }

    @Override
    protected void initData() {
        mPresenter = new OrderInfoPresenter(this);
        orderId = getIntent().getStringExtra("orderId");
        mPresenter.loadOrderInfo(orderId);
    }

    @Override
    protected void initEvent() {
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.loadOrderInfo(orderId);
            }
        });
    }

    @Override
    public void loadOrderInfo(OderInfoBean data) {
        AddressBean address = data.getAddress();
        order = data.getOrder();
        productId = order.getProductId() + "";
        orderNo = order.getOrderNO();
        if (address == null){
            mAddressLayout.setVisibility(View.GONE);
        }else {
            mAddressLayout.setVisibility(View.VISIBLE);
            mNameTv.setText(MessageFormat.format("收货人{0}", address.getConsigneeName()));
            mPhoneTv.setText(MessageFormat.format("  {0}", address.getConsigneePhone()));
            mAddressTv.setText(MessageFormat.format("{0} {1} {2} {3}", address.getProvince(), address.getCity(), address.getDistrict(), address.getConsigneeAddress()));
        }
        mShopNameTv.setText(order.getShopName());
        FrescoUtil.getInstance().loadNetImage(mImgIv,order.getProductPic());
        mProductNameTv.setText(order.getProductName());
        mPriceTv.setText(MessageFormat.format("￥{0}",order.getProductPrice()));
        mProductCountTv.setText(MessageFormat.format("数量x{0}", order.getProductNum()));
        mProductAllMoneyTv.setText(MessageFormat.format("￥{0}", order.getOrderAllMoney()));
        mOrdertAllMoneyTv.setText(MessageFormat.format("￥{0}", order.getOrderAllMoney()));
        mOrderStateTv.setText(order.getOrderState());
        mPayMoneyTv.setText(MessageFormat.format("￥{0}", order.getPayMoney()));
        if (TextUtils.isEmpty(order.getYdNO())){
            mOrderIv.setVisibility(View.GONE);
        }else {
            mOrderIv.setVisibility(View.VISIBLE);
            FrescoUtil.getInstance().loadNetImage(mOrderIv,order.getYdNO());
        }
        //用户显示4位单号
        //mOrderNoTv.setText(MessageFormat.format("订单号:{0}", order.getOrderNO()));
        mOrderNoTv.setText("订单号:" + order.getId());
        if (TextUtils.isEmpty(order.getCreateDate())){
            mCreateTimeTv.setVisibility(View.GONE);
        }else {
            mCreateTimeTv.setVisibility(View.VISIBLE);
            mCreateTimeTv.setText(MessageFormat.format("创建时间:{0}", order.getCreateDate()));
        }
        if (TextUtils.isEmpty(order.getPayDate())){
            mPayTimeTv.setVisibility(View.GONE);
        }else {
            mPayTimeTv.setVisibility(View.VISIBLE);
            mPayTimeTv.setText(MessageFormat.format("付款时间:{0}", order.getPayDate()));
        }
        if (TextUtils.isEmpty(order.getSendDate())){
            mSendTimeTv.setVisibility(View.GONE);
        }else {
            mSendTimeTv.setVisibility(View.VISIBLE);
            mSendTimeTv.setText(MessageFormat.format("发货时间:{0}", order.getSendDate()));
        }
        if (TextUtils.isEmpty(order.getDealDate())){
            mCompleteTimeTv.setVisibility(View.GONE);
        }else {
            mCompleteTimeTv.setVisibility(View.VISIBLE);
            mCompleteTimeTv.setText(MessageFormat.format("成交时间:{0}", order.getDealDate()));
        }
        if (TextUtils.isEmpty(order.getYdDate())){
            mCheckTimeTv.setVisibility(View.GONE);
        }else {
            mCheckTimeTv.setVisibility(View.VISIBLE);
            mCheckTimeTv.setText(MessageFormat.format("验单时间:{0}", order.getYdDate()));
        }
        mRefundLayout.setVisibility(View.GONE);
        mBottomLayout.setVisibility(View.VISIBLE);
        switch (order.getOrderState()){
//            case "未付款":
//                getTitleBar().setRightText("删除订单", view -> {
//                showLoadingDialog();
//                Map<String,String> body = new HashMap<>();
//                body.put("userId",CommonUtils.getUserId());
//                body.put("orderId",orderId);
//                RetrofitUtil.composeToSubscribe(HttpClient.getApiService().delById(body), new HttpObserver() {
//                @Override
//                public void onNext(String message, Object data1) {
//                    CommonUtils.showMsg(message);
//                    finish();
//                }
//
//                @Override
//                public void onError(int errCode, String errMessage) {
//                    CommonUtils.showMsg(errMessage);
//                }
//
//                @Override
//                public void onComplete() {
//                    dismissLoadingDialog();
//                }
//            },getLifeSubject());
//        });
//                    mBottomLayout.setVisibility(View.VISIBLE);
//                    for (int i = 0; i < mBottomLayout.getChildCount(); i++) {
//                        mBottomLayout.getChildAt(i).setVisibility(View.GONE);
//                    }
//                    mPayTv.setVisibility(View.VISIBLE);
//                break;
            case "支付成功":
                    mBottomLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < mBottomLayout.getChildCount(); i++) {
                        mBottomLayout.getChildAt(i).setVisibility(View.GONE);
                    }
                    mRefundTv.setVisibility(View.VISIBLE);
                break;
            case "已发货":
                    mBottomLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < mBottomLayout.getChildCount(); i++) {
                        mBottomLayout.getChildAt(i).setVisibility(View.GONE);
                    }
                    mReceiptTv.setVisibility(View.VISIBLE);
                break;
            case "申请退款":
                mBottomLayout.setVisibility(View.GONE);
//                    for (int i = 0; i < mBottomLayout.getChildCount(); i++) {
//                        mBottomLayout.getChildAt(i).setVisibility(View.GONE);
//                    }
//                    refundLayout.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(order.getRefundReason())){
                    mRefundLayout.setVisibility(View.VISIBLE);
                    mReasonTv.setText(order.getRefundReason());
                }
                break;
            case "退款成功":
                mBottomLayout.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(order.getRefundReason())){
                    mRefundLayout.setVisibility(View.VISIBLE);
                    mReasonTv.setText(order.getRefundReason());
                }
                break;
            case "已收货":
                mBottomLayout.setVisibility(View.VISIBLE);
                mCommentTv.setVisibility(View.VISIBLE);
                break;
            case "已评价":
                mBottomLayout.setVisibility(View.GONE);
                break;
        }
        //TODO 为啥要显示导航????
        //mGuideTv.setVisibility(View.VISIBLE);
        mBottomLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void closeActivity(){
        finish();
    }

    private void initCmmtPop(){
        mCmmtPopup=new CmmtPopup(this).createPopup();
        mCmmtPopup.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCmmtPopup.isShowing()) {
                    mCmmtPopup.dismiss();
                }
            }
        });

        mCmmtPopup.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCmmtPopup.isShowing()) {
                    String content = mCmmtPopup.getCmmtContent();
                    int star = mCmmtPopup.getCount();
                    if (StringUtils.isEmpty(content)) {
                        CommonUtils.showMsg("请输入评论内容");
                        return;
                    }
                    mPresenter.commentProduct(orderNo,orderId,productId,content,star);
                    mCmmtPopup.dismiss();
                }
            }
        });
    }

    private void initRefondPop(){
        refundPopup=new RefundPopup(this).createPopup();
        refundPopup.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (refundPopup.isShowing()) {
                    refundPopup.dismiss();
                }
            }
        });

        refundPopup.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (refundPopup.isShowing()) {
                    String reason = refundPopup.getContentEt().getText().toString().trim();
                    if (StringUtils.isEmpty(reason)){
                        CommonUtils.showMsg("请输入退款原因");
                        return;
                    }
                    mPresenter.updateStatus(orderNo,"REFUNDING",reason);
                    refundPopup.dismiss();
                }
            }
        });
    }



    private void showCmmtPop(View view){
        refundPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {}

    @Override
    public void onStartNavi(int i) {}

    @Override
    public void onCalculateRouteSuccess(int[] ints) {}

    @Override
    public void onCalculateRouteFailure(int i) {}

    @Override
    public void onStopSpeaking() {}

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    @Override
    public void onStrategyChanged(int i) {

    }

    @Override
    public View getCustomNaviBottomView() {
        return null;
    }

    @Override
    public View getCustomNaviView() {
        return null;
    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public View getCustomMiddleView() {
        return null;
    }
}
