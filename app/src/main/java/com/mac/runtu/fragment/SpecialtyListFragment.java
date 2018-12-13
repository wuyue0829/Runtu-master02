package com.mac.runtu.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mac.runtu.R;
import com.mac.runtu.activity.specialtyshop.SpecialtyDetailsActivity;
import com.mac.runtu.activity.specialtyshop.SpecialtyListlidingMenuActivity;
import com.mac.runtu.adapter.SpecialtyGoodAllAdapter;
import com.mac.runtu.business.SpecialtyGoodGetFromNetBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.SpecialtyGoodDetailInfo;
import com.mac.runtu.javabean.SpecialtyGoodInfo;
import com.mac.runtu.utils.ACache;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.view.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:特产电商列表页
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/5 0005 下午 3:49
 */
public class SpecialtyListFragment extends Fragment {

    private static final String TAG = "SpecialtyListFragment";

    public SpecialtyListlidingMenuActivity mActivity;


    private static final String food           = "2";
    private static final String local          = "3";
    private static final String fruitRecommend = "4";

    private static final int mPayMent     = 0;
    private static final int mMinPrice    = 1;
    private static final int mHitCount    = 2;
    private static       int mTabPosition = mPayMent;

    private static final String addressRecommend = "1";
    //private static final String productRecommend = "5";

    private static String                             value_payment  = "desc";
    private static String                             value_minPrice = "desc";
    private static String                             value_hitCount = "desc";
    private        int                                pageNum        = 1;
    private        ArrayList<SpecialtyGoodDetailInfo> mAllList       = new
            ArrayList<>();



    @BindView(R.id.back_Iv)
    ImageView             backIv;
    @BindView(R.id.crp_Lv)
    //ListView          crpLv;
    PullToRefreshListView crpLv;


    @BindView(R.id.tv_btnSales)
    TextView              tvBtnSales;
    @BindView(R.id.tv_btnPrice)
    TextView              tvBtnPrice;
    @BindView(R.id.tv_btnPopularity)
    TextView              tvBtnPopularity;
    @BindView(R.id.tv_btnScreening)
    TextView              tvBtnScreening;


    private boolean isSalseUp = false;   //默认是销量排序
    private boolean isPriceUp = true;
    private boolean isPopUp   = true;
    private Drawable imageDown;
    private Drawable imageUp;
    private String fromImageType = food;
    private int                     num;
    private SpecialtyGoodAllAdapter mAdapter;

    private boolean isLoading;
    private boolean isRefresh;
    private boolean isOrder; //点击排序

    private ACache mACache; //缓存类
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (SpecialtyListlidingMenuActivity) getActivity();
        mACache = ACache.get(mActivity, GlobalConstants.json_cache_name);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout
                .activity_specialty_list, null);

        ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    void initView() {

        //加载箭头图片
        imageUp = mActivity.getResources().getDrawable(R.drawable
                .icon_arrow_up);

        imageDown = mActivity.getResources().getDrawable(R.drawable
                .setting_down_arrow);

        imageUp.setBounds(0, 0, imageUp.getMinimumWidth(), imageUp
                .getMinimumHeight());
        imageDown.setBounds(0, 0, imageDown.getMinimumWidth(), imageDown
                .getMinimumHeight());


    }


    void initData() {

        fromImageType = MarkerConstants.specialty_Type;
        LogUtils.e(TAG, "特产电商传过来的类型=" + fromImageType);
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

    }


    //销量
    private void getData4Payment() {
        SpecialtyGoodGetFromNetBiz.getSpecialtyGoodData4NetPayment(pageNum,
                fromImageType, value_payment, GlobalConstants
                        .value_mode_filter_specialty, new
                        SpecialtyGoodGetFromNetBiz
                                .OnSpecialtyDataListener() {


                            @Override
                            public void onSuccess(String result) {
                                if (result != null) {
                                    parseData(result);
                                    crpLv.onRefreshComplete();
                                }
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
                fromImageType, value_minPrice, GlobalConstants
                        .value_mode_filter_specialty, new
                        SpecialtyGoodGetFromNetBiz
                                .OnSpecialtyDataListener() {


                            @Override
                            public void onSuccess(String result) {
                                if (result != null) {
                                    parseData(result);
                                    crpLv.onRefreshComplete();
                                }
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
                fromImageType, value_hitCount, GlobalConstants
                        .value_mode_filter_specialty, new
                        SpecialtyGoodGetFromNetBiz
                                .OnSpecialtyDataListener() {


                            @Override
                            public void onSuccess(String result) {
                                if (result != null) {


                                    parseData(result);
                                    crpLv.onRefreshComplete();
                                }
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

        ArrayList<SpecialtyGoodDetailInfo> infoList = info.objList;

        if (infoList != null && infoList.size() > 0) {

            if (isRefresh) {
                mAllList.clear();
                isRefresh = false;
            }

            if (isOrder) {
                mAllList.clear();
                isOrder = false;
            }

            mAllList.addAll(infoList);

            if (mAdapter == null) {
                mAdapter = new
                        SpecialtyGoodAllAdapter(mActivity,
                        mAllList);
                crpLv.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }


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
                        (GlobalConstants.value_mode_filter_specialty);
                toggle();
                break;
        }
    }

    private void initListener() {

        crpLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int
                    scrollState) {

                LogUtils.e(TAG, "条目位置" + crpLv.getLastVisiblePosition());
                //滑动改变状态
                if (scrollState == SCROLL_STATE_IDLE) {//ListView空闲
                    //获取当前可见的最后一个item的位置
                    int lastVisiblePosition = crpLv.getLastVisiblePosition();

                    //如果isLoading=true表示正在加载,还没结束,此时不允许加载下一页
                    if (lastVisiblePosition == mAllList.size() &&
                            !isLoading && mAllList.size() < num) {
                        //加载数据
                        crpLv.onRefreshOpen();
                        initData();
                    }

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int
                    visibleItemCount, int totalItemCount) {

            }
        });

        crpLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {

                //跳转详情页
                Intent intent = new Intent(mActivity, SpecialtyDetailsActivity
                        .class);

                intent.putExtra(GlobalConstants.key_uuid,mAllList.get(position)
                        .uuid);

                intent.putExtra(GlobalConstants.key_model,GlobalConstants.value_mode_filter_specialty);
                mActivity.startActivity(intent);

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

        SlidingMenu slidingMenu = mActivity.getSlidingMenu();
        slidingMenu.toggle();//如果侧边栏开,则关;反之亦然
    }

}
