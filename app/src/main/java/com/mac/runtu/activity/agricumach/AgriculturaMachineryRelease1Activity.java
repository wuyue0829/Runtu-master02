package com.mac.runtu.activity.agricumach;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.moreimage.AlbumsActivity;
import com.mac.runtu.activity.otherActivity.PublishIsLoginActivity;
import com.mac.runtu.business.AddressBiz4FromPro3;
import com.mac.runtu.business.MyShowDialog;
import com.mac.runtu.business.TypeDataSelectFromNetType1;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.interfaceif.OnBooleanListener;
import com.mac.runtu.javabean.AgricuMachineryCommitInfo;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 农资农机的发布页
 */
public class AgriculturaMachineryRelease1Activity extends
        PublishIsLoginActivity {


    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.sp_type1)
    Spinner   spType1;
    @BindView(R.id.sp_type2)
    Spinner   spType2;
    @BindView(R.id.sp_type3)
    Spinner   spType3;


    @BindView(R.id.tv_province)
    TextView  tvProvince;
    @BindView(R.id.iv_province)
    ImageView ivProvince;
    @BindView(R.id.tv_city)
    TextView  tvCity;
    @BindView(R.id.iv_city)
    ImageView ivCity;
    @BindView(R.id.tv_area)
    TextView  tvArea;
    @BindView(R.id.iv_area)
    ImageView ivArea;
    @BindView(R.id.tv_town)
    TextView  tvTown;
    @BindView(R.id.iv_town)
    ImageView ivTown;

    @BindView(R.id.et_title)
    EditText  etTitle;
    @BindView(R.id.et_des)
    EditText  etDes;
    @BindView(R.id.et_price)
    EditText  etPrice;
    @BindView(R.id.iv_addImage)
    ImageView ivAddImage;
    @BindView(R.id.next_Iv)
    ImageView nextIv;

    private String identity;
    private String[] identityName = {"厂家直销", "本地供应商", "农户", "其他"};
    private String[] identityNum  = {"1", "2", "3", "4"};

    private String type;
    private String[] typeName = {"供货", "求购"};
    private String[] typeNum  = {"1", "2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agricultural_machinery_release1);
        ButterKnife.bind(this);
        // ButterKnife.bind(this);

        //照片路径归零
        MarkerConstants.value_more_image_url = "";

        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(MarkerConstants.value_more_image_url)) {
            ImageLoadUtils.rectangleImage
                    (AgriculturaMachineryRelease1Activity.this,
                            MarkerConstants.value_more_image_url, ivAddImage);
        }
    }

    private void initData() {
        new TypeDataSelectFromNetType1(this, spType2, GlobalConstants
                .AD_TYPE_MACHINE);

        //四个地址
        new AddressBiz4FromPro3(this, null, null, tvProvince, ivProvince,
                tvCity, ivCity, tvArea,
                ivArea, tvTown, ivTown);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R
                .layout.sp_item_textview, typeName);
        spType1.setAdapter(adapter);
        spType1.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {
                type = typeNum[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R
                .layout.sp_item_textview, identityName);
        spType3.setAdapter(adapter2);
        spType3.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {
                identity = identityNum[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

                //提示用户
                MyShowDialog.showDialogSelectImage(this, new OnBooleanListener() {


                    @Override
                    public void onResultIsTrue() {
                        Intent intentImage = new Intent
                                (AgriculturaMachineryRelease1Activity.this,
                                        AlbumsActivity
                                                .class);
                        startActivity(intentImage);
                    }
                });

                break;
            case R.id.next_Iv:


                String title = getEtText(etTitle);
                String des = getEtText(etDes);
                String price = getEtText(etPrice);

                if (isEtEmpty(title) || isEtEmpty(des) || isEtEmpty(price)) {
                    ToastUtils.makeTextShowContent
                            (AgriculturaMachineryRelease1Activity.this);
                    return;
                }

                AgricuMachineryCommitInfo info = new
                        AgricuMachineryCommitInfo();

                info.type = type;
                info.kind = MarkerConstants.value_kind;
                info.identity = identity;
                info.province = MarkerConstants.value_province;
                info.city = MarkerConstants.value_city;
                info.county = MarkerConstants.value_county;
                info.town = MarkerConstants.value_town;
                info.village = MarkerConstants.value_village;

                info.title =title;
                info.content = des;
                info.price = price;

                Intent intent = new Intent(AgriculturaMachineryRelease1Activity
                        .this, AgriculturaMachineryRelease2Activity.class);
                 intent.putExtra(GlobalConstants.key_info,info);

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
