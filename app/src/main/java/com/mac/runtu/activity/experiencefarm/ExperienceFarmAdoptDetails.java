package com.mac.runtu.activity.experiencefarm;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.OrderPayActivity;
import com.mac.runtu.activity.personcentre.LoginActivity;
import com.mac.runtu.activity.specialtyshop.SpecialtyOneImageDetailActivity;
import com.mac.runtu.business.AdBiz;
import com.mac.runtu.business.AddShopCar2NetBiz;
import com.mac.runtu.business.GetSpecAndFarmGoodDetail4NetBiz;
import com.mac.runtu.business.MyShowDialog;
import com.mac.runtu.business.TopImageBannerBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnDataBooleanListener;
import com.mac.runtu.interfaceif.OnGetData4NetListener;
import com.mac.runtu.javabean.AddShoppingCarInfo;
import com.mac.runtu.javabean.BusinessParameterDetailInfos;
import com.mac.runtu.javabean.BusinessTypeInfo;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.javabean.ShopCarBusinessInfo;
import com.mac.runtu.javabean.ShoppCarlistDetailInfo;
import com.mac.runtu.javabean.SpecialtyGoodDetailInfo;
import com.mac.runtu.javabean.SpecialtyGoodInfoFromUuid;
import com.mac.runtu.utils.Arith;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.SPUtils;
import com.mac.runtu.utils.TimeUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;
import com.mac.runtu.view.MyNumSelectDialog;
import com.youth.banner.Banner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 体验农场体验详情页
 */
public class ExperienceFarmAdoptDetails extends AppCompatActivity {

    private static final String TAG = "ExperienceFarmAdoptDetails";

    private static final int downAddCar   = 0;
    private static final int downBuy      = 1;
    private static       int mDownPositon = downAddCar;

    @BindView(R.id.back_Iv)
    ImageView      backIv;
    @BindView(R.id.title_LL)
    RelativeLayout titleLL;
    @BindView(R.id.banner)
    Banner         banner;
    @BindView(R.id.tv_price1)
    TextView       tvPrice1;
    @BindView(R.id.tv_tilte)
    TextView       tvTilte;
    @BindView(R.id.tv_hitConunt)
    TextView       tvHitConunt;
    @BindView(R.id.tv_creattime)
    TextView       tvCreattime;
    @BindView(R.id.tv_price2)
    TextView       tvPrice2;
    @BindView(R.id.ses_Iv)
    LinearLayout   sesIv;
    @BindView(R.id.tv_address)
    TextView       tvAddress;
    @BindView(R.id.tv_type)
    TextView       tvType;
    @BindView(R.id.tv_lingyangWay)
    TextView       tvLingyangWay;
    @BindView(R.id.tv_lingyangTime)
    TextView       tvLingyangTime;
    @BindView(R.id.tv_lingyang_Authority)
    TextView       tvLingyangAuthority;
    @BindView(R.id.ef_Iv)
    LinearLayout   efIv;
    @BindView(R.id.tv_des)
    TextView       tvDes;
    @BindView(R.id.add_cart_Tv)
    TextView       addCartTv;
    @BindView(R.id.buy_Iv)
    ImageView      buyIv;
    @BindView(R.id.wv)
    WebView wv;
    @BindView(R.id.sv)
    ScrollView scrollView;



