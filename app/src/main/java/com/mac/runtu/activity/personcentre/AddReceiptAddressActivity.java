package com.mac.runtu.activity.personcentre;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.business.AddressBiz4FromPro3;
import com.mac.runtu.business.MyShowDialog;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.interfaceif.OnGetData2BooleanListener;
import com.mac.runtu.javabean.UserGetGoodAddressDetailInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.NumberUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加地址
 */
public class AddReceiptAddressActivity extends AppCompatActivity {

    @BindView(R.id.back_Iv)
    ImageView backIv;
    //添加按钮
    @BindView(R.id.add_address_Bt)
    Button    addAddressBt;

    //收件人
    @BindView(R.id.consignee_Et)
    EditText consigneeEt;
    //电话
    @BindView(R.id.receipt_phone_number_Et)
    EditText receiptPhoneNumberEt;
    //详细地址
    @BindView(R.id.detailed_address_Et)
    EditText detailedAddressEt;

   /* @BindView(R.id.sp_prov)
    Spinner spProv;
    @BindView(R.id.sp_city)
    Spinner spCity;
    @BindView(R.id.sp_country)
    Spinner spCountry;*/

    @BindView(R.id.et_guding_number)
    EditText  etGudingNumber;
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


    private UserGetGoodAddressDetailInfo info;
    private String                       detaileAdress;
    private String                       name;
    private String                       number;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt_address);
        ButterKnife.bind(this);

        initVeiw();
    }

    //初始化布局
    private void initVeiw() {
        //用户信息对象
        //userID = LoginBiz.getInstance().userID;
        new AddressBiz4FromPro3(this, tvProvince, ivProvince, tvCity,
                ivCity, tvArea, ivArea, null, null, null, null);


    }

    @OnClick({R.id.back_Iv, R.id.add_address_Bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.add_address_Bt:
                //添加数据
                //非空校验
                name = consigneeEt.getText().toString().trim();
                number = receiptPhoneNumberEt.getText().toString()
                        .trim();

                detaileAdress = detailedAddressEt.getText().toString()
                        .trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number) ||
                        TextUtils.isEmpty(detaileAdress)) {
                    ToastUtils.makeTextShow(AddReceiptAddressActivity.this,
                            "信息不完整");
                    return;
                }


                //正则匹配
                if (!NumberUtils.isNumnberMatch(number)) {
                    ToastUtils.makeTextShowIsPhone(this);
                    return;
                }

                //询问是否设为默认地址
                addAddressBt.setEnabled(false);

                //地址对象
                info = new UserGetGoodAddressDetailInfo();

                MyShowDialog.showDialogAddressIsDefalut(this, new
                        OnGetData2BooleanListener() {


                            @Override
                            public void onSuccess(Boolean result) {

                                info.status = 1;
                                setData2net();
                            }

                            @Override
                            public void onFail() {

                                info.status = 0;
                                setData2net();
                            }
                        });


                break;
        }
    }

    private void setData2net() {
        //向对象添加数据
        info.userUuid = UiUtils.getUserID();

        info.contacts = name;
        info.phone = number;

        String contact_number = etGudingNumber.getText().toString()
                .trim();

        info.contactNumber = !TextUtils.isEmpty(contact_number) ?
                contact_number : "";
        // 已省开始查 city对应省
        info.province = MarkerConstants.value_province;
        info.city = MarkerConstants.value_city;
        info.county = MarkerConstants.value_county;
        info.consigneeAddress = detaileAdress;
        //String addresJson = GSonUtil.obj2json();
        // c 有可能为空值

        //提交网路
        String infoJson = GSonUtil.obj2json(info);
        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                .user_add_address_url, null, GlobalConstants
                .key_info, infoJson, new MyHttpUtils
                .OnNewWorkRequestListener2Boolean() {


            @Override
            public void onNewWorkSuccess(Boolean result) {
                if (result) {
                    ToastUtils.makeTextShow(AddReceiptAddressActivity
                            .this, "地址添加成功");
                    finish();

                } else {
                    ToastUtils.makeTextShow(AddReceiptAddressActivity
                            .this, "地址添加失败");
                    addAddressBt.setEnabled(true);
                }
            }

            @Override
            public void onNewWorkError(String msg) {
                ToastUtils.makeTextShowNoNet(AddReceiptAddressActivity
                        .this);
                addAddressBt.setEnabled(true);
            }
        });
    }
}
