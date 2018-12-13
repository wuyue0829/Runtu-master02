package com.mac.runtu.business;


import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnCodeTestTestListener;
import com.mac.runtu.utils.MyHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Description:验证码
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/20 0020 上午 11:10
 */
public class CheckCodeBiz {

    //验证码验证
    public static void getCode(String userNumber, final OnCodeTestTestListener
            mListener) {

        MyHttpUtils.getStringDataFromNet(GlobalConstants.get_code, null,
                GlobalConstants.KEY_PHONENUM, userNumber, new
                        MyHttpUtils.OnNewWorkRequestListener() {


                            @Override
                            public void onNewWorkSuccess(String result) {

                                try {

                                    //得到验证码
                                    if (result != null) {


                                        JSONObject msg = new JSONObject(result);
                                        String mCode = msg.getString("msg");
                                        mListener.onData(mCode);
                                    } else {
                                        //验证码为空
                                        mListener.onNoData();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onNewWorkError(String msg) {
                                mListener.onNetWorkFail();
                            }
                        });
    }
}
