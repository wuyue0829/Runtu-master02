package com.mac.runtu.javabean;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/28 0028 下午 3:34
 */
public class LogisticsDetailInfo {

    /*"cargoInfos": "",
            "contacts": "张先生",
            "content": "本周 周四 有车要去延安市 洛川县 县城  始发地为 城中区 三里桥镇 货车车型：车厢8.5米长，2
            .3米宽，可拉19吨货物，已转载12吨，还可配货，有需要者请速度联系车主",
            "createTime": "2016-10-28T15:38:03",
            "departureStatus": "",
            "departureTime": "",
            "hitCount": "",
            "id": 15,
            "isDelete": 1,
            "kind": "",
            "kindDictionaryInfo": "",
            "phone": "13386636279",
            "pictureInfos": [],
            "publisher": "",
            "requests": "",
            "route": "",
            "startCity": "延安市",
            "startCounty": "城中区",
            "startTown": "关平",
            "startVillage": "樊家",
            "status": 1,
            "targetCity": "延安市",
            "targetCounty": "洛川县",
            "targetTown": "东平",
            "targetVillage": "李家",
            "title": "有8.5 米长的车 需要配货",
            "type": 1,
            "userUuid": "86102ef7ba35435c9f6800582ba19c7e",
            "uuid": "ca949dssd5f1asfgyy19gh983s4110f2",
            "weight": 0.0*/
    public String contacts;
    public String content;
    public String createTime;
    public String departureStatus;
    public String departureTime;
    public String hitCount;

    public KindDictionaryDetailInfo kindDictionaryInfo;

    public int                    id;
    public int                    isDelete;
    public String                 phone;
    public ArrayList<PictureInfo> pictureInfos;
    public String                 publisher;
    public String                 requests;
    public String                 route;

    public String startProvince ="";
    public String startCity;
    public String startCounty;
    public String startTown;
    public String startVillage;
    public String status;

    public String targetProvince ="";
    public String targetCity;
    public String targetCounty;
    public String targetTown;
    public String targetVillage;

    public String title;
    public int    type;
    public String userUuid;
    public String uuid;
    public float  weight;
}
