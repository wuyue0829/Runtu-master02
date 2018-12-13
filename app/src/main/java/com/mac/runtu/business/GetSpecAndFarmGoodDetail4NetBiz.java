package com.mac.runtu.business;

import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnGetData4NetListener;
import com.mac.runtu.utils.MyHttpUtils;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/16 0016 下午 8:44
 */
public class GetSpecAndFarmGoodDetail4NetBiz {


    public static void getData4Net(String uuid, final OnGetData4NetListener
            listener) {

        MyHttpUtils.getStringDataFromNet(GlobalConstants
                .specialty_product_detail_url, null, GlobalConstants
                .key_uuid, uuid, new MyHttpUtils
                .OnNewWorkRequestListener() {


            @Override
            public void onNewWorkSuccess(String result) {
                if (result != null) {
                    listener.onSuccess(result);
                }
            }

            @Override
            public void onNewWorkError(String msg) {
                listener.onFail();
            }
        });
    }
}
