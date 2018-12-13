package com.mac.runtu.utils;

import android.util.Log;

/**
 * Description:整个项目的日志类
 * 项目上线时，把日志的开关关掉
 * Copyright  : Copyright (c) 2016
 * Company    : 独孤求败
 * Author     : 王超
 * Date       : 2016/8/18 9:21
 */
public class LogUtils {

    //项目上线时，把日志的开关关掉
    private static boolean DEBUG = true;

    public static void v(String tag, String msg) {
        if (DEBUG)
            Log.v(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            Log.e(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG)
            Log.w(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (DEBUG)
            Log.i(tag, msg);
    }

}
