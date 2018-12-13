package com.mac.runtu.activity.logisticsinformation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.PublishIsLoginActivity;
import com.mac.runtu.business.AddressSelectDataFinalFromPro2;
import com.mac.runtu.business.CheckCodeBiz;
import com.mac.runtu.business.TypeDataSelectFromNetType1;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.interfaceif.OnCodeTestTestListener;
import com.mac.runtu.javabean.CargoCommitInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 物流发布页面 --捎货
 */
public class LogisticsInformationIncidentallyReleaseActivity extends
        PublishIsLoginActivity {

    private static final String TAG =
            "LogisticsInformationIncidentallyReleaseActivity";
    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.sp_type1)
    Spinner   spType1;
    @BindView(R.id.et_weight)
    EditText  etWeight;
    @BindView(R.id.sp_prov)
    Spinner   spProv;
    @BindView(R.id.sp_city)
    Spinner   spCity;
    @BindView(R.id.sp_area)
    Spinner   spArea;
    @BindView(R.id.sp_town)
    Spinner   spTown;
    @BindView(R.id.et_name)
    TextView  etName;
    @BindView(R.id.et_number)
    TextView  etNumber;
    @BindView(R.id.et_code)
    EditText  etCode;
    @BindView(R.id.tv_btnCode)
    TextView  tvBtnCode;
    @BindView(R.id.release_Iv)
    ImageView releaseIv;
    private String uuid;
    private String weight;
    private String name;
    private String phone;
    private String logID;

    private String mCode4Net;


    private int getCodeTotalTime = 60;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            getCodeTotalTime--;
            tvBtnCode.setText("剩余"+getCodeTotalTime+"秒");
            if (getCodeTotalTime == 0) {
                tvBtnCode.setText(R.string.get_verification_code);
                tvBtnCode.setEnabled(true);
                getCodeTotalTime = 60;
                mHandler.removeMessages(0);
            }else {
                mHandler.sendEmptyMessageDelayed(0,1000);
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout
                .activity_logistics_information_incidentally_release);
        ButterKnife.bind(this);

        logID = getIntent().getStringExtra(GlobalConstants
                .key_uuid);
        LogUtils.e(TAG,"跳转到捎货页面时logID=="+logID);
        initData();
    }

    private void initData() {
        //类型
        new TypeDataSelectFromNetType1(this, spType1, GlobalConstants
                .AD_TYPE_take_goods);

        new AddressSelectDataFinalFromPro2(this, spProv, spCity, spArea, spTown,null);

        etName.setText(UiUtils.getUserName());
        etNumber.setText(UiUtils.getUserNumber());

        getMainUuid();
    }

    private void getMainUuid() {
        //访问网络得到uuid
        MyHttpUtils.getStringDataFromNet(GlobalConstants
                .business_data_commiet_uuid_url, null, new MyHttpUtils
                .OnNewWorkRequestListener() {

            @Override
            public void onNewWorkSuccess(String result) {
                if (result != null) {
                    uuid = result.toString();
                    LogUtils.e(TAG, "网络获得的uuid" + uuid);


                }
            }

            @Override
            public void onNewWorkError(String msg) {
                ToastUtils.makeTextShowNoNet
                        (LogisticsInformationIncidentallyReleaseActivity.this);
            }
        });

    }

    @OnClick({R.id.back_Iv, R.id.tv_btnCode, R.id.release_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.tv_btnCode:

                //判断是否要登陆
                if (!UiUtils.isLogin()) {
                    UiUtils.go2LoginAcivity();
                    return;
                }

                weight = getEtText(etWeight);
                name = getEtText(etName);
                phone = getEtText(etNumber);

                if (isEtEmpty(weight) || isEtEmpty(name) || isEtEmpty(phone)) {
                    ToastUtils.makeTextShowContent
                            (LogisticsInformationIncidentallyReleaseActivity
                                    .this);
                    return;
                }

                tvBtnCode.setEnabled(false);
                getCode();
                break;
            case R.id.release_Iv:

                //判断是否要登陆
                if (!UiUtils.isLogin()) {
                    UiUtils.go2LoginAcivity();
                    return;
                }

                String mCode = etCode.getText().toString().trim();
                if (TextUtils.isEmpty(mCode)) {
                    ToastUtils.makeTextShowCodeNotEmpty(this);
                    return;
                }

                if (TextUtils.isEmpty(uuid)) {
                    return;
                }

                if (!mCode.equals(mCode4Net)) {
                    ToastUtils.makeTextShowCodeError(this);
                    return;
                }
                releaseIv.setEnabled(false);
                setData2NetWork();

                break;
        }
    }



    private void getCode() {

        mHandler.sendEmptyMessageDelayed(0,1000);
        tvBtnCode.setText("剩余"+getCodeTotalTime+"秒");

        //访问网络
        //得到验证码
        CheckCodeBiz.getCode(phone, new OnCodeTestTestListener() {

            @Override
            public void onData(String code) {

                mCode4Net = code;

                //解封按钮


                LogUtils.e(TAG, "网络获得code=" + code);



            }

            @Override
            public void onNoData() {
                //服务器没有返回验证码
            }

            @Override
            public void onNetWorkFail() {

            }
        });

    }

    private void setData2NetWork() {

        weight = getEtText(etWeight);
        name = getEtText(etName);
        phone = getEtText(etNumber);

        CargoCommitInfo info = new CargoCommitInfo();

        info.userUuid = UiUtils.getUserID();
        info.kind = MarkerConstants.value_kind;

        info.targetProvince = MarkerConstants.value_fainl_province;
        info.targetCity = MarkerConstants.value_fainl_city;
        info.targetCounty = MarkerConstants.value_fainl_county;
        info.targetTown = MarkerConstants.value_fainl_town;
        info.targetVillage = MarkerConstants.value_fainl_village;

        info.logID = logID;

        info.phone = phone;
        info.publisher = name;
        info.phone = phone;
        info.weight = weight;

        //进入页面获得的id
        info.uuid = uuid;

        String infoJson = GSonUtil.obj2json(info);

        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                .Logistics_add_good_commit_url, null, GlobalConstants
                .key_info, infoJson, new MyHttpUtils
                .OnNewWorkRequestListener2Boolean() {


            @Override
            public void onNewWorkSuccess(Boolean result) {

                //LogUtils.e(TAG, "result=" + result);
                if (result) {
                    ToastUtils.makeTextShowDataCommitSuccess
                            (LogisticsInformationIncidentallyReleaseActivity
                                    .this);

                    sendEvent();

                    finish();
                } else {
                    ToastUtils.makeTextShowDataCommitFail
                            (LogisticsInformationIncidentallyReleaseActivity
                                    .this);

                    releaseIv.setEnabled(true);
                }
            }

            @Override
            public void onNewWorkError(String msg) {
                ToastUtils.makeTextShowNoNet
                        (LogisticsInformationIncidentallyReleaseActivity.this);
                //LogUtils.e(TAG, "网络忙" + msg);
                releaseIv.setEnabled(true);
            }
        });
    }

    /**
     * 发送通知 主页面更新数据
     */
    private void sendEvent() {

        EventBus.getDefault().post(GlobalConstants.send_updata_Logistics);
    }

    public String getEtText(EditText et) {
        return et.getText().toString().trim();
    }

    public String getEtText(TextView et) {
        return et.getText().toString().trim();
    }

    public boolean isEtEmpty(String etStr) {
        return TextUtils.isEmpty(etStr);
    }
}
