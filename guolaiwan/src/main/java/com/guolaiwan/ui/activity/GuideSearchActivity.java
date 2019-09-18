package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SearchEvent;
import android.view.View;
import android.widget.EditText;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.Child;
import com.guolaiwan.bean.GuideSearchEvent;
import com.guolaiwan.ui.adapter.SpotAdapter;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;

public class GuideSearchActivity extends BaseActivity  {

    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;
    @BindView(R.id.etSearch)
    EditText etSearch;

    private List<Child> mChildList;
    private SpotAdapter mSpotAdapter;


    public static void launch(Context context, List<Child> childList){
        Intent intent = new Intent();
        intent.putExtra("childList", (Serializable) childList);
        intent.setClass(context,GuideSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mChildList = (List<Child>) intent.getSerializableExtra("childList");

        mSpotAdapter = new SpotAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        mRecyclerView.setAdapter(mSpotAdapter);
        mSpotAdapter.setNewData(mChildList);

        mSpotAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.item_tv:
                        Child child = mChildList.get(position);
                        etSearch.setText(child.getChildName().trim());
                        break;
                }
            }
        });
    }


    private Child mChild;
    @OnClick({R.id.btnSearchRight,R.id.btnSearch})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnSearchRight:
                finish();
                break;
            case R.id.btnSearch:
                String editStr = etSearch.getText().toString().trim();
                if(editStr.equals("")){
                    ToastUtils.showToast(GuideSearchActivity.this,"请输入景点名称");
                }else{
                   for(Child child : mChildList){
                       if(child.getChildName().trim().equals(editStr)){
                           mChild = child;
                           break;
                       }
                   }
                   if(mChild == null){
                       ToastUtils.showToast(GuideSearchActivity.this,"没有这个景点哦~~~");
                       etSearch.setText("");
                   }else {
                       GuideSearchEvent guideSearchEvent = new GuideSearchEvent(mChild);
                       EventBus.getDefault().post(guideSearchEvent);
                       finish();
                   }
                }
                break;
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_guide_search);
    }

    @Override
    protected void initData() {}

    @Override
    protected void initEvent() {}


}
