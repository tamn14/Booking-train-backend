package com.example.booking_train_backend.mapper;

import com.example.booking_train_backend.DTO.Request.BookingRequest;
import com.example.booking_train_backend.DTO.Request.CarriageClassRequest;
import com.example.booking_train_backend.DTO.Response.CarriageClassResponse;
import com.example.booking_train_backend.Entity.BookingStatus;
import com.example.booking_train_backend.Entity.CarriageClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarriageClassMapper {
    // Mapping from Request to Entity
    @Mapping(source = "className", target = "name")
    @Mapping(source = "seatingCapacity", target = "seatingCapacity")
    CarriageClass toEntity(CarriageClassRequest request);
    // Mapping from entity to DTO
    @Mapping(source = "name", target = "className")
    @Mapping(source = "seatingCapacity", target = "seatingCapacity")
    CarriageClassResponse toEntity(CarriageClass request);
}
