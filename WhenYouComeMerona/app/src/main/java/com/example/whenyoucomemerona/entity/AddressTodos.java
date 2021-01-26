package com.example.whenyoucomemerona.entity;


public class AddressTodos {
    Todos todos;
    Address address;

    public AddressTodos() {
    }

    public AddressTodos(Todos todos, Address address) {
        this.todos = todos;
        this.address = address;
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


}
