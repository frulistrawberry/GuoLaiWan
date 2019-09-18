package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;


import com.cgx.library.base.BaseActivity;
import com.guolaiwan.ui.widget.CircleIndicator;
import com.guolaiwan.ui.widget.PhotoViewPager;

import java.util.ArrayList;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;

/**
 * 作者: chengx
 * 日期: 2016/11/7.
 * 描述: 图片浏览页面
 */
public class ImageBrowserActivity extends BaseActivity {

    /*======= 控件声明区 =======*/
    private PhotoViewPager viewPager;
    private CircleIndicator indicator;
    /*========================*/

    private List<String> imageUrls;

    private int position;
    public static void launcher(Context context, ArrayList<String> imageUrls, int position){
        Intent intent = new Intent();
        intent.setClass(context,ImageBrowserActivity.class);
        intent.putStringArrayListExtra("imageUrls",  imageUrls);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
    public static void launcher(Context context, ArrayList<String> imageUrls){
       launcher(context,imageUrls,0);
    }

    @Override
    protected void initView() {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_browser);
        viewPager =  findViewById(R.id.pager_image_browser);
        indicator =  findViewById(R.id.indicator_image_browser);
        viewPager.setImagUrls(imageUrls);
        indicator.setViewPager(viewPager);
        viewPager.setCurrentItem(position);
    }

    @Override
    protected void initData() {
        imageUrls = getIntent().getStringArrayListExtra("imageUrls");
        position = getIntent().getIntExtra("position",0);
    }

    @Override
    protected void initEvent() {

    }

}
