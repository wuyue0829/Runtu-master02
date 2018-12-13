package com.mac.runtu.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mac.runtu.R;
import com.mac.runtu.activity.experiencefarm.ExperienceFarmAdoptDetails;
import com.mac.runtu.activity.experiencefarm.ExperienceFarmListSlidingMenuActivity;
import com.mac.runtu.adapter.ExperFarmHomeAdapter;
import com.mac.runtu.business.SpecialtyGoodGetFromNetBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.SpecialtyGoodDetailInfo;
import com.mac.runtu.javabean.SpecialtyGoodInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.view.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:体验农场领养推荐
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/12 0012 下午 7:19
 */
public class ExperienceFarmListFragment extends Fragment {

    private static final String TAG = "ExperienceFarmListFragment";


    /*Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            swipeRefreshLayout.setRefreshing(false);
        }
    };*/
    @BindView(R.id.back_Iv)
    ImageView         backIv;
    @BindView(R.id.crp_Lv)
    //ListView          crpLv;
    PullToRefreshListView crpLv;

    //@BindView(R.id.swipeRefreshLayout)
   // PullRefreshLayout swipeRefreshLayout;

    @BindView(R.id.tv_btnSales)
    TextView          tvBtnSales;
    @BindView(R.id.tv_btnPrice)
    TextView          tvBtnPrice;
    @BindView(R.id.tv_btnPopularity)
    TextView          tvBtnPopularity;
    @BindView(R.id.tv_btnScreening)
    TextView          tvBtnScreening;
    @BindView(R.id.screen_LL)
    LinearLayout      screenLL;


    private static String value_payment  = "desc";
    private static String value_minPrice = "desc";
    private static String value_hitCount = "desc";

    private static final int mPayMent     = 0;
    private static final int mMinPrice    = 1;
    private static final int mHitCount    = 2;
    private static       int mTabPosition = mPayMent;

    private boolean isSalseUp = false;   //默认是销量排序
    private boolean isPriceUp = true;
    private boolean isPopUp   = true;
    private Drawable imageDown;
    private Drawable imageUp;

    private int num;
    private int pageNum = 1;

    private boolean isLoading;
    private boolean isRefresh;
    private boolean isOrder; //点击排序


    private ExperienceFarmListSlidingMenuActivity mActivity;
    private ArrayList<SpecialtyGoodDetailInfo> mAllList = new ArrayList<>();
    private ExperFarmHomeAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (ExperienceFarmListSlidingMenuActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(mActivity, R.layout
                .activity_experience_farm_sub, null);

        ButterKnife.bind(this, view);


        initView();

        //出事化箭头
        upDataUIDown(tvBtnSales, tvBtnPrice, tvBtnPopularity);

        initData();
        initListener();

        return view;
    }

    private void initView() {
        //加载箭头图片
        imageUp = getResources().getDrawable(R.drawable
                .icon_arrow_up);

        imageDown = getResources().getDrawable(R.drawable
                .setting_down_arrow);

        imageUp.setBounds(0, 0, imageUp.getMinimumWidth(), imageUp
                .getMinimumHeight());
        imageDown.setBounds(0, 0, imageDown.getMinimumWidth(), imageDown
                .getMinimumHeight());


    }

    void initData() {

        isLoading = true;

        //地方推荐
        switch (mTabPosition) {
            case mPayMent:
                getData4Payment();
                break;
            case mMinPrice:
                getData4MinPrice();
                break;
            case mHitCount:
                getData4HitCount();
                break;

        }

        //crpLv.setAdapter(new ExperFarmHomeAdapter(mActivity, datas));

    }

    //销量
    private void getData4Payment() {
        SpecialtyGoodGetFromNetBiz.getSpecialtyGoodData4NetPayment(pageNum,
                "", value_payment, GlobalConstants
                        .value_mode_expm_farm, new
                        SpecialtyGoodGetFromNetBiz
                                .OnSpecialtyDataListener() {


                            @Override
                            public void onSuccess(String result) {

                                parseData(result);
                                crpLv.onRefreshComplete();
                            }

                            @Override
                            public void onFail() {
                               crpLv.onRefreshComplete();
                            }
                        });
    }

    //价格
    private void getData4MinPrice() {
        SpecialtyGoodGetFromNetBiz.getSpecialtyGoodData4NetMinPrice(pageNum,
                "", value_minPrice, GlobalConstants
                        .value_mode_expm_farm, new
                        SpecialtyGoodGetFromNetBiz
                                .OnSpecialtyDataListener() {


                            @Override
                            public void onSuccess(String result) {

                                parseData(result);
                                crpLv.onRefreshComplete();
                            }

                            @Override
                            public void onFail() {
                                crpLv.onRefreshComplete();
                            }
                        });
    }

    //人气
    private void getData4HitCount() {
        SpecialtyGoodGetFromNetBiz.getSpecialtyGoodData4NetHitCount(pageNum,
                "", value_hitCount, GlobalConstants
                        .value_mode_expm_farm, new
                        SpecialtyGoodGetFromNetBiz
                                .OnSpecialtyDataListener() {


                            @Override
                            public void onSuccess(String result) {

                                parseData(result);
                                crpLv.onRefreshComplete();
                            }

                            @Override
                            public void onFail() {
                                crpLv.onRefreshComplete();
                            }
                        });
    }

