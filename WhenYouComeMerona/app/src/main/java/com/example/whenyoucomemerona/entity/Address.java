package com.example.whenyoucomemerona.entity;

public class Address {
    private String address_name;
    private String place_name;
    private String road_address_name;
    private String category_name;
    private double lat;         // y
    private double lng;         // x

    public Address() {
        address_name = "";
        place_name ="";
        road_address_name = "";
        category_name ="";
        lat = -1.0;
        lng = -1.0;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getRoad_address_name() {
        return road_address_name;
    }

    public void setRoad_address_name(String road_address_name) {
        this.road_address_name = road_address_name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    @Override
    public String toString() {
        return "Address{" +
                "address_name='" + address_name + '\'' +
                ", place_name='" + place_name + '\'' +
                ", road_address_name='" + road_address_name + '\'' +
                ", category_name='" + category_name + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
