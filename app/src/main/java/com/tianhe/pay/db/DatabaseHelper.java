package com.tianhe.pay.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TABLE_NAME = "tianhe_pay.db";
    private static int DB_VERSION = 3;

    private HashMap<String, Dao> daoMap = new HashMap<String, Dao>();

    public DatabaseHelper(Context context) {
        super(context.getApplicationContext(), TABLE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource,
                    OrderHeaderEntity.class);
            TableUtils.createTableIfNotExists(connectionSource,
                    OrderItemEntity.class);
            TableUtils.createTableIfNotExists(connectionSource,
                    PaidInfoEntity.class);
            TableUtils.createTableIfNotExists(connectionSource,
                    OrderSimpleEntity.class);
        } catch (SQLException e) {
            Log.e("db", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int
            oldVersion, int newVersion) {
        try {
            if (oldVersion == 1 && newVersion == 2) {
                TableUtils.createTableIfNotExists(connectionSource,
                        OrderSimpleEntity.class);
            } else {
                TableUtils.dropTable(connectionSource, OrderHeaderEntity.class, true);
                TableUtils.dropTable(connectionSource, OrderItemEntity.class, true);
                TableUtils.dropTable(connectionSource, PaidInfoEntity.class, true);
                onCreate(database, connectionSource);
            }
        } catch (SQLException e) {
            Log.e("db", e.toString());
        }
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();
        if (daoMap.containsKey(className)) {
            dao = daoMap.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daoMap.put(className, dao);
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        daoMap.clear();
    }
}
