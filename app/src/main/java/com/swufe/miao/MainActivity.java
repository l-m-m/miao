package com.swufe.miao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //启用菜单项
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    //处理菜单事件
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.menu_rate){
            intent = new Intent(this, ExchangeActivity.class);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.menu_LogOut){

        }
        return super.onOptionsItemSelected(item);
    }

}