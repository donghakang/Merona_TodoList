package com.example.whenyoucomemerona.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AddFragment extends Fragment {

    EditText etContent;
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
        btnSubmit = view.findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertItem();
            }
        });

        return view;
    }

    public void insertItem() {
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        // TODO: login.do 로 변경한다.
        String url = "http://172.30.1.15:8098/merona/insertItem.do";

        StringRequest myReq = new StringRequest(Request.Method.POST, url,
                successListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("content", etContent.getText().toString());
                params.put("done", "false");
//                params.put("password", password);
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }


    Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            // 통신을 성공 할 시
            try {
                JSONObject j = new JSONObject(response);
                // 데이터 가져오기 성공할 때,
                Log.d("eeeee", response);
                if (j.optString("result").equals("ok")) {
                    Toast.makeText(getContext(), "삽입하기 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "삽입하기 실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.d("eeeee", "JSON에서 에러가 있습니다.");
                e.printStackTrace();
            }
        }
    };

    // when obtaining data is unsuccessful.
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // 통신을 실패할 시
            Log.d("eeeee", "통신 실패.");
            Toast.makeText(getContext(), "통신이 불가능 합니다.", Toast.LENGTH_SHORT).show();
        }
    };
}