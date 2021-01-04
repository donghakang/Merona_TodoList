package com.example.whenyoucomemerona.ui.home;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.example.whenyoucomemerona.ui.LoginActivity;
import com.example.whenyoucomemerona.ui.RegisterActivity;
import com.example.whenyoucomemerona.ui.search.SearchFragment;
import com.example.whenyoucomemerona.url.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HomeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    TodosAdapter adapter;
    ArrayList<Todos> arr = new ArrayList<>();
    ListView list;
    SwipeRefreshLayout pullToRefresh;

    Button btnSearchFriend;
    Spinner filterSpinner;

    SearchFragment searchFragment;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /*
    Server 에서 Data 들을 가지고 온다.
     */
    private void refresh(ArrayList<Todos> arr) {
        adapter = new TodosAdapter(getActivity(), arr);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void refreshData() {

        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        // TODO: login.do 로 변경한다.
        String url = URL.getUrl() +  "todoList.do";

        StringRequest myReq = new StringRequest(Request.Method.POST, url,
                successListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("id", id);
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
                if (j.optString("result").equals("ok")) {
                    arr.clear();                    // 데이터를 가져오기전 정리한다.
                    JSONArray data = j.optJSONArray("data");
                    for (int i = 0; i < data.length(); i ++ ){
                        JSONObject item = data.getJSONObject(i);
                        int todo_id = item.getInt("todo_id");
                        String content = item.getString("content");
                        boolean done = item.getBoolean("done");

                        Todos todo = new Todos();
                        todo.setTodo_id(todo_id);
                        todo.setContent(content);
                        todo.setDone(done);

                        arr.add(todo);
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "리스트 불러오기 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "리스트 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.d("ddddd", "JSON에서 에러가 있습니다.");
                e.printStackTrace();
            }
        }
    };

    // when obtaining data is unsuccessful.
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // 통신을 실패할 시
            Log.d("ddddd", "통신 실패.");
            Toast.makeText(getContext(), "통신이 불가능 합니다.", Toast.LENGTH_SHORT).show();
        }
    };



    /* ----------------------------------------------------------------------------------------------------
    * -------------------------------------------Create View----------------------------------------------
     ---------------------------------------------------------------------------------------------------- */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        list = (ListView) view.findViewById(R.id.home_list);
        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);
        filterSpinner = (Spinner) view.findViewById(R.id.filter_spinner);
        btnSearchFriend = (Button) view.findViewById(R.id.btn_search_friend);

        searchFragment = new SearchFragment();

        refreshData();

        adapter = new TodosAdapter(getActivity(), arr);
        list.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        // Scroll Down to refresh  ------------------------------------------------
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                pullToRefresh.setRefreshing(false);
            }

        });

        // 친구 찾기 버튼 설정 --------------------------------------------------------
        btnSearchFriend.setOnClickListener(this);

        // 필터 버튼 설정 ----------------------------------------------------------------
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.filter_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);

        filterSpinner.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search_friend) {
            Fragment searchFragment = new SearchFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    )
                    .replace(R.id.body_rl, searchFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    // ------------------------------------------------------------------
    // TODO: 필터
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        filterTodos(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {   }

    private void filterTodos(int position) {
        switch (position) {
            case 0:
                // 등록 순
                refresh(arr);
                break;
            case 1:
                // 다 된일 숨기기
                ArrayList<Todos> newArr = new ArrayList<>();
                for (Todos t : arr) {
                    if (!t.getDone()) {
                        newArr.add(t);
                    }
                }
                refresh(newArr);
                break;
            case 2:
                // 내가 할 일
                refresh(arr);
                break;
            case 3:
                // 공유 된 일
                refresh(arr);
                break;
            case 4:
                // 시간 순
                refresh(arr);
                break;
            case 5:
                // 중요도
                refresh(arr);
                break;
            default:
                break;
        }
    }
}