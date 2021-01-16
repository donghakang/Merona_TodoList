package com.example.whenyoucomemerona.controller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.whenyoucomemerona.model.Key;

import java.util.HashMap;
import java.util.Map;

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
}
