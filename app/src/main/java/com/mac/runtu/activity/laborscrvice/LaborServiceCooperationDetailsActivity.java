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
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.personcentre.LoginActivity;
import com.mac.runtu.adapter.LaborServiceAdapter;
import com.mac.runtu.business.MyShowDialog;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnGetData2BooleanListener;
import com.mac.runtu.javabean.LaborServiceDetailInfo;
import com.mac.runtu.javabean.LaborServiceInfo;
import com.mac.runtu.javabean.LaborServiceInfoFromUuid;
import com.mac.runtu.utils.Content2StrUtils;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.TimeUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 劳务合做详情页
 */
public class LaborServiceCooperationDetailsActivity extends AppCompatActivity {
    private static final String TAG = "LaborServiceCooperationDetailsActivity";
    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.release_Iv)
    ImageView releaseIv;
    @BindView(R.id.title_Tv)
    TextView  titleTv;
    @BindView(R.id.content_Tv)
    TextView  contentTv;
    @BindView(R.id.lv)
    ListView  lv;

    @BindView(R.id.tv_city)
    TextView  tvCity;
    @BindView(R.id.tv_area)
    TextView  tvArea;
    @BindView(R.id.tv_town)
    TextView  tvTown;
    @BindView(R.id.tv_worktype)
    TextView  tvWorktype;
    @BindView(R.id.tv_tilte)
    TextView  tvTilte;
    @BindView(R.id.tv_creattime)
    TextView  tvCreatTime;
    @BindView(R.id.tv_worktime)
    TextView  tvWorkTime;
    @BindView(R.id.tv_hitConunt)
    TextView  tvHitConunt;
    @BindView(R.id.tv_publishName)
    TextView  tvPublishName;
    @BindView(R.id.iv_callphone)
    ImageView ivCallphone;

    private String                            uuid;
    private LaborServiceDetailInfo            data;
    private boolean                           isQitan;
    private ArrayList<LaborServiceDetailInfo> infoList;
    private LaborServiceAdapter               mAdapter;
    private boolean isDownListView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labor_service_cooperation_details);
        ButterKnife.bind(this);

        uuid = getIntent().getStringExtra(GlobalConstants
                .key_uuid);
        initData();
        initListener();
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
        //setContentView(R.layout.activity_labor_service_cooperation_details);
        //ButterKnife.bind(this);
        if (isDownListView) {
            initData();
            isDownListView = false;
        }
    }

    void initData() {
        //得到详情信息
        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .lao_service_detail_url, null, GlobalConstants.key_uuid,
                uuid, new MyHttpUtils.OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {
                        if (result != null) {
                            LogUtils.e(TAG, "劳务协作 详情信息" + result);
                            parseData(result);
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {

                    }
                });
    }

    //解析数据
    private void parseData(String result) {
        LogUtils.e("解析的数据是",result);
        LaborServiceInfoFromUuid info = GSonUtil
                .parseJson(result, LaborServiceInfoFromUuid.class);
        data = info.objList;
        if (data != null) {
            tvCity.setText("" + data.city);
            tvArea.setText("" + data.county);
            tvTown.setText("" + data.town);
            tvWorktype.setText("" + data.kindDictionaryInfo.kindName);
            tvTilte.setText(data.title);
            tvCreatTime.setText(TimeUtils.setCreatTime(data.createTime));
            tvWorkTime.setText(TimeUtils.setCreatTime(data.labourTime));



//            tvHitConunt.setText(data.hitCount);
            if(data.mora == 1){
                tvHitConunt.setText("工作时间："+ "上午");
            }else if(data.mora == 2){
                tvHitConunt.setText("工作时间："+ "中午");
            }else if(data.mora == 3){
                tvHitConunt.setText("工作时间："+ "下午");
            }else if(data.mora == 4){
                tvHitConunt.setText("工作时间："+ "晚上");
            }else if(data.mora == 5){
                tvHitConunt.setText("工作时间："+ "全天");
            }


            Content2StrUtils.setContentStr(data.content, contentTv);
            tvPublishName.setText(data.contacts);


            if (data.kindDictionaryInfo != null) {
                getDataQitaFromNetWork();
            }
        }

    }

    private void getDataQitaFromNetWork() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.KEY_PAGENUM, "1");
        hashMap.put(GlobalConstants.KEY_PAGESIZE, "5");
        hashMap.put(GlobalConstants.key_uuid, uuid);
        hashMap.put(GlobalConstants.key_kind, data.kindDictionaryInfo.uuid);
        MyHttpUtils.getStringDataFromNet(GlobalConstants.lao_service_qita_url,
                null, hashMap, new MyHttpUtils.OnNewWorkRequestListener() {

                    @Override
                    public void onNewWorkSuccess(String result) {

                        if (result != null) {
                            LogUtils.e(TAG, "劳务协作其他" + result);
                            parserDataQita(result);
                            isQitan = true;
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        ToastUtils.makeTextShowNoNet
                                (LaborServiceCooperationDetailsActivity.this);
                    }
                });

    }

    private void parserDataQita(String result) {
        LaborServiceInfo info = GSonUtil.parseJson(result,
                LaborServiceInfo.class);
        infoList = info.objList;
        if (infoList != null && infoList.size() > 0) {

            if (mAdapter == null) {
                mAdapter = new
                        LaborServiceAdapter
                        (LaborServiceCooperationDetailsActivity.this, infoList);
                lv.setAdapter(mAdapter);
            }

        }
    }

    @OnClick({R.id.back_Iv, R.id.release_Iv, R.id.iv_callphone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.release_Iv:
                startActivity(new Intent
                        (LaborServiceCooperationDetailsActivity.this,
                                LaborServiceCooperationReleaseActivity.class));
            case R.id.iv_callphone:

                if (data != null) {
                    if (UiUtils.isLogin()) {
                        MyShowDialog.ShowCallPhone(this, data.phone,data.contacts);
                    } else {
                        showDialogLoginInfo();
                    }
                }


                break;
        }
    }

    private void initListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {

                isDownListView = true;
                LogUtils.e(TAG, "劳务协作其他条目被点击");

                Intent intent = new Intent
                        (LaborServiceCooperationDetailsActivity.this,
                                LaborServiceCooperationDetailsActivity.class);
                uuid = infoList.get
                        (position).uuid;
                intent.putExtra(GlobalConstants.key_uuid, uuid);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    /**
     * 提示用户没用登录
     */
    private void showDialogLoginInfo() {
        MyShowDialog.showDialogInfo(this, new OnGetData2BooleanListener() {
            @Override
            public void onSuccess(Boolean result) {
                startActivity(new Intent(LaborServiceCooperationDetailsActivity
                        .this, LoginActivity.class));
            }

            @Override
            public void onFail() {

            }
        });
    }
}
