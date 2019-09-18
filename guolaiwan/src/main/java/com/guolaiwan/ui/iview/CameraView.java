package com.guolaiwan.ui.iview;

import com.cgx.library.base.IBaseVIew;
import com.guolaiwan.bean.LookLiveData;
import com.guolaiwan.bean.MessageBean;
import com.guolaiwan.bean.ProductBean;
import com.guolaiwan.bean.StartLiveBean;

public interface CameraView extends IBaseVIew{

    void setLiveData(LookLiveData liveData);

    void addMessage(MessageBean message);

    void showProduct(ProductBean product);

    void changePrice(int price);

    void confirmPrice(float price);

    void setLookLiveData(LookLiveData data);

    void delePruduct();
}
