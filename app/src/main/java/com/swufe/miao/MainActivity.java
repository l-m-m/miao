package com.swufe.miao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取数据传递
//        User user= ((ArrayList<User>)getIntent().getExtras().get("LoginUser")).get(0);
//        Bundle bdl =(Bundle)getIntent().getExtras();
//        ArrayList<User> user = ((ArrayList<User>)bdl.getSerializable("LoginUser"));
//        Log.i(TAG,"user:"+user.get(0).getUserId());

    }

    //启用菜单项
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    //处理菜单事件
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.menu_ratelist){
            intent = new Intent(this, RateListActivity.class);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.menu_LogOut){
        }
        return super.onOptionsItemSelected(item);
    }

}