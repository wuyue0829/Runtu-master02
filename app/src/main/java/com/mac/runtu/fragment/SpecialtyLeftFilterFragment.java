package com.mac.runtu.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.mac.runtu.R;
import com.mac.runtu.activity.experiencefarm.ExperFarmFilterResultActivity;
import com.mac.runtu.activity.experiencefarm
        .ExperienceFarmListSlidingMenuActivity;
import com.mac.runtu.activity.experiencefarm.ExperienceFarmSlidingMenuActivity;
import com.mac.runtu.activity.specialtyshop.SpecialtyListlidingMenuActivity;
import com.mac.runtu.activity.specialtyshop.SpecialtySlidingMenuActivity;
import com.mac.runtu.activity.specialtyshop.SpecizltySearchListActivity;
import com.mac.runtu.adapter.GoodFilterOneAdapter;
import com.mac.runtu.business.AddressTV2Biz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.GoodFilterInfo;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.UiUtils;
import com.mac.runtu.view.FlowLayout;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:筛选原是版本 y
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/5 0005 下午 12:04
 */
public class SpecialtyLeftFilterFragment extends Fragment {

    private static final String TAG = "SpecialtyLeftFilterFragment";
    public Activity mActivity;//可以当做Context让子类使用, MainActivity

    @BindView(R.id.tv_again)
    TextView tvAgain;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.tv_province)
    TextView tvProvince;
    @BindView(R.id.tv_city)
    TextView tvCity;


    @BindView(R.id.tv_fenlei_name)
    TextView tvFenleiName;
    // @BindView(R.id.gv_fenlei)
    GridView gvFenlei;
    @BindView(R.id.lv)
    ListView     lv;
    @BindView(R.id.fl_myView)
    FlowLayout   flMyView;
    @BindView(R.id.ll_top_filter)
    LinearLayout llTopFilter;

    private String open;


    private static final String right = "right";
    private static final String left  = "left";
    private ArrayList<GoodFilterInfo> infoList;
    private GoodFilterOneAdapter      mListAdapter;
    private GoodFilterInfo            info;
    private ArrayList<GoodFilterInfo> infoListTwo = new ArrayList<>();


    private int    selectOneId;
    private String mModel;
    private ArrayList<TextView> tvList = new ArrayList<>();

    //fragment创建
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();//获取fragment依附的activity对象

    }

    //初始化fragment布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout
                .fragment_specialty_filter, null);
        ButterKnife.bind(this, view);

        //省市县
        new AddressTV2Biz(mActivity, tvProvince, tvCity);

        initListener();
        return view;
    }

    public void setSpecialttyAndFramModel(String model) {
        mModel = model;

        LogUtils.e(TAG, "筛选页面的model=" + mModel);
        initData();
    }

    private void initData() {
        //防止其他地方使用了此变量  给予了赋值
        MarkerConstants.value_kind = "";

        MyHttpUtils.getStringDataFromNet(GlobalConstants.good_filter_url,
                null, GlobalConstants.key_parentCode, "0", GlobalConstants
                        .key_model, mModel,
                new MyHttpUtils.OnNewWorkRequestListener() {
                    @Override
                    public void onNewWorkSuccess(String result) {
                        if (result != null) {
                            parseData(result);
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {

                    }
                });

    }

    private void parseData(String result) {
        Gson gson = new Gson();

        Type type = new
                TypeToken<ArrayList<GoodFilterInfo>>
                        () {
                }.getType();
        //从网络得到的地址集合

        infoList = gson
                .fromJson(result, type);

        if (infoList != null && infoList.size() > 0) {


            infoList.get(0).isSelected = true;
            mListAdapter = new
                    GoodFilterOneAdapter(mActivity, infoList);
            lv.setAdapter(mListAdapter);
            initListViewPositon();
        }


    }

    private void initListViewPositon() {
        tvFenleiName.setText(infoList.get(0).typeName);

        selectOneId = infoList.get(0).id;
        getDataTwo4Net();

    }


    public void getDataTwo4Net() {

        MyHttpUtils.getStringDataFromNet(GlobalConstants.good_filter_url,
                null, GlobalConstants.key_parentCode, selectOneId + "",
                GlobalConstants
                        .key_model, mModel,
                new MyHttpUtils.OnNewWorkRequestListener() {
                    @Override
                    public void onNewWorkSuccess(String result) {
                        if (result != null) {
                            parseGridViewData(result);
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {

                    }
                });
    }

    //二级筛选数据
    private void parseGridViewData(String result) {
        Gson gson = new Gson();

        Type type = new
                TypeToken<ArrayList<GoodFilterInfo>>
                        () {
                }.getType();
        //从网络得到的地址集合

        infoListTwo = gson
                .fromJson(result, type);

        if (infoListTwo != null && infoList.size() > 0) {

            initParam();

        }

    }

    //参数布局
    private void initParam() {

        tvList = new ArrayList<TextView>();
        tvList.clear();
        flMyView.removeAllViews();

        for (int i = 0; i < infoListTwo.size(); i++) {

            View paramView = View.inflate(mActivity, R.layout
                    .item_specialty_detail_good_size, null);

            TextView tvParmName = (TextView) paramView.findViewById(R.id
                    .tv_sizename);
            tvParmName.setText(infoListTwo.get(i).typeName);

            //tv.setBackgroundColor(color);
            tvParmName.setTag(i);

            tvParmName.setOnClickListener(listener);

            tvList.add(tvParmName);

            flMyView.addView(paramView);
        }

    }

    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();

            GoodFilterInfo info = infoListTwo.get(position);

            MarkerConstants.value_kind = info.uuid;


            LogUtils.e(TAG, "参数名称=" + info.typeName);

            upDataUi(position);
        }
    };

    private void upDataUi(int position) {

        if (tvList == null) {
            return;
        }

        for (int i = 0; i < tvList.size(); i++) {
            if (i == position) {
                tvList.get(i).setSelected(true);
            } else {
                tvList.get(i).setSelected(false);

            }
        }

        LogUtils.e(TAG, "数据 MarkerConstants.value_kind=" + MarkerConstants
                .value_kind);
    }


    private void initListener() {

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {
                //取消默认的选中
                info = infoList.get(position);
                info.isSelected = true;
                mListAdapter.notifyDataSetChanged();

                //当前条目选中
                tvFenleiName.setText(info.typeName);

                for (int i = 0; i < infoList.size(); i++) {
                    if (position == i) {
                        continue;
                    }
                    infoList.get(i).isSelected = false;

                }

                //访问网络得到二级数据
                selectOneId = info.id;
                getDataTwo4Net();
            }
        });


        tvAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkerConstants.value_kind = "";
                upDataUi(-1);
                //二级的uuid

            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             //确定
                                             //标记
                                             //MarkerConstants.isFromFilter =
                                             //true;

                                             switch (mModel) {

                                                 case GlobalConstants
                                                         .value_mode_filter_farm:
                                                     //去体验农场
                                                     go2FarmFilterResult();


                                                     break;
                                                 case GlobalConstants
                                                         .value_mode_filter_specialty:
                                                     //去特产电商


                                                     go2SpecSearchList();


                                                     break;
                                             }


                                         }
                                     }

        );

        //如果侧边栏开,则关;反之亦然  有四个地方打开侧边栏
    }

    private void go2SpecSearchList() {

        Intent intent1 = new
                Intent(mActivity,
                SpecizltySearchListActivity.class);
        intent1.putExtra
                (GlobalConstants
                        .key_model, mModel);
        intent1.putExtra
                (GlobalConstants
                        .key_from_fitler, true);


        mActivity.startActivity
                (intent1);

        new Thread() {
            @Override
            public void run() {

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toggle();

                    }
                });
            }
        }.start();

    }

    private void go2FarmFilterResult() {
        Intent intent = new
                Intent(mActivity,
                ExperFarmFilterResultActivity.class);
        intent.putExtra
                (GlobalConstants
                        .key_model, mModel);

        //转成各自对应的activity

        mActivity.startActivity
                (intent);


        new Thread() {
            @Override
            public void run() {

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        toggle();
                    }
                });
            }
        }.start();

    }

    protected void toggle() {

        SlidingFragmentActivity mainUI = null;

        switch (mModel) {
            case GlobalConstants.value_mode_filter_farm:

                if (left.equals(MarkerConstants
                        .slidingmenu_toggle_from)) {

                    mainUI = (ExperienceFarmSlidingMenuActivity)
                            mActivity;
                } else if (right.equals(MarkerConstants
                        .slidingmenu_toggle_from)) {
                    //来自右边
                    mainUI = (ExperienceFarmListSlidingMenuActivity)
                            mActivity;
                }


                break;
            case GlobalConstants.value_mode_filter_specialty:

                if (left.equals(MarkerConstants
                        .slidingmenu_toggle_from)) {
                    mainUI = (SpecialtySlidingMenuActivity) mActivity;

                } else if (right.equals(MarkerConstants
                        .slidingmenu_toggle_from)) {
                    mainUI = (SpecialtyListlidingMenuActivity)
                            mActivity;
                }
                break;

        }


        if (mainUI == null) {
            return;
        }
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();//如果侧边栏开,则关;反之亦然

    }
}
