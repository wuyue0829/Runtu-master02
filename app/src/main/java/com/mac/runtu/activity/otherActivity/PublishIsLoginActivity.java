package com.mac.runtu.activity.otherActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import com.mac.runtu.activity.personcentre.LoginActivity;
import com.mac.runtu.utils.UiUtils;

/**
 * Description:判断登录
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/27 1:10
 */
@SuppressLint("Registered")
public class PublishIsLoginActivity extends AppCompatActivity {

    private static final int requestCode_login = 0;
    private boolean isLogin;


    @Override
    protected void onResume() {
        super.onResume();

        isLogin = UiUtils.isLogin();

        if (!isLogin) {
            //跳转登录页
            startActivityForResult(new Intent(this, LoginActivity.class),
                    requestCode_login);
            //finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case requestCode_login:

                if (!UiUtils.isLogin()) {
                    finish();
                }
                break;

            default:
                break;
        }

    }
}
