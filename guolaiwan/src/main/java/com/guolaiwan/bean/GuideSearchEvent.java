package com.guolaiwan.bean;

import java.util.List;

/**
 * 蔡朝阳:景点搜索界面Event实体
 */

public class GuideSearchEvent {

    public Child mChild;

    public GuideSearchEvent(Child mChild) {
        this.mChild = mChild;
    }

    public Child getmChild() {
        return mChild;
    }

    public void setmChild(Child mChild) {
        this.mChild = mChild;
    }
}
