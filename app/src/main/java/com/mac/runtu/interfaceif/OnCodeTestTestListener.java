package com.mac.runtu.interfaceif;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/20 0020 下午 3:35
 */
public interface OnCodeTestTestListener {

    //成功
    void onData(String code);
    //不可用
    void onNoData();
    //网络错误
    void onNetWorkFail();

}
