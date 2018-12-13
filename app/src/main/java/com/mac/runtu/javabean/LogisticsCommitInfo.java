package com.mac.runtu.javabean;

import java.io.Serializable;

/**
 * Description:物流直配
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/28 0028 下午 3:34
 */
public class LogisticsCommitInfo implements Serializable {

   /*userUuid，type（信息类型1为找活信息，2为找车信息），title，content，contacts，phone，startCity
   （起始城市），startCounty，startTown，startVillage，targetCity（目的城市），targetCounty
   ，targetTown，targetVillage，kind（车辆类型），weight（载重），route（途经地），departureTime
   （发车时间），publisher，departureStatus（状态1为已发车，2为未发车）*/

    public String userUuid;
    public String type = "1";
    public String title;
    public String content;
    public String contacts;
    public String phone;

    public String startProvince = "";
    public String startCity;
    public String startCounty;
    public String startTown;
    public String startVillage;

    public String targetProvince = "";
    public String targetCity;
    public String targetCounty;
    public String targetTown;
    public String targetVillage;
    public String kind;
    public String weight;
    public String route = "";
    public String departureTime;
    public String publisher       = "";
    public String departureStatus = "1";
    public String uuid            = "1";
}
