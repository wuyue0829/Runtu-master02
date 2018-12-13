package com.mac.runtu.activity.entrepreneurshiptraining;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.EntreTrainDetailInfo;
import com.mac.runtu.javabean.EntreTrainInfoFromUuid;
import com.mac.runtu.utils.Content2StrUtils;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创业培训详情页
 */
public class EpreneurshipTrainingActivity extends AppCompatActivity {

    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.tv_address_city)
    TextView  tvAddressCity;
    @BindView(R.id.tv_address_area)
    TextView  tvAddressArea;
    @BindView(R.id.tv_title)
    TextView  tvTitle;
    @BindView(R.id.tv_publishTime)
    TextView  tvPublishTime;
    @BindView(R.id.tv_hitConunt)
    TextView  tvHitConunt;
    @BindView(R.id.tv_all_content)
    TextView  tvAllContent;
    @BindView(R.id.tv_organizer)
    TextView  tvOrganizer;
    @BindView(R.id.tv_call_number)
    TextView  tvCallNumber;
    @BindView(R.id.tv_enter_train_time)
    TextView  tvEnterTrainTime;
    @BindView(R.id.tv_address)
    TextView  tvAddress;
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrepreneurship_training_details);
        ButterKnife.bind(this);

        uuid = getIntent().getStringExtra(GlobalConstants
                .key_uuid);
        initData();
    }

    private void initData() {
        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .entre_train_detail_url, null, GlobalConstants
                        .key_uuid,
                uuid, new MyHttpUtils.OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {
                        if (result != null) {
                            parseData(result);
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {

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

    //解析数据
    private void parseData(String result) {
        EntreTrainInfoFromUuid info = GSonUtil.parseJson
                (result, EntreTrainInfoFromUuid.class);
        EntreTrainDetailInfo data = info.objList;

        if (data != null) {
            tvAddressCity.setText(data.city);
            tvAddressArea.setText(data.county);

            tvTitle.setText(data.title);
            Content2StrUtils.setContentStr(data.content, tvAllContent);

            tvPublishTime.setText(TimeUtils.setCreatTime(data.createTime));
            tvHitConunt.setText(data.hitCount);
            tvOrganizer.setText(data.organizer);
            tvCallNumber.setText(data.phone);
            tvEnterTrainTime.setText(data.trainingTime);
            tvAddress.setText("培训地点：    "+data.address);
        }

    }

    @OnClick(R.id.back_Iv)
    public void onClick() {
        finish();
    }
}
