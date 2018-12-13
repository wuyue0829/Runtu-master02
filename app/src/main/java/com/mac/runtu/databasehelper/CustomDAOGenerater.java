package com.mac.runtu.databasehelper;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class CustomDAOGenerater {
    public static void main(String[] args) throws Exception {
        //第一个参数表示数据库版本号,
        // 如果发生变更会导致数据库更新的操作被调用(如果不修改生成的DaoMaster中的方法
        // 升级默认操作是删除所有表并重新建表)
        //第二个参数是生成的DAO类的包路径
        Schema schema = new Schema(1, "com.mac.runtu.databasehelper");

        // 创建表,参数为表名
        Entity entity = schema.addEntity("AddressDataInfo");

        // 为表添加字段
        entity.addIdProperty().primaryKey().autoincrement(); //自增长id为主键
        entity.addStringProperty("district_code").notNull();//非null字段
        entity.addStringProperty("district_name");//Int类型字段
        entity.addStringProperty("latitude");// String类型字段
        entity.addStringProperty("longitude");// String类型字段
        entity.addStringProperty("parent_code");// String类型字段


        // 生成数据库相关类
        //第二个参数指定生成文件的本次存储路径,AndroidStudio工程指定到当前工程的java路径
        new DaoGenerator().generateAll(schema,
                "D:\\RuntuProject\\Runtu-master02\\app\\src\\main\\java");
    }
}