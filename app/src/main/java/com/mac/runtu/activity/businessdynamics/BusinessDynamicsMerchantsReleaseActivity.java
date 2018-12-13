package com.mac.runtu.activity.businessdynamics;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.PublishIsLoginActivity;
import com.mac.runtu.business.AddressBiz4FromPro3;
import com.mac.runtu.business.CheckCodeBiz;
import com.mac.runtu.business.TypeDataSelectFromNetType1;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.interfaceif.OnCodeTestTestListener;
import com.mac.runtu.javabean.BusinessCommitInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 客商发布
 */
public class BusinessDynamicsMerchantsReleaseActivity extends
        PublishIsLoginActivity {

    private static final String TAG =
            "BusinessDynamicsMerchantsReleaseActivity";

    @BindView(R.id.back_Iv)
    ImageView    backIv;
    @BindView(R.id.sp_type1)
    Spinner      spType1;
    @BindView(R.id.linearLayout1)
    LinearLayout linearLayout1;

    @BindView(R.id.et_title)
    EditText     etTitle;
    @BindView(R.id.et_name)
    TextView     etName;
    @BindView(R.id.et_number)
    TextView     etNumber;
    @BindView(R.id.et_code)
    EditText     etCode;
    @BindView(R.id.tv_btnCode)
    TextView     tvBtnCode;
    @BindView(R.id.release_Iv)
    ImageView    releaseIv;
    @BindView(R.id.et_des)
    EditText     etDes;
    @BindView(R.id.tv_province)
    TextView     tvProvince;
    @BindView(R.id.iv_province)
    ImageView    ivProvince;
    @BindView(R.id.tv_city)
    TextView     tvCity;
    @BindView(R.id.iv_city)
    ImageView    ivCity;
    @BindView(R.id.tv_area)
    TextView     tvArea;
    @BindView(R.id.iv_area)
    ImageView    ivArea;
    @BindView(R.id.tv_town)
    TextView     tvTown;
    @BindView(R.id.iv_town)
    ImageView    ivTown;
    private String phone;
    private String title;
    private String des;
    private String name;
    private String uuid;
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
        setContentView(R.layout.activity_business_dynamics_merchants_release);
        ButterKnife.bind(this);

        initData();

    }

    private void initData() {

        //两个类型
        new TypeDataSelectFromNetType1(this, spType1, GlobalConstants
                .AD_TYPE_BUSINESS);

        //四个地址

        new AddressBiz4FromPro3(this, null, null, tvProvince, ivProvince,
                tvCity, ivCity, tvArea,
                ivArea, tvTown, ivTown);

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
                    uuid = result;

                }
            }

            @Override
            public void onNewWorkError(String msg) {
                ToastUtils.makeTextShowNoNet
                        (BusinessDynamicsMerchantsReleaseActivity.this);
            }
        });

    }


    @OnClick({R.id.back_Iv, R.id.et_code, R.id.release_Iv, R.id.tv_btnCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:

                finish();

                break;

            case R.id.et_code:


                break;

            case R.id.tv_btnCode:

                title = getEtText(etTitle);
                des = getEtText(etDes);
                name = getEtText(etName);
                phone = getEtText(etNumber);

                if (isEtEmpty(title) || isEtEmpty(des) || isEtEmpty(name) ||
                        isEtEmpty(phone)) {
                    ToastUtils.makeTextShowContent(this);
                    return;
                }

                tvBtnCode.setEnabled(false);
                getCode();

                //

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

                setData2NetWork();
                releaseIv.setEnabled(false);
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

        //成功以后提交网络
        BusinessCommitInfo info = new BusinessCommitInfo();
        info.userUuid = UiUtils.getUserID();
        info.type = "1";
        info.kind = MarkerConstants.value_kind;
        info.province = MarkerConstants.value_province;
        info.city = MarkerConstants.value_city;
        info.county = MarkerConstants.value_county;
        info.town = MarkerConstants.value_town;
        info.title = title;
        info.content = des;
        info.contacts = name;
        info.phone = phone;
        info.uuid = uuid;

        String infoJson = GSonUtil.obj2json(info);

        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                .business_keshang_data_uploading_url, null, GlobalConstants
                .key_info, infoJson, new MyHttpUtils
                .OnNewWorkRequestListener2Boolean() {


            @Override
            public void onNewWorkSuccess(Boolean result) {

                LogUtils.e(TAG, "result=" + result);
                if (result) {
                    ToastUtils.makeTextShowDataCommitSuccess
                            (BusinessDynamicsMerchantsReleaseActivity.this);
                    setResult(RESULT_OK, new Intent());

                    finish();
                } else {
                    ToastUtils.makeTextShowDataCommitFail
                            (BusinessDynamicsMerchantsReleaseActivity.this);
                    releaseIv.setEnabled(true);
                }
            }

            @Override
            public void onNewWorkError(String msg) {
                ToastUtils.makeTextShowNoNet
                        (BusinessDynamicsMerchantsReleaseActivity.this);
                LogUtils.e(TAG, "网络忙" + msg);
                releaseIv.setEnabled(true);
            }
        });

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
