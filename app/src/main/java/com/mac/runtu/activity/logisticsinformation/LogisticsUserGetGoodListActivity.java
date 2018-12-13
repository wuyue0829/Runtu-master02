package com.mac.runtu.activity.logisticsinformation;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.adapter.CargoGoodsAdapter;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.CargoDetailInfo;
import com.mac.runtu.javabean.CargoUserPublishInfo;
import com.mac.runtu.javabean.LaborServiceDetailInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.ToastUtils;

import java.util.ArrayList;

/**
 * 用户自己发布的捎货列表页
 */
public class LogisticsUserGetGoodListActivity extends AppCompatActivity {

    private ImageView                   ivback;
    private ListView                    lv;
    private String                      uuid;
    private LaborServiceDetailInfo      data;
    private boolean                     isQitan;
    private ArrayList<CargoDetailInfo> cargoInfos;
    private String result;
    private CargoGoodsAdapter mAdapter;
    private ArrayList<CargoDetailInfo> infoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_user_get_good_list);

        lv = (ListView) findViewById(R.id.lv);
        ivback = (ImageView) findViewById(R.id.back_Iv);
        TextView tvTopName = (TextView) findViewById(R.id.tv_top_name);
        tvTopName.setText("信息列表");

        result = getIntent().getStringExtra(GlobalConstants
                .key_user);

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        parserDataQita(result);

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

    private void parserDataQita(String result) {
        CargoUserPublishInfo info = GSonUtil.parseJson(result,
                CargoUserPublishInfo.class);
        infoList = info.objList;
        if (infoList != null && infoList.size() > 0) {

                mAdapter = new CargoGoodsAdapter(this,
                        infoList);
            lv.setAdapter(mAdapter);
        } else {
            ToastUtils.makeTextShowNoData(LogisticsUserGetGoodListActivity
                    .this);
        }
    }
}
