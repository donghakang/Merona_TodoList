package com.example.whenyoucomemerona.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseActivity;
import com.example.whenyoucomemerona.url.URL;

public class RegisterActivity extends BaseActivity {

    private boolean checkEditTextEmpty(EditText editText) {
        return editText.getText().toString().length() == 0;
    }

    private boolean isTextEqual(EditText et1, EditText et2) {
        Log.d("eeeee", et1.getText().toString());
        Log.d("eeeee", et2.getText().toString());
        Log.d("eeeee", (et1.getText().toString().equals(et2.getText().toString())) + "");
        return et1.getText().toString().equals(et2.getText().toString());
    }





    public boolean checkRegister1(EditText id, EditText pwd1, EditText pwd2) {
        if (checkEditTextEmpty(id)) {
            Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
            id.setText("");
            id.requestFocus();
            return false;
        } else if (checkEditTextEmpty(pwd1)) {
            Toast.makeText(getApplicationContext(), "암호를 입력하세요", Toast.LENGTH_SHORT).show();
            pwd1.setText("");
            pwd1.requestFocus();
            return false;
        } else if (checkEditTextEmpty(pwd2)) {
            Toast.makeText(getApplicationContext(), "암호를 입력하세요", Toast.LENGTH_SHORT).show();
            pwd2.setText("");
            pwd2.requestFocus();
            return false;
        } else if (!isTextEqual(pwd1, pwd2)) {
            Toast.makeText(getApplicationContext(), "암호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
            pwd1.setText("");
            pwd2.setText("");
            pwd1.requestFocus();
            return false;
        } else {
            return true;
        }
    }


    public boolean checkRegister2(EditText name, EditText birth, EditText email) {
        if (checkEditTextEmpty(name)) {
            Toast.makeText(getApplicationContext(), "이름을 입력하세요", Toast.LENGTH_SHORT).show();
            name.setText("");
            name.requestFocus();
            return false;
        } else if (checkEditTextEmpty(birth)) {
            Toast.makeText(getApplicationContext(), "생년월일을 선택하세요", Toast.LENGTH_SHORT).show();
            birth.setText("");
            birth.requestFocus();
            return false;
        } else if (checkEditTextEmpty(email)) {
            Toast.makeText(getApplicationContext(), "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
            email.setText("");
            email.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}