package com.mac.runtu.activity.otherActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.business.LoginBiz;
import com.mac.runtu.utils.SPUtils;

/**
 * 闪屏页
 */
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private RelativeLayout rlSplash;
    private AnimationSet   set;

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

        //LoginBiz.getInstance().getUserOrdrData();

        setContentView(R.layout.activity_splash);

        //底层默认获得用户数据
        LoginBiz.getInstance().getUserInfo4Net();

        TextView tvVersionsName = (TextView) findViewById(R.id
                .tv_versions_name);

        //设置版权名称
        if (tvVersionsName != null) {
            tvVersionsName.setText("V  " + getVersion());
        }

        rlSplash = (RelativeLayout) findViewById(R.id.rl_splash01);


        //渐变
        AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
        animAlpha.setDuration(2000);


        rlSplash.startAnimation(animAlpha);


        animAlpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                boolean is_guide_show = SPUtils.getBoolean(SplashActivity
                                .this, "is_guide_show",
                        false);

                //将来改
               // if (is_guide_show) {
                    //startActivity(new Intent(SplashActivity.this,
                            //MainActivity.class));
                //} else {
                    startActivity(new Intent(SplashActivity.this,
                            GuideActivity.class));
                //}
                //跳转主页

                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public String getVersion() {
        String versionName = "";
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


}
