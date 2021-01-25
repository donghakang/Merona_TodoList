package com.example.whenyoucomemerona.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.entity.Address;
import com.example.whenyoucomemerona.model.Key;
import com.example.whenyoucomemerona.view.SearchLocationAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;


public class MapFragment extends BaseFragment implements View.OnClickListener, MapView.MapViewEventListener {

    FloatingActionButton mSearchButton;
    FloatingActionButton mBtnCurrentLocation;

    MapView mapView;
    ViewGroup mapViewContainer;

    FusedLocationProviderClient client;


    Location currLocation;

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

        mSearchButton = v.findViewById(R.id.location_search_activate);
        mBtnCurrentLocation = v.findViewById(R.id.current_location);

        arr = new ArrayList<>();

        //지도
        mapView = new MapView(getActivity());
        mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }


        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
        mapView.setShowCurrentLocationMarker(true);
        mapView.setMapViewEventListener(this);
        mSearchButton.setOnClickListener(this);
        mBtnCurrentLocation.setOnClickListener(this);


        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_search_activate:
                locate();
                break;
            case R.id.current_location:
                getCurrentLocation();
                break;
        }
    }

    private void getCurrentLocation() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        Location location = task.getResult();

                        if (location != null) {
                            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude()), true);
                        } else {
                            LocationRequest locationRequest = new LocationRequest()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(10000)
                                    .setFastestInterval(1000)
                                    .setNumUpdates(1);

                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    Location location1 = locationResult.getLastLocation();

                                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(location1.getLatitude(), location1.getLongitude()), true);
                                }
                            };

                            client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                        }

                    }
                });
            } else {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            getCurrentLocation();
        } else {
            Toast.makeText(getActivity(), "PERMISSION NOT GRANTED", Toast.LENGTH_SHORT).show();
        }
    }

    // ------------
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



    @Override
    public void response(String response) {
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

    private void searchLocation(String newText) {
        if (newText.length() != 0) {
            header_params.clear();
            header_params.put("Authorization", "KakaoAK " + Key.getREST() );
            specificRequest("https://dapi.kakao.com/v2/local/search/keyword.json" + "?query=" + Uri.encode(newText));
        }
    }


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
        Log.d("MAP TAG", mapPoint.getMapPointGeoCoord().latitude + "   " + mapPoint.getMapPointGeoCoord().longitude);
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



}