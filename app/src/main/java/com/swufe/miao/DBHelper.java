package com.swufe.miao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int VERSION=1;//版本号
    public static final String DB_NAME = "miao.db";
    public static final String TABLE_NAME = "userinfo";
    public static final String COLUMN_USERID = "uid";
    public static final String COLUMN_USERPWD = "upwd";

    //创建数据库
    private static final String CREATE_TABLE = "create table if not exists "
            + TABLE_NAME + "(" + COLUMN_USERID + " text not null primary key,"
            + COLUMN_USERPWD + " text not null)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    //创建数据库
    //只在没有数据库时执行
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            db.execSQL("insert into " + TABLE_NAME + " values('11','11')");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //真实场景下，数据库不能删
    //更新数据库版本(先删表，再建表)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table  if exists " + TABLE_NAME);
        db.execSQL(CREATE_TABLE);
    }

    //登录
    public User userlogin(String userId, String userPwd) {
        User user = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_USERID, COLUMN_USERPWD},
                COLUMN_USERID + "=? and " + COLUMN_USERPWD + "=?",
                new String[]{userId, userPwd},
                null,
                null,
                null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new User();
            user.setUserId(cursor.getString(cursor.getColumnIndex(COLUMN_USERID)));
            user.setUserPwd(cursor.getString(cursor.getColumnIndex(COLUMN_USERPWD)));

        }
        return user;
    }

    //注册
    public long registerUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERID, user.getUserId());
        contentValues.put(COLUMN_USERPWD, user.getUserPwd());
        return db.insert(TABLE_NAME, null, contentValues);

    }
}
