package com.mac.runtu.utils;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/26 0026 下午 2:23
 */
public class RedSpotsUtisl {

    public static String setReSpots(int redSpots) {
        String type = "普通景点";
        if (redSpots == 1) {
            type = "红色景点";
        }

        return type;
    }
}
