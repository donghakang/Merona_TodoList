package com.example.whenyoucomemerona.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.controller.StaticFunction;
import com.example.whenyoucomemerona.entity.Todos;
import com.example.whenyoucomemerona.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyPageFragment extends BaseFragment {

    int user_id;
    String id;
    String pw;
    String name;
    String email;
    String birth;
    User my;

    TextView myId, myEmail;
    public MyPageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_page, container, false);

        init(v);


        return v;
    }

    @Override
    public void response(String response) {
        try {
            JSONObject j = new JSONObject(response);
            // 데이터 가져오기 성공할 때,
            if (j.optString("result").equals("ok")) {
                // TODO: 마이페이지 부수기재들을 다 가져온다.
                Log.d("data", j.optJSONObject("data").toString());

                Toast.makeText(getContext(), "마이페이지 불러오기 성공", Toast.LENGTH_SHORT).show();

                JSONObject user_info = j.optJSONObject("data");
                assert user_info != null;
                my.setUser_id(user_info.getInt("user_id"));
                my.setId(user_info.getString("id"));
                my.setName(user_info.getString("name"));
                my.setEmail(user_info.getString("email"));
                my.setBirth(user_info.getString("birth"));

                myPageSetup(my);
            } else {
                Toast.makeText(getContext(), "마이페이지 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d("JSON ERROR", "JSON에서 에러가 있습니다.");
            e.printStackTrace();
        }
    }


    // init ------------------------------------------------------
    private void init (View v) {
        SharedPreferences auto = getActivity().getSharedPreferences("auto", Activity.MODE_PRIVATE);
        pw = auto.getString("auto_pw",null);
        id = auto.getString("auto_id",null);
        user_id = -1;
        name = "";
        email = "";
        birth = "";
        my = new User();

        myId = v.findViewById(R.id.my_id);
        myEmail = v.findViewById(R.id.my_email);

        if (id == null || pw == null) {
            // TODO: 세션이 만료 되었습니다, 로그인 페이지로.
        } else {
            params.clear();
            params.put("id", id);
            params.put("pw", StaticFunction.EncBySha256(pw));
            request("myPage.do");
        }
    }

    // 서버에서 받은 자료들을 마이페이지에 옮긴다.
    private void myPageSetup(User my) {
        myId.setText(my.getId());
        myEmail.setText(my.getEmail());
    }
}