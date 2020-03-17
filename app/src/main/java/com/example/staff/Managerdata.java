package com.example.staff;

public class Managerdata {

    String city;
    String name;
    String add;

    public String getAdd() {
        return add;
    }

    public Managerdata(String city, String name, String add) {
        this.city = city;
        this.name = name;
        this.add = add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public Managerdata() {
    }

    public Managerdata(String city, String name) {
        this.city = city;
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
