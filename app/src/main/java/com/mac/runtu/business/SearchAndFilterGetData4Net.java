package com.mac.runtu.business;

import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.interfaceif.OnGetData4NetListener;
import com.mac.runtu.utils.MyHttpUtils;

import java.util.HashMap;

/**
 * Description:  特产电商 体验农场  筛选 搜索
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/16 0016 下午 5:30
 */
public class SearchAndFilterGetData4Net {

    public static void getData4Net(int pageNum, String title,String model, final
    OnGetData4NetListener listener) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.key_tilte, title);
        hashMap.put(GlobalConstants.KEY_PAGENUM, pageNum + "");
        hashMap.put(GlobalConstants.KEY_PAGESIZE, GlobalConstants
                .value_page_size+"");
        //二级分类
        //全局变量
        hashMap.put(GlobalConstants.key_kind, MarkerConstants.value_kind+"");

        //地区 县
        hashMap.put(GlobalConstants.key_county, MarkerConstants.value_county+"");

        //区分标志
        hashMap.put(GlobalConstants.key_model, model+"");

        MyHttpUtils.getStringDataFromNet(GlobalConstants
                        .specialtey_search_filter_url,

                null, hashMap, new MyHttpUtils.OnNewWorkRequestListener() {

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
