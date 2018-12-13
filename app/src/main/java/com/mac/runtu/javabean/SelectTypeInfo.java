package com.mac.runtu.javabean;

import java.util.ArrayList;

/**
 * Description:网络获得的类型
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/27 0027 下午 2:24
 */
public class SelectTypeInfo {

    /*{
    "msg": "收貨地址列表查询成功",
    "num": 0,
    "objList": [
        {
            "createTime": "2016-08-31T15:52:26",
            "id": 1,
            "isDelete": 1,
            "kindName": "客商动态",
            "model": 3,
            "status": 1,
            "userUuid": "c2525506931f4026bbdf",
            "uuid": "38b08999f9b04384822780b8ad903696"
        },
        {
            "createTime": "2016-10-25T15:56:47",
            "id": 55,
            "isDelete": 1,
            "kindName": "农户动态",
            "model": 3,
            "status": 1,
            "userUuid": "null",
            "uuid": "e42ccd9de8e547a2b02a2e95ae0cf0ac"
        }
    ]
}*/

    public ArrayList<SelecttypeDetailInfo> objList;

    public class SelecttypeDetailInfo {
        public String createTime;
        public int    id;
        public String isDelete;
        public String kindName;
        public int    model;
        public int    status;
        public String userUuid;
        public String uuid;

    }

}
