package com.mac.runtu.databasehelper;

import android.database.sqlite.SQLiteDatabase;

import com.mac.runtu.utils.UiUtils;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/12/6 0006 下午 4:08
 */
public class AddressDaoBiz {

    // 该初始化过程最好放在Application中进行,避免创建多个Session
    public AddressDataInfoDao dao;

    public static AddressDaoBiz mInstance;

    private AddressDaoBiz() {
    }

    public static AddressDaoBiz getInstance() {
        if (mInstance == null) {
            mInstance = new AddressDaoBiz();
        }

        return mInstance;
    }

    public void setupDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper创建数据库
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表
        /**
         * @param context :　Context
         * @param name : 数据库名字
         * @param factory : CursorFactroy
         */

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(UiUtils
                .getContext(),
                "Address.db", null);
        // 获取数据库
        SQLiteDatabase database = helper.getWritableDatabase();
        // 获取DaoMaster
        DaoMaster daoMaster = new DaoMaster(database);
        // 获取Session
        DaoSession daoSession = daoMaster.newSession();
        // 获取对应的表的DAO对象
        dao = daoSession.getAddressDataInfoDao();


    }

    public AddressDataInfoDao getAddressDataInfoDao() {
        if (dao == null) {
            setupDatabase();
        }
        //return dao;
        return dao;
    }
}
