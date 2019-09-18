package com.guolaiwan.ui.activity;

import android.support.v7.widget.RecyclerView;

import com.cgx.library.base.BaseActivity;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

public class GuideCategoryActivity extends BaseActivity{
    @BindView(R.id.layout_ptr)
    public PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    @Override
    protected void initView() {
        setContentView(R.layout.include_ptr_recycler);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }
}
