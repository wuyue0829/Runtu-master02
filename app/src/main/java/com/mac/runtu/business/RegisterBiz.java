package com.mac.runtu.business;

import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnRegisterBizNumberTestListener;
import com.mac.runtu.interfaceif.OnRegisterBizSuccessListener;
import com.mac.runtu.utils.MD5Utils;
import com.mac.runtu.utils.MyHttpUtils;

import java.util.HashMap;

/**
 * Description: 注册 /忘记密码
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/19 0019 下午 7:21
 */
public class RegisterBiz {

    private static RegisterBiz rBiz;

    private RegisterBiz() {

    }

    public static RegisterBiz getInstance() {
        if (rBiz == null) {
            rBiz = new RegisterBiz();
        }

        return rBiz;
    }


    //手机号码验证
    public void checkUserNumber(String number, final
    OnRegisterBizNumberTestListener
            mListener) {

        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                .NUMBER_CHECK_CANUSE, null, GlobalConstants
                .KEY_PHONENUM, number, new MyHttpUtils
                .OnNewWorkRequestListener2Boolean() {


            @Override
            public void onNewWorkSuccess(Boolean result) {
                if (result) {
                    //号码可用
                    mListener.onData(result);
                } else {
                    //号码不可用
                    mListener.onNoData();
                }

            }

            @Override
            public void onNewWorkError(String msg) {
                //访问网络失败
                mListener.onNetWorkFail();
            }
        });

    }


    //提交注册信息
    public void setRegister2NetWork(String number, String password, final
    OnRegisterBizSuccessListener mListener) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.KEY_PHONENUM, number);
        hashMap.put(GlobalConstants.KEY_PASSWRED, MD5Utils.getMd5(password));

        MyHttpUtils.getBooleanDataFromNet(GlobalConstants.REGISTER_INFO,
                null, hashMap, new MyHttpUtils
                        .OnNewWorkRequestListener2Boolean() {


                    @Override
                    public void onNewWorkSuccess(Boolean result) {

                        if (result) {
                            //注册成功
                            mListener.onSuccess();
                        } else {
                            //失败
                            mListener.onFail();
                        }

                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        mListener.onNetWorkFail();
                    }
                });


    }

    //忘记密码
    //手机号码验证是否存在
    public void checkUserNumberExist(String number, final
    OnRegisterBizNumberTestListener
            mListener) {

        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                .NUMBER_CHECK_CANUSE, null, GlobalConstants
                .KEY_PHONENUM, number, new MyHttpUtils
                .OnNewWorkRequestListener2Boolean() {


            @Override
            public void onNewWorkSuccess(Boolean result) {
                if (result) {
                    //号码不存在
                    mListener.onNoData();
                } else {
                    //号码存在 可修改密码
                    mListener.onData(result);
                }

            }

            @Override
            public void onNewWorkError(String msg) {
                //访问网络失败
                mListener.onNetWorkFail();
            }
        });

    }

    //忘记密码 提交数据
    public void setUserData2NetWorkForgetPaw(String number, String password,
                                             final
    OnRegisterBizSuccessListener mListener) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.KEY_PHONENUM, number);
        hashMap.put(GlobalConstants.key_new_passwred, MD5Utils.getMd5(password));

        MyHttpUtils.getBooleanDataFromNet(GlobalConstants.user_forget_password,
                null, hashMap, new MyHttpUtils
                        .OnNewWorkRequestListener2Boolean() {


                    @Override
                    public void onNewWorkSuccess(Boolean result) {

                        if (result) {
                            //注册成功
                            mListener.onSuccess();
                        } else {
                            //失败
                            mListener.onFail();
                        }

                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        mListener.onNetWorkFail();
                    }
                });


    }

}
