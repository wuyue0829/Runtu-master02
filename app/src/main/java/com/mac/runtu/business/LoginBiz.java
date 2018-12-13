package com.mac.runtu.business;

import android.content.Intent;
import android.text.TextUtils;

import com.mac.runtu.application.MyApplication;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.UserInfo;
import com.mac.runtu.service.MyNotificationService;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MD5Utils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.SPUtils;
import com.mac.runtu.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Description:   登录的业务逻辑
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/19 0019 下午 5:49
 */
public class LoginBiz {

    private static final String TAG = "LoginBiz";

    private static LoginBiz mLoginBiz;

    //用户信息
    public UserInfo userInfo;

    public UserInfo.UserDesInfo userDesInfo;

    // public String userID;

    public  String userNumber;
    private String userPassword;
    private String userUuid;
    private String loginStatus;


    /**
     * 登录调
     */
    public interface OnLoginListener {

        void isLogingSuccess(UserInfo.UserDesInfo info);

        void isLogingFail();

        void onNetWorkFail();
    }

    /**
     * 获得数据回调
     */
    public interface OnDataListener {

        void isSuccess(UserInfo.UserDesInfo info);

        void onNetWorkFail();
    }

    /**
     * 获得数据回调
     */
    public interface OnOrderListener {

        void isSuccess();

        void onNetWorkFail();
    }


    private LoginBiz() {

    }


    public static LoginBiz getInstance() {

        if (mLoginBiz == null) {
            mLoginBiz = new LoginBiz();
        }

        return mLoginBiz;
    }


    /**
     * 用户登录
     *
     * @param number
     * @param password
     * @param mListener
     */
    public void loginFromActivity(String number, String password,
                                  OnLoginListener
                                          mListener) {
        getInfoFromNetWork(number, MD5Utils.getMd5(password), mListener);
    }


    //访问网络
    public void getInfoFromNetWork(String number, final String password, final
    OnLoginListener mListener) {

        //isLogin = false;

        MyHttpUtils.getStringDataFromNet(GlobalConstants.LOGIN_URL, null,
                GlobalConstants.KEY_PHONENUM, number, GlobalConstants
                        .KEY_PASSWRED, password, new MyHttpUtils
                        .OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {
                        if (result != null) {

                            //存入信息
                            //解析信息
                            userInfo = GSonUtil.parseJson(result,
                                    UserInfo.class);

                            if (userInfo.login) {

                                getUserDetailInfo();

                                if (mListener != null) {
                                    mListener.isLogingSuccess(userDesInfo);
                                }
                                startCheckUserStatus();
                            } else {

                                if (mListener != null) {
                                    //密码或账号错误
                                    mListener.isLogingFail();
                                }
                            }

                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        if (mListener != null) {
                            mListener.onNetWorkFail();
                        }

                    }
                });
    }

    /**
     * 启动账号检查
     */
    private void startCheckUserStatus() {

        String loginStatus = SPUtils.getString(UiUtils.getContext(),
                GlobalConstants.sp_loginStatus, "");

        LogUtils.e(TAG, "loginStatus = " + loginStatus);

        if (!TextUtils.isEmpty(loginStatus)) {
            MyApplication.instance.startCheckUser();
        }
    }


    //得到用户详情
    public void getUserInfo4Net(OnDataListener mListener) {
        getUserInfoData(mListener);
    }

    //得到用户详情
    public void getUserInfo4Net() {

        getUserInfoData(null);
    }

    /**
     * 从网路获得用户详情信息
     *
     * @param mListener
     */
    private void getUserInfoData(final OnDataListener mListener) {


        userUuid = UiUtils.getUserID();

        LogUtils.e(TAG, "userUuid=" + userUuid);

        if (TextUtils.isEmpty(userUuid)) {
            return;
        }


        MyHttpUtils.getStringDataFromNet(GlobalConstants.user_get_info_url,
                null,
                GlobalConstants.KEY_USERID, userUuid, new
                        MyHttpUtils.OnNewWorkRequestListener() {


                            @Override
                            public void onNewWorkSuccess(String result) {

                                LogUtils.e(TAG, "底层获得用详情数据=" + result);

                                if (result != null) {

                                    userInfo = GSonUtil.parseJson(result,
                                            UserInfo.class);

                                    getUserDetailInfo();

                                    if (mListener != null) {
                                        mListener.isSuccess(userDesInfo);
                                    }

                                    getUserOrdrData();
                                }
                            }

                            @Override
                            public void onNewWorkError(String msg) {

                                if (mListener != null) {
                                    mListener.onNetWorkFail();
                                }

                            }
                        });
    }

