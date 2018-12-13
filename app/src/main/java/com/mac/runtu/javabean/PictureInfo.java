package com.mac.runtu.javabean;

import java.io.Serializable;

public class PictureInfo implements Serializable {


        /* "createTime": "2016-11-02T19:22:07",
                    "id": 542,
                    "informationUuid": "915873e4d3104f6e91ede49ea9c89dcd",
                    "isDelete": 1,
                    "modelType": 5,
                    "pictureLocalUrl": "E:
                    "pictureName": "1478085727153147808572225015229284149.jpg",
                    "pictureNetUrl": "http://192.168.18
                    .90:8080/upload/1478085727153147808572225015229284149.jpg",
                    "pictureType": 1,
                    "status": 3,
                    "updateTime": "2016-11-02T19:22:07",
                    "uuid": "3eeb09527d794b7aac6f5ecd735e2c3f"*/

    public String createTime = "";
    public int    id;
    public String informationUuid;
    public int    isDelete;
    public int    modelType;

    public String pictureLocalUrl;

    public String pictureName;

    //图片路径
    public String pictureNetUrl = "";

    public int pictureType;

    public int status;

    public String updateTime;

    public String uuid;
}