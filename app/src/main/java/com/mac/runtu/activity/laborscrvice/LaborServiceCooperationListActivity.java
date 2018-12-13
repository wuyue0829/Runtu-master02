package com.mac.runtu.activity.laborscrvice;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.mac.runtu.R;
import com.mac.runtu.adapter.LaborServiceAdapter;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.LaborServiceDetailInfo;
import com.mac.runtu.javabean.LaborServiceInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.view.PullToRefreshListView;

import java.util.ArrayList;

/**
 * 用户发布的劳务列表页
 */
public class LaborServiceCooperationListActivity extends AppCompatActivity {


    private ArrayList<LaborServiceDetailInfo> mAllList = new ArrayList<>();

    private LaborServiceAdapter mAdapter;

    private ListView  lv;
    private ImageView ivback;
    private int       num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labor_service_cooperation_list);

        lv = (PullToRefreshListView) findViewById(R.id.lv);
        ivback = (ImageView) findViewById(R.id.back_Iv);

        initListener();

        String result = getIntent().getStringExtra(GlobalConstants
                .key_user);




        parserData(result);
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

    private void initListener() {

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {
               /* Toast.makeText(LaborServiceCooperationActivity.this, "click:"
                        + position, Toast.LENGTH_SHORT).show();*/

                //跳转详情页
                Intent intent = new Intent(LaborServiceCooperationListActivity
                        .this, LaborServiceCooperationDetailsActivity
                        .class);
                intent.putExtra(GlobalConstants.key_uuid, mAllList.get
                        (position).uuid);
                startActivity(intent);

            }
        });



        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //解析数据
    private void parserData(String result) {
        LaborServiceInfo info = GSonUtil.parseJson(result,
                LaborServiceInfo.class);

        num = info.num;
        mAllList = info.objList;

        if (mAllList != null && mAllList.size() > 0) {
            mAdapter = new LaborServiceAdapter
                    (LaborServiceCooperationListActivity.this, mAllList);
            lv.setAdapter(mAdapter);
        }
    }

}
