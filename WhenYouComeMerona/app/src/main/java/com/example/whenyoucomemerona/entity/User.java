package com.example.whenyoucomemerona.entity;

public class User {
    String id;
    String pw;

    public User() {
    }

    public User(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
