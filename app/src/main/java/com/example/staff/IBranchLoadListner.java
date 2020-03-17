package com.example.staff;

import java.util.List;

interface IBranchLoadListner {

    void onAllBranchLoadSuccess(List<Salon> salonList);
    void onAllBranchLoadFailed(String message);
}

