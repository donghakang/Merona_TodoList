package com.example.whenyoucomemerona.main;

import android.net.Uri;
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
import com.example.whenyoucomemerona.model.Key;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class MapFragment extends BaseFragment implements View.OnClickListener {

    EditText etSearch;
    Button btnCancel;
    Button btnSubmit;
    String searchPlace;

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
        etSearch = v.findViewById(R.id.et_search);
        btnCancel = v.findViewById(R.id.btn_search_clear);
        btnSubmit = v.findViewById(R.id.btn_search_submit);

        //지도
        mapView = new MapView(getActivity());
        mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        etSearch.bringToFront();
        btnCancel.bringToFront();
        btnSubmit.bringToFront();

        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search_clear) {
            etSearch.setText("");
        } else if (v.getId() == R.id.btn_search_submit) {
            Log.d("dddd", "button Pressed!");
            searchPlace = etSearch.getText().toString();

//            params.clear();
//            params.put("location", searchPlace);
//            request("searchLocation.do");
//

            header_params.clear();
            header_params.put("Authorization", "KakaoAK " + Key.getREST() );

            specificRequest("https://dapi.kakao.com/v2/local/search/keyword.json" + "?query=" + Uri.encode(searchPlace));
        }
    }

    @Override
    public void response(String response) {
        Log.d("RESPONSE", response);
    }

    //    @Override
//    public void response(String response) {
//        Log.d("ddddXX response", response);
//        try {
//            JSONObject j = new JSONObject(response);
//            // 데이터 가져오기 성공할 때,
//            if (j.optString("result").equals("ok")) {
//                if (j.optInt("description") == 0) {
//                    Toast.makeText(getContext(), "검색 결과가 없습니다.", Toast.LENGTH_LONG).show();
//                } else {
//                    JSONArray locations = j.optJSONArray("locations");
//                    for (int i = 0; i < locations.length(); i ++) {
//                        JSONObject location = locations.optJSONObject(i);
//                        double lat = location.optDouble("lat");
//                        double lng = location.optDouble("lng");
//                        String place_name = location.optString("place_name");
//                        String road_address_name = location.optString("road_address_name");
//                        String address_name = location.optString("address_name");
//
//                        Log.d("dddd", lat + "    " + lng + "   " + place_name);
//
//                        // TODO: do something with data
//                    }
//                }
//
//                Toast.makeText(getContext(), "위치 검색 성공", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getContext(), "위치 검색 실패", Toast.LENGTH_SHORT).show();
//            }
//        } catch (JSONException e) {
//            Log.d("JSON ERROR", "JSON에서 에러가 있습니다.");
//            e.printStackTrace();
//        }
//    }
}