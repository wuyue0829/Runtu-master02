package com.mac.runtu.activity.otherActivity;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.mac.runtu.R;
import com.mac.runtu.business.CaptureManager;

public class CustomScanActActivity extends AppCompatActivity implements
        DecoratedBarcodeView.TorchListener {

    private static final String TAG = "CustomScanActActivity";

    Button               swichLight;
    DecoratedBarcodeView mDBV;
    private CaptureManager captureManager;
    private boolean        isLightOn;
    private ImageView      ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scan_act);

        ivBack = (ImageView) findViewById(R.id.back_Iv);
        swichLight = (Button) findViewById(R.id.btn_switch);
        mDBV = (DecoratedBarcodeView) findViewById(R.id.dbv_custom);

        mDBV.setTorchListener(this);


        // 如果没有闪光灯功能，就去掉相关按钮
        if (!hasFlash()) {
            swichLight.setVisibility(View.GONE);
        }


        //重要代码，初始化捕获
        captureManager = new CaptureManager(this, mDBV);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();


        //选择闪关灯
        swichLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLightOn) {
                    mDBV.setTorchOff();
                } else {
                    mDBV.setTorchOn();
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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
    protected void onResume() {
        super.onResume();
        captureManager.onResume();

    }

    @Override
    public void onTorchOn() {
        isLightOn = true;
    }

    @Override
    public void onTorchOff() {
        isLightOn = false;
    }

    // 判断是否有闪光灯功能
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }


}
