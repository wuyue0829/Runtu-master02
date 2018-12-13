package com.mac.runtu.activity.specialtyshop;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.OrderPayActivity;
import com.mac.runtu.activity.personcentre.LoginActivity;
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
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.javabean.ShopCarBusinessInfo;
import com.mac.runtu.javabean.ShoppCarlistDetailInfo;
import com.mac.runtu.javabean.SpecialtyGoodDetailInfo;
import com.mac.runtu.javabean.SpecialtyGoodInfoFromUuid;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.SPUtils;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;
import com.mac.runtu.view.FlowLayout;
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
 * 特产电商的商品详情
 */
public class SpecialtyDetailsActivity extends AppCompatActivity {
    private static final String TAG = "SpecialtyDetailsActivity";
    @BindView(R.id.back_Iv)
    ImageView    backIv;
    @BindView(R.id.add_cart_Tv)
    TextView     addCart;
    @BindView(R.id.buy_Iv)
    ImageView    buyIv;
    @BindView(R.id.banner)
    Banner       banner;
    @BindView(R.id.bottom_LL)
    LinearLayout bottomLL;
    @BindView(R.id.tv_title)
    TextView     tvTitle;
    @BindView(R.id.tv_price)
    TextView     tvPrice;
    @BindView(R.id.tv_people_get)
    TextView     tvPeopleGet;
    @BindView(R.id.ll_product_size)
    LinearLayout llProductSize;
    @BindView(R.id.tv_title1)
    TextView     tvTitle1;



    @BindView(R.id.ll_product_detail_image)
    LinearLayout llProductDetailImage;


    @BindView(R.id.ef_Iv)
    LinearLayout efIv;
    @BindView(R.id.lsc_Iv)
    LinearLayout lscIv;
    @BindView(R.id.btn_cut)
    Button       btnCut;
    @BindView(R.id.btn_add)
    Button       btnAdd;
    @BindView(R.id.et_goodnum)
    TextView     etGoodnum;

    @BindView(R.id.fl_param)
    FrameLayout flParam;
    @BindView(R.id.fl_myView)
    FlowLayout  flMyView;
    @BindView(R.id.wv)
    WebView wv;

    private String                                  uuid;
    private SpecialtyGoodDetailInfo                 data;
    private ArrayList<String>                       images;
    private ArrayList<BusinessParameterDetailInfos> mBusiParameInfos;
    private int goodNum = 1;
    private String                       paramUuid;
    private BusinessParameterDetailInfos mBusinParame;
    private float                        allPay;
    private String                       mModel;
    private ArrayList<TextView>          tvList;


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
        setContentView(R.layout.activity_specialty_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mModel = intent.getStringExtra(GlobalConstants.key_model);
        uuid = intent.getStringExtra(GlobalConstants.key_uuid);

        if (uuid != null) {
            initData();
        }
    }

