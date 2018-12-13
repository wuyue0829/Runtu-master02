package com.mac.runtu.javabean;

import java.util.ArrayList;

/**
 * Description: 广告轮播条实体类
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/20 0020 上午 9:04
 */
public class AdAddressInfo {

    public ArrayList<TopImageData> objList;
    public String                  msg;

    public class TopImageData {

       /* "create_time": "2016-10-14T14:50:45",
                "id": 441,
                "information_uuid": "12b037df261e43cb81530ac505a0bf33",
                "is_delete": 1,
                "model_type": 1,
                "picture_local_url": "/home/install/upload/",
                "picture_name": "sky.jpg",
                "picture_net_url": "http://121.42.191.63/PDKJ-WEB/upload/",
                "picture_type": 1,
                "status": 1,
                "update_time": "2016-10-14T14:50:45",
                "uuid": "24198c5ad9404f35b92a8d1d245ae7ad"*/


        public String create_time;
        public int id;
        public String information_uuid;
        public int is_delete;
        public int model_type;
        public String picture_local_url;

        public String path;
        public String picture_name;
        //网络路径
        public String picture_net_url;

        public int picture_type;

        public int status;

        public String update_time;

        public String uuid;

    }

}
