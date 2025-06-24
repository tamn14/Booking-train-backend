package com.example.booking_train_backend.mapper;

import com.example.booking_train_backend.DTO.Request.JourneyCarriageRequest;
import com.example.booking_train_backend.DTO.Request.JourneyStationRequest;
import com.example.booking_train_backend.DTO.Response.JourneyCarriageResponse;
import com.example.booking_train_backend.DTO.Response.JourneyStationResponse;
import com.example.booking_train_backend.Entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JourneyStationMapper {
//    // mapping from request to entity
////    @Mapping(source = "journeyId", target = "trainStation.id")
//    @Mapping(source = "trainStationId", target = "trainJourney.id")
//    @Mapping(source = "stopOrder", target = "stopOrder")
//    @Mapping(source = "departureTime", target = "departureTime")
//    JourneyStation toEntity (JourneyStationRequest request) ;


    // mapping from entity to DTO
    @Mapping(source = "trainJourney.name", target = "trainJourney")
    @Mapping(source = "trainStation.stationName", target = "trainStation")
    @Mapping(source = "stopOrder", target = "stopOrder")
    @Mapping(source = "departureTime", target = "departureTime")
    JourneyStationResponse toDTO (JourneyStation journeyStation) ;

    // mapping from request to entity
    default JourneyStation toEntity(JourneyStationRequest request,
                                     TrainJourney trainJourney,
                                     TrainStation trainStation) {
        if (request == null || trainJourney == null || trainStation == null) {
            return null;
        }
        return JourneyStation.builder()
                .id(new JourneyStationId(trainJourney.getId() , trainStation.getId()))
                .stopOrder(request.getStopOrder())
                .departureTime(request.getDepartureTime())
                .build() ;
    }
}

