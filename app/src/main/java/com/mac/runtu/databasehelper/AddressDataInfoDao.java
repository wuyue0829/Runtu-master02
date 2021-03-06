package com.mac.runtu.databasehelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ADDRESS_DATA_INFO".
*/
public class AddressDataInfoDao extends AbstractDao<AddressDataInfo, Long> {

    public static final String TABLENAME = "ADDRESS_DATA_INFO";

    /**
     * Properties of entity AddressDataInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property District_code = new Property(1, String.class, "district_code", false, "DISTRICT_CODE");
        public final static Property District_name = new Property(2, String.class, "district_name", false, "DISTRICT_NAME");
        public final static Property Latitude = new Property(3, String.class, "latitude", false, "LATITUDE");
        public final static Property Longitude = new Property(4, String.class, "longitude", false, "LONGITUDE");
        public final static Property Parent_code = new Property(5, String.class, "parent_code", false, "PARENT_CODE");
    };


    public AddressDataInfoDao(DaoConfig config) {
        super(config);
    }
    
    public AddressDataInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ADDRESS_DATA_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"DISTRICT_CODE\" TEXT NOT NULL ," + // 1: district_code
                "\"DISTRICT_NAME\" TEXT," + // 2: district_name
                "\"LATITUDE\" TEXT," + // 3: latitude
                "\"LONGITUDE\" TEXT," + // 4: longitude
                "\"PARENT_CODE\" TEXT);"); // 5: parent_code
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ADDRESS_DATA_INFO\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, AddressDataInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getDistrict_code());
 
        String district_name = entity.getDistrict_name();
        if (district_name != null) {
            stmt.bindString(3, district_name);
        }
 
        String latitude = entity.getLatitude();
        if (latitude != null) {
            stmt.bindString(4, latitude);
        }
 
        String longitude = entity.getLongitude();
        if (longitude != null) {
            stmt.bindString(5, longitude);
        }
 
        String parent_code = entity.getParent_code();
        if (parent_code != null) {
            stmt.bindString(6, parent_code);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public AddressDataInfo readEntity(Cursor cursor, int offset) {
        AddressDataInfo entity = new AddressDataInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // district_code
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // district_name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // latitude
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // longitude
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // parent_code
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, AddressDataInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDistrict_code(cursor.getString(offset + 1));
        entity.setDistrict_name(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLatitude(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLongitude(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setParent_code(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(AddressDataInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(AddressDataInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
