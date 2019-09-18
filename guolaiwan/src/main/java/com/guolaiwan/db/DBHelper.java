package com.guolaiwan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.cgx.library.utils.FileUtils;
import com.cgx.library.utils.SDCardUtils;
import com.guolaiwan.constant.Constant;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.cgx.library.utils.SDCardUtils.*;
import static com.guolaiwan.App.APP_FILE_PATH;

/**
 * 作者：chengx
 * 日期：2016/12/23
 * 描述：
 */

public class DBHelper {
    public static final String DB_NAME = "guolaiwan.db";
    private AndroidConnectionSource connectionSource;
    private static DBHelper dbHelper;
    public static DBHelper getInstance(Context context){
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }

    private DBHelper(Context context)  {
        File file = new File(APP_FILE_PATH,DB_NAME);
        if (!file.exists()){
            try {
                FileUtils.copyFileFromAssets(context,file,"miy.db");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getPath(),null, SQLiteDatabase.OPEN_READWRITE);
        connectionSource = new AndroidConnectionSource(db);
    }

    public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws Exception {
        if (connectionSource != null) {
            return DaoManager.createDao(connectionSource, clazz);
        }
        return null;
    }

    public <T> List<T> queryForAll(Class<T> clazz){
        try {
            return getDao(clazz).queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> query(Class<T> clazz, Map<String,Object> map){
        try {
            Dao<T,?> dao = getDao(clazz);
            QueryBuilder<T,?> queryBuilder = dao.queryBuilder();
            if (!map.isEmpty()){
                Where<T,?> where = queryBuilder.where();
                Set<String> keySet = map.keySet();
                ArrayList<String> keys = new ArrayList<>();
                keys.addAll(keySet);
                for (int i = 0; i < keys.size(); i++) {
                        if (i == 0){
                            where.eq(keys.get(i),map.get(keys.get(i)));
                        }else {
                            where.and().eq(keys.get(i),map.get(keys.get(i)));
                        }
                }
                PreparedQuery<T> preparedQuery = queryBuilder.prepare();
                return dao.query(preparedQuery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
