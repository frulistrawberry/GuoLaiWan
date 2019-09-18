package com.guolaiwan.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import com.cgx.library.utils.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.ImageFloder;
import com.guolaiwan.ui.adapter.ImageDirListAdapter;

import java.util.ArrayList;
import java.util.List;

import app.guolaiwan.com.guolaiwan.R;

/**
 * 作者: chengx
 * 日期: 2016/11/17.
 * 描述: 图片路径选择弹窗
 */
public class ListImageDirPopupWindow extends PopupWindow implements View.OnTouchListener, BaseQuickAdapter.OnItemClickListener {

    private List<ImageFloder> data;
    private RecyclerView recyclerView;
    private ImageDirListAdapter adapter;
    private OnImageDirSelectedListener onImageDirSelectedListener;

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (onImageDirSelectedListener != null) {
            onImageDirSelectedListener.onSelected((ImageFloder) adapter.getData().get(position));
        }
    }

    public interface OnImageDirSelectedListener{
        void onSelected(ImageFloder floder);
    }

    public void setOnImageDirSelectedListener(OnImageDirSelectedListener onImageDirSelectedListener) {
        this.onImageDirSelectedListener = onImageDirSelectedListener;
    }

    public ListImageDirPopupWindow(Context context) {
        super(View.inflate(context, R.layout.widget_pop_list_image_dir, null), LinearLayout.LayoutParams.MATCH_PARENT, (int) (ScreenUtils.getScreenHeight(context) * 0.7), true);
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        setTouchable(true);
        setTouchInterceptor(this);
        setAnimationStyle(R.style.bottomDialogAnim);
        initData();
        initViews();
    }

    private void initData() {
        data = new ArrayList<>();
        adapter = new ImageDirListAdapter();
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    private View findViewById(int resId){
        return getContentView().findViewById(resId);
    }

    private Context getContext(){
        return getContentView().getContext();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE){
            dismiss();
            return true;
        }
        return false;
    }



    public void loadDir(List<ImageFloder> dirs){
        adapter.setNewData(dirs);
    }
}
