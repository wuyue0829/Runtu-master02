package com.mac.runtu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2017/1/14 0014 下午 3:20
 */
public class MyData {

    private static MyData myData;
    private SimpleDateFormat dateFormat;
    private String nowDate;


    private MyData() {

    }

    public static MyData getInstance() {
        if (myData == null) {
            myData = new MyData();

        }

        return myData;
    }

    public String getDataStr(String formatt) {

        if (formatt == null) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            nowDate = dateFormat.format(new Date());

        }else {
            dateFormat = new SimpleDateFormat(formatt);
            nowDate = dateFormat.format(new Date());
        }

        return nowDate;
    }
}
