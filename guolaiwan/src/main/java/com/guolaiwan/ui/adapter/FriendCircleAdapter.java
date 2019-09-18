package com.guolaiwan.ui.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.HeterogeneousExpandableList;
import android.widget.ImageView;

import com.amap.api.navi.view.PoiInputResItemWidget;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.BaseViewHolder;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.FrescoUtil;
import com.cgx.library.utils.StringUtils;
import com.cgx.library.widget.dialog.AlertDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.guolaiwan.bean.FriendBean;
import com.guolaiwan.bean.Paise;
import com.guolaiwan.bean.RichUpload;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.activity.CommentListActivity;
import com.guolaiwan.ui.activity.ImageBrowserActivity;
import com.guolaiwan.ui.activity.PicTextDetailActivity;
import com.guolaiwan.ui.activity.VideoActivity;
import com.guolaiwan.ui.widget.CmmtPopup;
import com.guolaiwan.ui.widget.MultiImageView;
import com.guolaiwan.ui.widget.RefundPopup;
import com.guolaiwan.utils.CommonUtils;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.guolaiwan.com.guolaiwan.R;

/**
 * Created by Administrator on 2018/5/22/022.
 */

public class FriendCircleAdapter extends BaseQuickAdapter<FriendBean,BaseViewHolder> implements View.OnClickListener {

    private EasyPopup pop;
    private RefundPopup cmmtPopup;


    private FriendBean position;

    private boolean isMy;
    CallBack callBack;
    public  interface CallBack{
        void onDelete(FriendBean item);
    }

