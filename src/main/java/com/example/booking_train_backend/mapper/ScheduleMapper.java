package com.example.booking_train_backend.mapper;

import com.example.booking_train_backend.DTO.Request.PassengerRequest;
import com.example.booking_train_backend.DTO.Request.ScheduleRequest;
import com.example.booking_train_backend.DTO.Response.PassengerResponse;
import com.example.booking_train_backend.DTO.Response.ScheduleResponse;
import com.example.booking_train_backend.Entity.Passenger;
import com.example.booking_train_backend.Entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    // mapping from request to entity

    @Mapping(source = "name", target = "name")
    Schedule toEntity (ScheduleRequest request) ;


    // mapping from entity to DTO
    @Mapping(source = "name", target = "name")
    ScheduleResponse toDTO (Schedule schedule) ;
}
