package com.mac.runtu.activity.property;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.PublishIsLoginActivity;
import com.mac.runtu.business.AddressBiz4FromPro3;
import com.mac.runtu.business.TypeDataSelectFromNetType1;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.PropertyCommiteInfo;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 农机发布第一页
 */
public class PropertyRightsRealse1Activity extends PublishIsLoginActivity {

    private static final String TAG = "PropertyRightsRealse1Activity";

    @BindView(R.id.back_Iv)
    ImageView      backIv;
    @BindView(R.id.relativeLayout1)
    RelativeLayout relativeLayout1;
    @BindView(R.id.text1)
    TextView       text1;
    @BindView(R.id.sp_type1)
    Spinner        spType1;

    @BindView(R.id.sp_liuzhuanWay)
    Spinner        spLiuzhuanWay;
    @BindView(R.id.et_size)
    EditText       etSize;
    @BindView(R.id.et_liuzhuanTime)
    EditText       etLiuzhuanTime;
    @BindView(R.id.et_price)
    EditText       etPrice;
    /*@BindView(R.id.tv_publishTime)
    TextView       tvPublishTime;*/
    @BindView(R.id.release_next_Iv)
    ImageView      releaseNextIv;
    @BindView(R.id.linearLayout)
    LinearLayout   linearLayout;


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


    private String exchange;
    private String[] exchangeName = {"转让", "转包", "互换", "入股", "出租"};
    private String[] exchangeNum  = {"1", "2", "3", "4", "5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_rights_release1);
        ButterKnife.bind(this);

        //照片路径归零
        MarkerConstants.value_more_image_url = "";

        initData();
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

    private void initData() {


        //两个类型
        new TypeDataSelectFromNetType1(this, spType1, GlobalConstants
                .AD_TYPE_PROPERTY_RIGHT);

        new AddressBiz4FromPro3(this, null, null, tvProvince, ivProvince,
                tvCity, ivCity, tvArea,
                ivArea, tvTown, ivTown);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R
                .layout.sp_item_textview, exchangeName);
        //流转方式
        spLiuzhuanWay.setAdapter(adapter);
        spLiuzhuanWay.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {
                exchange = exchangeNum[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @OnClick({R.id.back_Iv, R.id.release_next_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.release_next_Iv:

                if (!UiUtils.isLogin()) {
                    UiUtils.go2LoginAcivity();
                    return;
                }

                //先判断都不为空
                String size = getEtText(etSize);
                String etTime = getEtText(etLiuzhuanTime);
                String price = getEtText(etPrice);
                if (isEtEmpty(size) || isEtEmpty(etTime) || isEtEmpty(price)) {

                    ToastUtils.makeTextShowContent
                            (PropertyRightsRealse1Activity.this);
                    return;
                }
                PropertyCommiteInfo info = new
                        PropertyCommiteInfo();

                info.kind = MarkerConstants.value_kind;

                info.province = MarkerConstants.value_province;
                info.city = MarkerConstants.value_city;
                info.county = MarkerConstants.value_county;
                info.town = MarkerConstants.value_town;
                info.village = MarkerConstants.value_village;

                info.exchange = exchange;
                info.area = size;
                info.years = etTime;
                info.price = price;

                Intent intent = new Intent(this,
                        PropertyRightsRealse2Activity.class);
                intent.putExtra(GlobalConstants.key_info, info);


                startActivityForResult(intent,0);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            setResult(RESULT_OK,new Intent());
            finish();

        }
    }
}
