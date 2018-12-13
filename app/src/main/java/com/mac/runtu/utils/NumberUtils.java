package com.mac.runtu.utils;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/9/28 0028 上午 10:41
 */
public class NumberUtils {
    /**
     * 电话号码匹配
     * @param number
     * @return
     */
    public static boolean isNumnberMatch(String number) {

        if (number.matches("^1[3-8]\\d{9}$")) {
            return true;
        }

        return false;
    }


}
