package com.mac.runtu.business;

import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.global.MarkerConstants;
import com.mac.runtu.utils.MyHttpUtils;

import java.util.HashMap;

public class FromNetInfo2AddsSelectStartFainlBiz {


    public interface OnDataListener {
        void onSuccess(String result);

        void onfail();
    }

    //地区选择提交数据到网络
    public static void setData2Netword(int pageNum,final OnDataListener
                                               mListener) {

        //llLoading.setVisibility(View.VISIBLE);

        HashMap<String, String> hashMap = new HashMap<>();
        //可以做为类型字段
        //hashMap.put(GlobalConstants.key_type, type);
        hashMap.put(GlobalConstants.key_start_province, MarkerConstants
                .value_province);
        hashMap.put(GlobalConstants.key_start_city, MarkerConstants.value_city);
        hashMap.put(GlobalConstants.key_start_county, MarkerConstants
                .value_county);
        hashMap.put(GlobalConstants.key_start_town, MarkerConstants.value_town);
        //hashMap.put(GlobalConstants.key_village, MarkerConstants
        // .value_village);
        hashMap.put(GlobalConstants.key_kind, MarkerConstants.value_kind);

        hashMap.put(GlobalConstants.KEY_PAGENUM, pageNum + "");
        hashMap.put(GlobalConstants.KEY_PAGESIZE, GlobalConstants
                .value_page_size);


        hashMap.put(GlobalConstants.key_final_province, MarkerConstants
                .value_fainl_province);
        hashMap.put(GlobalConstants.key_final_city, MarkerConstants
                .value_fainl_city);
        hashMap.put(GlobalConstants.key_final_county, MarkerConstants
                .value_fainl_county);
        hashMap.put(GlobalConstants.key_final_town, MarkerConstants
                .value_fainl_town);
        hashMap.put(GlobalConstants.key_final_village, MarkerConstants
                .value_fainl_village);

        //类型
        //hashMap.put(GlobalConstants.key_kind, MarkerConstants.value_kind);


        MyHttpUtils.getStringDataFromNet(GlobalConstants
                .Logistics_address_select_url, null, hashMap, new MyHttpUtils
                .OnNewWorkRequestListener() {

            @Override
            public void onNewWorkSuccess(String result) {
                if (result != null) {
                    //解析数据
                    //LogUtils.e(TAG, "劳务合做:地址搜索" + result);
                    //设置给listview
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onNewWorkError(String msg) {
                mListener.onfail();
            }
        });
    }
}