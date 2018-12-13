package com.mac.runtu.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.ScanResultActivity;
import com.mac.runtu.activity.otherActivity.ShoppingCartActivity;
import com.mac.runtu.activity.personcentre.LoginActivity;
import com.mac.runtu.activity.specialtyshop.SpecialtyDetailsActivity;
import com.mac.runtu.activity.specialtyshop.SpecialtyListlidingMenuActivity;
import com.mac.runtu.activity.specialtyshop.SpecialtySlidingMenuActivity;
import com.mac.runtu.activity.specialtyshop.SpecizltySearchListActivity;
import com.mac.runtu.adapter.SpecialtyAddressTeSeAdapter;
import com.mac.runtu.adapter.SpecialtyGoodAllAdapter;
import com.mac.runtu.business.AdBiz;
import com.mac.runtu.business.SpecialtyGoodGetFromNetBiz;
import com.mac.runtu.business.TopImageBannerBiz;
import com.mac.runtu.custom.LocalCharacteristicsGridView;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.AdAddressInfo;
import com.mac.runtu.javabean.SpecialtyGoodDetailInfo;
import com.mac.runtu.javabean.SpecialtyGoodInfo;
import com.mac.runtu.utils.ACache;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.ImageLoadUtils;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.PhoneNetWordUitls;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description: 特产电商主页  Fragment
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/5 0005 下午 12:04
 */
