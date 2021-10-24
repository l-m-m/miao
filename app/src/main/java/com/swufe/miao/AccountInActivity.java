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
import android.widget.DatePicker;
import android.widget.EditText;
import android.view.View.OnTouchListener;
import android.app.DatePickerDialog.OnDateSetListener;
import android.view.View.OnFocusChangeListener;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AccountInActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG="AccountInActivity";
    Intent intent;
    float amount;
    String user_id;
    int in_year,in_month,in_day;
    int category_id;
    EditText etime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_income);
    }

    public void onClick(View btn) {
        if(btn.getId()==R.id.btn_outcome){
            intent = new Intent(this, AccountOutActivity.class);
            startActivity(intent);
        }
        Log.i(TAG,"category:");
        switch (btn.getId()){
            case R.id.income_iv1:
                category_id=1;
                break;
            case R.id.income_iv2:
                category_id=2;
                break;
            case R.id.income_iv3:
                category_id=3;
                break;
            case R.id.income_iv4:
                category_id=4;
                break;
            case R.id.income_iv5:
                category_id=5;
                break;
            case R.id.income_iv6:
                category_id=6;
                break;
        }
        Log.i(TAG,"category_id:"+category_id);
        //记录金额
        EditText amount_edt = findViewById(R.id.income_account);
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
        etime = (EditText) findViewById(R.id.income_time);
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
        //获取当前用户ID
        SharedPreferences sharedPreferences = getSharedPreferences("myUser", Activity.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        Log.i(TAG,"user_id:"+user_id);
        //插入数据库
        if(btn.getId()==R.id.in_btn){
            Bill bill_new = new Bill(user_id,category_id,1,amount,in_year,in_month,in_day);
            DBBillManager dbBillManager = new DBBillManager(AccountInActivity.this);
            dbBillManager.addBill(bill_new);
            //跳转回明细页面
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }
    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AccountInActivity.this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                in_year=year;
                in_month=monthOfYear;
                in_day=dayOfMonth;
                Log.i(TAG,"calendar:"+in_year+"-"+in_month+"-"+in_day);
                AccountInActivity.this.etime.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
