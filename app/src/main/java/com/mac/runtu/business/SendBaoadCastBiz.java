package com.mac.runtu.business;

import android.content.Intent;

import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.utils.UiUtils;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/12/14 0014 下午 3:57
 */
public class SendBaoadCastBiz {


    public static void send(){
        // 1. 创建意图对象

        MarkerConstants.isUpdataUserOrderData = true;


        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent();
                        // 2. 设置动作   包名+功能（大写）
                        intent.setAction(GlobalConstants.ORDER_UPDATE);
                        // 3. 发送广播
                        UiUtils.getContext().sendBroadcast(intent);
                    }
                });
            }
        }.start();

    }
}
