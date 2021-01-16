package com.example.whenyoucomemerona.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseActivity;
import com.example.whenyoucomemerona.controller.My;
import com.example.whenyoucomemerona.controller.StaticFunction;
import com.example.whenyoucomemerona.model.Key;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    EditText et_username, et_password;
    Button btn_login;
    TextView tv_register;

    String id;
    String password;
    // 자동 로그인



    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // token 값
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.d("dddd", "fail");
                    return;
                }
                token = task.getResult();
                Log.d("dddd", "token: " + token);
            }
        });

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

                updateToken("updateToken.do");
                updateMy("myPage.do");

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



    // --- 내 페이지를 저장한다.
    private void updateMy(String url) {

        RequestQueue stringRequest = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject j = new JSONObject(response);
                            // 데이터 가져오기 성공할 때,
                            if (j.optString("result").equals("ok")) {
                                JSONObject user_info = j.optJSONObject("data");
                                assert user_info != null;

                                My.Account.setUser_id(user_info.getInt("user_id"));
                                My.Account.setId(user_info.getString("id"));
                                My.Account.setName(user_info.getString("name"));
                                My.Account.setEmail(user_info.getString("email"));
                                My.Account.setBirth(user_info.getString("birth"));
                                My.Account.setToken(user_info.getString("token"));

                            } else {
                                Log.d("ddddd", "데이터 가져오기 실패...");
                            }
                        } catch (JSONException e) {
                            Log.d("JSON ERROR", "JSON에서 에러가 있습니다.");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "토큰 삽입 실패", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("pw", StaticFunction.EncBySha256(password));
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }

    // --------------- Update Token
    private void updateToken(String url) {


        RequestQueue stringRequest = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject j = new JSONObject(response);
                            // 데이터 가져오기 성공할 때,
                            if (j.optString("result").equals("ok")) {
                                Toast.makeText(getApplicationContext(), "토큰 삽입 성공", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "토큰 삽입 실패", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "토큰 삽입 실패", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("pw", StaticFunction.EncBySha256(password));
                params.put("token", token);
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);

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
            Intent intent = new Intent(this, RegisterActivity1.class);
            startActivity(intent);
            finish();
        }
    }
}