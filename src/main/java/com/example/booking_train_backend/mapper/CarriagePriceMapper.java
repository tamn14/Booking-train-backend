package com.example.booking_train_backend.mapper;

import com.example.booking_train_backend.DTO.Request.CarriagePriceRequest;
import com.example.booking_train_backend.DTO.Response.CarriagePriceResponse;
import com.example.booking_train_backend.Entity.CarriageClass;
import com.example.booking_train_backend.Entity.CarriagePrice;
import com.example.booking_train_backend.Entity.CarriagePriceId;
import com.example.booking_train_backend.Entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" )
public interface CarriagePriceMapper {


    // mapping from entity to DTO
    @Mapping(source = "schedule", target = "schedule")
    @Mapping(source = "carriageClass", target = "carriageClass")
    @Mapping(source = "price", target = "price")
    CarriagePriceResponse toDTO(CarriagePrice carriagePrice);

    // mapping from DTO  to entity
    default CarriagePrice toEntity(CarriagePriceRequest request, Schedule schedule, CarriageClass carriageClass) {
        if (request == null || schedule == null || carriageClass == null) {
            return null;
        }
        return CarriagePrice.builder()
                .id(new CarriagePriceId(schedule.getId(), carriageClass.getId()))
                .schedule(schedule)
                .carriageClass(carriageClass)
                .price(request.getPrice())
                .build();
    }
}
