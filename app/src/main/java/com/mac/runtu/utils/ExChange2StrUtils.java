package com.mac.runtu.utils;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/26 0026 下午 1:03
 */
public class ExChange2StrUtils {

    public static String setExChange(String exchange) {
        String type = "";
        switch (exchange) {
            case "1":
                type = "转让";
                break;
            case "2":
                type = "转包";
                break;
            case "3":
                type = "互换";
                break;
            case "4":
                type = "入股";
                break;
            case "5":

                type = "出租";
                break;

        }
        return type;
    }


    public static String getOrderStatud(int status) {
        String statudName = "";
        switch (status) {
            case 0:
                statudName = "待付款";
                break;
            case 1:
                statudName = "待发货";
                break;
            case 2:
                statudName = "待收货";
                break;
            case 3:
                statudName = "已签收";
                break;
            case 4:
                statudName = "已取消";
                break;
        }

        return statudName;
    }
}
