package com.example.whenyoucomemerona.controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.whenyoucomemerona.entity.User;
import com.example.whenyoucomemerona.model.Key;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class BaseFragment extends Fragment {
    public Map<String, String> params = new HashMap<String, String>();
    public Map<String, String> header_params = new HashMap<String, String>();
    public ProgressBar progressBar;
    // Progress Bar

    public void loadStart() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void loadEnd() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    // 통신
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // 통신이 안되는 메시지를 띄운다.
            Toast.makeText(getContext(), "통신이 불가능 합니다.", Toast.LENGTH_SHORT).show();

            getActivity().finish();
            System.exit(0);
        }
    };

    Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            response(response);
        }
    };

    public void response(String response) { }

    public void request(String url) {
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url,
                successListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }


    public void specificRequest(String url) {
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.GET, url,
                successListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return header_params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }


    public void checkSession() {
        SharedPreferences auto = getActivity().getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String auto_id = auto.getString("auto_id",null);

        if (auto_id == null) {
            // TODO: Session 이 만료 되었습니다.

        } else {

        }
    }


    public void sendNotification(String token, String title, String message) {
        String FCM_API = "https://fcm.googleapis.com/fcm/send";
        final String serverKey = "key=" + Key.getFirebaseServerKey();
        final String contentType = "application/json";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", title);
            notifcationBody.put("message", message);

            notification.put("to", token);
            notification.put("data", notifcationBody);
        } catch (Exception e) {
            Log.d("dddd", "onCreate: " + e.getMessage() );
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Notification Tag", "Notification Sent");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.d("Notification Tag", "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }



    // Noti Table에 업데이트 시킨다
    public void updateNotification(final int type, final User user, final User friend) {
        String url = "insertNoti.do";

        String DATE_FORMAT = "yyyyMMdd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        Date today = Calendar.getInstance().getTime();
        final String pushDate = sdf.format(today);

        Log.d("dddd", "FINAL: " + friend.getUser_id());

        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response Tag", "통신 실패");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> notiParams = new HashMap<String, String>();
                notiParams.put("type", type + "");
                notiParams.put("user_id", user.getUser_id() + "");
                notiParams.put("friend_id", friend.getUser_id() + "");
                notiParams.put("pushDate", pushDate);
                return notiParams;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }


}
