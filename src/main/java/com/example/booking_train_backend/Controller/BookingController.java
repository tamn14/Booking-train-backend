package com.example.booking_train_backend.Controller;

import com.example.booking_train_backend.DTO.Request.BookingRequest;
import com.example.booking_train_backend.DTO.Request.BookingUpdateRequest;
import com.example.booking_train_backend.DTO.Response.ApiResponse;
import com.example.booking_train_backend.DTO.Response.BookingResponse;
import com.example.booking_train_backend.Service.ServiceInterface.BookingService;
import com.example.booking_train_backend.Service.ServiceInterfaceImplement.BookingServiceImplement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookingController {
    private BookingService bookingService ;
    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ApiResponse<List<BookingResponse>> getAllBooking() {
        List<BookingResponse> bookingResponses = bookingService.findAll() ;
        return ApiResponse.<List<BookingResponse>>builder()
                .mess("Success")
                .result(bookingResponses)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BookingResponse> getBookingById(@PathVariable("id") int id) {
        BookingResponse bookingResponse = bookingService.findBooking(id) ;
        return ApiResponse.<BookingResponse>builder()
                .mess("Success")
                .result(bookingResponse)
                .build() ;
    }

    @GetMapping("/myBooking")
    public ApiResponse<List<BookingResponse>> getMyBooking() {
        List<BookingResponse> bookingResponses = bookingService.getMyBooking() ;
        return ApiResponse.<List<BookingResponse>>builder()
                .mess("Success")
                .result(bookingService.findAll())
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<BookingResponse> addBooking(@RequestBody @Valid BookingRequest bookingCreateRequest) {
        BookingResponse bookingResponse = bookingService.addBooking(bookingCreateRequest) ;
        return ApiResponse.<BookingResponse>builder()
                .mess("Success")
                .result(bookingResponse)
                .build() ;
    }
    @PutMapping("/{id}")
    public ApiResponse<BookingResponse> updateBooking(@Valid @PathVariable int id
            , @RequestBody BookingUpdateRequest bookingCreateRequest  ){
        BookingResponse bookingResponse = bookingService.updateBooking(bookingCreateRequest , id) ;
        return ApiResponse.<BookingResponse>builder()
                .mess("Success")
                .result(bookingResponse)
                .build() ;

    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBooking(@PathVariable int id ) {
        bookingService.deleteBooking(id);
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build() ;
    }










}
