package com.guolaiwan.presenter;

import android.content.Context;

import com.cgx.library.base.BaseActivity;
import com.cgx.library.base.BasePresenter;
import com.cgx.library.base.IBaseVIew;
import com.cgx.library.net.LifeCycleEvent;
import com.cgx.library.utils.AppUtils;
import com.cgx.library.utils.ThreadPoolUtils;
import com.guolaiwan.db.Area;
import com.guolaiwan.db.City;
import com.guolaiwan.db.DBHelper;
import com.guolaiwan.db.Province;
import com.guolaiwan.model.AddAddressModel;
import com.guolaiwan.net.HttpObserver;
import com.guolaiwan.ui.iview.AddAddressView;
import com.guolaiwan.utils.CommonUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.subjects.PublishSubject;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/25
 * 描述:
 */

public class AddAddressPresenter extends BasePresenter<AddAddressView> {

    private AddAddressModel mModel;
    private ThreadPoolUtils threadPool;
    private DBHelper dbHelper;
    public AddAddressPresenter(AddAddressView mIView) {
        super(mIView);
        threadPool = ThreadPoolUtils.getInstance(ThreadPoolUtils.Type.FixedThread,5);
        dbHelper = DBHelper.getInstance(AppUtils.getAppContext());
        mModel = new AddAddressModel();
    }

    public void addAddress(String name,String phone,String province,String city,String town,String address){
        mModel.addAddress(province, city, town, address, name, phone, new HttpObserver() {
            @Override
            public void onNext(String message, Object data) {
                CommonUtils.showMsg(message);
                if (mIView instanceof BaseActivity){
                    ((BaseActivity) mIView).finish();
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                CommonUtils.error(errCode,errMessage);
            }

            @Override
            public void onComplete() {}
        },mIView.getLifeSubject());
    }

    public void loadAreaData() {
        threadPool.submit(new loadAreaRunnable());
    }

    class loadAreaRunnable implements Runnable{

        @Override
        public void run() {
            List<Province> provinceList = dbHelper.queryForAll(Province.class);
            ArrayList<String> provinceArray = new ArrayList<>();
            ArrayList<ArrayList<String>> cityArray = new ArrayList<>();
            ArrayList<ArrayList<ArrayList<String>>> areaArray = new ArrayList<>();
            if (provinceList != null) {
                for (Province p:provinceList) {
                    provinceArray.add(p.getName());
                    ArrayList<String> cityNameArray = new ArrayList<>();
                    Map<String,Object> provinceKeyMap = new LinkedHashMap<>();
                    provinceKeyMap.put("prov_id",p.getId());
                    List<City> cityList = dbHelper.query(City.class,provinceKeyMap);
                    if (cityList == null)
                        continue;
                    ArrayList<ArrayList<String>> cityAreaArray = new ArrayList<>();
                    for (City city : cityList) {
                        cityNameArray.add(city.getName());
                        Map<String,Object> cityKeymap = new LinkedHashMap<>();
                        cityKeymap.put("city_id",city.getId());
                        List<Area> areaList = dbHelper.query(Area.class,cityKeymap);
                        if (areaList == null){
                            areaList = new ArrayList<>();
                            Area area = new Area();
                        }
                        ArrayList<String> areaNameArray = new ArrayList<>();
                        for (Area area : areaList) {
                            areaNameArray.add(area.getName());
                        }
                        cityAreaArray.add(areaNameArray);
                    }
                    areaArray.add(cityAreaArray);
                    cityArray.add(cityNameArray);
                }
                mIView.initAreaPicker(provinceArray,cityArray,areaArray);
            }
        }
    }
}
