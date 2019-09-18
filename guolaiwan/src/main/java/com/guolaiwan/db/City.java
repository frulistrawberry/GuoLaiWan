package com.guolaiwan.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 作者：chengx
 * 日期：2016/12/23
 * 描述：
 */
@DatabaseTable(tableName = "province_city")
public class City {
    @DatabaseField(columnName = "ty_id",id = true)
    private int ty_id;
    @DatabaseField(columnName = "id")
    private int id;
    @DatabaseField(columnName = "prov_id")
    private int prov_id;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "status")
    private int status;

    public int getTy_id() {
        return ty_id;
    }

    public void setTy_id(int ty_id) {
        this.ty_id = ty_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProv_id() {
        return prov_id;
    }

    public void setProv_id(int prov_id) {
        this.prov_id = prov_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
