package com.mac.runtu.business;

import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnDataBooleanListener;
import com.mac.runtu.utils.MyHttpUtils;

/**
 * Description: 添加数据到购物车
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/15 0015 下午 5:41
 */
public class AddShopCar2NetBiz {

    public static void addData2ShopCar(String model, String infoJson, final
    OnDataBooleanListener listener) {


        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                        .shopping_car_add_url, null, GlobalConstants.key_info,
                infoJson, GlobalConstants.key_model, model, new MyHttpUtils
                        .OnNewWorkRequestListener2Boolean() {


                    @Override
                    public void onNewWorkSuccess(Boolean result) {


                        if (result) {
                            listener.onOk(result);


                        } else {

                            listener.onFail();

                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {

                        listener.onNetWorkFail();

                    }
                });

    }
}
