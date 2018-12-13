package com.mac.runtu.activity.agricumach;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.PublishIsLoginActivity;
import com.mac.runtu.business.CheckCodeBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.interfaceif.OnCodeTestTestListener;
import com.mac.runtu.javabean.AgricuMachineryCommitInfo;
import com.mac.runtu.javabean.PictureCommiteInfo;
import com.mac.runtu.utils.FileuUtils;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgriculturaMachineryRelease2Activity extends
        PublishIsLoginActivity {

    private static final String TAG = "AgriculturaMachineryRelease2Activity";
    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.text1)
    TextView  text1;
    @BindView(R.id.et_name)
    TextView  etName;
    @BindView(R.id.et_number)
    TextView  etNumber;
    @BindView(R.id.et_code)
    EditText  etCode;
    @BindView(R.id.tv_btnCode)
    TextView  tvBtnCode;
    @BindView(R.id.release_Iv)
    ImageView releaseIv;
    private AgricuMachineryCommitInfo mInfo;
    private String                    uuid;
    private String                    name;
    private String                    phone;
    private LinearLayout              llLoading;

    private int position = 0;
    private ArrayList<String>  imageNameList;
    private ArrayList<String>  imagesContentPathList;
    private PictureCommiteInfo imageInfo;

    private String mCode4Net;

    private int getCodeTotalTime = 60;

    private  Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            getCodeTotalTime--;
            tvBtnCode.setText("剩余" + getCodeTotalTime + "秒");
            if (getCodeTotalTime == 0) {
                tvBtnCode.setText(R.string.get_verification_code);
                tvBtnCode.setEnabled(true);
                getCodeTotalTime = 60;
                mHandler.removeMessages(0);
            } else {
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agricultural_machinery_release2);
        ButterKnife.bind(this);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);

        mInfo = (AgricuMachineryCommitInfo) getIntent().getSerializableExtra
                (GlobalConstants.key_info);


        initData();
        getMainUuid();

    }

    private void initData() {
        etName.setText(UiUtils.getUserName());
        etNumber.setText(UiUtils.getUserNumber());

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
                        (AgriculturaMachineryRelease2Activity.this);
            }
        });

    }


    @OnClick({R.id.back_Iv, R.id.tv_btnCode, R.id.release_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.tv_btnCode:

                //判断是否要登陆
                if (!UiUtils.isLogin()) {
                    UiUtils.go2LoginAcivity();
                    return;
                }

                name = getEtText(etName);
                phone = getEtText(etNumber);

                if (isEtEmpty(name) || isEtEmpty(phone)) {
                    ToastUtils.makeTextShowContent
                            (AgriculturaMachineryRelease2Activity.this);
                    return;
                }

                tvBtnCode.setEnabled(false);
                getCode();
                break;
            case R.id.release_Iv:

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

                llLoading.setVisibility(View.VISIBLE);
                upLoadingImages2Net();

                break;
        }
    }


    private void getCode() {

        mHandler.sendEmptyMessageDelayed(0, 1000);
        tvBtnCode.setText("剩余" + getCodeTotalTime + "秒");
        //访问网络
        //得到验证码
        CheckCodeBiz.getCode(phone, new OnCodeTestTestListener() {

            @Override
            public void onData(String code) {

                mCode4Net = code;

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

    private void upLoadingImages2Net() {


        imageNameList = MarkerConstants
                .value_more_image_name;

        if (imageNameList != null && imageNameList.size() > 0) {
            //上传图片到网络
            imagesContentPathList = MarkerConstants
                    .value_more_image_content_path;

            LogUtils.e(TAG, "图片路径" + imagesContentPathList.get(0));

            imageInfo = new PictureCommiteInfo();
            imageInfo.informationUuid = uuid;
            imageInfo.modelType = GlobalConstants.AD_TYPE_BUSINESS;


            upLoadingImage();

        } else {
            upLoadingInfo2Net();
        }

    }

    private void upLoadingImage() {

        imageInfo.picture = FileuUtils.file2String(imagesContentPathList.get
                (position));

        LogUtils.e(TAG, "图片转成字符串的 ==" + imageInfo.picture);

        imageInfo.pictureName = imageNameList.get(position);

        String infoJson = GSonUtil.obj2json(imageInfo);
        LogUtils.e(TAG, "infoJson ==" + infoJson);

        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                .upLoading_images_url, null, GlobalConstants
                .key_info, infoJson, new MyHttpUtils
                .OnNewWorkRequestListener2Boolean() {

            @Override
            public void onNewWorkSuccess(Boolean result) {
                if (result) {
                    position++;

                    if (position < imageNameList.size()) {
                        upLoadingImage();

                    } else {
                        //上传用户信息
                        upLoadingInfo2Net();
                    }
                } else {
                    ToastUtils
                            .makeTextShowDataCommitFail
                                    (AgriculturaMachineryRelease2Activity.this);
                    position = 0;

                    llLoading.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNewWorkError(String msg) {
                ToastUtils.makeTextShowNoNet
                        (AgriculturaMachineryRelease2Activity.this);
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void upLoadingInfo2Net() {


        mInfo.phone = phone;
        mInfo.contacts = name;
        mInfo.uuid = uuid;


        mInfo.userUuid = UiUtils.getUserID();

        String infoJson = GSonUtil.obj2json(mInfo);


        LogUtils.e(TAG, "客户信息" + infoJson);

        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                        .mach_commit_url,
                null,
                GlobalConstants
                        .key_info, infoJson, new MyHttpUtils
                        .OnNewWorkRequestListener2Boolean() {


                    @Override
                    public void onNewWorkSuccess(Boolean result) {


                        if (result) {
                            ToastUtils
                                    .makeTextShowDataCommitSuccess
                                            (AgriculturaMachineryRelease2Activity.this);
                            llLoading.setVisibility(View.GONE);

                            MarkerConstants.value_more_image_content_path =
                                    null;
                            MarkerConstants.value_more_image_url = "";
                            MarkerConstants.value_more_image_name = null;
                            imageInfo = null;

                            setResult(RESULT_OK, new Intent());


                            finish();
                        } else {
                            ToastUtils
                                    .makeTextShowDataCommitFail
                                            (AgriculturaMachineryRelease2Activity.this);
                            llLoading.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        ToastUtils.makeTextShowNoNet
                                (AgriculturaMachineryRelease2Activity
                                        .this);
                        position = 0;
                        llLoading.setVisibility(View.GONE);
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
