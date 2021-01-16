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
import com.example.whenyoucomemerona.model.Key;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserPageFragment extends BaseFragment implements View.OnClickListener {


    User user;
    TextView userId;
    // TODO: change from userEmail to userName
    TextView userEmail;
    Button btnFriend;

    int status;

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
        btnFriend = v.findViewById(R.id.btn_friend);

        // 최소한의 세팅
        userId.setText(user.getId());
        userEmail.setText(user.getName());

        checkStatus();
        buttonSetup();

        btnFriend.setOnClickListener(this);
    }

    private void buttonSetup() {
        if (status == 0) {
            btnFriend.setBackgroundResource(R.drawable.button);
            btnFriend.setText("친구 신청 하기");
        } else if (status == 1) {
            btnFriend.setBackgroundResource(R.drawable.default_button);
            btnFriend.setText("친구 신청 취소 하기");
        } else if (status == 2) {
            btnFriend.setBackgroundResource(R.drawable.button);
            btnFriend.setText("친구 확인");
        } else if (status == 3) {
            btnFriend.setBackgroundResource(R.drawable.default_button);
            btnFriend.setText("친구 보기");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_friend) {
            // TODO: 버튼을 가리고, 보이게 한다.
            Log.d("dddd", user.toString() + "Add");

            if (status == 0) {
                // 친구 신청 하기
                requestFriend();

            } else if (status == 1) {
                // 친구 신청 취소 하기
                deleteFriendRequest();

                Log.d("dddd", "CURR_STATUS: " + 1 + "      NEXT_STATUS: " + status);
            } else if (status == 2) {
                // 친구 확인
                insertFriend();
                Log.d("dddd", "CURR_STATUS: " + 2 + "      NEXT_STATUS: " + status);
            } else if (status == 3) {
                // 친구 보기

            }
        }
//            requestFriend("requestFriend.do");
//            deleteFriendRequest();
//            insertFriend();
//            deleteFriend();
    }

    // -----------------------------------------------------------------------------------------------
    // 친구와 나의 관계 확인 ------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------

    private void checkStatus() {
        String url = "checkStatus.do";
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // success listener
                try {
                    JSONObject j = new JSONObject(response);
                    // 데이터 가져오기 성공할 때,
                    if (j.optString("result").equals("ok")) {
                        status = j.optInt("status");
                        Log.d("dddd", "STATUS: " + status);
                        Toast.makeText(getContext(), "status okay", Toast.LENGTH_LONG).show();
                        buttonSetup();
                    } else {
                        Toast.makeText(getContext(), "status failed", Toast.LENGTH_SHORT).show();
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


    // 친구 신청을 보낸다 ------------------------------------------------------------------------------
    public void requestFriend() {
        String url = "requestFriend.do";
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // success listener
                try {
                    JSONObject j = new JSONObject(response);
                    // 데이터 가져오기 성공할 때,
                    if (j.optString("result").equals("ok")) {
                        Toast.makeText(getContext(), "친구 신청 성공", Toast.LENGTH_SHORT).show();
                        checkStatus();
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
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // success listener
                try {
                    JSONObject j = new JSONObject(response);
                    // 데이터 가져오기 성공할 때,
                    if (j.optString("result").equals("ok")) {
                        Toast.makeText(getContext(), "친구 신청 취소 성공", Toast.LENGTH_SHORT).show();
                        checkStatus();
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


    // 친구 확인 --------------------------------------------------------------------------------------
    public void insertFriend() {
        String url = "insertFriend.do";
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // success listener
                try {
                    JSONObject j = new JSONObject(response);
                    // 데이터 가져오기 성공할 때,
                    if (j.optString("result").equals("ok")) {
                        Toast.makeText(getContext(), "친구 신청 취소 성공", Toast.LENGTH_SHORT).show();
                        checkStatus();
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



        // 친구 관계 해제 ----------------------------------------------------------------------------------
        public void deleteFriend() {
            String url = "deleteFriend.do";
            RequestQueue stringRequest = Volley.newRequestQueue(getContext());
            StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url, new Response.Listener<String>() {
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
