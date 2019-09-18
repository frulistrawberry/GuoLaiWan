package com.guolaiwan.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.FrescoUtil;
import com.cgx.library.utils.SizeUtils;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import app.guolaiwan.com.guolaiwan.R;
import com.guolaiwan.bean.MerchantBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.RecommendBean;
import com.guolaiwan.ui.activity.MerchantInfoActivity;

import java.util.Arrays;
import java.util.List;

import io.vov.vitamio.VIntent;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/27
 * 描述:
 */

public class ViewUtils {

    public static int getRecyclerViewScrollY(RecyclerView recyclerView){
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisitableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisitableChildView.getHeight();
        return (position) * itemHeight - firstVisitableChildView.getTop();
    }

    public static void loadHomeBanner(ConvenientBanner<RecommendBean> banner, List<RecommendBean> imageMapBeans){
        if (CollectionUtils.isEmpty(imageMapBeans)){
            banner.setVisibility(View.GONE);
        }else {
            banner.setVisibility(View.VISIBLE);
            if (imageMapBeans.size() > 1){
                banner.setCanLoop(true);
                banner.startTurning(3000);
            }else {
                banner.setCanLoop(false);
            }
            banner.setPages(new CBViewHolderCreator<HomeBannerHolder>() {
                @Override
                public HomeBannerHolder createHolder() {
                    return new HomeBannerHolder();
                }

            }, imageMapBeans)
                    .setPointViewVisible(true)
                    .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);

        }
    }

    public static void loadProductBanner(ConvenientBanner<ProductBean> banner, List<ProductBean> imageMapBeans){
        if (CollectionUtils.isEmpty(imageMapBeans)){
            banner.setVisibility(View.GONE);
        }else {
            banner.setVisibility(View.VISIBLE);
            if (imageMapBeans.size()>1){
                banner.setCanLoop(true);
                banner.startTurning(3000);
            }else {
                banner.setCanLoop(false);
            }
            banner.setPages(new CBViewHolderCreator<ProductBannerHolder>() {
                @Override
                public ProductBannerHolder createHolder() {
                    return new ProductBannerHolder();
                }

            }, imageMapBeans)
                    .setPointViewVisible(true)
                    .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);

        }
    }

    public static void loadMerchantBanner(ConvenientBanner<MerchantBean> banner, List<MerchantBean> imageMapBeans){
        if (CollectionUtils.isEmpty(imageMapBeans)){
            banner.setVisibility(View.GONE);
        }else {
            banner.setVisibility(View.VISIBLE);
            if (imageMapBeans.size()>1){
                banner.setCanLoop(true);
                banner.startTurning(3000);
            }else {
                banner.setCanLoop(false);
            }
            banner.setPages(new CBViewHolderCreator<MerchantBannerHolder>() {
                @Override
                public MerchantBannerHolder createHolder() {
                    return new MerchantBannerHolder();
                }

            }, imageMapBeans)
                    .setPointViewVisible(true)
                    .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);

        }
    }

    public static void loadMerchantInfoBanner(ConvenientBanner<String> banner, String[] imageMapBeans){
        if (imageMapBeans == null || imageMapBeans.length == 0 ){
            banner.setVisibility(View.INVISIBLE);
        }else {
            banner.setVisibility(View.VISIBLE);
            if (imageMapBeans.length>1){
                banner.setCanLoop(true);
                banner.startTurning(3000);
            }else {
                banner.setCanLoop(false);
            }
            banner.setPages(new CBViewHolderCreator<MerchantInfoBannerHolder>() {
                @Override
                public MerchantInfoBannerHolder createHolder() {
                    return new MerchantInfoBannerHolder();
                }

            }, Arrays.asList(imageMapBeans))
                    .setPointViewVisible(true)
                    .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

        }
    }

    public static class MerchantInfoBannerHolder implements Holder<String> {
        private SimpleDraweeView mImageView;
        @Override
        public View createView(Context context) {
            FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mImageView= new SimpleDraweeView(context);
            mImageView.setLayoutParams(imageParams);
            mImageView.setAspectRatio(1.33f);
            GenericDraweeHierarchyBuilder builder = GenericDraweeHierarchyBuilder.newInstance(context.getResources());
            GenericDraweeHierarchy genericDraweeHierarchy = builder.
                    setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP).build();
            mImageView.setHierarchy(genericDraweeHierarchy);
            return mImageView;
        }


        @Override
        public void UpdateUI(Context context, int position, String data) {
            String tag = (String)mImageView.getTag();
            if(TextUtils.isEmpty(tag)||!tag.equals(data)){
                mImageView.setTag(data);
                FrescoUtil.getInstance().loadNetImage(mImageView,data);
            }
        }
    }


    public static class HomeBannerHolder implements Holder<RecommendBean> {
        private SimpleDraweeView mImageView;
        private FrameLayout mFrameLayout;
        private TextView mTextView;
        @Override
        public View createView(Context context) {
            FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.gravity = Gravity.BOTTOM;
            mFrameLayout = new FrameLayout(context);
            mImageView= new SimpleDraweeView(context);
            mTextView = new TextView(context);
            mFrameLayout.setLayoutParams(frameParams);
            mImageView.setLayoutParams(imageParams);
            mTextView.setLayoutParams(textParams);
            mTextView.setBackgroundColor(Color.parseColor("#88000000"));
            int padding = SizeUtils.dp2px(context,5);
            mTextView.setPadding(padding,padding,padding,padding);
            mTextView.setTextColor(Color.WHITE);
            mTextView.setMaxLines(1);
            mTextView.setEllipsize(TextUtils.TruncateAt.END);
            mImageView.setAspectRatio(1.33f);
            GenericDraweeHierarchyBuilder builder = GenericDraweeHierarchyBuilder.newInstance(context.getResources());
            GenericDraweeHierarchy genericDraweeHierarchy = builder.
                    setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP).build();
            mImageView.setHierarchy(genericDraweeHierarchy);
            mFrameLayout.addView(mImageView);
            mFrameLayout.addView(mTextView);

            return mFrameLayout;
        }


        @Override
        public void UpdateUI(final Context context, int position, RecommendBean data) {
            //String url = data.getProductShowPic();
            //final String id = data.getProductMerchantID() + "";
            final String id = data.getMerchantId();
            String url = data.getSlidepic();
            String text = data.getProductName();
            String tag = (String)mImageView.getTag();
            if(TextUtils.isEmpty(tag)||!tag.equals(url)){
                mImageView.setTag(url);
                FrescoUtil.getInstance().loadNetImage(mImageView,url);
            }
            String tag2 = (String) mTextView.getTag();
            if (TextUtils.isEmpty(tag2 )|| !tag2.equals(text)){
                mTextView.setTag(text);
                mTextView.setText(text);
            }
            String tag3 = (String) mFrameLayout.getTag();
            if (TextUtils.isEmpty(tag3) || !tag3.equals(id)){
                mFrameLayout.setTag(id);
                mFrameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MerchantInfoActivity.launch(context,id,1);
                    }
                });
            }
        }
    }

    public static class ProductBannerHolder implements Holder<ProductBean> {
        private SimpleDraweeView mImageView;
        private FrameLayout mFrameLayout;
        private TextView mTextView;
        @Override
        public View createView(Context context) {
            FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.gravity = Gravity.BOTTOM;
            mFrameLayout = new FrameLayout(context);
            mImageView= new SimpleDraweeView(context);
            mTextView = new TextView(context);
            mFrameLayout.setLayoutParams(frameParams);
            mImageView.setLayoutParams(imageParams);
            mTextView.setLayoutParams(textParams);
            mTextView.setBackgroundColor(Color.parseColor("#88000000"));
            int padding = SizeUtils.dp2px(context,5);
            mTextView.setPadding(padding,padding,padding,padding);
            mTextView.setTextColor(Color.WHITE);
            mTextView.setMaxLines(1);
            mTextView.setEllipsize(TextUtils.TruncateAt.END);
            mImageView.setAspectRatio(1.33f);
            GenericDraweeHierarchyBuilder builder = GenericDraweeHierarchyBuilder.newInstance(context.getResources());
            GenericDraweeHierarchy genericDraweeHierarchy = builder.
                    setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP).build();
            mImageView.setHierarchy(genericDraweeHierarchy);
            mFrameLayout.addView(mImageView);
            mFrameLayout.addView(mTextView);
            return mFrameLayout;
        }


        @Override
        public void UpdateUI(Context context, int position, ProductBean data) {
            String url = data.getProductShowPic();
            String text = data.getProductName();
            String tag = (String)mImageView.getTag();
            if(TextUtils.isEmpty(tag)||!tag.equals(url)){
                mImageView.setTag(url);
                FrescoUtil.getInstance().loadNetImage(mImageView,url);
            }
            String tag2 = (String) mTextView.getTag();
            if (TextUtils.isEmpty(tag2 )|| !tag2.equals(text)){
                mTextView.setTag(text);
                mTextView.setText(text);
            }
        }
    }

    public static class MerchantBannerHolder implements Holder<MerchantBean> {
        private SimpleDraweeView mImageView;
        private FrameLayout mFrameLayout;
        private TextView mTextView;
        @Override
        public View createView(Context context) {
            FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.gravity = Gravity.BOTTOM;
            mFrameLayout = new FrameLayout(context);
            mImageView= new SimpleDraweeView(context);
            mTextView = new TextView(context);
            mFrameLayout.setLayoutParams(frameParams);
            mImageView.setLayoutParams(imageParams);
            mTextView.setLayoutParams(textParams);
            mTextView.setBackgroundColor(Color.parseColor("#88000000"));
            int padding = SizeUtils.dp2px(context,5);
            mTextView.setPadding(padding,padding,padding,padding);
            mTextView.setTextColor(Color.WHITE);
            mTextView.setMaxLines(1);
            mTextView.setEllipsize(TextUtils.TruncateAt.END);
            mImageView.setAspectRatio(1.33f);
            GenericDraweeHierarchyBuilder builder = GenericDraweeHierarchyBuilder.newInstance(context.getResources());
            GenericDraweeHierarchy genericDraweeHierarchy = builder.
                    setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP).build();
            mImageView.setHierarchy(genericDraweeHierarchy);
            mFrameLayout.addView(mImageView);
            mFrameLayout.addView(mTextView);
            return mFrameLayout;
        }


        @Override
        public void UpdateUI(final Context context, int position, MerchantBean data) {
            String url = data.getShopPic();
            String text = data.getShopName();
            final String id = data.getId()+"";
            String tag = (String)mImageView.getTag();
            if(TextUtils.isEmpty(tag)||!tag.equals(url)){
                mImageView.setTag(url);
                FrescoUtil.getInstance().loadNetImage(mImageView,url);
            }
            String tag2 = (String) mTextView.getTag();
            if (TextUtils.isEmpty(tag2 )|| !tag2.equals(text)){
                mTextView.setTag(text);
                mTextView.setText(text);
            }

            String tag3 = (String) mFrameLayout.getTag();
            if (TextUtils.isEmpty(tag3) || !tag3.equals(id)){
                mFrameLayout.setTag(id);
                mFrameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MerchantInfoActivity.launch(context,id,1);
                    }
                });
            }
        }
    }

    public static View getEmptyView(Context context,int res,String msg){
        View emptyView = View.inflate(context,R.layout.layout_empty,null);
        TextView msgTv = emptyView.findViewById(R.id.tv_msg);
        ImageView emptyIv = emptyView.findViewById(R.id.iv_empty);
        emptyIv.setImageResource(res);
        msgTv.setText(msg);
        return emptyView;
    }

    public static View getErrorView(Context context){
        View errorView = View.inflate(context,R.layout.layout_empty,null);
        TextView msgTv = errorView.findViewById(R.id.tv_msg);
        ImageView emptyIv = errorView.findViewById(R.id.iv_empty);
        emptyIv.setImageResource(R.mipmap.no_network);
        msgTv.setText("搜索不到网络");
        return errorView;
    }

    public static View getLoadingView(Context context){
        View loadingView = View.inflate(context,R.layout.layout_loading,null);
        return loadingView;
    }
}
