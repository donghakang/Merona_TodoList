package com.example.whenyoucomemerona.main;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.example.whenyoucomemerona.entity.Todos;
import com.example.whenyoucomemerona.entity.User;
import com.example.whenyoucomemerona.url.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SearchFragment extends Fragment implements View.OnClickListener {

    SearchFriendAdapter adapter;
    EditText etSearch;
    Button btnSearchClear;
    Button btnSearchSubmit;
    ListView listView;

    ArrayList<User> arr;
    public SearchFragment() {  }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        etSearch = view.findViewById(R.id.et_search);
        btnSearchClear = view.findViewById(R.id.btn_search_clear);
        btnSearchSubmit = view.findViewById(R.id.btn_search_submit);

        listView = view.findViewById(R.id.list_friend);
        arr = new ArrayList<>();

        btnSearchSubmit.setOnClickListener(this);
        btnSearchClear.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search_submit) {
            String username = etSearch.getText().toString();
            if (username.length() == 0) {
                Toast.makeText(getContext(), "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
                etSearch.requestFocus();
            } else {
                searchFriend(username);

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

                adapter = new SearchFriendAdapter(getActivity(), arr);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        } else if (v.getId() == R.id.btn_search_clear) {
            etSearch.setText("");
        }
    }

    private void searchFriend(final String username) {
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        // TODO: login.do 로 변경한다.
        String url = URL.getUrl() +  "searchFriend.do";

        StringRequest myReq = new StringRequest(Request.Method.POST, url,
                successListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // TODO: 나중에 Nickname 으로 변경한다.
                params.put("username", username);
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
                    arr.clear();                    // 데이터를 가져오기전 정리한다.
                    JSONArray data = j.optJSONArray("friend");
                    for (int i = 0; i < data.length(); i ++ ){
                        JSONObject userObject = data.getJSONObject(i);
                        int user_id = userObject.getInt("user_id");
                        String id = userObject.getString("id");

                        User user = new User();
                        user.setUser_id(user_id);
                        user.setId(id);

                        arr.add(user);

                        Log.d("useruser", user.toString());
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "찾기 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "찾기 실패", Toast.LENGTH_SHORT).show();
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