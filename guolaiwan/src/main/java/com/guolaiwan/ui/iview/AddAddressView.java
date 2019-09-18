package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;

import java.util.ArrayList;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/25
 * 描述:
 */

public interface AddAddressView extends IBaseVIew {
    void initAreaPicker(ArrayList<String> province, ArrayList<ArrayList<String>> city, ArrayList<ArrayList<ArrayList<String>>> area);
}
