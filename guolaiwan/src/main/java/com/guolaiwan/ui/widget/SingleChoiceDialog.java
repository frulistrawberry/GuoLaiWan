package com.guolaiwan.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.guolaiwan.bean.VoiceBean;

import java.util.List;

import app.guolaiwan.com.guolaiwan.R;

/**
 * 创建者: 蔡朝阳
 * 日期:  2018/11/19.
 * 说明:  导览界面,的语言选择框
 */

public class SingleChoiceDialog extends RelativeLayout{

    private Context mContext;
    private View mView;
    private TextView mTittle;
    private TextView mCancelBt;
    private TextView mConfirmBt;
    private RadioGroup mRadioGroup;
    private List<VoiceBean> mVoiceBeanList;
    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener;
    private OnClickListener mOnClickListenerForConfirm;
    private OnClickListener mOnClickListenerForCancel;


    public SingleChoiceDialog(Context context,AttributeSet attr) {
        super(context, attr);
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_single_choice_alert, this, true);
        mTittle = mView.findViewById(R.id.title_tv);
        mCancelBt = mView.findViewById(R.id.cancel_tv);
        mConfirmBt = mView.findViewById(R.id.confirm_tv);
        mRadioGroup = mView.findViewById(R.id.redio_group);
    }

    /*设置单选项*/
    public void setRadio(List<VoiceBean> voiceBeanList,String voiceId){
        this.mVoiceBeanList = voiceBeanList;
        if(voiceBeanList != null && voiceBeanList.size() != 0){
            for(int i = 0;i < voiceBeanList.size();i++){
                VoiceBean voiceBean = voiceBeanList.get(i);
                RadioButton radioButton = new RadioButton(mContext);
                if(voiceId.equals(voiceBean.getId())){
                    radioButton.setChecked(true);
                }else {
                    radioButton.setChecked(false);
                }
                radioButton.setId(i);
                radioButton.setText(voiceBean.getName());
                mRadioGroup.addView(radioButton);
            }
        }
    }

    /*用于判断当前Radio是否初始化完毕*/
    public List<VoiceBean>  getRadio(){
        return mVoiceBeanList;
    }


    /*单选事件接口回调*/
    public void setOnCheckedChangeListerner(RadioGroup.OnCheckedChangeListener onCheckedChangeListener){
        this.mOnCheckedChangeListener = onCheckedChangeListener;
        mRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    /*确定按钮事件接口回调*/
    public void setOnClickListernerForConfirm(OnClickListener onClickListenerForConfirm){
        this.mOnClickListenerForConfirm = onClickListenerForConfirm;
        mConfirmBt.setOnClickListener(mOnClickListenerForConfirm);
    }

    /*取消按钮事件接回调*/
    public void setOnClickListernerForCancel(OnClickListener onClickListenerForCancel){
        this.mOnClickListenerForCancel = onClickListenerForCancel;
        mCancelBt.setOnClickListener(mOnClickListenerForCancel);
    }
}
