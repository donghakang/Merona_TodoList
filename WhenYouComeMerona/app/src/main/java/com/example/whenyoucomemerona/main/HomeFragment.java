package com.example.whenyoucomemerona.main;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.controller.My;
import com.example.whenyoucomemerona.entity.Address;
import com.example.whenyoucomemerona.entity.AddressTodos;
import com.example.whenyoucomemerona.entity.Todos;
import com.example.whenyoucomemerona.view.TodosAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class HomeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

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


    @Override
    public void response(String response) {
        // 통신을 성공 할 시
        try {
            JSONObject j = new JSONObject(response);
            if (j.optString("result").equals("ok")) {
                arr.clear();
                My.todos.clear();
                JSONArray data = j.optJSONArray("data");
                for (int i = 0; i < data.length(); i ++ ){
                    JSONObject item = data.optJSONObject(i);

                    Todos todo = new Todos();
                    todo.setTodo_id(item.optInt("todo_id"));
                    todo.setContent(item.optString("content"));
                    todo.setMemo(item.optString("memo"));
                    todo.setDuedate(item.optString("duedate"));
                    todo.setDuetime(item.optString("duetime"));
                    todo.setShare_with(item.optString("share_with"));
                    todo.setWriter_id(item.optInt("writer_id"));
                    todo.setAddr_id(item.optInt("addr_id"));
                    todo.setDone(item.optBoolean("done"));

                    JSONObject jsonAddress = item.optJSONObject("address");
                    Address address = new Address();
                    address.setAddr_id(jsonAddress.optInt("addr_id"));
                    todo.setAddr_id(jsonAddress.optInt("addr_id"));
                    address.setAddress_name(jsonAddress.optString("address_name"));
                    address.setRoad_address_name(jsonAddress.optString("road_address_name"));
                    address.setPlace_name(jsonAddress.optString("place_name"));
                    address.setCategory_name(jsonAddress.optString("category_name"));
                    address.setLat(jsonAddress.optDouble("lat"));
                    address.setLng(jsonAddress.optDouble("lng"));
                    address.setNotify(jsonAddress.optBoolean("notify"));

                    AddressTodos addressTodos = new AddressTodos(todo, address);

                    arr.add(todo);
                    My.todos.add(addressTodos);

                    refresh(arr);
                    saveTodos();
                }
            } else {
                // data가 없습니다.
                Log.d("JSON TAG", "데이터가 없습니다.");
            }
        } catch (Exception e) {
            Log.d("RESPONSE", "데이터 없습니다.");
        }




    }

    /* ----------------------------------------------------------------------------------------------------
    * -------------------------------------------Create View----------------------------------------------
     ---------------------------------------------------------------------------------------------------- */

    @Override
    public void loadStart() {
        super.loadStart();
        list.setVisibility(View.INVISIBLE);
    }

    @Override
    public void loadEnd() {
        super.loadEnd();
        list.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        list = (ListView) view.findViewById(R.id.home_list);
        list.setItemsCanFocus(false);
        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);
        filterSpinner = (Spinner) view.findViewById(R.id.filter_spinner);
        btnSearchFriend = (Button) view.findViewById(R.id.btn_search_friend);

        searchFragment = new SearchFragment();

        params.clear();
        params.put("user_id", My.Account.getUser_id() + "");
        params.put("id", My.Account.getId());
        request("getMapData.do");


        // Scroll Down to refresh  ------------------------------------------------
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                params.clear();
                params.put("user_id", My.Account.getUser_id() + "");
                params.put("id", My.Account.getId());
                request("getMapData.do");
                pullToRefresh.setRefreshing(false);
            }
        });

        // 친구 찾기 버튼 설정 ---------------------------∂-----------------------------
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
                    if (!t.isDone()) {
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