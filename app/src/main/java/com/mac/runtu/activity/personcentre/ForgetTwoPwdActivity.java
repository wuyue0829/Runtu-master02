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
import android.widget.TableRow;

import com.mac.runtu.R;
import com.mac.runtu.business.RegisterBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.interfaceif.OnRegisterBizSuccessListener;
import com.mac.runtu.utils.MD5Utils;
import com.mac.runtu.utils.SPUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetTwoPwdActivity extends AppCompatActivity {

    @BindView(R.id.back_Iv)
    ImageView backIv;

    @BindView(R.id.et_forget_password)
    EditText etForgetPassword;

    @BindView(R.id.et_forget_password2)
    EditText etForgetPassword2;

    @BindView(R.id.imageView)
    ImageView imageView;

    //修改密码  开始隐藏
    @BindView(R.id.tr_changePwd_show)
    TableRow trChangePwdShow;
    @BindView(R.id.et_change_password)
    EditText etChangePassword;

    //修改密码  输入框
    @BindView(R.id.tr_user_changepwd)
    TableRow trUserChangepwd;

    private String password;
    private String password2;

    private String number;
    private String changePwd;
    private boolean isChangerPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd2);
        ButterKnife.bind(this);

        //得到第一页条状的数据
        Intent intent = getIntent();
        number = intent.getStringExtra(GlobalConstants.KEY_PHONENUM);
        isChangerPwd = intent.getBooleanExtra(GlobalConstants
                .key_isChangerPwd, false);

        if (isChangerPwd) {
            trChangePwdShow.setVisibility(View.VISIBLE);
            //来自修改密码
            etForgetPassword.setHint("请输入原密码");
            etForgetPassword2.setHint("请输入新密码");
            etChangePassword.setHint("请再次输入新密码");
        } else {
            etForgetPassword.setHint("请输入新密码");
            etForgetPassword2.setHint("请输入密码");
        }
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


    @OnClick({R.id.back_Iv, R.id.et_forget_password, R.id
            .et_forget_password2, R.id
            .imageView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;

            case R.id.et_forget_password:

                break;
            case R.id.et_forget_password2:

                break;
            case R.id.imageView:

                if (MarkerConstants.isChangerPwd) {
                    //修改密码
                    changePwd();
                } else {
                    //忘记密码
                    forgotPwd();
                }
                break;
        }
    }

    //修改密码
    private void changePwd() {
        //验证此一次的密码
        password = etForgetPassword.getText().toString().trim();
        password2 = etForgetPassword2.getText().toString().trim();
        String pwd = SPUtils.getString(UiUtils.getContext(),
                GlobalConstants.SP_USERPASS, "");
        changePwd = etChangePassword.getText().toString().trim();




        if (TextUtils.isEmpty(password) || TextUtils.isEmpty
                (password2) || TextUtils.isEmpty(changePwd)) {

            ToastUtils.makeTextShow(this, "内容不能为空");

            return;
        }


        if (!pwd.equals(MD5Utils.getMd5(password))) {

            //原密码错误
            ToastUtils.makeTextShow(this, "原密码不正确");
            return;

        }


        if (password2.length()<6) {

            ToastUtils.makeTextShow(ForgetTwoPwdActivity.this,"密码长度最少六位!");
            return;
        }

        if (password.equals(password2)) {
            ToastUtils.makeTextShow(ForgetTwoPwdActivity.this,"新密码与原密码一致!");
            return;
        }

        //输入的两次密码是否相同校验
        if (!password2.equals(changePwd)) {
            ToastUtils.makeTextShow
                    (ForgetTwoPwdActivity.this, "两次输入密码不同");



            return;
        }

        //清楚内存中的原密码
        SPUtils.remove(this, GlobalConstants.SP_USERPASS);

        //原密码正确
        //提交网络
        CommitData();
    }

    private void forgotPwd() {
        password = etForgetPassword.getText().toString().trim();
        changePwd = etChangePassword.getText().toString().trim();

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty
                (changePwd)) {
            ToastUtils.makeTextShow(this, "内容不能为空");
            return;
        }

        if (password.length()<6) {

            ToastUtils.makeTextShow(ForgetTwoPwdActivity.this,"密码长度最少六位!");
            return;
        }

        //密码是否相等
        if (!password.equals(changePwd)) {
            ToastUtils.makeTextShow
                    (ForgetTwoPwdActivity.this,
                            "两次输入密码不同");
            return;

        }
        CommitData();
    }

    //向服务器提交数据
    private void CommitData() {
        //提交数据
        RegisterBiz.getInstance().setUserData2NetWorkForgetPaw(number, changePwd
                , new OnRegisterBizSuccessListener() {


                    @Override
                    public void onSuccess() {

                        startActivity(new Intent
                                (ForgetTwoPwdActivity.this,
                                        LoginActivity.class));
                        ToastUtils.makeTextShow
                                (ForgetTwoPwdActivity.this, "密码设置成功!");

                        //清楚残留密码
                        SPUtils.remove(ForgetTwoPwdActivity.this,
                                GlobalConstants.SP_USERPASS);

                        finish();
                    }

                    @Override
                    public void onFail() {
                        ToastUtils.makeTextShow
                                (ForgetTwoPwdActivity.this, "失败");
                    }

                    @Override
                    public void onNetWorkFail() {
                        ToastUtils.makeTextShow
                                (ForgetTwoPwdActivity.this,
                                        "网络错误!");
                    }
                });
    }
}
