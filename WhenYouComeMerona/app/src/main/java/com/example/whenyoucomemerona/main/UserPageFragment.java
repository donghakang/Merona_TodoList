package com.example.whenyoucomemerona.main;

import android.content.DialogInterface;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.controller.My;
import com.example.whenyoucomemerona.entity.Todos;
import com.example.whenyoucomemerona.entity.User;
import com.example.whenyoucomemerona.lib.Level;
import com.example.whenyoucomemerona.model.Key;
import com.example.whenyoucomemerona.view.SimpleTodosAdapter;
import com.example.whenyoucomemerona.view.TodosAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserPageFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    User user;

    TextView userId, userEmail;
    Spinner userSetting;             // 친구 탈퇴 및 정보들이 있다.
    Button btnFriend;

    ProgressBar pgLevel;
    TextView tvLevelCount, tvUserContentCount, tvOurContentCount;
    TextView tvLevel, tvUserContent, tvOurContent;

    ListView lvUserPageList;
    SimpleTodosAdapter adapter;

    ArrayList<Todos> userTodos;
    ArrayList<Todos> sharedTodos;
    ArrayList<Todos> allTodos;

    int status;

    public UserPageFragment() {
        // Required empty public constructor
    }

    public UserPageFragment(User user) {
        this.user = user;
        Log.d("ddddd", user.toString());

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_page, container, false);
        LOAD_START();
        init(v);
        return v;
    }



    private void init(View v) {

        userId = v.findViewById(R.id.user_id);
        userEmail = v.findViewById(R.id.user_email);
        userSetting = v.findViewById(R.id.user_setting);
        btnFriend = v.findViewById(R.id.btn_friend);

        pgLevel = v.findViewById(R.id.pg_level);
        tvLevelCount = v.findViewById(R.id.tv_level_count);
        tvUserContentCount = v.findViewById(R.id.tv_user_content_count);
        tvOurContentCount = v.findViewById(R.id.tv_our_content_count);
        tvLevel = v.findViewById(R.id.tv_level);
        tvUserContent = v.findViewById(R.id.tv_user_content);
        tvOurContent = v.findViewById(R.id.tv_our_content);

        lvUserPageList = v.findViewById(R.id.lv_user_page_list);


        userTodos = new ArrayList<>();
        sharedTodos = new ArrayList<>();
        allTodos = new ArrayList<>();


        // Spinner 로 연결
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.friend_setting, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSetting.setAdapter(adapter);

        userSetting.setOnItemSelectedListener(this);

        btnFriend.setOnClickListener(this);

        params.clear();
        params.put("user_id", this.user.getUser_id() + "");
         request("getUserData.do");
    }


    @Override
    public void response(String response) {
        try {
            JSONObject json = new JSONObject(response);
            // 데이터 가져오기 성공할 때,
            if (json.optString("result").equals("ok")) {

                JSONObject user_data = json.optJSONObject("user");
                user.setUser_id(user_data.optInt("user_id"));
                user.setId(user_data.optString("id"));
                user.setName(user_data.optString("name"));
                user.setEmail(user_data.optString("email"));
                user.setBirth(user_data.optString("birth"));
                user.setToken(user_data.optString("token"));

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

                    if (tmp.isDone()) {
                        userTodos.add(tmp);
                    }

                    allTodos.add(tmp);
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


    // 서버에서 받은 자료들을 마이페이지에 옮긴다.
    private void myPageSetup() {
        userId.setText(user.getId());
        userEmail.setText(user.getName());

        tvUserContentCount.setText(userTodos.size() + "");
        tvOurContentCount.setText(sharedTodos.size() + "");

        // 레벨
        int[] level = Level.calcLevel(userTodos.size(), sharedTodos.size());
        pgLevel.setProgress(level[0]);
        pgLevel.setMax(level[1]);
        tvLevelCount.setText("Lv. " + level[2]);

        // 리스트
        ArrayList<Todos> tmpArr = new ArrayList<>();
        for (Todos t : allTodos) {
            if (t.isDone()) {
                tmpArr.add(t);              // 한 일 만 삽입한다.
            }
        }
        Collections.sort(tmpArr);           // sort by 작성된 날들.

        adapter = new SimpleTodosAdapter(getActivity(), tmpArr);
        lvUserPageList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        checkStatus();
    }






    private void buttonSetup() {
        if (status == 0) {
            btnFriend.setBackgroundResource(R.drawable.button);
            btnFriend.setText("친구 신청 하기");
        } else if (status == 1) {
            btnFriend.setBackgroundResource(R.drawable.default_button);
            btnFriend.setText("친구 신청 취소 하기");
        } else if (status == 2) {
            btnFriend.setBackgroundResource(R.drawable.button);
            btnFriend.setText("친구 확인");
        } else if (status == 3) {
            btnFriend.setBackgroundResource(R.drawable.default_button);
            btnFriend.setText("친구 보기");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_friend) {
            // TODO: 버튼을 가리고, 보이게 한다.
            Log.d("dddd", user.toString() + "Add");

            if (status == 0) {
                // 친구 신청 하기
                requestFriend();

            } else if (status == 1) {
                // 친구 신청 취소 하기
                deleteFriendRequest();

                Log.d("dddd", "CURR_STATUS: " + 1 + "      NEXT_STATUS: " + status);
            } else if (status == 2) {
                // 친구 확인
                insertFriend();
                Log.d("dddd", "CURR_STATUS: " + 2 + "      NEXT_STATUS: " + status);
            } else if (status == 3) {
                // 친구 보기
                getFriendList();
            }
        }

    }

    // -----------------------------------------------------------------------------------------------
    // 친구와 나의 관계 확인 ------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------

    private void checkStatus() {
        String url = "checkStatus.do";
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // success listener
                try {
                    JSONObject j = new JSONObject(response);
                    // 데이터 가져오기 성공할 때,
                    if (j.optString("result").equals("ok")) {
                        status = j.optInt("status");
                        Log.d("dddd", "STATUS: " + status);
                        Toast.makeText(getContext(), "status okay", Toast.LENGTH_LONG).show();
                        buttonSetup();
                    } else {
                        Toast.makeText(getContext(), "status failed", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getContext(), "JSON 오류", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                LOAD_STOP();        // init ends;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error listener
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
                LOAD_STOP();        // init ends;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", My.Account.getUser_id() + "");
                params.put("friend_id", user.getUser_id() + "");
                return params;
            }
        };


        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }


    // 친구 신청을 보낸다 ------------------------------------------------------------------------------
    public void requestFriend() {
        String url = "requestFriend.do";
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // success listener
                try {
                    JSONObject j = new JSONObject(response);
                    // 데이터 가져오기 성공할 때,
                    if (j.optString("result").equals("ok")) {
                        Toast.makeText(getContext(), "친구 신청 성공", Toast.LENGTH_SHORT).show();

                        String friend = user.getToken();
                        String title = "[" + user.getId() + "] " + My.Account.getId() + "님의 친구 신청";
                        String message = My.Account.getId() + "님께서 친구 신청을 보냈습니다!";

                        Log.d("Notification Tag" , user.getToken());
                        Log.d("Notification Tag" , user.getId());

                        sendNotification(friend, title, message);
                        // TODO: updateNotification
                        Log.d("dddd", "FRIEND's USERID: " + user.getUser_id());
                        updateNotification(1, My.Account, user);
                        checkStatus();
                    } else {
                        Toast.makeText(getContext(), "친구 신청 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "JSON 오류", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error listener
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // TODO: 데이터를 집어넣는다.
                params.put("user_id", My.Account.getUser_id() + "");
                params.put("friend_id", user.getUser_id() + "");
                return params;
            }
        };


        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }


    // 친구 신청을 취소한다 ------------------------------------------------------------------------------

    public void deleteFriendRequest() {
        String url = "deleteFriendRequest.do";
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // success listener
                try {
                    JSONObject j = new JSONObject(response);
                    // 데이터 가져오기 성공할 때,
                    if (j.optString("result").equals("ok")) {
                        Toast.makeText(getContext(), "친구 신청 취소 성공", Toast.LENGTH_SHORT).show();
                        checkStatus();
                    } else {
                        Toast.makeText(getContext(), "친구 신청 취소 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "JSON 오류", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error listener
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // TODO: 데이터를 집어넣는다.
                params.put("user_id", My.Account.getUser_id() + "");
                params.put("friend_id", user.getUser_id() + "");
                return params;
            }
        };


        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }


    // 친구 확인 --------------------------------------------------------------------------------------
    public void insertFriend() {
        String url = "insertFriend.do";
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // success listener
                try {
                    JSONObject j = new JSONObject(response);
                    // 데이터 가져오기 성공할 때,
                    if (j.optString("result").equals("ok")) {
                        Toast.makeText(getContext(), "친구와 할 일을 공유하세요", Toast.LENGTH_SHORT).show();
                        checkStatus();
                    } else {
                        Log.e("FRIEND CANCEL TAG", "친구 신청 취소 실패");
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "JSON 오류", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error listener
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // TODO: 데이터를 집어넣는다.
                params.put("user_id", My.Account.getUser_id() + "");
                params.put("friend_id", user.getUser_id() + "");
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }



    // 친구 관계 해제 ----------------------------------------------------------------------------------
    public void deleteFriend() {
        String url = "deleteFriend.do";
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // success listener
                try {
                    JSONObject j = new JSONObject(response);
                    // 데이터 가져오기 성공할 때,
                    if (j.optString("result").equals("ok")) {
                        Toast.makeText(getContext(), "친구 신청 취소 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "친구 신청 취소 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "JSON 오류", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error listener
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // TODO: 데이터를 집어넣는다.
                params.put("user_id", My.Account.getUser_id() + "");
                params.put("friend_id", user.getUser_id() + "");
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }



    // 친구 리스트 불러오기 페이지로 이동 --------------------------------------------------------------------
    private void getFriendList() {
        Log.d("dddd", "FRIEND LIST --> "  + user);
        Fragment friendListFragment = new FriendListFragment(user);
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


    // 드랍 다운
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        if (position == 0) {
//            // 친구 끊기
//
//            AlertDialog.Builder deleteBox = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
//            LayoutInflater lnf = getLayoutInflater();
//            View titleView = lnf.inflate(R.layout.add_level_title, null);
//            TextView title = titleView.findViewById(R.id.dialog_title);
//            title.setText("친구 끊기");
//            deleteBox.setCustomTitle(titleView);
//            deleteBox.setMessage(user.getId() + "님과 친구관계를 해제할까요?");
//            deleteBox.setPositiveButton("예", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    deleteFriend();
//                }
//            });
//
//            deleteBox.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // 아무일 일어나지 않는다.
//                }
//            });
//
//            deleteBox.create().show();
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
