package com.example.whenyoucomemerona.entity;

import java.util.Random;

public class Todos {
    private int todo_id;
    private String content;
    private boolean done;
    private String date;

    public Todos() {
        this.todo_id = 0;
        this.content = "";
        this.done = false;
        this.date = "";
    }

    public Todos(int todo_id, String content, boolean done, String date) {
        this.todo_id = todo_id;
        this.content = content;
        this.done = done;
        this.date = date;
    }

    public int getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(int todo_id) {
        this.todo_id = todo_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Todos{" +
                "content='" + content + '\'' +
                ", done=" + done +
                ", date='" + date + '\'' +
                '}';
    }
}
