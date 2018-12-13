package com.mac.runtu.activity.personcentre;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mac.runtu.R;
import com.mac.runtu.activity.agricumach.AgricuMachineryListActivity;
import com.mac.runtu.activity.businessdynamics.BusinessSerachListActivity;
import com.mac.runtu.activity.laborscrvice.LaborServiceCooperationListActivity;
import com.mac.runtu.activity.logisticsinformation.LogisticsInforListActivity;
import com.mac.runtu.activity.logisticsinformation
        .LogisticsUserGetGoodListActivity;
import com.mac.runtu.activity.property.PropertyListActivity;
import com.mac.runtu.business.MyShowDialog;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.SPUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的发布
 */
public class UserPublishActivity extends AppCompatActivity {

    @BindView(R.id.back_Iv)
    ImageView ivBack;

    @BindView(R.id.ll_propertypublish)
    LinearLayout llPropertypublish;
    @BindView(R.id.ll_businesspublish)
    LinearLayout llBusinesspublish;
    @BindView(R.id.ll_farmerpublish)
    LinearLayout llFarmerpublish;
    @BindView(R.id.ll_servicepublish)
    LinearLayout llServicepublish;
    @BindView(R.id.ll_machinerypublish)
    LinearLayout llMachinerypublish;
    @BindView(R.id.ll_tranypublish)
    LinearLayout llTranypublish;
    @BindView(R.id.ll_takegoodpublish)
    LinearLayout llTakegoodpublish;
    private String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_publish2);
        ButterKnife.bind(this);

        usertype = SPUtils.getString(this, GlobalConstants.sp_user_type_name,
                "");


    }

    @OnClick({R.id.back_Iv, R.id.ll_propertypublish, R.id.ll_businesspublish,
            R.id
                    .ll_farmerpublish, R.id.ll_servicepublish, R.id
            .ll_machinerypublish, R.id.ll_tranypublish, R.id
            .ll_takegoodpublish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                //产权
                finish();
                break;

            case R.id.ll_propertypublish:
                //产权
                getData(PropertyListActivity.class, GlobalConstants
                        .AD_TYPE_PROPERTY_RIGHT);
                break;
            case R.id.ll_businesspublish:
                //客商

                getData(BusinessSerachListActivity.class, GlobalConstants
                        .AD_TYPE_BUSINESS);

                break;
            case R.id.ll_farmerpublish:
                //农户

                getData(BusinessSerachListActivity.class, GlobalConstants
                        .AD_TYPE_BUSINESS);

                break;
            case R.id.ll_servicepublish:
                //劳务
                getData(LaborServiceCooperationListActivity.class,
                        GlobalConstants.AD_TYPE_SERVICE);
                break;
            case R.id.ll_machinerypublish:
                //农资
                getData(AgricuMachineryListActivity.class, GlobalConstants
                        .AD_TYPE_MACHINE);
                break;
            case R.id.ll_tranypublish:
                //物流
                getData(LogisticsInforListActivity.class, GlobalConstants
                        .AD_TYPE_wului);
                break;
            case R.id.ll_takegoodpublish:
                //捎货
                getData(LogisticsUserGetGoodListActivity.class, GlobalConstants
                        .AD_TYPE_take_goods);
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

    public void getData(final Class clz, String model) {
        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .user_publish_data_url, null, GlobalConstants
                        .KEY_USERID,

                UiUtils.getUserID(), GlobalConstants.key_model,
                model, new MyHttpUtils.OnNewWorkRequestListener() {

                    @Override
                    public void onNewWorkSuccess(String result) {
                        if (result != null) {
                            // MarkerConstants.isFromUser = true;

                            Intent intent = new Intent(UserPublishActivity
                                    .this, clz);
                            intent.putExtra(GlobalConstants.key_user, result);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        ToastUtils.makeTextShowNoNet(UserPublishActivity.this);
                    }
                });
    }

    private void showMyDialog(String s) {
        //提示用户
        MyShowDialog.showDialogSelectImage("提示信息:", s, this, null);

    }
}
