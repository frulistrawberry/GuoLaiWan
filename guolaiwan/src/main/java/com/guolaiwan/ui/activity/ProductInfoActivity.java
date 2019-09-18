package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.FrescoUtil;
import com.cgx.library.utils.ScreenUtils;
import com.cgx.library.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import app.guolaiwan.com.guolaiwan.R;

import com.guolaiwan.bean.AddressBean;
import com.guolaiwan.bean.CommentBean;
import com.guolaiwan.bean.DistributorBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.ProductInfoBean;
import com.guolaiwan.presenter.ProductInfoPresenter;
import com.guolaiwan.ui.adapter.DistributorAdapter;
import com.guolaiwan.ui.iview.ProductInfoView;
import com.guolaiwan.ui.widget.CmmtPopup;
import com.guolaiwan.ui.widget.RefundPopup;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ViewUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.MessageFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;



public class ProductInfoActivity extends BaseActivity implements ProductInfoView{
    @BindView(R.id.layout_title_black)
    public RelativeLayout mBlackTitle;
    @BindView(R.id.layout_ptr)
    public PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;
    @BindView(R.id.iv_collect)
    ImageView mCollectIv;
    @BindView(R.id.iv_collect1)
    ImageView mCollectIv1;
    public View mHeaderView;
    public ConvenientBanner<String> mBannerView;
    public TextView mProductNameTv;
    public TextView mSellCountTv;
    public WebView mShopDescTv;
    public TextView mShopOpenTimeTv;
    public TextView mCommentCountTv;
    public LinearLayout mCommentLayout;
    public LinearLayout mCommentEmptyLayout;
    public LinearLayout mCommentParentLayout;
    private CmmtPopup mCmmtPopup;
    RefundPopup mCommentPop;

    private String mDistributorId;
    private ProductInfoPresenter mPresenter;
    private DistributorAdapter mAdapter;
    private boolean isDistribution;
    private int productNum = 1;

    private TextView addTv;
    private TextView delTv;
    private TextView countTv;

    public static void launch(Context context,String distributorId){
        Intent intent = new Intent(context,ProductInfoActivity.class);
        intent.putExtra("distributorId",distributorId);
        context.startActivity(intent);
    }
    //一元购进入商品详情方法
    private String mActivityType;
    public static void launch(Context context,String distributorId,String activityType){
        Intent intent = new Intent(context,ProductInfoActivity.class);
        intent.putExtra("distributorId",distributorId);
        intent.putExtra("activityType",activityType);
        context.startActivity(intent);
    }

    @OnClick({R.id.iv_back,R.id.iv_back1,
            R.id.tv_join_basket,R.id.tv_pay,
            R.id.iv_comment,R.id.iv_comment1,
            R.id.iv_collect1,R.id.iv_collect,R.id.shopBtn})
    public void onClick(View v){
        if (v.getId() == R.id.iv_back1 || v.getId() == R.id.iv_back){
            finish();
        }else if (v.getId() == R.id.tv_join_basket){
            if (!CommonUtils.isLogin()){
                LoginActivity.launch(this);
                return;
            }

            if (isDistribution) {
                MyAddressActivity.launch(this,2,true);
            }else{
                //TODO 一元购活动：判断活动开始或者结束
                mPresenter.addToShopCart(mDistributorId,null);
            }
        }else if (v.getId() == R.id.tv_pay){
            if (isDistribution){
                MyAddressActivity.launch(this,1,true);
            }else {
                //TODO  一元购活动：判断活动开始或者结束

                //PayActivity.launch(this,"product",mDistributorId,null,productNum);
                //需先跳转商品信息界面，在商品信息界面选择联系人后完成支付
                BuyNowActivity.launch(this,mDistributorId,productNum,mProductBean);
            }
        }else if (v.getId() == R.id.iv_comment1 || v.getId() == R.id.iv_comment){
            if (CommonUtils.isLogin())
                mCmmtPopup.showAtLocation(v, Gravity.BOTTOM, 0, 0);
            else
                LoginActivity.launch(this);
        }else if (v.getId() == R.id.iv_collect || v.getId() == R.id.iv_collect1){
            if (CommonUtils.isLogin())
                mPresenter.collectProduct(mDistributorId);
            else
                LoginActivity.launch(this);
        }else if (v.getId() == R.id.shopBtn) {
            ShopCartActivity.launch(this);
        }
    }

