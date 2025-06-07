package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Request.ScheduleRequest;
import com.example.booking_train_backend.DTO.Response.ScheduleResponse;

public interface ScheduleService {
    public ScheduleResponse add(ScheduleRequest request) ;
    public ScheduleResponse update(ScheduleRequest request) ;
}
