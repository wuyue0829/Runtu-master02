package com.mac.runtu.activity.personcentre;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.mac.runtu.R;
import com.mac.runtu.business.RegisterBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnRegisterBizSuccessListener;
import com.mac.runtu.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistTwoActivity extends AppCompatActivity {

    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.reg_Iv)
    ImageView regIv;
    @BindView(R.id.et_password)
    EditText  etPassword;
    @BindView(R.id.et_password2)
    EditText  etPassword2;
    private String password;
    private String password2;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist2);
        ButterKnife.bind(this);

        number = getIntent().getStringExtra(GlobalConstants.KEY_PHONENUM);


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

    @OnClick({R.id.back_Iv, R.id.reg_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.reg_Iv:

                password = etPassword.getText().toString().trim();
                password2 = etPassword2.getText().toString().trim();

                if (TextUtils.isEmpty(password) || TextUtils.isEmpty
                        (password2)) {

                    ToastUtils.makeTextShow(RegistTwoActivity
                            .this, "密码不能为空!");
                    return;
                }

                if (password.length()<6) {

                    ToastUtils.makeTextShow(RegistTwoActivity
                            .this, "密码长度最少六位!");
                    return;
                }



                //密码是否相等
                if (password.equals(password2)) {
                    //提交数据
                    RegisterBiz.getInstance().setRegister2NetWork(number,
                            password, new OnRegisterBizSuccessListener() {


                                @Override
                                public void onSuccess() {
                                    //成功跳转登录页
                                    startActivity(new Intent
                                            (RegistTwoActivity.this,
                                                    LoginActivity.class));
                                    ToastUtils.makeTextShow(RegistTwoActivity
                                            .this, "注册成功!");

                                    finish();
                                }

                                @Override
                                public void onFail() {
                                    ToastUtils.makeTextShow(RegistTwoActivity
                                            .this, "注册失败");
                                }

                                @Override
                                public void onNetWorkFail() {
                                    ToastUtils.makeTextShow(RegistTwoActivity
                                            .this, "网络错误!");
                                }
                            });
                } else {
                    ToastUtils.makeTextShow(RegistTwoActivity.this, "两次密码不一致!");
                }

                break;

        }
    }


}
