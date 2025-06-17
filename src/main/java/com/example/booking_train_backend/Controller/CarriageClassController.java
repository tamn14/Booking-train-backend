package com.example.booking_train_backend.Controller;

import com.example.booking_train_backend.DTO.Request.BookingRequest;
import com.example.booking_train_backend.DTO.Request.BookingUpdateRequest;
import com.example.booking_train_backend.DTO.Request.CarriageClassRequest;
import com.example.booking_train_backend.DTO.Response.ApiResponse;
import com.example.booking_train_backend.DTO.Response.BookingResponse;
import com.example.booking_train_backend.DTO.Response.CarriageClassResponse;
import com.example.booking_train_backend.Service.ServiceInterface.CarriageClassService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/CarriageClass")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CarriageClassController {
    private CarriageClassService carriageClassService ;
    @Autowired
    public CarriageClassController(CarriageClassService carriageClassService) {
        this.carriageClassService = carriageClassService;
    }

    @GetMapping()
    public ApiResponse<List<CarriageClassResponse>> getAllCarriageClass() {
        List<CarriageClassResponse> carriageClassResponses = carriageClassService.findAll() ;
        return ApiResponse.<List<CarriageClassResponse>>builder()
                .mess("Success")
                .result(carriageClassResponses)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CarriageClassResponse> updateBooking(@Valid @PathVariable int id
            , @RequestBody CarriageClassRequest carriageClassRequest  ){
        CarriageClassResponse carriageClassResponse = carriageClassService.updateCarriageClass(carriageClassRequest , id) ;
        return ApiResponse.<CarriageClassResponse>builder()
                .mess("Success")
                .result(carriageClassResponse)
                .build() ;

    }

    @PostMapping
    public ApiResponse<CarriageClassResponse> addBooking(@RequestBody @Valid CarriageClassRequest carriageClassRequest) {
        CarriageClassResponse carriageClassResponse = carriageClassService.add(carriageClassRequest) ;
        return ApiResponse.<CarriageClassResponse>builder()
                .mess("Success")
                .result(carriageClassResponse)
                .build() ;
    }


}
