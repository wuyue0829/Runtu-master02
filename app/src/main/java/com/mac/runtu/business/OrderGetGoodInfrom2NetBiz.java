package com.mac.runtu.business;

import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnGetData2BooleanListener;
import com.mac.runtu.utils.MyHttpUtils;

/**
 * Description: 通知服务端 收货了
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/22 0022 下午 12:32
 */
public class OrderGetGoodInfrom2NetBiz {

    public static void infromFWD(String uuid, String status, final
    OnGetData2BooleanListener listener) {
        MyHttpUtils.getBooleanDataFromNet(GlobalConstants
                .order_get_good_inform_fwd_url, null, GlobalConstants
                .key_uuid, uuid, GlobalConstants.key_status, status, new
                MyHttpUtils.OnNewWorkRequestListener2Boolean() {


                    @Override
                    public void onNewWorkSuccess(Boolean result) {

                        listener.onSuccess(result);
                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        listener.onFail();
                    }
                });
    }
}
