package com.example.whenyoucomemerona.ui.home;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.whenyoucomemerona.R;

import java.util.ArrayList;

public class TodosAdapter extends RecyclerView.Adapter<TodosAdapter.TodosItemHolder> {

    ArrayList<Todos> todos;
    Context context;

    public TodosAdapter(Context context, ArrayList<Todos> todos) {
        this.context = context;
        this.todos = todos;
    }

    @NonNull
    @Override
    public TodosItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater lnf = LayoutInflater.from(context);
        View view = lnf.inflate(R.layout.todos_list, parent, false);

        return new TodosItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodosItemHolder holder, int position) {
        holder.tvContent.setText(todos.get(position).getContent());
        holder.rbIsDone.setChecked(todos.get(position).isDone());
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public class TodosItemHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        RadioButton rbIsDone;
        public TodosItemHolder(@NonNull View itemView) {
            super(itemView);

            tvContent = itemView.findViewById(R.id.tv_content);
            rbIsDone = itemView.findViewById(R.id.rb_is_done);
        }
    }
}
