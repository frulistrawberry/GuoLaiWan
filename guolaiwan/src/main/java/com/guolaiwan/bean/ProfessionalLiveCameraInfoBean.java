package com.guolaiwan.bean;

import java.util.List;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/2/11.
 * 说明:  导播界面初始化各机位使用
 *        包含各机位信息，是否录制，是否垫播
 */

public class ProfessionalLiveCameraInfoBean {
    private String isRecord;
    private String isMatPlay;
    private List<ProfessionalLiveSubLiveBean> subLives;

    public String getIsRecord() {
        return isRecord;
    }

    public void setIsRecord(String isRecord) {
        this.isRecord = isRecord;
    }

    public String getIsMatPlay() {
        return isMatPlay;
    }

    public void setIsMatPlay(String isMatPlay) {
        this.isMatPlay = isMatPlay;
    }

    public List<ProfessionalLiveSubLiveBean> getSubLives() {
        return subLives;
    }

    public void setSubLives(List<ProfessionalLiveSubLiveBean> subLives) {
        this.subLives = subLives;
    }
}
