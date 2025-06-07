package com.example.booking_train_backend.mapper;


import com.example.booking_train_backend.DTO.Request.TrainStationRequest;
import com.example.booking_train_backend.DTO.Response.TrainStationResponse;
import com.example.booking_train_backend.Entity.TrainStation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainStationMapper {// mapping from request to entity


    @Mapping(source = "name", target = "stationName")
    TrainStation toEntity (TrainStationRequest request) ;


    // mapping from entity to DTO
    @Mapping(source = "stationName", target = "name")
    TrainStationResponse toDTO (TrainStation trainStation) ;
}
