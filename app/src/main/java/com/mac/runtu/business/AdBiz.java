package com.mac.runtu.business;

import com.mac.runtu.R;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.AdAddressInfo;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.MyHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description: 从网络等到广告图片地址
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/20 0020 上午 8:57
 */
public class AdBiz {

    private static ArrayList<Integer> images;

    /**
     * 回调接口
     */
    public interface OnAdInfoListener {
        void onSuccessInfoList(ArrayList<AdAddressInfo.TopImageData>
                                       infoList, ArrayList<String>
                                       imageUrlList);


        void onfail();
    }

    /**
     * 访问网路加载数据
     * @param type
     * @param mListener
     */
    public static void getAdInfo(final String type, final OnAdInfoListener
            mListener) {

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(GlobalConstants.KEY_PAGENUM, "1");
            hashMap.put(GlobalConstants.KEY_PAGESIZE, "3");
            hashMap.put(GlobalConstants.key_type, type);


            MyHttpUtils.getStringDataFromNet(GlobalConstants.AD_INFO_URL, null,
                    hashMap, new MyHttpUtils.OnNewWorkRequestListener() {


                        @Override
                        public void onNewWorkSuccess(String result) {
                            if (result != null) {
                                parseData(result, mListener);

                            }
                        }

                        @Override
                        public void onNewWorkError(String msg) {
                            mListener.onfail();
                        }
                    });


    }


    public static void getAdInfo1(String paramString1, String paramString2, final OnAdInfoListener paramOnAdInfoListener)
    {
        HashMap localHashMap = new HashMap();
        localHashMap.put("pageNum", "1");
        localHashMap.put("pageSize", "3");
        localHashMap.put("type", paramString1);
        localHashMap.put("model", paramString2);
        localHashMap.put("pictureType", "7");
        MyHttpUtils.getStringDataFromNet("http://101.201.102.161/app/PictureAction/pictureList_app.action", null, localHashMap, new MyHttpUtils.OnNewWorkRequestListener()
        {
            public void onNewWorkError(String paramAnonymousString)
            {
                paramOnAdInfoListener.onfail();
            }

            public void onNewWorkSuccess(String paramAnonymousString)
            {
                if (paramAnonymousString != null) {
                    AdBiz.parseData(paramAnonymousString, paramOnAdInfoListener);
                }
            }
        });
    }

    /**
     * 解析网路数据
     * @param result
     * @param mListener
     */
    private static void parseData(String result, OnAdInfoListener mListener) {
        AdAddressInfo adAddressInfo = GSonUtil.parseJson
                (result,
                        AdAddressInfo.class);

        ArrayList<AdAddressInfo.TopImageData> objList =
                adAddressInfo.objList;

        if (objList != null) {
            ArrayList<String> images = new ArrayList<String>();

            for (int i = 0; i < objList.size(); i++) {
                if(objList.get(i).picture_type == 1){
                    images.add(GlobalConstants.Base_imgae_url
                            + objList.get(i).picture_name.trim());
                }

                if(objList.get(i).picture_type == 2){
                    images.add(GlobalConstants.Base_imgae_url
                            + objList.get(i).picture_name.trim());
                }
            }

            mListener.onSuccessInfoList(objList, images);

        } else {
            mListener.onfail();
        }
    }

    /**
     * 本地图片
     * @return
     */
    public static ArrayList<Integer> getImageListId() {
        if (images == null) {
            images = new ArrayList<>();
            images.add(R.drawable.image_home01);
            images.add(R.drawable.image_home02);
            images.add(R.drawable.image_home03);
            images.add(R.drawable.image_home04);
        }

        return images;
    }

    /**
     * 拼接网路图片加载路径
     * @param pictureInfos
     * @return
     */
    public static ArrayList<String> getImageListUrl(ArrayList<PictureInfo>
                                                            pictureInfos) {
        ArrayList<String> imageUrls = new ArrayList<>();
        if (pictureInfos != null && pictureInfos.size() > 0) {
            for (int i = 0; i < pictureInfos.size(); i++) {
                imageUrls.add(GlobalConstants.Base_imgae_url + pictureInfos.get
                        (i).pictureName.trim());
            }
        }

        return imageUrls;
    }

}
