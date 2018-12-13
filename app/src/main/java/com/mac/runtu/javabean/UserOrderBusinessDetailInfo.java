package com.mac.runtu.javabean;

/**
 * Description: 个人订单商品详情
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/11 0011 下午 3:57
 */
public class UserOrderBusinessDetailInfo {

    /* "businessInfo": {
                "content": "<img src="http://192.168.18
                .90:80/upload/1473316539230.jpg" style="width: 800px; height:
                 800px;"><img src="http://192.168.18
                 .90:80/upload/1473316544025.jpg" style="width: 800px;
                 height: 800px;"><img src="http://192.168.18
                 .90:80/upload/1473316570323.jpg" style="width: 800px;
                 height: 800px;"><img src="http://192.168.18
                 .90:80/upload/1473316580109.jpg" style="width: 800px;
                 height: 800px;">",
                "createTime": "2016-11-08T11:17:55",
                "district": "54fbfcadcc2849ed989626e6a24a5f44",
                "hitCount": 32,
                "id": 360,
                "isDelete": 1,
                "kind": "47700635d3204fa7b0f94716ab633d4d",
                "minPrice": 1.0,
                "model": 1,
                "name": "测试商品2公鸡试商品2公鸡试商品2公鸡试",
                "payment": 0,
                "status": 1,
                "stock": 520,
                "updateTime": "2016-09-08T14:36:37",
                "uuid": "24333b494df94561a8be270deeaa344a"
            },*/

    public String content;
    public String createTime;
    public String district;
    public String districtName;
    public int    hitCount;
    public int    id;
    public int    isDelete;
    public String kind;
    public String kindName;
    public float  minPrice;

    public int    model;

    public String name;
    public int    payment;
    public int    status;
    public int    stock;
    public String updateTime;
    public String uuid;

    //商品详情
    public String city;
    public String province;
    public String county;
    public String town;
    public String village ="";
}