    //产品的uuid
    private String                                  uuid;
    private SpecialtyGoodDetailInfo                 data;
    private ArrayList<String>                       images;
    private ArrayList<BusinessParameterDetailInfos> businessParameterInfos;
    private int goodNum = 1;
    private String                       paramUuid;
    private BusinessParameterDetailInfos mBusinParame;
    private float                        allPay;
    private int                          buyNum;
    private BusinessParameterDetailInfos mBusParame;
    private String                       mModel;
    private TopImageBannerBiz            bannerBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_farm_adopt_details);
        ButterKnife.bind(this);

        //标记是体验农场
        Intent intent = getIntent();
        mModel = intent.getStringExtra(GlobalConstants.key_model);
        uuid = intent.getStringExtra(GlobalConstants.key_uuid);

        LogUtils.e(TAG,"体验农场UUID="+uuid);

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
        if (!TextUtils.isEmpty(uuid)) {
            //访问网路
            GetSpecAndFarmGoodDetail4NetBiz.getData4Net(uuid, new
                    OnGetData4NetListener() {


                        @Override
                        public void onSuccess(String result) {
                            parseData(result);
                        }

                        @Override
                        public void onFail() {

                        }
                    });
        }
    }

    public static String getNewContent(String paramString)
    {
        try
        {
            Object localObject = Jsoup.parse(paramString);
            Iterator localIterator = ((Document)localObject).getElementsByTag("img").iterator();
            while (localIterator.hasNext()) {
                ((Element)localIterator.next()).attr("width", "100%").attr("height", "auto");
            }
            String str = (localObject).toString();
            return str;
        }
        catch (Exception localException) {}
        return paramString;
    }


    //解析数据
    private void parseData(String result) {
        SpecialtyGoodInfoFromUuid info = GSonUtil
                .parseJson(result, SpecialtyGoodInfoFromUuid.class);
        data = info.objList;


        if (data != null) {

            //图片
            ArrayList<PictureInfo> pictureInfos = new ArrayList<>();
            for(PictureInfo pictureInfo:data.pictureInfos){
                if(pictureInfo.pictureType == 1){
                    pictureInfos.add(pictureInfo);
                }
                if(pictureInfo.pictureType == 2){
                    pictureInfos.add(pictureInfo);
                }
            }
            images = AdBiz.getImageListUrl(pictureInfos);
            initBannerData();

            tvTilte.setText("" + data.name);
            tvPrice1.setText("¥" + data.minPrice + "元");
            tvPrice2.setText("¥" + data.minPrice);
            tvHitConunt.setText(data.hitCount + "次");
            tvCreattime.setText(TimeUtils.setCreatTime(data.createTime));

            WebSettings webSettings = this.wv.getSettings();
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setJavaScriptEnabled(true);
            this.wv.loadDataWithBaseURL("about:blank", getNewContent(data.content), "text/html", "utf-8", null);
            this.wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            this.wv.setWebViewClient(new WebViewClient()
            {
                public void onPageFinished(WebView paramAnonymousWebView, String paramAnonymousString)
                {
                    super.onPageFinished(paramAnonymousWebView, paramAnonymousString);
                    paramAnonymousWebView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                }
            });

            //地址
            StringBuilder builder = new StringBuilder();
            builder.append(TextUtils.isEmpty(data.province) ? "" : data.province
                    + "/");
            builder.append(TextUtils.isEmpty(data.city) ? "" : data.city + "/");
            builder.append(TextUtils.isEmpty(data.county) ? "" : data.county);
            tvAddress.setText(builder.toString());

            //领养类型

            tvType.setText(data.category);


            tvLingyangTime.setText(data.cycle);
        }

    }

    private void initBannerData() {
        //获得轮播条数据
        //广告轮播条数据
        bannerBiz = new TopImageBannerBiz
                (banner);

        if (images.size() > 0) {
            bannerBiz.autoShowImage(images, new TopImageBannerBiz
                    .OnBannerClickeListener() {
                @Override
                public void onBannerClickeListener(int paramAnonymousInt) {
                    go2ImageDetail();

                }
            });
        } else {
            bannerBiz.autoShowImage(AdBiz.getImageListId());
        }


    }

    private void go2ImageDetail() {
        //图片详情查看
        Intent intent = new Intent(ExperienceFarmAdoptDetails.this,
                SpecialtyOneImageDetailActivity.class);

        intent.putExtra(GlobalConstants.key_info, images);
        startActivity(intent);
    }


    @OnClick({R.id.back_Iv, R.id.add_cart_Tv, R.id.buy_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.add_cart_Tv:


                mDownPositon = downAddCar;
                //判断登录
                if (UiUtils.isLogin()) {

                    showDialogSelectNum();
                } else {
                    go2LoginActivity();
                }

                break;
            case R.id.buy_Iv:
                //标记是体验农场
                mDownPositon = downBuy;

                //判断登录
                if (UiUtils.isLogin()) {

                    showDialogSelectNum();

                    //告诉支付完成时刷新数据 要不要刷新 从订单去要刷新 从详情去不要刷新
                    SPUtils.setBoolean(this, GlobalConstants.sp_pay_from,
                            false);
                } else {
                    go2LoginActivity();
                }


                break;
        }
    }

    private void go2LoginActivity() {
        startActivity(new Intent(this,
                LoginActivity.class));
    }

    private void showDialogSelectNum() {

        final MyNumSelectDialog dialog = new MyNumSelectDialog(this);

        dialog.setIvCloseOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.setBtnOkOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buyNum = dialog.getTextCount();

                dialog.dismiss();
                go2Next();
            }
        });

        dialog.show();

    }

    private void go2Next() {

        if (data == null) {
            return;
        }
        if (mDownPositon == downAddCar) {
            addGood2ShopCar();
        } else {
            go2Buy();
        }
    }


    private void addGood2ShopCar() {

        //算总价
        allPay = Arith.getFloat(buyNum * data.minPrice);

        AddShoppingCarInfo info = new AddShoppingCarInfo();

        info.userUuid = UiUtils.getUserID();
        //数量
        info.buyCount = buyNum;
        info.busiUuid = data.uuid;

        //选择的规格参数  (默认第一条)
        if (data.businessParameterInfos.size() <= 0) {
            return;
        }

        info.paramUuid = data.businessParameterInfos.get(0).uuid;
        //标记体验农场
        info.model = GlobalConstants.value_mode_expm_farm;

        info.allPay = allPay;

        String infoJson = GSonUtil.obj2json(info);

        AddShopCar2NetBiz.addData2ShopCar(GlobalConstants
                .value_mode_filter_specialty, infoJson, new
                OnDataBooleanListener() {


                    @Override
                    public void onOk(Boolean result) {
                        MyShowDialog.showDialogGoShopCar
                                (ExperienceFarmAdoptDetails.this,
                                        GlobalConstants.value_mode_expm_farm);
                    }

                    @Override
                    public void onFail() {
                        ToastUtils.makeTextShow(ExperienceFarmAdoptDetails.this,
                                "添加失败!");
                    }

                    @Override
                    public void onNetWorkFail() {
                        ToastUtils.makeTextShowNoNet
                                (ExperienceFarmAdoptDetails.this);
                    }
                });


    }

    private void go2Buy() {

        //算总价
        if (data.minPrice == 0 || data == null) {

            return;
        }
        allPay = Arith.getFloat(buyNum * data.minPrice);

        //将选中的参数封装对象
        //为了 节省 付款页面的逻辑  在详情页 直接将 选中要付款的数据 封装成 购物车的对象 在付款页面走一样的逻辑
        ShoppCarlistDetailInfo mShoppCarInfo = new
                ShoppCarlistDetailInfo();

        //商品总金额
        mShoppCarInfo.allPay = allPay;
        //商品uuid
        mShoppCarInfo.busiUuid = data.uuid;

        //商品详情参数
        mShoppCarInfo.businessInfo = getGoodDetailParame();

        mShoppCarInfo.buyCount = buyNum;


        //参数对象一样 直接将选择的对象 赋予它  (默认第一条)
        if (data.businessParameterInfos.size() <= 0) {
            return;
        }

        mShoppCarInfo.paramInfo = data.businessParameterInfos.get(0);
        mShoppCarInfo.paramUuid = data.businessParameterInfos.get(0).uuid;

        mShoppCarInfo.pictureInfos = data.pictureInfos;
        mShoppCarInfo.userUuid = UiUtils.getUserID();

        //这条数据不是来自购物车 所以没有对弈地uuid
        mShoppCarInfo.uuid = "";

        ArrayList<ShoppCarlistDetailInfo> mShopInfoList
                = new ArrayList<>();
        mShopInfoList.add(mShoppCarInfo);


        go2PayPage(mShopInfoList);

    }

    @NonNull
    private ShopCarBusinessInfo getGoodDetailParame() {
        ShopCarBusinessInfo goodDetailInfo
                = new ShopCarBusinessInfo();

        goodDetailInfo.content = data.content;
        goodDetailInfo.createTime = data.createTime;
        goodDetailInfo.district = data.district;

        goodDetailInfo.minPrice = data.minPrice;

        goodDetailInfo.name = data.name;

        goodDetailInfo.stock = data.stock;

        goodDetailInfo.uuid = data.uuid;
        return goodDetailInfo;
    }

    private void go2PayPage(ArrayList<ShoppCarlistDetailInfo> mShopInfoList) {
        Intent intent = new Intent(ExperienceFarmAdoptDetails.this,
                OrderPayActivity.class);

        //和购物车一样
        intent.putExtra(GlobalConstants.key_shop_car_info,
                mShopInfoList);
        intent.putExtra(GlobalConstants.key_model, GlobalConstants
                .value_mode_filter_farm);
        startActivity(intent);
    }
}
