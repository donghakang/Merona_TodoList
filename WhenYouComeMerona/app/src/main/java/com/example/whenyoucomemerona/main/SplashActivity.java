package com.example.whenyoucomemerona.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.whenyoucomemerona.url.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private String auto_id;
    private String auto_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        auto_id = auto.getString("auto_id",null);
        auto_password = auto.getString("auto_password",null);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (auto_id != null && auto_password != null) {
                    // 자동 로그인
                    checkLoginStatus(auto_id, auto_password);
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);

                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }

    public void checkLoginStatus(final String _id, final String _password) {
        RequestQueue stringRequest = Volley.newRequestQueue(this);
        // TODO: login.do 로 변경한다.
        String url = URL.getUrl() +  "login.do";

        StringRequest myReq = new StringRequest(Request.Method.POST, url,
                successListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", _id);
                params.put("password", _password);
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }


    Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            // TODO: 통신을 성공 할 시
            try {
                JSONObject j = new JSONObject(response);
                if (j.optString("result").equals("ok")) {

                    Toast.makeText(SplashActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(SplashActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
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
            Toast.makeText(SplashActivity.this, "통신이 불가능 합니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };
}