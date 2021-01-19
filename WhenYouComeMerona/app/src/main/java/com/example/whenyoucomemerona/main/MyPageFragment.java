package com.example.whenyoucomemerona.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.controller.My;
import com.example.whenyoucomemerona.controller.StaticFunction;
import com.example.whenyoucomemerona.entity.Todos;
import com.example.whenyoucomemerona.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyPageFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {



    RelativeLayout rlMyPage;
    TextView myId, myEmail;
    Spinner mySetting;
    Button btnFriendList;
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
        rlMyPage = v.findViewById(R.id.my_page);

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

//                JSONObject user_info = j.optJSONObject("data");
//                assert user_info != null;
//                my.setUser_id(user_info.optInt("user_id"));
//                my.setId(user_info.optString("id"));
//                my.setName(user_info.optString("name"));
//                my.setEmail(user_info.optString("email"));
//                my.setBirth(user_info.optString("birth"));

//                myPageSetup();
            } else {
                Toast.makeText(getContext(), "마이페이지 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d("JSON ERROR", "JSON에서 에러가 있습니다.");
            e.printStackTrace();
        }
    }

    // 서버에서 받은 자료들을 마이페이지에 옮긴다.
    private void myPageSetup() {
        myId.setText(My.Account.getId());
        myEmail.setText(My.Account.getEmail());
    }

    // init ------------------------------------------------------
    private void init (View v) {

        myId = v.findViewById(R.id.my_id);
        myEmail = v.findViewById(R.id.my_email);
        mySetting = v.findViewById(R.id.my_setting);
        btnFriendList = v.findViewById(R.id.btn_friend_list);

        // TODO: load different object.
//        if (id == null || pw == null) {

//        } else {
//            params.clear();
//            params.put("user_id", My.Account.getUser_id() + "");
//            request("myPage.do");
//        }

        myPageSetup();


        // Spinner 로 연결
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.my_page_setting, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySetting.setAdapter(adapter);

        mySetting.setOnItemSelectedListener(this);

        btnFriendList.setOnClickListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            // TODO: 설정

        } else {
            // 로그아웃
            SharedPreferences auto = getActivity().getSharedPreferences("auto", Activity.MODE_PRIVATE);
            SharedPreferences.Editor autoLogin = auto.edit();
            autoLogin.putString("auto_id", "");
            autoLogin.putString("auto_pw", "");
            autoLogin.apply();

            My.Account = new User();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
            Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // 설정에서 아무것도 고르지 않는다.
    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_friend_list) {
            // TODO: get Friend List
            getFriendList(v);
        }
    }


    // 친구 리스트 불러오기 페이지로 이동 --------------------------------------------------------------------
    private void getFriendList(View v) {
        Fragment friendListFragment = new FriendListFragment(My.Account);
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_next1,
                R.anim.slide_next2
            )
            .replace(R.id.body_rl, friendListFragment)
            .addToBackStack(null)
            .commit();
    }
}