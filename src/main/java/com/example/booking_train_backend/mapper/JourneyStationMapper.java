package com.example.booking_train_backend.mapper;

import com.example.booking_train_backend.DTO.Request.JourneyCarriageRequest;
import com.example.booking_train_backend.DTO.Request.JourneyStationRequest;
import com.example.booking_train_backend.DTO.Response.JourneyCarriageResponse;
import com.example.booking_train_backend.DTO.Response.JourneyStationResponse;
import com.example.booking_train_backend.Entity.JourneyCarriage;
import com.example.booking_train_backend.Entity.JourneyStation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JourneyStationMapper {
    // mapping from request to entity
    @Mapping(source = "journeyId", target = "trainStation.id")
    @Mapping(source = "stationId", target = "trainJourney.id")
    @Mapping(source = "stopOrder", target = "stopOrder")
    @Mapping(source = "departureTime", target = "departureTime")
    JourneyStation toEntity (JourneyStationRequest request) ;


    // mapping from entity to DTO
    @Mapping(source = "trainStation.stationName", target = "trainJourney")
    @Mapping(source = "trainJourney.name", target = "trainStation")
    @Mapping(source = "stopOrder", target = "stopOrder")
    @Mapping(source = "departureTime", target = "departureTime")
    JourneyStationResponse toDTO (JourneyStation journeyStation) ;
}
