package com.swufe.miao;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener{
    private static final String TAG="MainActivity";
    Intent intent;
    String user_id;
    int month_current;
    TextView data_month;
    PopupMenu pm;
    List<Bill> bills;
    ArrayList<String> lists;
    ListView myBillList;
    ListAdapter adapter;
    DBBillManager dbBillManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取数据传递
//        User user= ((ArrayList<User>)getIntent().getExtras().get("LoginUser")).get(0);
//        Bundle bdl =(Bundle)getIntent().getExtras();
//        ArrayList<User> user = ((ArrayList<User>)bdl.getSerializable("LoginUser"));
//        Log.i(TAG,"user:"+user.get(0).getUserId());

        SharedPreferences sharedPreferences = getSharedPreferences("myUser", Activity.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        Log.i(TAG,"user_id:"+user_id);

        //获取所有账单
        dbBillManager = new DBBillManager(MainActivity.this);
        bills=dbBillManager.listAll();

        //默认选择当前月份
        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        month_current=Integer.parseInt(curDateStr.substring(5,7));
        Log.i(TAG,"初始month:"+month_current);
        amountSetting(month_current);//显示当前月的总支出+总收入

        //详细显示
        myBillList=findViewById(R.id.bill_lv);
        lists=billDetail(month_current) ;
        adapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_expandable_list_item_1,
                lists);
        myBillList.setAdapter(adapter);

        //长按删除
        myBillList.setOnItemLongClickListener(this);

        //下拉菜单
        data_month= findViewById(R.id.data_month);
        initMonthMenu(data_month);
        data_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pm.show();
            }
        });

    }

    //获得详细账单展示
    public ArrayList<String> billDetail(int month){
        ArrayList<String> lists=new ArrayList<String>();
        String str="";
        String[] a1={"","服饰","餐饮","住房","交通","娱乐","学习","美容","医疗","社交"};
        String[] a2={"","生活费","奖学金","工资","理财","兼职","其他"};
        for(Bill bill:bills){
            if(user_id.equals(bill.getUser_id()) && month==bill.getMonth()){
                if(0==bill.getFlag()){
                    str=bill.getYear()+"-"+bill.getMonth()+"-"+bill.getDay()+
                            "：    "+"支出"+
                            "-"+a1[bill.getCategory_id()]+
                            "       "+bill.getCost()+"元";
                }
                if(1==bill.getFlag()){
                    str=bill.getYear()+"-"+bill.getMonth()+"-"+bill.getDay()+
                            "：    "+"收入"+
                            "-"+a2[bill.getCategory_id()]+
                            "       "+bill.getCost()+"元";
                }
                lists.add(str);
            }
        }
        return lists;
    }
    //获取(xx月的第xx条账单)id号
    public int getMonthId(int month,int position){
        int id=0,i=0;
        for(Bill bill:bills){
            if(month==bill.getMonth()){
                if(i==position){
                    id=bill.getId();
                    break;
                }
                i++;
            }
        }
        return id;
    }
    //长按列表删除
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.i(TAG, "onItemLongClick: 长按列表项position=" + position);
        //删除操作 构造对话框进行确认操作
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前账单").setPositiveButton("是",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: 对话框事件处理");
                //从数据库中删除
                dbBillManager.remove(getMonthId(month_current,position));
                //页面刷新
                //((BaseAdapter)adapter).notifyDataSetChanged();
                bills=dbBillManager.listAll();
                lists=billDetail(month_current) ;//详细账单修改
                adapter = new ArrayAdapter<String>(
                        MainActivity.this,
                        android.R.layout.simple_expandable_list_item_1,
                        lists);
                myBillList.setAdapter(adapter);
            }
        }).setNegativeButton("否",null);
        builder.create().show();
        //Log.i(TAG, "onItemLongClick: size=" + listItems.size());
        return true;
    }

    //根据月份设置收入支出
    public void amountSetting(int month){
        Log.i(TAG,"month_Setting:"+month);
        float total_income=0,total_outcome=0;
        for(Bill bill:bills){
            Log.i(TAG,"month:"+bill.getMonth());
            Log.i(TAG,"cost:"+bill.getCost());
            if(user_id.equals(bill.getUser_id()) && month==bill.getMonth()){
                if(0==bill.getFlag()){
                    total_outcome+=bill.getCost();
                }
                if(1==bill.getFlag()){
                    total_income+=bill.getCost();
                }
            }
        }
        TextView t_outcome= findViewById(R.id.total_outcome);
        TextView t_income= findViewById(R.id.total_income);
        t_outcome.setText(""+total_outcome);
        t_income.setText(""+total_income);
    }

    //启用菜单项--右上角
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    //处理菜单事件--右上角
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.menu_ratelist){
            intent = new Intent(this, RateListActivity.class);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.menu_LogOut){
        }
        return super.onOptionsItemSelected(item);
    }

    //月份选择 下拉菜单
    private void initMonthMenu(TextView data_month){
        pm=new PopupMenu(MainActivity.this,data_month);
        Menu menu=pm.getMenu();
        pm.getMenuInflater().inflate(R.menu.month,menu);
        // 添加单击数据项事件
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem mitem) {
                // 选择某一个分类后将控件的值改为数据项的内容（catagory是EditText控件）
                data_month.setText(mitem.getTitle());
                month_current=Integer.parseInt(mitem.getTitle().toString());
                //相关修改
                amountSetting(month_current);//根据选择月份修改收入支出
                lists=billDetail(month_current) ;//详细账单修改
                adapter = new ArrayAdapter<String>(
                        MainActivity.this,
                        android.R.layout.simple_expandable_list_item_1,
                        lists);
                myBillList.setAdapter(adapter);
                Log.i("run","month "+month_current);
                return true;
            }
        });
    }

}