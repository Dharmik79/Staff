package com.example.staff;

import java.util.List;

interface ITimeSlotLoadListner {
    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotLoadEmpty();

}
