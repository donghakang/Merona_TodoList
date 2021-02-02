package com.example.whenyoucomemerona.main;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.controller.My;
import com.example.whenyoucomemerona.entity.User;
import com.example.whenyoucomemerona.view.SearchFriendAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SearchFragment extends BaseFragment implements View.OnClickListener, TextView.OnEditorActionListener, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    SearchFriendAdapter adapter;
    SearchView etSearch;
    Button btnSearchSubmit;
    BottomNavigationView bottomNavigation;
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
        btnSearchSubmit = view.findViewById(R.id.btn_search_submit);

        listView = view.findViewById(R.id.list_friend);
        arr = new ArrayList<>();

        btnSearchSubmit.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        etSearch.setOnQueryTextListener(this);


        return view;
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search_submit) {
            String username = etSearch.getQuery().toString();
            search(username);
        }
    }


    @Override
    public void response(String response) {
        // 통신을 성공 할 시
        try {
            JSONObject j = new JSONObject(response);
            // 데이터 가져오기 성공할 때,
            if (j.optString("result").equals("ok")) {
                arr.clear();                    // 데이터를 가져오기전 정리한다.
                JSONArray data = j.optJSONArray("friend");
                for (int i = 0; i < data.length(); i ++ ){

                    JSONObject userObject = data.optJSONObject(i);
                    int user_id = userObject.optInt("user_id");
                    String id = userObject.optString("id");
                    String name = userObject.optString("name");
                    String email = userObject.optString("email");
                    String birth = userObject.optString("birth");
                    String token = userObject.optString("token");
                    if (My.Account.getUser_id() != user_id) {
                        User user = new User();
                        user.setUser_id(user_id);
                        user.setId(id);
                        user.setName(name);
                        user.setEmail(email);
                        user.setBirth(birth);
                        user.setToken(token);

                        arr.add(user);
                    } else {
                        // 자신일 경우
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "찾기 실패", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d("eeeee", "JSON에서 에러가 있습니다.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // search 버튼을 누르지 않아도, 검색이 완료되면 실행된다.
        if (v.getId() == R.id.et_search) {
            String username = etSearch.getQuery().toString();
            params.clear();
            params.put("username", username);
            request("searchFriend.do");

            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

            adapter = new SearchFriendAdapter(getActivity(), arr);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("dddd", "arr__: " + arr.get(position).toString() + "        " + "");
        Fragment userPageFragment = new UserPageFragment(arr.get(position));
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                )
                .replace(R.id.body_rl, userPageFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        search(newText);
        return false;
    }



    private void search(String username) {
        if (username.length() != 0) {
            params.clear();
            params.put("username", username);
            request("searchFriend.do");

            adapter = new SearchFriendAdapter(getActivity(), arr);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            arr.clear();
        }

    }


}