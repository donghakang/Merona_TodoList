package com.example.whenyoucomemerona.main;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.controller.My;
import com.example.whenyoucomemerona.entity.User;
import com.example.whenyoucomemerona.model.Key;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class AddFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener {

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
            if (isChecked) {
                setupShare();
            } else {
                etShare.setText("");
            }
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
        AlertDialog.Builder  builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
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
        AlertDialog.Builder  builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
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



    // 공유 설정 ----------------------------------------------------------------
    SearchFriendAdapter adapter;
    ArrayList<User> arr;
    ListView shareSearchList;
    EditText etSearch;
    Button btnSearchClear;
    Button btnSearchSubmit;
    ChipGroup chipGroup;

    public void setupShare() {
        AlertDialog.Builder  builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        LayoutInflater lnf = getLayoutInflater();
        final View view = lnf.inflate(R.layout.add_share, null);
        View titleView = lnf.inflate(R.layout.add_level_title, null);
        TextView title = titleView.findViewById(R.id.dialog_title);
        title.setText("공유 설정");

        etSearch = view.findViewById(R.id.et_search);
        btnSearchClear = view.findViewById(R.id.btn_search_clear);
        btnSearchSubmit = view.findViewById(R.id.btn_search_submit);
        shareSearchList = view.findViewById(R.id.share_search_list);
        chipGroup = view.findViewById(R.id.quick_add_chips);
        arr = new ArrayList<>();

        btnSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
            }
        });

        btnSearchSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etSearch.getText().toString();
                searchShareList(username);
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String username = etSearch.getText().toString();
                searchShareList(username);

                return false;
            }
        });

        shareSearchList.setOnItemClickListener(this);


        builder.setCustomTitle(titleView);
        builder.setView(view);
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (switchShare.isChecked() && etShare.getText().toString().length() == 0) {
                    // 버튼을 눌렀는데, 취소를 누를시 다시 버튼을 꺼버린다.
                    switchShare.setChecked(false);
                }
            }
        });
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: 확인
                String outShare = "";
                Chip firstChip = (Chip)chipGroup.getChildAt(0);
                if (chipGroup.getChildCount() == 0) {
                    switchShare.setChecked(false);
                } else if (chipGroup.getChildCount() == 1) {
                    outShare = firstChip.getText() + "";
                    switchShare.setChecked(true);
                } else {
                    outShare = firstChip.getText() + " 외 " + chipGroup.getChildCount() + "명";
                    switchShare.setChecked(true);
                }

                etShare.setText(outShare);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void searchShareList(final String username) {
        String url = "searchFriend.do";
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // success listener
                try {
                    JSONObject j = new JSONObject(response);
                    // 데이터 가져오기 성공할 때,
                    if (j.optString("result").equals("ok")) {
                        arr.clear();                    // 데이터를 가져오기전 정리한다.
                        JSONArray data = j.optJSONArray("friend");
                        for (int i = 0; i < data.length(); i ++ ){

                            JSONObject userObject = data.getJSONObject(i);
                            int user_id = userObject.getInt("user_id");
                            String id = userObject.getString("id");
                            String name = userObject.getString("name");
                            String email = userObject.getString("email");
                            String birth = userObject.getString("birth");
                            String token = userObject.getString("token");
                            if (My.Account.getUser_id() != user_id) {
                                User user = new User();
                                user.setUser_id(user_id);
                                user.setId(id);
                                user.setName(name);
                                user.setEmail(email);
                                user.setBirth(birth);
                                user.setToken(token);

                                arr.add(user);
                            } else {
                                // 자신일 경우
                            }

                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

                            adapter = new SearchFriendAdapter(getActivity(), arr);
                            shareSearchList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "찾기 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "찾기 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("eeeee", "JSON에서 에러가 있습니다.");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error listener
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }
        };


        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User shareUser = arr.get(position);
        boolean isInChipGroup = false;

        final Chip chip = new Chip(getContext());
        chip.setChipIconVisible(true);
        chip.setCloseIconVisible(true);
        chip.setText(shareUser.getId());

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipGroup.removeView(chip);
            }
        });

        for (int i = 0; i < chipGroup.getChildCount(); i ++ ) {
            Chip c = (Chip)chipGroup.getChildAt(i);
            if (c.getText().toString().equals(shareUser.getId())) {
                isInChipGroup = true;
            }
        }
        if (!isInChipGroup) {
            chipGroup.addView(chip, chipGroup.getChildCount() - 1);
        }

        Log.d("dddd", chipGroup.getChildCount() + "" );
    }



    // 장소 설정 ----------------------------------------------------------------
    public void setupLocation() {
        AlertDialog.Builder  builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        LayoutInflater lnf = getLayoutInflater();
        final View view = lnf.inflate(R.layout.add_location, null);
        View titleView = lnf.inflate(R.layout.add_level_title, null);
        TextView title = titleView.findViewById(R.id.dialog_title);
        title.setText("장소 설정");

        MapView mapView = new MapView(getActivity());
        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.add_location_view);
        mapViewContainer.addView(mapView);

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
        AlertDialog.Builder  builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        LayoutInflater lnf = getLayoutInflater();
        final View view = lnf.inflate(R.layout.add_level, null);
        View titleView = lnf.inflate(R.layout.add_level_title, null);
        TextView title = titleView.findViewById(R.id.dialog_title);
        title.setText("중요도 설정");
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