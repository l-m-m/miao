package com.swufe.miao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RateListActivity extends AppCompatActivity implements Runnable{

    private static final String TAG = "RateListActivity";
    //每天爬取汇率，进行显示-->rate_list.xml

    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDateStr";
    Handler handler;
    ImageButton b1,b2,b3,b4,b5;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_list);

        //把日期数据放在SharedPreferences里，用于保存数据库中的汇率是哪一天的数据
        SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY, "");
        Log.i("List","lastRateDateStr=" + logDate);

        ListView mylist=findViewById(R.id.rate_lv);
        handler = new Handler(Looper.myLooper()){//处理接收回来的数据
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what==9){
                    ArrayList<String> list2 = (ArrayList<String>) msg.obj;
                    Log.i("list", "handleMessage: "+list2);
                    ListAdapter adapter = new ArrayAdapter<String>(
                            RateListActivity.this,
                            android.R.layout.simple_expandable_list_item_1,
                            list2);
                    mylist.setAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };
        Thread t = new Thread(this);
        t.start();

        //下方按钮
        b1=findViewById(R.id.btn_main);
        b2=findViewById(R.id.btn_pie);
        b3=findViewById(R.id.btn_account);
        b4=findViewById(R.id.btn_exchange);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RateListActivity.this, MainActivity.class);
                startActivity(in);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RateListActivity.this, PieCharOutActivity.class);
                startActivity(in);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RateListActivity.this, AccountOutActivity.class);
                startActivity(in);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RateListActivity.this, ExchangeActivity.class);
                startActivity(in);
            }
        });
    }

    public void run() {
        Log.i("List","run...");
        List<String> retList = new ArrayList<String>();
        Message msg = handler.obtainMessage();
        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        Log.i("run","curDateStr:" + curDateStr + " logDate:" + logDate);
        if(curDateStr.equals(logDate)){//如果相等，则不从网络中获取数据
            Log.i("run","日期相等，从数据库中获取数据");
            DBRateManager dbRateManager = new DBRateManager(RateListActivity.this);
            for(RateItem rateItem : dbRateManager.listAll()){
                retList.add(rateItem.getCurName() + "=>" + rateItem.getCurRate());
            }
        }else{
            Log.i("run","日期相等，从网络中获取在线数据");
            try {//获取网络数据
                List<RateItem> rateList = new ArrayList<RateItem>();
                Document doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
                Element firstTable=doc.getElementsByTag("table").first();
                Elements trs=firstTable.getElementsByTag("tr");//获取行
                trs.remove(0);//去掉第一行数据
                for(Element tr: trs){//获取元素
                    Log.i(TAG, "run: r=" + tr);
                    Elements tds=tr.getElementsByTag("td");
                    Element td1=tds.get(0);//第一列
                    Element td2=tds.get(5);//最后一列
                    Log.i(TAG, "run: td1=" + td1+"\t td2=" + td2);
                    float val = Float.parseFloat(td2.text());
                    val = 100/val;
                    retList.add(td1.text() + "->" + val);
                    RateItem rateItem = new RateItem(td1.text(),""+val);
                    rateList.add(rateItem);
                }
                DBRateManager dbRateManager = new DBRateManager(RateListActivity.this);
                dbRateManager.deleteAll();
                Log.i("dbRate","删除所有记录");
                dbRateManager.addAll(rateList);
                Log.i("dbRate","添加新记录集");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //更新记录日期
            SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(DATE_SP_KEY, curDateStr);
            edit.commit();
            Log.i("run","更新日期结束：" + curDateStr);
        }
        msg.obj = retList;
        msg.what=9;
        handler.sendMessage(msg);
    }
}
