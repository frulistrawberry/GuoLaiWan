package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.FrescoUtil;
import com.cgx.library.utils.log.LogUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.guolaiwan.bean.AddressBean;
import com.guolaiwan.bean.LookLiveData;
import com.guolaiwan.bean.MessageBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.StartLiveBean;
import com.guolaiwan.presenter.CameraPresenter;
import com.guolaiwan.ui.adapter.MessageAdapter;
import com.guolaiwan.ui.iview.CameraView;
import com.guolaiwan.ui.widget.RefundPopup;
import com.guolaiwan.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by Administrator on 2018/5/13/013.
 */

public class PlayerActivity extends BaseActivity implements CameraView, MediaPlayer.OnBufferingUpdateListener {

    private String path = "";
    @BindView(R.id.surface_view)
    VideoView mVideoView;
    @BindView(R.id.iv_img)
    SimpleDraweeView mImageTv;
    @BindView(R.id.tv_product_name)
    TextView mProductNameTv;
    @BindView(R.id.tv_price)
    TextView mPriceTv;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.product_layout)
    LinearLayout mProductLayout;
    @BindView(R.id.add_product)
    ImageView addProductIv;
    @BindView(R.id.iv_head)
    SimpleDraweeView headIv;
    @BindView(R.id.tv_live_name)
    TextView liveNameTv;
    @BindView(R.id.tv_author_name)
    TextView authorNameTv;
    @BindView(R.id.btn_confirm)
    TextView confirmBtn;
    //正在加载控件
    @BindView(R.id.rl_loading)
    RelativeLayout mLoadingRl;

    RefundPopup mCommentPop;
    RefundPopup mSendPricePop;

    private String liveId;
    private boolean isConfirm;

    CameraPresenter mPresenter;
    MessageAdapter mAdapter;
    private String productId;
    private int price;

    public static void launch(Context context,String liveId){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
            return;
        }
        Intent intent =new Intent(context,PlayerActivity.class);
        intent.putExtra("liveId",liveId);
        context.startActivity(intent);
    }


    @OnClick({R.id.send_message,R.id.stop_live,R.id.btn_confirm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.send_message:
                mCommentPop.show(view);
                break;

            case R.id.stop_live:
                mPresenter.stopLive();
                break;

            case R.id.btn_confirm:
                if (! isConfirm)
                    mSendPricePop.show(view);
                break;
            case R.id.add_product:
                if (mProductLayout.getVisibility() == View.VISIBLE){
                    mProductLayout.setVisibility(View.GONE);
                }else {
                        mProductLayout.setVisibility(View.VISIBLE);
                }
                break;

        }
    }


    @Override
    protected void initView() {
        if (!LibsChecker.checkVitamioLibs(this)){
            return;
        }
        setContentView(R.layout.activity_player);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mProductLayout.setVisibility(View.GONE);
        initRefondPop();
        //设置播放器监听
        mVideoView.setOnBufferingUpdateListener(this);
        mPresenter.getLiveInfo(liveId);

    }

    private String addressId;
    private void initRefondPop(){
        mCommentPop=new RefundPopup(this)
                .createPopup();
        mCommentPop.setOnCancelClickListener(v -> {
            if (mCommentPop.isShowing()) {
                mCommentPop.dismiss();
            }
        });

        mCommentPop.setOnOkClickListener(v -> {
            if (mCommentPop.isShowing()) {
                String reason = mCommentPop.getContentEt().getText().toString().trim();
                if (com.cgx.library.utils.StringUtils.isEmpty(reason)){
                    CommonUtils.showMsg("请输入消息内容");
                    return;
                }
                mPresenter.addMessage(reason,liveId);
                mCommentPop.dismiss();
            }
        });
        mCommentPop.setHinit("说点儿什么吧");
        mCommentPop.setOkText("发送");

        mSendPricePop=new RefundPopup(this)
                .createPopup();
        mSendPricePop.setOnCancelClickListener(v -> {
            if (mSendPricePop.isShowing()) {
                mSendPricePop.dismiss();
            }
        });
        mSendPricePop.getContentEt().setInputType(InputType.TYPE_CLASS_NUMBER);

        mSendPricePop.setOnOkClickListener(v -> {
            if (mSendPricePop.isShowing()) {
                if (TextUtils.isEmpty(addressId)){
                    CommonUtils.showMsg("请先选择收货地址");
                    MyAddressActivity.launch(PlayerActivity.this,1,true);
                    return;
                }
                String reason = mSendPricePop.getContentEt().getText().toString().trim();
                int sendPrice = Integer.valueOf(reason)*100;
                if (com.cgx.library.utils.StringUtils.isEmpty(reason)){
                    CommonUtils.showMsg("请输入价格");
                    return;
                }
                if (sendPrice<price){
                    CommonUtils.showMsg("出价必须大于当前拍卖价格");
                    return;
                }
                mPresenter.sendPrice(liveId,productId,sendPrice+"",addressId);
                mSendPricePop.dismiss();
            }
        });
        mSendPricePop.setHinit("输入价格");
        mSendPricePop.setOkText("出价");
    }

    @Override
    protected void initData() {
        mPresenter = new CameraPresenter(this);
        mAdapter = new MessageAdapter();
        liveId = getIntent().getStringExtra("liveId");
    }

    @Override
    protected void initEvent() {}

    public void setLiveData(StartLiveBean liveData) {
        if (!TextUtils.isEmpty(liveData.getUser().getUserHeadimg())){
            FrescoUtil.getInstance().loadNetImage(headIv,liveData.getUser().getUserHeadimg());
        }else {
            FrescoUtil.getInstance().loadResourceImage(headIv,R.mipmap.ic_launcher);
        }
        if (TextUtils.isEmpty(liveData.getUser().getUserNickname())){
            authorNameTv.setText("过来玩会员_" + liveData.getUserId());
        }else {
            authorNameTv.setText(liveData.getUser().getUserNickname());
        }
        liveNameTv.setText(liveData.getLiveName()+"-直播中");
        path = "rtmp://www.guolaiwan.net/live/" + liveData.getUserId() + "A" + liveData.getId();
        mVideoView.setVideoPath(path);
        mVideoView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setLiveData(LookLiveData liveData) {}

    public void addMessage(MessageBean message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<MessageBean> data = new ArrayList<>();
                if (CollectionUtils.isEmpty(mAdapter.getData())){
                    data.add(message);
                    mAdapter.setNewData(data);
                }else
                    mAdapter.addData(message);
                mRecyclerView.smoothScrollToPosition(mAdapter.getData().size() - 1);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void showProduct(ProductBean product){
        runOnUiThread(() -> {
            productId = product.getId()+"";
            mProductNameTv.setText(product.getProductName());
            mPriceTv.setText("当前出价:"+(product.getPrice()/100)+"元");
            FrescoUtil.getInstance().loadNetImage(mImageTv,product.getHeadPic());
            addProductIv.setImageResource(R.mipmap.ic_product_list_press);
            mProductLayout.setVisibility(View.VISIBLE);
            confirmBtn.setClickable(!product.isLocked());
            isConfirm = product.isLocked();
            if (product.isLocked()){
                confirmPrice(product.getPrice());
                confirmBtn.setText("已成交");
                confirmBtn.setBackgroundDrawable(getResourceDrawable(R.drawable.shap_btn_gray));
                if (product.getUserId().equals(CommonUtils.getUserId())){
                    PayActivity.launch(getContext(),"order",product.getOrderId(),null);
                }
            }else {
                confirmBtn.setText("我要出价");
                confirmBtn.setBackgroundDrawable(getResourceDrawable(R.drawable.shap_btn_green));
            }
        });
    }


    public void changePrice(final int prices){
        runOnUiThread(() -> {
            PlayerActivity.this.price = prices;
            mPriceTv.setText("当前出价:" + price / 100 + "元");
        });
    }


    @Override
    public void confirmPrice(float price) {
        runOnUiThread(() -> {
            isConfirm = true;
            mPriceTv.setText("成交价:" + (price / 100) + "元");
        });
    }

    @Override
    public void setLookLiveData(LookLiveData data) {
        StartLiveBean liveData = data.getLiveInfo();
        setLiveData(liveData);
        ProductBean productData = data.getLiveProductInfo();
        if (productData != null) {
            showProduct(productData);
        }
    }

    @Override
    public void delePruduct() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProductLayout.setVisibility(View.GONE);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            AddressBean addressBean = (AddressBean) data.getSerializableExtra("addressBean");
            addressId = addressBean.getId() + "";
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if(percent >= 99){
            mLoadingRl.setVisibility(View.GONE);
        }
    }
}
