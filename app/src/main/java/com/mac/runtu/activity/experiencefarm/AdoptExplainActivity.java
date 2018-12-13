package com.mac.runtu.activity.experiencefarm;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mac.runtu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 领养引导
 */
public class AdoptExplainActivity extends AppCompatActivity {


    @BindView(R.id.btn_my_lingyang)
    Button         btnMyLingyang;
    @BindView(R.id.back_Iv)
    ImageView      backIv;
    @BindView(R.id.shopping_cart_Tv)
    ImageView      shoppingCartTv;
    @BindView(R.id.title_RL)
    RelativeLayout titleRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_explain);
        ButterKnife.bind(this);
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


    @OnClick({R.id.back_Iv, R.id.btn_my_lingyang})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.btn_my_lingyang:
                startActivity(new Intent(AdoptExplainActivity.this,
                        ExperienceFarmSlidingMenuActivity.class));
                finish();
                break;
        }
    }
}
