package com.swufe.miao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBBillHelper extends SQLiteOpenHelper {
    private static final int VERSION = 3;
    private static final String DB_NAME = "miao.db";
    public static final String TABLE_NAME = "db_bills";

    public DBBillHelper(Context context) {
        super(context,DB_NAME,null,VERSION);
    }

    //id   uid   cid flag cost year month day
    //int String int int float int int int

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE "+TABLE_NAME+
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT,UID TEXT,CID INTEGER," +
                    "FLAG INTEGER,COST FLOAT,YEAR INTEGER,MONTH INTEGER,DAY INTEGER)");
            //向数据库中插入测试账号
            db.execSQL("insert into " + TABLE_NAME + " values(1,'123',1,1,125.5,2021,10,10)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        arg0.execSQL("DROP TABLE  IF EXISTS " + TABLE_NAME);
        arg0.execSQL("CREATE TABLE "+TABLE_NAME+
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT,UID TEXT,CID INTEGER," +
                "FLAG INTEGER,COST FLOAT,YEAR INTEGER,MONTH INTEGER,DAY INTEGER)");
    }
}