    /**
     * 从网路获取用户订单信息
     */
    public void getUserOrdrData() {

        getUserOrderCount4Net(null);
    }


    /**
     * 从网路获取用户订单信息
     */
    public void getUserOrdrData(OnOrderListener mListener) {
        getUserOrderCount4Net(mListener);
    }

    private void getUserOrderCount4Net(final OnOrderListener mListener) {
        if (userUuid == null) {
            userUuid = UiUtils.getUserID();

            if (TextUtils.isEmpty(userUuid)) {
                return;
            }
        }
        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .user_get_order_num_url, null,
                GlobalConstants.KEY_USERID, userUuid, new
                        MyHttpUtils.OnNewWorkRequestListener() {


                            @Override
                            public void onNewWorkSuccess(String result) {

                                LogUtils.e(TAG, "底层获得用户订单数据");

                                if (result != null) {
                                    saveUserOrderCount(result);
                                }

                                if (mListener != null) {
                                    mListener.isSuccess();
                                }
                            }


                            @Override
                            public void onNewWorkError(String msg) {
                                if (mListener != null) {
                                    mListener.onNetWorkFail();
                                }
                            }
                        });
    }

    /**
     * 存储用户订单数据
     *
     * @param result
     */
    private void saveUserOrderCount(String result) {
        try {
            JSONObject jo = new JSONObject(result);
            SPUtils.setString(UiUtils.getContext(),
                    GlobalConstants
                            .sp_use_ordercount,
                    jo.getInt
                            ("orderCount") + "");


            SPUtils.setString(UiUtils.getContext(),
                    GlobalConstants
                            .sp_use_finishcount,
                    jo.getInt("finishCount") + "");

            SPUtils.setString(UiUtils.getContext(),
                    GlobalConstants
                            .sp_use_unfinishcount,
                    jo.getInt("unfinishCount") +
                            "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检出用户是否在其他地方登录
     */
    public void checkoutUser() {

        LogUtils.e(TAG, "检测账号是否在其他地方登陆");


        loginStatus = SPUtils.getString(UiUtils.getContext(),
                GlobalConstants.sp_loginStatus, "");

        if (TextUtils.isEmpty(loginStatus)) {
            return;
        }


        if (userNumber == null) {

            userNumber = SPUtils.getString(UiUtils.getContext(),
                    GlobalConstants.SP_USERNUM,
                    "");

            if (TextUtils.isEmpty(userNumber)) {
                return;
            }
        }

        if (userPassword == null) {
            userPassword = SPUtils.getString(UiUtils.getContext(),
                    GlobalConstants.SP_USERPASS,
                    "");

            if (TextUtils.isEmpty(userPassword)) {
                return;
            }
        }


        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                        .user_status_choeck_url, null, GlobalConstants
                        .KEY_PHONENUM, userNumber, GlobalConstants
                        .KEY_PASSWRED, userPassword
                , GlobalConstants.key_loginStatus, loginStatus, new
                        MyHttpUtils
                                .OnNewWorkRequestListener2Boolean() {


                            @Override
                            public void onNewWorkSuccess(Boolean result) {

                                LogUtils.e(TAG, "检测账号是result=" + result);
                                if (result) {


                                } else {
                                    sendInform();
                                }
                            }

                            @Override
                            public void onNewWorkError(String msg) {

                            }
                        });
    }

    /**
     * 登录状态改变 发送通知
     */
    private void sendInform() {
        String strLoginStatus = SPUtils.getString(UiUtils
                .getContext(), GlobalConstants
                .sp_loginStatus, "");
        if (TextUtils.isEmpty(strLoginStatus)) {
            return;
        }
        UiUtils.getContext().startService(new
                Intent(UiUtils.getContext(),
                MyNotificationService.class));

        clearUserlocationData();
    }

    /**
     * 为成员变量赋值
     */
    private void getUserDetailInfo() {
        if (userInfo.objList == null) {
            return;
        }

        userDesInfo = userInfo.objList;

        if (userDesInfo == null) {
            return;
        }

        userNumber = userDesInfo.phone;

        userPassword = userDesInfo.password;

        userUuid = userDesInfo.uuid;

        loginStatus = String.valueOf(userDesInfo.loginStatus);
        //图片路径

        saveUserPortionInfo();

    }

    /**
     * 存储用户部分信息
     */
    private void saveUserPortionInfo() {

        if (userDesInfo.headimg != null) {
            SPUtils.setString(UiUtils.getContext(),
                    GlobalConstants.SP_use_iamgepath,
                    GlobalConstants.Base_imgae_url +
                            userDesInfo.headimg.trim());
        }


        SPUtils.setString(UiUtils.getContext(),
                GlobalConstants.SP_USERNUM,
                userDesInfo.phone);

        SPUtils.setString(UiUtils.getContext(),
                GlobalConstants.SP_USERPASS,
                userDesInfo.password);

        SPUtils.setString(UiUtils.getContext(),
                GlobalConstants.SP_USERID,
                userDesInfo.uuid);

        SPUtils.setString(UiUtils.getContext(),
                GlobalConstants.sp_user_type_name,
                userDesInfo.usertype);

        SPUtils.setString(UiUtils.getContext(), GlobalConstants
                .sp_loginStatus, String.valueOf(userDesInfo.loginStatus));

        //存用户姓名
        String name = userDesInfo.name;

        if (!TextUtils.isEmpty(name) && !name.equals("null")) {
            SPUtils.setString(UiUtils.getContext(), GlobalConstants
                    .sp_user_name, userDesInfo.name);
        }

        //村用户实名认证
        LogUtils.e(TAG,"userDesInfo.IDnumStatus="+userDesInfo.IDnumberStatus);
        SPUtils.setInt(UiUtils.getContext(), GlobalConstants
                .sp_really_name_status, userDesInfo.IDnumberStatus);
    }

    /**
     * 清楚存储的数据
     */
    public void clearUserlocationData() {

        SPUtils.remove(UiUtils.getContext(), GlobalConstants.SP_USERID);
        SPUtils.setString(UiUtils.getContext(), GlobalConstants.SP_USERID, "");

        SPUtils.remove(UiUtils.getContext(), GlobalConstants.SP_USERNUM);
        SPUtils.setString(UiUtils.getContext(), GlobalConstants.SP_USERNUM, "");
        SPUtils.remove(UiUtils.getContext(), GlobalConstants.SP_USERPASS);
        SPUtils.setString(UiUtils.getContext(), GlobalConstants.SP_USERPASS,
                "");


        SPUtils.remove(UiUtils.getContext(), GlobalConstants.SP_use_iamgepath);
        SPUtils.setString(UiUtils.getContext(), GlobalConstants
                .SP_use_iamgepath, "");
        SPUtils.remove(UiUtils.getContext(), GlobalConstants.sp_use_ordercount);
        SPUtils.setString(UiUtils.getContext(), GlobalConstants
                .sp_use_ordercount, "");
        SPUtils.remove(UiUtils.getContext(), GlobalConstants
                .sp_use_finishcount);

        SPUtils.setString(UiUtils.getContext(), GlobalConstants
                .sp_use_finishcount, "");

        SPUtils.remove(UiUtils.getContext(), GlobalConstants
                .sp_use_unfinishcount);

        SPUtils.setString(UiUtils.getContext(), GlobalConstants
                .sp_use_unfinishcount, "");

        SPUtils.remove(UiUtils.getContext(), GlobalConstants.sp_loginStatus);
        SPUtils.setString(UiUtils.getContext(), GlobalConstants
                .sp_loginStatus, "");
        SPUtils.remove(UiUtils.getContext(), GlobalConstants.sp_user_type_name);
        SPUtils.setString(UiUtils.getContext(), GlobalConstants
                .sp_user_type_name, "");


        SPUtils.setString(UiUtils.getContext(), GlobalConstants
                .sp_user_name, "");
        SPUtils.remove(UiUtils.getContext(), GlobalConstants.sp_user_name);

        //村用户实名认证
        SPUtils.remove(UiUtils.getContext(), GlobalConstants
                .sp_really_name_status);
        SPUtils.setInt(UiUtils.getContext(), GlobalConstants
                .sp_really_name_status, 0);

        //停止检查的服务
        MyApplication.instance.stopCheckUser();

        LogUtils.e(TAG, "删除数据");

    }
}
