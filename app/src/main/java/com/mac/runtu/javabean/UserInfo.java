package com.mac.runtu.javabean;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/19 0019 下午 6:42
 */
public class UserInfo {

  /* "msg": "个人资料查詢成功",
    "objList": {
        "IDnumber": "610125195001010513",
        "address": "长丰园小区9号楼",
        "birthday": "2016-10-14T12:32:59",
        "city": "100001",
        "county": "100054",
        "email": "5555",
        "headimg": "55555",
        "id": 5,
        "name": "张三",
        "nickname": "斜风细雨",
        "password": "96e79218965eb72c92a549dd5a330112",
        "phone": "13111111112",
        "province": "2500",
        "regTime": "2016-10-14T12:32:59",
        "rights": "5525252",
        "town": "100057",
        "username": "123",
        "usertype": 1,
        "uuid": "040cf6c04e1342e7ab89af2dd94f4d2d",
        "village": "1111"
    }*/
     /*{"msg":"个人资料查詢成功",
     "num":0,
     "objList":{
     "IDnumber":null,
     "address":"城关镇",
     "birthday":"2016-12-02T14:24:47",
     "city":"渭南",
     "county":"蒲城县",
     "email":null,
     "headimg":"1480646803840",
     "id":4,
     "loginStatus":1,
     "name":"我很看好你",
     "nickname":"新人",
     "password":"96e79218965eb72c92a549dd5a330112",
     "phone":"15229284149",
     "province":"陕西省",
     "regTime":"2016-12-02T14:24:47",
     "rights":null,
     "sex":null,
     "town":null,
     "username":"321",
     "usertype":2,
     "uuid":"86102ef7ba35435c9f6800582ba19c7e",
     "village":null}}*/

    //public int finishCount;
    //public int orderCount;
    //public int unfinishCount;
    public UserDesInfo objList;
    public boolean login;

    public class UserDesInfo {

        public String IDnumber ="";
         //地址
        public String address="";

        public String birthday="";
        //市
        public String city="";
        //县 区
        public String county="";
        public String email="";
        public String device = "123456";
        //图像地址
        public String headimg="";
        public String name="";
        //昵称
        public String nickname="";
        public String password="";
        public String phone="";
        //省
        public String province="";
        //注册时间
        public String regTime="";
        //权限
        public String rights="";
        //城镇
        public String town="";
        public String username="";
        //用户类型
        public String usertype="";
        //用户Id
        public String uuid="";
        //村
        public String village="";

        public String sex="";


        public String headPictrue="";

        public int loginStatus;

        public int IDnumberStatus; //0没审核 1 通过 2审核中

    }

}
