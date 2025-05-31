package com.example.booking_train_backend.mapper;

import com.example.booking_train_backend.DTO.Request.CarriagePriceRequest;
import com.example.booking_train_backend.DTO.Response.CarriagePriceResponse;
import com.example.booking_train_backend.Entity.CarriageClass;
import com.example.booking_train_backend.Entity.CarriagePrice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = {
        ScheduleMapper.class ,
        CarriageClass.class
})
public interface CarriagePriceMapper {
    // Mapping from Request to Entity
    @Mapping(source = "scheduleId", target = "schedule.id")
    @Mapping(source = "carriageClassId", target = "carriageClass.id")
    @Mapping(source = "price", target = "price")
    CarriagePrice toEntity(CarriagePriceRequest request);

    // mapping from entity to DTO
    @Mapping(source = "schedule", target = "schedule")
    @Mapping(source = "carriageClass", target = "carriageClass")
    @Mapping(source = "price", target = "price")
    CarriagePriceResponse toDTO(CarriagePrice carriagePrice);
}
