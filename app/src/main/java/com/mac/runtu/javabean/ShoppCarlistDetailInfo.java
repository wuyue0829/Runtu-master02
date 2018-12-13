package com.mac.runtu.javabean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/7 0007 下午 5:42
 */
public class ShoppCarlistDetailInfo implements Serializable {

    public float               allPay;
    public String              busiUuid;
    public ShopCarBusinessInfo businessInfo;
    public int                 buyCount;
    public String              createTime;

    public ArrayList<PictureInfo>       pictureInfos;
    public int                          flag;
    public int                          id;
    public BusinessParameterDetailInfos paramInfo;
    public String                       paramUuid;
    public String                       userUuid;
    public String                       uuid;
    //自己添加的字段
    public boolean isSelect = false;

}
