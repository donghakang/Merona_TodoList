package com.example.whenyoucomemerona.main;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

public class UserPageFragment extends BaseFragment implements View.OnClickListener {

    User currentUser;
    User user;
    TextView userId;
    TextView userEmail;
    Button btnFriendInvite;


    public UserPageFragment() {
        // Required empty public constructor
    }

    public UserPageFragment(User user) {
        this.currentUser = user;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_page, container, false);
        params.clear();
        params.put("user_id", currentUser.getUser_id() + "");
        request("getUserPage.do");

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
                user.setUser_id(user_info.getInt("user_id"));
                user.setId(user_info.getString("id"));
                user.setName(user_info.getString("name"));
                user.setEmail(user_info.getString("email"));
                user.setBirth(user_info.getString("birth"));

                userId.setText(user.getId());
                userEmail.setText(user.getEmail());
            } else {
                Toast.makeText(getContext(), "마이페이지 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d("JSON ERROR", "JSON에서 에러가 있습니다.");
            e.printStackTrace();
        }
    }

    private void init(View v) {
        userId = v.findViewById(R.id.user_id);
        userEmail = v.findViewById(R.id.user_email);
        btnFriendInvite = v.findViewById(R.id.btn_friend_invite);

        btnFriendInvite.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_friend_invite) {
            Log.d("dddd", currentUser.toString());
        }
    }
}
