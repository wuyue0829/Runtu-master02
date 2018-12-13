package com.mac.runtu.activity.logisticsinformation;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.business.CheckCodeBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnCodeTestTestListener;
import com.mac.runtu.javabean.LogisticsCommitInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 物流发布第二页
 */
public class LogisticsInformationRelease2Activity extends AppCompatActivity {

    private static final String TAG = "LogisticsInformationRelease2Activity";
    @BindView(R.id.back_Iv)
    ImageView backIv;
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
    private LogisticsCommitInfo mInfo;
    private String              name;
    private String              phone;

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
                mCode4Net ="";
            }else {
                mHandler.sendEmptyMessageDelayed(0,1000);
            }


        }
    };


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_information_release2);
        ButterKnife.bind(this);

        mInfo = (LogisticsCommitInfo) getIntent().getSerializableExtra
                (GlobalConstants.key_info);

        initData();
        getMainUuid();

    }

    private void initData() {
        etName.setText(UiUtils.getUserName());
        etNumber.setText(UiUtils.getUserNumber());

    }

    private void getMainUuid() {
        //访问网络得到uuid
        MyHttpUtils.getStringDataFromNet(GlobalConstants
                .business_data_commiet_uuid_url, null, new MyHttpUtils
                .OnNewWorkRequestListener() {

            @Override
            public void onNewWorkSuccess(String result) {
                if (result != null) {
                    uuid =  result.toString();
                    LogUtils.e(TAG,"网络获得第uuid"+uuid);

                }
            }

            @Override
            public void onNewWorkError(String msg) {
                ToastUtils.makeTextShowNoNet
                        (LogisticsInformationRelease2Activity.this);
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

                name = getEtText(etName);
                phone = getEtText(etNumber);

                if (isEtEmpty(name) || isEtEmpty(phone)) {
                    ToastUtils.makeTextShowContent
                            (LogisticsInformationRelease2Activity.this);
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
        //判断验证码

        //成功以后提交网络

        mInfo.contacts = name;
        mInfo.phone = phone;
        mInfo.userUuid = UiUtils.getUserID();
        mInfo.uuid = uuid;

        String infoJson = GSonUtil.obj2json(mInfo);

        LogUtils.e(TAG,"物理发布json="+infoJson);

        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                .Logistics_commit_url, null, GlobalConstants
                .key_info, infoJson, new MyHttpUtils
                .OnNewWorkRequestListener2Boolean() {


            @Override
            public void onNewWorkSuccess(Boolean result) {

                //LogUtils.e(TAG, "result=" + result);
                if (result) {
                    ToastUtils.makeTextShowDataCommitSuccess
                            (LogisticsInformationRelease2Activity.this);
                    setResult(RESULT_OK,new Intent());
                    finish();
                } else {
                    ToastUtils.makeTextShowDataCommitFail
                            (LogisticsInformationRelease2Activity.this);
                    releaseIv.setEnabled(true);
                }
            }

            @Override
            public void onNewWorkError(String msg) {
                ToastUtils.makeTextShowNoNet
                        (LogisticsInformationRelease2Activity.this);
                //LogUtils.e(TAG, "网络忙" + msg);
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
