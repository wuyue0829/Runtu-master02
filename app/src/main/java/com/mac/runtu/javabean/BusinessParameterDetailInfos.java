package com.mac.runtu.javabean;

import java.io.Serializable;

/**
 * Description:商品信息参数信息
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/5 0005 下午 6:12
 */
public class BusinessParameterDetailInfos implements Serializable {
      /*{
                    "busiUuid": "24333b494df94561a8be270deeaa344a",
                    "id": 360,
                    "isDelete": 1,
                    "paramName": "参数1",
                    "paramPrice": 1.0,
                    "stock": 1000,
                    "uuid": "0346e140a6024c7c811c64d83fd69582"
                },*/

    public String busiUuid;
    public int    id;
    public int    isDelete;
    public String paramName;
    public float  paramPrice;
    //该规格的库存
    public int    stock;
    public String uuid;

    public boolean isSelect;
}
