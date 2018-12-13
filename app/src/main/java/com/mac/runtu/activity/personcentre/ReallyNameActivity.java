package com.mac.runtu.activity.personcentre;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.moreimage.AlbumsActivity;
import com.mac.runtu.business.LoginBiz;
import com.mac.runtu.business.MyShowDialog;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.interfaceif.OnBooleanListener;
import com.mac.runtu.javabean.IDCardImageInfo;
import com.mac.runtu.javabean.ReallyNameInfo;
import com.mac.runtu.utils.FileuUtils;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.ImageCompressUtils;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 实名认证页面
 */
public class ReallyNameActivity extends AppCompatActivity {

    private static final String TAG = "ReallyNameActivity";

    @BindView(R.id.back_Iv)
    ImageView    backIv;
    @BindView(R.id.et_rel_name)
    EditText     etRelName;
    @BindView(R.id.tv_usernum)
    TextView     tvUsernum;
    @BindView(R.id.et_idnum)
    EditText     etIdnum;
    @BindView(R.id.iv_addImage)
    ImageView    ivAddImage;
    @BindView(R.id.iv_addImage1)
    ImageView ivAddImage1;
    @BindView(R.id.iv_addImage2)
    ImageView ivAddImage2;
    @BindView(R.id.btn_ok)
    Button       btnOk;
    @BindView(R.id.tv_content)
    TextView     tvContent;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    private int position = 0;
    private ArrayList<String> imageNameList;
    private ArrayList<String> imagesContentPathList;
    private IDCardImageInfo   imageInfo;

    private String idNum;
    private String uerRelName;

