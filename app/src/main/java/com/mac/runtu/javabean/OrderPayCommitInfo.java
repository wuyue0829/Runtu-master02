package com.mac.runtu.javabean;

import java.util.ArrayList;

/**
 * Description:付款页面 提交服务器生成订单
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/8 0008 下午 4:56
 */
public class OrderPayCommitInfo {

    public String                              userUuid;
    public float                               busifee; //商品总费用
    public float                               tranfee; //运费
    public float                               cost; //总费油
    public String                              consigneeUuid; //地址id
    public ArrayList<OrderPayCommitDetailInfo> busiInfo;  //产品详情
    //public String                              payModel; //来自哪个的付款
    public String                              model;   //特产电商 还是体验农场
   // public String                              scIp;
}
