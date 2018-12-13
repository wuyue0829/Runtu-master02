package com.mac.runtu.interfaceif;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/20 0020 下午 3:38
 */
public interface OnRegisterBizSuccessListener {
    //成功
    void onSuccess();
    //不可用
    void onFail();
    //网络错误
    void onNetWorkFail();
}
