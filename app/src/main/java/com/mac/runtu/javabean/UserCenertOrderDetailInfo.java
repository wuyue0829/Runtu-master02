package com.mac.runtu.javabean;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/11 0011 上午 11:26
 */
public class UserCenertOrderDetailInfo {

    public int amount;
    public String busiUuid;
    public UserOrderBusinessDetailInfo businessInfo;
    public BusinessParameterInfo businessParameterInfo;
    public String consigneeUuid;
    public float cost;
    public String createTime;
    public String district;
    public String gpsPort;
    public int id;
    public int isDelete;
    public String logisticsNumber;
    public int model;
    public String orderCode;
    public OrderInfo orderInfo;
    public int orderStatus;
    public String order_uuid;
    public String paramUuid;
    public ArrayList<PictureInfo> pictureInfos;
    public String reciveAddress;
    public String recivePhone;
    public String reciveUser;
    public String userUuid;
    public String uuid;
    public String videoID = "";
    public String videoName = "";
    public String videoPassword = "";

    public class BusinessParameterInfo
    {
        public String busiUuid;
        public String id;
        public String isDelete;
        public String paramName;
        public String paramPrice;
        public String stock;
        public String uuid;

        public BusinessParameterInfo() {}

        public String getBusiUuid()
        {
            return this.busiUuid;
        }

        public String getId()
        {
            return this.id;
        }

        public String getIsDelete()
        {
            return this.isDelete;
        }

        public String getParamName()
        {
            return this.paramName;
        }

        public String getParamPrice()
        {
            return this.paramPrice;
        }

        public String getStock()
        {
            return this.stock;
        }

        public String getUuid()
        {
            return this.uuid;
        }

        public void setBusiUuid(String paramString)
        {
            this.busiUuid = paramString;
        }

        public void setId(String paramString)
        {
            this.id = paramString;
        }

        public void setIsDelete(String paramString)
        {
            this.isDelete = paramString;
        }

        public void setParamName(String paramString)
        {
            this.paramName = paramString;
        }

        public void setParamPrice(String paramString)
        {
            this.paramPrice = paramString;
        }

        public void setStock(String paramString)
        {
            this.stock = paramString;
        }

        public void setUuid(String paramString)
        {
            this.uuid = paramString;
        }
    }

    public class OrderInfo
    {
        private String createTime;
        private String orderCode;

        public OrderInfo() {}

        public String getCreateTime()
        {
            return this.createTime;
        }

        public String getOrderCode()
        {
            return this.orderCode;
        }

        public void setCreateTime(String paramString)
        {
            this.createTime = paramString;
        }

        public void setOrderCode(String paramString)
        {
            this.orderCode = paramString;
        }
    }

}
