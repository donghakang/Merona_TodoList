package com.example.whenyoucomemerona.main;

import androidx.appcompat.widget.SwitchCompat;
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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.controller.My;
import com.example.whenyoucomemerona.entity.Address;
import com.example.whenyoucomemerona.entity.AddressTodos;
import com.example.whenyoucomemerona.entity.Todos;
import com.example.whenyoucomemerona.lib.StaticFunction;
import com.example.whenyoucomemerona.view.TodosAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

public class HomeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    TodosAdapter adapter;
    ArrayList<AddressTodos> arr;
    ArrayList<AddressTodos> filtered;
    ListView list;
    SwipeRefreshLayout pullToRefresh;
    SwitchCompat switchDone;

    TextView logoIcon;
    Button btnSearchFriend;
    Spinner filterSpinner;

    SearchFragment searchFragment;

    RelativeLayout emptyPage;

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
    private void refresh(ArrayList<AddressTodos> arr) {

        adapter = new TodosAdapter(getActivity(), arr);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        if (arr.size() < 1) {
//            emptyPage.setVisibility(View.VISIBLE);
//            list.setVisibility(View.INVISIBLE);
//        } else {
//            emptyPage.setVisibility(View.INVISIBLE);
//            list.setVisibility(View.VISIBLE);
//        }

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
                    todo.setImportance(item.optDouble("importance"));
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

                    AddressTodos addressTodos = new AddressTodos(todo, address, false);

                    arr.add(addressTodos);
                    My.todos.add(addressTodos);

                }

                JSONArray shared = j.optJSONArray("shared");
                for (int i = 0; i < shared.length(); i ++ ){
                    JSONObject item = shared.optJSONObject(i);

                    Todos todo = new Todos();
                    todo.setTodo_id(item.optInt("todo_id"));
                    todo.setContent(item.optString("content"));
                    todo.setMemo(item.optString("memo"));
                    todo.setDuedate(item.optString("duedate"));
                    todo.setDuetime(item.optString("duetime"));
                    todo.setShare_with(item.optString("share_with"));
                    todo.setWriter_id(item.optInt("writer_id"));
                    todo.setAddr_id(item.optInt("addr_id"));
                    todo.setImportance(item.optDouble("importance"));
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

                    AddressTodos addressTodos = new AddressTodos(todo, address, true);

                    arr.add(addressTodos);
                    My.shared.add(addressTodos);
                }
                Collections.sort(arr, new Comparator<AddressTodos>() {
                    @Override
                    public int compare(AddressTodos o1, AddressTodos o2) {
                        return o1.getTodos().getTodo_id() - o2.getTodos().getTodo_id();
                    }
                });
                refresh(arr);
            } else {
                // data가 없습니다.
                Log.d("JSON TAG", "데이터가 없습니다.");
            }
        } catch (Exception e) {
            Log.d("RESPONSE", "데이터 없습니다.");
        }

        LOAD_STOP();

        checkEmpty(arr);

    }

    /* ----------------------------------------------------------------------------------------------------
    * -------------------------------------------Create View----------------------------------------------
     ---------------------------------------------------------------------------------------------------- */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        LOAD_START();


        arr = new ArrayList<>();
        filtered = new ArrayList<>();

        list = (ListView) view.findViewById(R.id.home_list);
        list.setItemsCanFocus(false);
        logoIcon = view.findViewById(R.id.tv_icon);
        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);
        filterSpinner = (Spinner) view.findViewById(R.id.filter_spinner);
        btnSearchFriend = (Button) view.findViewById(R.id.btn_search_friend);
        switchDone = view.findViewById(R.id.switch_done);
        emptyPage = view.findViewById(R.id.empty_status);
        switchDone.setChecked(true);            //  항상 전체를 보여준다
        searchFragment = new SearchFragment();

        emptyPage.setVisibility(View.INVISIBLE);

        params.clear();
        params.put("user_id", My.Account.getUser_id() + "");
        params.put("id", My.Account.getId());
        request("getMapData.do");


        // Scroll Down to refresh  ------------------------------------------------
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                filterSpinner.setSelection(0);
                filterSpinner.setSelected(false);

                params.clear();
                params.put("user_id", My.Account.getUser_id() + "");
                params.put("id", My.Account.getId());
                request("getMapData.do");
                pullToRefresh.setRefreshing(false);
            }
        });

        // 친구 찾기 버튼 설정 ---------------------------∂-----------------------------
        btnSearchFriend.setOnClickListener(this);

        // 놀이 버튼 설정
        logoIcon.setOnClickListener(this);

        // 필터 버튼 설정 ----------------------------------------------------------------
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.filter_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);

        filterSpinner.setOnItemSelectedListener(this);

        // 다된 일 필터 설정 ------------------------------------------------------------
        switchDone.setOnCheckedChangeListener(this);




        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search_friend) {
            Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
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
        } else if (v.getId() == R.id.tv_icon) {
            Fragment funFragment = new FunFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    )
                    .replace(R.id.body_rl, funFragment)
                    .addToBackStack("fun")
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
                Collections.sort(arr, new Comparator<AddressTodos>() {
                    @Override
                    public int compare(AddressTodos o1, AddressTodos o2) {
                        return o1.getTodos().getTodo_id() - o2.getTodos().getTodo_id();
                    }

                });
                refresh(arr);
                break;
            case 1:
                // TODO: 시간순으로 정렬
                // 시간 순

                Collections.sort(arr, new Comparator<AddressTodos>() {
                    @Override
                    public int compare(AddressTodos o1, AddressTodos o2) {
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                        String date1 = o1.getTodos().getDuedate();
                        String date2 = o2.getTodos().getDuedate();
                        Date o1Date = null;
                        Date o2Date = null;

                        String time1 = o1.getTodos().getDuetime();
                        String time2 = o2.getTodos().getDuetime();
                        Date o1Time = null;
                        Date o2Time = null;

                        if (date1.isEmpty() && date2.isEmpty()) return 0;
                        if (date1.isEmpty()) return 1;
                        if (date2.isEmpty()) return -1;


                        try {
                            o1Date = sdf1.parse(date1);
                            o2Date = sdf1.parse(date2);
                        }  catch (ParseException e) {
                            e.printStackTrace();
                        }

                        int resultDate = o1Date.compareTo(o2Date);
                        if (resultDate > 0) return 1;
                        else if (resultDate < 0) return -1;
                        else {
                            // 시간 정렬
                            if (time1.isEmpty() && time2.isEmpty()) return 0;
                            if (time1.isEmpty()) return 1;
                            if (time2.isEmpty()) return -1;

                            try {
                                o1Time = sdf2.parse(time1);
                                o2Time = sdf2.parse(time2);
                            }  catch (ParseException e) {
                                e.printStackTrace();
                            }

                            int resultTime = o1Time.compareTo(o2Time);
                            if (resultTime > 0) return 1;
                            else if (resultTime < 0) return -1;
                            else return 0;

                        }
                    }
                });
                refresh(arr);
                break;
            case 2:
                // 중요도 순
                Collections.sort(arr, new Comparator<AddressTodos>() {
                    @Override
                    public int compare(AddressTodos o1, AddressTodos o2) {
                        double diff = o1.getTodos().getImportance() - o2.getTodos().getImportance();
                        Log.d("ddddd", "Before sorting: " +diff+ "      " + o1.getTodos().getImportance() + "   " + o2.getTodos().getImportance());
                        if (Double.isNaN(o1.getTodos().getImportance()) && Double.isNaN(o2.getTodos().getImportance())) return 0;
                        if (Double.isNaN(o1.getTodos().getImportance())) return 1;
                        if (Double.isNaN(o2.getTodos().getImportance())) return -1;
                        if (diff > 0.0) return -1;
                        if (diff < 0.0) return 1;
                        return 0;
                    }
                });
                refresh(arr);
                break;
            case 3:
                // 나만 할 일
                refresh(My.todos);
                checkEmpty(My.todos);
                break;
            case 4:
                // 공유 된 일
                refresh(My.shared);
                checkEmpty(My.shared);
                break;
            default:
                break;
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            refresh(arr);
        } else {
            filtered.clear();
            for (AddressTodos t : arr) {
                if (!t.getTodos().isDone()) {
                    filtered.add(t);
                }
            }
            refresh(filtered);
        }
    }


    private void checkEmpty(ArrayList<AddressTodos> arr) {
        if (arr.size() < 1) {
            emptyPage.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
        } else {
            emptyPage.setVisibility(View.INVISIBLE);
            list.setVisibility(View.VISIBLE);
        }
    }
}