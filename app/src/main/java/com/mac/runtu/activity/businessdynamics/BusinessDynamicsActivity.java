package com.mac.runtu.activity.businessdynamics;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.activity.otherActivity.BaseActivity;
import com.mac.runtu.adapter.BusinessDatalistAdapter;
import com.mac.runtu.adapter.FramDatalistAdapter;
import com.mac.runtu.business.AddressBiz4FromPro3;
import com.mac.runtu.business.TypeSelectBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.BusinessDataDetailInfo;
import com.mac.runtu.javabean.BusinessDataListInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * 客商动态
 */
public class BusinessDynamicsActivity extends BaseActivity implements
        OnClickListener {
    private static final String TAG = "BusinessDynamicsActivity";


    private int mBusNum;
    private int mFramNum;

    private int pageNumBus  = 1;
    private int pageNumFram = 1;

    private int                               type      = 1;
    private ArrayList<BusinessDataDetailInfo> mBusList  = new ArrayList<>();
    private ArrayList<BusinessDataDetailInfo> mFramList = new ArrayList<>();


    //轮播条数据集合
    TextView  merchantsReleaseTv;
    TextView  householdReleaseTv;
    ImageView confirmIv;


    private TextView tvCity;
    private TextView tvArea;

    private ImageView ivCity;
    private ImageView ivArea;
    private TextView  tvTown;
    private TextView  tvVillage;
    private ImageView ivTown;
    private ImageView ivVillage;

    private TextView  tvShuruType;
    private ImageView ivShuruType;


    //轮播条数据集合
    TextView  merchantsReleaseTv2;
    TextView  householdReleaseTv2;
    ImageView confirmIv2;


    private TextView tvCity2;
    private TextView tvArea2;

    private ImageView ivCity2;
    private ImageView ivArea2;
    private TextView  tvTown2;
    private TextView  tvVillage2;
    private ImageView ivTown2;
    private ImageView ivVillage2;

    private TextView  tvShuruType2;
    private ImageView ivShuruType2;


    private boolean isRefresh = false;


    private BusinessDatalistAdapter mBusAdapter;
    private FramDatalistAdapter     mFramAdapter;

    private LinearLayout llSerachMenu;
    private LinearLayout llSerachMenu2;


    @Override
    protected int getTopTitleName() {
        return R.string.business_dynamics;
    }

    @Override
    protected View getFloatView() {
        return getLayoutInflater().inflate(R.layout
                .business_dynamics_float_menu_layout, null);
    }

    @Override
    public void initView() {


        merchantsReleaseTv = ButterKnife.findById(floatView, R.id
                .merchants_release_Tv);

        householdReleaseTv = ButterKnife.findById(floatView, R.id
                .household_release_Tv);

        confirmIv = ButterKnife.findById(floatView, R.id.confirm_Iv);


        tvCity = ButterKnife.findById(floatView, R.id.tv_city);
        tvArea = ButterKnife.findById(floatView, R.id.tv_area);
        tvTown = ButterKnife.findById(floatView, R.id.tv_town);
        tvVillage = ButterKnife.findById(floatView, R.id.tv_village);
        ivCity = ButterKnife.findById(floatView, R.id.iv_city);
        ivArea = ButterKnife.findById(floatView, R.id.iv_area);
        ivTown = ButterKnife.findById(floatView, R.id.iv_town);
        ivVillage = ButterKnife.findById(floatView, R.id.iv_village);

        llSerachMenu = ButterKnife.findById(floatView, R.id.ll_serach);
        TextView tvSearch = ButterKnife.findById(floatView, R.id.search_Tv);
        tvSearch.setText(UiUtils.getString(R.string
                .business_dynamics_search_hint));

        tvShuruType = ButterKnife.findById(floatView, R.id.tv_shu_ru_type);
        ivShuruType = ButterKnife.findById(floatView, R.id.iv_shu_ru_type);

        //禁用点击确定按钮
        //confirmIv.setEnabled(false);

        //加载选择数据
        businessUpUi();

        merchantsReleaseTv2 = ButterKnife.findById(menuLL, R.id
                .merchants_release_Tv);

        householdReleaseTv2 = ButterKnife.findById(menuLL, R.id
                .household_release_Tv);

        confirmIv2 = ButterKnife.findById(menuLL, R.id.confirm_Iv);


        tvCity2 = ButterKnife.findById(menuLL, R.id.tv_city);
        tvArea2 = ButterKnife.findById(menuLL, R.id.tv_area);
        tvTown2 = ButterKnife.findById(menuLL, R.id.tv_town);
        tvVillage2 = ButterKnife.findById(menuLL, R.id.tv_village);
        ivCity2 = ButterKnife.findById(menuLL, R.id.iv_city);
        ivArea2 = ButterKnife.findById(menuLL, R.id.iv_area);
        ivTown2 = ButterKnife.findById(menuLL, R.id.iv_town);
        ivVillage2 = ButterKnife.findById(menuLL, R.id.iv_village);

        llSerachMenu2 = ButterKnife.findById(menuLL, R.id.ll_serach);
        TextView tvSearch2 = ButterKnife.findById(menuLL, R.id.search_Tv);
        tvSearch2.setText(UiUtils.getString(R.string
                .business_dynamics_search_hint));

        tvShuruType2 = ButterKnife.findById(menuLL, R.id.tv_shu_ru_type);
        ivShuruType2 = ButterKnife.findById(menuLL, R.id.iv_shu_ru_type);


    }

    @Override
    public void initView2() {


        if (type == 1) {
            businessUpUi();
            businessUpUi2();
        } else {
            framUpdataUi();
            framUpdataUi2();
        }

    }

    @Override
    public String getAdType() {
        return GlobalConstants.AD_TYPE_BUSINESS;
    }

    @Override
    protected void initDataOther() {
        new AddressBiz4FromPro3(this,null,null, tvCity, ivCity, tvArea,
                ivArea, tvTown, ivTown, tvVillage, ivVillage);
        //初始化type类型
        new TypeSelectBiz(this, tvShuruType,
                ivShuruType, GlobalConstants
                .AD_TYPE_BUSINESS);

        new AddressBiz4FromPro3(this,null,null, tvCity2, ivCity2, tvArea2,
                ivArea2, tvTown2, ivTown2, tvVillage2, ivVillage2);
        //初始化type类型
        new TypeSelectBiz(this, tvShuruType2,
                ivShuruType2, GlobalConstants
                .AD_TYPE_BUSINESS);

    }

    @Override
    protected Class<?> getReleaseActivity() {
        return BusinessDynamicsReleaseActivity.class;
    }

    @Override
    protected String getUrl() {
        return GlobalConstants.business_List_url;
    }

    @Override
    public String getType() {
        return type+"";
    }

    @Override
    public HashMap<String, String> getHashMap() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.key_type, type + "");
        hashMap.put(GlobalConstants.KEY_PAGESIZE, GlobalConstants
                .value_page_size);

        switch (type) {
            case 1:

                hashMap.put(GlobalConstants.KEY_PAGENUM, pageNumBus + "");
                break;
            case 2:
                hashMap.put(GlobalConstants.KEY_PAGENUM, pageNumFram + "");
                break;

        }

        return hashMap;
    }

    @Override
    public void parserData(String result) {
        //得到数据
        BusinessDataListInfo info = GSonUtil
                .parseJson(result, BusinessDataListInfo
                        .class);
        //type
        switch (type) {
            case 1:

                mBusNum = info.num;
                ArrayList<BusinessDataDetailInfo>
                        infoList = info.objList;
                if (infoList != null && infoList.size() > 0) {

                    if (isRefresh) {
                        mBusList.clear();
                        isRefresh = false;
                    }
                    mBusList.addAll(infoList);


                    //客商数据类型
                    if (mBusAdapter == null) {

                        mBusAdapter = new
                                BusinessDatalistAdapter
                                (BusinessDynamicsActivity.this, mBusList);
                        crpLv.setAdapter(mBusAdapter);

                    } else {
                        mBusAdapter.notifyDataSetChanged();
                    }

                    isLoading = false;
                    pageNumBus++;

                    //写入换存
                    mACache.put(cacheKey,result);
                } else {

                }

                break;
            case 2:
                mFramNum = info.num;

                ArrayList<BusinessDataDetailInfo>
                        infoList2 = info.objList;
                if (infoList2 != null && infoList2.size() > 0) {
                    if (isRefresh) {
                        mFramList.clear();
                        isRefresh = false;
                    }

                    mFramList.addAll(infoList2);


                    //农户数据
                    if (mFramAdapter == null) {

                        mFramAdapter = new
                                FramDatalistAdapter
                                (BusinessDynamicsActivity.this, mFramList);
                        crpLv.setAdapter(mFramAdapter);

                    } else {
                        mFramAdapter.notifyDataSetChanged();
                    }


                    isLoading = false;
                    pageNumFram++;

                    //写入换存
                    mACache.put(cacheKey,result);
                } else {

                }
                break;
        }


    }

    @Override
    public void refreshData() {
        if (!isLoading) {

            if (type == 1) {
                pageNumBus = 1;
            } else {
                pageNumFram = 1;
            }
            isRefresh = true;

            initData();
        }
    }

    @Override
    public void lvScrollAction() {
        switch (type) {
            case 1:

                LogUtils.e(TAG, "lastVisiblePosition=" + lastVisiblePosition);
                LogUtils.e(TAG, "集合长度mBusList.size()=" + mBusList.size());
                LogUtils.e(TAG, "getFirstVisiblePosition=" + crpLv
                        .getFirstVisiblePosition());

                //如果isLoading=true表示正在加载,还没结束,此时不允许加载下一页
                if (lastVisiblePosition == mBusList.size() - 1 +
                        2 + 1 &&
                        !isLoading && mBusList.size() < mBusNum) {

                    LogUtils.e(TAG, "加载数据");
                    //判断是否到达最后一条数据
                    //开始加载下一页数据
                    LogUtils.e(TAG, "lastVisiblePosition=" +
                            lastVisiblePosition);
                    LogUtils.e(TAG, "集合长度mBusList.size()=" + mBusList.size());

                    crpLv.onRefreshOpen();
                    initData();


                }
                break;
            case 2:
                //如果isLoading=true表示正在加载,还没结束,此时不允许加载下一页
                if (lastVisiblePosition == mFramList.size() - 1 +
                        2 + 1 &&
                        !isLoading && mFramList.size() < 0) {

                    //判断是否到达最后一条数据
                    //开始加载下一页数据

                    crpLv.onRefreshOpen();
                    initData();
                }
                break;

        }

    }

    @Override
    public void lvOnItemClick(int position) {
        int infoPosition = position - 2;

        LogUtils.e(TAG, "点击的位置是" + position);
        if (position > 1) {

            switch (type) {
                case 1:
                    Intent intent = new Intent(BusinessDynamicsActivity
                            .this,
                            BusinessDynamicsDetailsActivity.class);
                    //获取条目的id跳转详情页
                    intent.putExtra(GlobalConstants.key_uuid,
                            mBusList.get(infoPosition).uuid);
                    startActivity(intent);
                    break;
                case 2:
                    Intent intent2 = new Intent(BusinessDynamicsActivity
                            .this,
                            FarmerDynamicsDetailsActivity.class);
                    //获取条目的id跳转详情页

                    intent2.putExtra(GlobalConstants.key_uuid,
                            mFramList.get(infoPosition).uuid);
                    startActivity(intent2);
                    break;
            }
        }

    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;


        }
    }

    private void framUpdataUi() {
        merchantsReleaseTv.setBackgroundResource(R.drawable
                .shape_just_bottom_line_business_dynamics_normal_bg);
        householdReleaseTv.setBackgroundResource(R.drawable
                .shape_just_bottom_line_business_dynamics_select_bg);
       /* merchantsReleaseTv.setTextColor(getColor(R.color.normal_color));
        householdReleaseTv.setTextColor(getColor(R.color.titlebg));*/
        merchantsReleaseTv.setTextColor(UiUtils.getColor(R.color.normal_color));
        householdReleaseTv.setTextColor(UiUtils.getColor(R.color.titlebg));

    }

    private void businessUpUi() {
        merchantsReleaseTv.setBackgroundResource(R.drawable
                .shape_just_bottom_line_business_dynamics_select_bg);
        householdReleaseTv.setBackgroundResource(R.drawable
                .shape_just_bottom_line_business_dynamics_normal_bg);
        /*merchantsReleaseTv.setTextColor(getColor(R.color.titlebg));
        householdReleaseTv.setTextColor(getColor(R.color.normal_color));*/
        merchantsReleaseTv.setTextColor(UiUtils.getColor(R.color.titlebg));
        householdReleaseTv.setTextColor(UiUtils.getColor(R.color.normal_color));
    }

    private void framUpdataUi2() {
        merchantsReleaseTv2.setBackgroundResource(R.drawable
                .shape_just_bottom_line_business_dynamics_normal_bg);
        householdReleaseTv2.setBackgroundResource(R.drawable
                .shape_just_bottom_line_business_dynamics_select_bg);
       /* merchantsReleaseTv.setTextColor(getColor(R.color.normal_color));
        householdReleaseTv.setTextColor(getColor(R.color.titlebg));*/
        merchantsReleaseTv2.setTextColor(UiUtils.getColor(R.color
                .normal_color));
        householdReleaseTv2.setTextColor(UiUtils.getColor(R.color.titlebg));

    }

    private void businessUpUi2() {
        merchantsReleaseTv2.setBackgroundResource(R.drawable
                .shape_just_bottom_line_business_dynamics_select_bg);
        householdReleaseTv2.setBackgroundResource(R.drawable
                .shape_just_bottom_line_business_dynamics_normal_bg);
        /*merchantsReleaseTv.setTextColor(getColor(R.color.titlebg));
        householdReleaseTv.setTextColor(getColor(R.color.normal_color));*/
        merchantsReleaseTv2.setTextColor(UiUtils.getColor(R.color.titlebg));
        householdReleaseTv2.setTextColor(UiUtils.getColor(R.color
                .normal_color));
    }


    @Override
    public void initListener() {

        merchantsReleaseTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                businessUpUi();
                type = 1;

                int firstVisiblePosition = crpLv.getFirstVisiblePosition();
                crpLv.setAdapter(mBusAdapter);
                crpLv.setSelection(firstVisiblePosition);
                //crpLv.setAdapter(mBusAdapter);

                if (mBusList.size() <= 0) {
                    initData();
                }
            }
        });

        householdReleaseTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                framUpdataUi();
                type = 2;
                int firstVisiblePosition = crpLv.getFirstVisiblePosition();
                crpLv.setAdapter(mFramAdapter);
                crpLv.setSelection(firstVisiblePosition);

                LogUtils.e(TAG, "农户点击访问网络!==" + mFramList.size());

                if (mFramList.size() <= 0) {
                    LogUtils.e(TAG, "农户点击访问网络!");
                    initData();
                    //LogUtils.e(TAG,"农户点击访问网络!");
                }
            }
        });

        confirmIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                LogUtils.e(TAG, "地址 省" + MarkerConstants.value_province);
                LogUtils.e(TAG, "地址 市" + MarkerConstants.value_city);
                LogUtils.e(TAG, "地址 县" + MarkerConstants.value_county);
                LogUtils.e(TAG, "地址 镇" + MarkerConstants.value_town);
                LogUtils.e(TAG, "地址 村" + MarkerConstants.value_village);


                Intent intent = new Intent(BusinessDynamicsActivity.this,
                        BusinessSerachListActivity.class);
                //身份
                intent.putExtra(GlobalConstants.key_type, type + "");
                //标记是哪个
                intent.putExtra(GlobalConstants.key_content_ui,
                        GlobalConstants.value_from_select);
                startActivity(intent);
            }
        });

        //搜索框的按钮点击市

        //获得数据
        llSerachMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusinessDynamicsActivity.this,
                        BusinessSerachListActivity.class);
                //身份
                intent.putExtra(GlobalConstants.key_type, type + "");
                startActivity(intent);
            }
        });


        merchantsReleaseTv2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                businessUpUi2();
                type = 1;

                int firstVisiblePosition = crpLv.getFirstVisiblePosition();
                crpLv.setAdapter(mBusAdapter);
                crpLv.setSelection(firstVisiblePosition);

                if (mBusList.size() <= 0) {
                    initData();
                }
            }
        });

        householdReleaseTv2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                framUpdataUi2();
                type = 2;

                int firstVisiblePosition = crpLv.getFirstVisiblePosition();
                crpLv.setAdapter(mFramAdapter);
                crpLv.setSelection(firstVisiblePosition);


                LogUtils.e(TAG, "农户点击访问网络!==" + mFramList.size());

                if (mFramList.size() <= 0) {
                    LogUtils.e(TAG, "农户点击访问网络!");
                    initData();
                    //LogUtils.e(TAG,"农户点击访问网络!");
                }
            }
        });

        confirmIv2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BusinessDynamicsActivity.this,
                        BusinessSerachListActivity.class);
                //身份
                intent.putExtra(GlobalConstants.key_type, type + "");
                //标记是哪个
                intent.putExtra(GlobalConstants.key_content_ui,
                        GlobalConstants.value_from_select);
                startActivity(intent);
            }
        });

        //搜索框的按钮点击市

        //获得数据
        llSerachMenu2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusinessDynamicsActivity.this,
                        BusinessSerachListActivity.class);
                //身份
                intent.putExtra(GlobalConstants.key_type, type + "");
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            refreshData();
        }
    }

}
