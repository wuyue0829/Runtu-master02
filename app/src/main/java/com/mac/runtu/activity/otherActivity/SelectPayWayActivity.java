package com.mac.runtu.activity.otherActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alipay.sdk.app.AuthTask;
import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.OrderPayMoneryInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.PayResult;
import com.mac.runtu.utils.PhoneNetWordUitls;
import com.mac.runtu.utils.ToastUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectPayWayActivity extends AppCompatActivity {

    private static final String TAG = "SelectPayWayActivity";

    @BindView(R.id.back_Iv)
    ImageView      backIv;
    @BindView(R.id.relativeLayout1)
    RelativeLayout relativeLayout1;
    @BindView(R.id.zhifubao_pay_Bt)
    Button         zhifubaoPayBt;
    @BindView(R.id.weixin_pay_Bt)
    Button         weixinPayBt;


    private static final int SDK_PAY_FLAG = 1;//支付宝支付用到

    private static final String pay_model_zfb = "1";
    private static final String pay_model_wx  = "2";

    //标记是哪个支付
    private static String pay_one = "";

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();

            res.updateConfiguration(newConfig, res.getDisplayMetrics());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                createConfigurationContext(newConfig);
            } else {
                res.updateConfiguration(newConfig, res.getDisplayMetrics());
            }
        }
        return res;
    }
    private Handler mHandler = new Handler() {

        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {

            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>)
                    msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            Map<String, String> stringStringMap = (Map<String,
                    String>) msg.obj;
            LogUtils.e(TAG, "支付结果msg=" + stringStringMap.toString());

            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                ToastUtils.makeTextShow(SelectPayWayActivity.this,
                        "支付成功");

                //SendBaoadCastBiz.send();
                EventBus.getDefault().post(GlobalConstants
                        .user_Order_Updata);


                setResult(RESULT_OK,new Intent());

                finish();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                ToastUtils.makeTextShow(SelectPayWayActivity.this,
                        "支付失败");
            }
        }

        ;
    };

    //微信支付
    private IWXAPI msgApi;
    private String orderUuid;
    private float  cost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pay_way);
        ButterKnife.bind(this);


        msgApi = WXAPIFactory.createWXAPI
                (SelectPayWayActivity.this, null);
        // 将该app注册到微信
        msgApi.registerApp(GlobalConstants.WXZF_APPID);
        //访问地址


        //info.payModel = payModel;
        //是 特产电商还是 体验农场
        //info.model = MarkerConstants.value_mode_filter_4one;

        //info.scIp =

        Intent intent = getIntent();
        orderUuid = intent.getStringExtra(GlobalConstants.key_uuid);
        cost = intent.getFloatExtra(GlobalConstants.key_allPay, 0);

        LogUtils.e(TAG, "订单的id=" + orderUuid);
        LogUtils.e(TAG, "订单的花费=" + cost);


    }

    @OnClick({R.id.back_Iv, R.id.zhifubao_pay_Bt, R.id.weixin_pay_Bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.zhifubao_pay_Bt:

                if (!PhoneNetWordUitls.isNetworkConnected(this)) {
                    ToastUtils.makeTextShow(this,"请打开网络!");
                    return;
                }


                zhifubaoPayBt.setEnabled(false);

                pay_one = pay_model_zfb;

                creatGoodJson();

                break;
            case R.id.weixin_pay_Bt:

                if (!PhoneNetWordUitls.isNetworkConnected(this)) {
                    ToastUtils.makeTextShow(this,"请打开网络!");
                    return;
                }

                //微信
                weixinPayBt.setEnabled(false);

                pay_one = pay_model_wx;

                MarkerConstants.activity2 = SelectPayWayActivity.this;

                creatGoodJson();

                break;
        }
    }

    private void creatGoodJson() {

        OrderPayMoneryInfo info = new OrderPayMoneryInfo();

        info.orderUuid = orderUuid;
        info.cost = cost;
        info.payModel = pay_one;

        if (pay_one.equals(pay_model_zfb)) {
            info.scIp = "";

        } else {
            String ipAddress = PhoneNetWordUitls.getIPAddress(this);
            info.scIp = ipAddress != null ? ipAddress :
                    "192.168.1.1";
        }

        String infojson = GSonUtil.obj2json(info);
        getDataGoodOrderStr(infojson);
    }

    private void getDataGoodOrderStr(String infojson) {

        MyHttpUtils.getStringDataFromNet(GlobalConstants
                .get_pay_way_str_url, null, GlobalConstants
                .key_info, infojson, new MyHttpUtils.OnNewWorkRequestListener
                () {


            @Override
            public void onNewWorkSuccess(String result) {

                if (result != null) {
                    try {
                        JSONObject jb = new JSONObject(result);
                        String orderInfo = jb.getString("orderInfo");
                        String orderUuid = jb.getString("orderUuid");
                        //String cost = jb.getString("cost");
                        LogUtils.e(TAG, "orderInfo=" + orderInfo);
                        LogUtils.e(TAG, "orderUuid=" + orderUuid);
                        if (!TextUtils.isEmpty(orderInfo)) {

                            //调用支付方法

                            payMonery(orderInfo);


                        } else {
                            ToastUtils.makeTextShow(SelectPayWayActivity.this,
                                    "支付失败!");
                            weixinPayBt.setEnabled(true);
                            zhifubaoPayBt.setEnabled(true);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    ToastUtils.makeTextShow(SelectPayWayActivity.this, "支付失败!");
                    weixinPayBt.setEnabled(true);
                    zhifubaoPayBt.setEnabled(true);
                }

            }

            @Override
            public void onNewWorkError(String msg) {
                ToastUtils.makeTextShowNoNet(SelectPayWayActivity.this);
            }
        });

    }

    private void payMonery(String result) {

        if (pay_one.equals(pay_model_zfb)) {
            payMoney2ZhiFuBao(result);
        } else {
            payMoney2WX(result);
        }
    }

    //微信支付
    private void payMoney2WX(String result) {
        LogUtils.e(TAG, "微信的结果result==" + result);

        // MyWXPayInfo myWXPayInfo = GSonUtil.parseJson(result, MyWXPayInfo
        // .class);
        //LogUtils.e(TAG, "解析的对象=" + myWXPayInfo.toString());

        try {
            JSONObject json = new JSONObject(result);

            // 将该app注册到微信
            msgApi.registerApp(json.getString("appid"));

            PayReq req = new PayReq();
            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
            req.appId = json.getString("appid");


            req.partnerId = json.getString("partnerid");


            req.prepayId = json.getString("prepayid");


            req.nonceStr = json.getString("noncestr");


            req.timeStamp = json.getString("timestamp");


            req.packageValue = json.getString("package");


            req.sign = json.getString("sign");
            LogUtils.e(TAG, "sign=" + req.sign);
            //req.extData			= "app data"; // optional

            msgApi.sendReq(req);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //支付宝支付
    private void payMoney2ZhiFuBao(String result) {


        MarkerConstants.activity2 = SelectPayWayActivity.this;


        final String orderInfo1 = result;   // 订单信息

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {

                AuthTask alipay = new AuthTask(SelectPayWayActivity.this);
                Map<String, String> result = alipay.authV2(orderInfo1, true);

                Message msg = new Message();
                //msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();



    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

}
