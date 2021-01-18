package com.example.whenyoucomemerona.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.entity.Todos;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class EditFragment extends BaseFragment implements View.OnClickListener {

    EditText etContent;
    Button btnSubmit;

    Todos todo;


    public EditFragment(Todos todo) {
        this.todo = todo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        etContent = v.findViewById(R.id.et_content);
        btnSubmit = v.findViewById(R.id.btn_submit);

        // keyboard를 킨다.
        etContent.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        btnSubmit.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        params.clear();
        params.put("todo_id", todo.getTodo_id() + "");
        params.put("content", etContent.getText().toString());
        params.put("done", todo.isDone() + "");
        request("editItem.do");
    }

    @Override
    public void response(String response) {
        // 통신을 성공 할 시
        try {
            JSONObject j = new JSONObject(response);
            // 데이터 가져오기 성공할 때,
            if (j.optString("result").equals("ok")) {
                Toast.makeText(getContext(), "수정 성공", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "수정 실패", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d("eeeee", "JSON에서 에러가 있습니다.");
            e.printStackTrace();
        }
    }
}
