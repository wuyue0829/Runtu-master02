package com.mac.runtu.javabean;

import java.io.Serializable;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/7 0007 下午 5:44
 */
public class ShopCarBusinessInfo implements Serializable {

    /* "content": "自然放羊成年山羊",
                "createTime": "2016-11-07T11:09:08",
                "district": "54fbfcadcc2849ed989626e6a24a5f44",
                "hitCount": 36,
                "id": 431,
                "isDelete": 1,
                "kind": "47700635d3204fa7b0f94716ab633d4d",
                "minPrice": 123.0,
                "name": "我二",
                "payment": 0,
                "status": 1,
                "stock": 123,
                "updateTime": "2016-09-19T17:23:43",
                "uuid": "305bbfa5acf6407c906f770badc8e9d6"*/
    public String content;
    public String createTime;
    public String district;
    public int    hitCount;
    public int    id;
    public int    isDelete;
    public String kind;
    public float  minPrice;
    public int    model;
    public String name;
    public int    payment;
    public int    status;
    public int    stock;
    public String updateTime;
    public String uuid;
}
