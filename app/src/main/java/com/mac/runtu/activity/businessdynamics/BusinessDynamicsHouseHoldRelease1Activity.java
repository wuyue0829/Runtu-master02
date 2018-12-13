package com.mac.runtu.activity.businessdynamics;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.moreimage.AlbumsActivity;
import com.mac.runtu.activity.otherActivity.PublishIsLoginActivity;
import com.mac.runtu.business.AddressBiz4FromPro3;
import com.mac.runtu.business.TypeDataSelectFromNetType1;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.BusinessCommitInfo;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 农户发布 第一页
 */
public class BusinessDynamicsHouseHoldRelease1Activity extends
        PublishIsLoginActivity {

    private static final String TAG =
            "BusinessDynamicsHouseHoldRelease1Activity";
    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.sp_type1)
    Spinner   spType1;

    @BindView(R.id.et_title)
    EditText  etTitle;
    @BindView(R.id.et_des)
    EditText  etDes;
    @BindView(R.id.iv_addImage)
    ImageView ivAddImage;
    @BindView(R.id.next_Iv)
    ImageView nextIv;


    @BindView(R.id.tv_province)
    TextView     tvProvince;
    @BindView(R.id.iv_province)
    ImageView    ivProvince;
    @BindView(R.id.tv_city)
    TextView     tvCity;
    @BindView(R.id.iv_city)
    ImageView    ivCity;
    @BindView(R.id.tv_area)
    TextView     tvArea;
    @BindView(R.id.iv_area)
    ImageView    ivArea;
    @BindView(R.id.tv_town)
    TextView     tvTown;
    @BindView(R.id.iv_town)
    ImageView    ivTown;
    @BindView(R.id.linearLayout5)
    LinearLayout linearLayout5;

    private String title;
    private String des;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_dynamics_household_release1);
        ButterKnife.bind(this);

        //照片路径归零
        MarkerConstants.value_more_image_url = "";
        initData();

        //LogUtils.e(TAG,"上传信息需+++++++++++++");
    }

    private void initData() {

        //两个类型
        new TypeDataSelectFromNetType1(this, spType1, GlobalConstants
                .AD_TYPE_BUSINESS);

        //四个地址

        new AddressBiz4FromPro3(this, null, null, tvProvince, ivProvince,
                tvCity, ivCity, tvArea,
                ivArea, tvTown, ivTown);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(MarkerConstants.value_more_image_url)) {
            ImageLoadUtils.rectangleImage
                    (BusinessDynamicsHouseHoldRelease1Activity.this,
                            MarkerConstants.value_more_image_url, ivAddImage);
        }
    }

    @OnClick({R.id.back_Iv, R.id.iv_addImage, R.id.next_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:

                MarkerConstants.value_more_image_name = null;
                MarkerConstants.value_more_image_url = "";
                MarkerConstants.value_more_image_content_path = null;
                ivAddImage.setImageDrawable(null);
                finish();
                break;
            case R.id.iv_addImage:
                //去添加图片
                MarkerConstants.value_more_image_name = null;
                MarkerConstants.value_more_image_url = "";
                MarkerConstants.value_more_image_content_path = null;

                MarkerConstants.isBussFarm = true;

                Intent intentImage = new Intent
                        (BusinessDynamicsHouseHoldRelease1Activity.this,
                                AlbumsActivity
                                        .class);
                startActivity(intentImage);

                break;
            case R.id.next_Iv:
                //判断是否要登陆
                if (!UiUtils.isLogin()) {
                    UiUtils.go2LoginAcivity();
                    return;
                }


                //省市县的选择
                LogUtils.e(TAG, "地址 省" + MarkerConstants.value_province);
                LogUtils.e(TAG, "地址 市" + MarkerConstants.value_city);
                LogUtils.e(TAG, "地址 县" + MarkerConstants.value_county);
                LogUtils.e(TAG, "地址 镇" + MarkerConstants.value_town);
                LogUtils.e(TAG, "地址 村" + MarkerConstants.value_village);

                title = getEtText(etTitle);
                des = getEtText(etDes);


                if (isEtEmpty(title) || isEtEmpty(des)) {
                    ToastUtils.makeTextShowContent(this);
                    return;
                }

                BusinessCommitInfo info = new BusinessCommitInfo();

                info.province = MarkerConstants.value_province;
                info.village = MarkerConstants.value_village;

                info.type = "2";

                info.kind = MarkerConstants.value_kind;
                info.city = MarkerConstants.value_city;

                info.county = MarkerConstants.value_county;
                info.town = MarkerConstants.value_town;
                info.title = title;
                info.content = des;


                Intent intent = new Intent(this,
                        BusinessDynamicsHouseHoldRelease2Activity.class);

                //将对象带过去
                intent.putExtra(GlobalConstants.key_info, info);

                startActivityForResult(intent, 0);

                break;
        }
    }


    public String getEtText(EditText et) {
        return et.getText().toString().trim();
    }

    public boolean isEtEmpty(String etStr) {
        return TextUtils.isEmpty(etStr);
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