    public void setMy(boolean my) {
        isMy = my;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public FriendCircleAdapter() {
        super(R.layout.item_friend_circle);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void convert(BaseViewHolder helper, FriendBean item) {
        if (pop == null){
            pop = new EasyPopup(mContext).setContentView(R.layout.popup_comment).setFocusAndOutsideEnable(true).createPopup();
            pop.getView(R.id.item_like).setOnClickListener(this);
            pop.getView(R.id.item_comment).setOnClickListener(this);
        }


        helper.setGone(R.id.btn_delete,isMy);
        if (item.getUser() != null) {
            String headUrl = item.getUser().getUserHeadimg();
            if (StringUtils.isEmpty(headUrl)){
                FrescoUtil.getInstance().loadResourceImage(helper.getView(R.id.iv_img),R.mipmap.ic_launcher);
            }else
                helper.setImageUrl(R.id.iv_img,item.getUser().getUserHeadimg());
            String nickName = item.getUser().getUserNickname();
            if (StringUtils.isEmpty(nickName)){
                helper.setText(R.id.tv_nick_name,"过来玩会员_"+item.getUser().getId());
            }else
                helper.setText(R.id.tv_nick_name,nickName);
        }

        helper.setOnClickListener(R.id.btn_delete, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack!=null)
                    callBack.onDelete(item);
            }
        });
        helper.setText(R.id.tv_time,item.getUpdateTime());
        SimpleDraweeView video = helper.getView(R.id.iv_video);
        String fUrl = item.getFUrl();
        video.setVisibility(View.GONE);
        ArrayList<String> imgs = new ArrayList<>();
        if (item.getType().equals("图文")){
            helper.setGone(R.id.iv_play,false);
            try {
                helper.setGone(R.id.pic_layout,true);
                String context = item.getContent();
                RichUpload richUpload = new Gson().fromJson(context,RichUpload.class);
                helper.setText(R.id.titleTv,richUpload.title);
                List<RichUpload.Content> contents = richUpload.content;
                boolean havecontent = false;
                for (RichUpload.Content content : contents) {
                    if (!StringUtils.isEmpty(content.text)){
                        helper.setText(R.id.descTv,content.text);
                        havecontent = true;
                        break;
                    }
                }
                if (!havecontent){
                    helper.setText(R.id.descTv,"");
                }
                helper.setImageUrl(R.id.iv_news,richUpload.cover);
            }catch (Exception e){

            }

            helper.getView(R.id.pic_layout).setOnClickListener(v -> PicTextDetailActivity.launch(mContext, item.getId()+""));
        }else if (item.getType().equals("小视频")){
            helper.setGone(R.id.iv_play,true);
            helper.setGone(R.id.pic_layout,true);
            if (TextUtils.isEmpty(item.getName())) {
                helper.setGone(R.id.titleTv,false);
            }else {
                helper.setText(R.id.titleTv,item.getName());
                helper.setGone(R.id.titleTv,true);

            }
            if (!TextUtils.isEmpty(item.getContent())){
                helper.setText(R.id.descTv,item.getContent());
                helper.setGone(R.id.descTv,true);
            }else {
                helper.setGone(R.id.descTv,false);
            }
            if (!TextUtils.isEmpty(item.getHeadPic()))
                helper.setImageUrl(R.id.iv_news,item.getHeadPic());
            else
                helper.setImageResource(R.id.iv_news,R.drawable.video);
            helper.getView(R.id.pic_layout).setOnClickListener(v -> VideoActivity.launch(mContext, fUrl
                    ,item.getUser().getUserNickname(),item.getUser().getUserHeadimg(),item.getContent(),item.getName(),item.getUserId()+""));
        }
        helper.setText(R.id.tv_comment,"评论 ("+item.getCommentCount()+")");
        helper.setText(R.id.tv_paise,"赞 ("+item.getPraiseCount()+")");
        helper.getView(R.id.tv_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = item;
                CommentListActivity.launch(mContext,item.getId()+"");

            }
        });
        helper.getView(R.id.tv_paise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = item;
                Map<String,String> params = new HashMap<>();
                params.put("userId", CommonUtils.getUserId());
                params.put("vpId", position.getId()+"");
                RetrofitUtil.composeToSubscribe(HttpClient.getApiService().praisePic(params), new HttpObserver<Paise>() {
                    @Override
                    public void onNext(String message, Paise data) {
                        int countCount = position.getPraiseCount();
                        if (data.code.equals("1"))
                            countCount++;
                        else if (data.code.equals("0"))
                            countCount--;
                        position.setPraiseCount(countCount);
                        notifyDataSetChanged();
                        CommonUtils.showMsg(data.message);
                    }

                    @Override
                    public void onError(int errCode, String errMessage) {

                    }



                    @Override
                    public void onComplete() {

                    }
                },((BaseActivity)mContext).getLifeSubject());
            }
        });





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_comment:
                pop.dismiss();
                cmmtPopup.show(v);
                break;
            case R.id.item_like:
                break;
        }
    }

    private void initRefondPop(){
        cmmtPopup=new RefundPopup(mContext)
                .createPopup();
        cmmtPopup.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cmmtPopup.isShowing()) {
                    cmmtPopup.dismiss();
                }
            }
        });

        cmmtPopup.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cmmtPopup.isShowing()) {
                    String reason = cmmtPopup.getContentEt().getText().toString().trim();
                    if (com.cgx.library.utils.StringUtils.isEmpty(reason)){
                        CommonUtils.showMsg("请输入消息内容");
                        return;
                    }
                    Map<String,String> params = new HashMap<>();
                    params.put("userId", CommonUtils.getUserId());
                    params.put("vpId", position.getId()+"");
                    params.put("commentText", cmmtPopup.getContentEt().getText().toString());
                    RetrofitUtil.composeToSubscribe(HttpClient.getApiService().commentPic(params), new HttpObserver() {
                        @Override
                        public void onNext(String message, Object data) {

                        }

                        @Override
                        public void onError(int errCode, String errMessage) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    },((BaseActivity)mContext).getLifeSubject());
                    cmmtPopup.getContentEt().setText("");
                    cmmtPopup.dismiss();
                }
            }
        });
        cmmtPopup.setHinit("说点儿什么吧");
        cmmtPopup.setOkText("发送");
    }

    @Subscribe
    public void onEventMainThread(String str){
        if (str.equals("update_comment_count")){
            int countCount = position.getCommentCount();
            countCount++;
            position.setCommentCount(countCount);
            notifyDataSetChanged();

        }
    }
}
