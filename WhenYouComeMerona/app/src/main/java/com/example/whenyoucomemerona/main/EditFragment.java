package com.example.whenyoucomemerona.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.entity.Todos;
import com.example.whenyoucomemerona.url.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class EditFragment extends Fragment implements View.OnClickListener {

    EditText etContent;
    Button btnSubmit;

    Todos todo;


    public EditFragment(Todos todo) {
        this.todo = todo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        etContent = v.findViewById(R.id.et_content);
        btnSubmit = v.findViewById(R.id.btn_submit);

        // keyboard를 킨다.
        etContent.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        btnSubmit.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        RequestQueue stringRequest = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        // TODO: login.do 로 변경한다.
        String url = URL.getUrl() + "editItem.do";

        StringRequest myReq = new StringRequest(Request.Method.POST, url,
                successListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("todo_id", todo.getTodo_id() + "");
                params.put("content", etContent.getText().toString());
                params.put("done", todo.getDone() + "");
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
                if (j.optString("result").equals("ok")) {
                    Toast.makeText(getContext(), "수정 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "수정 실패", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "통신이 불가능 합니다.", Toast.LENGTH_SHORT).show();
        }
    };
}
