package com.mac.runtu.javabean;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/28 0028 下午 2:10
 */
public class MyWXPayInfo {
    /*request.appId = "wxd930ea5d5a258f4f";
request.partnerId = "1900000109";
request.prepayId= "1101000000140415649af9fc314aa427",;
request.packageValue = "Sign=WXPay";
request.nonceStr= "1101000000140429eb40476f8896f4c9";
request.timeStamp= "1398746574";
request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
api.sendReq(req);*/

    public String appId;
    public String partnerId;
    public String prepayId;

    public String packageValue ="Sign=WXPay";
    public String nonceStr;
    public String timeStamp;
    public String sign;

}
