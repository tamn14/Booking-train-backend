package com.example.booking_train_backend.mapper;

import com.example.booking_train_backend.DTO.Request.PassengerRequest;
import com.example.booking_train_backend.DTO.Response.PassengerResponse;
import com.example.booking_train_backend.Entity.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PassengerMapper {
    // mapping from request to entity
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    Passenger toEntity (PassengerRequest request) ;


    // mapping from entity to DTO
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    PassengerResponse toDTO (Passenger passenger) ;
}
