package com.mac.runtu.activity.agricumach;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.personcentre.LoginActivity;
import com.mac.runtu.business.AdBiz;
import com.mac.runtu.business.MyShowDialog;
import com.mac.runtu.business.TopImageBannerBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnGetData2BooleanListener;
import com.mac.runtu.javabean.AgricuMachineryDetailInfo;
import com.mac.runtu.javabean.AgricuMachineryInfoFromUuid;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.utils.Content2StrUtils;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.TimeUtils;
import com.mac.runtu.utils.UiUtils;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 农资农机详情页
 */
public class AgriculturalMachineryDetailsActivity extends AppCompatActivity {

    private static final String TAG = "AgriculturalMachineryDetailsActivity";
    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.phone_service_Iv)
    ImageView phoneServiceIv;
    @BindView(R.id.banner)
    Banner    banner;
    @BindView(R.id.tv_image_price)
    TextView  tvImagePrice;
    @BindView(R.id.tv_title)
    TextView  tvTitle;

    @BindView(R.id.tv_liulanCount)
    TextView  tvLiulanCount;
    @BindView(R.id.tv_creatTime)
    TextView  tvCreatTime;
    @BindView(R.id.tv_price)
    TextView  tvPrice;
    @BindView(R.id.tv_address)
    TextView  tvAddress;
    @BindView(R.id.tv_kind1)
    TextView  tvKind1;
    @BindView(R.id.tv_kind2)
    TextView  tvKind2;
    @BindView(R.id.tv_des)
    TextView  tvDes;
    @BindView(R.id.user_photo_Iv)
    ImageView userPhotoIv;
    @BindView(R.id.tv_people_name)
    TextView  tvPeopleName;
    private String                    uuid;
    private AgricuMachineryDetailInfo data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agricultural_machinery_details);
        ButterKnife.bind(this);

        uuid = getIntent().getStringExtra(GlobalConstants
                .key_uuid);
        LogUtils.e(TAG, "农资农机的uuid" + uuid);
        initData();

    }

    private void initData() {
        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .mach_detail_url, null, GlobalConstants
                        .key_uuid,
                uuid, new MyHttpUtils.OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {
                        if (result != null) {
                            parseData(result);
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {

                    }
                });
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

    //解析数据
    private void parseData(String result) {
        AgricuMachineryInfoFromUuid info = GSonUtil
                .parseJson(result, AgricuMachineryInfoFromUuid.class);

        data = info.objList;

        if (data != null) {
            //图片
            ArrayList<PictureInfo> pictureInfos = data.pictureInfos;
            if (pictureInfos != null) {
                showAdImage(pictureInfos);

            }

            tvTitle.setText(data.title);
            tvKind1.setText(data.kindDictionaryInfo.kindName);
            tvKind2.setText(data.kindDictionaryInfo.kindName);
            tvCreatTime.setText(TimeUtils.setCreatTime(data.createTime));
            tvLiulanCount.setText("浏览" + data.hitCount + "次");

            tvImagePrice.setText("¥" + data.price + "万元");
            tvPrice.setText("¥" + data.price);

            //地址
            StringBuilder builder = new StringBuilder();
            builder.append("");
            builder.append(TextUtils.isEmpty(data.city) ? "" : data.city
                    + "/");
            builder.append(TextUtils.isEmpty(data.county) ? "" : data.county
                    + "/");
            builder.append(TextUtils.isEmpty(data.town) ? "" : data.town);
            tvAddress.setText(builder);


            Content2StrUtils.setContentStr(data.content, tvDes);
            tvPeopleName.setText(data.contacts);

            //userPhotoIv.setOnClickListener();

        }

    }

    private void showAdImage(ArrayList<PictureInfo> pictureInfos) {

        //获得轮播条数据
        //广告轮播条数据
        ArrayList<String> imageUrls = AdBiz.getImageListUrl(pictureInfos);
        TopImageBannerBiz bannerBiz = new TopImageBannerBiz
                (banner);

        if (imageUrls.size() > 0) {
            bannerBiz.autoShowImage(imageUrls);
        } else {
            bannerBiz.autoShowImage(AdBiz.getImageListId());
        }


    }

    @OnClick({R.id.back_Iv, R.id.phone_service_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.phone_service_Iv:

                if (UiUtils.isLogin()) {
                    MyShowDialog.ShowCallPhone(this, data.phone,data.contacts);
                } else {
                    showDialogLoginInfo();
                }
                break;
        }
    }

    /**
     * 提示用户没用登录
     */
    private void showDialogLoginInfo() {
        MyShowDialog.showDialogInfo(this, new OnGetData2BooleanListener() {
            @Override
            public void onSuccess(Boolean result) {
                startActivity(new Intent(AgriculturalMachineryDetailsActivity
                        .this, LoginActivity.class));
            }

            @Override
            public void onFail() {

            }
        });
    }
}
