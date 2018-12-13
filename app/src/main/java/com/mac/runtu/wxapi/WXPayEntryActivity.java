package com.mac.runtu.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.utils.LogUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, GlobalConstants.WXZF_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtils.e(TAG, "onPayFinish, errCode = " + resp.errCode);


        switch (resp.errCode) {
            case 0:
                showDialog("支付成功!");
                //更新个人中心的数据
                //RefreshOrderBiz.refresh3OrderData();

                //LoginBiz.getInstance().getUserOrdrData();

                //SendBaoadCastBiz.send();
                EventBus.getDefault().post(GlobalConstants
                        .user_Order_Updata);


                MarkerConstants.activity.finish();
                MarkerConstants.activity2.finish();

                break;
            case -1:
                showDialog("支付失败!");
                break;
            case -2:
                finish();
                break;

            default:
                break;
        }


    }

    private void showDialog(String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.weixin_result_layou,
                null);

        TextView tvResult = (TextView) dialogView.findViewById(R.id
                .tv_result);
        tvResult.setText(result);
        Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        dialog.setCancelable(false);//不可以取消(点击弹窗外侧,返回键,弹窗不消失)

        dialog.show();

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params =
                window.getAttributes();
        window.setAttributes(params);
    }


}