package com.mac.runtu.activity.otherActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mac.runtu.R;
import com.mac.runtu.activity.agricumach.AgriculturalMachineryActivity;
import com.mac.runtu.activity.businessdynamics.BusinessDynamicsActivity;
import com.mac.runtu.activity.entrepreneurshiptraining.EntrepreneurshipTrainingActivity;
import com.mac.runtu.activity.experiencefarm.ExperienceFarmSlidingMenuActivity;
import com.mac.runtu.activity.laborscrvice.LaborServiceCooperationActivity;
import com.mac.runtu.activity.logisticsinformation.LogisticsInformationActivity;
import com.mac.runtu.activity.logisticsinformation.LogisticsLaowuActivity;
import com.mac.runtu.activity.personcentre.LoginActivity;
import com.mac.runtu.activity.personcentre.PersonalCenterActitity;
import com.mac.runtu.activity.property.CirculationOfPropertyRightsActivity;
import com.mac.runtu.activity.ruraltourism.RuralTourismActivity;
import com.mac.runtu.activity.specialtyshop.SpecialtyOneImageDetailActivity;
import com.mac.runtu.activity.specialtyshop.SpecialtySlidingMenuActivity;
import com.mac.runtu.business.AdBiz;
import com.mac.runtu.business.TopImageBannerBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.AdAddressInfo;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.PhoneNetWordUitls;
import com.mac.runtu.utils.SPUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;
import com.youth.banner.Banner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主页
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    @BindView(R.id.personal_center_Ll)
    LinearLayout personalCenterLl;


    //特产电商
    @BindView(R.id.ses_Iv)
    ImageView sesIv;

    //体验农场
    @BindView(R.id.ef_Iv)
    ImageView efIv;

    //劳务协作
    @BindView(R.id.lsc_Iv)
    ImageView lscIv;

    //客商动态
    @BindView(R.id.business_dynamics_LL)
    LinearLayout businessDynamicsLL;

    //产权流转
    @BindView(R.id.circulation_of_property_rights_LL)
    LinearLayout circulationOfPropertyRightsLL;
    //乡村旅游
    @BindView(R.id.rural_tourism_LL)
    LinearLayout ruralTourismLL;
    //物流支配
    @BindView(R.id.logistics_distribution_LL)
    LinearLayout logisticsDistributionLL;
    //培训
    @BindView(R.id.entrepreneurship_training_LL)
    LinearLayout entrepreneurshipTrainingLL;
    //农资农机在线市场
    @BindView(R.id.agricultural_machinery_Rl)
    LinearLayout agriculturalMachineryRl;

    @BindView(R.id.banner)
    Banner banner;

    private int               code;
    private String            description;
    private String            apkName;
    private String            target;
    private File              file;
    private ArrayList<AdAddressInfo.TopImageData> infoLists;
    private TopImageBannerBiz bannerBiz;
    //本地图片集合


    //private ConvenientBanner convenientBanner;
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

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        if (!PhoneNetWordUitls.isNetworkConnected(this)) {
            ToastUtils.makeTextShow(this, "请打开网络连接");
            //轮播条数据还是要看的
            showTopImages(null);
        }

        checkVersion();

        initBannerData();

    }


    void initBannerData() {
        //广告轮播条数据
        //直接方位路径
        showTopImages(null);
        //二选一
        AdBiz.getAdInfo(GlobalConstants.AD_TYPE_home_ad_image, new AdBiz
                .OnAdInfoListener() {

            @Override
            public void onSuccessInfoList(ArrayList<AdAddressInfo
                    .TopImageData> infoList, ArrayList<String> imageUrlList) {

                showTopImages(imageUrlList);
                infoLists = infoList;
            }

            @Override
            public void onfail() {
                showTopImages(null);
            }
        });


    }


    //轮播条数据
    private void showTopImages(final ArrayList<String> imageUrlList) {
        bannerBiz = new TopImageBannerBiz(banner);

        if (imageUrlList != null && imageUrlList.size() > 0) {
            LogUtils.e(TAG, "轮播条的图片" + imageUrlList.toString());
            bannerBiz.autoShowImage(imageUrlList, new TopImageBannerBiz
                    .OnBannerClickeListener() {

                @Override
                public void onBannerClickeListener(int paramAnonymousInt) {
                    if (!TextUtils.isEmpty((infoLists.get(paramAnonymousInt - 1)).path))
                    {
                        Intent localIntent = new Intent();
                        localIntent.setAction("android.intent.action.VIEW");
                        localIntent.setData(Uri.parse((infoLists.get(paramAnonymousInt - 1)).path));
                        MainActivity.this.startActivity(localIntent);
                    }
                }
            });

        } else {
            bannerBiz.autoShowImage(AdBiz.getImageListId());
        }
    }


    @OnClick({R.id.personal_center_Ll, R.id.business_dynamics_LL, R.id
            .circulation_of_property_rights_LL, R.id.rural_tourism_LL, R.id
            .logistics_distribution_LL, R.id.entrepreneurship_training_LL, R
            .id.lsc_Iv, R.id.agricultural_machinery_Rl, R.id.ef_Iv, R.id
            .ses_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_center_Ll:
                //个人中心
                if (!PhoneNetWordUitls.isNetworkConnected(this)) {
                    ToastUtils.makeTextShow(this, "请打开网络连接");
                    return;
                }
                go2PerCenAct();


                break;
            case R.id.business_dynamics_LL:
                startActivity(new Intent(MainActivity.this,
                        BusinessDynamicsActivity.class));
                break;
            case R.id.circulation_of_property_rights_LL:
                startActivity(new Intent(MainActivity.this,
                        CirculationOfPropertyRightsActivity.class));
                break;
            case R.id.rural_tourism_LL:
                startActivity(new Intent(MainActivity.this,
                        RuralTourismActivity.class));
                break;
            case R.id.logistics_distribution_LL:
                //物流信息
                startActivity(new Intent(MainActivity.this,
                        LogisticsInformationActivity.class));
                break;
            case R.id.entrepreneurship_training_LL:
                startActivity(new Intent(MainActivity.this,
                        EntrepreneurshipTrainingActivity.class));
                break;
            case R.id.lsc_Iv:
                startActivity(new Intent(MainActivity.this,
                        LogisticsLaowuActivity.class));
                break;
            case R.id.agricultural_machinery_Rl:
                startActivity(new Intent(MainActivity.this,
                        AgriculturalMachineryActivity.class));
                break;
            case R.id.ef_Iv:
                //体验农场


                startActivity(new Intent(MainActivity.this,
                        ExperienceFarmSlidingMenuActivity.class));
                break;
            case R.id.ses_Iv:
                startActivity(new Intent(MainActivity.this,
                        SpecialtySlidingMenuActivity
                                .class));
                break;


        }
    }

    private void go2PerCenAct() {

        if (UiUtils.isLogin()) {
            startActivity(new Intent(this, PersonalCenterActitity.class));
        } else {

            startActivityForResult(new Intent(this, LoginActivity.class), 2);
        }

    }


    //检查版本
    private void checkVersion() {

        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo
                    (getPackageName(), 0);
            //版本号
            code = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        getVersionfromNet();

    }


    //获得服务端版本信息
    public void getVersionfromNet() {

        MyHttpUtils.getStringDataFromNet(GlobalConstants.check_version_url,
                null, new MyHttpUtils.OnNewWorkRequestListener() {

                    @Override
                    public void onNewWorkSuccess(String result) {
                        parseString(result);

                    }

                    @Override
                    public void onNewWorkError(String msg) {

                    }
                });

    }


    private void parseString(String result) {
        LogUtils.e(TAG, result);

        try {
            JSONObject jo = new JSONObject(result);
            String versionCode = jo.getString("versionCode");
            Integer newCode = Integer.valueOf(versionCode);
            if (newCode > code) {
                //有版本跟新
                apkName = jo.getString("apkName");
                description = jo.getString("description");
                //弹框提示有版本跟新
                showUpdateDialog();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //弹框提示下载
    private void showUpdateDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("有新版本!").setMessage(description);
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载
                showUpdateProgess();
            }
        }).setNegativeButton("取消", null).show();

    }

    //弹框显示下载
    private void showUpdateProgess() {

        getSavePath();

        //展示进度的弹窗
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//水平进度条,可以展示进度
        dialog.setTitle("正在下载,请稍候...");
        dialog.setCancelable(false);//不可以取消(点击弹窗外侧,返回键,弹窗不消失)
        dialog.show();


        LogUtils.e(TAG, apkName);

        String downUrl = GlobalConstants.NEW_VERSION_URL + File
                .separator + apkName;

        MyHttpUtils.getFileFromNetWork(downUrl, target, apkName,
                new MyHttpUtils.OnNewWorkRequestListener2File() {
                    @Override
                    public void onNewWorkSuccess(File file) {
                        LogUtils.e(TAG, file.getAbsolutePath());
                        dialog.dismiss();

                        //跳转安装页
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setDataAndType(Uri.fromFile(file),
                                "application/vnd.android.package-archive");
                        startActivityForResult(intent, 0);//获取从安装页面返回的结果

                        //删除引导页的标注
                        SPUtils.remove(MainActivity.this, "is_guide_show");
                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        ToastUtils.makeTextShow(MainActivity.this, "下载失败");
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        int percent = (int) (progress * 100);
                        //更新进度
                        dialog.setProgress(percent);

                    }
                });

    }


    private void getSavePath() {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            target = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        } else {
            //下载到手机
            target = getFilesDir().getAbsolutePath();
        }

        //判断之前有下载过没
        file = new File(target + File.separator + apkName);

        LogUtils.e(TAG, "文件路径" + target + File.separator + apkName);

        boolean isExists = file.exists();
        if (isExists) {
            //删除之前存在的
            LogUtils.e(TAG, "删除" + target);
            file.delete();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(MainActivity.this,
                        PersonalCenterActitity.class));
            }
        } else if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //删除下载好的apk
                file.delete();
                ToastUtils.makeTextShow(this, "已删除安装包!");
            }
        }


    }


}
