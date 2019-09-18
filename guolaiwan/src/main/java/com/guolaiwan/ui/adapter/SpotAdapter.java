package com.guolaiwan.ui.adapter;



import com.cgx.library.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.Child;
import app.guolaiwan.com.guolaiwan.R;

/**
 * 作者: 蔡朝阳
 * 日期: 2018/4/9
 * 描述: 导览界面导览点搜索adapter
 */

public class SpotAdapter extends BaseQuickAdapter<Child,BaseViewHolder> {


    public SpotAdapter() {
        super(R.layout.item_guide_search);
    }

    @Override
    protected void convert(BaseViewHolder helper, Child data) {
        helper.setText(R.id.item_tv,data.getChildName().trim()).addOnClickListener(R.id.item_tv);
    }
}
