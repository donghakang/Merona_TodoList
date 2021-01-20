package com.example.whenyoucomemerona.main;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.entity.Address;
import com.example.whenyoucomemerona.model.Key;
import com.example.whenyoucomemerona.view.SearchLocationAdapter;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MapFragment extends BaseFragment implements View.OnClickListener {

    Button mSearchButton;

    MapView mapView;
    ViewGroup mapViewContainer;


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
        arr = new ArrayList<>();

        //지도
        mapView = new MapView(getActivity());
        mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        mSearchButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.location_search_activate) {
            locate();
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




}