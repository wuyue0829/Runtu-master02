package com.mac.runtu.javabean;

import java.io.Serializable;

/**
 * Description: 产权流转信息发布
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/31 0031 下午 8:41
 */
public class PropertyCommiteInfo implements Serializable{
    /*userUuid，kind（种类），title，content，contacts，phone，city，county，town，village
    ，area（亩），price（价格），years（年限），exchange(流转方式1为转让，2为转包，3为互换，4为入股，5为出租) uuid
    (单独获得第uuid)*/


    public String userUuid;

    public String kind;
    public String title;
    public String content;
    public String contacts;
    public String phone;
    public String publisher = "";
    public String province ="";
    public String city;
    public String county  = "";
    public String town    = "";
    public String village = "";
    public String uuid    = "";

    public String area;
    public String price;
    public String years;
    public String exchange;

}
