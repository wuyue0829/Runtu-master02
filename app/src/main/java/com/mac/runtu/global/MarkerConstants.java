package com.mac.runtu.global;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/23 14:55
 */
public class MarkerConstants {

    public static boolean isUpdataUserOrderData = false;
    public static boolean isUpdataUserInfoData = false;

    //客商动态农户区选择图片
    public static boolean isBussFarm = false;

    public static boolean isChangerPwd = false;

    //默认为空串
    public static String value_province = "";
    public static String value_city     = "";
    public static String value_county   = "";
    public static String value_town     = "";
    public static String value_village  = "";

    public static String value_kind = "";


    public static String value_service_time_type = "";


    //默认为空串
    public static String value_fainl_province = "";
    public static String value_fainl_city     = "";
    public static String value_fainl_county   = "";
    public static String value_fainl_town     = "";
    public static String value_fainl_village  = "";

    //public static String path_user_iamge = "";

    public static ArrayList<String> value_more_image_name;
    public static ArrayList<String> value_more_image_content_path;
    public static String value_more_image_url = "";
    public static String value_more_image_url1 = "";
    public static Activity activity = null;
    public static Activity activity2 = null;

    //特产电商 不同标签
    public static String specialty_Type      = "";


    public static String slidingmenu_toggle_from = "";


    public static String value_good_uuid = "";

    //筛选的mode
    public static String value_mode_filter_4one = "";


    //实名认证标志
    public static  boolean isFromRelName = false;


    public static void clearImages(){
        MarkerConstants.value_more_image_content_path =
                null;
        MarkerConstants.value_more_image_url = "";
        MarkerConstants.value_more_image_name = null;
    }


}
