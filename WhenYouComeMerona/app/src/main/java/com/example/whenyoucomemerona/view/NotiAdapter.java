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

    Noti notification;
    NotiHolder viewHolder;
    int type;

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
        if (convertView == null) {
            convertView = lnf.inflate(R.layout.notification_list, parent, false);
            viewHolder = new NotiHolder();

            viewHolder.profileUserImg = convertView.findViewById(R.id.profile_user_img);
            viewHolder.tvMessage = convertView.findViewById(R.id.tv_message);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NotiHolder) convertView.getTag();
        }

        notification = arr.get(position);

        setupView(notification);

        return convertView;
    }




    private void setupView(Noti noti) {

        String msg = "";
        String time = noti.getTimeDiff();
        switch (noti.getType()) {
            // TODO: 아이디 이름을 변경한다.
            case 0:
                msg = notification.getWelcomeMsg();
                viewHolder.tvMessage.setText(Html.fromHtml("<b>" + noti.getUser().getId() + "</b>" + msg + "<font color='#d3d3d3'>  " + time + "</font>"));
                break;
            case 1:
                msg = notification.getFriendReqMsg();
                viewHolder.tvMessage.setText(Html.fromHtml("<b>" + noti.getUser().getId() + "</b>" + msg + "<font color='#d3d3d3'>  " + time + "</font>"));
                break;
            case 2:
                msg = notification.getTodoReqMsg();
                viewHolder.tvMessage.setText(Html.fromHtml("<b>" + noti.getUser().getId() + "</b>" + msg + "<font color='#d3d3d3'>  " + time + "</font>"));
                break;
            case 3:
                msg = notification.getLocationMsg();
                break;
            default:
                break;
        }
    }
}

