package com.swufe.miao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBRateManager {
    private DBRateHelper dbRateHelper;
    private String TBNAME;

    public DBRateManager(Context context) {
        dbRateHelper = new DBRateHelper(context);
        TBNAME = DBRateHelper.TABLE_NAME;
    }

    //添加所有汇率
    public void addAll(List<RateItem> list){
        SQLiteDatabase dbRate = dbRateHelper.getWritableDatabase();
        for (RateItem item : list) {
            ContentValues values = new ContentValues();
            values.put("curname", item.getCurName());
            values.put("currate", item.getCurRate());
            dbRate.insert(TBNAME, null, values);
        }
        dbRate.close();
    }

    //删除原有所有汇率数据
    public void deleteAll(){
        SQLiteDatabase dbRate = dbRateHelper.getWritableDatabase();
        dbRate.delete(TBNAME,null,null);
        dbRate.close();
    }

    //获取表中所保存的汇率数据
    public List<RateItem> listAll(){
        List<RateItem> rateList = null;
        SQLiteDatabase dbRate = dbRateHelper.getReadableDatabase();
        Cursor cursor = dbRate.query(TBNAME, null, null, null, null, null, null);
        if(cursor!=null){
            rateList = new ArrayList<RateItem>();
            while(cursor.moveToNext()){
                RateItem item = new RateItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item.setCurName(cursor.getString(cursor.getColumnIndex("CURNAME")));
                item.setCurRate(cursor.getString(cursor.getColumnIndex("CURRATE")));

                rateList.add(item);
            }
            cursor.close();
        }
        dbRate.close();
        return rateList;
    }

    public RateItem findByName(String name){
        SQLiteDatabase dbRate = dbRateHelper.getReadableDatabase();
        Cursor cursor = dbRate.query(TBNAME, null, "CURNAME=?", new String[]{name}, null, null, null);
        RateItem rateItem = null;
        if(cursor!=null && cursor.moveToFirst()){
            rateItem = new RateItem();
            rateItem.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            rateItem.setCurName(cursor.getString(cursor.getColumnIndex("CURNAME")));
            rateItem.setCurRate(cursor.getString(cursor.getColumnIndex("CURRATE")));
            cursor.close();
        }
        dbRate.close();
        return rateItem;
    }
}
