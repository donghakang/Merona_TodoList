package com.example.whenyoucomemerona.entity;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.lang.Math.floor;

public class Noti {
    private int noti_id;
    // TODO: User 로 변경
    private int user_id;
    private int friend_id;
    private int type;
    private String pushDate;


    private User user;
    private User friend;

    public Noti() {
    }

    public int getNoti_id() {
        return noti_id;
    }

    public void setNoti_id(int noti_id) {
        this.noti_id = noti_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPushDate() {
        return pushDate;
    }

    public void setPushDate(String pushDate) {
        this.pushDate = pushDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    // Static function
    public String getWelcomeMsg() {
        return "님! 올때, 메로나에 가입하신거를 축하드립니다! 첫 해야 할 일을 올려보세요!";
    }

    public String getFriendReqMsg() {
        return "님이 친구 요청을 보냈습니다.";
    }

    public String getTodoReqMsg() {
        return "님이 해야 할 일을 공유 했습니다.";
    }
    public String getLocationMsg() {
        return "님 근처에 해야 할 일이 있습니다.";
    }

    public String getTimeDiff() {
        // String --> Date
        String DATE_FORMAT = "yyyyMMdd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        Date today = Calendar.getInstance().getTime();
        Log.d("Date Tag", sdf.format(today));

        Date date = new Date();
        try {
            date = sdf.parse(pushDate);
        } catch (ParseException ex) {
            Log.e("Date Tag", ex.getLocalizedMessage());
        }

        long timeDiff = (today.getTime() - date.getTime()) / 1000;

        String result = "";

        if (timeDiff < 60) {
            // 1분 전
            result = (int)timeDiff + "초 전";
        } else if (timeDiff < 60 * 60) {
            // 1시간 전
            result = (int)floor(timeDiff / 60.0) + "분 전";
        } else if (timeDiff < 60 * 60 * 24) {
            // 하루 전
            result = (int)floor(timeDiff / (60.0 * 60.0)) + "시간 전";
        } else if (timeDiff < 60 * 60 * 24 * 7) {
            // 일 전
            result = (int)floor(timeDiff / (60 * 60.0 * 24.0)) + "일 전";
        } else {
            // 주 전
            result = (int)floor(timeDiff / (60.0 * 60.0 * 24.0 * 7.0)) + "주 전";
        }


        return result;
    }
}
