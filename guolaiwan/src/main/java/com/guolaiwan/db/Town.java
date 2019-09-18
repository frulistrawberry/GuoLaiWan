package com.guolaiwan.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 作者：chengx
 * 日期：2016/12/23
 * 描述：
 */

@DatabaseTable(tableName = "province_city_area_town")
public class Town {
    @DatabaseField(columnName = "ty_id",id = true)
    private int ty_id;
    @DatabaseField(columnName = "id")
    private int id;
    @DatabaseField(columnName = "prov_id")
    private int prov_id;
    @DatabaseField(columnName = "city_id")
    private int city_id;
    @DatabaseField(columnName = "area_id")
    private int area_id;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "status")
    private int status;
}
