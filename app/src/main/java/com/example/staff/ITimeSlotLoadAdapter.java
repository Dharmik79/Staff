package com.example.staff;

import java.util.List;

interface ITimeSlotLoadAdapter {
    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotLoadEmpty();

}
