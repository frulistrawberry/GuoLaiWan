package com.guolaiwan.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cgx.library.base.BaseActivity;
import com.cgx.library.utils.ToastUtils;
import com.guolaiwan.bean.ActivityEvent;
import com.guolaiwan.presenter.ProfessionalLiveApplyPresenter;
import com.guolaiwan.ui.iview.ProfessionalLiveApplyView;
import com.guolaiwan.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;

public class ProfessionalLiveApplyActivity extends BaseActivity implements ProfessionalLiveApplyView,View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    //开始日期
    @BindView(R.id.tv_date_start)
    TextView mDateStartTv;
    //结束日期
    @BindView(R.id.tv_date_end)
    TextView mDateEndTv;
    //机位数量
    @BindView(R.id.rg_camera_count)
    RadioGroup mCameraCountRg;
    @BindView(R.id.rb_camera_count_two)
    RadioButton mCameraCountTwoRb;
    @BindView(R.id.rb_camera_count_three)
    RadioButton mCameraCountThreeRb;
    @BindView(R.id.rb_camera_count_four)
    RadioButton mCameraCountFourRb;
    @BindView(R.id.rb_camera_count_five)
    RadioButton mCameraCountFiveRb;
    @BindView(R.id.rb_camera_count_six)
    RadioButton mCameraCountSixRb;
    //是否保存
    @BindView(R.id.rg_save)
    RadioGroup mSaveRg;
    @BindView(R.id.rb_save_yes)
    RadioButton mSaveYesRb;
    @BindView(R.id.rb_save_no)
    RadioButton mSaveNoRb;
    //保存时长
    @BindView(R.id.ll_save_hour)
    LinearLayout mSaveHourLinearLayout;
    @BindView(R.id.et_save_hour)
    EditText mSaveHourEt;
    //是否垫播
    @BindView(R.id.rg_mat_play)
    RadioGroup mMatPlayRg;
    @BindView(R.id.rb_mat_play_yes)
    RadioButton mMatPlayYesRb;
    @BindView(R.id.rb_mat_play_no)
    RadioButton mMatPlayNoRb;
    //总计价格
    @BindView(R.id.tv_total_price)
    TextView mTotalPriceTv;
    //核算价格
    @BindView(R.id.tv_check_price)
    TextView mCheckPriceTv;
    //申请直播
    @BindView(R.id.tv_apply)
    TextView mApplyTv;
    //时间选择器控件
    private TimePickerView mTimePickerView;
    //各选项结果值
    private Date mStartDate;
    private Date mEndDate;
    private String mStartTime;
    private String mEndTime;
    private String mCameraCount = "2";
    private String mIsSave = "NO";
    private String mSaveTime = "";
    private String mIsMatPlay = "NO";

    private ProfessionalLiveApplyPresenter mPresenter;

    public static void launch(Context context){
        if (!CommonUtils.isLogin()){
            LoginActivity.launch(context);
            return;
        }
        context.startActivity(new Intent(context,ProfessionalLiveApplyActivity.class));
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().showBack().setTitle("专业直播申请").show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_professional_apply_live);
        initTimerPicker();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        mPresenter = new ProfessionalLiveApplyPresenter(this);
    }

    @Override
    protected void initEvent() {
        mDateStartTv.setOnClickListener(this);
        mDateEndTv.setOnClickListener(this);
        mCameraCountRg.setOnCheckedChangeListener(this);
        mSaveRg.setOnCheckedChangeListener(this);
        mMatPlayRg.setOnCheckedChangeListener(this);
        mCheckPriceTv.setOnClickListener(this);
        mApplyTv.setOnClickListener(this);
    }

    /*初始化TimerPicker*/
    private void initTimerPicker(){
        //显示时间范围
        Calendar startDate = Calendar.getInstance();
        int startYear = startDate.get(Calendar.YEAR);
        int startMonth = startDate.get(Calendar.MONTH);
        int startDay = startDate.get(Calendar.DAY_OF_MONTH);
        int startHour = startDate.get(Calendar.HOUR_OF_DAY);
        startDate.set(startYear,startMonth,startDay,startHour,0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2100,11,31,20,0);
        mTimePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时");
                String formatDate = dateFormat.format(date);
                if(v.getId() == R.id.tv_date_start){
                    mStartDate = date;
                    mStartTime = formatDate;
                    mDateStartTv.setText(formatDate);
                }

                if(v.getId() == R.id.tv_date_end){
                    mEndDate = date;
                    mEndTime = formatDate;
                    mDateEndTv.setText(formatDate);
                }
            }
        })
        .setLabel("年","月","日","时","","")
        .setRangDate(startDate,endDate)
        .setType(new boolean[]{true, true, true, true,false,false})
        .isDialog(true)
        .build();
        Dialog timePickerViewDialog = mTimePickerView.getDialog();
        if (timePickerViewDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            mTimePickerView.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = timePickerViewDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);
                dialogWindow.setGravity(Gravity.BOTTOM);
                dialogWindow.setDimAmount(0.1f);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_date_start:
                mTimePickerView.setTitleText("开始时间");
                mTimePickerView.show(view);
                break;
            case R.id.tv_date_end:
                mTimePickerView.setTitleText("结束时间");
                mTimePickerView.show(view);
                break;

            case R.id.tv_check_price:
                if(mStartTime == null || mStartTime.equals("")){
                    ToastUtils.showToast(getContext(),"温馨提示:请输入开始时间。");
                    return;
                }

                if(mEndTime == null || mEndTime.equals("")){
                    ToastUtils.showToast(getContext(),"温馨提示:请输入结束时间。");
                    return;
                }

                if(mEndDate.getTime() <= mStartDate.getTime()){
                    ToastUtils.showToast(getContext(),"温馨提示:结束时间不能小于开始时间。");
                    return;
                }


                if(mIsSave.equals("YES")){
                    mSaveTime = mSaveHourEt.getText().toString();
                    if(mEndTime == null || mSaveTime.equals("")){
                        ToastUtils.showToast(getContext(),"温馨提示:请输入保存时间。");
                        return;
                    }
                }

                Log.i("CAI","开始时间: " + mStartTime);
                Log.i("CAI","结束时间: " + mEndTime);
                Log.i("CAI","机位数量: " + mCameraCount);
                Log.i("CAI","是否录制: " + mIsSave);
                Log.i("CAI","录制时间: " + mSaveTime);
                Log.i("CAI","是否垫播: " + mIsMatPlay);
                mPresenter.checkPrice(mStartTime,mEndTime,mCameraCount,mIsSave,mSaveTime,mIsMatPlay);
                break;

            case R.id.tv_apply:
                if(mStartTime == null || mStartTime.equals("")){
                    ToastUtils.showToast(getContext(),"温馨提示:请输入开始时间。");
                    return;
                }

                if(mEndTime == null || mEndTime.equals("")){
                    ToastUtils.showToast(getContext(),"温馨提示:请输入结束时间。");
                    return;
                }

                if(mEndDate.getTime() <= mStartDate.getTime()){
                    ToastUtils.showToast(getContext(),"温馨提示:结束时间不能小于或者等于开始时间。");
                    return;
                }


                if(mIsSave.equals("YES")){
                    mSaveTime = mSaveHourEt.getText().toString();
                    if(mEndTime == null || mSaveTime.equals("")){
                        ToastUtils.showToast(getContext(),"温馨提示:请输入保存时间。");
                        return;
                    }
                }
                Log.i("CAI","开始时间: " + mStartTime);
                Log.i("CAI","结束时间: " + mEndTime);
                Log.i("CAI","机位数量: " + mCameraCount);
                Log.i("CAI","是否录制: " + mIsSave);
                Log.i("CAI","录制时间: " + mSaveTime);
                Log.i("CAI","是否垫播: " + mIsMatPlay);
                ProfessionalLiveOrderActivity.launch(this,mStartTime,mEndTime,mCameraCount,mIsSave,mSaveTime,mIsMatPlay);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (radioGroup.getId()){
            //机位数量
            case R.id.rg_camera_count:
                switch (checkedId){
                    case R.id.rb_camera_count_two:
                        mCameraCount = "2";
                        break;
                    case R.id.rb_camera_count_three:
                        mCameraCount = "3";
                        break;
                    case R.id.rb_camera_count_four:
                        mCameraCount = "4";
                        break;
                    case R.id.rb_camera_count_five:
                        mCameraCount = "5";
                        break;
                    case R.id.rb_camera_count_six:
                        mCameraCount = "6";
                        break;
                }
                break;

            //是否保存
            case R.id.rg_save:
                if(checkedId == mSaveYesRb.getId()){
                    mIsSave = "YES";
                    mSaveHourLinearLayout.setVisibility(View.VISIBLE);
                }

                if(checkedId == mSaveNoRb.getId()){
                    mIsSave = "NO";
                    mSaveHourLinearLayout.setVisibility(View.GONE);
                }
                break;

            //是否垫播
            case R.id.rg_mat_play:
                if(checkedId == mMatPlayYesRb.getId()){
                    mIsMatPlay = "YES";
                }

                if(checkedId == mMatPlayNoRb.getId()){
                    mIsMatPlay = "NO";
                }
                break;
        }
    }

    @Override
    public void setCheckedPrice(String checkedPrice) {
        mTotalPriceTv.setText(checkedPrice);
    }

    /*支付成功后关闭界面*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActivityEvent(ActivityEvent activityEvent) {
        String action = activityEvent.action;
        if(action.equals("close")){
            finish();
        }
    }
}