    private void parseData(String result) {

        SpecialtyGoodInfo info = GSonUtil.parseJson(result,
                SpecialtyGoodInfo.class);

        num = info.num;

        LogUtils.e(TAG, "体验农场推荐num=" + num);

        ArrayList<SpecialtyGoodDetailInfo> infoList = info.objList;

        if (infoList != null && infoList.size() > 0) {

            //刷新
            if (isRefresh) {
                mAllList.clear();
                isRefresh = false;
            }

            //点击了排序
            if (isOrder) {
                mAllList.clear();
                isOrder = false;
            }

            mAllList.addAll(infoList);

            if (mAdapter == null) {
                mAdapter = new ExperFarmHomeAdapter(mActivity, mAllList);
                crpLv.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }

            pageNum++;
            //pageSize += 10;
            isLoading = false;


            LogUtils.e(TAG, "排序按钮被点击==获得数据");
        }
    }


    //排序的按钮
    @OnClick({R.id.back_Iv, R.id.tv_btnSales, R.id.tv_btnPrice, R.id
            .tv_btnPopularity, R.id.tv_btnScreening})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                mActivity.finish();
                break;
            case R.id.tv_btnSales:
                //销量排序
                pageNum = 1;
                mTabPosition = mPayMent;
                upDataSale();
                getData4Payment();
                break;
            case R.id.tv_btnPrice:
                //价格排序
                pageNum = 1;
                mTabPosition = mMinPrice;
                upDataPrice();
                getData4MinPrice();
                break;
            case R.id.tv_btnPopularity:
                //人气排序
                pageNum = 1;
                mTabPosition = mHitCount;

                upDataPeople();
                getData4HitCount();
                break;
            case R.id.tv_btnScreening:
                //标记是从右侧打开的侧边栏
                MarkerConstants.slidingmenu_toggle_from = "right";
                mActivity.getLeftMenuFragment().setSpecialttyAndFramModel
                        (GlobalConstants.value_mode_expm_farm);
                toggle();
                break;
        }
    }


    private void initListener() {
       /* swipeRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout
                .OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //获取数据代码填这里
                        refreshHandler.sendEmptyMessage(1);
                    }
                }, 3000);
            }
        });*/

        crpLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {
                //跳转详情页

                MarkerConstants.value_good_uuid = mAllList.get
                        (position)
                        .uuid;

                mActivity.startActivity(new Intent(mActivity,
                        ExperienceFarmAdoptDetails.class));
            }
        });

        crpLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int
                    scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {//ListView空闲
                    //获取当前可见的最后一个item的位置
                    int lastVisiblePosition = crpLv.getLastVisiblePosition();

                    //如果isLoading=true表示正在加载,还没结束,此时不允许加载下一页
                    if (lastVisiblePosition == mAllList.size() &&
                            !isLoading && mAllList.size() < num) {
                        //加载数据
                        crpLv.onRefreshOpen();
                        LogUtils.e(TAG, "上拉==");
                        initData();

                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int
                    visibleItemCount, int totalItemCount) {

            }
        });
    }

    //销量排序
    private void upDataSale() {
        //更新ui
        //访问网络数据
        if (isSalseUp) {
            upDataUIDown(tvBtnSales, tvBtnPrice, tvBtnPopularity);
            isSalseUp = false;

            value_payment = "desc";
        } else {
            upDataUIUp(tvBtnSales, tvBtnPrice, tvBtnPopularity);
            isSalseUp = true;

            value_payment = "asc";
        }

        isOrder = true;

        LogUtils.e(TAG, "销量排序=" + value_payment);
    }

    // 价格排序
    private void upDataPrice() {
        if (isPriceUp) {
            upDataUIDown(tvBtnPrice, tvBtnSales, tvBtnPopularity);
            isPriceUp = false;
            value_minPrice = "desc";
        } else {
            upDataUIUp(tvBtnPrice, tvBtnSales, tvBtnPopularity);
            isPriceUp = true;
            value_minPrice = "asc";
        }

        isOrder = true;
    }

    //人气排序
    private void upDataPeople() {
        if (isPopUp) {
            upDataUIDown(tvBtnPopularity, tvBtnSales, tvBtnPrice);
            isPopUp = false;
            value_hitCount = "desc";
        } else {
            upDataUIUp(tvBtnPopularity, tvBtnSales, tvBtnPrice);
            isPopUp = true;
            value_hitCount = "asc";
        }

        isOrder = true;
    }


    private void upDataUIUp(TextView tv1, TextView tv2, TextView tv3) {

        tv1.setCompoundDrawables(null, null, imageUp, null);
        tv2.setCompoundDrawables(null, null, null, null);
        tv3.setCompoundDrawables(null, null, null, null);
    }

    private void upDataUIDown(TextView tv, TextView tv2, TextView tv3) {


        tv.setCompoundDrawables(null, null, imageDown, null);
        tv2.setCompoundDrawables(null, null, null, null);
        tv3.setCompoundDrawables(null, null, null, null);
    }

    //如果侧边栏开,则关;反之亦然
    protected void toggle() {
        ExperienceFarmListSlidingMenuActivity mainUI =
                (ExperienceFarmListSlidingMenuActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();//如果侧边栏开,则关;反之亦然
    }

}
