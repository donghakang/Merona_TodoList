package com.example.whenyoucomemerona.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

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
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.controller.My;
import com.example.whenyoucomemerona.entity.Address;
import com.example.whenyoucomemerona.entity.AddressTodos;
import com.example.whenyoucomemerona.entity.Todos;
import com.example.whenyoucomemerona.lib.MapHelper;
import com.example.whenyoucomemerona.model.Key;
import com.example.whenyoucomemerona.view.CustomBalloonAdapter;
import com.example.whenyoucomemerona.view.SearchLocationAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class MapFragment extends BaseFragment implements View.OnClickListener, MapView.MapViewEventListener, MapView.CurrentLocationEventListener {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;



    FloatingActionButton mSearchButton;
    FloatingActionButton mBtnCurrentLocation;

    MapView mapView;
    ViewGroup mapViewContainer;
    CustomBalloonAdapter customBalloonAdapter;

    FusedLocationProviderClient client;

    ArrayList<AddressTodos> addressTodosArr;

    int TRACKING_MODE;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {



                startLocationService();
                init(v);
            } else {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Fragment mapFragment = new MapFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.body_rl, mapFragment).commit();
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},
                    100);
            Fragment mapFragment = new MapFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.body_rl, mapFragment).commit();
        }


        return v;
    }





    private void init(View v) {
        mSearchButton = v.findViewById(R.id.location_search_activate);
        mBtnCurrentLocation = v.findViewById(R.id.current_location);

        addressTodosArr = new ArrayList<>();
        TRACKING_MODE = 1;

        // 지도
        mapView = new MapView(getActivity());
        mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);


        mapView.setCurrentLocationEventListener(this);
        mapView.setCurrentLocationRadius(5);
        mapView.setCurrentLocationRadiusFillColor(R.color.colorPrimaryLight);

        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        mapView.setZoomLevel(2, false);
        mapView.setShowCurrentLocationMarker(true);
        customBalloonAdapter = new CustomBalloonAdapter(getActivity());
        mapView.setCalloutBalloonAdapter(customBalloonAdapter);

        mapView.setMapViewEventListener(this);
        mSearchButton.setOnClickListener(this);
        mBtnCurrentLocation.setOnClickListener(this);

        getMapViewData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_search_activate:
                locate();
                break;
            case R.id.current_location:
                TRACKING_MODE ++;

                switch (TRACKING_MODE % 3) {
                    case 0:
                        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                        break;
                    case 1:
                        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                        break;
                    case 2:
                        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                        break;

                }
