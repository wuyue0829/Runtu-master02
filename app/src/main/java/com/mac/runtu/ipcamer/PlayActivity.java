package com.mac.runtu.ipcamer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mac.runtu.R;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.ipcamer.adapter.ViewPagerAdapter;
import com.mac.runtu.ipcamer.bean.OpenOrClose;
import com.mac.runtu.ipcamer.utils.AudioPlayer;
import com.mac.runtu.ipcamer.utils.ContentCommon;
import com.mac.runtu.ipcamer.utils.CustomAudioRecorder;
import com.mac.runtu.ipcamer.utils.CustomBuffer;
import com.mac.runtu.ipcamer.utils.CustomBufferData;
import com.mac.runtu.ipcamer.utils.CustomBufferHead;
import com.mac.runtu.ipcamer.utils.MyRender;
import com.mac.runtu.ipcamer.utils.SystemValue;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.SPUtils;
import com.skydoves.colorpickerview.ColorListener;
import com.skydoves.colorpickerview.ColorPickerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import hfsdk.light.pro.HFColorLightPro;
import hfsdk.light.pro.Utils;
import socket.hfsdk.spro.HFSocketPro;
import vstc2.nativecaller.NativeCaller;

public class PlayActivity extends AppCompatActivity implements View.OnTouchListener,
        GestureDetector.OnGestureListener, View.OnClickListener,
        BridgeService.PlayInterface, CustomAudioRecorder.AudioRecordResult {

    private static final String LOG_TAG = "PlayActivity";
    private static final int AUDIO_BUFFER_START_CODE = 0xff00ff;
    //surfaceView控件
    private GLSurfaceView playSurface = null;

    //视频数据
    private byte[] videodata     = null;
    private int    videoDataLen  = 0;
    public  int    nVideoWidths  = 0;
    public  int    nVideoHeights = 0;
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_4;
    private TextView tv_5;
    private TextView tv_6;
    private TextView tv_7;
    private TextView tv_8;
    private       View            progressView = null;
    private       boolean         bProgress    = true;
    private       GestureDetector gt           = new GestureDetector(this);
    private final int             BRIGHT       = 1;//亮度标志
    private final int             CONTRAST     = 2;//对比度标志
    private final int             IR_STATE     = 14;//IR(夜视)开关
    private       int             nResolution  = 0;//分辨率值
    private       int             nBrightness  = 0;//亮度值
    private       int             nContrast    = 0;//对比度
    ArrayList<OpenOrClose> mAddresslist;
    private boolean  bInitCameraParam = false;
    private boolean  bManualExit      = false;
    private TextView textosd          = null;
    private String   strName          = null;
    private String   strDID           = null;
    private View     osdView          = null;
    private ImageButton fill;
    private ToggleButton irSwitch;
    private boolean      bDisplayFinished = true;
    private CustomBuffer AudioBuffer      = null;
    private AudioPlayer  audioPlayer      = null;
    private boolean      bAudioStart      = false;
    private ImageView back_Iv;
    private ToggleButton c1_toggle_danse;
    private ToggleButton c1_toggle_qicai;

    private boolean isLeftRight = false;
    private boolean isUpDown    = false;
    private LinearLayout ly_deng;


    private boolean isHorizontalMirror = false;
    private boolean isVerticalMirror   = false;
    private boolean isUpDownPressed    = false;
    private boolean isShowtoping       = false;
    private SeekBar seekBarStr;
    private ImageView            videoViewPortrait;
    private ImageView            videoViewStandard;
    //顶部控件声明
    private HorizontalScrollView bottomView;
    private ImageButton          ptzAudio, ptztalk, ptzDefaultSet,
            ptzBrightness, ptzContrast, ptzTake_photos, ptzTake_vodeo,
            ptzResolutoin, preset;
    private int nStreamCodecType;//分辨率格式

    private ColorPickerView cpv;
    private PopupWindow controlWindow;//设备方向控制提示控件
    private PopupWindow mPopupWindowProgress;//进度条控件
    private PopupWindow presetBitWindow;//预置位面板
    private PopupWindow resolutionPopWindow;//分辨率面板
    //上下左右提示文本
    private TextView    control_item;
    //正在控制设备
    private boolean isControlDevice = false;

    private String stqvga   = "qvga";
    private String stvga    = "vga";
    private String stqvga1  = "qvga1";
    private String stvga1   = "vga1";
    private String stp720   = "p720";
    private String sthigh   = "high";
    private String stmiddle = "middle";
    private String stmax    = "max";

    //预位置设置
    private Button[] btnLeft  = new Button[16];
    private Button[] btnRigth = new Button[16];
    private ViewPager  prePager;
    private List<View> listViews;
    //分辨率标识符
    private boolean ismax    = false;
    private boolean ishigh   = false;
    private boolean isp720   = false;
    private boolean ismiddle = false;
    private boolean isqvga1  = false;
    private boolean isvga1   = false;
    private boolean isqvga   = false;
    private boolean isvga    = false;

    private Animation showAnim;
    private boolean isTakepic    = false;
    private boolean isPictSave   = false;
    private boolean isTalking    = false;//是否在说话
    private boolean isMcriophone = false;//是否在
    //视频录像方法
    public  boolean isH264      = false;//是否是H264格式标志
    public  boolean isJpeg      = false;
    private boolean isTakeVideo = false;
    private long    videotime   = 0;// 录每张图片的时间

    private Animation dismissAnim;
    private int timeTag = 0;
    private int timeOne = 0;
    private int timeTwo = 0;
    private ImageButton button_back;
    private BitmapDrawable drawable          = null;
    private boolean        bAudioRecordStart = false;
    //送话器
    private CustomAudioRecorder customAudioRecorder;

    private MyRender myRender;

    //镜像标志
    private boolean m_bUpDownMirror;
    private boolean m_bLeftRightMirror;


    private int i = 0;//拍照张数标志

    /****
     * 退出确定dialog
     */
    public void showSureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.app);
        builder.setTitle(getResources().getString(R.string.exit));
        builder.setMessage(R.string.exit_alert);
        builder.setPositiveButton(R.string.str_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int
                            whichButton) {
                        // Process.killProcess(Process.myPid());
                        NativeCaller.PPPPPTZControl(strDID, ContentCommon
                                .CMD_PTZ_LEFT_RIGHT_STOP);

                        NativeCaller.PPPPPTZControl(strDID, ContentCommon
                                .CMD_PTZ_UP_DOWN_STOP);

                        Intent intent = new Intent("finish");
                        sendBroadcast(intent);
                        PlayActivity.this.finish();

                        LogUtils.e(LOG_TAG, "视频播放关闭1");
                    }
                });
        builder.setNegativeButton(R.string.str_cancel, null);
        builder.show();
    }

    //显示顶部菜单
    private void showTop() {
        if (isShowtoping) {
            isShowtoping = false;
            topbg.setVisibility(View.GONE);
            topbg.startAnimation(dismissTopAnim);
        } else {
            isShowtoping = true;
            topbg.setVisibility(View.VISIBLE);
            topbg.startAnimation(showTopAnim);
        }
    }

    private Handler deviceParamsHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    m_bUpDownMirror = false;
                    m_bLeftRightMirror = false;

                    ptzVertMirror2.setImageResource(R.drawable.ptz_vert_mirror);
                    ptzHoriMirror2.setImageResource(R.drawable.ptz_hori_mirror);

                    break;
                case 1:
                    m_bUpDownMirror = true;
                    m_bLeftRightMirror = false;

                    ptzHoriMirror2.setImageResource(R.drawable.ptz_hori_mirror);
                    ptzVertMirror2.setImageResource(R.drawable
                            .ptz_vert_mirror_press);

                    break;
                case 2:
                    m_bUpDownMirror = false;
                    m_bLeftRightMirror = true;
                    ptzHoriMirror2.setImageResource(R.drawable
                            .ptz_hori_mirror_press);
                    ptzVertMirror2.setImageResource(R.drawable.ptz_vert_mirror);
                    break;
                case 3:
                    m_bUpDownMirror = true;
                    m_bLeftRightMirror = true;
                    ptzVertMirror2.setImageResource(R.drawable
                            .ptz_vert_mirror_press);
                    ptzHoriMirror2.setImageResource(R.drawable
                            .ptz_hori_mirror_press);
                    break;
                default:
                    break;
            }
        }
    };



    @SuppressLint("HandlerLeak")
    private Handler mHandler1 = new Handler()
    {
        public void handleMessage(Message paramAnonymousMessage)
        {
            super.handleMessage(paramAnonymousMessage);
            switch (paramAnonymousMessage.what) {
                case 1000:
                    ly_deng.removeAllViews();
                    ly_deng.addView(new PlayItem(getApplicationContext(), "Xo3w3s1shs0zXQEvSi6bR4aeIqrm\\/GUCP5m4bYG+Qr7V4E5haw+LzjdaxnrDUrw4", true, "插座", "888888"));
                    break;
            }
        }
    };

    //默认视频参数
    private void defaultVideoParams() {
        nBrightness = 1;
        nContrast = 128;
        NativeCaller.PPPPCameraControl(strDID, 1, 0);
        NativeCaller.PPPPCameraControl(strDID, 2, 128);
        showToast(R.string.ptz_default_vedio_params);
    }

    private void showToast(int i) {
        Toast.makeText(PlayActivity.this, i, Toast.LENGTH_SHORT).show();
    }


    //设置视频可见
    private void setViewVisible() {
        if (bProgress) {
            bProgress = false;
            progressView.setVisibility(View.INVISIBLE);
//            osdView.setVisibility(View.VISIBLE);
            getCameraParams();
        }
    }

    int disPlaywidth;
    private Bitmap mBmp;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == 1 || msg.what == 2) {
                setViewVisible();
            }
            if (!isPTZPrompt) {
                isPTZPrompt = true;

            }
            int width = getWindowManager().getDefaultDisplay().getWidth();
            int height = getWindowManager().getDefaultDisplay().getHeight();
            switch (msg.what) {
                case 1: // h264
                {
                    if (reslutionlist.size() == 0) {
                        if (nResolution == 0) {
                            ismax = true;
                            ismiddle = false;
                            ishigh = false;
                            isp720 = false;
                            isqvga1 = false;
                            isvga1 = false;
                            addReslution(stmax, ismax);
                        } else if (nResolution == 1) {
                            ismax = false;
                            ismiddle = false;
                            ishigh = true;
                            isp720 = false;
                            isqvga1 = false;
                            isvga1 = false;
                            addReslution(sthigh, ishigh);
                        } else if (nResolution == 2) {
                            ismax = false;
                            ismiddle = true;
                            ishigh = false;
                            isp720 = false;
                            isqvga1 = false;
                            isvga1 = false;
                            addReslution(stmiddle, ismiddle);
                        } else if (nResolution == 3) {
                            ismax = false;
                            ismiddle = false;
                            ishigh = false;
                            isp720 = true;
                            isqvga1 = false;
                            isvga1 = false;
                            addReslution(stp720, isp720);
                            nResolution = 3;
                        } else if (nResolution == 4) {
                            ismax = false;
                            ismiddle = false;
                            ishigh = false;
                            isp720 = false;
                            isqvga1 = false;
                            isvga1 = true;
                            addReslution(stvga1, isvga1);
                        } else if (nResolution == 5) {
                            ismax = false;
                            ismiddle = false;
                            ishigh = false;
                            isp720 = false;
                            isqvga1 = true;
                            isvga1 = false;
                            addReslution(stqvga1, isqvga1);
                        }
                    } else {
                        if (reslutionlist.containsKey(strDID)) {
                            getReslution();
                        } else {
                            if (nResolution == 0) {
                                ismax = true;
                                ismiddle = false;
                                ishigh = false;
                                isp720 = false;
                                isqvga1 = false;
                                isvga1 = false;
                                addReslution(stmax, ismax);
                            } else if (nResolution == 1) {
                                ismax = false;
                                ismiddle = false;
                                ishigh = true;
                                isp720 = false;
                                isqvga1 = false;
                                isvga1 = false;
                                addReslution(sthigh, ishigh);
                            } else if (nResolution == 2) {
                                ismax = false;
                                ismiddle = true;
                                ishigh = false;
                                isp720 = false;
                                isqvga1 = false;
                                isvga1 = false;
                                addReslution(stmiddle, ismiddle);
                            } else if (nResolution == 3) {
                                ismax = false;
                                ismiddle = false;
                                ishigh = false;
                                isp720 = true;
                                isqvga1 = false;
                                isvga1 = false;
                                addReslution(stp720, isp720);
                                nResolution = 3;
                            } else if (nResolution == 4) {
                                ismax = false;
                                ismiddle = false;
                                ishigh = false;
                                isp720 = false;
                                isqvga1 = false;
                                isvga1 = true;
                                addReslution(stvga1, isvga1);
                            } else if (nResolution == 5) {
                                ismax = false;
                                ismiddle = false;
                                ishigh = false;
                                isp720 = false;
                                isqvga1 = true;
                                isvga1 = false;
                                addReslution(stqvga1, isqvga1);
                            }
                        }

                    }

                    if (getResources().getConfiguration().orientation ==
                            Configuration.ORIENTATION_PORTRAIT) {
                        FrameLayout.LayoutParams lp = new FrameLayout
                                .LayoutParams(
                                width, width * 3 / 4);
                        lp.gravity = Gravity.CENTER;
                        playSurface.setLayoutParams(lp);
                    } else if (getResources().getConfiguration().orientation
                            == Configuration.ORIENTATION_LANDSCAPE) {
                        FrameLayout.LayoutParams lp = new FrameLayout
                                .LayoutParams(
                                width, height);
                        lp.gravity = Gravity.CENTER;
                        playSurface.setLayoutParams(lp);
                    }
                    myRender.writeSample(videodata, nVideoWidths,
                            nVideoHeights);
                }
                break;
                case 2: // JPEG
                {
                    if (reslutionlist.size() == 0) {
                        if (nResolution == 1) {
                            isvga = true;
                            isqvga = false;
                            addReslution(stvga, isvga);
                        } else if (nResolution == 0) {
                            isqvga = true;
                            isvga = false;
                            addReslution(stqvga, isqvga);
                        }
                    } else {
                        if (reslutionlist.containsKey(strDID)) {
                            getReslution();
                        } else {
                            if (nResolution == 1) {
                                isvga = true;
                                isqvga = false;
                                addReslution(stvga, isvga);
                            } else if (nResolution == 0) {
                                isqvga = true;
                                isvga = false;
                                addReslution(stqvga, isqvga);
                            }
                        }
                    }
                    mBmp = BitmapFactory.decodeByteArray(videodata, 0,
                            videoDataLen);
                    if (mBmp == null) {
                        bDisplayFinished = true;
                        return;
                    }
                    if (isTakepic) {
                        takePicture(mBmp);
                        isTakepic = false;
                    }
                    nVideoWidths = mBmp.getWidth();
                    nVideoHeights = mBmp.getHeight();

                    if (getResources().getConfiguration().orientation ==
                            Configuration.ORIENTATION_PORTRAIT) {
                        // Bitmap
                        Bitmap bitmap = Bitmap.createScaledBitmap(mBmp, width,
                                width * 3 / 4, true);
                        //videoViewLandscape.setVisibility(View.GONE);
                        videoViewPortrait.setVisibility(View.VISIBLE);
                        videoViewPortrait.setImageBitmap(bitmap);

                    } else if (getResources().getConfiguration().orientation
                            == Configuration.ORIENTATION_LANDSCAPE) {
                        Bitmap bitmap = Bitmap.createScaledBitmap(mBmp,
                                width, height, true);
                        videoViewPortrait.setVisibility(View.GONE);
                        //videoViewLandscape.setVisibility(View.VISIBLE);
                        //videoViewLandscape.setImageBitmap(bitmap);
                    }

                }
                break;
                default:
                    break;
            }
            if (msg.what == 1 || msg.what == 2) {
                bDisplayFinished = true;
            }
        }

    };

    private void getCameraParams() {

        NativeCaller.PPPPGetSystemParams(strDID,
                ContentCommon.MSG_TYPE_GET_CAMERA_PARAMS);
    }

    private Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.d("tag", "断线了");
                Toast.makeText(getApplicationContext(),
                        R.string.pppp_status_disconnect, Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_play1);

        strName = SystemValue.deviceName;
        strDID = SystemValue.deviceId;

        disPlaywidth = getWindowManager().getDefaultDisplay().getWidth();

        findView();
        AudioBuffer = new CustomBuffer();
        audioPlayer = new AudioPlayer(AudioBuffer);
        customAudioRecorder = new CustomAudioRecorder(this);
        BridgeService.setPlayInterface(this);

        NativeCaller.StartPPPPLivestream(strDID, 10, 1);//确保不能重复start

        getCameraParams();
        dismissTopAnim = AnimationUtils.loadAnimation(this,
                R.anim.ptz_top_anim_dismiss);
        showTopAnim = AnimationUtils.loadAnimation(this,
                R.anim.ptz_top_anim_show);
        showAnim = AnimationUtils.loadAnimation(this,
                R.anim.ptz_otherset_anim_show);
        dismissAnim = AnimationUtils.loadAnimation(this,
                R.anim.ptz_otherset_anim_dismiss);

        myRender = new MyRender(playSurface);
        playSurface.setRenderer(myRender);
        MyHttpUtils.getStringDataFromNet("http://101.201.102.161/app/Equipment/queryEquipmentInfo.action", null, "uuid", SPUtils.getString(getApplicationContext(), "orderuuidtoken", ""), new MyHttpUtils.OnNewWorkRequestListener()
        {
            public void onNewWorkError(String paramAnonymousString)
            {
               mHandler1.sendEmptyMessage(2000);
            }

            public void onNewWorkSuccess(String paramAnonymousString)
            {
                if (paramAnonymousString != null)
                {
                    LogUtils.e("返回的数据是",paramAnonymousString);

                    mHandler1.sendEmptyMessage(1000);   
                    return;
                }
                mHandler1.sendEmptyMessage(2000);
            }
        });




    }


    private void OpenDeng()
    {
        String str = "pBICtpwMOzTSfW7O2qde\\/PcS9MCS2tcpquY47Jx8Itw3gcwfRtppWFXvpHqVEKbD";
        getSendOrder("HFCLPROOPEN", str, "WAN", "888888");
    }

    private void CloseDeng()
    {
        String str = "pBICtpwMOzTSfW7O2qde\\/PcS9MCS2tcpquY47Jx8Itw3gcwfRtppWFXvpHqVEKbD";
        getSendOrder("HFCLPROCLOSE", str, "WAN", "888888");
    }

    private void qicai()
    {
        String str = "pBICtpwMOzTSfW7O2qde\\/PcS9MCS2tcpquY47Jx8Itw3gcwfRtppWFXvpHqVEKbD";
        getSendOrder("HFCLPROMODE=FFFFFF000099,02,03", str, "WAN", "888888");
    }

    private void Danse()
    {
        String str = "pBICtpwMOzTSfW7O2qde\\/PcS9MCS2tcpquY47Jx8Itw3gcwfRtppWFXvpHqVEKbD";
        getSendOrder("HFCLPROMODE=FFFFFF000099,01,03", str, "WAN", "888888");
    }


    private String tiaojielight(String paramString)
    {
        String str = "HFCLPRORGBW=FFCCAA0000" + paramString;
        paramString = "pBICtpwMOzTSfW7O2qde\\/PcS9MCS2tcpquY47Jx8Itw3gcwfRtppWFXvpHqVEKbD";
        getSendOrder(str, paramString, "WAN", "888888");
        return "";
    }


    /**
     * 根据设置的信息，获取徐发送的指令
     * @return 需发送的指令
     */
    private String getSendOrder(String arg0,String arg1,String arg2,String arg3){
        String result = "";
        if(arg0.equals("HFCLPROSTATE")){//获取状态
            result = HFColorLightPro.sendHFCLPROStateOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFCLPROOPEN")){//打开设备
            result = HFColorLightPro.sendHFCLPROOpenOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFCLPROCLOSE")){//关闭设备
            result = HFColorLightPro.sendHFCLPROCloseOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.equals("HFCLPROTIMETASK")){//获取定时任务信息
            result = HFColorLightPro.getHFCLPROTimeTaskOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.split("=").length == 2 && arg0.split("=")[0].equals("HFCLPROTIMETASK")){//设置定时任务
            result = HFColorLightPro.setHFCLPROTimeTaskOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.equals("HFCLPROTIMESHUT")){//获取延时
            result = HFColorLightPro.getHFCLPROTimeShutOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.split("=").length == 2 && arg0.split("=")[0].equals("HFCLPROTIMESHUT")){//获取延时
            result = HFColorLightPro.setHFCLPROTimeShutOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.length()>14 && arg0.subSequence(0, 15).equals("HFCLPROTIMESHUT=")){//设置延时
            result = HFColorLightPro.setHFCLPROTimeShutOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.equals("HFCLPROTIME")){//获取设备的时间
            result = HFColorLightPro.getHFCLPROTimeOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.length()>10 && arg0.subSequence(0, 11).equals("HFCLPROTIME=")){//设备校时
            result = HFColorLightPro.setHFCLPROTimeOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.length()>12 && arg0.split("=")[0].equals("HFCLPROCMPPWD")){//密码判断
            result = HFColorLightPro.sendHFCLPROCmpPwdOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.split("=")[0].equals("HFCLPROUPDATEPWD")){//修改密码
            result = HFColorLightPro.sendHFCLPROUpdatePwdOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.split("=")[0].equals("HFCLPROUPDATE")){//固件更新
            result = HFColorLightPro.sendHFCLPROUpdateOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFCLPROV")){//获取设备固件版本信息
            result = HFColorLightPro.getHFCLPROVOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.equals("HFCLPRODFT")){//恢复出厂
            result = HFColorLightPro.sendHFCLPRODFTOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFCLPRORESET")){//设备重启
            result = HFColorLightPro.sendHFCLPROResetOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFREQUESTTIMEZONE")){
            result = Utils.getHFDEVTimeZoneOrder(arg0, arg1);
        }else if(arg0.split("=").length == 2 && arg0.split("=")[0].equals("HFPUSHTIMEZONE")){
            result = Utils.setHFDEVTimeZoneOrder(arg0, arg1);
        }else if(arg0.equals("HFCLPRORSSI")){
            result = HFColorLightPro.getHFCLPRORssiOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFCLPROAUTODFT")){
            result = HFColorLightPro.getHFCLPROAutoDftOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.split("=").length == 2 && arg0.split("=")[0].equals("HFCLPROAUTODFT")){
            result = HFColorLightPro.setHFCLPROAutoDftOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFCLPRORGBW")){
            result = HFColorLightPro.getHFCLPRORGBWOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.split("=").length == 2 && arg0.split("=")[0].equals("HFCLPRORGBW")){
            result = HFColorLightPro.setHFCLPRORGBWOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.split("=").length == 2 && arg0.split("=")[0].equals("HFCLPROMODE")){
            result = HFColorLightPro.setHFCLPROModeOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFSERVTIME")){
            result = Utils.sendHFServTimeOrder();
        }else if(arg0.equals("HFLINK")){
            result = Utils.sendHFServLinkOrder(arg1);
        }
        return result;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPopupWindowProgress != null && mPopupWindowProgress.isShowing()) {
            mPopupWindowProgress.dismiss();
        }
        if (resolutionPopWindow != null && resolutionPopWindow.isShowing()) {
            resolutionPopWindow.dismiss();
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!bProgress) {
                Date date = new Date();
                if (timeTag == 0) {
                    timeOne = date.getSeconds();
                    timeTag = 1;
                    showToast(R.string.main_show_back1);

                } else if (timeTag == 1) {
                    timeTwo = date.getSeconds();
                    if (timeTwo - timeOne <= 3) {
                        Intent intent = new Intent("finish");
                        sendBroadcast(intent);
                        PlayActivity.this.finish();
                        timeTag = 0;
                    } else {
                        timeTag = 1;
                        showToast(R.string.main_show_back1);


                    }
                }
            } else {
                showSureDialog();
            }

            NativeCaller.PPPPPTZControl(strDID, ContentCommon
                    .CMD_PTZ_LEFT_RIGHT_STOP);

            NativeCaller.PPPPPTZControl(strDID, ContentCommon
                    .CMD_PTZ_UP_DOWN_STOP);

            LogUtils.e(LOG_TAG, "视频播放关闭2");

            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (!bProgress) {
                showTop();
                showBottom();
            } else {
                showSureDialog();
            }
        }

        NativeCaller.PPPPPTZControl(strDID, ContentCommon
                .CMD_PTZ_LEFT_RIGHT_STOP);

        NativeCaller.PPPPPTZControl(strDID, ContentCommon
                .CMD_PTZ_UP_DOWN_STOP);

        LogUtils.e(LOG_TAG, "视频播放关闭2");

        return super.onKeyDown(keyCode, event);
    }

    protected void setResolution(int Resolution) {
        Log.d("tag", "setResolution resolution:" + Resolution);
        NativeCaller.PPPPCameraControl(strDID, 16, Resolution);
    }

    private void findView() {
        //方向控制提示框
        initControlDailog();
        //视频渲染画面控件
        playSurface = (GLSurfaceView) findViewById(R.id.mysurfaceview);
        playSurface.setOnTouchListener(this);
        playSurface.setLongClickable(true);//确保手势识别正确工作
        c1_toggle_danse = (ToggleButton) findViewById(R.id.c1_toggle_danse);
        c1_toggle_qicai = (ToggleButton) findViewById(R.id.c1_toggle_qicai);
        button_back = (ImageButton) findViewById(R.id.login_top_back);
        ly_deng = (LinearLayout) findViewById(R.id.ly_deng);
        button_back.setOnClickListener(this);
        videoViewPortrait = (ImageView) findViewById(R.id.vedioview);
        videoViewStandard = (ImageView) findViewById(R.id.vedioview_standard);

        progressView = (View) findViewById(R.id.progressLayout);
        //顶部菜单
        topbg = (RelativeLayout) findViewById(R.id.top_bg);
        osdView = (View) findViewById(R.id.osdlayout);
        irSwitch = (ToggleButton) findViewById(R.id.ir_switch);
        fill = (ImageButton) findViewById(R.id.fill);
        irSwitch.setOnClickListener(this);
        fill.setOnClickListener(this);

        //显示设备名称
        textosd = (TextView) findViewById(R.id.textosd);
        textosd.setText(strName);
        //textosd.setVisibility(View.VISIBLE);
        cpv = ((ColorPickerView)findViewById(R.id.cpv));
        ptzHoriMirror2 = (ImageButton) findViewById(R.id.ptz_hori_mirror);
        ptzVertMirror2 = (ImageButton) findViewById(R.id.ptz_vert_mirror);
        ptzHoriTour2 = (ImageButton) findViewById(R.id.ptz_hori_tour);
        ptzVertTour2 = (ImageButton) findViewById(R.id.ptz_vert_tour);
        back_Iv = (ImageView) findViewById(R.id.back_Iv);
        back_Iv.setOnClickListener(this);
        ptzHoriMirror2.setOnClickListener(this);
        ptzVertMirror2.setOnClickListener(this);
        ptzHoriTour2.setOnClickListener(this);
        ptzVertTour2.setOnClickListener(this);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_6 = (TextView) findViewById(R.id.tv_6);
        tv_7 = (TextView) findViewById(R.id.tv_7);
        tv_8 = (TextView) findViewById(R.id.tv_8);
        seekBarStr = (SeekBar) findViewById(R.id.seekBar);
        //底部菜单
        bottomView = (HorizontalScrollView) findViewById(R.id.bottom_view);

        ptztalk = (ImageButton) findViewById(R.id.ptz_talk);
        ptzAudio = (ImageButton) findViewById(R.id.ptz_audio);
        ptzTake_photos = (ImageButton) findViewById(R.id.ptz_take_photos);
        ptzTake_vodeo = (ImageButton) findViewById(R.id.ptz_take_videos);
        ptzDefaultSet = (ImageButton) findViewById(R.id.ptz_default_set);
        ptzBrightness = (ImageButton) findViewById(R.id.ptz_brightness);
        ptzContrast = (ImageButton) findViewById(R.id.ptz_contrast);
        ptzResolutoin = (ImageButton) findViewById(R.id.ptz_resolution);
        preset = (ImageButton) findViewById(R.id.preset);

        ptztalk.setOnClickListener(this);
        ptzAudio.setOnClickListener(this);
        ptzTake_photos.setOnClickListener(this);
        ptzTake_vodeo.setOnClickListener(this);
        ptzBrightness.setOnClickListener(this);
        ptzContrast.setOnClickListener(this);
        ptzResolutoin.setOnClickListener(this);
        ptzDefaultSet.setOnClickListener(this);
        preset.setOnClickListener(this);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.top_bg);
        drawable = new BitmapDrawable(bitmap);
        drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        drawable.setDither(true);

        topbg.setBackgroundDrawable(drawable);
        bottomView.setBackgroundDrawable(drawable);

        seekBarStr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {}

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {}

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
            {
                tiaojielight(paramAnonymousSeekBar.getProgress() + "");
            }
        });

        c1_toggle_danse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Danse();
                }else{
                    CloseDeng();
                }

            }
        });


        c1_toggle_qicai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    qicai();
                }else{
                    CloseDeng();
                }

            }
        });

        cpv.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(int color) {
                String str = "#" + PlayActivity.toBrowserHexValue(color) + PlayActivity.toBrowserHexValue(color) + PlayActivity.toBrowserHexValue(color);
                chooseColor(str);
            }

        });

        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseColor("fe0000");
            }
        });
        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseColor("00ff01");
            }
        });
        tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseColor("1512ff");
            }
        });
        tv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseColor("ffff01");
            }
        });
        tv_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseColor("ff00ff");
            }
        });
        tv_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseColor("ffffff");
            }
        });
        tv_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseColor("fdfda5");
            }
        });
        tv_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseColor("01ffff");
            }
        });
    }


    private static String toBrowserHexValue(int paramInt)
    {
        StringBuilder localStringBuilder = new StringBuilder(Integer.toHexString(paramInt & 0xFF));
        while (localStringBuilder.length() < 2) {
            localStringBuilder.append("0");
        }
        return localStringBuilder.toString().toUpperCase();
    }


    private void chooseColor(String paramString)
    {
        String str = "HFCLPRORGBW=" + paramString + "000099";
        paramString = "pBICtpwMOzTSfW7O2qde\\/PcS9MCS2tcpquY47Jx8Itw3gcwfRtppWFXvpHqVEKbD";
        getSendOrder(str, paramString, "WAN", "888888");
    }

    private boolean isDown       = false;
    private boolean isSecondDown = false;
    private float   x1           = 0;
    private float   x2           = 0;
    private float   y1           = 0;
    private float   y2           = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (!isDown) {
            x1 = event.getX();
            y1 = event.getY();
            isDown = true;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                originalScale = getScale();
                break;
            case MotionEvent.ACTION_POINTER_UP:

                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs((x1 - x2)) < 25 && Math.abs((y1 - y2)) < 25) {

                    if (resolutionPopWindow != null
                            && resolutionPopWindow.isShowing()) {
                        resolutionPopWindow.dismiss();
                    }

                    if (mPopupWindowProgress != null
                            && mPopupWindowProgress.isShowing()) {
                        mPopupWindowProgress.dismiss();
                    }
                    if (!isSecondDown) {
                        if (!bProgress) {
                            showTop();
                            showBottom();
                        }
                    }
                    isSecondDown = false;
                }
                x1 = 0;
                x2 = 0;
                y1 = 0;
                y2 = 0;
                isDown = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                isSecondDown = true;
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                x2 = event.getX();
                y2 = event.getY();

                if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 0f) {

                    }
                }
        }

        return gt.onTouchEvent(event);
    }

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    private int mode = NONE;
    private float oldDist;
    private Matrix matrix      = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private PointF start       = new PointF();
    private PointF mid         = new PointF();
    float mMaxZoom = 2.0f;
    float mMinZoom = 0.3125f;
    float originalScale;
    float baseValue;
    protected     Matrix  mBaseMatrix    = new Matrix();
    protected     Matrix  mSuppMatrix    = new Matrix();
    private       Matrix  mDisplayMatrix = new Matrix();
    private final float[] mMatrixValues  = new float[9];

    protected void zoomTo(float scale, float centerX, float centerY) {
        Log.d("zoomTo", "zoomTo scale:" + scale);
        if (scale > mMaxZoom) {
            scale = mMaxZoom;
        } else if (scale < mMinZoom) {
            scale = mMinZoom;
        }

        float oldScale = getScale();
        float deltaScale = scale / oldScale;
        Log.d("deltaScale", "deltaScale:" + deltaScale);
        mSuppMatrix.postScale(deltaScale, deltaScale, centerX, centerY);
        videoViewStandard.setScaleType(ImageView.ScaleType.MATRIX);
        videoViewStandard.setImageMatrix(getImageViewMatrix());
    }

    protected Matrix getImageViewMatrix() {
        mDisplayMatrix.set(mBaseMatrix);
        mDisplayMatrix.postConcat(mSuppMatrix);
        return mDisplayMatrix;
    }

    protected float getScale(Matrix matrix) {
        return getValue(matrix, Matrix.MSCALE_X);
    }

    protected float getScale() {
        return getScale(mSuppMatrix);
    }

    protected float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    private float spacing(MotionEvent event) {
        try {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        } catch (Exception e) {
        }
        return 0;
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("tag", "onDown");

        return false;
    }

    private final int MINLEN = 80;//最小间距
    private RelativeLayout topbg;
    private Animation      showTopAnim;
    private Animation      dismissTopAnim;
    private ImageButton    ptzHoriMirror2;
    private ImageButton    ptzVertMirror2;
    private ImageButton    ptzHoriTour2;
    private ImageButton    ptzVertTour2;
    private boolean        isPTZPrompt;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        /*float x1 = e1.getX();
        float x2 = e2.getX();
        float y1 = e1.getY();
        float y2 = e2.getY();

        float xx = x1 > x2 ? x1 - x2 : x2 - x1;
        float yy = y1 > y2 ? y1 - y2 : y2 - y1;

        if (xx > yy) {
            if ((x1 > x2) && (xx > MINLEN)) {// right
                if (!isControlDevice)
                    new ControlDeviceTask(ContentCommon.CMD_PTZ_RIGHT)
                            .execute();

            } else if ((x1 < x2) && (xx > MINLEN)) {// left
                if (!isControlDevice)
                    new ControlDeviceTask(ContentCommon.CMD_PTZ_LEFT).execute();
            }

        } else {
            if ((y1 > y2) && (yy > MINLEN)) {// down
                if (!isControlDevice)
                    new ControlDeviceTask(ContentCommon.CMD_PTZ_DOWN).execute();
            } else if ((y1 < y2) && (yy > MINLEN)) {// up
                if (!isControlDevice)
                    new ControlDeviceTask(ContentCommon.CMD_PTZ_UP).execute();
            }

        }*/
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }


    public void showSureDialogPlay() {

        NativeCaller.PPPPPTZControl(strDID, ContentCommon
                .CMD_PTZ_LEFT_RIGHT_STOP);

        NativeCaller.PPPPPTZControl(strDID, ContentCommon
                .CMD_PTZ_UP_DOWN_STOP);

        LogUtils.e(LOG_TAG, "弹出退出框");


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.app);
        builder.setTitle(getResources().getString(R.string.exit_show));
        builder.setMessage(R.string.exit_play_show);
        builder.setPositiveButton(R.string.str_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int
                            whichButton) {


                        if (MarkerConstants.activity != null) {
                            MarkerConstants.activity.finish();
                        }

                        PlayActivity.this.finish();
                    }
                });
        builder.setNegativeButton(R.string.str_cancel, null);
        builder.show();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_top_back:
                bManualExit = true;

                NativeCaller.PPPPPTZControl(strDID, ContentCommon
                        .CMD_PTZ_LEFT_RIGHT_STOP);

                NativeCaller.PPPPPTZControl(strDID, ContentCommon
                        .CMD_PTZ_UP_DOWN_STOP);

                LogUtils.e(LOG_TAG, "点击了退出按钮");

                if (!bProgress) {
                    if (isTakeVideo == true) {
                        showToast(R.string.eixt_show_toast);
                    } else {
                        showSureDialogPlay();
                    }
                }


                break;
            case R.id.ptz_hori_mirror:
                int value1;

                if (m_bLeftRightMirror) {
                    ptzHoriMirror2.setImageResource(R.drawable.ptz_hori_mirror);
                    if (m_bUpDownMirror) {
                        value1 = 1;
                    } else {
                        value1 = 0;
                    }
                } else {
                    ptzHoriMirror2
                            .setImageResource(R.drawable.ptz_hori_mirror_press);
                    if (m_bUpDownMirror) {
                        value1 = 3;
                    } else {
                        value1 = 2;
                    }
                }

                NativeCaller.PPPPCameraControl(strDID, 5, value1);
                m_bLeftRightMirror = !m_bLeftRightMirror;
                break;
            case R.id.ptz_vert_mirror:
                int value;
                if (m_bUpDownMirror) {
                    ptzVertMirror2.setImageResource(R.drawable.ptz_vert_mirror);
                    if (m_bLeftRightMirror) {
                        value = 2;
                    } else {
                        value = 0;
                    }
                } else {
                    ptzVertMirror2.setImageResource(R.drawable
                            .ptz_vert_mirror_press);
                    if (m_bLeftRightMirror) {
                        value = 3;
                    } else {
                        value = 1;
                    }
                }
                NativeCaller.PPPPCameraControl(strDID, 5, value);
                m_bUpDownMirror = !m_bUpDownMirror;
                break;
            case R.id.ptz_hori_tour:
                if (isLeftRight) {
                    ptzHoriTour2.setBackgroundColor(0x000044aa);
                    isLeftRight = false;
                    NativeCaller.PPPPPTZControl(strDID, ContentCommon
                            .CMD_PTZ_LEFT_RIGHT_STOP);
                } else {
                    ptzHoriTour2.setBackgroundColor(0xff0044aa);
                    isLeftRight = true;
                    NativeCaller.PPPPPTZControl(strDID, ContentCommon
                            .CMD_PTZ_LEFT_RIGHT);
                }
                break;
            case R.id.ptz_vert_tour:

                if (isUpDown) {
                    ptzVertTour2.setBackgroundColor(0x000044aa);
                    isUpDown = false;
                    NativeCaller.PPPPPTZControl(strDID, ContentCommon
                            .CMD_PTZ_UP_DOWN_STOP);
                } else {
                    ptzVertTour2.setBackgroundColor(0xff0044aa);
                    isUpDown = true;
                    NativeCaller.PPPPPTZControl(strDID, ContentCommon
                            .CMD_PTZ_UP_DOWN);
                }
                break;
            case R.id.ptz_talk://对讲
                goMicroPhone();
                break;
            case R.id.ptz_take_videos://录像
                goTakeVideo();
                break;
            case R.id.ptz_take_photos://拍照
                dismissBrightAndContrastProgress();
                if (existSdcard()) {// 判断sd卡是否存在
                    //takePicture(mBmp);
                    isTakepic = true;
                } else {
                    showToast(R.string.ptz_takepic_save_fail);
                }
                break;
            case R.id.ptz_audio:
                goAudio();
                break;
            case R.id.ptz_brightness:
                if (mPopupWindowProgress != null
                        && mPopupWindowProgress.isShowing()) {
                    mPopupWindowProgress.dismiss();
                    mPopupWindowProgress = null;
                }
                setBrightOrContrast(BRIGHT);
                break;
            case R.id.ptz_contrast:
                if (mPopupWindowProgress != null
                        && mPopupWindowProgress.isShowing()) {
                    mPopupWindowProgress.dismiss();
                    mPopupWindowProgress = null;
                }
                setBrightOrContrast(CONTRAST);
                break;
            case R.id.ptz_resolution:
                showResolutionPopWindow();
                break;
            case R.id.preset://预置位设置
                presetBitWindow();
                break;


            case R.id.ptz_resolution_jpeg_qvga:
                dismissBrightAndContrastProgress();
                resolutionPopWindow.dismiss();
                nResolution = 1;
                setResolution(nResolution);
                Log.d("tag", "jpeg resolution:" + nResolution + " qvga");
                break;

            case R.id.ptz_resolution_jpeg_vga:

                dismissBrightAndContrastProgress();
                resolutionPopWindow.dismiss();
                nResolution = 0;
                setResolution(nResolution);
                Log.d("tag", "jpeg resolution:" + nResolution + " vga");
                break;
            case R.id.ptz_resolution_h264_qvga:

                dismissBrightAndContrastProgress();
                resolutionPopWindow.dismiss();
                ismax = false;
                ismiddle = false;
                ishigh = false;
                isp720 = false;
                isqvga1 = true;
                isvga1 = false;
                addReslution(stqvga1, isqvga1);
                nResolution = 5;
                setResolution(nResolution);
                break;
            case R.id.ptz_resolution_h264_vga:
                dismissBrightAndContrastProgress();
                resolutionPopWindow.dismiss();
                ismax = false;
                ismiddle = false;
                ishigh = false;
                isp720 = false;
                isqvga1 = false;
                isvga1 = true;
                addReslution(stvga1, isvga1);
                nResolution = 4;
                setResolution(nResolution);

                break;
            case R.id.ptz_resolution_h264_720p:
                dismissBrightAndContrastProgress();
                resolutionPopWindow.dismiss();
                ismax = false;
                ismiddle = false;
                ishigh = false;
                isp720 = true;
                isqvga1 = false;
                isvga1 = false;
                addReslution(stp720, isp720);
                nResolution = 3;
                setResolution(nResolution);
                break;
            case R.id.ptz_resolution_h264_middle:
                dismissBrightAndContrastProgress();
                resolutionPopWindow.dismiss();
                ismax = false;
                ismiddle = true;
                ishigh = false;
                isp720 = false;
                isqvga1 = false;
                isvga1 = false;
                addReslution(stmiddle, ismiddle);
                nResolution = 2;
                setResolution(nResolution);
                break;
            case R.id.ptz_resolution_h264_high:
                dismissBrightAndContrastProgress();
                resolutionPopWindow.dismiss();
                ismax = false;
                ismiddle = false;
                ishigh = true;
                isp720 = false;
                isqvga1 = false;
                isvga1 = false;
                addReslution(sthigh, ishigh);
                nResolution = 1;
                setResolution(nResolution);
                break;
            case R.id.ptz_resolution_h264_max:
                dismissBrightAndContrastProgress();
                resolutionPopWindow.dismiss();
                ismax = true;
                ismiddle = false;
                ishigh = false;
                isp720 = false;
                isqvga1 = false;
                isvga1 = false;
                addReslution(stmax, ismax);
                nResolution = 0;
                setResolution(nResolution);
                break;

            case R.id.ptz_default_set:
                dismissBrightAndContrastProgress();
                defaultVideoParams();
                break;
            case R.id.ir_switch:

                if (irSwitch.isChecked()) {
                    NativeCaller.PPPPCameraControl(strDID, IR_STATE, 1);
                    Toast.makeText(PlayActivity.this, "IR开", Toast
                            .LENGTH_SHORT).show();

                } else {
                    NativeCaller.PPPPCameraControl(strDID, IR_STATE, 0);
                    Toast.makeText(PlayActivity.this, "IR关", Toast
                            .LENGTH_SHORT).show();
                }
                break;
            case R.id.back_Iv:
                Intent intent = new Intent("finish");
                sendBroadcast(intent);
                PlayActivity.this.finish();
                break;

        }
    }

    private void dismissBrightAndContrastProgress() {
        if (mPopupWindowProgress != null && mPopupWindowProgress.isShowing()) {
            mPopupWindowProgress.dismiss();
            mPopupWindowProgress = null;
        }
    }

    private void showBottom() {
        if (isUpDownPressed) {
            isUpDownPressed = false;
            bottomView.startAnimation(dismissAnim);
            bottomView.setVisibility(View.GONE);
        } else {
            isUpDownPressed = true;
            bottomView.startAnimation(showAnim);
            bottomView.setVisibility(View.GONE);
        }
    }

    /*
     *异步控制方向
     */
    private class ControlDeviceTask extends AsyncTask<Void, Void, Integer> {
        private int type;

        public ControlDeviceTask(int type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if (type == ContentCommon.CMD_PTZ_RIGHT) {
                control_item.setText(R.string.right);
            } else if (type == ContentCommon.CMD_PTZ_LEFT) {
                control_item.setText(R.string.left);
            } else if (type == ContentCommon.CMD_PTZ_UP) {
                control_item.setText(R.string.up);
            } else if (type == ContentCommon.CMD_PTZ_DOWN) {
                control_item.setText(R.string.down);
            }
            if (controlWindow != null && controlWindow.isShowing())
                controlWindow.dismiss();

            if (controlWindow != null && !controlWindow.isShowing())
                controlWindow.showAtLocation(playSurface, Gravity.CENTER, 0, 0);
        }

        @Override
        protected Integer doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            isControlDevice = true;
            if (type == ContentCommon.CMD_PTZ_RIGHT) {
                NativeCaller.PPPPPTZControl(strDID, ContentCommon
                        .CMD_PTZ_RIGHT);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NativeCaller.PPPPPTZControl(strDID, ContentCommon
                        .CMD_PTZ_RIGHT_STOP);
            } else if (type == ContentCommon.CMD_PTZ_LEFT) {
                NativeCaller.PPPPPTZControl(strDID, ContentCommon.CMD_PTZ_LEFT);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NativeCaller.PPPPPTZControl(strDID, ContentCommon
                        .CMD_PTZ_LEFT_STOP);
            } else if (type == ContentCommon.CMD_PTZ_UP) {
                NativeCaller.PPPPPTZControl(strDID, ContentCommon.CMD_PTZ_UP);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NativeCaller.PPPPPTZControl(strDID, ContentCommon
                        .CMD_PTZ_UP_STOP);
            } else if (type == ContentCommon.CMD_PTZ_DOWN) {
                NativeCaller.PPPPPTZControl(strDID, ContentCommon.CMD_PTZ_DOWN);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NativeCaller.PPPPPTZControl(strDID, ContentCommon
                        .CMD_PTZ_DOWN_STOP);
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            isControlDevice = false;
            if (controlWindow != null && controlWindow.isShowing())
                controlWindow.dismiss();
        }

    }

    /*
     * 上下左右提示框
     */
    private void initControlDailog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.control_device_view, null);
        control_item = (TextView) view.findViewById(R.id.textView1_play);
        controlWindow = new PopupWindow(view, LinearLayout.LayoutParams
                .WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        controlWindow.setBackgroundDrawable(new ColorDrawable(0));
        controlWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                controlWindow.dismiss();
            }
        });
        controlWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    controlWindow.dismiss();
                }
                return false;
            }
        });
    }

    /*
     * 16个预置位设置面板
     */
    private void presetBitWindow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.preset_view, null);
        initViewPager(view);
        presetBitWindow = new PopupWindow(view, LinearLayout.LayoutParams
                .WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        presetBitWindow.setAnimationStyle(R.style.AnimationPreview);
        presetBitWindow.setFocusable(true);
        presetBitWindow.setOutsideTouchable(true);
        presetBitWindow.setBackgroundDrawable(new ColorDrawable(0));
        presetBitWindow.showAtLocation(playSurface, Gravity.CENTER, 0, 0);

    }

    private void initViewPager(View view) {
        final TextView left = (TextView) view.findViewById(R.id.text_pre_left);
        final TextView rigth = (TextView) view.findViewById(R.id
                .text_pre_right);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                left.setTextColor(Color.BLUE);
                rigth.setTextColor(0xffffffff);
                prePager.setCurrentItem(0);
            }
        });
        rigth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                rigth.setTextColor(Color.BLUE);
                left.setTextColor(0xffffffff);
                prePager.setCurrentItem(1);
            }
        });

        prePager = (ViewPager) view.findViewById(R.id.vPager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        View view1 = mInflater.inflate(R.layout.popuppreset, null);
        View view2 = mInflater.inflate(R.layout.popuppreset, null);
        btnLeft[0] = (Button) view1.findViewById(R.id.pre1);
        btnRigth[0] = (Button) view2.findViewById(R.id.pre1);
        btnLeft[1] = (Button) view1.findViewById(R.id.pre2);
        btnRigth[1] = (Button) view2.findViewById(R.id.pre2);
        btnLeft[2] = (Button) view1.findViewById(R.id.pre3);
        btnRigth[2] = (Button) view2.findViewById(R.id.pre3);
        btnLeft[3] = (Button) view1.findViewById(R.id.pre4);
        btnRigth[3] = (Button) view2.findViewById(R.id.pre4);
        btnLeft[4] = (Button) view1.findViewById(R.id.pre5);
        btnRigth[4] = (Button) view2.findViewById(R.id.pre5);
        btnLeft[5] = (Button) view1.findViewById(R.id.pre6);
        btnRigth[5] = (Button) view2.findViewById(R.id.pre6);
        btnLeft[6] = (Button) view1.findViewById(R.id.pre7);
        btnRigth[6] = (Button) view2.findViewById(R.id.pre7);
        btnLeft[7] = (Button) view1.findViewById(R.id.pre8);
        btnRigth[7] = (Button) view2.findViewById(R.id.pre8);
        btnLeft[8] = (Button) view1.findViewById(R.id.pre9);
        btnRigth[8] = (Button) view2.findViewById(R.id.pre9);
        btnLeft[9] = (Button) view1.findViewById(R.id.pre10);
        btnRigth[9] = (Button) view2.findViewById(R.id.pre10);
        btnLeft[10] = (Button) view1.findViewById(R.id.pre11);
        btnRigth[10] = (Button) view2.findViewById(R.id.pre11);
        btnLeft[11] = (Button) view1.findViewById(R.id.pre12);
        btnRigth[11] = (Button) view2.findViewById(R.id.pre12);
        btnLeft[12] = (Button) view1.findViewById(R.id.pre13);
        btnRigth[12] = (Button) view2.findViewById(R.id.pre13);
        btnLeft[13] = (Button) view1.findViewById(R.id.pre14);
        btnRigth[13] = (Button) view2.findViewById(R.id.pre14);
        btnLeft[14] = (Button) view1.findViewById(R.id.pre15);
        btnRigth[14] = (Button) view2.findViewById(R.id.pre15);
        btnLeft[15] = (Button) view1.findViewById(R.id.pre16);
        btnRigth[15] = (Button) view2.findViewById(R.id.pre16);
        listViews.add(view1);
        listViews.add(view2);
        prePager.setAdapter(new ViewPagerAdapter(listViews));
        prePager.setCurrentItem(0);
        prePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == 0) {
                    left.setTextColor(Color.BLUE);
                    rigth.setTextColor(0xffffffff);
                } else {
                    rigth.setTextColor(Color.BLUE);
                    left.setTextColor(0xffffffff);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        PresetListener listener = new PresetListener();
        for (int i = 0; i < 16; i++) {
            btnLeft[i].setOnClickListener(listener);
            btnRigth[i].setOnClickListener(listener);
        }
    }

    //预位置设置监听
    private class PresetListener implements View.OnClickListener {
        @Override
        public void onClick(View arg0) {
            int id = arg0.getId();
            presetBitWindow.dismiss();
            int currIndex = prePager.getCurrentItem();
            switch (id) {
                case R.id.pre1:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 31);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 30);
                    }
                    break;

                case R.id.pre2:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 33);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 32);
                    }
                    break;

                case R.id.pre3:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 35);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 34);
                    }

                case R.id.pre4:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 37);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 36);
                    }
                    break;

                case R.id.pre5:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 39);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 38);
                    }
                    break;

                case R.id.pre6:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 41);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 40);
                    }
                    break;

                case R.id.pre7:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 43);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 42);
                    }
                    break;

                case R.id.pre8:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 45);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 44);
                    }
                    break;

                case R.id.pre9:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 47);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 46);
                    }
                    break;

                case R.id.pre10:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 49);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 48);
                    }
                    break;

                case R.id.pre11:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 51);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 50);
                    }
                    break;

                case R.id.pre12:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 53);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 52);
                    }
                    break;

                case R.id.pre13:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 55);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 54);
                    }
                    break;

                case R.id.pre14:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 57);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 56);
                    }
                    break;

                case R.id.pre15:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 59);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 58);
                    }
                    break;

                case R.id.pre16:
                    if (currIndex == 0) {
                        NativeCaller.PPPPPTZControl(strDID, 61);
                    } else {
                        NativeCaller.PPPPPTZControl(strDID, 60);
                    }
                    break;
            }
        }
    }

    //判断sd卡是否存在
    private boolean existSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    // 拍照
    private void takePicture(final Bitmap bmp) {
        if (!isPictSave) {
            isPictSave = true;
            new Thread() {
                public void run() {
                    savePicToSDcard(bmp);
                }
            }.start();
        } else {
            return;
        }
    }

    /*
     * 保存到本地
     * 注意：此处可以做本地数据库sqlit 保存照片，以便于到本地照片观看界面从SQLite取出照片
     */
    private synchronized void savePicToSDcard(final Bitmap bmp) {
        String strDate = getStrDate();
        //String date = strDate.substring(0, 10);
        FileOutputStream fos = null;
        try {
            File div = new File(Environment.getExternalStorageDirectory(),
                    "ipcamerademo/takepic");
            if (!div.exists()) {
                div.mkdirs();
            }
            ++i;
            Log.e("", i + "");
            File file = new File(div, strDate + "_" + strDID + "_" + i + "" +
                    ".jpg");
            fos = new FileOutputStream(file);
            if (bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                fos.flush();
                Log.d("tag", "takepicture success");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        showToast(R.string.ptz_takepic_ok);
                    }
                });
            }
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToast(R.string.ptz_takepic_fail);
                }
            });
            Log.d("tag", "exception:" + e.getMessage());
            e.printStackTrace();
        } finally {
            isPictSave = false;
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = null;
            }
        }
    }

    //时间格式
    private String getStrDate() {
        Date d = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd_HH_mm");
        String strDate = f.format(d);
        return strDate;
    }

    /*
     * 录像
     */
    private void goTakeVideo() {
        dismissBrightAndContrastProgress();
        if (isTakeVideo) {
            showToast(R.string.ptz_takevideo_end);
            Log.d("tag", "停止录像");
            if (!isJpeg) {
                NativeCaller.RecordLocal(strDID, 0);
            }

            isTakeVideo = false;
            ptzTake_vodeo.setImageResource(R.drawable.ptz_takevideo);
        } else {
            isTakeVideo = true;
            showToast(R.string.ptz_takevideo_begin);
            Log.d("tag", "开始录像");
            videotime = (new Date()).getTime();
            ptzTake_vodeo.setImageResource(R.drawable.ptz_takevideo_pressed);
            if (!isJpeg) {
                NativeCaller.RecordLocal(strDID, 1);
            }

        }
    }

    private void stopTakevideo() {
        if (isTakeVideo) {
            showToast(R.string.ptz_takevideo_end);
            Log.d("tag", "停止录像");
            isTakeVideo = false;
            // cameratakevideo.stopRecordVideo(strDID);
        }
    }

    //讲话
    private void StartTalk() {
        if (customAudioRecorder != null) {
            Log.i("info", "startTalk");
            customAudioRecorder.StartRecord();
            NativeCaller.PPPPStartTalk(strDID);
        }
    }

    //停止讲话
    private void StopTalk() {
        if (customAudioRecorder != null) {
            Log.i("info", "stopTalk");
            customAudioRecorder.StopRecord();
            NativeCaller.PPPPStopTalk(strDID);
        }
    }

    //监听
    private void StartAudio() {
        synchronized (this) {
            AudioBuffer.ClearAll();
            audioPlayer.AudioPlayStart();
            NativeCaller.PPPPStartAudio(strDID);
        }
    }

    //停止监听
    private void StopAudio() {
        synchronized (this) {
            audioPlayer.AudioPlayStop();
            AudioBuffer.ClearAll();
            NativeCaller.PPPPStopAudio(strDID);
        }
    }

    /*
     * 监听
     */
    private void goAudio() {
        dismissBrightAndContrastProgress();
        if (!isMcriophone) {
            if (bAudioStart) {
                Log.d("info", "没有声音");
                isTalking = false;
                bAudioStart = false;
                ptzAudio.setImageResource(R.drawable.ptz_audio_off);
                StopAudio();
            } else {
                Log.d("info", "有声");
                isTalking = true;
                bAudioStart = true;
                ptzAudio.setImageResource(R.drawable.ptz_audio_on);
                StartAudio();
            }

        } else {
            isMcriophone = false;
            bAudioRecordStart = false;
            ptztalk.setImageResource(R.drawable.ptz_microphone_off);
            StopTalk();
            isTalking = true;
            bAudioStart = true;
            ptzAudio.setImageResource(R.drawable.ptz_audio_on);
            StartAudio();
        }

    }

    /*
     * 对讲
     */
    private void goMicroPhone() {
        dismissBrightAndContrastProgress();
        if (!isTalking) {
            if (bAudioRecordStart) {
                Log.d("tag", "停止说话");
                isMcriophone = false;
                bAudioRecordStart = false;
                ptztalk.setImageResource(R.drawable.ptz_microphone_off);
                StopTalk();
            } else {
                Log.d("info", "开始说话");
                isMcriophone = true;
                bAudioRecordStart = true;
                ptztalk.setImageResource(R.drawable.ptz_microphone_on);
                StartTalk();
            }
        } else {
            isTalking = false;
            bAudioStart = false;
            ptzAudio.setImageResource(R.drawable.ptz_audio_off);
            StopAudio();
            isMcriophone = true;
            bAudioRecordStart = true;
            ptztalk.setImageResource(R.drawable.ptz_microphone_on);
            StartTalk();
        }

    }

    /*
     * 分辨率设置
     */
    private void showResolutionPopWindow() {

        if (resolutionPopWindow != null && resolutionPopWindow.isShowing()) {
            return;
        }
        if (nStreamCodecType == ContentCommon.PPPP_STREAM_TYPE_JPEG) {
            // jpeg
            LinearLayout layout = (LinearLayout) LayoutInflater.from(this)
                    .inflate(R.layout.ptz_resolution_jpeg, null);
            TextView qvga = (TextView) layout
                    .findViewById(R.id.ptz_resolution_jpeg_qvga);
            TextView vga = (TextView) layout
                    .findViewById(R.id.ptz_resolution_jpeg_vga);
            if (reslutionlist.size() != 0) {
                getReslution();
            }
            if (isvga) {
                vga.setTextColor(Color.RED);
            }
            if (isqvga) {
                qvga.setTextColor(Color.RED);
            }
            qvga.setOnClickListener(this);
            vga.setOnClickListener(this);

            resolutionPopWindow = new PopupWindow(layout, 100,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            int x_begin = getWindowManager().getDefaultDisplay().getWidth() / 6;
            int y_begin = ptzResolutoin.getTop();
            resolutionPopWindow.showAtLocation(findViewById(R.id.play),
                    Gravity.BOTTOM | Gravity.RIGHT, x_begin, y_begin);

        } else {
            // h264
            LinearLayout layout = (LinearLayout) LayoutInflater.from(this)
                    .inflate(R.layout.ptz_resolution_h264, null);
            TextView qvga1 = (TextView) layout
                    .findViewById(R.id.ptz_resolution_h264_qvga);
            TextView vga1 = (TextView) layout
                    .findViewById(R.id.ptz_resolution_h264_vga);
            TextView p720 = (TextView) layout
                    .findViewById(R.id.ptz_resolution_h264_720p);
            TextView middle = (TextView) layout
                    .findViewById(R.id.ptz_resolution_h264_middle);
            TextView high = (TextView) layout
                    .findViewById(R.id.ptz_resolution_h264_high);
            TextView max = (TextView) layout
                    .findViewById(R.id.ptz_resolution_h264_max);

            if (reslutionlist.size() != 0) {
                getReslution();
            }
            if (ismax) {
                max.setTextColor(Color.RED);
            }
            if (ishigh) {
                high.setTextColor(Color.RED);
            }
            if (ismiddle) {
                middle.setTextColor(Color.RED);
            }
            if (isqvga1) {
                qvga1.setTextColor(Color.RED);
            }
            if (isvga1) {
                vga1.setTextColor(Color.RED);
            }
            if (isp720) {
                p720.setTextColor(Color.RED);
            }
            high.setOnClickListener(this);
            middle.setOnClickListener(this);
            max.setOnClickListener(this);
            qvga1.setOnClickListener(this);
            vga1.setOnClickListener(this);
            p720.setOnClickListener(this);
            resolutionPopWindow = new PopupWindow(layout, 100,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            Display display = ((WindowManager) getSystemService(Context
                    .WINDOW_SERVICE))
                    .getDefaultDisplay();
            int oreation = display.getOrientation();
            int x_begin = getWindowManager().getDefaultDisplay().getWidth() / 6;
            int y_begin = ptzResolutoin.getTop();
            resolutionPopWindow.showAtLocation(findViewById(R.id.play),
                    Gravity.BOTTOM | Gravity.RIGHT, x_begin, y_begin + 60);

        }

    }

    /**
     * 获取reslution
     */
    public static Map<String, Map<Object, Object>> reslutionlist = new
            HashMap<String, Map<Object, Object>>();

    /**
     * 增加reslution
     */
    private void addReslution(String mess, boolean isfast) {
        if (reslutionlist.size() != 0) {
            if (reslutionlist.containsKey(strDID)) {
                reslutionlist.remove(strDID);
            }
        }
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put(mess, isfast);
        reslutionlist.put(strDID, map);
    }

    private void getReslution() {
        if (reslutionlist.containsKey(strDID)) {
            Map<Object, Object> map = reslutionlist.get(strDID);
            if (map.containsKey("qvga")) {
                isqvga = true;
            } else if (map.containsKey("vga")) {
                isvga = true;
            } else if (map.containsKey("qvga1")) {
                isqvga1 = true;
            } else if (map.containsKey("vga1")) {
                isvga1 = true;
            } else if (map.containsKey("p720")) {
                isp720 = true;
            } else if (map.containsKey("high")) {
                ishigh = true;
            } else if (map.containsKey("middle")) {
                ismiddle = true;
            } else if (map.containsKey("max")) {
                ismax = true;
            }
        }
    }

    /*
     * @param type
     * 亮度饱和对比度
     */
    private void setBrightOrContrast(final int type) {

        if (!bInitCameraParam) {
            return;
        }
        int width = getWindowManager().getDefaultDisplay().getWidth();
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.brightprogress, null);
        SeekBar seekBar = (SeekBar) layout.findViewById(R.id.brightseekBar1);
        seekBar.setMax(255);
        switch (type) {
            case BRIGHT:
                seekBar.setProgress(nBrightness);
                break;
            case CONTRAST:
                seekBar.setProgress(nContrast);
                break;
            default:
                break;
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar
                .OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                switch (type) {
                    case BRIGHT:// 亮度
                        nBrightness = progress;
                        NativeCaller.PPPPCameraControl(strDID, BRIGHT,
                                nBrightness);
                        break;
                    case CONTRAST:// 对比度
                        nContrast = progress;
                        NativeCaller.PPPPCameraControl(strDID, CONTRAST,
                                nContrast);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress,
                                          boolean arg2) {

            }
        });
        mPopupWindowProgress = new PopupWindow(layout, width / 2, 180);
        mPopupWindowProgress.showAtLocation(findViewById(R.id.play),
                Gravity.TOP, 0, 0);

    }

    @Override
    protected void onDestroy() {

        LogUtils.e(LOG_TAG, "视频播放停止3");

        NativeCaller.StopPPPPLivestream(strDID);
        StopAudio();
        StopTalk();
        stopTakevideo();
        //给全局变量赋值

        super.onDestroy();

    }

    /***
     * BridgeService callback 视频参数回调
     **/
    @Override
    public void callBackCameraParamNotify(String did, int resolution,
                                          int brightness, int contrast, int
                                                  hue, int saturation,
                                          int flip, int mode) {
        Log.e("设备返回的参数", resolution + "," + brightness + "," + contrast + ","
                + hue + "," + saturation + "," + flip + "," + mode);
        nBrightness = brightness;
        nContrast = contrast;
        nResolution = resolution;
        bInitCameraParam = true;
        deviceParamsHandler.sendEmptyMessage(flip);
    }

    /***
     * BridgeService callback 视频数据流回调
     **/
    @Override
    public void callBackVideoData(byte[] videobuf, int h264Data, int len, int
            width, int height) {
        Log.d("底层返回数据", "videobuf:" + videobuf + "--" + "h264Data" + h264Data
                + "len" + len + "width" + width + "height" + height);
        if (!bDisplayFinished)
            return;
        bDisplayFinished = false;
        videodata = videobuf;
        videoDataLen = len;
        Message msg = new Message();
        if (h264Data == 1) { // H264
            nVideoWidths = width;
            nVideoHeights = height;
            if (isTakepic) {
                isTakepic = false;
                byte[] rgb = new byte[width * height * 2];
                NativeCaller.YUV4202RGB565(videobuf, rgb, width, height);
                ByteBuffer buffer = ByteBuffer.wrap(rgb);
                mBmp = Bitmap.createBitmap(width, height, Bitmap.Config
                        .RGB_565);
                mBmp.copyPixelsFromBuffer(buffer);
                takePicture(mBmp);
            }
            isH264 = true;
            msg.what = 1;
        } else { // MJPEG
            isJpeg = true;
            msg.what = 2;
        }
        mHandler.sendMessage(msg);
        //录像数据
        if (isTakeVideo) {

            Date date = new Date();
            long times = date.getTime();
            int tspan = (int) (times - videotime);
            Log.d("tag", "play  tspan:" + tspan);
            videotime = times;
            if (videoRecorder != null) {
                if (isJpeg) {

                    videoRecorder.VideoRecordData(2, videobuf, width, height,
                            tspan);
                }
            }
        }
    }

    /***
     * BridgeService callback
     **/
    @Override
    public void callBackMessageNotify(String did, int msgType, int param) {
        Log.d("tag", "MessageNotify did: " + did + " msgType: " + msgType
                + " param: " + param);
        if (bManualExit)
            return;

        if (msgType == ContentCommon.PPPP_MSG_TYPE_STREAM) {
            nStreamCodecType = param;
            return;
        }

        if (msgType != ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS) {
            return;
        }

        if (!did.equals(strDID)) {
            return;
        }

        Message msg = new Message();
        msg.what = 1;
        msgHandler.sendMessage(msg);
    }

    /***
     * BridgeService callback
     **/
    @Override
    public void callBackAudioData(byte[] pcm, int len) {
        Log.d(LOG_TAG, "AudioData: len :+ " + len);
        if (!audioPlayer.isAudioPlaying()) {
            return;
        }
        CustomBufferHead head = new CustomBufferHead();
        CustomBufferData data = new CustomBufferData();
        head.length = len;
        head.startcode = AUDIO_BUFFER_START_CODE;
        data.head = head;
        data.data = pcm;
        AudioBuffer.addData(data);
    }

    /***
     * BridgeService callback
     **/
    @Override
    public void callBackH264Data(byte[] h264, int type, int size) {
        Log.d("tag", "CallBack_H264Data" + " type:" + type + " size:" + size);
        if (isTakeVideo) {
            Date date = new Date();
            long time = date.getTime();
            int tspan = (int) (time - videotime);
            Log.d("tag", "play  tspan:" + tspan);
            videotime = time;
            if (videoRecorder != null) {
                videoRecorder.VideoRecordData(type, h264, size, 0, tspan);
            }
        }
    }

    //对讲数据
    @Override
    public void AudioRecordData(byte[] data, int len) {
        // TODO Auto-generated method stub
        if (bAudioRecordStart && len > 0) {
            NativeCaller.PPPPTalkAudioData(strDID, data, len);
        }
    }

    //定义录像接口
    public void setVideoRecord(VideoRecorder videoRecorder) {
        this.videoRecorder = videoRecorder;
    }

    public VideoRecorder videoRecorder;

    public interface VideoRecorder {
        abstract public void VideoRecordData(int type, byte[] videodata, int
                width, int height, int time);
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
