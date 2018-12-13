package com.mac.runtu.activity.experiencefarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.mac.runtu.R;

import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.GpsCoordinateDetailInfo;
import com.mac.runtu.javabean.GpsCoordinateInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 体验农场定位
 */
public class ExperFarmGPSActivity extends AppCompatActivity {

    private static final String TAG = "ExperFarmGPSActivity";

    private String     gspProt; //商品对应的gps端口号

    private ImageView  ivBack;

    public  LatLng      position;
    private SDKReceiver mReceiver;
    private double mLatitude  = 35.7682030000;
    private double mLongitude = 109.4387830000;

    // 定位相关
    LocationClient mLocClient;
    private MyLocationConfiguration.LocationMode mCurrentMode;

    public BitmapDescriptor mCurrentMarker;
    public MyLocationListenner myListener = new MyLocationListenner();

    private MapView  mMapView;
    private BaiduMap mBaiduMap;

    // UI相关
    private Button               requestLocButton;
    private View.OnClickListener btnClickListener;
    private MyLocationData       locData;
    private MapStatus.Builder builder = new MapStatus.Builder();
    private boolean           isOpen  = false;

    //折线绘制
    // 普通折线，点击时改变宽度
    Polyline mPolyline;
    // 多颜色折线，点击时消失
    Polyline mColorfulPolyline;
    private List<Integer> colorValue1;
    private List<Integer> colorValue2;
    private List<Integer> colorValue;
    private int          mPositionDraw = 0;
    private List<LatLng> points1       = new ArrayList<LatLng>();//存储坐标集合
    private OverlayOptions ooPolyline1;
    private boolean isOnce = false;

    private Thread                             mThread;
    private ArrayList<GpsCoordinateDetailInfo> infolist;

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
        setContentView(R.layout.activity_exper_farm_gps);

        gspProt = getIntent().getStringExtra(GlobalConstants
                .key_gspProt);

        LogUtils.e(TAG, "gps设备的Gspprot=" + gspProt);

        initView();

        if (mMapView == null) {
            return;
        }

        initData();
        initListener();

    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.back_Iv);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        requestLocButton = (Button) findViewById(R.id.button1);
    }

    private void initData() {
        //启动服务端socket
        initMap();


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


        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        requestLocButton.setText("普通");


        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        // 界面加载时添加绘制图层
        //addCustomElementsDemo();
        //颜色集合
        colorValue1 = new ArrayList<Integer>();
        colorValue1.add(0xAAFF0000);

        getAddress4Net();
        //initMap();
    }

    private void getAddress4Net() {

        if (TextUtils.isEmpty(gspProt)) {
            return;
        }

        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .exper_farm_gps_location_url, null, GlobalConstants
                        .key_IMEI,
                gspProt, new MyHttpUtils.OnNewWorkRequestListener() {

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
        LogUtils.e(TAG, "infolist.num=" + result);
        GpsCoordinateInfo info = GSonUtil.parseJson(result,
                GpsCoordinateInfo.class);


        infolist = info.objList;

        LogUtils.e(TAG, "infolist.num=" + info.num);

        if (infolist != null && infolist.size() > 0) {

            isOpen = true;
            startMove();

            LogUtils.e(TAG, "GpsCoordinateInfo=" + infolist.toString());
        }
    }


    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = false;

                finish();
            }
        });


        btnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (mCurrentMode) {
                    case NORMAL:
                        requestLocButton.setText("跟随");
                        mCurrentMode = MyLocationConfiguration.LocationMode
                                .FOLLOWING;
                        mBaiduMap
                                .setMyLocationConfigeration(new
                                        MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                    case COMPASS:
                        requestLocButton.setText("普通");
                        mCurrentMode = MyLocationConfiguration.LocationMode
                                .NORMAL;
                        mBaiduMap
                                .setMyLocationConfigeration(new
                                        MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                    case FOLLOWING:
                        requestLocButton.setText("罗盘");
                        mCurrentMode = MyLocationConfiguration.LocationMode
                                .COMPASS;
                        mBaiduMap
                                .setMyLocationConfigeration(new
                                        MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                    default:
                        break;
                }
            }
        };


        requestLocButton.setOnClickListener(btnClickListener);

    }


    //模拟
    private void startMove() {

        // 此处设置开发者获取到的方向信息，顺时针0-360
        // 此处设置开发者获取到的方向信息，顺时针0-360
        //判断集合长度
        //下面集合长度不能为 1
        //放入集合
        new Thread() {
            @Override
            public void run() {
                super.run();

                if (isOpen) {

                    for (int i = 0; i < infolist.size(); i++) {
                        try {
                            sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }



                        mLatitude = infolist.get(i).latitude;
                        mLongitude = infolist.get(i).longitude;
                        LogUtils.e(TAG,"mLatitude="+mLatitude);
                        LogUtils.e(TAG,"mLongitude="+mLongitude);
                        // 此处设置开发者获取到的方向信息，顺时针0-360

                        locData = new MyLocationData.Builder()
                                .accuracy(90.000f)
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                                .direction(100).latitude(mLatitude)
                                .longitude(mLongitude).build();

                        mBaiduMap.setMyLocationData(locData);

                        LatLng position = new LatLng(mLatitude,
                                mLongitude);

                        //判断集合长度
                        if (points1.size() > 40) {
                            points1.clear();
                            clearClick();
                            isOnce = false;//下面集合长度不能为 1
                        }

                        points1.add(position);//放入集合
                        builder.target(position);

                        upDataUi();
                    }

                }

            }

        }.start();

    }

    private void upDataUi() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LogUtils.e(TAG, "坐标在变化");

                //请例图层
                clearClick();

                if (isOpen) {
                    mBaiduMap.animateMapStatus
                            (MapStatusUpdateFactory
                                    .newMapStatus
                                            (builder.build()));

                    //第一次不花图
                    if (isOnce) {
                        addCustomElementsDemo();
                    }
                    isOnce = true;
                }
            }
        });
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置

        }

    }


    //更改地图状态
    private void initMap() {
        //显示地图中心 起点坐标 从网路获得
        position = new LatLng(mLatitude, mLongitude);
        //设置地图中心点
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng
                (position));
        //设置地图的缩放级别
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(19.0f));

    }


    /**
     * 添加点、线、多边形、圆、文字
     */
    public void addCustomElementsDemo() {


        // 添加多颜色分段的折线绘制
        ooPolyline1 = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points1);//.colorsValues(colorValue);
        mColorfulPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline1);

    }


    public void clearClick() {
        // 清除所有图层
        if (mMapView == null) {
            return;
        }
        mMapView.getMap().clear();
    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        isOpen = true;

        super.onPause();
    }

    @Override
    protected void onResume() {

        isOpen = false;


        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        isOpen = false;

        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        unregisterReceiver(mReceiver);


        super.onDestroy();
    }


    public class SDKReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();


            if (s.equals(SDKInitializer
                    .SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                ToastUtils.makeTextShow(ExperFarmGPSActivity
                        .this, "key错误!");
            } else if (s.equals(SDKInitializer
                    .SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                ToastUtils.makeTextShow(ExperFarmGPSActivity
                        .this, "正常使用!");
            } else if (s.equals(SDKInitializer
                    .SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                ToastUtils.makeTextShow(ExperFarmGPSActivity
                        .this, "网路异常!");
            }
        }
    }

}
