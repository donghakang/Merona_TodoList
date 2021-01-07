package com.example.whenyoucomemerona.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.whenyoucomemerona.entity.Todos;
import com.example.whenyoucomemerona.url.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TodosAdapter extends ArrayAdapter {

    LayoutInflater lnf;
    ArrayList<Todos> arr;
    Activity ctx;

    class TodosItemHolder{
        TextView tvContent;
        CheckBox cbIsDone;
        TextView tvDate;
    }

    public TodosAdapter (Activity context, ArrayList<Todos> arr) {
        super(context, R.layout.fragment_home, arr);
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
    public Todos getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final TodosAdapter.TodosItemHolder viewHolder;
        if (convertView == null) {
            convertView = lnf.inflate(R.layout.todos_list, parent, false);
            viewHolder  = new TodosItemHolder();

            viewHolder.tvContent = convertView.findViewById(R.id.tv_content);
            viewHolder.cbIsDone  = convertView.findViewById(R.id.cb_is_done);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TodosAdapter.TodosItemHolder) convertView.getTag();
        }

        viewHolder.tvContent.setText(arr.get(position).getContent());
        viewHolder.cbIsDone.setChecked(arr.get(position).getDone());

        viewHolder.cbIsDone.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),viewHolder.cbIsDone.isChecked()+"",  Toast.LENGTH_SHORT).show();
                boolean status = viewHolder.cbIsDone.isChecked();       // 버튼을 업데이트할때, 상태를 파악함 (true/false);
                updateItem(position, status);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),getItem(position).getContent() + "  " + position,  Toast.LENGTH_SHORT).show();
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("타이틀 짜잔");
                builder.setMessage("메시지가 들어갑니다");
                builder.setPositiveButton("수정하기", new DialogInterface.OnClickListener() {
                    /**
                     * 수정하기 버튼을 누른다.
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment editFragment = new EditFragment(getItem(position));
                        ((AppCompatActivity) ctx).getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(
                                        R.anim.slide_in,  // enter
                                        R.anim.fade_out,  // exit
                                        R.anim.fade_in,   // popEnter
                                        R.anim.slide_out  // popExit
                                )
                                .replace(R.id.body_rl, editFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });


                builder.setNegativeButton("삭제하기", new DialogInterface.OnClickListener() {
                    /**
                     * 삭제하기 버튼을 누른다.
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder deleteBox = new AlertDialog.Builder(getContext());
                        deleteBox.setTitle("삭제하기");
                        deleteBox.setMessage("정말로 삭제하시겠습니까?");
                        deleteBox.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RequestQueue stringRequest = Volley.newRequestQueue(getContext());
                                String url = "deleteItem.do";

                                StringRequest myReq = new StringRequest(Request.Method.POST, URL.getUrl() + url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // 통신을 성공 할 시
                                        try {
                                            JSONObject j = new JSONObject(response);
                                            // 데이터 가져오기 성공할 때,
                                            Log.d("eeeee", response);
                                            if (j.optString("result").equals("ok")) {
                                                remove(getItem(position));
                                                notifyDataSetChanged();
                                                Toast.makeText(getContext(), "삭제 성공", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "삭제 실패", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            Log.d("eeeee", "JSON에서 에러가 있습니다.");
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // 통신을 실패할 시
                                        Toast.makeText(getContext(), "통신이 불가능 합니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("todo_id", getItem(position).getTodo_id()+"");
                                        params.put("content", getItem(position).getContent());
                                        params.put("done", getItem(position).getDone()+"");
                                        return params;
                                    }
                                };

                                myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
                                stringRequest.add(myReq);

                                // TODO: Adapter에서 HomeFragment getTodoList와 연결해야함.


                            }
                        });

                        deleteBox.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {    }
                        });

                        deleteBox.create().show();
                    }

                });

                builder.setNeutralButton("취소하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  }
                });

                builder.create().show();
                return true;
            }
        });
        return convertView;
    }


    private void updateItem(final int position, final boolean status) {
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        String url = "updateCheckbox.do";

        StringRequest myReq = new StringRequest(Request.Method.POST, URL.getUrl() + url,
                updateSuccessListener, updateErrorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("todo_id", arr.get(position).getTodo_id() + "");
                params.put("content", arr.get(position).getContent());
                params.put("done", status + "");
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }

    Response.Listener<String> updateSuccessListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            // 통신을 성공 할 시
            try {
                JSONObject j = new JSONObject(response);
                // 데이터 가져오기 성공할 때,
                Log.d("eeeee", response);
                if (j.optString("result").equals("ok")) {
                    Toast.makeText(getContext(), "업데이트 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "업데이트 실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.d("eeeee", "JSON에서 에러가 있습니다.");
                e.printStackTrace();
            }
        }
    };

    // when obtaining data is unsuccessful.
    Response.ErrorListener updateErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // 통신을 실패할 시
            Log.d("eeeee", "통신 실패.");
            Toast.makeText(getContext(), "통신이 불가능 합니다.", Toast.LENGTH_SHORT).show();
        }
    };



}