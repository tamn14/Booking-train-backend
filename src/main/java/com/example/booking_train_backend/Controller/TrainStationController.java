package com.example.booking_train_backend.Controller;

import com.example.booking_train_backend.DTO.Request.BookingRequest;
import com.example.booking_train_backend.DTO.Request.TrainStationRequest;
import com.example.booking_train_backend.DTO.Response.ApiResponse;
import com.example.booking_train_backend.DTO.Response.BookingResponse;
import com.example.booking_train_backend.DTO.Response.TrainStationResponse;
import com.example.booking_train_backend.Service.ServiceInterface.TrainStationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/TrainStation")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TrainStationController {
    private TrainStationService trainStationService ;
    @Autowired
    public TrainStationController(TrainStationService trainStationService) {
        this.trainStationService = trainStationService;
    }

    @GetMapping
    public ApiResponse<List<TrainStationResponse>> getAllTrainStation() {
        List<TrainStationResponse> trainStationResponses = trainStationService.findAll() ;
        return ApiResponse.<List<TrainStationResponse>>builder()
                .mess("Success")
                .result(trainStationResponses)
                .build();
    }

    @GetMapping("/{name}")
    public ApiResponse<TrainStationResponse> getTrainStationByName(@PathVariable String name) {
        TrainStationResponse trainStationResponse = trainStationService.findByStationName(name);
        return ApiResponse.<TrainStationResponse>builder()
                .mess("Success")
                .result(trainStationResponse)
                .build();
    }
    @PostMapping
    public ApiResponse<TrainStationResponse> addTrainStation(@RequestBody @Valid TrainStationRequest trainStationRequest) {
        TrainStationResponse trainStationResponse = trainStationService.addTrainStation(trainStationRequest) ;
        return ApiResponse.<TrainStationResponse>builder()
                .mess("Success")
                .result(trainStationResponse)
                .build() ;
    }



}
