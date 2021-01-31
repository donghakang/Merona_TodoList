package com.example.whenyoucomemerona.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.controller.My;
import com.example.whenyoucomemerona.entity.Noti;
import com.example.whenyoucomemerona.view.NotiAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NotificationFragment extends BaseFragment {

    ListView lvNotification;
    ArrayList<Noti> arr;
    NotiAdapter adapter;

    public NotificationFragment() {    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        LOAD_START();
        init(view);
        return view;
    }


    private void init(View v) {
        lvNotification = v.findViewById(R.id.lv_notification);

        arr = new ArrayList<>();

        params.clear();
        params.put("user_id", My.Account.getUser_id() + "");
        params.put("id", My.Account.getId());

        request("getNotiList.do");
    }


    @Override
    public void response(String response) {
        Log.d("noti", response);
        try {
            JSONObject j = new JSONObject(response);
            Log.d("noti", response);
            // 데이터 가져오기 성공할 때,
            if (j.optString("result").equals("ok")) {
                arr.clear();                    // 데이터를 가져오기전 정리한다.
                JSONArray data = j.optJSONArray("data");
                for (int i = 0; i < data.length(); i ++ ){
                    JSONObject item = data.optJSONObject(i);
                    int noti_id = item.optInt("noti_id");
                    int user_id = item.optInt("user_id");
                    int friend_id = item.optInt("friend_id");
                    int type = item.optInt("type");
                    String pushDate = item.optString("pushDate");;

                    Noti noti = new Noti();
                    noti.setNoti_id(noti_id);
                    noti.setUser_id(user_id);
                    noti.setFriend_id(friend_id);
                    noti.setType(type);
                    noti.setPushDate(pushDate);

                    arr.add(noti);
                }

            } else {
                Toast.makeText(getContext(), "리스트 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
            refresh(arr);
            setup();


        } catch (JSONException e) {
            Log.e("NOTIFICATION TAG", "JSON에서 에러가 있습니다.");
            e.printStackTrace();
        }

    }

    private void setup() {
        lvNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Noti noti = arr.get(position);

                switch (noti.getType()) {
                    case 0:
                        ((BottomNavigationView)getActivity().findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.page_1);
                        break;
                    case 1:
                        hideKeyboard(getActivity());
                        Fragment userPage = new UserPageFragment(noti.getUser_id());
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.body_rl, userPage)
                                .commit();
                        break;
                    case 2:
                        ((BottomNavigationView)getActivity().findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.page_1);
                        break;
                    default:
                        break;
                }
            }
        });

        LOAD_STOP();
    }

    private void refresh(ArrayList<Noti> noti) {
        adapter = new NotiAdapter(getActivity(), noti);
        lvNotification.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}