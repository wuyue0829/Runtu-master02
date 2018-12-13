package com.mac.runtu.ipcamer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.ipcamer.adapter.SearchListAdapter;
import com.mac.runtu.ipcamer.utils.ContentCommon;
import com.mac.runtu.ipcamer.utils.SystemValue;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.SPUtils;
import com.mac.runtu.utils.ToastUtils;

import dalvik.system.DexClassLoader;
import vstc2.nativecaller.NativeCaller;

/**
 * 视频账户链接
 */
public class AddCameraActivity extends Activity implements OnClickListener,
        BridgeService.AddCameraInterface
        , OnItemSelectedListener, BridgeService.IpcamClientInterface,
        BridgeService.CallBackMessageInterface {

    private static final String TAG = "AddCameraActivity";
    private Context contex;


    private EditText didEdit           = null;
    private TextView textView_top_show = null;
    private Button done;
    private static final int SEARCH_TIME = 3000;
    private int option = ContentCommon.INVALID_OPTION;
    private int CameraType = ContentCommon.CAMERA_TYPE_MJPEG;
    private Button btnSearchCamera;
    private SearchListAdapter listAdapter = null;
    private ProgressDialog progressdlg = null;
    private boolean isSearched;
    private MyBroadCast receiver;
    private WifiManager  manager = null;
    private ProgressBar  progressBar = null;
    private static final String STR_DID       = "did";
    private static final String STR_MSG_PARAM = "msgparam";
    private MyWifiThread myWifiThread  = null;
    private boolean blagg = false;
    private Intent intentbrod = null;
    private WifiInfo info = null;
    boolean bthread = true;
    private Button button_play = null;
    private Button button_setting = null;
    private Button pic_video = null;
    private Button button_linkcamera = null;
    private int tag = 0;
    private LinearLayout llLoading;
    private Intent intent;

    class MyWifiThread extends Thread {
        @Override
        public void run() {
            while (blagg == true) {
                super.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private class MyBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            MarkerConstants.value_mode_filter_4one = GlobalConstants
                    .value_mode_filter_farm;
            AddCameraActivity.this.finish();


            //Log.d("ip", "AddCameraActivity.this.finish()");
        }

    }

    class StartPPPPThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(100);
                startCameraPPPP();
            } catch (Exception e) {

            }
        }
    }

    private void startCameraPPPP() {
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }

        if (SystemValue.deviceId.toLowerCase().startsWith("vsta")) {
            NativeCaller.StartPPPPExt(SystemValue.deviceId, SystemValue.deviceName,
                    SystemValue.devicePass, 1, "",
                    "EFGFFBBOKAIEGHJAEDHJFEEOHMNGDCNJCDFKAKHLEBJHKEKMCAFCDLLLHAOCJPPMBHMNOMCJKGJEBGGHJHIOMFBDNPKNFEGCEGCBGCALMFOHBCGMFK");
        } else {
            NativeCaller.StartPPPP(SystemValue.deviceId, "admin",
                    SystemValue.devicePass, 1, "");

            //跳转play

            LogUtils.e(TAG, "视频跳转播放页面");


        }

    }

    private void stopCameraPPPP() {
        NativeCaller.StopPPPP(SystemValue.deviceId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_camera_add);
        contex = this;

        //将必要的全局变量存储起来防止 播放视频 内存占用过大 全局变连被删掉


        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        llLoading.setVisibility(View.VISIBLE);
        TextView tvContent = (TextView) llLoading.findViewById(R.id.tv_content);
        tvContent.setText("正在链接....");


        intent = new Intent();
        intent.setClass(this, BridgeService.class);
        startService(intent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //子线程开启视频连接
                    NativeCaller.PPPPInitialOther
                            ("ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL");

                    LogUtils.e(TAG, "视频第一步成功一个页面");


                    Thread.sleep(5000);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            manager = (WifiManager) getApplicationContext().getSystemService(Context
                                    .WIFI_SERVICE);


                            BridgeService.setAddCameraInterface
                                    (AddCameraActivity.this);
                            BridgeService.setCallBackMessage
                                    (AddCameraActivity.this);

                            receiver = new MyBroadCast();

                            IntentFilter filter = new IntentFilter();
                            filter.addAction("finish");
                            registerReceiver(receiver, filter);

                            intentbrod = new Intent("drop");

                            LogUtils.e(TAG, "链接前一步");
                            done();
                        }
                    });
                } catch (Exception e) {

                }
            }
        }).start();

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        blagg = true;
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (myWifiThread != null) {
            blagg = false;
        }

        NativeCaller.StopSearch();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }

        NativeCaller.Free();
        stopCameraPPPP();

        tag = 0;

        MarkerConstants.value_mode_filter_4one =
                GlobalConstants.value_mode_filter_farm;

        //停止视频的服务
        stopService(intent);
    }


    public static String int2ip(long ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }


    /**
     * 摄像机在线时可以获取一张摄像机当前的画面图
     */
    /*private void getSnapshot(){
        String msg="snapshot.cgi?loginuse=admin&loginpas=" + SystemValue
		.devicePass
		 + "&user=admin&pwd=" + SystemValue.devicePass;
		NativeCaller.TransferMessage(SystemValue.deviceId, msg, 1);
	}
*/
    @Override
    public void onClick(View v) {
       /* switch (v.getId()) {
            case R.id.play:

                break;
            case R.id.setting:
                if (tag == 1) {
                 *//*   Intent intent1 = new Intent(AddCameraActivity.this,
                            SettingActivity.class);
                    intent1.putExtra(ContentCommon.STR_CAMERA_ID,
                            SystemValue.deviceId);
                    intent1.putExtra(ContentCommon.STR_CAMERA_NAME,
                            SystemValue.deviceName);
                    intent1.putExtra(ContentCommon.STR_CAMERA_PWD,
							SystemValue.devicePass);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.in_from_right,
                            R.anim.out_to_left);*//*
                } else {
                    Toast.makeText(AddCameraActivity.this, getResources()
							.getString(R.string.main_setting_prompt), Toast
							.LENGTH_SHORT).show();
                }
                break;
            case R.id.location_pics_videos://本地视频图像
                if (SystemValue.deviceId != null) {
                    *//*Intent intent1 = new Intent(AddCameraActivity.this,
                            LocalPictureAndVideoActivity.class);
                    intent1.putExtra(ContentCommon.STR_CAMERA_ID,
                            SystemValue.deviceId);
                    intent1.putExtra(ContentCommon.STR_CAMERA_NAME,
                            SystemValue.deviceName);
                    intent1.putExtra(ContentCommon.STR_CAMERA_PWD,
							SystemValue.devicePass);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.in_from_right,
                            R.anim.out_to_left);*//*
                } else {
                    Toast.makeText(AddCameraActivity.this, "请确认是否选择设备", Toast
							.LENGTH_SHORT).show();
                }
                break;
            case R.id.done:
                if (tag == 1) {
                    Toast.makeText(AddCameraActivity.this, "设备已经是在线状态了",
							Toast.LENGTH_SHORT).show();
                } else if (tag == 2) {
                    Toast.makeText(AddCameraActivity.this, "设备不在线", Toast
							.LENGTH_SHORT).show();
                } else {
                    done();
                }

                break;
            case R.id.btn_searchCamera:
                stopCameraPPPP();
                //把相机状态，设备id置空
                tag = 0;
                textView_top_show.setText(R.string.login_stuta_camer);
                SystemValue.deviceId = null;
                searchCamera();
                break;
            case R.id.btn_linkcamera:
               *//* Intent it = new Intent(AddCameraActivity.this,
                        LinkCameraSettingActivity.class);
                it.putExtra(ContentCommon.STR_CAMERA_ID,
                        SystemValue.deviceId);
                it.putExtra(ContentCommon.STR_CAMERA_NAME,
                        SystemValue.deviceName);
                it.putExtra(ContentCommon.STR_CAMERA_PWD, SystemValue
						.devicePass);
                startActivity(it);*//*
                break;
            default:
                break;
        }*/
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            AddCameraActivity.this.finish();

            //给标记赋值
            MarkerConstants.value_mode_filter_4one =
                    GlobalConstants.value_mode_filter_farm;

            return false;
        }
        return false;
    }

    private void done() {

        Intent in = new Intent();

        if (option == ContentCommon.INVALID_OPTION) {
            option = ContentCommon.ADD_CAMERA;
        }


        in.putExtra(ContentCommon.CAMERA_OPTION, option);
        in.putExtra(ContentCommon.STR_CAMERA_ID, SystemValue.deviceId);
        in.putExtra(ContentCommon.STR_CAMERA_USER, SystemValue.deviceName);
        in.putExtra(ContentCommon.STR_CAMERA_PWD, SystemValue.devicePass);
        in.putExtra(ContentCommon.STR_CAMERA_TYPE, CameraType);

        //SystemValue.deviceName = mUserName;
        //SystemValue.deviceId = strDID;
        //SystemValue.devicePass = strPwd;
        //SystemValue.devicePass = strPwd;

        BridgeService.setIpcamClientInterface(this);
        NativeCaller.Init();

        LogUtils.e(TAG, "添加数据");

        new Thread(new StartPPPPThread()).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * BridgeService callback
     **/
    @Override
    public void callBackSearchResultData(int sysver, String strMac,
                                         String strName, String strDeviceID,
                                         String strIpAddr, int port) {
        Log.e("AddCameraActivity", strDeviceID + strName);
        if (!listAdapter.AddCamera(strMac, strName, strDeviceID)) {
            return;
        }
    }

    public String getInfoSSID() {

        info = manager.getConnectionInfo();
        String ssid = info.getSSID();
        return ssid;
    }

    public int getInfoIp() {

        info = manager.getConnectionInfo();
        int ip = info.getIpAddress();
        return ip;
    }

    @SuppressLint("HandlerLeak")
    private Handler PPPPMsgHandler = new Handler() {
        public void handleMessage(Message msg) {

            Bundle bd = msg.getData();
            int msgParam = bd.getInt(STR_MSG_PARAM);
            int msgType = msg.what;
            Log.i("aaa", "====" + msgType + "--msgParam:" + msgParam);
            String did = bd.getString(STR_DID);
            switch (msgType) {
                case ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS:
                    int resid;
                    switch (msgParam) {
                        case ContentCommon.PPPP_STATUS_CONNECTING://0
                            resid = R.string.pppp_status_connecting;
                            LogUtils.e("摄像头状态","正在连接");
                            tag = 2;
                            break;
                        case ContentCommon.PPPP_STATUS_CONNECT_FAILED://3
                            resid = R.string.pppp_status_connect_failed;
                            LogUtils.e("摄像头状态","链接失败");
                            tag = 0;
                            break;
                        case ContentCommon.PPPP_STATUS_DISCONNECT://4
                            resid = R.string.pppp_status_disconnect;
                            LogUtils.e("摄像头状态","断线");
                            tag = 0;
                            break;
                        case ContentCommon.PPPP_STATUS_INITIALING://1
                            resid = R.string.pppp_status_initialing;
                            LogUtils.e("摄像头状态","初始化");
                            tag = 2;
                            break;
                        case ContentCommon.PPPP_STATUS_INVALID_ID://5
                            resid = R.string.pppp_status_invalid_id;
                            LogUtils.e("摄像头状态","ID号无效");
                            tag = 0;
                            break;
                        case ContentCommon.PPPP_STATUS_ON_LINE://2 在线状态
                            resid = R.string.pppp_status_online;
                            LogUtils.e("摄像头状态","在线");
                            //摄像机在线之后读取摄像机类型
                            String cmd = "get_status" +
                                    ".cgi?loginuse=admin&loginpas=" +
                                    SystemValue.devicePass
                                    + "&user=admin&pwd=" + SystemValue
                                    .devicePass;
                            NativeCaller.TransferMessage(did, cmd, 1);

                            tag = 1;

                            //状态链接 可以跳转页面

                            llLoading.setVisibility(View.GONE);

                            //存储这个activity
                            MarkerConstants.activity = AddCameraActivity.this;
                            SPUtils.setString(getApplicationContext(), "orderuuidtoken", SystemValue.uuid);

                            startActivity(new Intent(AddCameraActivity.this,
                                    PlayActivity.class));

                            break;
                        case ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE://6
                            resid = R.string.device_not_on_line;
                            LogUtils.e("摄像头状态","摄像机不在线");
                            ToastUtils.makeTextShow(getApplicationContext(),"摄像机不在线");
                            finish();
                            tag = 0;
                            break;
                        case ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT://7
                            resid = R.string.pppp_status_connect_timeout;
                            LogUtils.e("摄像头状态","链接超市");
                            ToastUtils.makeTextShow(getApplicationContext(),"摄像机连接超时");
                            finish();
                            tag = 0;
                            break;
                        case ContentCommon.PPPP_STATUS_CONNECT_ERRER://8
                            resid = R.string.pppp_status_pwd_error;
                            LogUtils.e("摄像头状态","密码错误");
                            ToastUtils.makeTextShow(getApplicationContext(),"摄像机故障，请联系管理员");
                            finish();
                            tag = 0;
                            break;
                        default:
                            resid = R.string.pppp_status_unknown;
                            LogUtils.e("摄像头状态","位置状态");
                    }

                    if (msgParam == ContentCommon.PPPP_STATUS_ON_LINE) {
                        NativeCaller.PPPPGetSystemParams(did, ContentCommon
                                .MSG_TYPE_GET_PARAMS);
                    }
                    if (msgParam == ContentCommon.PPPP_STATUS_INVALID_ID
                            || msgParam == ContentCommon
                            .PPPP_STATUS_CONNECT_FAILED
                            || msgParam == ContentCommon
                            .PPPP_STATUS_DEVICE_NOT_ON_LINE
                            || msgParam == ContentCommon
                            .PPPP_STATUS_CONNECT_TIMEOUT
                            || msgParam == ContentCommon
                            .PPPP_STATUS_CONNECT_ERRER) {
                        NativeCaller.StopPPPP(did);
                    }
                    break;
                case ContentCommon.PPPP_MSG_TYPE_PPPP_MODE:
                    break;

            }

        }
    };

    @Override
    public void BSMsgNotifyData(String did, int type, int param) {

        Bundle bd = new Bundle();
        Message msg = PPPPMsgHandler.obtainMessage();
        msg.what = type;
        bd.putInt(STR_MSG_PARAM, param);
        bd.putString(STR_DID, did);
        msg.setData(bd);
        PPPPMsgHandler.sendMessage(msg);
        if (type == ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS) {
            intentbrod.putExtra("ifdrop", param);
            sendBroadcast(intentbrod);
        }

    }

    @Override
    public void BSSnapshotNotify(String did, byte[] bImage, int len) {
        // TODO Auto-generated method stub
        Log.i("ip", "BSSnapshotNotify---len" + len);
    }

    @Override
    public void callBackUserParams(String did, String user1, String pwd1,
                                   String user2, String pwd2, String user3,
                                   String pwd3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void CameraStatus(String did, int status) {

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void CallBackGetStatus(String did, String resultPbuf, int cmd) {
        // TODO Auto-generated method stub
        if (cmd == ContentCommon.CGI_IEGET_STATUS) {
            String cameraType = spitValue(resultPbuf, "upnp_status=");
            int intType = Integer.parseInt(cameraType);
            int type14 = (int) (intType >> 16) & 1;// 14位 来判断是否报警联动摄像机
            if (intType == 2147483647) {// 特殊值
                type14 = 0;
            }


        }
    }

    private String spitValue(String name, String tag) {
        String[] strs = name.split(";");
        for (int i = 0; i < strs.length; i++) {
            String str1 = strs[i].trim();
            if (str1.startsWith("var")) {
                str1 = str1.substring(4, str1.length());
            }
            if (str1.startsWith(tag)) {
                String result = str1.substring(str1.indexOf("=") + 1);
                return result;
            }
        }
        return -1 + "";
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

}
