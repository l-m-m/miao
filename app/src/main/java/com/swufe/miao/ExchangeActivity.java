package com.swufe.miao;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExchangeActivity extends AppCompatActivity {

    private static final String TAG = "ExchangeActivity";

    PopupMenu pm;
    String currencyStr;//币种
    float amount;//金额
    float rate;//汇率

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange);

        initRateMenu(findViewById(R.id.exchange_currency));
        EditText catagory=(EditText)findViewById(R.id.exchange_currency);
        // 单击catagory控件时后显示下拉菜单项
        catagory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pm.show();
            }
        });
        //计算显示
        EditText amount_edt = findViewById(R.id.exchange_amount);
        amount_edt.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                EditText rmb_edt = findViewById(R.id.exchange_rmb);
                String inp = amount_edt.getText().toString();
                if (inp.length() > 0) {
                    amount = Float.parseFloat(inp);
                    rmb_edt.setText(""+1/rate*amount);
                }
            }
        });

    }

    private void initRateMenu(EditText exchange_currency){
        pm=new PopupMenu(ExchangeActivity.this,exchange_currency);
        Menu menu=pm.getMenu();
        pm.getMenuInflater().inflate(R.menu.rate_catagory,menu);
        // 数据库查询所有分类的方法
        DBRateManager dbRateManager = new DBRateManager(ExchangeActivity.this);
        List<RateItem> items=dbRateManager.listAll();
        // 将分类全部添加到数据项中
        for(RateItem item:items){
            Log.i("run",""+item);
            // 1-组别、2-数据项id、3-数据项顺序、4-数据项内容
            menu.add(0,item.getId(),0,item.getCurName());
        }
        // 添加单击数据项事件
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem mitem) {
                // 选择某一个分类后将控件的值改为数据项的内容（catagory是EditText控件）
                exchange_currency.setText(mitem.getTitle());
                currencyStr = mitem.getTitle().toString();
                Log.i("run","currencyStr "+currencyStr );
                //获得汇率
                RateItem aa=dbRateManager.findByName(currencyStr);
                rate=Float.parseFloat(aa.getCurRate());
                return true;
            }
        });
    }

}
