package com.mac.runtu.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.mac.runtu.activity.otherActivity.CustomScanActActivity;
import com.mac.runtu.activity.personcentre.LoginActivity;
import com.mac.runtu.application.MyApplication;
import com.mac.runtu.global.GlobalConstants;


/**
 * 处理ui常见操作的帮助类
 *
 * @author zhengping
 */
public class UiUtils {


    public static Context getContext() {
        return MyApplication.instance.context;
    }

    //获取主线程的handler对象
    public static Handler getMainThreadHandler() {
        return MyApplication.instance.mHanlder;
    }

    //获取主线程的线程id
    public static int getMainThreadId() {
        return MyApplication.instance.mainThreadId;
    }

    //获取字符串资源
    public static String getString(int resId) {
        return getContext().getResources().getString(resId);
    }

    //获取字符串数组
    public static String[] getStringArray(int resId) {
        return getContext().getResources().getStringArray(resId);
    }

    //获取Drawable
    public static Drawable getDrawable(int resId) {
        return getContext().getResources().getDrawable(resId);

    }

    //获取color
    public static int getColor(int resId) {
        return getContext().getResources().getColor(resId);
    }

    //获取颜色的状态选择器
    public static ColorStateList getColorStateList(int resId) {
        // TODO Auto-generated method stub
        return getContext().getResources().getColorStateList(resId);
    }

    //获取dimen
    public static int getDimen(int resId) {
        return getContext().getResources().getDimensionPixelSize(resId);
    }

    //dip2px
    public static int dip2px(int dip) {
        //获取设备密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    //px2dip
    public static int px2dip(int px) {
        //获取设备密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    //Android中能操作ui的线程是主线程
    public static boolean isRunOnUiThread() {
        //1、获取当前线程id
        int currentThreadId = android.os.Process.myTid();
        //2、获取主线程的id
        int mainThreadId = getMainThreadId();
        //3、比较
        return currentThreadId == mainThreadId;
    }


    //Thread:线程
    //Runnable：任务

    //保证某一些操作一定运行在主线程中
    public static void runOnUiThread(Runnable r) {

        if (isRunOnUiThread()) {
            //主线程
            //new Thread(r).start();
            r.run();
        } else {
            //把r丢到主线程的消息队列
            getMainThreadHandler().post(r);
        }

    }


    public static int getScreenWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }


    //运行在后台子线程
    public static void runOnBackgroundThread(Runnable r) {
        new Thread(r).start();
    }

    //判断用户以前登
    public static boolean isLogin() {
        return SPUtils.getString(getContext(), GlobalConstants.SP_USERID,
                "").equals("") ? false : true;
    }

    public static void go2LoginAcivity() {
        Intent intent = new Intent(getContext(), LoginActivity
                .class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    public static String getUserID() {
        return SPUtils.getString(getContext(), GlobalConstants.SP_USERID, "");
    }

    /**
     * 得到用户电话
     *
     * @return
     */
    public static String getUserNumber() {

        return SPUtils.getString(getContext(), GlobalConstants.SP_USERNUM, "");
    }

    /**
     * 用户联系人
     *
     * @return
     */
    public static String getUserName() {
        return SPUtils.getString(getContext(), GlobalConstants
                .sp_user_name, "");
    }

    /**
     * 用户是否实名认证
     *
     * @return
     */
    public static int getAttestationStuts() {

      return SPUtils.getInt(getContext(), GlobalConstants
               .sp_really_name_status, 0);

       //return 1;
       //return 2;
       //return 3;
    }

    /**
     * 打开二维码
     *
     * @param act
     */
    public static void openScan4Activity(Activity act) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(act);
        intentIntegrator
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setPrompt("将二维码/条码放入框内，即可自动扫描")//写那句提示的话
                .setOrientationLocked(false)//扫描方向固定
                .setCaptureActivity( CustomScanActActivity.class) // 设置自定义的activity是CustomActivity
                .initiateScan(); // 初始化扫描


    }

    /**
     * fragme
     *
     * @param frg
     */
    public static void openScan4Fragment(Fragment frg) {
        IntentIntegrator intentIntegrator = IntentIntegrator
                .forSupportFragment(frg).setCaptureActivity
                        (CustomScanActActivity.class);
        intentIntegrator
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setPrompt("将二维码/条码放入框内，即可自动扫描")//写那句提示的话
                .setOrientationLocked(false)//扫描方向固定
                .setCaptureActivity(CustomScanActActivity.class) // 设置自定义的activity是CustomActivity
                .initiateScan(); // 初始化扫描


    }
}
