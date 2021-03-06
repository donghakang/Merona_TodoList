package com.example.whenyoucomemerona.main;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.My;
import com.example.whenyoucomemerona.lib.StaticFunction;
import com.example.whenyoucomemerona.model.Key;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class RegisterActivity2 extends RegisterActivity implements View.OnClickListener {

    EditText etBirth, etName, etEmail;
    Button btnSubmit;
    TextView btnBack;
    String id;
    String pw;
    String name, email, birth;

    AlertDialog.Builder builder;
    DatePicker datePicker;
    final Calendar cal = Calendar.getInstance();

    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        id = getIntent().getStringExtra("id");
        pw = getIntent().getStringExtra("pw");
        name = "";
        email = "";
        birth = "";
        token = "";

        etName = findViewById(R.id.et_registerName);
        etBirth = findViewById(R.id.et_registerBirth);
        etEmail = findViewById(R.id.et_registerEmail);
        btnSubmit = findViewById(R.id.btn_register_submit);
        btnBack = findViewById(R.id.btn_register_back);
        datePicker = findViewById(R.id.register_date_picker);

        etBirth.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_registerBirth) {
            builder = new AlertDialog.Builder(this);
            LayoutInflater lnf = getLayoutInflater();
            final View view = lnf.inflate(R.layout.register_date, null);
            final DatePicker date_picker = view.findViewById(R.id.register_date_picker);
            builder.setView(view);


            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    etBirth.setText("");
                }
            });
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO: 확인
                    cal.set(Calendar.YEAR, date_picker.getYear());
                    cal.set(Calendar.MONTH, date_picker.getMonth());
                    cal.set(Calendar.DAY_OF_MONTH, date_picker.getDayOfMonth());
                    updateLabel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();


        } else if (v.getId() == R.id.btn_register_back) {
            Intent intent = new Intent(this, RegisterActivity1.class);
            intent.putExtra("id", id);
            intent.putExtra("pw", pw);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_back1, R.anim.slide_back2);
            startActivity(intent, options.toBundle());
            finish();
        } else if (v.getId() == R.id.btn_register_submit) {
            if (checkRegister2(etName, etBirth, etEmail)) {
                String email = etEmail.getText().toString();
                checkEmail(email);
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, RegisterActivity1.class);
        intent.putExtra("id", id);
        intent.putExtra("pw", pw);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_back2, R.anim.slide_back1);
        startActivity(intent, options.toBundle());
        finish();
    }

    // 이메일 체크 ------------------------------------------------------------------------
    private void checkEmail(final String email) {
        String url = "checkEmail.do";

        RequestQueue stringRequest = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url,
                emailSuccessListener, emailErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                params.clear();
                params.put("email", email);
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }

    Response.ErrorListener emailErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), "통신이 불가능 합니다.", Toast.LENGTH_SHORT).show();
        }
    };

    Response.Listener<String> emailSuccessListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject j = new JSONObject(response);
                // 데이터 가져오기 성공할 때,
                if (j.optString("result").equals("ok")) {
                    register();
                } else {
                    Toast.makeText(getApplicationContext(), "이미 사용중인 이메일 입니다.", Toast.LENGTH_LONG).show();
                    etEmail.setText("");
                    etEmail.requestFocus();
                }
            } catch (JSONException e) {
                Log.d("eeeee", "JSON에서 에러가 있습니다.");
                e.printStackTrace();
                etEmail.setText("");
                etEmail.requestFocus();
            }
        }
    };


    // 생 년 월 일 ------------------------------------------------------------------------
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etBirth.setText(sdf.format(cal.getTime()));
    }

    // 회원 등록 ------------------------------------------------------------------------

    private void register() {

        name = etName.getText().toString();
        birth = etBirth.getText().toString();
        email = etEmail.getText().toString();

        Log.d("dddd", "current Token: " + My.Account.getToken());

        params.clear();
        params.put("id", id);
        params.put("pw", StaticFunction.EncBySha256(pw));
        params.put("name", name);
        params.put("birth", birth);
        params.put("email", email);
        params.put("token", My.Account.getToken());
        request("register.do");
    }

    @Override
    public void response(String response) {
        try {
            JSONObject j = new JSONObject(response);
            // 데이터 가져오기 성공할 때,
            if (j.optString("result").equals("ok")) {
                // TODO: 회원가입 완료 페이지로 이동

                processLogin();


            } else {
                Toast.makeText(this, "어떠한 이유 때문에 회원가입을 할 수 없습니다.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            Log.d("JSON ERROR", "JSON에서 에러가 있습니다.");
            e.printStackTrace();
        }
    }


    // 회원가입이 완료 된 후, 바로 첫번째 페이지로 이동한다,
    // 그 와 동시에 첫 notification을 준다.
    private void processLogin() {
        // token 값


        String url = "login.do";
        RequestQueue stringRequest = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject j = new JSONObject(response);
                            // 데이터 가져오기 성공할 때,
                            if (j.optString("result").equals("ok")) {
                                updateMy("myPage.do");
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> loginParams = new HashMap<String, String>();
                loginParams.put("id", id);
                loginParams.put("pw", StaticFunction.EncBySha256(pw));
                loginParams.put("token", token);

                return loginParams;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);


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
                                Log.d("dddd", user_info.optString("token"));

                                My.Account.setToken(user_info.getString("token"));



                                updateNotification(0, My.Account, My.Account);

                                Intent intent = new Intent(RegisterActivity2.this, MainActivity.class);
                                startActivity(intent);
                                finish();

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
                params.put("pw", StaticFunction.EncBySha256(pw));
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }

}