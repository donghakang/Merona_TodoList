package com.example.whenyoucomemerona.controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.whenyoucomemerona.controller.My;
import com.example.whenyoucomemerona.entity.AddressTodos;
import com.example.whenyoucomemerona.lib.MapHelper;
import com.example.whenyoucomemerona.model.Key;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import net.daum.mf.map.api.MapPoint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocationService extends Service {

    public static final int LOCATION_SERVICE_ID = 175;
    public static final String ACTION_START_LOCATION_SERVICE = "startLocationService";
    public static final String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";




    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location1 = locationResult.getLastLocation();

            My.Lat = location1.getLatitude();
            My.Lng = location1.getLongitude();


            ArrayList<Integer> todo_status = new ArrayList<>();
            boolean notify = false;

            Log.d("dddd", My.todos.size() + "");

            for (AddressTodos t : My.todos) {
                Log.d("ddddd", "t: " + t.getAddress().getAddr_id());
                String title = "근처에 해야할 일이 있네요";
                String message = "지도를 보고 지금 바로 해야 할 일을 끝내세!";
                double lat2 = t.getAddress().getLat();
                double lng2 = t.getAddress().getLng();
                double dist = MapHelper.distance(My.Lat, My.Lng, lat2, lng2, "meter");
//                Log.d("dddd", "DISTANCE BETWEEN: " + dist + "m");

                if (dist < 100 ) {
                    if (!t.getAddress().isNotify()) {
                        Log.d("dddddd", "current notify " + t.getAddress().getAddr_id() + " : " + t.getAddress().getAddress_name());
                        todo_status.add(t.getTodos().getTodo_id());     // todo_status에서 투두 아이디를 넣고 나중에 업데이트 해준다.
                        notify = true;
                    }
                    t.getAddress().setNotify(true);
                }

                if (notify) {
                    sendNotification(title, message, todo_status);      // 푸쉬 알림을 보내고 상태를 업데이트한다.
                }

            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet Implemented");
    }

    private void startLocationService() {
        String channel_id = "location_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity (
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                channel_id
        ) ;

        builder.setContentTitle("Location Service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager !=null && notificationManager.getNotificationChannel(channel_id) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channel_id,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("used by location service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }


        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(4000)
                .setFastestInterval(2000);

        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        startForeground(LOCATION_SERVICE_ID, builder.build());
    }

    private void stopLocationService() {
        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if(action.equals(ACTION_START_LOCATION_SERVICE)) {
                    startLocationService();
                } else {
                    stopLocationService();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }





    private void sendNotification(String title, String message, final ArrayList<Integer> update_todos) {
        String FCM_API = "https://fcm.googleapis.com/fcm/send";
        final String serverKey = "key=" + Key.getFirebaseServerKey();
        final String contentType = "application/json";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", title);
            notifcationBody.put("message", message);

            notification.put("to", My.Account.getToken());
            notification.put("data", notifcationBody);
        } catch (Exception e) {
            Log.d("dddd", "onCreate: " + e.getMessage() );
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Notification Tag", "Notification Sent");
                        updateNotifyStatus(update_todos);                               // todo_list의 status 를 바꿔준다.
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }



    private void updateNotifyStatus(ArrayList<Integer> todos) {
        String url = "updateMapNotification.do";

        final JSONArray arr = new JSONArray();
        for (int i : todos) {
            arr.put(i+"");
        }
        RequestQueue stringRequest = Volley.newRequestQueue(getApplicationContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, Key.getUrl() + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.optString("result").equals("ok")) {
                                Log.d("NOTIFICATION TAG", "update 완료.");
                            } else {
                                Log.d("NOTIFICATION TAG", "update 실패.");
                            }
                        } catch (Exception e) {
                            Log.d("JSON PARSING TAG", "json 가져오는데 오류가 있습니다.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("todo_ids", arr.toString());
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }

}
