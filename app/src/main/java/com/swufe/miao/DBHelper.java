package com.swufe.miao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int VERSION=3;//版本号
    public static final String DB_NAME = "miao.db";
    public static final String TABLE_NAME = "userinfo";
    public static final String COLUMN_USERID = "uid";
    public static final String COLUMN_USERPWD = "upwd";

    public DBHelper(Context context) {
        super(context,DB_NAME,null,VERSION);
    }

    //创建数据库,只在没有数据库时执行
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE "+TABLE_NAME+"(" + COLUMN_USERID +
                    "TEXT PRIMARY KEY, "+ COLUMN_USERPWD + "TEXT)");
            //向数据库中插入测试账号
            db.execSQL("insert into " + TABLE_NAME + " values('123','123')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //真实场景下，数据库不能删
    //更新数据库版本(先删表，再建表)
    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL("DROP TABLE  IF EXISTS " + TABLE_NAME);
        db.execSQL("CREATE TABLE "+TABLE_NAME+"('" + COLUMN_USERID +
                "' TEXT PRIMARY KEY, '"+ COLUMN_USERPWD + "' TEXT)");
    }

}
