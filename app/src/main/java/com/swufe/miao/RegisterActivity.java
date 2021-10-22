package com.swufe.miao;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG="RegisterActivity";
    EditText edt_rid,edt_rpwd;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        edt_rid =findViewById(R.id.register_edt_id);
        edt_rpwd =findViewById(R.id.register_edt_pwd);

    }
    public void onClick(View btn) {
        Log.i(TAG,"click:");
        User user = new User(edt_rid.getText().toString(),edt_rpwd.getText().toString());
        DBManager dbManager = new DBManager(RegisterActivity.this);
        if (dbManager.registerUser(user) > 0) {
            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "该账户已注册，请返回登录", Toast.LENGTH_SHORT).show();
        }

    }
}