    @Override
    public void showContent() {
        super.showContent();
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showEmpty() {
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
    }

    @Override
    public void showError() {
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_product_info);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.layout_product_info,mRecyclerView,false);
        mBannerView = mHeaderView.findViewById(R.id.banner);
        mProductNameTv = mHeaderView.findViewById(R.id.tv_product_name);
        mSellCountTv = mHeaderView.findViewById(R.id.tv_sell_count);
        mCommentCountTv = mHeaderView.findViewById(R.id.tv_comment_count);
        mShopDescTv = mHeaderView.findViewById(R.id.tv_shop_desc);
        mShopOpenTimeTv = mHeaderView.findViewById(R.id.tv_open_time);
        mCommentLayout = mHeaderView.findViewById(R.id.layout_comment);
        mCommentEmptyLayout = mHeaderView.findViewById(R.id.layout_comment_empty);
        mCommentParentLayout = mHeaderView.findViewById(R.id.comment_parent);
        addTv = mHeaderView.findViewById(R.id.tv_add);
        countTv = mHeaderView.findViewById(R.id.tv_count);
        delTv = mHeaderView.findViewById(R.id.tv_del);
        mAdapter.addHeaderView(mHeaderView);
        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productNum++;
                countTv.setText(""+productNum);
            }
        });

        delTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productNum--;
                if (productNum<=0){
                    productNum = 1;
                }
                countTv.setText(productNum+"");
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.refreshData(mDistributorId);
        initCmmtPop();
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
                    CommonUtils.showMsg("购买数量");
                    return;
                }
                productNum = Integer.valueOf(mCommentPop.getContentEt().getText().toString());

                mCommentPop.dismiss();
            }
        });
        mCommentPop.setHinit("请输入购买数量");
        mCommentPop.setOkText("购买");
        mCommentPop.getContentEt().setInputType(InputType.TYPE_CLASS_NUMBER);


    }

    @Override
    protected void initData() {
        mDistributorId = getIntent().getStringExtra("distributorId");
        //一元购进入商品详，携带activityTyoe
        if(getIntent().getStringExtra("activityType") != null){
            mActivityType = getIntent().getStringExtra("activityType");
        }
        mPresenter = new ProductInfoPresenter(this);
        mAdapter = new DistributorAdapter();
        mAdapter.setHeaderAndEmpty(true);
    }

    @Override
    protected void initEvent() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int scrollY = ViewUtils.getRecyclerViewScrollY(recyclerView);
                if (mBannerView.getBottom()-scrollY<0){
                    mBlackTitle.setAlpha(1.0f);
                }else {
                    mBlackTitle.setAlpha(0);
                }
            }
        });

        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.refreshData(mDistributorId);
            }
        });
    }

    private ProductBean mProductBean;
    @Override
    public void loadProductInfo(ProductInfoBean productInfo) {
        ProductBean product = productInfo.getProduct();
        mProductBean = productInfo.getProduct();
        mProductNameTv.setText(String.format("%s ￥%s", product.getProductName(), product.getProductPrice()));
        mSellCountTv.setText(MessageFormat.format("销量：{0}", product.getProductSaleNum()));
        mCommentCountTv.setText(MessageFormat.format("评价 ({0})", productInfo.getCommentCount()));
        String webContent  = getFormatWebString(productInfo.getProduct().getProductIntroduce());
        mShopDescTv.loadDataWithBaseURL(null,webContent,"text/html","utf-8",null);
        mShopOpenTimeTv.setText("");
        String pics = product.getProductMorePic();
        if (!StringUtils.isEmpty(pics)){
            String[] imgUrls = pics.split(",");
            ViewUtils.loadMerchantInfoBanner(mBannerView,imgUrls);

        }
        isDistribution = productInfo.getProduct().getProductType() == 1;
        if (productInfo.getProduct().getIfcollection() == 1){
            mCollectIv.setImageResource(R.mipmap.ic_collect_white);
            mCollectIv1.setImageResource(R.mipmap.ic_collect_black);
        }else {
            mCollectIv.setImageResource(R.mipmap.ic_un_collect_white);
            mCollectIv1.setImageResource(R.mipmap.ic_un_collect_black);
        }

    }

    /*
    * 格式化web字符串
    * 解决图片不能自适应屏幕的bug
    */
    private String getFormatWebString(String data) {
        Document document = Jsoup.parse(data);

        Elements pElements = document.select("p:has(img)");
        for (Element pElement : pElements) {
            pElement.attr("style", "text-align:center");
            pElement.attr("max-width", String.valueOf(ScreenUtils.getScreenWidth(getContext()) + "px"))
                    .attr("height", "auto");
        }
        Elements imgElements = document.select("img");
        for (Element imgElement : imgElements) {
            //重新设置宽高
            imgElement.attr("max-width", "100%")
                    .attr("height", "auto");
            imgElement.attr("style", "max-width:100%;height:auto");
        }
        return document.toString();
    }

    @Override
    public void loadComments(List<CommentBean> comments) {
        if (CollectionUtils.isEmpty(comments)){
            mCommentEmptyLayout.setVisibility(View.VISIBLE);
            mCommentLayout.setVisibility(View.GONE);
        }else {
            mCommentEmptyLayout.setVisibility(View.GONE);
            mCommentLayout.setVisibility(View.VISIBLE);
            mCommentParentLayout.removeAllViews();
            for (int i = 0; i < comments.size(); i++) {
                CommentBean comment = comments.get(i);
                View commentView = LayoutInflater.from(this).inflate(R.layout.item_comment_list,mCommentParentLayout,false);
                SimpleDraweeView imageView = commentView.findViewById(R.id.iv_img);
                TextView commentContentTv = commentView.findViewById(R.id.tv_comment_content);
                TextView nickNameTv = commentView.findViewById(R.id.tv_nick_name);
                TextView timeTv = commentView.findViewById(R.id.tv_time);
                TextView replyTv = commentView.findViewById(R.id.tv_reply);
                LinearLayout replyLayout = commentView.findViewById(R.id.layout_reply);
                LinearLayout levelLayout = commentView.findViewById(R.id.layout_level);
                View divider = commentView.findViewById(R.id.v_divider);
                if (i==0){
                    divider.setVisibility(View.GONE);
                }else {
                    divider.setVisibility(View.VISIBLE);
                }
                FrescoUtil.getInstance().loadNetImage(imageView,comment.getUserHeadimg());
                nickNameTv.setText(comment.getUserName());
                commentContentTv.setText(comment.getContent());
                timeTv.setText(comment.getUserDate());
                if (StringUtils.isEmpty(comment.getMerContent())){
                    replyLayout.setVisibility(View.GONE);
                }else {
                    replyLayout.setVisibility(View.VISIBLE);
                    replyTv.setText(comment.getMerContent());
                }
                //TODO 小星星先不加需要和公众号对应，公众号暂时没有小星星
//                if(comment.getStart() > 0){
//                    for (int j = 0; j < comment.getStart() / 2; j++) {
//                        if(levelLayout.getChildAt(i) != null){
//                            ((ImageView)levelLayout.getChildAt(i)).setImageResource(R.mipmap.star);
//                        }
//                    }
//                }
                mCommentParentLayout.addView(commentView);
            }
        }
    }

    @Override
    public void loadDistributor(List<DistributorBean> distributors) {
        mAdapter.setNewData(distributors);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            AddressBean addressBean = (AddressBean) data.getSerializableExtra("addressBean");
            String addressId = addressBean.getId() + "";
            if (requestCode == 2){
                mPresenter.addToShopCart(mDistributorId,addressId);
            }else if (requestCode == 1){
                PayActivity.launch(this,"product",mDistributorId,addressId,productNum);
            }
        }
    }

    private void initCmmtPop(){
        mCmmtPopup=new CmmtPopup(this)
                .createPopup();
        mCmmtPopup.setOnCancelClickListener(v -> {
            if (mCmmtPopup.isShowing()) {
                mCmmtPopup.dismiss();
            }
        });

        mCmmtPopup.setOnOkClickListener(v -> {
            if (mCmmtPopup.isShowing()) {
                String content = mCmmtPopup.getCmmtContent();
                int star = mCmmtPopup.getCount();
                if (StringUtils.isEmpty(content)) {
                    CommonUtils.showMsg("请输入评论内容");
                    return;
                }
                mPresenter.commentProduct(mDistributorId,content,star);
                mCmmtPopup.dismiss();
            }
        });
    }
}
