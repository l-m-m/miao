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
import android.widget.ImageButton;

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
    ImageButton b1,b2,b3,b4,b5;

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

        //下方页面选择
        if(btn.getId()==R.id.btn_main){
            intent = new Intent(this, MainActivity .class);
            startActivity(intent);
        }
        if(btn.getId()==R.id.btn_pie){
            intent = new Intent(this, PieCharOutActivity .class);
            startActivity(intent);
        }
        if(btn.getId()==R.id.btn_exchange){
            intent = new Intent(this, ExchangeActivity .class);
            startActivity(intent);
        }
        if(btn.getId()==R.id.btn_rate){
            intent = new Intent(this, RateListActivity .class);
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
            //dbBillManager.deleteAll(); 报错时解决问题
//            Bill b1 = new Bill("0417",1,1,1234,2021,10,1);
//            dbBillManager.addBill(b1);
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
                in_month=monthOfYear+1;
                in_day=dayOfMonth;
                Log.i(TAG,"calendar:"+in_year+"-"+in_month+"-"+in_day);
                AccountInActivity.this.etime.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
