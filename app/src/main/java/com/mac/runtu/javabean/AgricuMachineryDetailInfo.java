package com.mac.runtu.javabean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 农资机 详细对象数据
 */
public class AgricuMachineryDetailInfo implements Serializable {

        /*"num": 6,
    "objList": [
        {
            "city": "000001",
            "contacts": "张三",
            "content": "<p>
	<span style="font-size:16px;"><span style="color:#008000;
	"><strong><em>会话层对偶句颇丰vlkfmlkgbmlkfgmb&#39;
	</em></strong></span></span></p>",
            "county": "000001",
            "createTime": "2016-10-26T14:46:29",
            "hitCount": 2,
            "id": 29,
            "identity": 1,
            "isDelete": 1,
            "kind": "c18d97bca632465489801c924a0d9db9",
            "kindDictionaryInfo": {
                "createTime": "2016-09-30T10:18:30",
                "id": 5,
                "isDelete": 1,
                "kindName": "种子",
                "model": 4,
                "status": 1,
                "userUuid": "c2525506931f4026bbdf",
                "uuid": "c18d97bca632465489801c924a0d9db9"
            },
            "phone": "13698765432",
            "pictureInfos": [],
            "price": 1.0,
            "publisher": "1",
            "status": 1,
            "title": "1",
            "town": "000001",
            "type": 1,
            "userUuid": "d39a925bf64046e2b45eda536726cdc5",
            "uuid": "03f7edc9a4304d92b18a75c6eef476dc",
            "village": "000001"
        }*/


    // 流转方式1为转让，2为转包，3为互换，4为入股，5为出租

    public int hitCount;

    public String province;
    public String city       = "";
    public String contacts   = "";
    public String content    = "";
    public String county     = "";
    public String createTime = "";
    public int id;

    public int identity;


    public int isDelete;
    public String kind = "";

    public KindDictionaryDetailInfo kindDictionaryInfo;
    public String phone = "";
    public ArrayList<PictureInfo> pictureInfos;
    public String price    = "";
    public String status   = "";
    public String title    = "";
    public String town     = "";
    public String type     = "";
    public String userUuid = "";
    public String uuid     = "";
    public String village  = "";


}