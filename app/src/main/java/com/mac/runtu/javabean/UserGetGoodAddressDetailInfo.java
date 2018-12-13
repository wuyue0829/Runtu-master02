package com.mac.runtu.javabean;

import java.io.Serializable;

/**
 * Description:个人订单详情
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/2 0002 上午 10:26
 */
public class UserGetGoodAddressDetailInfo implements Serializable {

    /* "city": "100001",
            "consignee_address": "高新路30号凯创国际a座808室",
            "contact_number": "123",
            "contacts": "张三",
            "county": "100054",
            "create_time": "2016-11-02T10:19:51",
            "id": 2,
            "is_delete": 1,
            "phone": "13500000000",
            "province": "27",
            "status": 1,
            "user_uuid": "86102ef7ba35435c9f6800582ba19c7e",
            "uuid": "29af42eb39de4673a0bcb288a916fb18"*/

    public String userUuid;
    public String city;
    public String consigneeAddress;
    public String consignee_address;
    //固定电弧
    public String contact_number;
    public String contactNumber;
    public String contacts;

    public String county;
    public String create_time;
    public int id;
    public int is_delete;
    public String province;
    public int status;

    public String uuid;
    public String phone;

}
