package com.example.booking_train_backend.Controller;

import com.example.booking_train_backend.DTO.Request.BookingRequest;
import com.example.booking_train_backend.DTO.Request.BookingUpdateRequest;
import com.example.booking_train_backend.DTO.Request.CarriagePriceRequest;
import com.example.booking_train_backend.DTO.Request.CarriagePriceUpdateRequest;
import com.example.booking_train_backend.DTO.Response.ApiResponse;
import com.example.booking_train_backend.DTO.Response.BookingResponse;
import com.example.booking_train_backend.DTO.Response.CarriagePriceResponse;
import com.example.booking_train_backend.Service.ServiceInterface.CarriagePriceService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/CarriagePrice")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CarriagePriceController {
    private CarriagePriceService carriagePriceService ;
    @Autowired
    public CarriagePriceController(CarriagePriceService carriagePriceService) {
        this.carriagePriceService = carriagePriceService;
    }
    @PostMapping("/create")
    public ApiResponse<CarriagePriceResponse> addCarriagePrice(@RequestBody @Valid CarriagePriceRequest carriagePriceRequest) {
        CarriagePriceResponse carriagePriceResponse = carriagePriceService.addCarriagePrice(carriagePriceRequest) ;
        return ApiResponse.<CarriagePriceResponse>builder()
                .mess("Success")
                .result(carriagePriceResponse)
                .build() ;
    }
    @PutMapping("/{schedualId}/{carriageClassId}")
    public ApiResponse<CarriagePriceResponse> updateCarriagePrice(@Valid @PathVariable("schedualId") int schedualId ,
                                                            @PathVariable("carriageClassId") int carriageClassId ,
                                                            @RequestBody CarriagePriceUpdateRequest carriagePriceRequest  ){
        CarriagePriceResponse carriagePriceResponse = carriagePriceService.updateCarriagePrice(carriagePriceRequest , schedualId , carriageClassId) ;
        return ApiResponse.<CarriagePriceResponse>builder()
                .mess("Success")
                .result(carriagePriceResponse)
                .build() ;

    }


}
