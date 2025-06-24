package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Request.BookingRequest;
import com.example.booking_train_backend.DTO.Response.BookingResponse;

import java.util.List;

public interface BookingService {
    public BookingResponse addBooking(BookingRequest request) ;
    public BookingResponse findBooking( int id ) ;
    public void deleteBooking(int id) ;
    public List<BookingResponse> findAll() ;
    public List<BookingResponse> getMyBooking() ;

}
