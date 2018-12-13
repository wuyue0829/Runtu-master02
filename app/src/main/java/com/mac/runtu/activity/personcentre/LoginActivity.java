package com.mac.runtu.activity.personcentre;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.MainActivity;
import com.mac.runtu.application.MyApplication;
import com.mac.runtu.business.LoginBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.UserInfo;
import com.mac.runtu.utils.PhoneNetWordUitls;
import com.mac.runtu.utils.SPUtils;
import com.mac.runtu.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.username_Et)
    EditText usernameEt;
    @BindView(R.id.username_line)
    View     usernameLine;
    @BindView(R.id.password_Et)
    EditText passwordEt;
    @BindView(R.id.password_line)
    View     passwordLine;

    @BindView(R.id.forgot_pwd_Tv)
    TextView forgotPwdTv;
    @BindView(R.id.login_Iv)
    Button   loginIv;
    @BindView(R.id.reg_Iv)
    Button   regIv;

    @BindView(R.id.tv_content)
    TextView     tvContent;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        LoginBiz.getInstance().clearUserlocationData();


        if (!PhoneNetWordUitls.isNetworkConnected(this)) {
            ToastUtils.makeTextShow(this, "请打开网络");
            return;
        }

        llLoading.setVisibility(View.GONE);
        tvContent.setText("登录中...");

        MyApplication.instance.stopCheckUser();


    }

    @OnClick({R.id.login_Iv, R.id.reg_Iv, R.id.forgot_pwd_Tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_Iv:

                String number = usernameEt.getText().toString().trim();

                String password = passwordEt.getText().toString().trim();


                if (TextUtils.isEmpty(number) || TextUtils.isEmpty(password)) {
                    ToastUtils.makeTextShowEmpty(this);
                    return;
                }


                llLoading.setVisibility(View.VISIBLE);

                LoginBiz.getInstance().loginFromActivity(number,
                        password, new LoginBiz.OnLoginListener() {

                            @Override
                            public void isLogingSuccess(UserInfo.UserDesInfo
                                                                info) {

                                ToastUtils.makeTextShow(LoginActivity.this,
                                        "登录成功");

                                setResult(RESULT_OK);

                                isFromXiaXianBtn();

                                finish();
                            }

                            @Override
                            public void isLogingFail() {


                                llLoading.setVisibility(View.GONE);

                                ToastUtils.makeTextShow(LoginActivity.this,
                                        "用户名或密码错误");

                            }

                            @Override
                            public void onNetWorkFail() {

                                llLoading.setVisibility(View.GONE);
                                ToastUtils.makeTextShow(LoginActivity.this,
                                        "无可用网络");


                            }
                        });

                //startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.reg_Iv:

                //跳转协议页面

                Intent intent = new Intent(this, AgreementTextActivity.class);
                intent.putExtra(GlobalConstants.key_kind, 1);
                startActivity(intent);

                break;
            case R.id.forgot_pwd_Tv:
                startActivity(new Intent(this, ForgetOnePwdActivity.class));
                break;
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        isFromXiaXianBtn();

    }




    //是否是点击个人中心退出登录按钮
    private void isFromXiaXianBtn() {

        boolean isFromXiaXian = SPUtils.getBoolean(LoginActivity.this,
                GlobalConstants
                        .sp_tuichu_deng_lu, false);
        if (isFromXiaXian) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        //不管是否来自退出登录按钮,删除标记
        SPUtils.remove(LoginActivity.this, GlobalConstants
                .sp_tuichu_deng_lu);
    }

}
