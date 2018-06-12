package com.mobile.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by CimZzz on 2018/4/26.<br>
 * Description:<br>
 * 注册界面
 */
public class RegisterActivity extends Activity {

    private EditText userName;
    private EditText userPwd;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = (EditText) findViewById(R.id.userName);
        userPwd = (EditText) findViewById(R.id.userPwd);
        registerBtn = (Button) findViewById(R.id.registerBtn);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userNameTxt = userName.getText().toString();
                String userPwdTxt = userPwd.getText().toString();


                if(TextUtils.isEmpty(userNameTxt)) {
                    Toast.makeText(RegisterActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(userPwdTxt)) {
                    Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(checkUserExist(userNameTxt)) {
                    Toast.makeText(RegisterActivity.this,"用户已经存在",Toast.LENGTH_SHORT).show();
                    return;
                }


                User user = new User(userNameTxt,userPwdTxt);
                insertUser(user);
                //需要接入接口
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(RegisterActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean checkUserExist(String userName) {
        UserDao userDao = ((NewApplication)getApplication()).daoSession.getUserDao();
        User user = userDao.load(userName);
        return user != null;
    }

    private void insertUser(User user) {
        UserDao userDao = ((NewApplication)getApplication()).daoSession.getUserDao();
        userDao.insert(user);
    }
}
