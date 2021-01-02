package com.example.whenyoucomemerona.ui.home;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.whenyoucomemerona.R;

import java.util.ArrayList;

public class TodosAdapter extends ArrayAdapter {

    LayoutInflater lnf;
    ArrayList<Todos> arr;

    class TodosItemHolder{
        TextView tvContent;
        RadioButton rbIsDone;
        TextView tvDate;
    }

    public TodosAdapter (Activity context, ArrayList<Todos> arr) {
        super(context, R.layout.fragment_home, arr);
        this.arr = arr;
        lnf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Nullable
    @Override
    public Todos getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TodosAdapter.TodosItemHolder viewHolder;
        if (convertView == null) {
            convertView = lnf.inflate(R.layout.todos_list, parent, false);
            viewHolder = new TodosItemHolder();

            viewHolder.tvContent = convertView.findViewById(R.id.tv_content);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TodosAdapter.TodosItemHolder) convertView.getTag();
        }

        viewHolder.tvContent.setText(arr.get(position).content);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),getItem(position).getContent() + "  " + position,  Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

}
