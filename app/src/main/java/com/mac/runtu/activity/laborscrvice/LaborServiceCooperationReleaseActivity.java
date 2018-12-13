package com.mac.runtu.activity.laborscrvice;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.business.AddressBiz4FromPro3;
import com.mac.runtu.business.CheckCodeBiz;
import com.mac.runtu.business.LoginBiz;
import com.mac.runtu.business.TypeDataSelectFromNetType1;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.interfaceif.OnCodeTestTestListener;
import com.mac.runtu.javabean.LaborServiceCommitInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyData;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 劳务发布页
 */
public class LaborServiceCooperationReleaseActivity extends AppCompatActivity {

    private static final String TAG = "LaborServiceCooperationReleaseActivity";

    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.sp_type1)
    Spinner   spType1;
    @BindView(R.id.sp_type2)
    Spinner   spType2;
    @BindView(R.id.type_Tv)
    TextView  typeTv;


    @BindView(R.id.sp_time)
    Spinner spTime;


    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_des)
    EditText etDes;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.et_name)
    TextView etName;
    @BindView(R.id.et_number)
    TextView etNumber;
    @BindView(R.id.et_personCount)
    EditText etNersonCount;

    @BindView(R.id.et_code)
    EditText  etCode;
    @BindView(R.id.tv_btnCode)
    TextView  tvBtnCode;
    @BindView(R.id.confirm_release_Iv)
    ImageView confirmReleaseIv;

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
    @BindView(R.id.tr_top)
    TableRow tr_top;
    @BindView(R.id.view_top)
    View view_top;

    private String title;
    private String des;
    private String name;
    private String phone;
    private String uuid;

    //信息类型0为工人找活，1为商人雇佣
    private String type;
    private String[] typeName         = {"商人雇佣", "工人找活"};
    private String[] typeNum          = {"0", "1"};
    private String[] serviceTimesName = {"上午", "中午", "下午", "晚上", "全天" };
    private String[] serviceTimesType = { "1", "2", "3", "4", "5"};
    private String timesType;

    private String mCode4Net;


    private int getCodeTotalTime = 60;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            getCodeTotalTime--;
            tvBtnCode.setText("剩余"+getCodeTotalTime+"秒");
            if (getCodeTotalTime == 0) {
                tvBtnCode.setText(R.string.get_verification_code);
                tvBtnCode.setEnabled(true);
                getCodeTotalTime = 60;
                mHandler.removeMessages(0);

            }else {
                mHandler.sendEmptyMessageDelayed(0,1000);
            }


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labor_service_cooperation_release);
        ButterKnife.bind(this);

        iniData();
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

    private void iniData() {

        etName.setText(UiUtils.getUserName());
        etNumber.setText(UiUtils.getUserNumber());

        getMainUuid();

        new TypeDataSelectFromNetType1(this, spType2, GlobalConstants
                .AD_TYPE_SERVICE);

        //四个地址

        new AddressBiz4FromPro3(this, null, null, tvProvince, ivProvince,
                tvCity, ivCity, tvArea,
                ivArea, tvTown, ivTown);



        tvTime.setText(MyData.getInstance().getDataStr(null));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R
                .layout.sp_item_textview, typeName);
        spType1.setAdapter(adapter);
        spType1.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {
                type = typeNum[position];
                if(position == 0){
                    tr_top.setVisibility(View.VISIBLE);
                    view_top.setVisibility(View.VISIBLE);
                }else{
                    tr_top.setVisibility(View.GONE);
                    view_top.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R
                .layout.sp_item_textview, serviceTimesName);
        spTime.setAdapter(adapter2);
        spTime.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {

                timesType = serviceTimesType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getMainUuid() {
        //访问网络得到uuid
        MyHttpUtils.getStringDataFromNet(GlobalConstants
                .business_data_commiet_uuid_url, null, new MyHttpUtils
                .OnNewWorkRequestListener() {

            @Override
            public void onNewWorkSuccess(String result) {
                if (result != null) {
                    uuid = result.toString();
                }
            }

            @Override
            public void onNewWorkError(String msg) {
                ToastUtils.makeTextShowNoNet
                        (LaborServiceCooperationReleaseActivity.this);
            }
        });

    }


    @OnClick({R.id.back_Iv, R.id.tv_time, R.id.tv_btnCode, R.id
            .confirm_release_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;

            case R.id.tv_time:

                Calendar c = Calendar.getInstance();
                new DatePickerDialog(this, new DatePickerDialog
                        .OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int
                            monthOfYear, int dayOfMonth) {
                        tvTime.setText(year + "-" + (monthOfYear +
                                1) + "-" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get
                        (Calendar.DAY_OF_MONTH)).show();

                break;
            case R.id.tv_btnCode:

                //判断是否要登陆
                if (!UiUtils.isLogin()) {
                    UiUtils.go2LoginAcivity();
                    return;
                }

                title = getEtText(etTitle);
                des = getEtText(etDes);
                name = getEtText(etName);
                phone = getEtText(etNumber);

                if (isEtEmpty(title) || isEtEmpty(des) || isEtEmpty(name) ||
                        isEtEmpty(phone)) {
                    ToastUtils.makeTextShowContent
                            (LaborServiceCooperationReleaseActivity.this);
                    return;
                }
                tvBtnCode.setEnabled(false);
                getCode();

                break;
            case R.id.confirm_release_Iv:

                //判断是否要登陆
                if (!UiUtils.isLogin()) {
                    UiUtils.go2LoginAcivity();
                    return;
                }


                String mCode = etCode.getText().toString().trim();
                if (TextUtils.isEmpty(mCode)) {
                    ToastUtils.makeTextShowCodeNotEmpty(this);
                    return;
                }

                if (TextUtils.isEmpty(uuid)) {
                    return;
                }

                if (!mCode.equals(mCode4Net)) {
                    ToastUtils.makeTextShowCodeError(this);
                    return;
                }
                confirmReleaseIv.setEnabled(false);
                setData2NetWork();
                break;
        }
    }


    private void getCode() {

        mHandler.sendEmptyMessageDelayed(0,1000);
        tvBtnCode.setText("剩余"+getCodeTotalTime+"秒");
        //访问网络
        //得到验证码
        CheckCodeBiz.getCode(phone, new OnCodeTestTestListener() {

            @Override
            public void onData(String code) {

                mCode4Net = code;

                //解封按钮


                LogUtils.e(TAG, "网络获得code=" + code);


            }

            @Override
            public void onNoData() {
                //服务器没有返回验证码
            }

            @Override
            public void onNetWorkFail() {

            }
        });
    }


    private void setData2NetWork() {
        //判断验证码

        //成功以后提交网络
        LaborServiceCommitInfo info = new LaborServiceCommitInfo();
        info.userUuid = LoginBiz.getInstance().userDesInfo.uuid;
        info.type = type;
        info.kind = MarkerConstants.value_kind;
        info.province = MarkerConstants.value_province;
        info.city = MarkerConstants.value_city;
        info.county = MarkerConstants.value_county;
        info.town = MarkerConstants.value_town;
        info.village = MarkerConstants.value_village;
        info.title = title;
        info.content = des;

        info.contacts = name;

        info.phone = phone;
        info.uuid = uuid;
        info.labourTime = tvTime.getText().toString();
        info.personCount = etNersonCount.getText().toString();

        //时间
        info.mora = timesType;

        String infoJson = GSonUtil.obj2json(info);

        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                .lao_commit_url, null, GlobalConstants
                .key_info, infoJson, new MyHttpUtils
                .OnNewWorkRequestListener2Boolean() {


            @Override
            public void onNewWorkSuccess(Boolean result) {

                //LogUtils.e(TAG, "result=" + result);
                if (result) {
                    ToastUtils.makeTextShowDataCommitSuccess
                            (LaborServiceCooperationReleaseActivity.this);

                    setResult(RESULT_OK, new Intent());

                    finish();
                } else {
                    ToastUtils.makeTextShowDataCommitFail
                            (LaborServiceCooperationReleaseActivity.this);
                    confirmReleaseIv.setEnabled(true);
                }
            }

            @Override
            public void onNewWorkError(String msg) {
                ToastUtils.makeTextShowNoNet
                        (LaborServiceCooperationReleaseActivity.this);
                //LogUtils.e(TAG, "网络忙" + msg);
                confirmReleaseIv.setEnabled(true);
            }
        });

    }


    public String getEtText(EditText et) {
        return et.getText().toString().trim();
    }

    public String getEtText(TextView et) {
        return et.getText().toString().trim();
    }

    public boolean isEtEmpty(String etStr) {
        return TextUtils.isEmpty(etStr);
    }
}
