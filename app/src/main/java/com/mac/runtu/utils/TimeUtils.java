package com.mac.runtu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/26 0026 下午 12:49
 */
public class TimeUtils {


    public static String setCreatTime(String creatime) {
        if (creatime != null) {
            String[] ts = creatime.split("T");
            return ts[0];
        } else {
            return "";
        }

    }

    public static String[] getDataArry(String time) {
        if (time != null) {
            String[] ts = time.split("T");
            return ts;
        } else {
            return null;
        }

    }

    public static String setDepartureTime(String creatime) {
        if (creatime != null) {
            String[] ts = creatime.split(" ");
            return ts[0];
        } else {
            return "";
        }

    }

    //提交发车时间
    public static String departTime(String time) {

        //String str = "2014-01-01 12:12:12";
        //"yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(time);

            long time2 = date.getTime();

            String s = String.valueOf(time2);
            return s;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getCarStuat(String time) {
        //Date date = new Date();
        //"yyyy-MM-dd HH:mm:ss"
        Calendar c = Calendar.getInstance();
        long current = System.currentTimeMillis();
        try {
            c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time));
            long timeInMillis = c.getTimeInMillis();
            if (current > timeInMillis) {
                return "已发车";
            } else {
                return "未发车";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return "";
    }
}
