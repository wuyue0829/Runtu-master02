package com.mac.runtu.activity.businessdynamics;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.personcentre.LoginActivity;
import com.mac.runtu.adapter.BusinessDatalistAdapter;
import com.mac.runtu.business.MyShowDialog;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnGetData2BooleanListener;
import com.mac.runtu.javabean.BusinessDataDetailInfo;
import com.mac.runtu.javabean.BusinessDataListInfo;
import com.mac.runtu.javabean.BusinessDataListInfoFromUuid;
import com.mac.runtu.utils.Content2StrUtils;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.TimeUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;
import com.mac.runtu.view.MyListView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 客商动态详情页
 */
public class BusinessDynamicsDetailsActivity extends AppCompatActivity {

    private static final String TAG = "BusinessDynamicsDetailsActivity";
    @BindView(R.id.back_Iv)
    ImageView  backIv;
    @BindView(R.id.tv_city)
    TextView   tvCity;
    @BindView(R.id.tv_area)
    TextView   tvArea;
    @BindView(R.id.tv_kind)
    TextView   tvKind;
    @BindView(R.id.tv_title)
    TextView   tvTitle;
    @BindView(R.id.tv_creattime)
    TextView   tvCreattime;
    @BindView(R.id.tv_hitConunt)
    TextView   tvHitConunt;
    @BindView(R.id.title_Tv)
    TextView   titleTv;
    @BindView(R.id.content_Tv)
    TextView   contentTv;
    @BindView(R.id.lv_my)
    MyListView lvMy;
    @BindView(R.id.user_photo_Iv)
    ImageView  userPhotoIv;
    @BindView(R.id.tv_name)
    TextView   tvName;
    @BindView(R.id.phone_service_Iv)
    ImageView  phoneServiceIv;
    private String                            uuid;
    private BusinessDataDetailInfo            data;
    private ArrayList<BusinessDataDetailInfo> infoList;
    private BusinessDatalistAdapter           mAdapter;
    private boolean isDownListView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_dynamics_details);
        ButterKnife.bind(this);

        uuid = getIntent().getStringExtra(GlobalConstants
                .key_uuid);
        initData();
        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //setContentView(R.layout.activity_business_dynamics_details);
        //ButterKnife.bind(this);
        if (isDownListView) {
            initData();
            isDownListView = false;
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


    void initData() {

        MyHttpUtils.getStringDataFromNet(GlobalConstants.business_detail_url,
                null, GlobalConstants.key_uuid, uuid, new MyHttpUtils
                        .OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {
                        if (result != null) {
                            parserData(result);
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        ToastUtils.makeTextShowNoNet
                                (BusinessDynamicsDetailsActivity.this);
                    }
                });


    }

    private void parserData(String result) {
        BusinessDataListInfoFromUuid info = GSonUtil
                .parseJson(result, BusinessDataListInfoFromUuid.class);

         data = info.objList;
        if (data != null) {
            tvCity.setText(data.city);
            tvArea.setText(data.county);
            tvKind.setText(data.kindDictionaryInfo.kindName);
            tvCreattime.setText(TimeUtils.setCreatTime(data.createTime));
            tvHitConunt.setText(data.hitCount);
            Content2StrUtils.setContentStr(data.content, contentTv);
            tvName.setText(data.contacts);
            tvTitle.setText(data.title);

            getDataQitaFromNetWork();


        }
    }


    private void getDataQitaFromNetWork() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.KEY_PAGENUM, "1");
        hashMap.put(GlobalConstants.KEY_PAGESIZE, "5");
        hashMap.put(GlobalConstants.key_uuid, uuid);
        hashMap.put(GlobalConstants.key_kind, data.kindDictionaryInfo.uuid);
        MyHttpUtils.getStringDataFromNet(GlobalConstants.business_qita_url,
                null, hashMap, new MyHttpUtils.OnNewWorkRequestListener() {

                    @Override
                    public void onNewWorkSuccess(String result) {

                        if (result != null) {
                            LogUtils.e(TAG, "客商动态其他" + result);
                            parserDataQita(result);

                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {

                    }
                });

    }

    private void parserDataQita(String result) {
        BusinessDataListInfo info = GSonUtil.parseJson
                (result,
                        BusinessDataListInfo.class);
        infoList = info.objList;
        if (infoList != null && infoList.size() > 0) {

            //客商数据类型
            if (mAdapter == null) {
                mAdapter = new
                        BusinessDatalistAdapter
                        (this, infoList);
                lvMy.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }


        }
    }

    @OnClick({R.id.back_Iv, R.id.phone_service_Iv})
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
        }
    }



    private void initListener() {
        lvMy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {

                isDownListView = true;

                LogUtils.e(TAG, "客商动态条目被点击");

                Intent intent = new Intent
                        (BusinessDynamicsDetailsActivity.this,
                                BusinessDynamicsDetailsActivity.class);
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
                startActivity(new Intent(BusinessDynamicsDetailsActivity
                        .this, LoginActivity.class));
            }

            @Override
            public void onFail() {

            }
        });
    }
}
