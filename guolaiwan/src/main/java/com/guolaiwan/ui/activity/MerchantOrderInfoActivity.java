package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.FrescoUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.guolaiwan.bean.AddressBean;
import com.guolaiwan.bean.OderInfoBean;
import com.guolaiwan.bean.OrderBean;
import com.guolaiwan.presenter.OrderInfoPresenter;
import com.guolaiwan.ui.iview.OrderInfoView;
import com.guolaiwan.utils.CommonUtils;

import java.text.MessageFormat;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MerchantOrderInfoActivity extends BaseActivity implements OrderInfoView{
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
    @BindView(R.id.tv_order_state)
    TextView mOrderStateTv;
    OrderInfoPresenter mPresenter;
    String orderId;
    String orderNo;


    public static void launch(Context context,String orderId){
        Intent intent = new Intent(context,MerchantOrderInfoActivity.class);
        intent.putExtra("orderId",orderId);
        context.startActivity(intent);
    }

    @OnClick({R.id.tv_pay,R.id.tv_refund,R.id.tv_receipt,R.id.tv_deliver,R.id.tv_comment,R.id.tv_refunded,R.id.tv_refundfail})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_deliver:
                mPresenter.updateStatus(orderNo,"DELIVER",null);
                //发货后关闭详情界面
                finish();
                break;
            case R.id.tv_refunded:
                mPresenter.updateStatus(orderNo,"REFUNDED",null);
                break;
            case R.id.tv_refundfail:
                mPresenter.updateStatus(orderNo,"REFUNDFAIL",null);
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
    public void closeActivity() {}

    @Override
    public void loadOrderInfo(OderInfoBean data) {
        AddressBean address = data.getAddress();
        OrderBean order = data.getOrder();
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
        //商家订单详情不显示二维码
//        if (TextUtils.isEmpty(order.getYdNO())){
//            mOrderIv.setVisibility(View.GONE);
//        }else {
//            mOrderIv.setVisibility(View.VISIBLE);
//            FrescoUtil.getInstance().loadNetImage(mOrderIv,order.getYdNO());
//        }
        mOrderNoTv.setText(MessageFormat.format("订单号:{0}", order.getOrderNO()));
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
        switch (order.getOrderState()){
//            case "未付款":
//                mBottomLayout.setVisibility(View.GONE);
//                break;
            case "支付成功":
                    mBottomLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < mBottomLayout.getChildCount(); i++) {
                        mBottomLayout.getChildAt(i).setVisibility(View.GONE);
                    }
                    mDeliverTv.setVisibility(View.VISIBLE);
                break;
            case "已发货":
                mBottomLayout.setVisibility(View.GONE);
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
                mBottomLayout.setVisibility(View.GONE);
                break;
            case "已评价":
                mBottomLayout.setVisibility(View.GONE);
                break;
        }

    }
}
