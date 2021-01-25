package com.example.whenyoucomemerona.controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.entity.User;
import com.example.whenyoucomemerona.model.Key;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class BaseFragment extends Fragment {
    public Map<String, String> params = new HashMap<String, String>();
    public Map<String, String> header_params = new HashMap<String, String>();
    public ProgressBar progressBar;
    // Progress Bar

    public void loadStart() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void loadEnd() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    // 통신
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // 통신이 안되는 메시지를 띄운다.
            Toast.makeText(getContext(), "통신이 불가능 합니다.", Toast.LENGTH_SHORT).show();

            getActivity().finish();
            System.exit(0);
        }
    };

    Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            response(response);
        }
    };

    public void response(String response) { }

    public void request(String url) {
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url,
                successListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }


    public void specificRequest(String url) {
        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.GET, url,
                successListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return header_params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }


    public void checkSession() {
        SharedPreferences auto = getActivity().getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String auto_id = auto.getString("auto_id",null);

        if (auto_id == null) {
            // TODO: Session 이 만료 되었습니다.

        } else {

        }
    }


    public void sendNotification(String token, String title, String message) {
        String FCM_API = "https://fcm.googleapis.com/fcm/send";
        final String serverKey = "key=" + Key.getFirebaseServerKey();
        final String contentType = "application/json";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", title);
            notifcationBody.put("message", message);

            notification.put("to", token);
            notification.put("data", notifcationBody);
        } catch (Exception e) {
            Log.d("dddd", "onCreate: " + e.getMessage() );
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Notification Tag", "Notification Sent");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.d("Notification Tag", "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }



    // Noti Table에 업데이트 시킨다
    public void updateNotification(final int type, final User user, final User friend) {
        String url = "insertNoti.do";

        String DATE_FORMAT = "yyyyMMdd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        Date today = Calendar.getInstance().getTime();
        final String pushDate = sdf.format(today);

        Log.d("dddd", "FINAL: " + friend.getUser_id());

        RequestQueue stringRequest = Volley.newRequestQueue(getContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response Tag", "통신 실패");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> notiParams = new HashMap<String, String>();
                notiParams.put("type", type + "");
                notiParams.put("user_id", user.getUser_id() + "");
                notiParams.put("friend_id", friend.getUser_id() + "");
                notiParams.put("pushDate", pushDate);
                return notiParams;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }




    // 위치 기반을 위한 서비스
    private static final String LOG_TAG = "MainActivity";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                int chk = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (chk == PackageManager.PERMISSION_DENIED) {
                    // 익스터널 스토리지에 저장을 받았는가 안받았는가 물어봄
                    String[] arr = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                    // GPS

                    ActivityCompat.requestPermissions(getActivity(), arr, 3000);
                }

                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();


    }


    public void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED ) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(getContext(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

}
