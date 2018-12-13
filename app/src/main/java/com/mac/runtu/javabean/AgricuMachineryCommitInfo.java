package com.mac.runtu.javabean;

import java.io.Serializable;

/**
 * Description:农资农机信息提交页
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/1 0001 下午 1:19
 */
public class AgricuMachineryCommitInfo implements Serializable {
    /*userUuid，type（信息类型1为供货信息，2为求购信息），identity（发布者身份1为厂家直销，2为本地供应商，3为农户，4
    为其他），kind（信息种类），title，content，contacts（联系人），phone，city，county，town
    ，village，price（价格），publisher（发布人）*/

    public String userUuid;
    public String type = "";
    public String identity;
    public String kind;
    public String title;
    public String content;
    public String contacts;
    public String phone;
    public String city;
    public String province;
    public String county;
    public String town;
    public String village ="";
    public String price;
    public String publisher = "";
    public String uuid;

}
