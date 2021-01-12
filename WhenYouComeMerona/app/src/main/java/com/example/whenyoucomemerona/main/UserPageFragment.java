package com.example.whenyoucomemerona.main;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.controller.My;
import com.example.whenyoucomemerona.entity.User;
import com.example.whenyoucomemerona.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserPageFragment extends BaseFragment implements View.OnClickListener {


    User user;
    TextView userId;
    TextView userEmail;
    Button btnFriendInvite;
    Button btnFriendFriendList;


    public UserPageFragment() {
        // Required empty public constructor
    }

    public UserPageFragment(User user) {
        this.user = user;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_page, container, false);
        init(v);
        return v;
    }



    private void init(View v) {

        userId = v.findViewById(R.id.user_id);
        userEmail = v.findViewById(R.id.user_email);
        btnFriendInvite = v.findViewById(R.id.btn_friend_add);
        btnFriendFriendList = v.findViewById(R.id.btn_friend_friend_list);

        // 최소한의 세팅
        userId.setText(user.getId());
        userId.setText(user.getName());



        btnFriendInvite.setOnClickListener(this);
        btnFriendFriendList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_friend_add) {
            // TODO: 버튼을 가리고, 보이게 한다.
            Log.d("dddd", user.toString() + "Add");
        } else if (v.getId() == R.id.btn_friend_friend_list) {
            // TODO: 우선 이 버튼을 친구 신청으로 바꾼다
            Log.d ("dddd", user.toString() + "FFLIst");
//            requestFriend("requestFriend.do");
            deleteFriendRequest();
        }
    }


    // 친구 신청을 보낸다 ------------------------------------------------------------------------------
    public void requestFriend() {
        String url = "requestFriend.do";
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, URL.getUrl() + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // success listener
                try {
                    JSONObject j = new JSONObject(response);
                    // 데이터 가져오기 성공할 때,
                    if (j.optString("result").equals("ok")) {
                        Toast.makeText(getContext(), "친구 신청 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "친구 신청 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "JSON 오류", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error listener
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // TODO: 데이터를 집어넣는다.
                params.put("user_id", My.Account.getUser_id() + "");
                params.put("friend_id", user.getUser_id() + "");
                return params;
            }
        };


        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }


    // 친구 신청을 취소한다 ------------------------------------------------------------------------------

    public void deleteFriendRequest() {
        String url = "deleteFriendRequest.do";
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, URL.getUrl() + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // success listener
                try {
                    JSONObject j = new JSONObject(response);
                    // 데이터 가져오기 성공할 때,
                    if (j.optString("result").equals("ok")) {
                        Toast.makeText(getContext(), "친구 신청 취소 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "친구 신청 취소 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "JSON 오류", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error listener
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // TODO: 데이터를 집어넣는다.
                params.put("user_id", My.Account.getUser_id() + "");
                params.put("friend_id", user.getUser_id() + "");
                return params;
            }
        };


        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }


}
