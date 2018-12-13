package com.mac.runtu.activity.property;

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
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.javabean.PropertyDetailInfo;
import com.mac.runtu.javabean.PropertyInfoFromUUid;
import com.mac.runtu.utils.Content2StrUtils;
import com.mac.runtu.utils.ExChange2StrUtils;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.TimeUtils;
import com.mac.runtu.utils.UiUtils;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 产权流转详情页
 */
public class PropertyRightsDetailsActivity extends AppCompatActivity {


    @BindView(R.id.back_Iv)
    ImageView backIv;

    @BindView(R.id.banner)
    Banner    banner;
    @BindView(R.id.tv_image_price)
    TextView  tvImagePrice;
    @BindView(R.id.tv_title)
    TextView  tvTitle;
    @BindView(R.id.tv_exchange)
    TextView  tvExchange;
    @BindView(R.id.tv_liulanCount)
    TextView  tvLiulanCount;
    @BindView(R.id.tv_creatTime)
    TextView  tvCreatTime;
    @BindView(R.id.tv_price)
    TextView  tvPrice;
    @BindView(R.id.tv_address)
    TextView  tvAddress;
    @BindView(R.id.tv_kind)
    TextView  tvKind;
    @BindView(R.id.tv_size)
    TextView  tvSize;
    @BindView(R.id.tv_time)
    TextView  tvTime;
    @BindView(R.id.tv_exchange02)
    TextView  tvExchange02;
    @BindView(R.id.tv_des)
    TextView  tvDes;
    @BindView(R.id.user_photo_Iv)
    ImageView userPhotoIv;
    @BindView(R.id.tv_people_name)
    TextView  tvPeopleName;
    @BindView(R.id.phone_service_Iv)
    ImageView phoneServiceIv;
    private String             uuid;
    private PropertyDetailInfo infoDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_rights_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        uuid = intent.getStringExtra(GlobalConstants.key_uuid);

        initData();

    }

    private void initData() {
        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .property_info_Detail_url, null, GlobalConstants
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
        PropertyInfoFromUUid propertyInfoFromUUid = GSonUtil.parseJson
                (result, PropertyInfoFromUUid.class);
        infoDetail = propertyInfoFromUUid.objList;

        if (infoDetail != null) {
            tvImagePrice.setText("¥"+infoDetail.price + "元");

            //图片价格
            ArrayList<PictureInfo> pictureInfos = infoDetail.pictureInfos;
            showAdImage(pictureInfos);


            tvTitle.setText(infoDetail.title);

            String exchange = infoDetail.exchange;

            if (!TextUtils.isEmpty(exchange)) {
                String change = ExChange2StrUtils.setExChange(infoDetail
                        .exchange);
                tvExchange.setText(change);
                tvExchange02.setText(change);


            }

            tvCreatTime.setText(TimeUtils.setCreatTime(infoDetail.createTime));
            tvLiulanCount.setText("浏览" + infoDetail.hitCount + "次");
            tvTime.setText(TimeUtils.setCreatTime(infoDetail.createTime));
            tvPrice.setText("¥" + infoDetail.price);

            StringBuilder builder = new StringBuilder();
            builder.append("");

            builder.append(TextUtils.isEmpty(infoDetail.province) ? "" : infoDetail.province
                    + "/");
            builder.append(TextUtils.isEmpty(infoDetail.city) ? "" : infoDetail.city
                    + "/");
            builder.append(TextUtils.isEmpty(infoDetail.county) ? "" : infoDetail
                    .county);
            tvAddress.setText(builder.toString());

            tvKind.setText(infoDetail.kindDictionaryInfo.kindName);
            tvSize.setText(infoDetail.area + "亩");
            tvTime.setText(infoDetail.years + "年");

            Content2StrUtils.setContentStr(infoDetail.content, tvDes);
            //tvDes.setText
            // ("产权是经济所有制关系的法律表现形式。它包括财产的所有权、占有权、支配权、使用权、收益权和处置权。在市场经济条件下，产权的属性主要表现在三个方面：产权具有经济实体性、产权具有可分离性、产权流动具有独立性。产权的功能包括：激励功能、约束功能、资源配置功能、协调功能。以法权形式体现所有制关系的科学合理的产权制度，是用来巩固和规范商品经济中财产关系，约束人的经济行为，维护商品经济秩序，保证商品经济顺利运行的法权工具。");
            tvPeopleName.setText(infoDetail.contacts);


        }

    }

    private void showAdImage(ArrayList<PictureInfo> pictureInfos) {

        //获得轮播条数据
        //广告轮播条数据
        ArrayList<String> imageUrls = AdBiz.getImageListUrl(pictureInfos);

        TopImageBannerBiz bannerBiz = new TopImageBannerBiz
                (banner);

        if (imageUrls.size()>0) {
            bannerBiz.autoShowImage(imageUrls);
        }else {
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
                if (infoDetail != null) {
                    if (UiUtils.isLogin()) {
                        MyShowDialog.ShowCallPhone(this, infoDetail.phone,infoDetail.contacts);
                    } else {
                        showDialogLoginInfo();
                    }
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
                startActivity(new Intent(PropertyRightsDetailsActivity
                        .this, LoginActivity.class));
            }

            @Override
            public void onFail() {

            }
        });
    }
}
