package com.mac.runtu.activity.personcentre;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.PublishIsLoginActivity;
import com.mac.runtu.business.AddressBiz4FromPro3;
import com.mac.runtu.business.LoginBiz;
import com.mac.runtu.business.MyShowDialog;
import com.mac.runtu.business.ReallyNameBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.interfaceif.OnDialogListener;
import com.mac.runtu.javabean.UserInfo;
import com.mac.runtu.utils.FileuUtils;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyData;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.SPUtils;
import com.mac.runtu.utils.TimeUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 个人资料
 */
public class PersonalInfoActivity extends PublishIsLoginActivity {


    private static final String TAG = "PersonalInfoActivity";

    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.tr_user_image)
    TableRow  trUserImage;
    @BindView(R.id.tr_user_nickName)
    TableRow  trUserNickName;
    @BindView(R.id.tr_user_reallyName)
    TableRow  trUserReallyName;
    @BindView(R.id.tr_user_sex)
    TableRow  trUserSex;
    @BindView(R.id.tr_user_status)
    TableRow  trUserStatus;
    @BindView(R.id.tv_user_birthdate)
    TextView  tvUserBirthdate;
    @BindView(R.id.tr_user_birth_deta)
    TableRow  trUserBirthDeta;
    @BindView(R.id.tr_user_changepwd)
    TableRow  trUserChangepwd;

    //昵称
    @BindView(R.id.tv_nickName)
    TextView tvNickName;
    //真名
    @BindView(R.id.tv_reallyName)
    TextView tvReallyName;
    //性别
    @BindView(R.id.tv_sex)
    TextView tvSex;
    //身份
    @BindView(R.id.tv_shenFen)
    TextView tvShenFen;
    //详细信息
    @BindView(R.id.et_detialAddress)
    EditText etDetialAddress;


    @BindView(R.id.tv_content)
    TextView     tvContent;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    @BindView(R.id.tr_real_name)
    TableRow trRealName;
    @BindView(R.id.tv_reg_num)
    TextView tvRegNum;
    @BindView(R.id.tv_reallyName_result)
    TextView tvReallyNameResult;

    private Button               btnConfirm;
    private TextView             tvProvince;
    private TextView             tvCity;
    private TextView             tvArea;
    private ImageView            ivProvince;
    private ImageView            ivCity;
    private ImageView            ivArea;
    //用户头像
    private ImageView            ivUserImage;
    private String               imagePath;
    private UserInfo.UserDesInfo userDesInfo;

    private String userDesInfoStr;
    private String  imageDeatil = "";
    private boolean isHaveImage = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_personal_center_data);
        ButterKnife.bind(this);

        initVeiw();
        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();

        initDate();

        ivUserImage.setImageDrawable(null);
        if (!TextUtils.isEmpty(imagePath)) {
            ImageLoadUtils.roundImageNoCache(this, imagePath, ivUserImage);
        } else {
            ImageLoadUtils.roundImageNoCache(this,
                    SPUtils.getString(this,
                            GlobalConstants.SP_use_iamgepath, ""),
                    ivUserImage);
        }


    }

    //初始化布局
    private void initVeiw() {

        llLoading.setVisibility(View.GONE);

        ivUserImage = (ImageView) findViewById(R.id.iv_user_image);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        tvProvince = (TextView) findViewById(R.id.tv_province);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvArea = (TextView) findViewById(R.id.tv_area);
        ivProvince = (ImageView) findViewById(R.id.iv_province);
        ivCity = (ImageView) findViewById(R.id.iv_city);
        ivArea = (ImageView) findViewById(R.id.iv_area);


        new AddressBiz4FromPro3(this, tvProvince, ivProvince, tvCity,
                ivCity, tvArea, ivArea, null, null, null, null);


        tvReallyNameResult.setText(ReallyNameBiz.getResultName());

    }

    //初始化话数据
    private void initDate() {

        tvContent.setText("提交资料");
        tvRegNum.setText(UiUtils.getUserNumber());


        //用户信息对象
        LoginBiz loginBiz = LoginBiz.getInstance();
        userDesInfo = loginBiz.userDesInfo;

        if (userDesInfo == null) {

            getUserData4Net(loginBiz);
        } else {
            initUserMessage();
        }

        //实名认证结果
        tvReallyNameResult.setText(ReallyNameBiz.getResultName());
    }

    /**
     * 内存中用户信息被干掉  访问网路
     *
     * @param loginBiz
     */
    private void getUserData4Net(LoginBiz loginBiz) {
        loginBiz.getUserInfo4Net(new LoginBiz.OnDataListener() {
            @Override
            public void isSuccess(UserInfo.UserDesInfo info) {
                userDesInfo = info;
                initUserMessage();
            }

            @Override
            public void onNetWorkFail() {
                ToastUtils.makeTextShowNoNet(PersonalInfoActivity.this);
                btnConfirm.setEnabled(false);
            }
        });
    }

    private void initUserMessage() {

        if (this.userDesInfo != null) {

            //显示图像
            if (!TextUtils.isEmpty(this.userDesInfo.headimg)) {
                ImageLoadUtils.roundImageNoCache(this,
                        SPUtils.getString(this,
                                GlobalConstants.SP_use_iamgepath, ""),
                        ivUserImage);
                isHaveImage = true;
            }

            //昵称
            setTVText(this.userDesInfo.nickname, tvNickName);

            //setTVText(this.userDesInfo.name, tvReallyName);
            tvReallyName.setText(UiUtils.getUserName());
            //地址
            setTVText(this.userDesInfo.province, tvProvince);
            setTVText(this.userDesInfo.city, tvCity);
            setTVText(this.userDesInfo.county, tvArea);

            setTVText(this.userDesInfo.address, etDetialAddress);
            //性别
            if (this.userDesInfo.sex != null) {

                LogUtils.e("sex",userDesInfo.sex);
                if(userDesInfo.sex.equals("0")){
                    tvSex.setText("男");
                }else{
                    tvSex.setText("女");
                }
            }


            String sfSign = TextUtils.isEmpty(this.userDesInfo.usertype) ? "1"
                    : this.userDesInfo.usertype;
            setShenFenInfoText(SPUtils.getString(this,GlobalConstants.userType,"3"));

            setTVText(TimeUtils.setCreatTime(this.userDesInfo.birthday),
                    tvUserBirthdate);
        } else {
            userDesInfo = new UserInfo().new UserDesInfo();
            LogUtils.e(TAG, "userDesInfo" + userDesInfo.toString());
            //设置默认当前时间
            tvUserBirthdate.setText(MyData.getInstance().getDataStr(null));
        }
    }


    private void initListener() {
        //确认按钮
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                createUserJson();

                LogUtils.e("内容", userDesInfoStr);

                upLoadData2Net(userDesInfoStr);

            }
        });

    }

    /**
     * 上传用户信息到网络
     */
    private void upLoadData2Net(String userDesInfoStr) {

        llLoading.setVisibility(View.VISIBLE);


        HashMap<String, String> hashMap = new HashMap<>();
        //hashMap.put("headPictrue", imageDeatil);
        hashMap.put("userInfo", userDesInfoStr);


        LogUtils.e("userinfo",userDesInfoStr);
        //封印按钮
        btnConfirm.setEnabled(false);
        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                .USER_COMMIT_URL, null, hashMap, new MyHttpUtils
                .OnNewWorkRequestListener2Boolean() {

            @Override
            public void onNewWorkSuccess(Boolean result) {
                if (result) {
                    //修改内存里面用户的 信息
                    btnConfirm.setEnabled(true);
                    llLoading.setVisibility(View.GONE);
                    LogUtils.e(TAG,"SUCCESS");
                    successSaveData();

                } else {
                    ToastUtils.makeTextShow(PersonalInfoActivity
                            .this, "资料提交失败");
                    btnConfirm.setEnabled(true);
                    LogUtils.e(TAG,"failed");

                    llLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNewWorkError(String msg) {
                ToastUtils.makeTextShowNoNet(PersonalInfoActivity.this);
                LogUtils.e(TAG,"onNewWorkError");
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 封装用户信息到Json
     */
    private void createUserJson() {
        //图片
        if (imagePath != null) {
            //得到名字
            String imageName = imagePath.substring(imagePath
                    .lastIndexOf("/") + 1) + userDesInfo.phone + "" +
                    ".jpg";

            userDesInfo.headPictrue = FileuUtils.file2String(imagePath);

            //单独体检网络
            userDesInfo.headimg = imageName;
            //或者给用户对象
            LogUtils.e(TAG, "imageName=" + imageName);
        }else{
            userDesInfo.headimg = "";
        }

        userDesInfo.nickname = getTVText(tvNickName);
        userDesInfo.name = getTVText(tvReallyName);

        //有可能传code
        userDesInfo.province = getTVText(tvProvince);

        userDesInfo.city = getTVText(tvCity);

        userDesInfo.county = getTVText(tvArea);

        userDesInfo.address = getTVText(etDetialAddress);

        switch (tvSex.getText().toString().trim()) {
            case "男":
                userDesInfo.sex = "0";
                break;
            case "女":
                userDesInfo.sex = "1";
                break;
        }


        switch (tvShenFen.getText().toString().trim()) {
            case "客商":
                SPUtils.setString(this,GlobalConstants.userType,"1");
                break;
            case "农户":
                SPUtils.setString(this,GlobalConstants.userType,"2");
                break;
            case "普通用户":
                SPUtils.setString(this,GlobalConstants.userType,"3");
                break;
        }

        userDesInfo.device = "123456";


        //生日
        userDesInfo.birthday = getTVText(tvUserBirthdate);

        //json
        userDesInfoStr = new Gson().toJson(userDesInfo);


    }

    /**
     * 资料上传成功存储数据
     */
    private void successSaveData() {

        ToastUtils.makeTextShow(PersonalInfoActivity
                .this, "资料提交成功");

        //告诉系统用户资料用变动,刷新本地数据
        MarkerConstants.isUpdataUserInfoData = true;

        SPUtils.setString(UiUtils.getContext(),
                GlobalConstants.SP_use_iamgepath,
                imagePath);

        //跟新底层存储的数据
        LoginBiz.getInstance().getUserInfo4Net();

        finish();
    }

    //赋值
    private void setTVText(String value, TextView tv) {
        if (!TextUtils.isEmpty(value)) {
            tv.setText(value);
        }
    }

    //赋值
    private void setTVText(String value, EditText et) {
        if (!TextUtils.isEmpty(value)) {
            et.setText(value);
        }
    }


    //赋值
    private String getTVText(TextView tv) {
        String value = tv.getText().toString().trim();
        if (!TextUtils.isEmpty(value)) {
            return value;
        }
        return "";
    }

    //赋值
    private String getTVText(EditText tv) {
        String value = tv.getText().toString().trim();
        if (!TextUtils.isEmpty(value)) {
            return value;
        }
        return "";
    }


    @OnClick({R.id.back_Iv, R.id.tr_user_image, R.id.tr_user_nickName, R.id
            .tr_user_reallyName, R.id.tr_user_sex, R.id.tr_user_status, R.id
            .tv_user_birthdate, R.id.tr_user_birth_deta, R.id
            .tr_user_changepwd, R.id.tr_real_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.tr_user_image:

                ivUserImage.setImageDrawable(null);

                Crop.pickImage(this);
                break;
            case R.id.tr_user_nickName:
                showDialogGetName("请输入昵称:", tvNickName);
                break;
            case R.id.tr_user_reallyName:
                if (UiUtils.getAttestationStuts() == GlobalConstants
                        .reaNemAttestationSucc) {
                } else {
                    Intent intent2 = new Intent(this, AgreementTextActivity.class);
                    intent2.putExtra(GlobalConstants.key_kind, 2);
                    startActivity(intent2);
                }

                break;
            case R.id.tr_user_sex:

                getUserGender();

                break;
            case R.id.tr_user_status:

                getUserShenFen();
                break;
            case R.id.tv_user_birthdate:

                break;
            case R.id.tr_user_birth_deta:
                //生日
                getBirthday();

                break;
            case R.id.tr_real_name:
                //实名认证
                go2ReallyName();

                break;

            case R.id.tr_user_changepwd:

                Intent intent = new Intent(PersonalInfoActivity.this,
                        ForgetOnePwdActivity.class);

                intent.putExtra(GlobalConstants.key_isChangerPwd, true);

                //跳转忘记密码页
                startActivity(intent);

                break;
        }
    }

    /**
     * 获得用户性别
     */
    private void getUserGender() {
        String sex = tvSex.getText().toString().trim();

        if (sex.equals("男")) {
            tvSex.setText("女");
            userDesInfo.sex = "女";
        } else {
            tvSex.setText("男");
            userDesInfo.sex = "男";
        }


        LogUtils.e("sex",userDesInfo.sex);
    }

    /**
     * 获得用户身份
     */
    private void getUserShenFen() {

        MyShowDialog.showDialogSelect(PersonalInfoActivity.this, "身份选择 :",
                new OnDialogListener() {


                    @Override
                    public void onSuccess(String result) {

                        setShenFenInfoText(result);
                    }

                    @Override
                    public void onFail() {

                    }
                });


    }

    private void setShenFenInfoText(String result) {

        switch (result) {
            case "1":
                tvShenFen.setText("客商");
                break;
            case "2":
                tvShenFen.setText("农户");
                break;
            case "3":
                tvShenFen.setText("普通用户");
                break;

        }

    }

    /**
     * 获得用户生日
     */
    private void getBirthday() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog
                .OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int
                    monthOfYear, int dayOfMonth) {
                tvUserBirthdate.setText(year + "-" + (monthOfYear +
                        1) + "-" + dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get
                (Calendar.DAY_OF_MONTH)).show();
    }


    /**
     * 弹框
     *
     * @param name
     * @param tv
     */
    public void showDialogGetName(String name, final TextView tv) {

        MyShowDialog.showDialogGetName(this, name, new OnDialogListener() {
            @Override
            public void onSuccess(String result) {
                tv.setText(result);
            }

            @Override
            public void onFail() {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            result) {

        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            //相册
            beginCrop(result.getData());

        } else if (requestCode == Crop.REQUEST_CROP) {
            //文件
            handleCrop(resultCode, result);

        }
    }

    private void beginCrop(Uri source) {

        //第二个是图片名称
        String imageName = String.valueOf(System.currentTimeMillis());

        Uri destination = Uri.fromFile(new File(getCacheDir(), imageName));

        Crop.of(source, destination).asSquare().start(this);

    }

    private void handleCrop(int resultCode, Intent result) {

        if (resultCode == RESULT_OK) {

            Uri output = Crop.getOutput(result);

            imagePath = output.getPath();

            ImageLoadUtils.roundImageNoCache(this, imagePath, ivUserImage);

            //isHaveImage = false;
            //将图片转成字符串提交网络
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast
                    .LENGTH_SHORT).show();
        }
    }


    private void go2ReallyName() {

        if (UiUtils.getAttestationStuts() == GlobalConstants
                .reaNemAttestationSucc) {

            ToastUtils.makeTextShow(getApplication(), "已认证通过");
        } else if(UiUtils.getAttestationStuts() == GlobalConstants
                .reaNemAttestationIng){
            Toast.makeText(getApplicationContext(),"您的身份正在审核中！",Toast.LENGTH_LONG).show();
        } else
            {
            Intent intent2 = new Intent(this, AgreementTextActivity.class);
            intent2.putExtra(GlobalConstants.key_kind, 2);
            startActivity(intent2);
        }
    }
}
