package com.guolaiwan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.cgx.library.base.BaseActivity;
import com.guolaiwan.bean.SelectPicEvent;

import org.greenrobot.eventbus.EventBus;

import app.guolaiwan.com.guolaiwan.R;
import butterknife.BindView;

public class AddTextActivity extends BaseActivity {
    @BindView(R.id.et_content)
    EditText editText;
    private SelectPicEvent event;

    public static void launcher(Activity context, SelectPicEvent event){
        Intent intent = new Intent();
        intent.setClass(context,AddTextActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("event",event);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getTitleBar().setRightText("完成", v -> {
            event.text = editText.getText().toString();
            EventBus.getDefault().post(event);
            finish();
        }).setLeftText("取消", v -> finish()).setTitle("编辑文字").show();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activty_edit_text);
        editText.setText(TextUtils.isEmpty(event.text)?"":event.text);
    }

    @Override
    protected void initData() {
        if (getIntent().getExtras() != null) {
            event = (SelectPicEvent) getIntent().getSerializableExtra("event");
        }
    }

    @Override
    protected void initEvent() {

    }
}
