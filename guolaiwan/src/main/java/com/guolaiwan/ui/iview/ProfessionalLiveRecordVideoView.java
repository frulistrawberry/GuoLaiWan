package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.RecordVideoBean;
import com.guolaiwan.ui.adapter.RecordVideoListAdapter;

import java.util.List;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/3/7.
 * 说明:
 */
public interface ProfessionalLiveRecordVideoView extends IBaseVIew {
    void loadRecordVideoList( List<RecordVideoBean> recordVideoBeanList);
    void setDeleteVideoResult(int itemLayoutPositon);
}
