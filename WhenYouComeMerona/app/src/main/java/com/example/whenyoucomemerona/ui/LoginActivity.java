package com.example.whenyoucomemerona.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.whenyoucomemerona.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText et_username, et_password;
    Button btn_login;
    TextView tv_register;

    String id;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            id = et_username.getText().toString();
            password = et_password.getText().toString();

            RequestQueue stringRequest = Volley.newRequestQueue(this);
            // TODO: login.do 로 변경한다.
            String url = "http://172.30.1.16:8098/merona/login.do";

            StringRequest myReq = new StringRequest(Request.Method.POST, url,
                    successListener, errorListener) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", id);
                    params.put("password", password);
                    return params;
                }
            };

            myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
            stringRequest.add(myReq);
        } else if (v.getId() == R.id.tv_register) {
            // TODO: 회원가입
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            // TODO: 통신을 성공 할 시
            Log.d("ddddd", response);
            try {
                JSONObject j = new JSONObject(response);
                Log.d("ddddd", j.toString());
                if (j.optString("result").equals("ok")) {
                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    // when obtaining data is unsuccessful.
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // 통신을 실패할 시
            Toast.makeText(LoginActivity.this, "통신이 불가능 합니다.", Toast.LENGTH_SHORT).show();
        }
    };
}