package com.swufe.miao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    EditText edt_id,edt_pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //填写用户名及密码
        edt_id = findViewById(R.id.login_edt_id);
        edt_pwd =findViewById(R.id.login_edt_pwd);
        //登录
        Button btn_login = findViewById(R.id.login_btn1);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String userId=edt_id.getText().toString();
                    String userPwd=edt_pwd.getText().toString();
                    DBHelper dbuserHelper=new DBHelper(getApplicationContext());
                    User user = dbuserHelper.userlogin(userId,userPwd);
                    //登录成功跳转对应类型界面
                    if(user!=null) {
                        Toast.makeText(getApplicationContext(), user.getUserId() + "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent;
                        ArrayList<User> list = new ArrayList<>();
                        list.add(user);

                        //intent = new Intent(getApplicationContext(), UserCenterActivity.class);
                        intent = new Intent(getApplicationContext(), MainActivity.class);

                        intent.putParcelableArrayListExtra("LoginUser", list);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"登录失败，密码错误或账号不存在！",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"数据库异常",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //注册按键
        Button btn_register=findViewById(R.id.login_btn2);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);

                startActivity(intent);
            }
        });
    }
}
