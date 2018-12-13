package com.mac.runtu.activity.ruraltourism;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.mac.runtu.R;
import com.mac.runtu.baidumapgps.BikingRouteOverlay;
import com.mac.runtu.baidumapgps.DrivingRouteOverlay;
import com.mac.runtu.baidumapgps.MassTransitRouteOverlay;
import com.mac.runtu.baidumapgps.OverlayManager;
import com.mac.runtu.baidumapgps.RouteLineAdapter;
import com.mac.runtu.baidumapgps.TransitRouteOverlay;
import com.mac.runtu.baidumapgps.WalkingRouteOverlay;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 乡村旅游地图导航
 */
public class RuralTourismMapGPSActivity extends AppCompatActivity implements
        BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener {

    private static final String TAG = "RuralTourismMapGPSActivity";

    @BindView(R.id.back_Iv)
    ImageView backIv;


    public LatLng position;


    // 浏览路线节点相关
    Button               mBtnPre        = null; // 上一个节点
    Button               mBtnNext       = null; // 下一个节点
    int                  nodeIndex      = -1; // 节点索引,供浏览节点时使用
    RouteLine            route          = null;
    MassTransitRouteLine massroute      = null;
    OverlayManager       routeOverlay   = null;
    boolean              useDefaultIcon = false;
    private TextView popupText = null; // 泡泡view

    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    MapView         mMapView  = null;    // 地图View
    BaiduMap        mBaidumap = null;
    // 搜索相关
    RoutePlanSearch mSearch   = null;    // 搜索模块，也可去掉地图模块独立使用

    WalkingRouteResult     nowResultwalk   = null;
    BikingRouteResult      nowResultbike   = null;
    TransitRouteResult     nowResultransit = null;
    DrivingRouteResult     nowResultdrive  = null;
    MassTransitRouteResult nowResultmass   = null;

    private LocationClient     locationClient = null;
    public  BDLocationListener myListener     = new MyBdlocationListener();

    int nowSearchType = -1; // 当前进行的检索，供判断浏览节点时结果使用。

    String startNodeStr = "";
    String endNodeStr   = "";

    //经度
    private double mLongitude = 109.4387830000;
    //纬度
    private double mLatitude  = 35.7682030000;


    //经度
    private double mEndLongitude = 0.00;
    //纬度
    private double mEndLatitude  = 0.00;

    private boolean isShowMessage = true;
    private String detailAddress;
    private String mCounty;
    private String mProvince;
    private String mCity;
    private String mStartPro;
    private String mStartCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rural_tourism_map_gps);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        mEndLatitude = intent.getDoubleExtra(GlobalConstants
                .key_latitude, 0.00);
        mEndLongitude = intent.getDoubleExtra(GlobalConstants
                .key_longitude, 0.00);

        LogUtils.e(TAG,"旅游景点的坐标mEndLatitude="+mEndLatitude);
        LogUtils.e(TAG,"旅游景点的坐标mEndLongitude="+mEndLongitude);

        initVeiw();
        initMap();

        //获得用户当前位置信息
        locationClient = new LocationClient(RuralTourismMapGPSActivity.this);
        locationClient.registerLocationListener(myListener);
        initLocation();//初始化LocationgClient
        locationClient.start();

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
     * 定位SDK监听函数
     */
    public class MyBdlocationListener implements BDLocationListener {


        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            startNodeStr = location.getAddrStr();

            LogUtils.e(TAG, "位置信息地图导航=" + startNodeStr);


            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();

            LogUtils.e(TAG, "mLatitude=" + mLatitude);
            LogUtils.e(TAG, "mLongitude=" + mLongitude);

            if (startNodeStr == null) {

                showNotHavePermission();
                initMap();
                return;
            }

            // mStartPro = startNodeStr.substring(2, 5);
            mStartCity = startNodeStr.substring(5, 8);

            //startNodeStr = startNodeStr.substring(8);

            LogUtils.e(TAG, "当前位置=" + startNodeStr);
            LogUtils.e(TAG, "当前位置mStartPro=" + mStartPro);

            LogUtils.e(TAG, "当前位置mStartCity=" + mStartCity);

            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();

            LogUtils.e(TAG, "mLatitude=" + mLatitude);
            LogUtils.e(TAG, "mLongitude=" + mLongitude);

            initMap();
            initOverlay();

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }

    }


    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 60000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps

        option.setIsNeedLocationDescribe(true);
        // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        option.disableCache(true);
        // 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        //option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(true);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要

        locationClient.setLocOption(option);
    }


    private void initVeiw() {

        findViewById(R.id.back_Iv).setOnClickListener(new View
                .OnClickListener() {


            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //注册广播

        CharSequence titleLable = "路线规划功能";
        setTitle(titleLable);


        // 初始化地图
        mMapView = (MapView) findViewById(R.id.map);
        mBaidumap = mMapView.getMap();

        mBtnPre = (Button) findViewById(R.id.pre);
        mBtnNext = (Button) findViewById(R.id.next);

        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        // 地图点击事件处理
        mBaidumap.setOnMapClickListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);


        // 开启定位图层
        mBaidumap.setMyLocationEnabled(true);
    }

    //更改地图状态
    private void initMap() {

        //显示地图中心 起点坐标 从网路获得
        //position = new LatLng(35.7682030000, 109.4387830000); //纬度  经度

        position = new LatLng(mLatitude, mLongitude);
        //设置地图中心点
        mBaidumap.animateMapStatus(MapStatusUpdateFactory.newLatLng
                (position));
        //设置地图的缩放级别
        mBaidumap.animateMapStatus(MapStatusUpdateFactory.zoomTo(16));

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
        mBaidumap.addOverlay(options);
    }


    public class SDKReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();


            if (s.equals(SDKInitializer
                    .SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                ToastUtils.makeTextShow(RuralTourismMapGPSActivity
                        .this, "key错误!");
            } else if (s.equals(SDKInitializer
                    .SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
              /*  ToastUtils.makeTextShow(RuralTourismMapGPSActivity
                        .this, "正常使用!");*/
            } else if (s.equals(SDKInitializer
                    .SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                ToastUtils.makeTextShow(RuralTourismMapGPSActivity
                        .this, "网路异常!");
            }
        }
    }

    /**
     * 发起路线规划搜索示例
     *
     * @param v
     */
    public void searchButtonProcess(View v) {
        // 重置浏览节点的路线数据
        route = null;
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        mBaidumap.clear();
        // 处理搜索按钮响应
        // 设置起终点信息，对于tranist search 来说，城市名无意义
        if (mSearch == null) {
            return;

        }

        PlanNode stNode = PlanNode.withLocation(position);
        // PlanNode stNode = PlanNode.withCityNameAndPlaceName(mStartCity,
        // "长盛科技产业园");

        LatLng endStrLatLng = new LatLng(mEndLatitude, mEndLongitude);

        //PlanNode enNode = PlanNode.withCityNameAndPlaceName(mCity, "锦业一路");
        PlanNode enNode = PlanNode.withLocation(endStrLatLng);

        // 实际使用中请对起点终点城市进行正确的设定
        if (stNode == null || enNode == null) {
            return;
        }


        if (v.getId() == R.id.mass) {
           /* PlanNode stMassNode = PlanNode.withCityNameAndPlaceName(mStartPro,
                    "天安门");
            PlanNode enMassNode = PlanNode.withCityNameAndPlaceName(mProvince,
                    "东方明珠");

            mSearch.masstransitSearch(new MassTransitRoutePlanOption().from
                    (stMassNode).to(enMassNode));*/
            nowSearchType = 0;
        } else if (v.getId() == R.id.drive) {

            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode).to(enNode));

            nowSearchType = 1;

        } else if (v.getId() == R.id.transit) {
            if (mStartCity == null) {
                return;
            }

            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode).city(mStartCity).to(enNode));
            nowSearchType = 2;
        } else if (v.getId() == R.id.walk) {
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode).to(enNode));
            nowSearchType = 3;
        } else if (v.getId() == R.id.bike) {
            mSearch.bikingSearch((new BikingRoutePlanOption())
                    .from(stNode).to(enNode));
            nowSearchType = 4;
        }
    }

    /**
     * 节点浏览示例
     *
     * @param v
     */
    public void nodeClick(View v) {
        LatLng nodeLocation = null;
        String nodeTitle = null;
        Object step = null;

        if (nowSearchType != 0 && nowSearchType != -1) {
            // 非跨城综合交通
            if (route == null || route.getAllStep() == null) {
                return;
            }
            if (nodeIndex == -1 && v.getId() == R.id.pre) {
                return;
            }
            // 设置节点索引
            if (v.getId() == R.id.next) {
                if (nodeIndex < route.getAllStep().size() - 1) {
                    nodeIndex++;
                } else {
                    return;
                }
            } else if (v.getId() == R.id.pre) {
                if (nodeIndex > 0) {
                    nodeIndex--;
                } else {
                    return;
                }
            }
            // 获取节结果信息
            step = route.getAllStep().get(nodeIndex);
            if (step instanceof DrivingRouteLine.DrivingStep) {
                nodeLocation = ((DrivingRouteLine.DrivingStep) step)
                        .getEntrance().getLocation();
                nodeTitle = ((DrivingRouteLine.DrivingStep) step)
                        .getInstructions();
            } else if (step instanceof WalkingRouteLine.WalkingStep) {
                nodeLocation = ((WalkingRouteLine.WalkingStep) step)
                        .getEntrance().getLocation();
                nodeTitle = ((WalkingRouteLine.WalkingStep) step)
                        .getInstructions();
            } else if (step instanceof TransitRouteLine.TransitStep) {
                nodeLocation = ((TransitRouteLine.TransitStep) step)
                        .getEntrance().getLocation();
                nodeTitle = ((TransitRouteLine.TransitStep) step)
                        .getInstructions();
            } else if (step instanceof BikingRouteLine.BikingStep) {
                nodeLocation = ((BikingRouteLine.BikingStep) step)
                        .getEntrance().getLocation();
                nodeTitle = ((BikingRouteLine.BikingStep) step)
                        .getInstructions();
            }
        } else if (nowSearchType == 0) {
            // 跨城综合交通  综合跨城公交的结果判断方式不一样


            if (massroute == null || massroute.getNewSteps() == null) {
                return;
            }
            if (nodeIndex == -1 && v.getId() == R.id.pre) {
                return;
            }
            boolean isSamecity = nowResultmass.getOrigin().getCityId() ==
                    nowResultmass.getDestination().getCityId();
            int size = 0;
            if (isSamecity) {
                size = massroute.getNewSteps().size();
            } else {
                for (int i = 0; i < massroute.getNewSteps().size(); i++) {
                    size += massroute.getNewSteps().get(i).size();
                }
            }

            // 设置节点索引
            if (v.getId() == R.id.next) {
                if (nodeIndex < size - 1) {
                    nodeIndex++;
                } else {
                    return;
                }
            } else if (v.getId() == R.id.pre) {
                if (nodeIndex > 0) {
                    nodeIndex--;
                } else {
                    return;
                }
            }
            if (isSamecity) {
                // 同城
                step = massroute.getNewSteps().get(nodeIndex).get(0);
            } else {
                // 跨城
                int num = 0;
                for (int j = 0; j < massroute.getNewSteps().size(); j++) {
                    num += massroute.getNewSteps().get(j).size();
                    if (nodeIndex - num < 0) {
                        int k = massroute.getNewSteps().get(j).size() +
                                nodeIndex - num;
                        step = massroute.getNewSteps().get(j).get(k);
                        break;
                    }
                }
            }

            nodeLocation = ((MassTransitRouteLine.TransitStep) step)
                    .getStartLocation();
            nodeTitle = ((MassTransitRouteLine.TransitStep) step)
                    .getInstructions();
        }

        if (nodeLocation == null || nodeTitle == null) {
            return;
        }

        // 移动节点至中心
        mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
        // show popup
        popupText = new TextView(RuralTourismMapGPSActivity.this);
        popupText.setBackgroundResource(R.drawable.popup);
        popupText.setPadding(20, 20, 20, 20);
        popupText.setTextColor(0xFF000000);
        popupText.setText(nodeTitle);
        mBaidumap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));
    }

    /**
     * 切换路线图标，刷新地图使其生效
     * 注意： 起终点图标使用中心对齐.
     */
    public void changeRouteIcon(View v) {
        if (routeOverlay == null) {
            return;
        }
        if (useDefaultIcon) {
            ((Button) v).setText("自定义起终点图标");
            Toast.makeText(this,
                    "将使用系统起终点图标",
                    Toast.LENGTH_SHORT).show();

        } else {
            ((Button) v).setText("系统起终点图标");
            Toast.makeText(this,
                    "将使用自定义起终点图标",
                    Toast.LENGTH_SHORT).show();

        }
        useDefaultIcon = !useDefaultIcon;
        routeOverlay.removeFromMap();
        routeOverlay.addToMap();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {

            ToastUtils.makeTextShow(RuralTourismMapGPSActivity.this,
                    "抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);

            if (result.getRouteLines().size() > 1) {
                nowResultwalk = result;

                MyTransitDlg myTransitDlg = new MyTransitDlg
                        (RuralTourismMapGPSActivity.this,
                                result.getRouteLines(),
                                RouteLineAdapter.Type.WALKING_ROUTE);
                myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                    public void onItemClick(int position) {
                        route = nowResultwalk.getRouteLines().get(position);
                        WalkingRouteOverlay overlay = new
                                MyWalkingRouteOverlay(mBaidumap);
                        mBaidumap.setOnMarkerClickListener(overlay);
                        routeOverlay = overlay;
                        overlay.setData(nowResultwalk.getRouteLines().get
                                (position));
                        overlay.addToMap();
                        overlay.zoomToSpan();
                    }

                });
                myTransitDlg.show();

            } else if (result.getRouteLines().size() == 1) {
                // 直接显示
                route = result.getRouteLines().get(0);
                WalkingRouteOverlay overlay = new MyWalkingRouteOverlay
                        (mBaidumap);
                mBaidumap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            } else {
                Log.d("route result", "结果数<0");
                return;
            }

        }

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtils.makeTextShow(RuralTourismMapGPSActivity.this,
                    "抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);


            if (result.getRouteLines().size() > 1) {
                nowResultransit = result;

                MyTransitDlg myTransitDlg = new MyTransitDlg
                        (RuralTourismMapGPSActivity.this,
                                result.getRouteLines(),
                                RouteLineAdapter.Type.TRANSIT_ROUTE);
                myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                    public void onItemClick(int position) {
                        route = nowResultransit.getRouteLines().get(position);
                        TransitRouteOverlay overlay = new
                                MyTransitRouteOverlay(mBaidumap);
                        mBaidumap.setOnMarkerClickListener(overlay);
                        routeOverlay = overlay;
                        overlay.setData(nowResultransit.getRouteLines().get
                                (position));
                        overlay.addToMap();
                        overlay.zoomToSpan();
                    }

                });
                myTransitDlg.show();


            } else if (result.getRouteLines().size() == 1) {
                // 直接显示
                route = result.getRouteLines().get(0);
                TransitRouteOverlay overlay = new MyTransitRouteOverlay
                        (mBaidumap);
                mBaidumap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            } else {
                Log.d("route result", "结果数<0");
                return;
            }


        }
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtils.makeTextShow(RuralTourismMapGPSActivity.this,
                    "抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点模糊，获取建议列表
            result.getSuggestAddrInfo();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nowResultmass = result;

            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);


            // 列表选择
            MyTransitDlg myTransitDlg = new MyTransitDlg
                    (RuralTourismMapGPSActivity.this,
                            result.getRouteLines(),
                            RouteLineAdapter.Type.MASS_TRANSIT_ROUTE);
            nowResultmass = result;
            myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                public void onItemClick(int position) {
                    MyMassTransitRouteOverlay overlay = new
                            MyMassTransitRouteOverlay(mBaidumap);

                    mBaidumap.setOnMarkerClickListener(overlay);

                    routeOverlay = overlay;

                    massroute = nowResultmass.getRouteLines().get(position);

                    overlay.setData(nowResultmass.getRouteLines().get
                            (position));

                    MassTransitRouteLine line = nowResultmass.getRouteLines()
                            .get(position);
                    overlay.setData(line);
                    if (nowResultmass.getOrigin().getCityId() ==
                            nowResultmass.getDestination().getCityId()) {
                        // 同城
                        overlay.setSameCity(true);
                    } else {
                        // 跨城
                        overlay.setSameCity(false);

                    }
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }

            });
            myTransitDlg.show();
        }

    }


    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtils.makeTextShow(RuralTourismMapGPSActivity.this,
                    "抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;


            if (result.getRouteLines().size() > 1) {
                nowResultdrive = result;

                MyTransitDlg myTransitDlg = new MyTransitDlg
                        (RuralTourismMapGPSActivity.this,
                                result.getRouteLines(),
                                RouteLineAdapter.Type.DRIVING_ROUTE);
                myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                    public void onItemClick(int position) {
                        route = nowResultdrive.getRouteLines().get(position);
                        DrivingRouteOverlay overlay = new
                                MyDrivingRouteOverlay(mBaidumap);
                        mBaidumap.setOnMarkerClickListener(overlay);
                        routeOverlay = overlay;
                        overlay.setData(nowResultdrive.getRouteLines().get
                                (position));
                        overlay.addToMap();
                        overlay.zoomToSpan();
                    }

                });
                myTransitDlg.show();

            } else if (result.getRouteLines().size() == 1) {
                route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay
                        (mBaidumap);
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
                mBtnPre.setVisibility(View.VISIBLE);
                mBtnNext.setVisibility(View.VISIBLE);
            } else {
                Log.d("route result", "结果数<0");
                return;
            }

        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtils.makeTextShow(RuralTourismMapGPSActivity.this,
                    "抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);

            if (result.getRouteLines().size() > 1) {
                nowResultbike = result;

                MyTransitDlg myTransitDlg = new MyTransitDlg
                        (RuralTourismMapGPSActivity.this,
                                result.getRouteLines(),
                                RouteLineAdapter.Type.DRIVING_ROUTE);
                myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                    public void onItemClick(int position) {
                        route = nowResultbike.getRouteLines().get(position);
                        BikingRouteOverlay overlay = new MyBikingRouteOverlay
                                (mBaidumap);
                        mBaidumap.setOnMarkerClickListener(overlay);
                        routeOverlay = overlay;
                        overlay.setData(nowResultbike.getRouteLines().get
                                (position));
                        overlay.addToMap();
                        overlay.zoomToSpan();
                    }

                });
                myTransitDlg.show();

            } else if (result.getRouteLines().size() == 1) {
                route = result.getRouteLines().get(0);
                BikingRouteOverlay overlay = new MyBikingRouteOverlay
                        (mBaidumap);
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
                mBtnPre.setVisibility(View.VISIBLE);
                mBtnNext.setVisibility(View.VISIBLE);
            } else {
                Log.d("route result", "结果数<0");
                return;
            }

        }
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyBikingRouteOverlay extends BikingRouteOverlay {
        public MyBikingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }


    }

    private class MyMassTransitRouteOverlay extends MassTransitRouteOverlay {
        public MyMassTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }


    }

    @Override
    public void onMapClick(LatLng point) {
        mBaidumap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi poi) {
        return false;
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mSearch != null) {
            mSearch.destroy();
        }
        mMapView.onDestroy();

        super.onDestroy();

        locationClient.stop();
    }

    // 响应DLg中的List item 点击
    interface OnItemInDlgClickListener {
        public void onItemClick(int position);
    }

    // 供路线选择的Dialog
    class MyTransitDlg extends Dialog {

        private List<? extends RouteLine> mtransitRouteLines;
        private ListView                  transitRouteList;
        private RouteLineAdapter          mTransitAdapter;

        OnItemInDlgClickListener onItemInDlgClickListener;

        public MyTransitDlg(Context context, int theme) {
            super(context, theme);
        }

        public MyTransitDlg(Context context, List<? extends RouteLine>
                transitRouteLines, RouteLineAdapter.Type
                                    type) {
            this(context, 0);
            mtransitRouteLines = transitRouteLines;
            mTransitAdapter = new RouteLineAdapter(context,
                    mtransitRouteLines, type);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_transit_dialog);

            transitRouteList = (ListView) findViewById(R.id.transitList);
            transitRouteList.setAdapter(mTransitAdapter);

            transitRouteList.setOnItemClickListener(new AdapterView
                    .OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int
                        position, long id) {
                    onItemInDlgClickListener.onItemClick(position);
                    mBtnPre.setVisibility(View.VISIBLE);
                    mBtnNext.setVisibility(View.VISIBLE);
                    dismiss();

                }
            });
        }

        public void setOnItemInDlgClickLinster(OnItemInDlgClickListener
                                                       itemListener) {
            onItemInDlgClickListener = itemListener;
        }

    }

    protected void showNotHavePermission() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("权限");
        builder.setMessage("无法得到位置信息,请在设置里面授予权限");
        builder.setPositiveButton("确定", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Settings
                                .ACTION_MANAGE_APPLICATIONS_SETTINGS);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        finish();
                    }
                });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.show();
    }


}
