package com.example.booking_train_backend.mapper;


import com.example.booking_train_backend.DTO.Request.TrainTripRequest;
import com.example.booking_train_backend.DTO.Response.TrainTripResponse;
import com.example.booking_train_backend.Entity.TrainTrip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainTripMapper {
    // mapping from request to entity

    @Mapping(source = "departureDate", target = "departureDate")
    @Mapping(source = "departureTime", target = "departureTime")
    TrainTrip toEntity (TrainTripRequest request) ;


    // mapping from entity to DTO
    @Mapping(source = "departureDate", target = "departureDate")
    @Mapping(source = "departureTime", target = "departureTime")
    TrainTripResponse toDTO (TrainTrip trainTrip) ;
}
