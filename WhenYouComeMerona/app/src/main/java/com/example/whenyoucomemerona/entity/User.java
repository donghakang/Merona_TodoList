package com.example.whenyoucomemerona.entity;

public class User {
    int user_id;
    String id;
    String pw;

    public User() {
    }

    public User(int user_id, String id, String pw) {
        this.user_id = user_id;
        this.id = id;
        this.pw = pw;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", id='" + id + '\'' +
                ", pw='" + pw + '\'' +
                '}';
    }
}
