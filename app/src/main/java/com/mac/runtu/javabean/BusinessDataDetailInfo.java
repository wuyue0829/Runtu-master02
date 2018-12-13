package com.mac.runtu.javabean;

import java.io.Serializable;
import java.util.ArrayList;

public class BusinessDataDetailInfo implements Serializable {
         /*{
            "contacts": "张三",
            "content": "我自横刀向天笑，去留肝胆两昆仑",
            "createTime": "2016-10-14T14:50:44",
            "id": 34,
            "isDelete": 1,
            "kind": "000086",
            "phone": "13353362420",
            "pictureInfos": [],
            "status": 1,
            "title": "收购洛川苹果20W斤，价钱好商量",
            "type": 1,
            "userUuid": "d39a925bf64046e2b45eda53ca26cdc5",
            "uuid": "aba9f77cd5d14ff689e83c60ccf15125"
        }*/


        /*public String                 contacts;
        public String                 content;
        public String                 createTime;
        public String                 id;
        public String                 isDelete;
        public String                 kind;
        public String                 phone;
        public ArrayList<PictureInfo> pictureInfos;
        public String                 status;
        public String                 title;

        public String                 userUuid;
        //
        public String                 uuid;
        public String                 type;*/



        public String area = "";
        public String                 type;

        // 流转方式1为转让，2为转包，3为互换，4为入股，5为出租
        public String exchange = "";
        public String hitCount= "";

        public String province;
        public String city= "";
        public String contacts= "";
        public String content= "";
        public String county= "";
        public String createTime= "";
        public int id;
        public int isDelete;
        public String kind= "";

        public KindDictionaryDetailInfo kindDictionaryInfo;
        public String                   phone= "";
        public ArrayList<PictureInfo> pictureInfos;
        public String                   price= "";
        public String                   status= "";
        public String                   title= "";
        public String                   town= "";
        public String                   userUuid= "";
        public String                   uuid= "";
        public String                   village= "";
        public String                   years= "";
        
        
    }