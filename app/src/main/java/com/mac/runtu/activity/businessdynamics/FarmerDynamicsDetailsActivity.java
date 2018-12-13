package com.mac.runtu.activity.businessdynamics;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.personcentre.LoginActivity;
import com.mac.runtu.adapter.BusinessDatalistAdapter;
import com.mac.runtu.business.MyShowDialog;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnGetData2BooleanListener;
import com.mac.runtu.javabean.BusinessDataDetailInfo;
import com.mac.runtu.javabean.BusinessDataListInfoFromUuid;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.utils.Content2StrUtils;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.TimeUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 农户发布详情页
 */
public class FarmerDynamicsDetailsActivity extends AppCompatActivity {

    private static final String TAG = "FarmerDynamicsDetailsActivity";

    @BindView(R.id.back_Iv)
    ImageView      backIv;
    @BindView(R.id.relativeLayout1)
    RelativeLayout relativeLayout1;
    @BindView(R.id.tv_city)
    TextView       tvCity;
    @BindView(R.id.tv_area)
    TextView       tvArea;
    @BindView(R.id.tv_kind)
    TextView       tvKind;
    @BindView(R.id.linearLayout)
    LinearLayout   linearLayout;
    @BindView(R.id.line)
    View           line;
    @BindView(R.id.tv_title)
    TextView       tvTitle;
    @BindView(R.id.tv_creattime)
    TextView       tvCreattime;
    @BindView(R.id.tv_hitConunt)
    TextView       tvHitConunt;
    @BindView(R.id.title_Tv)
    TextView       titleTv;
    @BindView(R.id.content_Tv)
    TextView       contentTv;
    @BindView(R.id.linearLayout1)
    LinearLayout   linearLayout1;
    @BindView(R.id.ll_add_image)
    LinearLayout   llAddImage;
    @BindView(R.id.user_photo_Iv)
    ImageView      userPhotoIv;
    @BindView(R.id.tv_name)
    TextView       tvName;
    @BindView(R.id.phone_service_Iv)
    ImageView      phoneServiceIv;
    private String                            uuid;
    private BusinessDataDetailInfo            data;
    private ArrayList<BusinessDataDetailInfo> infoList;
    private BusinessDatalistAdapter           mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_dynamics_details);
        ButterKnife.bind(this);


        uuid = getIntent().getStringExtra(GlobalConstants
                .key_uuid);




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

        MyHttpUtils.getStringDataFromNet(GlobalConstants.business_detail_url,
                null, GlobalConstants.key_uuid, uuid, new MyHttpUtils
                        .OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {
                        if (result != null) {
                            parserData(result);
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        ToastUtils.makeTextShowNoNet
                                (FarmerDynamicsDetailsActivity.this);
                    }
                });
    }

    private void parserData(String result) {
        BusinessDataListInfoFromUuid info = GSonUtil
                .parseJson(result, BusinessDataListInfoFromUuid.class);

        data = info.objList;
        if (data != null) {
            tvCity.setText(data.city);
            tvArea.setText(data.county);
            tvKind.setText(data.kindDictionaryInfo.kindName);
            tvCreattime.setText(TimeUtils.setCreatTime(data.createTime));
            tvHitConunt.setText(data.hitCount);
            Content2StrUtils.setContentStr(data.content, contentTv);
            tvName.setText(data.contacts);
            tvTitle.setText(data.title);


            ArrayList<PictureInfo> infos = data.pictureInfos;
            if (infos != null && infos.size() > 0) {
                for (int i = 0; i < infos.size(); i++) {
                    ImageView imageView = new ImageView
                            (FarmerDynamicsDetailsActivity.this);

                   // ViewGroup.LayoutParams params = imageView
                            //.getLayoutParams();
                     //params.width = widthPixels;

                    imageView.setBackgroundResource(R.drawable.bg_icon_transparent);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                    LogUtils.e(TAG, "图片路径=" + GlobalConstants.Base_imgae_url
                            + infos.get(i)
                            .pictureName.trim());

                    ImageLoadUtils.rectangleImage(this,
                                    GlobalConstants.Base_imgae_url + infos
                                            .get(i)
                                            .pictureName.trim(), imageView);

                    llAddImage.addView(imageView);
                }
            }

        }
    }


    @OnClick({R.id.back_Iv, R.id.phone_service_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.phone_service_Iv:
                if (data != null) {
                    if (UiUtils.isLogin()) {
                        MyShowDialog.ShowCallPhone(this, data.phone,data.contacts);
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
                startActivity(new Intent(FarmerDynamicsDetailsActivity
                        .this, LoginActivity.class));
            }

            @Override
            public void onFail() {

            }
        });
    }
}
