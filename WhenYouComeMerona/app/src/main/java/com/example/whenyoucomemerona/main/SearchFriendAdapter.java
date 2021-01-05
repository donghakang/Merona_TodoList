package com.example.whenyoucomemerona.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.entity.Todos;
import com.example.whenyoucomemerona.entity.User;

import java.util.ArrayList;

public class SearchFriendAdapter extends ArrayAdapter {
    LayoutInflater lnf;
    ArrayList<User> arr;
    Activity ctx;

    class SearchFriendHolder{
        ImageView profileUserImg;
        TextView tvUsername;
    }

    public SearchFriendAdapter (Activity context, ArrayList<User> arr) {
        super(context, R.layout.fragment_search, arr);
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
    public User getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final SearchFriendAdapter.SearchFriendHolder viewHolder;
        if (convertView == null) {
            convertView = lnf.inflate(R.layout.search_friend_list, parent, false);
            viewHolder = new SearchFriendAdapter.SearchFriendHolder();

            viewHolder.profileUserImg = convertView.findViewById(R.id.profile_user_img);
            viewHolder.tvUsername = convertView.findViewById(R.id.tv_username);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SearchFriendAdapter.SearchFriendHolder) convertView.getTag();
        }
        viewHolder.tvUsername.setText(arr.get(position).getId());

        return convertView;
    }
}