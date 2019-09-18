package com.guolaiwan.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import app.guolaiwan.com.guolaiwan.R;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 作者: chengx
 * 日期: 2016/11/7.
 * 描述: 图片查看器
 */
public class PhotoViewPager extends ViewPager {

    private PagerAdapter adapter;

    public PhotoViewPager(Context context) {
        super(context);
    }

    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImagUrls(List<String> imagUrls) {
        this.adapter = new PagerAdapter(getContext(),imagUrls);
        setAdapter(adapter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static class PagerAdapter extends android.support.v4.view.PagerAdapter implements PhotoViewAttacher.OnPhotoTapListener {
        private Context context;
        private List<String> imageUrls;
        private SparseArray<View> cacheView;
        private ViewGroup containerTemp;

        public PagerAdapter(Context context, List<String> imageUrls) {
            this.context = context;
            this.imageUrls = imageUrls;
            cacheView = new SparseArray<>();
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (containerTemp == null)
                containerTemp = container;
            View view = cacheView.get(position);
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_photo_view_pager,container,false);
                view.setTag(position);
                PhotoView imageView =  view.findViewById(R.id.image_photo);
                Glide.with(context).load(imageUrls.get(position)).thumbnail(0.3f).into(imageView);
                imageView.setOnPhotoTapListener(this);
            }
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public void onPhotoTap(View view, float v, float v1) {
            Activity activity = (Activity) context;
            activity.finish();
        }
    }
}
