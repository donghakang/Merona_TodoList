package com.example.whenyoucomemerona.controller;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.whenyoucomemerona.entity.User;
import com.example.whenyoucomemerona.model.Key;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class BaseActivity extends AppCompatActivity {
    public Map<String, String> params = new HashMap<String, String>();


    public void request(String url) {
        RequestQueue stringRequest = Volley.newRequestQueue(this);
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

    public void response(String response) { }

    public Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            response(response);
        }
    };

    public Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), "통신이 불가능 합니다.", Toast.LENGTH_SHORT).show();
        }
    };



    // Noti Table에 업데이트 시킨다
    public void updateNotification(final int type, final User user, final User friend) {
        String url = "insertNoti.do";

        String DATE_FORMAT = "yyyyMMdd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        Date today = Calendar.getInstance().getTime();
        final String pushDate = sdf.format(today);

        RequestQueue stringRequest = Volley.newRequestQueue(getApplicationContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dddd", response);
                        try {
                            JSONObject j = new JSONObject(response);
                            // 데이터 가져오기 성공할 때,
                            if (j.optString("result").equals("ok")) {
                                Toast.makeText(getApplicationContext(), "삽입하기 성공", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "삽입하기 실패", Toast.LENGTH_SHORT).show();
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


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
//            view = new View(activity);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
