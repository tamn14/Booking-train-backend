package com.example.booking_train_backend.mapper;

import com.example.booking_train_backend.DTO.Request.BookingRequest;
import com.example.booking_train_backend.DTO.Response.BookingResponse;
import com.example.booking_train_backend.Entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    // Mapping from BookingRequest to Booking entity
    @Mapping(source = "passenger", target = "passenger.id")
    @Mapping(source = "bookingStatus", target = "bookingStatus.id")
    @Mapping(source = "trainStationStart", target = "trainStationStart.stationName")
    @Mapping(source = "trainStationEnd", target = "trainStationEnd.stationName")
    @Mapping(source = "trainJourney", target = "trainJourney.id")
    @Mapping(source = "carriageClass", target = "carriageClass.id")
    @Mapping(source = "bookingDate", target = "bookingDate")
    @Mapping(source = "amountPaid", target = "amountPaid")
    @Mapping(source = "ticketNo", target = "ticketNo")
    @Mapping(source = "seatNo", target = "seatNo")
    Booking toEntity(BookingRequest request);

    // Mapping from Booking entity to BookingResponse
    @Mapping(source = "passenger", target = "passenger")
    @Mapping(source = "bookingStatus.name", target = "bookingStatus")
    @Mapping(source = "trainStationStart.stationName", target = "trainStationStart")
    @Mapping(source = "trainStationEnd.stationName", target = "trainStationEnd")
    @Mapping(source = "trainJourney.id", target = "trainJourney")
    @Mapping(source = "carriageClass.id", target = "carriageClass")
    @Mapping(source = "bookingDate", target = "bookingDate")
    @Mapping(source = "amountPaid", target = "amountPaid")
    @Mapping(source = "ticketNo", target = "ticketNo")
    @Mapping(source = "seatNo", target = "seatNo")
    BookingResponse toResponse(Booking booking);
}
