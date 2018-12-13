package com.mac.runtu.business;

import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.utils.MyHttpUtils;

import java.util.HashMap;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/5 0005 下午 6:50
 */
public class SpecialtyGoodGetFromNetBiz {

    public interface OnSpecialtyDataListener {
        void onSuccess(String result);

        void onFail();
    }

    //访问网络得到数据
    public static void getSpecialtyGoodData4NetPayment(int num,
                                                       String type,
                                                       String payment, String
                                                               model, final
                                                       OnSpecialtyDataListener
                                                               listener) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.KEY_PAGENUM, num + "");
        hashMap.put(GlobalConstants.KEY_PAGESIZE, GlobalConstants
                .value_page_size);
        //类型 1 地方推荐
        hashMap.put(GlobalConstants.key_type, type);
        hashMap.put(GlobalConstants.key_payment, payment);
        hashMap.put(GlobalConstants.key_model, model);


        MyHttpUtils.getStringDataFromNet(GlobalConstants.specialty_list_url,
                null, hashMap, new MyHttpUtils.OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {
                        if (result!=null) {
                            listener.onSuccess(result);
                        }

                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        listener.onFail();
                    }
                });

    }

    //访问网络得到数据
    public static void getSpecialtyGoodData4NetMinPrice(int num,
                                                        String type,
                                                        String minPrice,
                                                        String model, final
                                                        OnSpecialtyDataListener
                                                                listener) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.KEY_PAGENUM, num + "");
        hashMap.put(GlobalConstants.KEY_PAGESIZE, GlobalConstants
                .value_page_size);
        //类型 1 地方推荐
        hashMap.put(GlobalConstants.key_type, type);

        hashMap.put(GlobalConstants.key_minPrice, minPrice);
        hashMap.put(GlobalConstants.key_model, model);

        MyHttpUtils.getStringDataFromNet(GlobalConstants.specialty_list_url,
                null, hashMap, new MyHttpUtils.OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {
                        if (result!=null) {
                            listener.onSuccess(result);
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        listener.onFail();
                    }
                });

    }

    //访问网络得到数据
    public static void getSpecialtyGoodData4NetHitCount(int num,
                                                        String type, String
                                                                hitCount,
                                                        String model, final
                                                        OnSpecialtyDataListener
                                                                listener) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.KEY_PAGENUM, num + "");
        hashMap.put(GlobalConstants.KEY_PAGESIZE, GlobalConstants
                .value_page_size);
        //类型 1 地方推荐
        hashMap.put(GlobalConstants.key_type, type);
        hashMap.put(GlobalConstants.key_hitCount, hitCount);
        hashMap.put(GlobalConstants.key_model, model);

        MyHttpUtils.getStringDataFromNet(GlobalConstants.specialty_list_url,
                null, hashMap, new MyHttpUtils.OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {
                        if (result!=null) {
                            listener.onSuccess(result);
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        listener.onFail();
                    }
                });

    }


    //访问网络得到数据
    public static void getGoodRecommData4Net(int num, String model,
                                             final OnSpecialtyDataListener
                                                     listener) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(GlobalConstants.KEY_PAGENUM, num + "");
        hashMap.put(GlobalConstants.KEY_PAGESIZE, GlobalConstants
                .value_page_size);
        //类型 1 地方推荐

        //hashMap.put(GlobalConstants.key_hitCount, hitCount);
        hashMap.put(GlobalConstants.key_model, model);

        MyHttpUtils.getStringDataFromNet(GlobalConstants
                .specialty_product_recommend_url, null, hashMap, new MyHttpUtils
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
