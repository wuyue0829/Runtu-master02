package com.mac.runtu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mac.runtu.R;
import com.mac.runtu.activity.experiencefarm.AdoptExplainActivity;
import com.mac.runtu.activity.experiencefarm.ExperienceFarmAdoptDetails;
import com.mac.runtu.activity.experiencefarm.ExperienceFarmListSlidingMenuActivity;
import com.mac.runtu.activity.experiencefarm.ExperienceFarmSlidingMenuActivity;
import com.mac.runtu.activity.otherActivity.ShoppingCartActivity;
import com.mac.runtu.activity.personcentre.LoginActivity;
import com.mac.runtu.activity.personcentre.OrderActivity;
import com.mac.runtu.adapter.ExperFarmHomeAdapter;
import com.mac.runtu.business.AdBiz;
import com.mac.runtu.business.SpecialtyGoodGetFromNetBiz;
import com.mac.runtu.business.TopImageBannerBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.AdAddressInfo;
import com.mac.runtu.javabean.SpecialtyGoodDetailInfo;
import com.mac.runtu.javabean.SpecialtyGoodInfo;
import com.mac.runtu.utils.ACache;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.PhoneNetWordUitls;
import com.mac.runtu.utils.ToastUtils;
import com.mac.runtu.utils.UiUtils;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:体验农场主页面
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/12 0012 下午 4:02
 */
