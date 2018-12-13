package com.mac.runtu.javabean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Description:特产电商产品详情
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/5 0005 下午 5:32
 */
public class SpecialtyGoodDetailInfo implements Serializable {

    /*参数集合*/
    public ArrayList<BusinessParameterDetailInfos> businessParameterInfos;
    public BusinessTypeInfo                        businessTypeInfo;

    public String category;
    public String city;
    public String content;
    public String county;
    public String createTime;
    public String cycle;
    public String district;
    public String districtName;
    public int hitCount;
    public int id;
    public int isDelete;
    public String kind;
    public String kindName;
    public float minPrice;
    public String name;
    public int payment;
    public ArrayList<PictureInfo> pictureInfos;
    public String province;
    public String seller_uuid;
    public int status;
    public int stock;
    public String town;
    public String updateTime;
    public String uuid;
    public String village = "";

}
