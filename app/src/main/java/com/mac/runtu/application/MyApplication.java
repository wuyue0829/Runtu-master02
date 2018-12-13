package com.mac.runtu.application;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.mac.runtu.activity.otherActivity.MainActivity;
import com.mac.runtu.service.OtherLoginCheckService;
import com.mac.runtu.utils.ServiceStatusUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;


public class MyApplication extends Application implements Thread
        .UncaughtExceptionHandler {

    public Context context;
    public Handler mHanlder;
    public int     mainThreadId;

    public static MyApplication instance;
    public static Intent        intent;


    /**
     * 此方法先onCreat
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        //百度地图初话
        SDKInitializer.initialize(this);

        //初始化全局的context对象   new出一个View  Toast
        context = getApplicationContext();
        //handler.sendEmptyMessage(0)
        //初始化主线程的handler对象
        mHanlder = new Handler();
        //获取主线程的线程id
        mainThreadId = android.os.Process.myTid();

        instance = this;


        //网络请求时间
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(8000L, TimeUnit.MILLISECONDS)
                .readTimeout(8000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);


        //bug收集
        CrashReport.initCrashReport(getApplicationContext(), "129e22cb76",
                true);

        //启动不同设备登陆张好的校验
        startCheckUser();


        //设置未捕获异常帮助
        //Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        //关闭服务
        stopCheckUser();

        super.onTerminate();

    }



    public void stopCheckUser() {

        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this,
                OtherLoginCheckService.class);

        if (serviceRunning && intent != null) {
            stopService(intent);
        }
    }


    public void startCheckUser() {

        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this,
                OtherLoginCheckService.class);

        if (!serviceRunning) {
            intent = new Intent(this, OtherLoginCheckService.class);
            startService(intent);
        }

    }


    //铺货全局异常 一秒以后重新启动app
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        /*Intent intent = new Intent(getApplicationContext(),
                MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent restartIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent,
                0);

        // 退出程序
        AlarmManager mgr = (AlarmManager) getSystemService(Context
                .ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                restartIntent); // 1秒钟后重启应用
        System.exit(-1);*/
    }
}
