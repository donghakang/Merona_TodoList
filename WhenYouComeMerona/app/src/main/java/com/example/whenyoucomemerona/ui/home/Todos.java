package com.example.whenyoucomemerona.ui.home;

import java.util.Random;

public class Todos {
    public String content;
    public boolean isDone;
    public String date;

    public Todos() {
        this.content = "";
        this.isDone = false;
        this.date = "";
    }

    public Todos(String content, boolean isDone, String date) {
        this.content = content;
        this.isDone = isDone;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
