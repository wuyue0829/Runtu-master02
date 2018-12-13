package com.mac.runtu.javabean;

import java.util.ArrayList;

/**
 * Description:劳务合作详情对象l
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/27 1:50
 */
public class LaborServiceDetailInfo {
     /*"num": 21,
    "objList": [
        {
            "city": "铜川33",
            "contacts": "王五",
            "county": "洛川33",
            "createTime": "2016-10-14T14:50:32",
            "id": 34,
            "isDelete": 1,
            "kind": "96de233c708a4d9da49ec8ff9e5d2cf3",
            "kindDictionaryInfo": {
                "createTime": "2016-09-30T14:09:13",
                "id": 9,
                "isDelete": 1,
                "kindName": "劳务",
                "model": 2,
                "status": 1,
                "userUuid": "c2525506931f4026bbdf",
                "uuid": "96de233c708a4d9da49ec8ff9e5d2cf3"
            },
            "labourTime": "2016-10-26T15:00:43",
            "personCount": 12,
            "phone": "1382345678",
            "pictureInfos": [],
            "status": 1,
            "title": "收购洛川苹果20W斤，价钱好商量",
            "town": "乡镇33",
            "type": 1,
            "userUuid": "da7ae77efe164403ab8b62fe0b175240",
            "uuid": "2bd287472b6e471b8039bd6e55e9e7a8",
            "village": "乡村"
        },*/

    public String address    = "";
    public String province   = "";
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
    public String labourTime  = "";
    public String personCount = "";
    public String phone       = "";
    public ArrayList<PictureInfo> pictureInfos;

    // public int redSpots;
    //public String price    = "";
    public String status   = "";
    public String title    = "";
    public String town     = "";
    public String type     = "";
    public String userUuid = "";
    public String uuid     = "";
    public String village  = "";
    //public String years    = "";

    /**
     * 精度
     */
    public String longitude  = "0";
    /**
     * 纬度
     */
    public String latitude = "0";


    public int mora = 0;
}
