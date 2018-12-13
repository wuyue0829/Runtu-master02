package com.mac.runtu.javabean;

import java.io.Serializable;
import java.util.ArrayList;

public class PropertyDetailInfo implements Serializable {
        public String area = "";

        // 流转方式1为转让，2为转包，3为互换，4为入股，5为出租
        public String exchange = "";
        public int hitCount;
        public String province ="";

        public String city= "";
        public String contacts= "";
        public String content= "";
        public String county= "";
        public String createTime= "";
        public int id;
        public int isDelete;
        public String kind= "";

        public KindDictionaryDetailInfo kindDictionaryInfo;
        public String                   phone= "";
        public ArrayList<PictureInfo> pictureInfos;
        public String                   price= "";
        public String                   status= "";
        public String                   title= "";
        public String                   town= "";
        public String                   userUuid= "";
        public String                   uuid= "";
        public String                   village= "";
        public String                   years= "";


        @Override
        public String toString() {
                return "PropertyDetailInfo{" +
                        "area='" + area + '\'' +
                        ", exchange='" + exchange + '\'' +
                        ", hitCount=" + hitCount +
                        ", province='" + province + '\'' +
                        ", city='" + city + '\'' +
                        ", contacts='" + contacts + '\'' +
                        ", content='" + content + '\'' +
                        ", county='" + county + '\'' +
                        ", createTime='" + createTime + '\'' +
                        ", id=" + id +
                        ", isDelete=" + isDelete +
                        ", kind='" + kind + '\'' +
                        ", kindDictionaryInfo=" + kindDictionaryInfo +
                        ", phone='" + phone + '\'' +
                        ", pictureInfos=" + pictureInfos +
                        ", price='" + price + '\'' +
                        ", status='" + status + '\'' +
                        ", title='" + title + '\'' +
                        ", town='" + town + '\'' +
                        ", userUuid='" + userUuid + '\'' +
                        ", uuid='" + uuid + '\'' +
                        ", village='" + village + '\'' +
                        ", years='" + years + '\'' +
                        '}';
        }
}