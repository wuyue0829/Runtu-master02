package com.mac.runtu.utils;


import android.app.Activity;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Description: 网络数据交互工具类
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/9/9 22:35
 */
public class MyHttpUtils {

    /**
     * 回调
     */
    public interface OnNewWorkRequestListener {

        //成功
        public void onNewWorkSuccess(String result);

        //失败
        public void onNewWorkError(String msg);

    }

    /**
     * 回调
     */
    public interface OnNewWorkRequestListener2Boolean {

        //成功
        public void onNewWorkSuccess(Boolean result);

        //失败
        public void onNewWorkError(String msg);

    }

    /**
     * 回调
     */
    public interface OnNewWorkRequestListener2File {

        //成功
        public void onNewWorkSuccess(File file);

        //失败
        public void onNewWorkError(String msg);

        //进度
        public void inProgress(float progress, long total, int id);

    }

    /**
     * 取消请求
     */
    public static void cancelTag(Object obj) {
        OkHttpUtils.getInstance().cancelTag(obj);
    }

    /**
     * get请求 返回String
     *
     * @param url
     * @param obj
     * @param mListener
     */
    public static void getStringDataFromNet(String url, Object obj, final
    OnNewWorkRequestListener mListener) {

        //http://192.168.0.101:8080/update008.json

        OkHttpUtils
                .get()
                .url(url)
                .tag(obj)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mListener.onNewWorkError(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mListener.onNewWorkSuccess(response);
                    }
                });

    }


    /**
     * get请求 返回布尔值
     *
     * @param url
     * @param obj
     * @param mListener
     */
    public static void getBooleanDataFromNet(String url, Object obj, final
    OnNewWorkRequestListener2Boolean mListener) {

        //http://192.168.0.101:8080/update008.json

        OkHttpUtils
                .get()
                .url(url)
                .tag(obj)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mListener.onNewWorkError(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mListener.onNewWorkSuccess(Boolean.valueOf(response));
                    }
                });

    }


    /**
     * post请求 单键值对 返回String
     *
     * @param url
     * @param obj
     * @param key
     * @param value
     * @param mListener
     */
    public static void getStringDataFromNet(String url, Object obj,
                                            String key, String value, final
                                            OnNewWorkRequestListener
                                                    mListener) {

        OkHttpUtils
                .post()
                .url(url)
                .tag(obj)
                .addParams(key, value)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mListener.onNewWorkError(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mListener.onNewWorkSuccess(response);
                    }
                });

    }

    /**
     * 两个参数
     *
     * @param url
     * @param obj
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @param mListener
     */
    public static void getStringDataFromNet(String url, Object obj,
                                            String key1, String value1,
                                            String key2, String value2, final
                                            OnNewWorkRequestListener
                                                    mListener) {

        OkHttpUtils
                .post()
                .url(url)
                .tag(obj)
                .addParams(key1, value1)
                .addParams(key2, value2)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mListener.onNewWorkError(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mListener.onNewWorkSuccess(response);
                    }
                });

    }

    /**
     * post请求 单键值对 返回Boolean
     *
     * @param url
     * @param obj
     * @param key
     * @param value
     * @param mListener
     */
    public static void getBooleanDataFromNet(String url, Object obj,
                                             String key, String value, final
                                             OnNewWorkRequestListener2Boolean
                                                     mListener) {

        OkHttpUtils
                .post()
                .url(url)
                .tag(obj)
                .addParams(key, value)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mListener.onNewWorkError(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        mListener.onNewWorkSuccess(Boolean.valueOf(response));
                    }
                });

    }

    /**
     * @param url
     * @param obj
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @param mListener
     */
    public static void getBooleanDataFromNet(String url, Object obj,
                                             String key1, String value1,
                                             String key2, String value2, final
                                             OnNewWorkRequestListener2Boolean
                                                     mListener) {

        OkHttpUtils
                .post()
                .url(url)
                .tag(obj)
                .addParams(key1, value1)
                .addParams(key2, value2)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mListener.onNewWorkError(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        mListener.onNewWorkSuccess(Boolean.valueOf(response));
                    }
                });

    }

    /**
     * @param url
     * @param obj
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @param mListener
     */
    public static void getBooleanDataFromNet(String url, Object obj,
                                             String key1, String value1,
                                             String key2, String value2,
                                             String key3, String value3, final
                                             OnNewWorkRequestListener2Boolean
                                                     mListener) {

        OkHttpUtils
                .post()
                .url(url)
                .tag(obj)
                .addParams(key1, value1)
                .addParams(key2, value2)
                .addParams(key3, value3)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mListener.onNewWorkError(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        mListener.onNewWorkSuccess(Boolean.valueOf(response));
                    }
                });

    }


    /**
     * post请求(单参数/多参数)  返回String
     *
     * @param url
     * @param obj
     * @param map
     * @param mListener
     */
    public static void getStringDataFromNet(String url, Object obj,
                                            HashMap<String, String>
                                                    map, final
                                            OnNewWorkRequestListener
                                                    mListener) {

        PostFormBuilder tag = OkHttpUtils
                .post()
                .url(url)
                .tag(obj);

        PostFormBuilder builder = null;

        Set<Map.Entry<String, String>> set = map.entrySet();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder = tag.addParams(entry.getKey(), entry
                    .getValue());
        }

        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mListener.onNewWorkError(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //成功回调,返回值市json
                        mListener.onNewWorkSuccess(response);
                    }
                });

    }


    public static void getBooleanDataFromNet(String url, Object obj,
                                             HashMap<String, String>
                                                     map, final
                                             OnNewWorkRequestListener2Boolean
                                                     mListener) {

        PostFormBuilder tag = OkHttpUtils
                .post()
                .url(url)
                .tag(obj);

        PostFormBuilder builder = null;

        Set<Map.Entry<String, String>> set = map.entrySet();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder = tag.addParams(entry.getKey(), entry
                    .getValue());
        }

        builder.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mListener.onNewWorkError(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //成功回调,返回值市json
                        mListener.onNewWorkSuccess(Boolean.valueOf(response));
                    }
                });

    }


    /**
     * post请求上传文件 返回Boolean
     *
     * @param url
     * @param file
     * @param mListener
     */
    public static void uploadingResources(String url, File file,
                                          final OnNewWorkRequestListener2Boolean
                                                  mListener) {

        OkHttpUtils
                .postFile()
                .url(url)
                .file(file)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //失败回调
                        mListener.onNewWorkError(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //成功回调
                        mListener.onNewWorkSuccess(Boolean.valueOf(response));
                    }
                });

    }

    /**
     * okhttp(原) 上传多张图片到服务器
     *
     * @param activity
     * @param url
     * @param mImgUrls
     * @param mListener
     */
    public static void uploadingMoreRes(final Activity activity,
                                        String url,
                                        ArrayList<String> mImgUrls,
                                        final OnNewWorkRequestListener2Boolean
                                                mListener) {

        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        OkHttpClient okHttpClient = new OkHttpClient();

        // mImgUrls为存放图片的url集合
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType
                (MultipartBody.FORM);

        for (int i = 0; i < mImgUrls.size(); i++) {
            File f = new File(mImgUrls.get(i));
            if (f != null) {

                //key  "img"
                //文件名
                mbody.addFormDataPart("image" + i, f.getName(), RequestBody
                        .create(MEDIA_TYPE_PNG, f));

            }
        }

        //添加其它信息
        //        builder.addFormDataPart("time",takePicTime);
        //        builder.addFormDataPart("mapX", SharedInfoUtils
        // .getLongitude());
        //        builder.addFormDataPart("mapY",SharedInfoUtils.getLatitude());
        //        builder.addFormDataPart("name",SharedInfoUtils.getUserName());

        RequestBody requestBody = mbody.build();

        //构建请求
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID" + "...")
                .url(url)//地址
                .post(requestBody)//添加请求体
                .build();

        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                final String eMessage = e.getLocalizedMessage();

                // System.out.println("上传失败:e.getLocalizedMessage() = " + e
                // .getLocalizedMessage());
                //失败
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        mListener.onNewWorkError(eMessage);

                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) {

                try {
                    final String message = response.body()
                            .string();

                    //成功
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mListener.onNewWorkSuccess(Boolean.valueOf
                                    (message));
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });


    }


    /**
     * json上传  返回字符串
     *
     * @param url
     * @param obj
     * @param mListener
     */
    public static void uploadingJsonGetStr(String url, Object obj, String
            data, final OnNewWorkRequestListener mListener) {
        OkHttpUtils
                .postString()
                .url(url)
                .content(data)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //失败回调
                        mListener.onNewWorkError(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //成功回调
                        mListener.onNewWorkSuccess(response);
                    }
                });
    }

    /**
     * json上传  返回Boolean
     *
     * @param url
     * @param obj
     * @param json
     * @param mListener
     */
    public static void uploadingJsonGetBoolean(String url, Object obj,
                                               String json,
                                               final
                                               OnNewWorkRequestListener2Boolean mListener) {
        //LogUtils.e("Myhttp", json);

        OkHttpUtils
                .postString()
                .url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //失败回调
                        mListener.onNewWorkError(e.toString());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //成功回调
                        mListener.onNewWorkSuccess(Boolean.valueOf(response));
                    }
                });
    }

    /**
     * json上传  返回Boolean
     *
     * @param url
     * @param obj
     * @param json
     * @param mListener
     */
    public static void uploadingJsonGetBoolean(String url, Object obj,
                                               Object json,
                                               final
                                               OnNewWorkRequestListener2Boolean mListener) {
        OkHttpUtils
                .postString()
                .url(url)
                .content(new Gson().toJson(json))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //失败回调
                        mListener.onNewWorkError(e.toString());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //成功回调
                        mListener.onNewWorkSuccess(Boolean.valueOf(response));
                    }
                });
    }

    /**
     * 下载文件
     *
     * @param url
     * @param filepath
     * @param fileName
     * @param mListener
     */
    public static void getFileFromNetWork(String url, String filepath, String
            fileName, final OnNewWorkRequestListener2File mListener) {

        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(filepath, fileName) {

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        mListener.inProgress(progress, total, id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mListener.onNewWorkError(e.toString());
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        mListener.onNewWorkSuccess(response);
                    }
                });
    }


}
