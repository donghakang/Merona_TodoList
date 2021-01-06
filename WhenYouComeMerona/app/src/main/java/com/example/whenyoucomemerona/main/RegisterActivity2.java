package com.example.whenyoucomemerona.main;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.whenyoucomemerona.controller.StaticFunction;
import com.example.whenyoucomemerona.url.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity2 extends RegisterActivity implements View.OnClickListener {

    EditText etBirth, etName, etEmail;
    Button btnSubmit;
    TextView btnBack;
    String id;
    String pw;
    String name, email, birth;

    final Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        id = getIntent().getStringExtra("id");
        pw = getIntent().getStringExtra("pw");
        name = "";
        email = "";
        birth = "";

        etName = findViewById(R.id.et_registerName);
        etBirth = findViewById(R.id.et_registerBirth);
        etEmail = findViewById(R.id.et_registerEmail);
        btnSubmit = findViewById(R.id.btn_register_submit);
        btnBack = findViewById(R.id.btn_register_back);

        etBirth.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_registerBirth) {
            int yy = cal.get(Calendar.YEAR);
            int mm = cal.get(Calendar.MONTH);
            int dd = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetListener,
                    yy, mm, dd);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        } else if (v.getId() == R.id.btn_register_back) {
            Intent intent = new Intent(this, RegisterActivity1.class);
            intent.putExtra("id", id);
            intent.putExtra("pw", pw);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_back1, R.anim.slide_back2);
            startActivity(intent, options.toBundle());
            finish();
        } else if (v.getId() == R.id.btn_register_submit) {
            params.clear();
            params.put("username", id);
            request(URL.getUrl() + "checkId.do");

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
        String url = URL.getUrl() + "checkEmail.do";

        RequestQueue stringRequest = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, url,
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
                    Toast.makeText(getApplicationContext(), "OKAY EMAIL", Toast.LENGTH_SHORT).show();

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
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

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

        params.clear();
        params.put("id", id);
        params.put("pw", StaticFunction.EncBySha256(pw));
        params.put("name", name);
        params.put("birth", birth);
        params.put("email", email);
        request(URL.getUrl() + "register.do");
    }

    @Override
    public void response(String response) {
        try {
            JSONObject j = new JSONObject(response);
            // 데이터 가져오기 성공할 때,
            if (j.optString("result").equals("ok")) {
                // TODO: 회원가입 완료 페이지로 이동

                Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(this, RegisterActivity2.class);
//                    intent.putExtra("id", id);
//                    intent.putExtra("pw", pw);
//                    ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_next1, R.anim.slide_next2);
//                    startActivity(intent, options.toBundle());
//                    finish();
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
}