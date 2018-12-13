package com.mac.runtu.activity.laborscrvice;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.mac.runtu.R;
import com.mac.runtu.adapter.LaborServiceMapAdapter;
import com.mac.runtu.business.AddressBiz4FromPro3;
import com.mac.runtu.business.FromNetInfo2AddsSelectBiz;
import com.mac.runtu.business.TypeSelectBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.javabean.LaborServiceDetailInfo;
import com.mac.runtu.javabean.LaborServiceInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 劳务合做 地图展示消息
 */
public class LaborServiceCooperationMapActivity extends AppCompatActivity {
    private static final String TAG = "LaborServiceCooperationMapActivity";

    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.my_release_Iv)
    ImageView myReleaseIv;
    /* @BindView(R.id.confirm_Iv)
     ImageView conformIv;*/
    @BindView(R.id.map_mode_Iv)
    Button    btnMap;

    @BindView(R.id.bmapView)
    MapView mapView;

    private MapStatusUpdate mapStatusUpdate;
    private View            popVeiw;
    private int             heightPixels;
    private int             widthPixels;
    private int pageNum = 1;
    private int     num;
    private boolean isLoading;
    private boolean isSelect_address;
    private boolean isRefresh = false;
    private LinearLayout        llLiner01;
    private ListView            lv;
    private TextView            tvWorkTime;
    private ImageView           ivWorkTiem;
    private SDKReceiver         mReceiver;

    private TextView            tvCity;
    private TextView            tvArea;
    private ImageView           ivCity;
    private ImageView           ivArea;
    private TextView            tvTown;
    private TextView            tvVillage;
    private ImageView           ivTown;
    private ImageView           ivVillage;
    private TextView            tvWorkType;
    private ImageView           ivWorkType;
    public  BaiduMap            mapController;
    public  LatLng              position;


    private ArrayList<LaborServiceDetailInfo> mAllList = new ArrayList<>();


    private LaborServiceMapAdapter mAdapter;
    private ListView               mPopListView;
    private PopupWindow            pop;
    private ArrayAdapter<String>   mPopAdapter;

    private String[] serviceTimesName = {"上午", "中午", "下午", "晚上", "全天" };
    private String[] serviceTimesType = { "1", "2", "3", "4", "5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_labor_service_cooperation_map);
        ButterKnife.bind(this);

        // 注册 SDK 广播监听者

        //注册的广播监听
        initVeiw();
        //initData();
        initMap();
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

    private void initVeiw() {
        LinearLayout floatView = (LinearLayout) findViewById(R.id.menu_LL);
        tvCity = ButterKnife.findById(floatView, R.id.tv_city);
        tvArea = ButterKnife.findById(floatView, R.id.tv_area);
        tvTown = ButterKnife.findById(floatView, R.id.tv_town);
        tvVillage = ButterKnife.findById(floatView, R.id.tv_village);
        ivCity = ButterKnife.findById(floatView, R.id.iv_city);
        ivArea = ButterKnife.findById(floatView, R.id.iv_area);
        ivTown = ButterKnife.findById(floatView, R.id.iv_town);
        ivVillage = ButterKnife.findById(floatView, R.id.iv_village);

        tvWorkType = ButterKnife.findById(floatView, R.id.tv_worktype);
        ivWorkType = ButterKnife.findById(floatView, R.id.iv_worktype);

        tvWorkTime = ButterKnife.findById(floatView, R.id.tv_worktime);
        ivWorkTiem = ButterKnife.findById(floatView, R.id
                .iv_worktime);

        llLiner01 = ButterKnife.findById(floatView, R.id.linearLayout1);

        new TypeSelectBiz(this, tvWorkType,
                ivWorkType, GlobalConstants.AD_TYPE_SERVICE
        );
        new AddressBiz4FromPro3(this,null,null, tvCity, ivCity, tvArea,
                ivArea, tvTown, ivTown, tvVillage, ivVillage);


        //劳务时间选择
        mPopAdapter = new ArrayAdapter<String>(this, R.layout
                .pop_service_item_textview, serviceTimesName);
        mPopListView = (ListView) View.inflate(this, R.layout
                .listview_address, null);

        tvWorkTime.setText(serviceTimesName[0]);
        //默认值
        MarkerConstants.value_service_time_type = serviceTimesType[0];

        //注册广播
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer
                .SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer
                .SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer
                .SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
        mapController = mapView.getMap();

        //加载pop布局
        popVeiw = View.inflate(this, R.layout
                .pop_labor_service_show_layout, null);
        lv = (ListView) popVeiw.findViewById(R.id.lv);

        //获得屏幕尺寸
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //屏幕高度
        heightPixels = metrics.heightPixels;
        //屏幕宽度
        widthPixels = metrics.widthPixels;

    }

    //更改地图状态
    private void initMap() {

        //显示地图中心 起点坐标 从网路获得
        position = new LatLng(35.7682030000, 109.4387830000);

        //设置地图中心点

        mapController.animateMapStatus(MapStatusUpdateFactory.newLatLng
                (position));
        //设置地图的缩放级别
        mapController.animateMapStatus(MapStatusUpdateFactory.zoomTo(16));


    }

    private void initData() {
        //访问网络获得数据
        isLoading = true;
        FromNetInfo2AddsSelectBiz.setData2Netword(GlobalConstants
                .lao_service_address_select_url,pageNum, new
                FromNetInfo2AddsSelectBiz.OnAddressDataListener() {


                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {
                            parserData(result);
                        } else {
                            ToastUtils.makeTextShow
                                    (LaborServiceCooperationMapActivity.this,
                                            "暂无搜索的数据!");
                            //initData();
                        }

                    }

                    @Override
                    public void onfail() {
                        ToastUtils.makeTextShowNoNet
                                (LaborServiceCooperationMapActivity.this);
                    }
                });

    }

    private void initListener() {

        //劳务时间选择
        ivWorkTiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpWindow(tvWorkTime);
                mPopListView.setAdapter(mPopAdapter);
            }
        });

        mPopListView.setOnItemClickListener(new AdapterView
                .OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {
                //默认值
                MarkerConstants.value_service_time_type =
                        serviceTimesType[position];
                tvWorkTime.setText(serviceTimesName[position]);
                pop.dismiss();

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {
                //跳转详情页
                Intent intent = new Intent(LaborServiceCooperationMapActivity
                        .this, LaborServiceCooperationDetailsActivity
                        .class);
                intent.putExtra(GlobalConstants.key_uuid, mAllList.get
                        (position).uuid);
                startActivity(intent);
            }
        });

    }

    //下拉框
    private void showPopUpWindow(TextView view) {
        // 参数1：popupwindow显示的内容对象
        // 参数2：popupwindow的宽度
        // 参数3：popupwindow的高度
        // 参数4：是否可以获取焦点
        pop = new PopupWindow(mPopListView, view.getWidth(), WindowManager
                .LayoutParams.WRAP_CONTENT,
                true);

        pop.setBackgroundDrawable(new ColorDrawable());

        pop.showAsDropDown(view);// 将popupwindown显示在某一个View的正下方

    }


    @OnClick({R.id.back_Iv, R.id.my_release_Iv, R.id.map_mode_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.my_release_Iv:
                startActivity(new Intent(LaborServiceCooperationMapActivity
                        .this, LaborServiceCooperationReleaseActivity.class));
                break;

            case R.id.map_mode_Iv:
                //弹框

                initData();
                //initMapDataFromNet();
                break;
        }
    }


    private void initMapDataFromNet(double latitude, double longitude) {


        //设置地图中心点            latitude
        position = new LatLng(latitude, longitude);
        mapController.animateMapStatus(MapStatusUpdateFactory.newLatLng
                (position));
        initOverlay();
    }


    private void initOverlay() {
        //创建图片覆盖物
        MarkerOptions options = new MarkerOptions();
        //设置属性信息
        BitmapDescriptor bitMap = BitmapDescriptorFactory.fromResource(R
                .drawable.maker_icon);
        options.icon(bitMap);
        options.position(position);
        //添加到地图上
        mapController.addOverlay(options);
        //弹出

    }


    //解析数据
    private void parserData(String result) {

        LaborServiceInfo info = GSonUtil.parseJson(result,
                LaborServiceInfo.class);

        mAllList = info.objList;

        if (mAllList != null && mAllList.size() > 0) {
            //弹pop
            PopupWindow popupWindow = new PopupWindow(popVeiw, widthPixels /
                    2, WindowManager
                    .LayoutParams.WRAP_CONTENT,
                    true);
            popupWindow.setBackgroundDrawable(new ColorDrawable());
            popupWindow.setOutsideTouchable(false);

           /* popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] -
                    popupWindow.getWidth(), location[1]);*/

            popupWindow.showAtLocation(llLiner01, Gravity.CENTER, 150, 200);

            LaborServiceDetailInfo detailInfo = mAllList.get(0);

            if (detailInfo.latitude != null && detailInfo.longitude != null) {
                LogUtils.e(TAG, "纬度度=" + Double
                        .valueOf(detailInfo.latitude));

                LogUtils.e(TAG, "精度度=" + Double.valueOf(detailInfo.longitude));


                initMapDataFromNet(Double
                        .valueOf(detailInfo.latitude), Double.valueOf(detailInfo
                        .longitude));

            }


            mAdapter = new LaborServiceMapAdapter
                    (LaborServiceCooperationMapActivity.this, mAllList);
            lv.setAdapter(mAdapter);

            isLoading = false;
        } else {
            ToastUtils.makeTextShowNoData(LaborServiceCooperationMapActivity
                    .this);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    public class SDKReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();

            if (s.equals(SDKInitializer
                    .SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                ToastUtils.makeTextShow(LaborServiceCooperationMapActivity
                        .this, "key错误!");
            } else if (s.equals(SDKInitializer
                    .SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                ToastUtils.makeTextShow(LaborServiceCooperationMapActivity
                        .this, "正常使用!");
            } else if (s.equals(SDKInitializer
                    .SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                ToastUtils.makeTextShow(LaborServiceCooperationMapActivity
                        .this, "网路异常!");
            }
        }
    }


}
