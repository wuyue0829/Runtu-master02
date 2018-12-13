package com.mac.runtu.interfaceif;

/**
 * Description:服务中得到当前地址的回调
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/7 0007 上午 9:33
 */
public interface OnGetLocationListener {

    void onSuccess(double longitude, double latitude, String addrss);

    void onfail();
}
