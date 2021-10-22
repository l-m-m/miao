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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG="LoginActivity";

    EditText edt_id,edt_pwd;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
    public void onClick(View btn) {
        Log.i(TAG,"click:");
        edt_id = findViewById(R.id.login_edt_id);
        edt_pwd =findViewById(R.id.login_edt_pwd);
        ArrayList<User> list = new ArrayList<>();
        if(btn.getId()==R.id.login_btn1){
            try {
                String userId=edt_id.getText().toString();
                String userPwd=edt_pwd.getText().toString();
                DBManager dbManager = new DBManager(LoginActivity.this);
                User user = dbManager.userlogin(userId,userPwd);
                if(user!=null) {//登录成功跳转对应类型界面
                    Toast.makeText(LoginActivity.this, user.getUserId() + "登录成功", Toast.LENGTH_SHORT).show();
                    list.add(user);
                    //选择跳转页面
                    intent = new Intent(this, MainActivity.class);
                    intent.putExtra("LoginUser", list);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,"登录失败，密码错误或账号不存在！",Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this,"数据库异常",Toast.LENGTH_SHORT).show();
            }
        }else if(btn.getId()==R.id.login_btn2){
            Intent intent=new Intent(this,RegisterActivity.class);
            startActivity(intent);
        }
    }
}
