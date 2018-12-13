package com.mac.runtu.activity.logisticsinformation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.PublishIsLoginActivity;
import com.mac.runtu.business.AddressSelectData4FromPro2;
import com.mac.runtu.business.AddressSelectDataFinalFromPro2;
import com.mac.runtu.business.TypeDataSelectFromNetType1;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.LogisticsCommitInfo;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyData;
import com.mac.runtu.utils.ToastUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 物流信息发布页
 */
public class LogisticsInformationRelease1Activity extends
        PublishIsLoginActivity {

    private static final String TAG = "LogisticsInformationRelease1Activity";
    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.sp_type1)
    Spinner   spType1;
    @BindView(R.id.et_weight)
    EditText  etWeight;
    @BindView(R.id.sp_prov)
    Spinner   spProv;
    @BindView(R.id.sp_city)
    Spinner   spCity;
    @BindView(R.id.sp_area)
    Spinner   spArea;
    @BindView(R.id.sp_town)
    Spinner   spTown;
    @BindView(R.id.sp_final_prov)
    Spinner   spFinalProv;
    @BindView(R.id.sp_final_city)
    Spinner   spFinalCity;
    @BindView(R.id.sp_final_area)
    Spinner   spFinalArea;
    @BindView(R.id.sp_final_town)
    Spinner   spFinalTown;
    @BindView(R.id.et_address01)
    EditText  etAddress01;
    @BindView(R.id.et_address02)
    EditText  etAddress02;
    @BindView(R.id.et_address03)
    EditText  etAddress03;
    @BindView(R.id.et_address04)
    EditText  etAddress04;
    @BindView(R.id.et_address05)
    EditText  etAddress05;
    @BindView(R.id.tv_start_car_time)
    TextView  tvStartCarTime;
    @BindView(R.id.tv_start_car_data)
    TextView  tvStartCarData;
    @BindView(R.id.et_title)
    EditText  etTitle;
    @BindView(R.id.et_des)
    EditText  etDes;
    @BindView(R.id.next_Iv)
    ImageView nextIv;
    private String address01 = "";
    private String address02 = "";
    private String address03 = "";
    private String address04 = "";
    private String address05 = "";
    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_information_release1);
        ButterKnife.bind(this);

        initData();

    }

    private void initData() {
        //类型
        new TypeDataSelectFromNetType1(this, spType1, GlobalConstants
                .AD_TYPE_wului);

        //四个地址
        new AddressSelectData4FromPro2(this,
                spProv, spCity, spArea, spTown,null);

        new AddressSelectDataFinalFromPro2(this, spFinalProv, spFinalCity, spFinalArea,
                spFinalTown,null);
        //getMainUuid();

        //设置默认时间日期
        c = Calendar.getInstance();

        String nowDate = MyData.getInstance().getDataStr("yyyy-MM-dd " +
                "HH:mm:ss");
        String[] nowDataTime = nowDate.split(" ");
        tvStartCarData.setText(nowDataTime[0]);

        tvStartCarTime.setText(nowDataTime[1]);

    }


    @OnClick({R.id.back_Iv, R.id.tv_start_car_time, R.id.tv_start_car_data, R
            .id.next_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.tv_start_car_time:

                new TimePickerDialog(this, new TimePickerDialog
                        .OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int
                            minute) {
                        tvStartCarTime.setText(hourOfDay + ":" + minute + ":"
                                + "00");
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true)
                        .show();

                break;
            case R.id.tv_start_car_data:
                //生日

                new DatePickerDialog(this, new DatePickerDialog
                        .OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int
                            monthOfYear, int dayOfMonth) {
                        tvStartCarData.setText(year + "-" + (monthOfYear +
                                1) + "-" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get
                        (Calendar.DAY_OF_MONTH)).show();


                break;
            case R.id.next_Iv:

                String weight = getEtText(etWeight);
                address01 = getEtText(etAddress01);
                address02 = getEtText(etAddress02);
                address03 = getEtText(etAddress03);
                address04 = getEtText(etAddress04);
                address05 = getEtText(etAddress05);

               // String startCarTime = tvStartCarTime.getText().toString();
                String title = getEtText(etTitle);
                String des = getEtText(etDes);

                if (isEtEmpty(weight) || isEtEmpty
                        (title) || isEtEmpty(des)) {
                    ToastUtils.makeTextShowContent
                            (LogisticsInformationRelease1Activity.this);
                    return;
                }

                LogisticsCommitInfo info = new
                        LogisticsCommitInfo();

                info.kind = MarkerConstants.value_kind;

                info.startProvince = MarkerConstants.value_province;

                info.startCity = MarkerConstants.value_city;
                info.startCounty = MarkerConstants.value_county;
                info.startTown = MarkerConstants.value_town;
                info.startVillage = MarkerConstants.value_village;

                info.targetProvince = MarkerConstants.value_fainl_province;

                info.targetCity = MarkerConstants.value_fainl_city;
                info.targetCounty = MarkerConstants.value_fainl_county;
                info.targetTown = MarkerConstants.value_fainl_town;
                info.targetVillage = MarkerConstants.value_fainl_village;

                setRoute(info);

                LogUtils.e(TAG, "途径地=" + info.route);

                info.weight = weight;
                info.title = title;
                info.content = des;

                String time = tvStartCarTime.getText().toString();
                info.departureTime = tvStartCarData.getText().toString() +
                        " " + time ;



                LogUtils.e(TAG,"选择的时间"+info.departureTime);

                Intent intent = new Intent(LogisticsInformationRelease1Activity
                        .this,
                        LogisticsInformationRelease2Activity.class);

                intent.putExtra(GlobalConstants.key_info, info);

                startActivityForResult(intent,0);
                break;
        }
    }

    //设置途径地
    private void setRoute(LogisticsCommitInfo info) {
        if (!isEtEmpty(address01)) {
            info.route = address01 + "-";
        } else {
            info.route = "";
        }
        if (!isEtEmpty(address02)) {
            info.route += address02 + "-";

        }
        if (!isEtEmpty(address03)) {
            info.route += address03 + "-";
        }
        if (!isEtEmpty(address04)) {
            info.route += address04 + "-";
        }
        if (!isEtEmpty(address05)) {
            info.route += address05;
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
