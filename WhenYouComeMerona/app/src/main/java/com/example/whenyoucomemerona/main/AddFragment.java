package com.example.whenyoucomemerona.main;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;


public class AddFragment extends BaseFragment implements View.OnClickListener {

    EditText etContent;
    EditText etMemo;

    EditText etDate, etTime, etLocation, etShare;
    SwitchCompat switchDate, switchTime, switchLocation, switchShare;

    Button   btnSubmit;

    public AddFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        etContent = view.findViewById(R.id.et_content);
        etMemo = view.findViewById(R.id.et_memo);
        etDate = view.findViewById(R.id.et_date);
        etTime = view.findViewById(R.id.et_time);
        etLocation = view.findViewById(R.id.et_location);
        etShare = view.findViewById(R.id.et_share);
        switchDate = view.findViewById(R.id.switch_date);
        switchTime = view.findViewById(R.id.switch_time);
        switchLocation = view.findViewById(R.id.switch_location);
        switchShare = view.findViewById(R.id.switch_share);

        btnSubmit = view.findViewById(R.id.btn_submit);

        etDate.setOnClickListener(this);
        etTime.setOnClickListener(this);
        etLocation.setOnClickListener(this);
        etShare.setOnClickListener(this);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertItem();
            }
        });
        return view;
    }

    public void insertItem() {
        params.clear();
        params.put("content", etContent.getText().toString());
        params.put("memo", etMemo.getText().toString());
        params.put("duedate", etDate.getText().toString());
        params.put("duetime", etDate.getText().toString());
        params.put("location", etLocation.getText().toString());    // TODO: fix these.
        params.put("share_with", etShare.getText().toString());
        params.put("done", "false");

        request("insertItem.do");
    }


    @Override
    public void response(String response) {
        // 통신을 성공 할 시
        try {
            JSONObject j = new JSONObject(response);
            // 데이터 가져오기 성공할 때,
            if (j.optString("result").equals("ok")) {
                Toast.makeText(getContext(), "삽입하기 성공", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "삽입하기 실패", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // EDIT TEXT 가 눌러질 경우
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_date) {
            // 날짜
            Log.d("debugging", "날짜 선택");
        } else if (v.getId() == R.id.et_time) {
            Log.d("debugging", "시간 선택");
        } else if (v.getId() == R.id.et_location) {
            Log.d("debugging", "장소 선택");
        } else if (v.getId() == R.id.et_share) {
            Log.d("debugging", "공유 선택");
        }
    }
}