    private void initData() {

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

            WebSettings webSettings = this.wv.getSettings();
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setJavaScriptEnabled(true);
            LogUtils.e("内容",data.content);
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

            //图片
            ArrayList<PictureInfo> pictureInfos = new ArrayList<>();
            for(PictureInfo pictureInfo :data.pictureInfos){
                if(pictureInfo.pictureType == 1){
                    pictureInfos.add(pictureInfo);
                }

                if(pictureInfo.pictureType == 2){
                    pictureInfos.add(pictureInfo);
                }
            }

            images = AdBiz.getImageListUrl(pictureInfos);

            initBannerData();


            tvTitle.setText("" + data.name);
            tvTitle1.setText("" + data.name);
            //tvPrice.setText("¥" + data.minPrice);
            tvPeopleGet.setText(data.payment + "人收货");

            initBusinessParame();

            etGoodnum.setText(goodNum + "");

            showAllImage();
        }

    }

    /**
     * 展示所有图片
     */
    private void showAllImage() {

        if (images!=null&&images.size()>0) {

            for (int i = 0; i < images.size(); i++) {

                final int positon = i;
                ImageView view = new ImageView(this);

                view.setBackgroundResource(R.drawable.bg_icon_transparent);
                view.setScaleType(ImageView.ScaleType.FIT_XY);

                ImageLoadUtils.rectangleImage(this, images.get(i).trim(), view);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SpecialtyDetailsActivity.this,
                                SpecialtyOneImageDetailActivity.class);
                        intent.putExtra(GlobalConstants.key_info, images);
                        intent.putExtra(GlobalConstants.key_position, positon);
                        startActivity(intent);
                    }
                });

                llProductDetailImage.addView(view);
            }
        }
    }

    private void initBusinessParame() {

        mBusiParameInfos = data.businessParameterInfos;
        //参数
        if (mBusiParameInfos.size() > 0) {
            //第一条默认被选中
            mBusinParame = mBusiParameInfos.get(0);
            mBusinParame.isSelect = true;

            tvPrice.setText("¥" + mBusinParame.paramPrice);
            //初始化参数布局
            initParam();
        }

    }

    //参数布局
    private void initParam() {

        tvList = new ArrayList<TextView>();
        tvList.clear();
        flMyView.removeAllViews();

        for (int i = 0; i < mBusiParameInfos.size(); i++) {

            View paramView = View.inflate(this, R.layout
                    .item_specialty_detail_good_size, null);

            TextView tvParmName = (TextView) paramView.findViewById(R.id
                    .tv_sizename);
            tvParmName.setText(mBusiParameInfos.get(i).paramName);

            //tv.setBackgroundColor(color);
            tvParmName.setTag(i);

            tvParmName.setOnClickListener(listener);

            tvList.add(tvParmName);

            flMyView.addView(paramView);
        }

        //将第一个默认选中
        tvList.get(0).setSelected(true);
        mBusinParame = mBusiParameInfos.get(0);
    }

    /**
     * 规格参数点击事件
     */
    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();

            mBusinParame = mBusiParameInfos.get(position);

            LogUtils.e(TAG, "参数名称=" + mBusinParame.paramName);

            tvPrice.setText("¥"+mBusinParame.paramPrice);

            upDataUi(position);
        }
    };


    private void upDataUi(int position) {
        for (int i = 0; i < tvList.size(); i++) {
            if (i == position) {
                tvList.get(i).setSelected(true);
            } else {
                tvList.get(i).setSelected(false);
            }
        }

    }

    private void initBannerData() {
        //获得轮播条数据
        //广告轮播条数据

        TopImageBannerBiz bannerBiz = new TopImageBannerBiz
                (banner);

        if (images != null && images.size() > 0) {

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
        Intent intent = new Intent(SpecialtyDetailsActivity.this,
                SpecialtyOneImageDetailActivity.class);

        intent.putExtra(GlobalConstants.key_info, images);

        startActivity(intent);
    }


    @OnClick({R.id.back_Iv, R.id.add_cart_Tv, R.id.buy_Iv, R.id
            .ll_product_size, R.id.ll_product_detail_image, R.id.btn_cut, R
            .id.btn_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;

            case R.id.ll_product_size:
                //规格
                break;
            case R.id.btn_cut:

                cutGoodCount();
                break;
            case R.id.btn_add:

                addGoodCount();
                break;
            case R.id.ll_product_detail_image:

                //详情图片
                //go2GoodDetailImages();

                break;
            case R.id.add_cart_Tv:

                //判断登录

                if (UiUtils.isLogin()) {

                    if (parameJudge())
                        return;

                    addGood2ShoppingCar();

                } else {
                    go2LoginActivity();
                }

                break;
            case R.id.buy_Iv:
                //带着参数去付款页面
                if (UiUtils.isLogin()) {

                    if (parameJudge())
                        return;

                    getPayParam();
                } else {
                    go2LoginActivity();
                }

                break;
        }
    }
    //登录页面
    private void go2LoginActivity() {
        startActivity(new Intent(SpecialtyDetailsActivity.this,
                LoginActivity.class));
    }

    //参数判断
    private boolean parameJudge() {
        if (mBusinParame == null) {
            ToastUtils.makeTextShow(SpecialtyDetailsActivity.this,
                    "请选择规格参数");
            return true;
        }
        return false;
    }

    //详情图
    private void go2GoodDetailImages() {

        if (images != null && images.size() > 0) {
            Intent intent = new Intent(SpecialtyDetailsActivity.this,
                    SpecialtyDetailImagesActivity.class);
            intent.putExtra(GlobalConstants.key_info, images);
            startActivity(intent);
        }
    }

    //增加树量
    private void addGoodCount() {
        if (parameJudge())
            return;

        if (goodNum < mBusinParame.stock) {
            goodNum++;
            etGoodnum.setText(goodNum + "");
        }
    }
    //减少数量
    private void cutGoodCount() {
        if (goodNum > 1) {
            goodNum--;
            etGoodnum.setText(goodNum + "");
        }
    }

    /**
     * 组装参数
     */
    private void getPayParam() {
        //将选中的参数封装对象
        //为了 节省 付款页面的逻辑  在详情页 直接将 选中要付款的数据 封装成 购物车的对象 在付款页面走一样的逻辑
        ShoppCarlistDetailInfo mShoppCarInfo = new
                ShoppCarlistDetailInfo();

        //商品总金额
        mShoppCarInfo.allPay = getGoodCount() * mBusinParame
                .paramPrice;
        //商品uuid
        mShoppCarInfo.busiUuid = data.uuid;

        //商品详情参数
        mShoppCarInfo.businessInfo = getGoodDetailParam();


        mShoppCarInfo.buyCount = getGoodCount();

        //参数对象一样 直接将选择的对象 赋予它
        mShoppCarInfo.paramInfo = mBusinParame;
        mShoppCarInfo.paramUuid = mBusinParame.uuid;
        mShoppCarInfo.pictureInfos = data.pictureInfos;
        mShoppCarInfo.userUuid = UiUtils.getUserID();
        //这条数据不是来自购物车 所以没有对弈地uuid
        mShoppCarInfo.uuid = "";


        ArrayList<ShoppCarlistDetailInfo> mShopInfoList
                = new ArrayList<>();
        mShopInfoList.add(mShoppCarInfo);

        //标记是特产电商
        go2Pay(mShopInfoList);
    }

    @NonNull
    private ShopCarBusinessInfo getGoodDetailParam() {

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

    /***
     * 得到商品数量
     * @return
     */
    private Integer getGoodCount() {
        return Integer.valueOf(etGoodnum.getText()
                .toString().trim());
    }

    /**
     * 跳转付款页面
     * @param mShopInfoList
     */
    private void go2Pay(ArrayList<ShoppCarlistDetailInfo> mShopInfoList) {

        Intent intent = new Intent(SpecialtyDetailsActivity.this,
                OrderPayActivity.class);
        intent.putExtra(GlobalConstants.key_shop_car_info,
                mShopInfoList);
        intent.putExtra(GlobalConstants.key_model, GlobalConstants
                .value_mode_filter_specialty);
        startActivity(intent);

        //告诉支付完成时刷新数据 要不要刷新 从订单去要刷新从详情去不要刷新
        SPUtils.setBoolean(this, GlobalConstants.sp_pay_from, false);

    }

    /**
     * 添加商品到购物车
     */
    private void addGood2ShoppingCar() {

        AddShoppingCarInfo info = new AddShoppingCarInfo();

        info.userUuid = UiUtils.getUserID();
        info.buyCount = goodNum;
        info.busiUuid = data.uuid;
        info.paramUuid = mBusinParame.uuid;
        info.model = GlobalConstants.value_mode_filter_specialty;

        goodNum = getGoodCount();
        //总价
        allPay = goodNum * mBusinParame.paramPrice;

        info.allPay = allPay;

        String infoJson = GSonUtil.obj2json(info);

        upLoadData2Net(infoJson);

    }

    /**
     * 提交数据到网络
     * @param infoJson
     */
    private void upLoadData2Net(String infoJson) {
        AddShopCar2NetBiz.addData2ShopCar(GlobalConstants
                .value_mode_filter_specialty, infoJson, new
                OnDataBooleanListener() {


                    @Override
                    public void onOk(Boolean result) {

                        //将model传过去
                        MyShowDialog.showDialogGoShopCar
                                (SpecialtyDetailsActivity.this,
                                        GlobalConstants
                                                .value_mode_filter_specialty);
                    }

                    @Override
                    public void onFail() {
                        ToastUtils.makeTextShow(SpecialtyDetailsActivity
                                        .this,
                                "添加失败!");
                    }

                    @Override
                    public void onNetWorkFail() {
                        ToastUtils.makeTextShowNoNet(SpecialtyDetailsActivity
                                .this);
                    }
                });
    }
}
