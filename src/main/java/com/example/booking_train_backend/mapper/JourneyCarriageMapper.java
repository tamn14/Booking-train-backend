package com.example.booking_train_backend.mapper;

import com.example.booking_train_backend.DTO.Request.JourneyCarriageRequest;
import com.example.booking_train_backend.DTO.Response.JourneyCarriageResponse;
import com.example.booking_train_backend.Entity.JourneyCarriage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JourneyCarriageMapper {
    // mapping from request to entity
//    @Mapping(source = "trainJourneyId", target = "trainJourney.id")
    @Mapping(source = "carriageClass", target = "carriageClass.id")
    @Mapping(source = "position", target = "position")
    JourneyCarriage toEntity (JourneyCarriageRequest request) ;


    // mapping from entity to DTO
    @Mapping(source = "trainJourney.id", target = "trainJourneyId")
    @Mapping(source = "carriageClass.name", target = "className")
    @Mapping(source = "position", target = "position")
    JourneyCarriageResponse toDTO (JourneyCarriage journeyCarriage) ;
}
