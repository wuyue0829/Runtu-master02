package com.mac.runtu.activity.otherActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.mac.runtu.R;
import com.mac.runtu.business.AdBiz;
import com.mac.runtu.business.ReallyNameBiz;
import com.mac.runtu.business.TopImageBannerBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.AdAddressInfo;
import com.mac.runtu.utils.ACache;
import com.mac.runtu.utils.MD5Utils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.PhoneNetWordUitls;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;
import com.mac.runtu.view.PullToRefreshListView;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description: 客,产,乡,物,创,农,劳,父类
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/21 0021 上午 10:08
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            swipeRefreshLayout.setRefreshing(false);
        }
    };
    @BindView(R.id.back_Iv)
    public ImageView backIv;
    @BindView(R.id.release_Iv)
    public ImageView releaseIv;


    @BindView(R.id.crp_Lv)
    //public ListView crpLv;
    public PullToRefreshListView crpLv;


    @BindView(R.id.swipeRefreshLayout)
    public PullRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fl_top_cover)
    public FrameLayout flTopCover;

    @BindView(R.id.tv_top_title)
    public TextView tvTopTitle;

    public boolean                 isLoading;
    public HashMap<String, String> hashMap;
    public int                     lastVisiblePosition;
    public View                    menuLL;
    public Banner                  banner;
    public View                    floatView;
    public ACache                  mACache;
    public String                  cacheKey;
    public boolean isReadingCache = false;
    private TopImageBannerBiz mTopAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_base_activity);
        ButterKnife.bind(this);


        //换村的类
        mACache = ACache.get(UiUtils.getContext(), GlobalConstants
                .json_cache_name);


        //页面名称
        tvTopTitle.setText(getTopTitleName());

        View header = getLayoutInflater().inflate(R.layout
                .business_dynamics_header_layout, null);

        banner = ButterKnife.findById(header, R.id.banner);
        floatView = getFloatView();

        crpLv.addHeaderView(header);
        crpLv.addHeaderView(floatView, null, true);

        menuLL = getFloatView();

        flTopCover.addView(menuLL);

        initView();

        //轮播条
        initBannerData();
        //展示数据
        initData();
        //其他数据  比如地址
        initDataOther();

        //listVeiw的时间监听
        initListViewListener();

        //放回建和发布
        initTopListener();

        //其他按钮时间监听
        initListener();


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

    /**
     * 顶部标题名称
     *
     * @return
     */
    protected abstract int getTopTitleName();


    /**
     * listView布局的一部分
     *
     * @return
     */
    protected abstract View getFloatView();


    /**
     * 初始化布局
     */
    public abstract void initView();

    /**
     * 初始化轮播条数据
     */
    public void initBannerData() {
        //获得轮播条数据
        //广告轮播条数据

        //是否打开网络
        mTopAd = new TopImageBannerBiz(banner);
        showTopImages(null);

        AdBiz.getAdInfo(getAdType(), new AdBiz
                .OnAdInfoListener() {


            @Override
            public void onSuccessInfoList(ArrayList<AdAddressInfo
                    .TopImageData> infoList, ArrayList<String> imageUrlList) {

                showTopImages(imageUrlList);
            }

            @Override
            public void onfail() {
            }
        });


    }

    /**
     * 显示图片
     *
     * @param imageUrlList
     */
    private void showTopImages(ArrayList<String> imageUrlList) {
        if (imageUrlList != null && imageUrlList.size() > 0) {
            mTopAd.autoShowImage(imageUrlList);
        } else {
            mTopAd.autoShowImage(AdBiz.getImageListId());
        }
    }


    /**
     * 加载数据
     */
    public void initData() {
        //换存的key 访问网路就更新key  主要是为了客商动态页面,有个type
        cacheKey = MD5Utils.getMd5(getUrl() + getType());


        if (!PhoneNetWordUitls.isNetworkConnected(this)) {

            ToastUtils.makeTextShow(this, "请打开网络!");

        } else {

            isLoading = true;//表示正在加载中
            //网络加载数据

            hashMap = getHashMap();
            if (hashMap != null) {
                MyHttpUtils.getStringDataFromNet(getUrl(), null,
                        hashMap, new MyHttpUtils.OnNewWorkRequestListener() {


                            @Override
                            public void onNewWorkSuccess(String result) {

                                if (result != null) {
                                    parserData(result);

                                }
                                //不管是否有数据完了就得关闭
                                crpLv.onRefreshComplete();
                            }

                            @Override
                            public void onNewWorkError(String msg) {
                                ToastUtils.makeTextShowNoNet
                                        (BaseActivity.this);

                                crpLv.onRefreshComplete();

                                //加载换存

                                getData4Cache();
                            }
                        });

            }
        }

    }


    private void getData4Cache() {
        isReadingCache = true;
        String cacheJson = mACache.getAsString(cacheKey);
        if (!TextUtils.isEmpty(cacheJson) && isReadingCache) {
            parserData(cacheJson);
            isReadingCache = false;
        }
    }


    /**
     * 顶部按钮的监听
     */
    public void initTopListener() {
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        releaseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (GlobalConstants.reaNemAttestationSucc == UiUtils.getAttestationStuts()) {
                    if (getReleaseActivity() != null) {
                        startActivityForResult(new Intent(BaseActivity.this,
                                getReleaseActivity()), 0);
                    }
                }else {
                    new ReallyNameBiz(BaseActivity.this).status();
                }


            }
        });
    }



    /**
     * 轮播条数据类型
     *
     * @return
     */
    public abstract String getAdType();

    /**
     * 加载其他数据
     */
    protected abstract void initDataOther();


    /**
     * 跳转的发布页
     *
     * @return
     */
    protected abstract Class<?> getReleaseActivity();


    /**
     * 网络加载数据路径
     *
     * @return
     */
    protected abstract String getUrl();

    /**
     * 类型(客商动态)
     *
     * @return
     */
    protected String getType() {
        return "";
    }

    ;

    /**
     * 访问网络的数据
     *
     * @return
     */
    public abstract HashMap<String, String> getHashMap();


    //解析数据
    public abstract void parserData(String result);

    public void initListViewListener() {
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

                        if (!PhoneNetWordUitls.isNetworkConnected(UiUtils
                                .getContext())) {
                            return;
                        }

                        refreshData();

                        refreshHandler.sendEmptyMessage(1);
                    }
                }, 3000);
            }
        });


        crpLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int
                    scrollState) {


                if (scrollState == SCROLL_STATE_IDLE) {//ListView空闲
                    //获取当前可见的最后一个item的位置
                    lastVisiblePosition = crpLv.getLastVisiblePosition();
                    //如果isLoading=true表示正在加载,还没结束,此时不允许加载下一页

                    lvScrollAction();

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int
                    visibleItemCount, int totalItemCount) {

                if (firstVisibleItem >= 1) {
                    menuLL.setVisibility(View.GONE);

                    initView2();
                } else {
                    initView2();
                    menuLL.setVisibility(View.GONE);
                }
            }
        });

        crpLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {


                //得到信息跳转详情页
                lvOnItemClick(position);
            }
        });
    }

    public void initView2() {

    }


    /**
     * 界面刷新
     */
    public abstract void refreshData();

    /**
     * listview滑动事件
     */
    public abstract void lvScrollAction();

    /**
     * lv条目点击事件
     *
     * @param position
     */
    public abstract void lvOnItemClick(int position);

    /**
     * 设置的时间监听
     */
    public abstract void initListener();


}
