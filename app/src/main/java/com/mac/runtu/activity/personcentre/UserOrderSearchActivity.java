package com.mac.runtu.activity.personcentre;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mac.runtu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户订单搜索
 */
public class UserOrderSearchActivity extends AppCompatActivity {

    @BindView(R.id.back_Iv)
    ImageView    backIv;
    @BindView(R.id.et_serach)
    EditText     etSerach;
    @BindView(R.id.tv_search_btn)
    TextView     tvSearchBtn;
    @BindView(R.id.ll_serach_et)
    LinearLayout llSerachEt;
    @BindView(R.id.lv)
    ListView     lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_search);
        ButterKnife.bind(this);

        etSerach.setHint(R.string.order_seach_hint);
        initData();
    }

    private void initData() {

    }

    @OnClick({R.id.back_Iv, R.id.tv_search_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.tv_search_btn:
                //发起网络搜索

                break;
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
}
