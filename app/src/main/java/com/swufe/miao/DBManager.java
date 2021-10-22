package com.swufe.miao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DBHelper dbHelper;
    private String TBNAME;
    public static final String COLUMN_USERID = "uid";
    public static final String COLUMN_USERPWD = "upwd";

    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
        TBNAME = DBHelper.TABLE_NAME;
    }

    //登录
    public User userlogin(String userId, String userPwd) {
        User user = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME,
                new String[]{COLUMN_USERID, COLUMN_USERPWD},
                COLUMN_USERID + "=? and " + COLUMN_USERPWD + "=?",
                new String[]{userId, userPwd},
                null,null,null);
        if (cursor!=null && cursor.moveToFirst()) {
            user = new User();
            user.setUserId(cursor.getString(cursor.getColumnIndex(COLUMN_USERID)));
            user.setUserPwd(cursor.getString(cursor.getColumnIndex(COLUMN_USERPWD)));
            cursor.close();
        }
        db.close();
        return user;
    }

    //注册
    public long registerUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid", user.getUserId());
        values.put("upwd", user.getUserPwd());
        return db.insert(TBNAME, null, values);
    }
}
