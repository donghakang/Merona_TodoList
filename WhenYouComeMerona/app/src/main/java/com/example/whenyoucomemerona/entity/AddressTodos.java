package com.example.whenyoucomemerona.entity;


public class AddressTodos implements Comparable{
    Todos todos;
    Address address;
    boolean isShared;

    public AddressTodos() {
    }

    public AddressTodos(Todos todos, Address address, boolean isShared) {
        this.todos = todos;
        this.address = address;
        this.isShared = isShared;
    }


    public Todos getTodos() {
        return todos;
    }

    public void setTodos(Todos todos) {
        this.todos = todos;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    @Override
    public int compareTo(Object t) {
        int compareTodoId = ((AddressTodos)t).getTodos().getTodo_id();
        /* For Ascending order*/
        return this.getTodos().getTodo_id() - compareTodoId;
    }
}
