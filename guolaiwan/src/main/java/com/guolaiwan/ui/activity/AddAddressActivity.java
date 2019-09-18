package com.guolaiwan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.StringUtils;
import com.guolaiwan.presenter.AddAddressPresenter;
import com.guolaiwan.ui.iview.AddAddressView;
import com.guolaiwan.utils.CommonUtils;

import java.util.ArrayList;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;
import butterknife.OnClick;


public class AddAddressActivity extends BaseActivity implements AddAddressView{
    @BindView(R.id.et_name)
    EditText mNameEt;
    @BindView(R.id.et_phone)
    EditText mPhoneEt;
    @BindView(R.id.et_address)
    EditText mAddressEt;
    @BindView(R.id.tv_city)
    TextView mCityTv;
    private OptionsPickerView<String> areaPicker;

    private String mProvience = "";
    private String mCity = "";
    private String mTown = "";
    private AddAddressPresenter mPresenter;

    public static void launch(Context context){
        context.startActivity(new Intent(context,AddAddressActivity.class));
    }

    @OnClick({R.id.tv_city,R.id.btn_commit})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_city:
                InputMethodManager inputMethodManager = (InputMethodManager)AddAddressActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(AddAddressActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                areaPicker.show();
                break;
            case R.id.btn_commit:
                String name = mNameEt.getText().toString().trim();
                String phone = mPhoneEt.getText().toString();
                String cityInfo = mCityTv.getText().toString().trim();
                String address = mAddressEt.getText().toString();
                if (StringUtils.isEmpty(name)){
                    CommonUtils.showMsg("请输入名字");
                    break;
                }
                if (StringUtils.isEmpty(phone)){
                    CommonUtils.showMsg("请输入联系电话");
                    break;
                }
                if (StringUtils.isEmpty(cityInfo)){
                    CommonUtils.showMsg("请选择所在城市");
                    break;
                }
                if (StringUtils.isEmpty(address)){
                    CommonUtils.showMsg("请输入详细地址");
                    break;
                }
                mPresenter.addAddress(name,phone,mProvience,mCity,mTown,address);
                break;
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_adress);
        mPresenter.loadAreaData();
//        areaPicker = new OptionsPickerView<>(this);
//        areaPicker.setTitle("所在城市");
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setTitle("添加收货地址").showBack().show();
    }

    @Override
    protected void initData() {
        mPresenter = new AddAddressPresenter(this);
    }

    @Override
    protected void initEvent() {}

    @Override
    public void initAreaPicker(final ArrayList<String> provinceList, final ArrayList<ArrayList<String>> cityList, final ArrayList<ArrayList<ArrayList<String>>> areaList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                areaPicker.setPicker(provinceList,cityList,areaList,true);
//                areaPicker.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
//                    @Override
//                    public void onOptionsSelect(int options1, int option2, int options3) {
//                        String area = provinceList.get(options1)+" "+ cityList.get(options1).get(option2)+
//                                " "+ areaList.get(options1).get(option2).get(options3);
//                        mProvience = provinceList.get(options1);
//                        mCity = cityList.get(options1).get(option2);
//                        mTown = areaList.get(options1).get(option2).get(options3);
//                        mCityTv.setText(area);
//                    }
//                });
            }
        });
    }

}
