package com.mac.runtu.javabean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/25 0025 下午 4:46
 */
public class PropertyInfo implements Serializable{
    /* "objList": [
        {
            "area": "2222222",
            "city": "",
            "contacts": "王五",
            "content": "<p>
	bjbjn</p>",
            "county": "111111",
            "createTime": "2016-10-15T14:56:53",
            "exchange": "2222222",
            "hitCount": "2222222",
            "id": 32,
            "isDelete": 1,
            "kind": "db259421f67647b19554a60e73411971",
            "kindDictionaryInfo": {
                "createTime": "2016-09-30T14:09:22",
                "id": 10,
                "isDelete": 1,
                "kindName": "土地产权",
                "model": 5,
                "status": 1,
                "userUuid": "c2525506931f4026bbdf",
                "uuid": "db259421f67647b19554a60e73411971"
            },
            "phone": "13695214563",
            "pictureInfos": [],
            "price": "2222222",
            "status": 1,
            "title": "靖边果园转让",
            "town": "",
            "userUuid": "2222222",
            "uuid": "f79551dc4c6243dfb27186c929b520df",
            "village": "55555555",
            "years": "2222222"
        }
    ],*/
    public int num;
    public ArrayList<PropertyDetailInfo> objList;

    @Override
    public String toString() {
        return "PropertyInfo{" +
                "num=" + num +
                ", objList=" + objList +
                '}';
    }
}
