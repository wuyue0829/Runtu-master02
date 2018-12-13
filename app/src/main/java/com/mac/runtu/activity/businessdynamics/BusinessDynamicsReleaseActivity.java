package com.mac.runtu.activity.businessdynamics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.PublishIsLoginActivity;
import com.mac.runtu.activity.personcentre.PersonalInfoActivity;
import com.mac.runtu.business.MyShowDialog;
import com.mac.runtu.global.GlobalConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布页选择
 */
public class BusinessDynamicsReleaseActivity extends PublishIsLoginActivity {
    private static final String TAG = "BusinessDynamicsReleaseActivity";
    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.merchants_Iv)
    ImageView merchantsIv;
    @BindView(R.id.household_Iv)
    ImageView householdIv;
    @BindView(R.id.tv_userinfo)
    TextView  tvUserinfo;
    private String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_dynamics_release);
        ButterKnife.bind(this);


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @OnClick({R.id.back_Iv, R.id.tv_userinfo, R.id.merchants_Iv, R.id
            .household_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;

            case R.id.tv_userinfo:

                startActivity(new Intent(this, PersonalInfoActivity.class));

                break;
            case R.id.merchants_Iv:
                //客商

                Intent intent = new Intent(BusinessDynamicsReleaseActivity
                        .this, BusinessDynamicsMerchantsReleaseActivity
                        .class);
                intent.putExtra(GlobalConstants.key_type, GlobalConstants
                        .value_type_buiss_shenfn);

                startActivityForResult(intent, 0);


                break;
            case R.id.household_Iv:
                //农户

                    Intent intent2 = new Intent(BusinessDynamicsReleaseActivity
                            .this, BusinessDynamicsHouseHoldRelease1Activity
                            .class);
                    intent2.putExtra(GlobalConstants.key_type, GlobalConstants
                            .value_shenfen_fram_type);
                    startActivityForResult(intent2, 0);

                break;
        }
    }

    private void showMyDialog(String s) {
        //提示用户
        MyShowDialog.showDialogSelectImage("提示信息:", s, this, null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK, new Intent());
            finish();
        }
    }


}
