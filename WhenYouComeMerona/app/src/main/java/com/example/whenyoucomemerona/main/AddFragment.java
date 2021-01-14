package com.example.whenyoucomemerona.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    EditText etContent;
    EditText etMemo;

    EditText etDate, etTime, etLocation, etShare, etLevel;
    SwitchCompat switchDate, switchTime, switchLocation, switchShare, switchLevel;

    Button   btnSubmit;

    public AddFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        etContent = view.findViewById(R.id.et_content);
        etMemo = view.findViewById(R.id.et_memo);
        etDate = view.findViewById(R.id.et_date);
        etTime = view.findViewById(R.id.et_time);
        etLocation = view.findViewById(R.id.et_location);
        etShare = view.findViewById(R.id.et_share);
        etLevel = view.findViewById(R.id.et_level);

        switchDate = view.findViewById(R.id.switch_date);
        switchTime = view.findViewById(R.id.switch_time);
        switchLocation = view.findViewById(R.id.switch_location);
        switchShare = view.findViewById(R.id.switch_share);
        switchLevel = view.findViewById(R.id.switch_level);

        btnSubmit = view.findViewById(R.id.btn_submit);

        etDate.setOnClickListener(this);
        etTime.setOnClickListener(this);
        etLocation.setOnClickListener(this);
        etShare.setOnClickListener(this);
        etLevel.setOnClickListener(this);

        switchDate.setOnCheckedChangeListener(this);
        switchTime.setOnCheckedChangeListener(this);
        switchLocation.setOnCheckedChangeListener(this);
        switchShare.setOnCheckedChangeListener(this);
        switchLevel.setOnCheckedChangeListener(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertItem();
            }
        });
        return view;
    }

    public void insertItem() {
        params.clear();
        params.put("content", etContent.getText().toString());
        params.put("memo", etMemo.getText().toString());
        params.put("duedate", etDate.getText().toString());
        params.put("duetime", etDate.getText().toString());
        // TODO: fix these.
        params.put("location", etLocation.getText().toString());
        params.put("share_with", etShare.getText().toString());
        params.put("level", etLevel.getText().toString());
        params.put("done", "false");

        request("insertItem.do");
    }


    @Override
    public void response(String response) {
        // 통신을 성공 할 시
        try {
            JSONObject j = new JSONObject(response);
            // 데이터 가져오기 성공할 때,
            if (j.optString("result").equals("ok")) {
                Toast.makeText(getContext(), "삽입하기 성공", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "삽입하기 실패", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // EDIT TEXT 가 눌러질 경우
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_date) {
            // 날짜
            setupDate();
        } else if (v.getId() == R.id.et_time) {
            setupTime();
        } else if (v.getId() == R.id.et_location) {
            Log.d("debugging", "장소 선택");
            setupLocation();
        } else if (v.getId() == R.id.et_share) {
            Log.d("debugging", "공유 선택");
        } else if (v.getId() == R.id.et_level) {
            setupLevel();
        }
    }

    // SWITCH COMPAT 가 눌러질 경우
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.switch_date) {
            if (isChecked) {
                Log.d("dddd", "on");
                setupDate();
            } else {
                Log.d("dddd", "off");
                etDate.setText("");
            }
        } else if (buttonView.getId() == R.id.switch_time) {
            if (isChecked) {
                setupTime();
            } else {
                etTime.setText("");
            }
        } else if (buttonView.getId() == R.id.switch_location) {
            if (isChecked) {
                setupLocation();
            } else {
                etLocation.setText("");
            }
        } else if (buttonView.getId() == R.id.switch_share) {

        } else if (buttonView.getId() == R.id.switch_level) {
            if (isChecked) {
                setupLevel();
            } else {
                etLevel.setText("");
            }
        }
    }





    // 날짜를 설정한다.
    public void setupDate() {
        final Calendar cal = Calendar.getInstance();
        AlertDialog.Builder  builder = new AlertDialog.Builder(getContext());
        LayoutInflater lnf = getLayoutInflater();
        final View view = lnf.inflate(R.layout.add_date, null);
        final DatePicker date_picker = view.findViewById(R.id.add_date_picker);
        builder.setView(view);


        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (switchDate.isChecked() && etDate.getText().toString().length() == 0) {
                    // 버튼을 눌렀는데, 취소를 누를시 다시 버튼을 꺼버린다.
                    switchDate.setChecked(false);
                }
            }
        });
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: 확인
                cal.set(Calendar.YEAR, date_picker.getYear());
                cal.set(Calendar.MONTH, date_picker.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, date_picker.getDayOfMonth());

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etDate.setText(sdf.format(cal.getTime()));

                switchTime.setChecked(true);            // 스위치를 온으로 바꾼다
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 시간을 설정한다.
    public void setupTime() {
        AlertDialog.Builder  builder = new AlertDialog.Builder(getContext());
        LayoutInflater lnf = getLayoutInflater();
        final View view = lnf.inflate(R.layout.add_time, null);
        final TimePicker timePicker = view.findViewById(R.id.add_time_picker);
        timePicker.setIs24HourView(true);
        builder.setView(view);

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (switchTime.isChecked() && etTime.getText().toString().length() == 0) {
                    // 버튼을 눌렀는데, 취소를 누를시 다시 버튼을 꺼버린다.
                    switchTime.setChecked(false);
                }
            }
        });
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: 확인
                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                if(hour > 12) {
                    am_pm = "PM";
                    hour = hour - 12;
                } else {
                    am_pm="AM";
                }
                etTime.setText(hour + ":" + minute + " " + am_pm);
                switchTime.setChecked(true);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setupLocation() {
        AlertDialog.Builder  builder = new AlertDialog.Builder(getContext());
        LayoutInflater lnf = getLayoutInflater();
        final View view = lnf.inflate(R.layout.add_location, null);
        View titleView = lnf.inflate(R.layout.add_level_title, null);

        final RatingBar ratingBar = view.findViewById(R.id.add_rating);
        builder.setCustomTitle(titleView);
        builder.setView(view);

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (switchLocation.isChecked() && etLocation.getText().toString().length() == 0) {
                    // 버튼을 눌렀는데, 취소를 누를시 다시 버튼을 꺼버린다.
                    switchLocation.setChecked(false);
                }
            }
        });
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: 확인
                float rating = ratingBar.getRating();
//                etLocation.setText(rating + "");
                switchLocation.setChecked(true);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public void setupLevel() {
        AlertDialog.Builder  builder = new AlertDialog.Builder(getContext());
        LayoutInflater lnf = getLayoutInflater();
        final View view = lnf.inflate(R.layout.add_level, null);
        View titleView = lnf.inflate(R.layout.add_level_title, null);
        final RatingBar ratingBar = view.findViewById(R.id.add_rating);
        builder.setCustomTitle(titleView);
        builder.setView(view);

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (switchLevel.isChecked() && etLevel.getText().toString().length() == 0) {
                    // 버튼을 눌렀는데, 취소를 누를시 다시 버튼을 꺼버린다.
                    switchLevel.setChecked(false);
                }
            }
        });
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: 확인
                float rating = ratingBar.getRating();
                etLevel.setText(rating + "");
                switchLevel.setChecked(true);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}