public class ExperienceFarmContentFragment extends Fragment implements View
        .OnClickListener {

    private static final String TAG = "ExperienceFarmContentFragment";

    private ExperienceFarmSlidingMenuActivity mActivity;

    Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            swipeRefreshLayout.setRefreshing(false);
        }
    };
    @BindView(R.id.back_Iv)
    ImageView         backIv;
    @BindView(R.id.crp_Lv)
    ListView          crpLv;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;
    @BindView(R.id.menu_LL)
    LinearLayout      menuLL;
    Banner banner;
    @BindView(R.id.shopping_cart_Tv)
    TextView       shopping_cart_Tv;
    @BindView(R.id.title_RL)
    RelativeLayout titleRL;

    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.tv_content)
    TextView     tvShowLoadingContent;

    LinearLayout adopt_type_LL;
    LinearLayout adopt_recommend_LL;
    LinearLayout adopt_explain_LL;
    LinearLayout my_adopt_LL;
    private ArrayList<String> images;

    private int pageNum = 1;
    private int     num;
    private boolean isLoading;
    private boolean isRefresh = false;
    private ExperFarmHomeAdapter mAdapter;
    private ArrayList<SpecialtyGoodDetailInfo> mAllList = new
            ArrayList<>();
    private ACache mACache;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (ExperienceFarmSlidingMenuActivity) getActivity();

        mACache = ACache.get(mActivity, GlobalConstants.json_cache_name);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(mActivity, R.layout
                .activity_experience_farm, null);

        ButterKnife.bind(this, view);


        initView();


        initListener();
        if (!PhoneNetWordUitls.isNetworkConnected(mActivity)) {
            ToastUtils.makeTextShow(mActivity, "请打开网络!");
            //getData4Cache();
            //menuLL.set

        } else {

            initBannerData();
            initData();

        }
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity.getLeftMenuFragment().setSpecialttyAndFramModel
                (GlobalConstants.value_mode_expm_farm);
    }

    private void initView() {
        View header = mActivity.getLayoutInflater().inflate(R.layout
                .business_dynamics_header_layout, null);
        View floatView = mActivity.getLayoutInflater().inflate(R.layout
                .experience_farm_float_menu_layout, null);
        banner = ButterKnife.findById(header, R.id.banner);


        crpLv.addHeaderView(header);
        adopt_type_LL = ButterKnife.findById(floatView, R.id.adopt_type_LL);
        adopt_recommend_LL = ButterKnife.findById(floatView, R.id
                .adopt_recommend_LL);
        adopt_explain_LL = ButterKnife.findById(floatView, R.id
                .adopt_explain_LL);
        my_adopt_LL = ButterKnife.findById(floatView, R.id.my_adopt_LL);
        adopt_type_LL.setOnClickListener(this);
        adopt_recommend_LL.setOnClickListener(this);
        adopt_explain_LL.setOnClickListener(this);
        my_adopt_LL.setOnClickListener(this);
        crpLv.addHeaderView(floatView, null, true);
    }

    public void initBannerData() {

        final TopImageBannerBiz bannerBiz = new TopImageBannerBiz
                (banner);
        bannerBiz.autoShowImage(AdBiz.getImageListId());
        //广告轮播条数据
        AdBiz.getAdInfo(GlobalConstants.AD_TYPE_exper_farm, new AdBiz
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


    }

    public void initData() {

        if (!isRefresh) {
            llLoading.setVisibility(View.VISIBLE);
            tvShowLoadingContent.setText("玩命加载...");
        }

        isLoading = true;
        //crpLv.setAdapter(new ExperFarmHomeAdapter(mActivity, datas));

        //参数一  pageNum  参数二区分哪个标签  参数三  排序 , 参数四 区分特产 农场
        SpecialtyGoodGetFromNetBiz.getSpecialtyGoodData4NetPayment(pageNum,
                "", "", GlobalConstants.value_mode_expm_farm, new
                        SpecialtyGoodGetFromNetBiz.OnSpecialtyDataListener() {


                            @Override
                            public void onSuccess(String result) {


                                parseData(result);


                            }

                            @Override
                            public void onFail() {
                                llLoading.setVisibility(View.GONE);
                                ToastUtils.makeTextShowNoNet(mActivity);

                                //访问网路失败 换存

                                //getData4Cache();

                            }
                        });


    }


    private void parseData(String result) {
        SpecialtyGoodInfo info = GSonUtil.parseJson(result,
                SpecialtyGoodInfo.class);

        num = info.num;

        LogUtils.e(TAG, "主页的num==" + num);
        LogUtils.e(TAG, "主页的num==" + num);

        ArrayList<SpecialtyGoodDetailInfo> infoList = info.objList;

        if (infoList != null && infoList.size() > 0) {

            if (isRefresh) {
                mAllList.clear();
                isRefresh = false;
            }

            mAllList.addAll(infoList);

            //LogUtils.e(TAG, "property" + mAllList.toString());
            if (mAdapter == null) {
                mAdapter = new ExperFarmHomeAdapter(mActivity, mAllList);
                crpLv.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }

            pageNum++;
            //pageSize += 10;
            isLoading = false;

            //写入换存
           // mACache.put(TAG, result);

        } else {
            ToastUtils.makeTextShowNoData(mActivity);
        }

        llLoading.setVisibility(View.GONE);
    }


    @OnClick({R.id.back_Iv, R.id.shopping_cart_Tv, R.id.adopt_type_LL, R.id
            .adopt_recommend_LL, R.id.adopt_explain_LL, R.id.my_adopt_LL})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                mActivity.finish();
                break;
            case R.id.shopping_cart_Tv:

                if (UiUtils.isLogin()) {

                    //标记是体验农场
                    //MarkerConstants.value_mode_filter_4one = GlobalConstants
                    //.value_mode_filter_farm;
                    Intent intent = new Intent
                            (mActivity,
                                    ShoppingCartActivity.class);

                    intent.putExtra(GlobalConstants.key_model,
                            GlobalConstants.value_mode_filter_farm);
                    mActivity.startActivity(intent);
                } else {
                    startActivity(new Intent
                            (mActivity,
                                    LoginActivity.class));
                }

                break;
            case R.id.adopt_type_LL:

                //分类 告诉侧边栏 来自左边
                MarkerConstants.slidingmenu_toggle_from = "left";
                //领养分类告诉侧边栏你来自哪里

                mActivity.getLeftMenuFragment().setSpecialttyAndFramModel
                        (GlobalConstants.value_mode_expm_farm);
                //打开开关
                toggle();

                break;
            case R.id.adopt_recommend_LL:
                //领养推荐

                startActivity(new Intent(mActivity,
                        ExperienceFarmListSlidingMenuActivity.class));

                break;
            case R.id.adopt_explain_LL:
                startActivity(new Intent(mActivity,
                        AdoptExplainActivity.class));
                break;
            case R.id.my_adopt_LL:
                //我的领养

                if (UiUtils.isLogin()) {

                    MarkerConstants.value_mode_filter_4one = GlobalConstants
                            .value_mode_filter_farm;

                    Intent intent = new Intent(mActivity,
                            OrderActivity.class);

                    intent.putExtra(GlobalConstants.key_model,
                            GlobalConstants.value_mode_filter_farm);
                    mActivity.startActivity(intent);
                } else {
                    startActivity(new Intent(mActivity, LoginActivity.class));
                }

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
                        if (!PhoneNetWordUitls.isNetworkConnected(UiUtils
                                .getContext())) {
                            return;
                        }
                        if (!isLoading) {
                            pageNum = 1;
                            isRefresh = true;
                            initData();
                        }
                        refreshHandler.sendEmptyMessage(1);
                    }
                }, 3000);
            }
        });

        crpLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int
                    scrollState) {

                if (scrollState == SCROLL_STATE_IDLE && PhoneNetWordUitls
                        .isNetworkConnected(UiUtils.getContext())) {//ListView空闲
                    //获取当前可见的最后一个item的位置
                    int lastVisiblePosition = crpLv.getLastVisiblePosition();

                    //如果isLoading=true表示正在加载,还没结束,此时不允许加载下一页
                    if (lastVisiblePosition == mAllList.size() - 1 + 2 &&
                            !isLoading && mAllList.size() < num) {
                        //加载数据
                        initData();
                    }

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int
                    visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 1) {
                    menuLL.setVisibility(View.VISIBLE);
                } else {
                    menuLL.setVisibility(View.GONE);
                }
            }
        });

        crpLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {
                //详情页

                LogUtils.e(TAG, "体验农场点击的位置!" + position);
                if (position > 1) {
                    //产品的uuid
                    int listPosition = position - 2;

                    Intent intent = new Intent(mActivity,
                            ExperienceFarmAdoptDetails.class);
                    intent.putExtra(GlobalConstants.key_model,
                            GlobalConstants.value_mode_filter_farm);
                    intent.putExtra(GlobalConstants.key_uuid, mAllList
                            .get(listPosition).uuid);
                    mActivity.startActivity(intent);
                }

            }
        });

    }

    //如果侧边栏开,则关;反之亦然
    protected void toggle() {
        SlidingMenu slidingMenu = mActivity.getSlidingMenu();
        slidingMenu.toggle();//如果侧边栏开,则关;反之亦然
    }

}
