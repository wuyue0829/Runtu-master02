package com.mac.runtu.javabean;

/**
 * Description: 捎货信息提交
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/28 0028 下午 5:54
 */
public class CargoCommitInfo {
    /*userUuid, logID(物流信息id), kind（发布信息种类）, weight（重量）, targetCity（目标城市）,
    targetCounty, targetTown, targetVillage, publisher, phone*/
    public String userUuid      = "";
    public String logID         = "";
    public String kind          = "";
    public String weight        = "";
    public String targetProvince   = "";
    public String targetCity    = "";
    public String targetCounty  = "";
    public String targetTown    = "";
    public String targetVillage = "";

    //新加的
    public String uuid = "";
    public String phone = "";
    public String publisher ="";
}
