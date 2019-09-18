package com.guolaiwan.ui.iview;

import com.cgx.library.base.BasePresenter;
import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.AddressBean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/24/024.
 */

public interface MyAddressView extends IBaseVIew {
    void loadAddressList(List<AddressBean> data);
}
