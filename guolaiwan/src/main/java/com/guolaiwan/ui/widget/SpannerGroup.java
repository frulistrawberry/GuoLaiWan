package com.guolaiwan.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.SizeUtils;
import com.cgx.library.utils.StringUtils;
import app.guolaiwan.com.guolaiwan.R;
import com.guolaiwan.bean.FilerBean;

import java.util.List;
import java.util.Map;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/12
 * 描述:
 */

public class SpannerGroup extends LinearLayout  {

    private SpannerPop mPop;

    private SpannerPop.OnItemClickListener mListener;

    private String[] categoryNames;


    public void setOnItemClickListener(SpannerPop.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public SpannerGroup(Context context) {
        this(context,null);
    }

    public SpannerGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        categoryNames = new String[20];
        mPop = new SpannerPop(getContext()).createPopup();
    }

    public void setData(final List<FilerBean> data){
        removeAllViews();
        if (CollectionUtils.isEmpty(data)){
            setVisibility(GONE);
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            final LinearLayout childView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_spanner_group,this,false);
            childView.setLayoutParams(new LinearLayout.LayoutParams(0, SizeUtils.dp2px(getContext(),48),1));
            final TextView filterNameTv = childView.findViewById(R.id.tv_text);
            if (StringUtils.isEmpty(categoryNames[i]))
            filterNameTv.setText(data.get(i).getName());
            else
                filterNameTv.setText(categoryNames[i]);
            final int finalI = i;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        mPop.setOnItemClickListener(new SpannerPop.OnItemClickListener() {
                            @Override
                            public void onItemClick(List<Map<String, String>> retrievals, String currentRetrievals,int index) {
                                filterNameTv.setText(currentRetrievals);
                                categoryNames[finalI] = currentRetrievals;
                                mListener.onItemClick(retrievals,currentRetrievals,finalI);
                            }
                        });
                    }
                    mPop.setData(data.get(finalI));
                    mPop.showAsDropDown(SpannerGroup.this);
                }
            });
            addView(childView);
        }
    }

    public void setCategoryNames(String name ,int index){
        categoryNames[index] = name;
    }


}
