package com.example.staff;

import java.util.List;

interface IOnAllStateLoadListner {

    void onAllStateLoadSuccess(List<City> cityList);
    void onAllStateLoadFailed(String m);
}
