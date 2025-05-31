package com.example.booking_train_backend.mapper;

import com.example.booking_train_backend.DTO.Request.ScheduleRequest;
import com.example.booking_train_backend.DTO.Request.TrainJourneyRequest;
import com.example.booking_train_backend.DTO.Response.ScheduleResponse;
import com.example.booking_train_backend.DTO.Response.TrainJourneyResponse;
import com.example.booking_train_backend.Entity.Schedule;
import com.example.booking_train_backend.Entity.TrainJourney;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        JourneyStationMapper.class,
        JourneyCarriageMapper.class,
        ScheduleMapper.class
})
public interface TrainJourneyMapper {
    // mapping from request to entity
    @Mapping(source = "schedule", target = "schedule.id")
    @Mapping(source = "journeyStationRequests", target = "journeyStations")
    @Mapping(source = "journeyCarriageRequests", target = "journeyCarriages")
    TrainJourney toEntity(TrainJourneyRequest request);

    // Mapping từ Entity -> Response DTO
    @Mapping(source = "schedule.id", target = "schedule")
    @Mapping(source = "journeyStations", target = "journeyStationRespones")
    @Mapping(source = "journeyCarriages", target = "journeyCarriageResponses")
    TrainJourneyResponse toDTO(TrainJourney trainJourney);
}