    //审核地状态


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_really_name);
        ButterKnife.bind(this);

        //提示进度窗口
        tvContent.setText("上传中..");
        llLoading.setVisibility(View.GONE);
        //照片路径归零
        MarkerConstants.clearImages();
        tvUsernum.setText(UiUtils.getUserNumber());

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


    @Override
    protected void onResume() {
        super.onResume();

        if (!TextUtils.isEmpty(MarkerConstants.value_more_image_url)) {
            this.ivAddImage.setVisibility(View.GONE);
            this.ivAddImage1.setVisibility(View.VISIBLE);
            this.ivAddImage2.setVisibility(View.VISIBLE);
            ImageLoadUtils.rectangleImage
                    (this, MarkerConstants.value_more_image_url, ivAddImage1);
            ImageLoadUtils.rectangleImage(this, MarkerConstants.value_more_image_url1, ivAddImage2);
        }else{
            this.ivAddImage.setVisibility(View.VISIBLE);
            this.ivAddImage1.setVisibility(View.GONE);
            this.ivAddImage2.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.back_Iv, R.id.iv_addImage, R.id.btn_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:

                MarkerConstants.clearImages();
                ivAddImage.setImageDrawable(null);

                finish();
                break;
            case R.id.iv_addImage:

                Intent intentImage = new Intent
                        (this, AlbumsActivity.class);
                //提示来自实名认证
                MarkerConstants.isFromRelName = true;
                startActivity(intentImage);
                break;
            case R.id.btn_ok:
                llLoading.setVisibility(View.VISIBLE);
                dataJudge();

                break;
        }
    }

    /**
     * 数据校验
     */
    private void dataJudge() {


        uerRelName = etRelName.getText().toString().trim();

        if (TextUtils.isEmpty(uerRelName)) {

            ToastUtils.makeTextShowEmpty(getApplication());
            return;
        }

        idNum = etIdnum.getText().toString().trim();

        if (TextUtils.isEmpty(idNum)) {

            ToastUtils.makeTextShowEmpty(getApplication());
            return;
        }


        //15位数身份证验证正则表达式：
        String isIDCard1 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)" +
                "|3[0-1])" +
                "\\d{3}$";

        //18位数身份证验证正则表达式 ：
        String isIDCard2 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(" +
                "([0|1|2]\\d)" +
                "|3[0-1])\\d{3}([0-9]|X)$";


        if (idNum.matches(isIDCard1) | idNum.matches(isIDCard2)) {


            //得到图片名称集合
            imageNameList = MarkerConstants
                    .value_more_image_name;

            if (imageNameList == null || imageNameList.size() < 2) {

                ToastUtils.makeTextShow(getApplication(), "请选择两张照片");
                return;
            }

            btnOk.setEnabled(false);
            upLoadingImages2Net();
        }else {
            ToastUtils.makeTextShow(getApplication(), "格式不正确");
            //
        }



    }


    /**
     * 提交数据到网络
     */
    private void upLoadingImages2Net() {

        //上传图片到网络
        imagesContentPathList = MarkerConstants
                .value_more_image_content_path;

        LogUtils.e("path:",MarkerConstants
                .value_more_image_content_path+"");

        imageInfo = new IDCardImageInfo();
        //imageInfo.informationUuid = uuid;
        upLoadingImage();


    }

    /**
     * 上传图片
     */
    private void upLoadingImage() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                imageInfo.status = String.valueOf(position); //status从一开始
                imageInfo.picture = FileuUtils.file2StringGzip(imagesContentPathList.get
                        (position));
                imageInfo.img_name = imageNameList.get(position);
                imageInfo.uuid = UiUtils.getUserID();
                    String infoJson = GSonUtil.obj2json(imageInfo);


                LogUtils.e(TAG, "infoJson ==" + infoJson);

                MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                        .user_rel_name_image_url, null, GlobalConstants
                        .key_info, infoJson, new MyHttpUtils
                        .OnNewWorkRequestListener2Boolean() {

                    @Override
                    public void onNewWorkSuccess(Boolean result) {
                        if (result) {
                            position++;
                            LogUtils.e(TAG, "result ==" + result);

                            if (position < imageNameList.size()) {
                                LogUtils.e(TAG, "position ==" + position);
                                upLoadingImage();

                            } else {
                                //上传用户信息
                                upLoadingInfo2Net();
                            }
                        } else {

                            dataUploadFail();
                        }

                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        LogUtils.e("msg",msg);
                        uploadFail();
                    }
                });
            }
        }).run();




    }



    /**
     * 上传资料
     */
    private void upLoadingInfo2Net() {

        ReallyNameInfo info = new ReallyNameInfo();


        info.name = uerRelName;
        info.IDnumber = idNum;
        info.uuid = UiUtils.getUserID();


        String infoJson = GSonUtil.obj2json(info);


        LogUtils.e(TAG, "客户信息" + infoJson);

        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                        .user_rel_name_detail_url,
                null,
                GlobalConstants
                        .key_info, infoJson, new MyHttpUtils
                        .OnNewWorkRequestListener2Boolean() {


                    @Override
                    public void onNewWorkSuccess(Boolean result) {


                        if (result) {

                            MarkerConstants.clearImages();
                            imageInfo = null;

                            //更新用户底层数据
                            LoginBiz.getInstance().getUserInfo4Net();

                            llLoading.setVisibility(View.GONE);

                            showDialogInfo();

                        } else {

                            llLoading.setVisibility(View.GONE);
                            ToastUtils.makeTextShowDataCommitFail
                                    (ReallyNameActivity.this);

                        }

                        btnOk.setEnabled(true);
                    }

                    @Override
                    public void onNewWorkError(String msg) {

                        uploadFail();
                    }
                });
    }

    /**
     * 资料上传失败
     */
    private void dataUploadFail() {
        llLoading.setVisibility(View.GONE);
        ToastUtils.makeTextShowDataCommitFail
                (ReallyNameActivity.this);
        position = 0;
        btnOk.setEnabled(true);
    }

    /**
     * 资料上传无网络
     */
    private void uploadFail() {

        llLoading.setVisibility(View.GONE);

        ToastUtils.makeTextShowNoNet
                (ReallyNameActivity
                        .this);
        position = 0;

        btnOk.setEnabled(true);
    }

    /**
     * 提示用户审核时间
     */
    private void showDialogInfo() {

        MyShowDialog.showDialogSelectImage("提示信息:", "您的资料上传成功\n身份信息审核1-7天",
                ReallyNameActivity.this, new OnBooleanListener() {


                    @Override
                    public void onResultIsTrue() {

                        setResult(RESULT_OK);

                        finish();
                    }
                });
    }


}