//                getCurrentLocation(client, mapView);
                mapView.setZoomLevel(2, true);
                break;
        }
    }


    // 지도에 표시된 데이터 가져오기
    private void getMapViewData() {
        params.clear();
        params.put("user_id", My.Account.getUser_id() + "");
        params.put("id", My.Account.getId());
        request("getMapData.do");
    }

    @Override
    public void response(String response) {
        try {
            JSONObject j = new JSONObject(response);
            if (j.optString("result").equals("ok")) {
                JSONArray data = j.optJSONArray("data");
                for (int i = 0; i < data.length(); i ++ ){
                    JSONObject item = data.optJSONObject(i);

                    Todos todo = new Todos();
                    todo.setTodo_id(item.optInt("todo_id"));
                    todo.setContent(item.optString("content"));
                    todo.setMemo(item.optString("memo"));
                    todo.setDuedate(item.optString("duedate"));
                    todo.setDuetime(item.optString("duetime"));
                    todo.setShare_with(item.optString("share_with"));
                    todo.setWriter_id(item.optInt("writer_id"));
                    todo.setAddr_id(item.optInt("addr_id"));
                    todo.setDone(item.optBoolean("done"));

                    JSONObject jsonAddress = item.optJSONObject("address");
                    Address address = new Address();
                    address.setAddr_id(jsonAddress.optInt("addr_id"));
                    todo.setAddr_id(jsonAddress.optInt("addr_id"));
                    address.setAddress_name(jsonAddress.optString("address_name"));
                    address.setRoad_address_name(jsonAddress.optString("road_address_name"));
                    address.setPlace_name(jsonAddress.optString("place_name"));
                    address.setCategory_name(jsonAddress.optString("category_name"));
                    address.setLat(jsonAddress.optDouble("lat"));
                    address.setLng(jsonAddress.optDouble("lng"));
                    address.setNotify(jsonAddress.optBoolean("notify"));

                    AddressTodos addressTodos = new AddressTodos(todo, address);

                    addressTodosArr.add(addressTodos);

                    updatePOI();
                }
            } else {
                // data가 없습니다.
                Log.d("JSON TAG", "데이터가 없습니다.");
            }
        } catch (Exception e) {
            Log.d("RESPONSE", "데이터 없습니다.");
        }
    }




    // 데이터를 기반으로 Marker를 삽입한다.
    private void updatePOI() {

        for (AddressTodos content : addressTodosArr) {
            if (!content.getTodos().isDone()) {
                MapPOIItem customMarker = new MapPOIItem();
                customMarker.setTag(content.getTodos().getTodo_id());           // todo id를 태그 넘버로 설정
                JSONObject item = new JSONObject();
                try {
                    item.put("content", content.getTodos().getContent());
                    item.put("due", content.getTodos().getDuedate() + " " + content.getTodos().getDuetime());
                    customMarker.setItemName(item.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MapPoint mp = MapPoint.mapPointWithGeoCoord(content.getAddress().getLat(), content.getAddress().getLng());
                customMarker.setMapPoint(mp);

                customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                customMarker.setCustomImageResourceId(R.mipmap.marker); // 마커 이미지.
                customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

                mapView.addPOIItem(customMarker);
            }

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }



    // 장소 검색
    SearchView searchView;
    ListView listView;
    ArrayList<Address> arr;
    SearchLocationAdapter adapter;



    public void locate() {
        AlertDialog.Builder  builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        LayoutInflater lnf = getLayoutInflater();

        final View view = lnf.inflate(R.layout.dialog_location, null);
        View titleView = lnf.inflate(R.layout.add_level_title, null);
        TextView title = titleView.findViewById(R.id.dialog_title);
        title.setText("장소 검색하기");

        searchView  = view.findViewById(R.id.location_bar);
        listView = view.findViewById(R.id.search_list);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchLocation(newText);
                return false;
            }
        });


        builder.setCustomTitle(titleView);
        builder.setView(view);
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
//        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // list 에 아이템이 클릭 될 시, map view 를 다시 옮긴다.
                Address selectedAddress = arr.get(position);
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(selectedAddress.getLat(), selectedAddress.getLng()), true);
                dialog.cancel();

            }
        });
    }





    private void searchLocation(String newText) {
        if (newText.length() != 0) {

            String url = "https://dapi.kakao.com/v2/local/search/keyword.json" + "?query=" + Uri.encode(newText);
            RequestQueue stringRequest = Volley.newRequestQueue(getContext());
            StringRequest myReq = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONArray documents = json.optJSONArray("documents");
                                Log.d("RESPONSE", "총 " + documents.length() + "의 데이터가 발견되었습니다.");
                                arr.clear();
                                for (int i = 0; i < documents.length(); i ++) {
                                    JSONObject loc = documents.getJSONObject(i);
                                    Address addr = new Address();
                                    addr.setAddress_name(loc.optString("address_name"));
                                    addr.setPlace_name(loc.optString("place_name"));
                                    addr.setRoad_address_name(loc.optString("road_address_name"));
                                    addr.setCategory_name(loc.optString("category_group_name"));
                                    addr.setLat(loc.optDouble("y"));
                                    addr.setLng(loc.optDouble("x"));
                                    arr.add(addr);
                                }
                                adapter = new SearchLocationAdapter(getActivity(), arr);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                Log.d("RESPONSE", "데이터 없습니다.");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "통신이 불가능 합니다.", Toast.LENGTH_SHORT).show();

                            getActivity().finish();
                            System.exit(0);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header_params = new HashMap<String, String>();
                    header_params.put("Authorization", "KakaoAK " + Key.getREST() );
                    return header_params;
                }
            };

            myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
            stringRequest.add(myReq);
        }
    }




    // MapView.
    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }


    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {


    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }
}