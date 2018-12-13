package com.mac.runtu.activity.logisticsinformation;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.personcentre.LoginActivity;
import com.mac.runtu.adapter.CargoGoodsAdapter;
import com.mac.runtu.business.MyShowDialog;
import com.mac.runtu.business.ReallyNameBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnBooleanListener;
import com.mac.runtu.interfaceif.OnGetData2BooleanListener;
import com.mac.runtu.javabean.CargoDetailInfo;
import com.mac.runtu.javabean.LogisticsDetailInfo;
import com.mac.runtu.javabean.LogisticsInfoFromUuid;
import com.mac.runtu.utils.Content2StrUtils;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.TimeUtils;
import com.mac.runtu.utils.UiUtils;
import com.mac.runtu.view.MyListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 物流新信息详情页
 */
public class LogisticsInformationDetailsActivity extends AppCompatActivity {

    private static final String TAG = "LogisticsInformationDetailsActivity";
    @BindView(R.id.back_Iv)
    ImageView    backIv;
    @BindView(R.id.title_Tv)
    TextView     titleTv;
    @BindView(R.id.time_Tv)
    TextView     timeTv;
    @BindView(R.id.browse_Tv)
    TextView     browseTv;
    @BindView(R.id.content_Tv)
    TextView     contentTv;
    @BindView(R.id.crp_Lv)
    MyListView   crpLv;
    @BindView(R.id.shipment_Tv)
    TextView     shipmentTv;
    @BindView(R.id.phone_service_Iv)
    ImageView    phoneServiceIv;
    @BindView(R.id.tv_city)
    TextView     tvCity;
    @BindView(R.id.tv_area)
    TextView     tvArea;
    @BindView(R.id.tv_cartype)
    TextView     tvCartype;
    @BindView(R.id.tv_name)
    TextView     tvName;
    @BindView(R.id.tv_start_address)
    TextView     tvStartAddress;
    @BindView(R.id.tv_end_address)
    TextView     tvEndAddress;
    @BindView(R.id.tv_rount_address)
    TextView     tvRountAddress;
    @BindView(R.id.user_photo_Iv)
    ImageView    userPhotoIv;
    @BindView(R.id.relativeLayout)
    LinearLayout relativeLayout;


    private String                     uuid;
    private LogisticsDetailInfo        data;
    private ArrayList<CargoDetailInfo> cargoInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_information_details);
        ButterKnife.bind(this);

        uuid = getIntent().getStringExtra(GlobalConstants
                .key_uuid);

        LogUtils.e(TAG, "物流详情信息页uuid===" + uuid);
        initView();
        // initData();
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

    private void initView() {
        View header = getLayoutInflater().inflate(R.layout
                .logistcs_information_shipment_list_header_layout, null);
        //捎货列表
        crpLv.addHeaderView(header);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    void initData() {

        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .Logistics_info_detail_url, null, GlobalConstants
                        .key_uuid,
                uuid, new MyHttpUtils.OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {
                        if (result != null) {
                            LogUtils.e(TAG, "物流详情信息" + result);
                            parseData(result);
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {

                    }
                });


    }

    private void parseData(String result) {

        LogisticsInfoFromUuid info = GSonUtil.parseJson(result,
                LogisticsInfoFromUuid.class);
        //要捎货的信息

        cargoInfos = info.cargoInfos;

        data = info.logInfo;

        if (data != null) {
            //详情信息
            tvCity.setText("" + data.startCity);
            tvArea.setText(data.startCounty);


            tvStartAddress.setText(data.startProvince + "-" + data.startCity
                    + "-" + data.startCounty);
            tvEndAddress.setText(data.targetProvince + "-" + data.targetCity
                    + "-" + data.targetCounty);

            if (!TextUtils.isEmpty(data.route)) {
                String[] arrAddress = data.route.split("-");
                StringBuilder builder = new StringBuilder();

                if (arrAddress.length > 1) {
                    for (int i = 0; i < arrAddress.length; i++) {

                        if (i == arrAddress.length) {
                            builder.append(arrAddress[i]);
                        } else {
                            builder.append(arrAddress[i]).append(",");
                        }

                    }
                } else {
                    builder.append(arrAddress[0]);
                }


                tvRountAddress.setText(builder.toString());
            }

            //车型 货车
            if (data.kindDictionaryInfo != null) {
                tvCartype.setText(data.kindDictionaryInfo.kindName);
            }

            titleTv.setText(data.title);
            timeTv.setText(TimeUtils.setCreatTime(data.createTime));
            browseTv.setText(data.hitCount);

            tvName.setText(data.contacts);
            Content2StrUtils.setContentStr(data.content, contentTv);

            if (cargoInfos != null && cargoInfos.size() > 0) {
                setDataCargoList();
            }

        }

    }

    private void setDataCargoList() {
        // LogUtils.e();

        CargoGoodsAdapter mAdapter = new CargoGoodsAdapter(this,
                cargoInfos);
        crpLv.setAdapter(mAdapter);

    }

    @OnClick({R.id.back_Iv, R.id.phone_service_Iv, R.id.shipment_Tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.phone_service_Iv:
                if (data != null) {
                    if (UiUtils.isLogin()) {
                        MyShowDialog.ShowCallPhone(this, data.phone,data.contacts);
                    } else {
                        showDialogLoginInfo();
                    }
                }

                break;
            case R.id.shipment_Tv:

                if (data != null) {
                    showIsPublish();
                }
                break;
        }
    }

    /**
     * 发布前情况判断
     */
    private void showIsPublish() {

        //已发车拦截
        String carStuat = TimeUtils.getCarStuat(data.departureTime);
        if ("已发车".equals(carStuat)) {
            showHintInfo("车辆已经出发");
            return;
        }

        if (GlobalConstants.reaNemAttestationSucc == UiUtils.getAttestationStuts()) {
            go2GetGoodPublish();
        }else {
            new ReallyNameBiz(LogisticsInformationDetailsActivity.this).status();
        }


    }


    /**
     * 条转发补页
     */
    private void go2GetGoodPublish() {

        Intent intent = new Intent(LogisticsInformationDetailsActivity
                .this,
                LogisticsInformationIncidentallyReleaseActivity.class);

        intent.putExtra(GlobalConstants.key_uuid, data.uuid);

        LogUtils.e(TAG, "详情信息第uuid==" + data.uuid);

        startActivity(intent);
    }




    private void showHintInfo(String content) {
        MyShowDialog.showDialogSelectImage("提示信息:",content,
                LogisticsInformationDetailsActivity.this, new
                        OnBooleanListener() {


                    @Override
                    public void onResultIsTrue() {

                    }
                });
    }

    /**
     * 提示用户没用登录
     */
    private void showDialogLoginInfo() {
        MyShowDialog.showDialogInfo(this, new OnGetData2BooleanListener() {
            @Override
            public void onSuccess(Boolean result) {
                startActivity(new Intent(LogisticsInformationDetailsActivity
                        .this, LoginActivity.class));
            }

            @Override
            public void onFail() {

            }
        });
    }

}
