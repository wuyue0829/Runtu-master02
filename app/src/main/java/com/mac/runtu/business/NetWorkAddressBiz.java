package com.mac.runtu.business;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mac.runtu.databasehelper.AddressDaoBiz;
import com.mac.runtu.databasehelper.AddressDataInfo;
import com.mac.runtu.databasehelper.AddressDataInfoDao;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.interfaceif.OnAddressDataListener;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/24 0024 下午 3:11
 */
public class NetWorkAddressBiz {


    private static final String                     TAG        =
            "NetWorkAddressBiz";



    public static void getAddressDataFromNet(String parentCode, final
    OnAddressDataListener mListener) {

        if (TextUtils.isEmpty(parentCode)) {
            ArrayList<AddressDataInfo> mEmptyList = new
                    ArrayList<>();
            AddressDataInfo info = new AddressDataInfo();
            info.district_code = "";
            info.district_name = "无";
            info.parent_code = "";
            info.latitude = "0.00";
            info.longitude = "0.00";
            mEmptyList.add(info);

            mListener.onSuccess(mEmptyList);
            return;
        }

        //先查数据库
        List<AddressDataInfo> addressList = getAddressList4DataBase
                (parentCode);
        // List<AddressDataInfo> addressList =null;


        if (addressList != null && addressList.size() > 0) {

            if (mListener != null) {

                mListener.onSuccess((ArrayList<AddressDataInfo>) addressList);
            }

        } else {

            LogUtils.e(TAG, "从网络查询地址11");

            //网路查询
            MyHttpUtils.getStringDataFromNet(GlobalConstants.ADDRESS_URL, null,
                    GlobalConstants.KEY_ADDRESS
                    , parentCode, new MyHttpUtils.OnNewWorkRequestListener() {

                        @Override
                        public void onNewWorkSuccess(String result) {

                            LogUtils.e(TAG, "result==" + result);
                            LogUtils.e(TAG, "从网络查询地址22");

                            if (result != null) {
                                LogUtils.e(TAG, "从网络查询地址");

                                Gson gson = new Gson();

                                Type type = new
                                        TypeToken<ArrayList<AddressDataInfo>>
                                                () {
                                        }.getType();
                                //从网络得到的地址集合

                                ArrayList<AddressDataInfo> mAddresslist = gson
                                        .fromJson(result, type);

                                mListener.onSuccess(getAddressDataInfos
                                        (mAddresslist));

                            } else {
                                mListener.onSuccess(null);
                            }


                        }

                        @Override
                        public void onNewWorkError(String msg) {
                            mListener.onfail();

                        }
                    });
        }

    }

    @NonNull
    private static ArrayList<AddressDataInfo> getAddressDataInfos
            (ArrayList<AddressDataInfo> mAddresslist) {

        ArrayList<AddressDataInfo> mList = new ArrayList<>();

        if (mAddresslist != null && mAddresslist.size
                () > 0) {

            for (int i = 0; i < mAddresslist.size(); i++) {

                if (mAddresslist.get(i).district_name.equals("陕西省")) {
                    mList.add(0, mAddresslist.get(i));
                } else if (mAddresslist.get(i).district_name.equals("延安市")) {
                    mList.add(0, mAddresslist.get(i));
                } else if (mAddresslist.get(i).district_name.equals("洛川县")) {
                    mList.add(0, mAddresslist.get(i));
                } else if (mAddresslist.get(i).district_name.equals("朱牛乡")) {
                    mList.add(0, mAddresslist.get(i));
                } else if (mAddresslist.get(i).district_name.equals("贠李河村")) {
                    mList.add(0, mAddresslist.get(i));
                } else {
                    mList.add(mAddresslist.get(i));
                }
            }

            AddressDataInfo addressDataInfo = new
                    AddressDataInfo();
            addressDataInfo.district_code = "";
            addressDataInfo.district_name = "无";
            addressDataInfo.latitude = "0.00";
            addressDataInfo.longitude = "0.00";
            addressDataInfo.parent_code =
                    mAddresslist.get(0).parent_code;

            mList.add(addressDataInfo);

            //插入数据库
            LogUtils.e(TAG, "地址集合数据加入 无=" +
                    mAddresslist.toString());

            insertData(mList);

        }

        return mList;
    }

    /**
     * 查询数据库
     * @param parentCode
     * @return
     */
    private static List<AddressDataInfo> getAddressList4DataBase(String parentCode) {

        AddressDataInfoDao dao = AddressDaoBiz.getInstance()
                .getAddressDataInfoDao();

        List<AddressDataInfo> parent_code = dao
                .queryRaw("where parent_code = ?", parentCode);


        LogUtils.e(TAG, "查询走数据库List<AddressDataInfo>=" + parent_code.toString
                ());

        return parent_code;
    }


    /**
     * 地址插入数据库
     * @param mAddresslist
     */
    private static void insertData(final ArrayList<AddressDataInfo>
                                           mAddresslist) {


        AddressDataInfoDao dao = AddressDaoBiz.getInstance()
                .getAddressDataInfoDao();


        Query query = dao.queryBuilder().where(AddressDataInfoDao.Properties
                .District_name.eq("陕西省"), AddressDataInfoDao.Properties
                .Parent_code.eq("0"))
                .build();

        for (int i = 0; i < mAddresslist.size(); i++) {
            //插入之前想判断是否已经存在

            //query.setParameter(0, mAddresslist.get(i).district_code);
            query.setParameter(0, mAddresslist.get(i).district_name);
            query.setParameter(1, mAddresslist.get(i).parent_code);

            Object unique = query.unique();

            if (unique == null) {
                dao.insertOrReplace(mAddresslist.get(i));
                LogUtils.e(TAG, "插入数据库的次数");
            }

        }

    }
}
