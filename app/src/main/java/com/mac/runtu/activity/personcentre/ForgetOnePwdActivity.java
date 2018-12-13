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

/**
 * 忘记密码第一页
 */
public class ForgetOnePwdActivity extends AppCompatActivity {

    private static final String TAG = "ForgetOnePwdActivity";

    @BindView(R.id.vc_Iv)
    Button    vcIv;
    @BindView(R.id.next_Iv)
    ImageView nextIv;
    @BindView(R.id.back_Iv)
    ImageView backIv;

    @BindView(R.id.et_forget_number)
    EditText etForgetNumber;
    @BindView(R.id.et_forget_code)
    EditText etForgetCode;

    private String number;
    private String mCode;


    private boolean isGetCode   = false;

    private String  code;
    private boolean isChangerPwd;
    private int getCodeTotalTime = 60;

    /**
     * 验证码计时
     */
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            getCodeTotalTime--;
            vcIv.setText(getCodeTotalTime+"秒");
            if (getCodeTotalTime == 0) {
                vcIv.setText("验证码");
                vcIv.setBackgroundResource(R.drawable.bg_btn_code_yellow);
                getCodeTotalTime = 60;
                mHandler.removeMessages(0);
                vcIv.setEnabled(true);
            }else {
                mHandler.sendEmptyMessageDelayed(0,1000);
            }


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd1);
        ButterKnife.bind(this);

        isChangerPwd = getIntent().getBooleanExtra("isChangerPwd",
                false);



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

    @OnClick({R.id.vc_Iv, R.id.next_Iv, R.id.back_Iv})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.vc_Iv:
                //得到输入的内容
                number = etForgetNumber.getText().toString().trim();

                if (TextUtils.isEmpty(number)) {
                    ToastUtils.makeTextShow
                            (this, "内容不能为空!");
                    return;
                }


                //正则匹配
                if (!NumberUtils.isNumnberMatch(number)) {
                    // LogUtils.e(TAG, "号码正则验证成功");
                    ToastUtils.makeTextShowIsPhone(ForgetOnePwdActivity.this);
                    return;
                }




                if (isGetCode) {

                    getCode();
                } else {
                    vcIv.setEnabled(false);
                    checkUserNumber();
                }


                break;

            case R.id.next_Iv:


                codeText();

                break;

            case R.id.back_Iv:

                finish();

                break;
        }

    }

    /**
     * 验证用户验证码
     */
    private void checkUserNumber() {
        //验证号码可用性并获得验证码
        RegisterBiz.getInstance().checkUserNumberExist(number,

                new OnRegisterBizNumberTestListener() {

                    @Override
                    public void onData(Boolean isUse) {
                        //号码可用

                        getCode();

                    }

                    @Override
                    public void onNoData() {
                        vcIv.setEnabled(true);
                        ToastUtils.makeTextShow
                                (ForgetOnePwdActivity.this,
                                        "号码不可用!");
                    }

                    @Override
                    public void onNetWorkFail() {
                        vcIv.setEnabled(true);
                        ToastUtils.makeTextShow
                                (ForgetOnePwdActivity.this,
                                        "网络不可用");
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


    //从网络忽的验证码
    private void getCodeFromNetWork() {
        //得到验证码
        CheckCodeBiz.getCode(number, new OnCodeTestTestListener() {

            @Override
            public void onData(String code) {
                mCode = code;
                //解封按钮
                nextIv.setEnabled(true);
                LogUtils.e(TAG, "得到的验证码=" + code);
                isGetCode = true;

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



    //验证码测试
    private void codeText() {

        code = etForgetCode.getText().toString().trim();

        if (TextUtils.isEmpty(code)) {

            ToastUtils.makeTextShow(ForgetOnePwdActivity.this,
                    "验证码不能为空");
            return;
        }


        if (!TextUtils.isEmpty(mCode)) {

            if (!code.equals(mCode)) {
                ToastUtils.makeTextShow(ForgetOnePwdActivity.this,
                        "验证码不正确");
                return;
            }

            go2Next();

        }
    }


    private void go2Next() {
        Intent intent = new Intent(this, ForgetTwoPwdActivity
                .class);
        //电话号码带过去
        intent.putExtra(GlobalConstants.KEY_PHONENUM, number);
        intent.putExtra(GlobalConstants.key_isChangerPwd, isChangerPwd);
        startActivity(intent);
        finish();
    }


}
