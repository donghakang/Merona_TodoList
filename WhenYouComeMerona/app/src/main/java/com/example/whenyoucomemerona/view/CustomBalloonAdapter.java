package com.example.whenyoucomemerona.view;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.whenyoucomemerona.R;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomBalloonAdapter implements CalloutBalloonAdapter {

    private final View callOutBalloon;

    public CustomBalloonAdapter(Activity ctx) {
        Log.d("dddd", "callout started");
        this.callOutBalloon = ctx.getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        Log.d("dddd", "callout started success");
    }

    @Override
    public View getCalloutBalloon(MapPOIItem mapPOIItem) {
        JSONObject json =  null;
        String content = "";
        String due = "";
        try {
            json = new JSONObject(mapPOIItem.getItemName());
            content = json.optString("content");
            due = json.optString("due");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((TextView) callOutBalloon.findViewById(R.id.balloon_title)).setText(content);
        ((TextView) callOutBalloon.findViewById(R.id.balloon_due)).setText(due);
        return callOutBalloon;
    }

    @Override
    public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
        Log.d("ddd", "Helllo");
        return callOutBalloon;
    }
}
