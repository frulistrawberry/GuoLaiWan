package com.guolaiwan.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgx.library.utils.CollectionUtils;
import com.google.gson.Gson;
import app.guolaiwan.com.guolaiwan.R;
import com.guolaiwan.bean.FilerBean;
import com.zyyoona7.lib.BaseCustomPopup;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vov.vitamio.VIntent;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/12
 * 描述:
 */

public class SpannerPop extends BaseCustomPopup {

    private OnItemClickListener mListener;
    private LinearLayout mParentLayout;
    private List<Map<String,String>> mRetrievals;

    public interface OnItemClickListener{
        void onItemClick(List<Map<String,String>> retrievals,String currentRetrievals,int index);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }


    public void setData(final FilerBean data){
        final List<FilerBean.Children> children = data.getChildren();
        if (!CollectionUtils.isEmpty(children)){
            mParentLayout.removeAllViews();
            for (int i = 0; i < children.size(); i++) {
                View child = LayoutInflater.from(getContext()).inflate(R.layout.item_spanner_pop,mParentLayout, false);
                TextView textView = child.findViewById(R.id.tv_text);

                textView.setText(children.get(i).getClassName());

                if (i == 0){
                    child.findViewById(R.id.v_divider).setVisibility(View.GONE);
                }
                final int finalI = i;
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String type = data.getType();
                        String value = children.get(finalI).getClassCode();
                        String className = children.get(finalI).getClassName();
                        boolean isAddedType = false;
                        for (Map<String, String> mRetrieval : mRetrievals) {
                            if (mRetrieval.get("type").equals(type)){
                                mRetrieval.put("value",value);
                                isAddedType = true;
                                break;
                            }
                        }
                        if (!isAddedType){
                            Map<String,String> retrieval = new HashMap<>();
                            retrieval.put("type",type);
                            retrieval.put("value",value);
                            mRetrievals.add(retrieval);
                        }
                        if (mListener!=null){
                            mListener.onItemClick(mRetrievals,className,finalI);
                        }
                        dismiss();
                    }
                });
                mParentLayout.addView(child);
            }
            View view = new View(getContext());
            view.setBackgroundColor(Color.parseColor("#88000000"));
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            mParentLayout.addView(view);
        }
    }

    protected SpannerPop(Context context) {
        super(context);
        mRetrievals = new ArrayList<>();
    }



    @Override
    protected void initAttributes() {
        setContentView(R.layout.layout_spanner_pop);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusAndOutsideEnable(true);
    }

    @Override
    protected void initViews(View view) {
        mParentLayout =  getView(R.id.layout_parent);
    }

}
