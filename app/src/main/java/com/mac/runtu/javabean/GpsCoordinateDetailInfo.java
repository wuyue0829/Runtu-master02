package com.mac.runtu.javabean;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/12/6 0006 下午 6:42
 */
public class GpsCoordinateDetailInfo {
    /*"IMEI": "352544071630207",
            "id": 1,
            "latitude": 34.2765,
            "longitude": 109.008965,
            "uuid": "8fe30c05d2ea4956a24d33db4f85f40e"*/

    public String IMEI;
    public int    id;
    public double latitude;
    public double longitude;
    public String uuid;


    @Override
    public String toString() {
        return "GpsCoordinateDetailInfo{" +
                "IMEI='" + IMEI + '\'' +
                ", id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
