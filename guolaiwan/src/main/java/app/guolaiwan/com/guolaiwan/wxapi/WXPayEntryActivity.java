package app.guolaiwan.com.guolaiwan.wxapi;

import android.support.v7.app.AlertDialog;

import com.cgx.library.utils.log.LogUtil;
import com.guolaiwan.utils.CommonUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/26
 * 描述:
 */

public class WXPayEntryActivity extends WXCallbackActivity {

    @Override
    public void onResp(BaseResp resp) {
        super.onResp(resp);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0){
                CommonUtils.showMsg("支付成功");
                EventBus.getDefault().post("closePayActivity");
            }else {
                LogUtil.d("WeChatPay","errCode=" + resp.errCode);
                CommonUtils.showMsg("支付失败");
            }
            finish();
        }
    }
}
