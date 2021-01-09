package com.example.whenyoucomemerona.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
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
import com.example.whenyoucomemerona.controller.BaseActivity;
import com.example.whenyoucomemerona.controller.StaticFunction;
import com.example.whenyoucomemerona.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends BaseActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private String auto_id;
    private String auto_password;


    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        getAppKeyHash();

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        auto_id = auto.getString("auto_id",null);
        auto_password = auto.getString("auto_pw",null);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (auto_id != null && auto_password != null) {
                    // 자동 로그인

                    login(auto_id, auto_password, "login.do");

                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);

                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void login(final String id, final String pw, String url) {
        RequestQueue stringRequest = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, URL.getUrl() + url,
                successListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("pw", StaticFunction.EncBySha256(pw));
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }

    public Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };

    public Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            response(response);
        }
    };

    public void response(String response) {
        // TODO: 통신을 성공 할 시
        try {
            JSONObject j = new JSONObject(response);
            if (j.optString("result").equals("ok")) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


}