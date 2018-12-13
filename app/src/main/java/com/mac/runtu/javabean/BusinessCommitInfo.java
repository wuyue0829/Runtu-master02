package com.mac.runtu.javabean;

import java.io.Serializable;

/**
 * Description:客商发布资料提交
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/31 0031 下午 3:07
 */
public class BusinessCommitInfo implements Serializable{
    /*userUuid（用户ID），type（用户类型），kind（发布信息所属种类），title（标题），content
    （内容），contacts（联系人），phone（联系电话），publisher（发布者），city（市）,county（区县）,
    town（镇）, village（村）, picture(图片内容) , pictureName(图片名称),*/

    public String userUuid;
    public String type;
    public String kind;
    public String title;
    public String content;
    public String contacts;
    public String phone;
    public String publisher = "";
    public String province;
    public String city;
    public String county = "";
    public String town = "";
    public String village = "";
    public String uuid = "";


}
