package com.mac.runtu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mac.runtu.business.LoginBiz;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Description: 定时校验账号异地登陆
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/12/2 0002 下午 4:40
 */
public class OtherLoginCheckService extends Service {


    private Timer mTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (mTimer != null) {
            mTimer.cancel();
        }
        //定时器
//        startTimer();
    }

    //启动定时器(java自带, 偶尔会用)
    private void startTimer() {
        //定时器
        mTimer = new Timer();
        //按固定频率执行任务, 参1:任务对象;参2:第一次执行延时时间;参3:每次执行的间隔时间
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                LoginBiz.getInstance().checkoutUser();

            }
        },0,100000);
    }

    @Override
    public void onDestroy() {
        //停止定时器
        if (mTimer != null) {
            mTimer.cancel();
        }
        super.onDestroy();
    }
}
