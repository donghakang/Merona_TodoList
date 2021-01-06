package com.example.whenyoucomemerona.main;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.StaticFunction;
import com.example.whenyoucomemerona.url.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity1 extends RegisterActivity implements View.OnClickListener {

    EditText etRegisterId;
    EditText etRegisterPassword;
    EditText etRegisterRepassword;
    Button btnRegister;
    TextView btnRegisterCancel;

    String id;
    String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        etRegisterId = findViewById(R.id.et_registerId);
        etRegisterPassword = findViewById(R.id.et_registerPassword);
        etRegisterRepassword = findViewById(R.id.et_registerRepassword);
        btnRegister = findViewById(R.id.btn_register);
        btnRegisterCancel = findViewById(R.id.btn_register_cancel);

        if (getIntent().getStringExtra("id") != null && getIntent().getStringExtra("pw") != null) {
            etRegisterId.setText(getIntent().getStringExtra("id"));
            etRegisterPassword.setText(getIntent().getStringExtra("pw"));
            etRegisterRepassword.setText(getIntent().getStringExtra("pw"));
        }


        // keyboard를 킨다.
        etRegisterId.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        btnRegister.setOnClickListener(this);
        btnRegisterCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_register_cancel) {
            // 취소하기 버튼기
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.btn_register) {
            // 다음 가입 페이지로

            if (checkRegister1(etRegisterId, etRegisterPassword, etRegisterRepassword)) {
                String id = etRegisterId.getText().toString();
                checkId(id);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkId(String id) {
        params.clear();
        params.put("username", id);
        request(URL.getUrl() + "checkId.do");
    }

    @Override
    public void response(String response) {
        // 통신을 성공 할 시
        try {
            JSONObject j = new JSONObject(response);
            // 데이터 가져오기 성공할 때,
            if (j.optString("result").equals("ok")) {
                String id = etRegisterId.getText().toString();
                String pw = etRegisterPassword.getText().toString();
                Intent intent = new Intent(this, RegisterActivity2.class);
                intent.putExtra("id", id);
                intent.putExtra("pw", pw);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_next1, R.anim.slide_next2);
                startActivity(intent, options.toBundle());
            } else {
                Toast.makeText(this, "이미 사용중인 아이디 입니다.", Toast.LENGTH_LONG).show();
                etRegisterId.setText("");
                etRegisterId.requestFocus();
            }
        } catch (JSONException e) {
            Log.d("eeeee", "JSON에서 에러가 있습니다.");
            e.printStackTrace();
            etRegisterId.setText("");
            etRegisterId.requestFocus();
        } catch (Exception e) {
            Log.d("eeeee", "PASSWORD에서 에러가 있습니다.");
            e.printStackTrace();
        }
    }




}