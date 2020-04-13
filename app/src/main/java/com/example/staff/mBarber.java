package com.example.staff;

public class mBarber {

    private String name,password,username;
    private Long Rating;

    public mBarber() {
    }

    public mBarber(String name, String password, String username, Long rating) {
        this.name = name;
        this.password = password;
        this.username = username;
        Rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getRating() {
        return Rating;
    }

    public void setRating(Long rating) {
        Rating = rating;
    }
}
