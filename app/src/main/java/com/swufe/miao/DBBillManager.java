package com.swufe.miao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBBillManager {
    private DBBillHelper dbBillHelper;
    private String TBNAME;

    public DBBillManager(Context context) {
        dbBillHelper = new DBBillHelper(context);
        TBNAME = DBBillHelper.TABLE_NAME;
    }

    //添加一个账单
    public void addBill(Bill bill){
        SQLiteDatabase dbBill = dbBillHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("uid", bill.getUser_id());
        value.put("cid", bill.getCategory_id());
        value.put("flag", bill.getFlag());
        value.put("cost", bill.getCost());
        value.put("year", bill.getYear());
        value.put("month", bill.getMonth());
        value.put("day", bill.getDay());
        dbBill.insert(TBNAME, null, value);
        dbBill.close();
    }

    //删除一个账单
    public void deleteAll(int id){
        SQLiteDatabase dbBill = dbBillHelper.getWritableDatabase();
        dbBill.delete(TBNAME,null,null);
        dbBill.close();
    }

    //获取所有账单
    @SuppressLint("Range")
    public List<Bill> listAll(){
        List<Bill> billList = null;
        SQLiteDatabase dbBill = dbBillHelper.getReadableDatabase();
        Cursor cursor = dbBill.query(TBNAME, null, null, null, null, null, null);
        if(cursor!=null){
            billList = new ArrayList<Bill>();
            while(cursor.moveToNext()){
                Bill bill = new Bill();
                bill.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                bill.setUser_id(cursor.getString(cursor.getColumnIndex("UID")));
                bill.setCategory_id(cursor.getInt(cursor.getColumnIndex("CID")));
                bill.setFlag(cursor.getInt(cursor.getColumnIndex("FLAG")));
                bill.setCost(cursor.getFloat(cursor.getColumnIndex("COST")));
                bill.setYear(cursor.getInt(cursor.getColumnIndex("YEAR")));
                bill.setMonth(cursor.getInt(cursor.getColumnIndex("MONTH")));
                bill.setDay(cursor.getInt(cursor.getColumnIndex("DAY")));
                billList.add(bill);
            }
            cursor.close();
        }
        dbBill.close();
        return billList;
    }

}
