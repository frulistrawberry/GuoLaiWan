package com.guolaiwan.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgx.library.utils.CollectionUtils;
import com.cgx.library.utils.SizeUtils;
import app.guolaiwan.com.guolaiwan.R;
import com.zyyoona7.lib.BaseCustomPopup;

import java.util.List;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/11
 * 描述:
 */

public class ListPop extends BaseCustomPopup {
    private OnItemClickListener mListener;
    private LinearLayout mParentLayout;

    public ListPop(Context context) {
        super(context);
    }

    @Override
    protected void initAttributes() {
        setContentView(R.layout.layout_pop_list);
        setWidth(SizeUtils.dp2px(getContext(),100));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusAndOutsideEnable(true);
    }

    @Override
    protected void initViews(View view) {
        mParentLayout = getView(R.id.layout_parent);
    }

    public ListPop setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
        return this;
    }

    public ListPop setData(List<String> data){
        if (!CollectionUtils.isEmpty(data)){
            mParentLayout.removeAllViews();
            for (int i = 0; i < data.size(); i++) {
                View child = LayoutInflater.from(getContext()).inflate(R.layout.item_pop_list,mParentLayout, false);
                TextView textView = child.findViewById(R.id.tv_text);
                textView.setText(data.get(i));
                if (i == 0){
                    child.findViewById(R.id.v_divider).setVisibility(View.GONE);
                }
                final int finalI = i;
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener!=null)
                            mListener.onItemClick(finalI);
                    }
                });
                mParentLayout.addView(child);
            }
        }
        return this;
    }

   public  interface OnItemClickListener{
        void onItemClick(int position);
   }
}
