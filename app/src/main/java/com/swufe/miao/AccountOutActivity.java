package com.swufe.miao;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.app.DatePickerDialog.OnDateSetListener;
import android.view.View.OnFocusChangeListener;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class AccountOutActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG="AccountOutActivity";
    Intent intent;
    float amount;
    String user_id;
    int out_year,out_month,out_day;
    int category_id;
    EditText etime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_outcome);

        //获取当前用户ID
        SharedPreferences sharedPreferences = getSharedPreferences("myUser", Activity.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        Log.i(TAG,"user_id:"+user_id);
    }

    public void onClick(View btn) {
        if(btn.getId()==R.id.btn_income){
            intent = new Intent(this, AccountInActivity.class);
            startActivity(intent);
        }
        Log.i(TAG,"category:");
        switch (btn.getId()){
            case R.id.outcome_iv1:
                category_id=1;
                break;
            case R.id.outcome_iv2:
                category_id=2;
                break;
            case R.id.outcome_iv3:
                category_id=3;
                break;
            case R.id.outcome_iv4:
                category_id=4;
                break;
            case R.id.outcome_iv5:
                category_id=5;
                break;
            case R.id.outcome_iv6:
                category_id=6;
                break;
            case R.id.outcome_iv7:
                category_id=7;
                break;
            case R.id.outcome_iv8:
                category_id=8;
                break;
            case R.id.outcome_iv9:
                category_id=9;
                Log.i(TAG,"社交");
                break;
        }
        Log.i(TAG,"category_id:"+category_id);
        //记录金额
        EditText amount_edt = findViewById(R.id.outcome_account);
        amount_edt.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String inp = amount_edt.getText().toString();
                if (inp.length() > 0) {
                    amount = Float.parseFloat(inp);
                    Log.i(TAG,"amount:"+amount);
                }
            }
        });
        //记录时间
        etime = (EditText) findViewById(R.id.outcome_time);
        etime.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        etime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });
        //插入数据库
        if(btn.getId()==R.id.out_btn){
            Bill bill_new = new Bill(user_id,category_id,0,amount,out_year,out_month,out_day);
            DBBillManager dbBillManager = new DBBillManager(AccountOutActivity.this);
            dbBillManager.addBill(bill_new);
//            Bill b1 = new Bill("0417",7,0,500,2021,10,1);
//            dbBillManager.addBill(b1);
            //跳转回明细页面
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }
    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AccountOutActivity.this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                out_year=year;
                out_month=monthOfYear+1;
                out_day=dayOfMonth;
                Log.i(TAG,"calendar:"+out_year+"-"+out_month+"-"+out_day);
                AccountOutActivity.this.etime.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
