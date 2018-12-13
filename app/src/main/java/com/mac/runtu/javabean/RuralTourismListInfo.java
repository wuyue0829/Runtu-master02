package com.mac.runtu.javabean;

import java.util.ArrayList;

/**
 * 旅游信息的单个对象
 */
public class RuralTourismListInfo {

    // 流转方式1为转让，2为转包，3为互换，4为入股，5为出租

    public String address       = "";
    public String province ="";
    public String city       = "";
    public String contacts   = "";
    public String content    = "";
    public String county     = "";
    public String createTime = "";
    public String hitCount   = "";

    public int id;
    public int isDelete;
    public String kind = "";
    public KindDictionaryDetailInfo kindDictionaryInfo;
    public String phone = "";
    public ArrayList<PictureInfo> pictureInfos;

    public int redSpots;
    public String price    = "";
    public String status   = "";
    public String title    = "";
    public String town     = "";
    public String userUuid = "";
    public String uuid     = "";
    public String village  = "";
    public String years    = "";

    //经度
    public double longitude ;
    //纬度
    public double latitude;
}