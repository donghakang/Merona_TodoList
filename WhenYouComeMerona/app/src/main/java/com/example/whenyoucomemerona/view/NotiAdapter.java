package com.example.whenyoucomemerona.view;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.entity.Noti;
import com.example.whenyoucomemerona.entity.Todos;
import com.example.whenyoucomemerona.entity.User;
import com.example.whenyoucomemerona.model.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotiAdapter extends ArrayAdapter {

    LayoutInflater lnf;
    ArrayList<Noti> arr;
    Activity ctx;

    User user;
    User friend;

    class NotiHolder{
        ImageView profileUserImg;
        TextView tvMessage;
    }

    public NotiAdapter (Activity context, ArrayList<Noti> arr) {
        super(context, R.layout.fragment_notification, arr);
        this.ctx = context;
        this.arr = arr;
        lnf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Nullable
    @Override
    public Noti getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final NotiHolder viewHolder;
        if (convertView == null) {
            convertView = lnf.inflate(R.layout.notification_list, parent, false);
            viewHolder = new NotiHolder();

            viewHolder.profileUserImg = convertView.findViewById(R.id.profile_user_img);
            viewHolder.tvMessage = convertView.findViewById(R.id.tv_message);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NotiHolder) convertView.getTag();
        }




        Noti notification = arr.get(position);
        int type = notification.getType();
        String msg = "";
        String id = "";
        String time = notification.getTimeDiff();
//        getUser(notification.getUser_id());
//        getUser(notification.getFriend_id());


        switch (type) {
            // TODO: 아이디 이름을 변경한다.
            case 0:
                msg = notification.getWelcomeMsg();
                break;
            case 1:
                msg = notification.getFriendReqMsg();
                viewHolder.tvMessage.setText(Html.fromHtml(msg + "<font color='#c3c3c3'>  " + time + "</font>"));
                break;
            case 2:
                msg = notification.getTodoReqMsg();
                break;
            case 3:
                msg = notification.getLocationMsg();
                break;
            default:
                break;
        }

        Log.d("dddd", msg + "    " + time);

        viewHolder.tvMessage.setText(Html.fromHtml(msg + "<font color='#d3d3d3'>  " + time + "</font>"));

        return convertView;
    }



    private void getUser(final int id_number) {
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        String url = "getUserPage.do";

        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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

                            } else {
                                Log.d("Result Tag", "User 가 없습니다.");
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "JSON 오류", Toast.LENGTH_SHORT).show();
                            Log.e("JSON Tag", "JSON 형식에 오류가 있습니다");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response Tag", "response 를 받지 못했습니다.");
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", id_number + "");
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }
}

