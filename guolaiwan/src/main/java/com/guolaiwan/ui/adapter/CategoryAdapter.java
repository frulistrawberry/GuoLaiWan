package com.guolaiwan.ui.adapter;

import com.cgx.library.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import app.guolaiwan.com.guolaiwan.R;
import com.guolaiwan.constant.ItemType;

import java.util.List;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/11
 * 描述:
 */

public class CategoryAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder> {
    public CategoryAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(ItemType.ITEM_TYPE_MERCHANT_REC,R.layout.item_category_recommand);
        addItemType(ItemType.ITEM_TYPE_MERCHANT,R.layout.item_category_list);
        addItemType(ItemType.ITEM_TYPE_PRODUCT_REC,R.layout.item_category_recommand);
        addItemType(ItemType.ITEM_TYPE_PRODUCT,R.layout.item_category_list);
        addItemType(ItemType.ITEM_TYPE_DISTRIBUTOR_REC,R.layout.item_category_recommand);
        addItemType(ItemType.ITEM_TYPE_DISTRIBUTOR,R.layout.item_category_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {

    }
}
