package com.guolaiwan.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cgx.library.base.BaseViewHolder;
import com.cgx.library.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guolaiwan.App;
import com.guolaiwan.bean.RecordVideoBean;
import com.guolaiwan.presenter.ProfessionalLiveRecordVideoPresenter;
import com.guolaiwan.ui.activity.PrefessionalLiveRecordVideoPlayActivity;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

import app.guolaiwan.com.guolaiwan.R;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/3/7.
 * 说明:
 */
public class RecordVideoListAdapter extends BaseQuickAdapter<RecordVideoBean,BaseViewHolder> {

    private Context mContext;
    private String mStorePath;
    private int mDownLoadId;
    private ProfessionalLiveRecordVideoPresenter mPresenter;

    public RecordVideoListAdapter(Context context) {
        super(R.layout.item_record_video);
        this.mContext = context;
        mStorePath = App.APP_VIDEO_PATH;
        File file = new File(mStorePath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, RecordVideoBean item) {
        String id = item.getId();
        String downLoadUrl = item.getPubUrl();
        String storePath = mStorePath + item.getRecordName() + ".mp4";
        String videoName = item.getSubLiveName();
        if(helper.getAdapterPosition() == 0){
            helper.setVisible(R.id.view_divider,false);
        }
        helper.setText(R.id.tv_video_name,videoName);
        Button downLoadBt =  helper.getView(R.id.bt_down_load);
        ProgressBar pbProgress = helper.getView(R.id.pb_progress);
        Button deleteBt = helper.getView(R.id.bt_delete);
        RelativeLayout mRecordItemRl = helper.getView(R.id.rl_record_item);

        MyDownLoadListerner myDownLoadListerner = new MyDownLoadListerner(videoName,pbProgress,downLoadBt);

        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(downLoadBt.getText().equals("暂停")){
                    //说明下载中
                    //停止下载
                    FileDownloader.getImpl().pauseAll();
                }
                int itemLayoutPosition = helper.getLayoutPosition();
                //调用删除接口删除服务器数据和文件
                mPresenter.professionalLiveDeleteRecordVideoListItem(id,itemLayoutPosition);
            }
        });


        downLoadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(downLoadBt.getText().equals("完成")){
                    return;
                }

                if(downLoadBt.getText().equals("下载")){
                    //修改按钮文本
                    downLoadBt.setText("暂停");
                    //执行下载逻辑
                    mDownLoadId = FileDownloader.getImpl()
                            .create(downLoadUrl)
                            .setPath(storePath)
                            .setListener(myDownLoadListerner)
                            .start();
                }else {
                    //修改按钮文本
                    downLoadBt.setText("下载");
                    //执行暂停逻辑
                    FileDownloader.getImpl().pause(mDownLoadId);
                }
            }
        });

        mRecordItemRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefessionalLiveRecordVideoPlayActivity.launch(mContext,videoName,downLoadUrl);
            }
        });
    }


    /*向Activity提供:绑定Presenter*/
    public void setPresenter(ProfessionalLiveRecordVideoPresenter presenter){
        this.mPresenter = presenter;
    }

    /*向Activity提供:用于界面退出*/
    public void releaseAllDownLoadTask(){
        FileDownloader.getImpl().pauseAll();
    }

    private class MyDownLoadListerner extends FileDownloadLargeFileListener{

        private ProgressBar mProgressBar;
        private Button mDownLoadBt;
        private String mVideoName;

        public MyDownLoadListerner(String videoName,ProgressBar progressBar, Button downLoadBt) {
            this.mVideoName = videoName;
            this.mProgressBar = progressBar;
            this.mDownLoadBt = downLoadBt;
        }

        @Override
        protected void pending(BaseDownloadTask task, long soFarBytes, long totalBytes) {}

        @Override
        protected void progress(BaseDownloadTask task, long soFarBytes, long totalBytes) {
            float persent = soFarBytes / (float) totalBytes;
            int progress = (int) (persent * 100);
            mProgressBar.setProgress(progress);
        }

        @Override
        protected void paused(BaseDownloadTask task, long soFarBytes, long totalBytes) {}

        @Override
        protected void completed(BaseDownloadTask task) {
            mDownLoadBt.setText("完成");
            mProgressBar.setProgress(100);
            ToastUtils.showToast(mContext,"温馨提示:" + mVideoName + " 下载完成");
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            mDownLoadBt.setText("下载");
            ToastUtils.showToast(mContext,"温馨提示:" + mVideoName + " 下载失败");
        }

        @Override
        protected void warn(BaseDownloadTask task) {}
    }


}
