package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.autonavi.ae.dice.InitConfig;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.BaseViewHolder;
import com.cgx.library.net.RetrofitUtil;
import com.cgx.library.utils.CollectionUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.bean.FriendBean;
import com.guolaiwan.bean.VpCommentBean;
import com.guolaiwan.net.HttpClient;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.net.HttpResult;
import com.guolaiwan.ui.fragment.FriendCircleFragment;
import com.guolaiwan.ui.widget.NormalSelectDialog;
import com.guolaiwan.utils.CommonUtils;
import com.guolaiwan.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/5/26/026.
 */

public class CommentListActivity extends BaseActivity {

    @BindView(R.id.layout_ptr)
    public PtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;
    @BindView(R.id.et_cmmt)
    EditText editText;
    public int mCurrentPage=1;
    public BaseQuickAdapter<VpCommentBean,BaseViewHolder> mAdapter;
    private String id;

    public static void launch(Context context,String id){
        Intent intent = new Intent(context,CommentListActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    public void showContent() {
        super.showContent();
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
        if (mAdapter.isLoading())
            mAdapter.loadMoreComplete();
    }

    @Override
    public void showLoading() {
        if (!mPtrLayout.isRefreshing() && mCurrentPage == 1){
            mAdapter.setEmptyView(ViewUtils.getLoadingView(getContext()));
        }
    }

    @Override
    public void showEmpty() {
        if (mCurrentPage == 1){
            mAdapter.setNewData(null);
            mAdapter.setEmptyView(ViewUtils
                    .getEmptyView(getContext(),R.mipmap.no_project,"暂无数据"));
        }

        else {
            mAdapter.loadMoreEnd(false);
        }
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
    }

    @Override
    public void showError() {
        mAdapter.setEmptyView(ViewUtils.getErrorView(getContext()));
        if (mPtrLayout.isRefreshing())
            mPtrLayout.refreshComplete();
        if (mCurrentPage>1){
            mAdapter.loadMoreFail();
        }
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setTitle("评论").showBack().show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_comment_list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        request();
    }


    @Override
    protected void initData() {
        id = getIntent().getStringExtra("id");
        mAdapter = new BaseQuickAdapter<VpCommentBean, BaseViewHolder>(R.layout.item_vpcomment_list) {
            @Override
            protected void convert(BaseViewHolder helper, VpCommentBean item) {
                helper.setText(R.id.tv_comment_content,item.getCommentText());
                helper.setText(R.id.tv_time,item.getUpdateTime());
                helper.setText(R.id.tv_nick_name,item.getUser().getUserNickname());
                helper.setImageUrl(R.id.iv_img,item.getUser().getUserHeadimg());
            }
        };
        mAdapter.setEnableLoadMore(true);
     }


    @Override
    protected void initEvent() {
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mCurrentPage = 1;
                request();
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mCurrentPage++;
                request();
            }
        });
    }

    private void request(){
        showLoading();
        Observable<HttpResult<List<VpCommentBean>>> observable = HttpClient.getApiService().getCommentList(CommonUtils.getUserId(),id,mCurrentPage,10);
        RetrofitUtil.composeToSubscribe(observable, new HttpObserver<List<VpCommentBean>>() {
            @Override
            public void onNext(String message, List<VpCommentBean> data) {
                if (CollectionUtils.isEmpty(data)){
                    showEmpty();
                    return;
                }
                if (mCurrentPage == 1){
                    mAdapter.setNewData(data);
                }else
                    mAdapter.addData(data);
                showContent();
            }

            @Override
            public void onError(int errCode, String errMessage) {
                showError();
            }

            @Override
            public void onComplete() {

            }
        },getLifeSubject());
    }

    @OnClick(R.id.btn_commit)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_commit:
                if (CommonUtils.isLogin()){
                    String reason = editText.getText().toString().trim();
                    if (com.cgx.library.utils.StringUtils.isEmpty(reason)){
                        CommonUtils.showMsg("请输入消息内容");
                        return;
                    }
                    Map<String,String> params = new HashMap<>();
                    params.put("userId", CommonUtils.getUserId());
                    params.put("vpId", id);
                    params.put("commentText", editText.getText().toString());
                    RetrofitUtil.composeToSubscribe(HttpClient.getApiService().commentPic(params), new HttpObserver() {
                        @Override
                        public void onNext(String message, Object data) {
                            EventBus.getDefault().post("update_comment_count");
                            mCurrentPage = 1;
                            request();
                            editText.setText("");
                        }

                        @Override
                        public void onError(int errCode, String errMessage) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    },getLifeSubject());
                }else {
                    LoginActivity.launch(this);
                }
                break;
        }
    }
}
