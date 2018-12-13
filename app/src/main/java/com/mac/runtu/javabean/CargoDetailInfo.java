package com.mac.runtu.javabean;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/28 0028 下午 5:54
 */
public class CargoDetailInfo {

    public String name = "无敌";

    /* "createTime": "2016-11-01T18:22:57",
            "id": 18,
            "isDelete": 1,
            "kind": "0a654bc239304033b3db8dbbf1a7a9be",
            "kindDictionaryInfo": {
                "createTime": "2016-10-17T16:42:21",
                "id": 16,
                "isDelete": 1,
                "kindName": "钢材",
                "model": 8,
                "status": 1,
                "userUuid": "c2525506931f4026bbdf",
                "uuid": "0a654bc239304033b3db8dbbf1a7a9be"
            },
            "phone": "13386636279",
            "publisher": "王麻子",
            "status": 1,
            "targetCity": "延安市",
            "targetCounty": "洛川县",
            "targetTown": "平湾",
            "targetVillage": "两耳",
            "userUuid": "86102ef7ba35435c9f6800582ba19c7e",
            "uuid": "095badb729ba422a8caf5d71bc9bbdd9",
            "weight": 19.2*/

    public String createTime = "";
    public int id;
    public int isDelete;
    public String kind;
    public KindDictionaryWuLiuInfo kindDictionaryInfo;
    public String phone;
    public String publisher;
    public int status;
    public String targetProvince ="";

    public String targetCity;
    public String targetCounty;
    public String targetTown;
    public String targetVillage;
    public String userUuid;
    public String uuid;
    public float weight;
}
