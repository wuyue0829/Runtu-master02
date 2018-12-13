package com.mac.runtu.activity.personcentre;

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
import android.widget.EditText;
import android.widget.ImageView;

import com.mac.runtu.R;
import com.mac.runtu.business.CheckCodeBiz;
import com.mac.runtu.business.RegisterBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnCodeTestTestListener;
import com.mac.runtu.interfaceif.OnRegisterBizNumberTestListener;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.NumberUtils;
import com.mac.runtu.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistOneActivity extends AppCompatActivity {

    private static final String TAG = "RegistOneActivity";

    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.vc_Iv)
    Button vcIv;
    @BindView(R.id.next_Iv)
    ImageView nextIv;


    private        String   number;
    private static String   mCode;
    private        EditText etNumber;
    private        EditText etCode;
    private static String   getUserCode;


    //验证码时间校验
    private long mCodeTimeSpace = 60 * 1000;
    private long    startGetCodeTime;
    private boolean isGetCode;
    private boolean isGetCode01 = true;
    private int getCodeTotalTime = 60;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            getCodeTotalTime--;
            vcIv.setText(getCodeTotalTime+"秒");
            if (getCodeTotalTime == 0) {
                vcIv.setText("验证码");
                vcIv.setBackgroundResource(R.drawable.bg_btn_code_yellow);
                vcIv.setEnabled(true);
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
        setContentView(R.layout.activity_regist1);
        ButterKnife.bind(this);


        etNumber = (EditText) findViewById(R.id.et_Number);
        etCode = (EditText) findViewById(R.id.et_code);

        nextIv.setEnabled(false);


    }

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

    @OnClick({R.id.back_Iv, R.id.vc_Iv, R.id.next_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();

                break;
            case R.id.vc_Iv:
                //获得验证码

                number = etNumber.getText().toString().trim();

                if (TextUtils.isEmpty(number)) {
                    ToastUtils.makeTextShow(this,"内容不能为空");
                    return;
                }

                //正则匹配
                if (!NumberUtils.isNumnberMatch(number)) {
                    // LogUtils.e(TAG, "号码正则验证成功");
                    ToastUtils.makeTextShowIsPhone(RegistOneActivity.this);
                    return;
                }


                if (isGetCode) {
                    getCode();
                }else {
                    vcIv.setEnabled(false);
                    checkUserNumber();
                }

                break;
            case R.id.next_Iv:
                //下一页
                //验证验证码
                getUserCode = etCode.getText().toString().trim();

                //mIntUserCode = Integer.valueOf(getUserCode);

                LogUtils.e(TAG, "getUserCode" + getUserCode);
                if (TextUtils.isEmpty(getUserCode)) {
                    ToastUtils.makeTextShowCodeNotEmpty(RegistOneActivity.this);
                    return;
                }

                codeText();


                break;
        }
    }

    /**
     * 验证号码
     */
    private void checkUserNumber() {

        RegisterBiz.getInstance().checkUserNumber(number,

                new OnRegisterBizNumberTestListener() {

                    @Override
                    public void onData(Boolean isUse) {
                        getCode();
                        //开始计时获得验证码的时间
                    }

                    @Override
                    public void onNoData() {

                        vcIv.setEnabled(true);
                        ToastUtils.makeTextShow(RegistOneActivity
                                .this, "号码不可用!");
                    }

                    @Override
                    public void onNetWorkFail() {

                        vcIv.setEnabled(true);
                        ToastUtils.makeTextShow(RegistOneActivity
                                .this, "网络不可用");
                    }
                }

        );
    }



    /**
     * 得到验证码
     */
    private void getCode() {
        //计时
        vcIv.setBackgroundResource(R.drawable.bg_btn_code);
        vcIv.setText(getCodeTotalTime+"秒");

        mHandler.sendEmptyMessageDelayed(0,1000);
        getCodeFromNetWork();
    }




    private void codeText() {
        //网络好的的验证码不能为空
        if (!TextUtils.isEmpty(mCode)) {

            LogUtils.e(TAG, "mCode" + mCode);

            if (!getUserCode.equals(mCode)) {
                ToastUtils.makeTextShow(RegistOneActivity.this,
                        "验证码不正确");
                return;
            }

            go2Next();

        }
    }


    private void go2Next() {
        Intent intent = new Intent(this, RegistTwoActivity.class);
        //电话号码带过去
        intent.putExtra(GlobalConstants.KEY_PHONENUM, number);
        startActivity(intent);
        finish();
    }

    //从网络忽的验证码
    private void getCodeFromNetWork() {

        //得到验证码
        CheckCodeBiz.getCode(number, new OnCodeTestTestListener() {

            @Override
            public void onData(String code) {

                mCode = code;

                LogUtils.e(TAG, "网络获得code=" + code);

                //得到验证码
                isGetCode = true;

                //解封按钮
                nextIv.setEnabled(true);
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




}

