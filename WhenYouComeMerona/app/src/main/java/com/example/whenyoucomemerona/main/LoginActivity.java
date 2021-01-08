package com.example.whenyoucomemerona.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseActivity;
import com.example.whenyoucomemerona.controller.StaticFunction;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    EditText et_username, et_password;
    Button btn_login;
    TextView tv_register;

    String id;
    String password;
    // 자동 로그인
    String auto_id;
    String auto_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 자동 로그인 실패
        et_username = findViewById(R.id.et_loginUsername);
        et_password = findViewById(R.id.et_loginPassword);
        tv_register = findViewById(R.id.tv_register);
        btn_login = findViewById(R.id.btn_login);
        id = "";
        password = "";

        tv_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }


    @Override
    public void response(String response) {
        try {
            JSONObject j = new JSONObject(response);
            if (j.optString("result").equals("ok")) {

                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.putString("auto_id", id);
                autoLogin.putString("auto_pw", password);
                autoLogin.apply();
                Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            id = et_username.getText().toString();
            password = et_password.getText().toString();

            params.clear();
            params.put("id", id);
            params.put("pw", StaticFunction.EncBySha256(password));
            request("login.do");

        } else if (v.getId() == R.id.tv_register) {
            // TODO: 회원가입
            Intent intent = new Intent(this, RegisterActivity1.class);
            startActivity(intent);
            finish();
        }
    }
}