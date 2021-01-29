package com.example.whenyoucomemerona.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.controller.My;
import com.example.whenyoucomemerona.entity.User;
import com.example.whenyoucomemerona.view.SearchFriendAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendListFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    // Variables
    Button btnPrevious;
    TextView tvId;
    SearchView searchFriend;
    ListView listFriend;
    SearchFriendAdapter adapter;

    User currentUser;
    ArrayList<User> arr;

    // Functions
    FriendListFragment() {

    }

    FriendListFragment(User user) {
        this.currentUser = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friend_list, container, false);
        init(v);
        return v;
    }




    private void refresh(ArrayList<User> arr) {
        adapter = new SearchFriendAdapter(getActivity(), arr);
        listFriend.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    private void init(View v) {
        btnPrevious = v.findViewById(R.id.btn_previous);
        tvId = v.findViewById(R.id.tv_menubar_id);
        tvId.setText(this.currentUser.getId());
        searchFriend = v.findViewById(R.id.search_friend);
        listFriend = v.findViewById(R.id.list_friend);

        arr = new ArrayList<>();

        Log.d("dddd", "FRiNED LIST -->>> " + currentUser);
        params.clear();
        params.put("user_id", currentUser.getUser_id()+"");
        params.put("id", currentUser.getId());
        request("getFriendList.do");




        // 뒤로가기 버튼
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment userPageFragment = new UserPageFragment(currentUser);
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_back1,  // popExit
                            R.anim.slide_back2
                    )
                    .replace(R.id.body_rl, userPageFragment)
                    .addToBackStack(null)
                    .commit();
            }
        });




        // 친구 검색
        searchFriend.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<User> tmpArr = new ArrayList<>();

                for (User u : arr) {
                    if (u.getId().contains(newText)) {
                        tmpArr.add(u);
                    }
                }
                refresh(tmpArr);

                return false;
            }
        });


        // 아이템 클릭
        // TODO: tmpArr를 사용할때도 있어서, user_id 가 일치하는 지 화인 해봐야한다.
        listFriend.setOnItemClickListener(this);

    }





    @Override
    public void response(String response) {
        try {
            JSONObject j = new JSONObject(response);
            // 데이터 가져오기 성공할 때,
            if (j.optString("result").equals("ok")) {
                arr.clear();                    // 데이터를 가져오기전 정리한다.
                JSONArray data = j.optJSONArray("friend");
                for (int i = 0; i < data.length(); i ++ ){

                    JSONObject userObject = data.getJSONObject(i);
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

                refresh(arr);
            } else {
                Toast.makeText(getContext(), "찾기 실패", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d("eeeee", "JSON에서 에러가 있습니다.");
            e.printStackTrace();
        }
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User u = (User)listFriend.getItemAtPosition(position);

        Fragment userPageFragment = new UserPageFragment(u);
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
}
