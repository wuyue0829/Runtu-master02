package com.mac.runtu.interfaceif;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/20 0020 下午 8:08
 */
public interface OnDataBooleanListener {

    //成功
    void onOk(Boolean result);
    //无数据
    void onFail();
    //网络错误
    void onNetWorkFail();

}