public class SpecialtyContentFragment extends Fragment implements View
        .OnClickListener {
    private static final String TAG = "SpecialtyContentFragment";
    public SpecialtySlidingMenuActivity mActivity;//可以当做Context让子类使用,
    // MainActivity


    private static final String food           = "2";
    private static final String local          = "3";
    private static final String fruitRecommend = "4";

    private static final String addressRecommend = "1";
    private static final String productRecommend = "5";

    private String middleAdUuid;

    Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
           /* Toast.makeText(SpecialtyActivity.this, "刷新完毕", Toast
           .LENGTH_SHORT).show();*/
            super.handleMessage(msg);
            swipeRefreshLayout.setRefreshing(false);
        }
    };
    @BindView(R.id.back_Iv)
    ImageView backIv;

    @BindView(R.id.shopping_cart_Tv)
    TextView          shoppintCartTv;
    @BindView(R.id.bd_Lv)
    ListView          bdLv;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;

    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.tv_content)
    TextView     tvShowLoadingContent;

    LinearLayout                 productCalssfiy;
    LinearLayout                 cerealsLL;
    LinearLayout                 bornAndBredLL;
    LinearLayout                 fuiltLL;
    TextView                     localFeaturesTv;
    LocalCharacteristicsGridView gridView;
    ImageView                    adIv;
    TextView                     productsRecommendedTv;
    Banner                       banner;
    private LinearLayout llSerachMenu;

    private ArrayList<String>                  images;
    private RelativeLayout                     rlProductsRecommended;
    private RelativeLayout                     rlLocalSpecialty;
    private ArrayList<SpecialtyGoodDetailInfo> mProductinfoList;
    private ArrayList<SpecialtyGoodDetailInfo> mAddressRecominfoList;

    private ACache    mACache; //缓存类
    private ImageView ivScan;


    //fragment创建
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (SpecialtySlidingMenuActivity) getActivity();
        //获取fragment依附的activity对象
        mACache = ACache.get(mActivity, GlobalConstants.json_cache_name);


    }

    //初始化fragment布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.activity_specialty,
                null);
        ButterKnife.bind(this, view);


        if (!PhoneNetWordUitls.isNetworkConnected(mActivity)) {

            ToastUtils.makeTextShow(mActivity, "请打开网络!");

            //读缓存


        } else {
            initView();

            initBannerData();
            initGridData();
            initListData();
            // parseData("");
            initListener();

        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //经过压力测试 筛选页的model有时会空指针  所以开始就给 侧边fragmentmodel赋值
        SpecialtyLeftFilterFragment leftMenuFragment = mActivity
                .getLeftMenuFragment();

        leftMenuFragment.setSpecialttyAndFramModel(GlobalConstants
                .value_mode_filter_specialty);
    }

    private void initView() {
        View header = mActivity.getLayoutInflater().inflate(R.layout
                .business_dynamics_header_layout, null);

        View floatView = mActivity.getLayoutInflater().inflate(R.layout
                .specialty_float_menu_layout, null);

        gridView = ButterKnife.findById(floatView, R.id.gridView);

        productCalssfiy = ButterKnife.findById(floatView, R.id
                .product_classify_LL);

        cerealsLL = ButterKnife.findById(floatView, R.id.cereals_LL);
        bornAndBredLL = ButterKnife.findById(floatView, R.id.born_and_bred_LL);
        fuiltLL = ButterKnife.findById(floatView, R.id
                .fruit_LL);


        rlLocalSpecialty = ButterKnife.findById(floatView, R.id
                .rl_local_recomment);


        rlProductsRecommended = ButterKnife.findById(floatView, R.id
                .rl_goodRecommend);

        //searchTv = ButterKnife.findById(floatView, R.id.search_Tv);
        llSerachMenu = ButterKnife.findById(floatView, R.id.ll_serach);

        //扫描
        ivScan = ButterKnife.findById(floatView, R.id.iv_scan);


        TextView tvSearch = ButterKnife.findById(floatView, R.id.search_Tv);
        tvSearch.setText(UiUtils.getString(R.string
                .specialty_search_hint));

        adIv = ButterKnife.findById(floatView, R.id.ad_Iv);

        //localFeaturesTv.setOnClickListener(this);
        rlLocalSpecialty.setOnClickListener(this);
        //购物车
        shoppintCartTv.setOnClickListener(this);
        cerealsLL.setOnClickListener(this);
        bornAndBredLL.setOnClickListener(this);
        fuiltLL.setOnClickListener(this);
        //localFeaturesTv.setOnClickListener(this);
        // productsRecommendedTv.setOnClickListener(this);
        rlProductsRecommended.setOnClickListener(this);
        productCalssfiy.setOnClickListener(this);
        backIv.setOnClickListener(this);

        banner = ButterKnife.findById(header, R.id.banner);

        bdLv.addHeaderView(header);
        bdLv.addHeaderView(floatView, null, true);

    }

    //轮播条
    private void initBannerData() {

        final TopImageBannerBiz bannerBiz = new TopImageBannerBiz
                (banner);
        bannerBiz.autoShowImage(AdBiz.getImageListId());
        //广告轮播条数据
        AdBiz.getAdInfo(GlobalConstants.AD_TYPE_SHOP, new AdBiz
                .OnAdInfoListener() {


            @Override
            public void onSuccessInfoList(ArrayList<AdAddressInfo
                    .TopImageData> infoList, ArrayList<String> imageUrlList) {


                if (imageUrlList.size() > 0) {
                    bannerBiz.autoShowImage(imageUrlList);
                }
            }

            @Override
            public void onfail() {

            }
        });


        //中部广告条图片
        AdBiz.getAdInfo1("1", "7", new AdBiz
                .OnAdInfoListener() {


            @Override
            public void onSuccessInfoList(ArrayList<AdAddressInfo
                    .TopImageData> infoList, ArrayList<String> imageUrlList) {

                if (imageUrlList.size() > 0) {

                    ImageLoadUtils.rectangleImage(imageUrlList.get(0), adIv);
                    middleAdUuid = infoList.get(0).uuid;
                }
            }

            @Override
            public void onfail() {

            }
        });

    }


    //地方特色
    private void initGridData() {

        String jsonAddressRecommend = mACache.getAsString
                (GlobalConstants.cache_key_addressRecommend);

        if (!TextUtils.isEmpty(jsonAddressRecommend)) {

            parseGridViewData(jsonAddressRecommend);
            LogUtils.e(TAG, "产品推荐读取换村");
        } else {
            SpecialtyGoodGetFromNetBiz.getSpecialtyGoodData4NetPayment(1,
                    SpecialtyContentFragment.addressRecommend, "",
                    GlobalConstants
                            .value_mode_filter_specialty, new
                            SpecialtyGoodGetFromNetBiz
                                    .OnSpecialtyDataListener() {


                                @Override
                                public void onSuccess(String result) {

                                    if (result != null) {
                                        mACache.put
                                                (GlobalConstants
                                                                .cache_key_addressRecommend,
                                                        result,
                                                        GlobalConstants
                                                                .json_cache_time);
                                        LogUtils.e(TAG, "特产电商地方特色写入缓存");
                                        parseGridViewData(result);
                                    }

                                }

                                @Override
                                public void onFail() {
                                    ToastUtils.makeTextShowNoNet(mActivity);
                                }
                            });
        }

    }

    private void parseGridViewData(String result) {
        SpecialtyGoodInfo info = GSonUtil.parseJson(result,
                SpecialtyGoodInfo.class);
        mAddressRecominfoList = info.objList;


        if (mAddressRecominfoList != null && mAddressRecominfoList.size() > 0) {
            //缓存数据


            gridView.setAdapter(new SpecialtyAddressTeSeAdapter(mActivity,
                    mAddressRecominfoList));
        } else {
            ToastUtils.makeTextShowNoData(mActivity);
        }

    }

    //产品推荐
    private void initListData() {

        llLoading.setVisibility(View.VISIBLE);
        tvShowLoadingContent.setText("玩命加载...");

        mACache.getAsString(GlobalConstants.cache_key_productRecommend);

        SpecialtyGoodGetFromNetBiz.getGoodRecommData4Net(1, GlobalConstants
                .value_mode_filter_specialty, new SpecialtyGoodGetFromNetBiz
                .OnSpecialtyDataListener() {


            @Override
            public void onSuccess(String result) {

                mACache.put(GlobalConstants.cache_key_productRecommend,
                        result, GlobalConstants
                                .json_cache_time);
                LogUtils.e(TAG, "产品推荐写入缓存");
                parseData(result);
            }

            @Override
            public void onFail() {
                llLoading.setVisibility(View.GONE);
            }
        });

    }

    private void parseData(String result) {

        SpecialtyGoodInfo info = GSonUtil.parseJson(result,
                SpecialtyGoodInfo.class);

        mProductinfoList = info.objList;

        // ArrayList<SpecialtyGoodDetailInfo>  infoList = new ArrayList<>();

        if (mProductinfoList != null && mProductinfoList.size() > 0) {

            bdLv.setAdapter(new SpecialtyGoodAllAdapter(mActivity,
                    mProductinfoList));
        } else {
            LogUtils.e(TAG, "根被就没有数据");
        }

        llLoading.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mActivity,
                SpecialtyListlidingMenuActivity.class);

        switch (view.getId()) {
            case R.id.back_Iv:
                mActivity.finish();
                break;

            case R.id.shopping_cart_Tv:
                if (UiUtils.isLogin()) {

                    //标记是特产电商
                    //MarkerConstants.value_mode_filter_4one = GlobalConstants
                    // .value_mode_filter_specialty;
                    Intent intent1 = new Intent(mActivity, ShoppingCartActivity
                            .class);
                    intent1.putExtra(GlobalConstants.key_model,
                            GlobalConstants.value_mode_filter_specialty);
                    mActivity.startActivity(intent1);
                } else {
                    startActivity(new Intent(mActivity, LoginActivity.class));
                }

                break;
            case R.id.product_classify_LL:

                //产品分类
                MarkerConstants.slidingmenu_toggle_from = "left";
                // 1 代表特产电商

                SpecialtyLeftFilterFragment leftMenuFragment = mActivity
                        .getLeftMenuFragment();

                leftMenuFragment.setSpecialttyAndFramModel(GlobalConstants
                        .value_mode_filter_specialty);

                toggle();
                break;
            case R.id.cereals_LL:
                //五谷杂粮
                MarkerConstants.specialty_Type = food;
                startActivity(intent);
                break;
            case R.id.born_and_bred_LL:
                //土生土长
                MarkerConstants.specialty_Type = local;
                startActivity(intent);
                break;
            case R.id.fruit_LL:
                //条目的时令水果
                MarkerConstants.specialty_Type = fruitRecommend;
                startActivity(intent);
                break;
            case R.id.rl_local_recomment:
                //girdView条目的地方推荐/特色
                MarkerConstants.specialty_Type = addressRecommend;
                startActivity(intent);
                break;
            case R.id.ad_Iv:

                break;
            case R.id.rl_goodRecommend:
                //产品推荐
                //MarkerConstants.specialty_Type = productRecommend;
                // startActivity(intent);
                break;

        }
    }

    private void initListener() {

        swipeRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout
                .OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //获取数据代码填这里
                        refreshHandler.sendEmptyMessage(1);
                    }
                }, 3000);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {
                //跳转详情页
                Intent intent = new Intent(mActivity, SpecialtyDetailsActivity
                        .class);

                //MarkerConstants.value_good_uuid = ;

                intent.putExtra(GlobalConstants.key_uuid, mAddressRecominfoList
                        .get(position)
                        .uuid);
                mActivity.startActivity(intent);

            }
        });

        bdLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {

                LogUtils.e(TAG, "position==" + position);

                //跳转详情页
                Intent intent = new Intent(mActivity, SpecialtyDetailsActivity
                        .class);


                SpecialtyGoodDetailInfo info =
                        mProductinfoList.get
                                (position - 2);
                if (info != null) {
                    intent.putExtra(GlobalConstants.key_uuid, info.uuid);
                    mActivity.startActivity(intent);
                }


            }
        });

        llSerachMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,
                        SpecizltySearchListActivity.class);


                intent.putExtra(GlobalConstants.key_model, GlobalConstants
                        .value_mode_filter_specialty);
                mActivity.startActivity(intent);

            }
        });

        ivScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //扫描
                LogUtils.e(TAG, "扫一扫");

                UiUtils.openScan4Fragment(SpecialtyContentFragment.this);
            }
        });


        //中部广告条
        adIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(middleAdUuid)) {
                    //跳转详情页
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse(SpecialtyContentFragment.this.middleAdUuid));
                    mActivity.startActivity(intent);
                }else {
                    ToastUtils.makeTextShow(mActivity,"暂无数据");
                }
            }
        });
    }

    //如果侧边栏开,则关;反之亦然
    protected void toggle() {
        SlidingMenu slidingMenu = mActivity.getSlidingMenu();
        slidingMenu.toggle();//如果侧边栏开,则关;反之亦然
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult
                (requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {

                ToastUtils.makeTextShow(getActivity(), "暂无扫描结果");

            } else {
                String scanResult = intentResult.getContents();
                scanResultDispose(scanResult);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 扫描结果处
     */
    private void scanResultDispose(String scanResult) {
        // ScanResult 为获取到的字符串

        //tvResult.setText(ScanResult);
        //Log.e(TAG, "ScanResult=" + ScanResult);

        if (scanResult.contains("http")) {

            Uri uri = Uri.parse(scanResult);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(it);

        } else {
            Intent intent = new Intent(mActivity, ScanResultActivity.class);
            intent.putExtra(GlobalConstants.key_info, scanResult);
            mActivity.startActivity(intent);
        }

    }
}
