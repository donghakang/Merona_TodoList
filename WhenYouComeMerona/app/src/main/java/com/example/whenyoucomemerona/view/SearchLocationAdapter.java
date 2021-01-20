package com.example.whenyoucomemerona.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.entity.Address;

import java.util.ArrayList;

public class SearchLocationAdapter extends ArrayAdapter {

    LayoutInflater lnf;
    ArrayList<Address> arr;
    Activity ctx;

    class SearchLocationHolder{
        TextView addressName;
        TextView categoryName;
        TextView placeName;
    }

    public SearchLocationAdapter (Activity context, ArrayList<Address> arr) {
        super(context, R.layout.fragment_map, arr);
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
    public Address getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final SearchLocationAdapter.SearchLocationHolder viewHolder;
        if (convertView == null) {
            convertView = lnf.inflate(R.layout.search_location_list, parent, false);
            viewHolder = new SearchLocationAdapter.SearchLocationHolder();

            viewHolder.addressName = convertView.findViewById(R.id.tv_address_name);
            viewHolder.categoryName = convertView.findViewById(R.id.tv_category_name);
            viewHolder.placeName = convertView.findViewById(R.id.tv_place_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SearchLocationAdapter.SearchLocationHolder) convertView.getTag();
        }
        viewHolder.addressName.setText(arr.get(position).getAddress_name());
        viewHolder.categoryName.setText(arr.get(position).getCategory_name());
        viewHolder.placeName.setText(arr.get(position).getPlace_name());

        return convertView;
    }
}
