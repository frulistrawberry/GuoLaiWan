package com.guolaiwan.ui.adapter;

import android.text.TextUtils;

import com.cgx.library.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.MessageBean;
import com.guolaiwan.utils.EmojiUtil;

import app.guolaiwan.com.guolaiwan.R;

/**
 * Created by Administrator on 2018/5/13/013.
 */

public class MessageAdapter extends BaseQuickAdapter<MessageBean,BaseViewHolder> {

    public MessageAdapter() {
        super(R.layout.item_message);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBean item) {
        String nickName = item.getNickName();
        if (TextUtils.isEmpty(nickName)){
            nickName = "过来玩会员_"+item.getUserId()+":";
        }else {
            nickName = nickName+":";
        }

        String msgStr = EmojiUtil.stringToEmoji(item.getMessage());
        helper.setText(R.id.tv_message,nickName + msgStr);
    }
}
