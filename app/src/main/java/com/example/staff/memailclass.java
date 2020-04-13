package com.example.staff;

public class memailclass {

    String Salon_name,City,Password,email,phone_number;

    public memailclass() {
    }

    public memailclass(String salon_name, String city, String password, String email, String phone_number) {
        Salon_name = salon_name;
        City = city;
        Password = password;
        this.email = email;
        this.phone_number = phone_number;

    }

    public String getSalon_name() {
        return Salon_name;
    }

    public void setSalon_name(String salon_name) {
        Salon_name = salon_name;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    }
