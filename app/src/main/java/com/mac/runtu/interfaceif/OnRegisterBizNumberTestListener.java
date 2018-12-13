package com.mac.runtu.interfaceif;

//回调
public interface OnRegisterBizNumberTestListener{
    //成功
    void onData(Boolean isUse);
    //不可用
    void onNoData();
    //网络错误
    void onNetWorkFail();
}
