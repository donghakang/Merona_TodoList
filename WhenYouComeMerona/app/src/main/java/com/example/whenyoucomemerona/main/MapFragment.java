package com.example.whenyoucomemerona.main;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

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
            searchPlace = etSearch.getText().toString();
            params.clear();
            params.put("location", searchPlace);
            request("searchLocation.do");
        }
    }

    @Override
    public void response(String response) {
        Log.d("dddd", "되긴 되겟지");
    }
}