package com.mac.runtu.javabean;

import java.util.ArrayList;

/**
 * Description:创业培训c
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/26 0026 下午 4:44
 */
public class EntreTrainDetailInfo {

    /* "address": "鹏迪科技",
            "city": "1",
            "content": "
        不uhon11
        ",
        "county": "1",
            "createTime": "2016-10-17T13:07:53",
            "hitCount": 1,
            "id": 8,
            "isDelete": 1,
            "kind": "创业培训",
            "organizer": "陈某某11",
            "phone": "13454585678",
            "pictureInfos": [],
        "status": 1,
            "title": "洛川200亩水浇地转让",
            "town": "1",
            "trainingTime": "2016.03.10-2016.5.20",
            "userUuid": "86102ef7ba35435c9f6800582ba19c7e",
            "uuid": "21cd572eb54044119f12c60cfc3fd866",
            "village": "1"*/


    // 流转方式1为转让，2为转包，3为互换，4为入股，5为出租
    public String address = "";
    public String city= "";
    public String content= "";
    public  String province="";
    public String county= "";
    public String createTime= "";
    public String hitCount= "";
    public int id;
    public int isDelete;
    public String kind= "";
    public String organizer= "";
    public String                   phone= "";
    public ArrayList<PictureInfo> pictureInfos;
    public int                   status;
    public String                   title= "";
    public String                   town= "";
    public String                   trainingTime= "";
    public String                   userUuid= "";
    public String                   uuid= "";
    public String                   village= "";

  /*  public String contacts= "";

    public KindDictionaryDetailInfo kindDictionaryInfo;

    public String                   price= "";


    public String                   years= "";*/

}
