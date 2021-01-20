package com.example.whenyoucomemerona.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.controller.My;
import com.example.whenyoucomemerona.controller.StaticFunction;
import com.example.whenyoucomemerona.entity.Todos;
import com.example.whenyoucomemerona.entity.User;
import com.example.whenyoucomemerona.lib.Level;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyPageFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    RelativeLayout rlMyPage;
    TextView myId, myEmail;
    Spinner mySetting;
    Button btnFriendList;

    ProgressBar pgLevel;
    TextView tvLevelCount, tvMyContentCount, tvOurContentCount;
    TextView tvLevel, tvMyContent, tvOurContent;

    ListView lvMyPageList;


    ArrayList<Todos> myTodos;
    ArrayList<Todos> sharedTodos;
    ArrayList<Todos> allTodos;

    public MyPageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_page, container, false);
        rlMyPage = v.findViewById(R.id.my_page);

        init(v);

        return v;
    }





    // init ------------------------------------------------------
    private void init (View v) {

        myId = v.findViewById(R.id.my_id);
        myEmail = v.findViewById(R.id.my_email);
        mySetting = v.findViewById(R.id.my_setting);
        btnFriendList = v.findViewById(R.id.btn_friend_list);


        pgLevel = v.findViewById(R.id.pg_level);
        tvLevelCount = v.findViewById(R.id.tv_level_count);
        tvMyContentCount = v.findViewById(R.id.tv_my_content_count);
        tvOurContentCount = v.findViewById(R.id.tv_our_content_count);
        tvLevel = v.findViewById(R.id.tv_level);
        tvMyContent = v.findViewById(R.id.tv_my_content);
        tvOurContent = v.findViewById(R.id.tv_our_content);

        lvMyPageList = v.findViewById(R.id.lv_my_page_list);


        myTodos = new ArrayList<>();
        sharedTodos = new ArrayList<>();
        allTodos = new ArrayList<>();

        // TODO: load different object.
        params.clear();
        params.put("user_id", My.Account.getUser_id() + "");
        params.put("id", My.Account.getId());
        request("getUserData.do");
        myPageSetup();


        // Spinner 로 연결
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.my_page_setting, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySetting.setAdapter(adapter);

        mySetting.setOnItemSelectedListener(this);

        btnFriendList.setOnClickListener(this);

    }


    // 서버에서 받은 자료들을 마이페이지에 옮긴다.
    private void myPageSetup() {
        myId.setText(My.Account.getId());
        myEmail.setText(My.Account.getEmail());

        tvMyContentCount.setText(myTodos.size() + "");
        tvOurContentCount.setText(sharedTodos.size() + "");

        int[] level = Level.calcLevel(myTodos.size(), sharedTodos.size());
        pgLevel.setProgress(level[0]);
        pgLevel.setMax(level[1]);
        tvLevelCount.setText("Lv. " + level[2]);

    }


    @Override
    public void response(String response) {
        Log.d("dddd", response);
        try {
            JSONObject json = new JSONObject(response);
            // 데이터 가져오기 성공할 때,
            if (json.optString("result").equals("ok")) {
                JSONArray dataArr = json.optJSONArray("data");
                for (int i = 0; i < dataArr.length(); i ++) {
                    JSONObject d = dataArr.optJSONObject(i);
                    Todos tmp = new Todos();

                    tmp.setTodo_id(d.optInt("todo_id"));
                    tmp.setContent(d.optString("content"));
                    tmp.setMemo(d.optString("memo"));
                    tmp.setDuedate(d.optString("duedate"));
                    tmp.setDuetime(d.optString("duetime"));
                    tmp.setShare_with(d.optString("share_with"));
                    tmp.setImportance(d.optDouble("importance"));
                    tmp.setWriter_id(d.optInt("writer_id"));
                    tmp.setAddr_id(d.optInt("addr_id"));
                    tmp.setDone(d.optBoolean("done"));

                    Log.d("dddd", tmp.isDone() + "");
                    Log.d("dddd", (tmp.isDone() == false) + "");
                    if (tmp.isDone()) {
                        myTodos.add(tmp);
                    }
                    Log.d("dddd", tmp.isDone() + "");
                    allTodos.add(tmp);
                    Log.d("dddd", tmp.isDone() + "");
                }

                JSONArray sharedArr = json.optJSONArray("shared_data");
                for (int j = 0; j < sharedArr.length(); j ++) {
                    JSONObject d = dataArr.optJSONObject(j);
                    Todos tmp = new Todos();

                    tmp.setTodo_id(d.optInt("todo_id"));
                    tmp.setContent(d.optString("content"));
                    tmp.setMemo(d.optString("memo"));
                    tmp.setDuedate(d.optString("duedate"));
                    tmp.setDuetime(d.optString("duetime"));
                    tmp.setShare_with(d.optString("share_with"));
                    tmp.setImportance(d.optDouble("importance"));
                    tmp.setWriter_id(d.optInt("writer_id"));
                    tmp.setAddr_id(d.optInt("addr_id"));
                    tmp.setDone(d.optBoolean("done"));

                    Log.d("dddd", tmp.isDone() + "");

                    if (tmp.isDone()) {
                        sharedTodos.add(tmp);
                    }

                    allTodos.add(tmp);
                }

                myPageSetup();
            } else {
                Toast.makeText(getContext(), "status failed", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getContext(), "JSON 오류", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }






    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            // TODO: 설정

        } else {
            // 로그아웃
            SharedPreferences auto = getActivity().getSharedPreferences("auto", Activity.MODE_PRIVATE);
            SharedPreferences.Editor autoLogin = auto.edit();
            autoLogin.putString("auto_id", "");
            autoLogin.putString("auto_pw", "");
            autoLogin.apply();

            My.Account = new User();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
            Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // 설정에서 아무것도 고르지 않는다.
    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_friend_list) {
            // TODO: get Friend List
            getFriendList(v);
        }
    }


    // 친구 리스트 불러오기 페이지로 이동 --------------------------------------------------------------------
    private void getFriendList(View v) {
        Fragment friendListFragment = new FriendListFragment(My.Account);
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_next1,
                R.anim.slide_next2
            )
            .replace(R.id.body_rl, friendListFragment)
            .addToBackStack(null)
            .commit();
    }
}