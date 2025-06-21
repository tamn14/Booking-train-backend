package com.example.booking_train_backend.Controller;

import com.example.booking_train_backend.DTO.Request.BookingRequest;
import com.example.booking_train_backend.DTO.Request.BookingUpdateRequest;
import com.example.booking_train_backend.DTO.Request.TrainJourneyRequest;
import com.example.booking_train_backend.DTO.Request.TrainJourneyUpdateRequest;
import com.example.booking_train_backend.DTO.Response.ApiResponse;
import com.example.booking_train_backend.DTO.Response.BookingResponse;
import com.example.booking_train_backend.DTO.Response.TrainJourneyResponse;
import com.example.booking_train_backend.Service.ServiceInterface.TrainJourneyService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/TrainJourney")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TrainJourneyController {
    private TrainJourneyService trainJourneyService ;
    @Autowired
    public TrainJourneyController(TrainJourneyService trainJourneyService) {
        this.trainJourneyService = trainJourneyService;
    }
    @GetMapping("/name/{name}")
    public ApiResponse<List<TrainJourneyResponse>> getTrainJourneyByName(@PathVariable String name) {
        List<TrainJourneyResponse> trainJourneyResponses = trainJourneyService.findByName(name) ;
        return ApiResponse.<List<TrainJourneyResponse>>builder()
                .mess("Success")
                .result(trainJourneyResponses)
                .build();
    }

    @GetMapping("/id/{id}")
    public ApiResponse<TrainJourneyResponse> getTrainJourneyById(@PathVariable int id) {
        TrainJourneyResponse trainJourneyResponse = trainJourneyService.findById(id) ;
        return ApiResponse.<TrainJourneyResponse>builder()
                .mess("Success")
                .result(trainJourneyResponse)
                .build() ;
    }

    @PostMapping
    public ApiResponse<TrainJourneyResponse> addTrainJourney(@RequestBody @Valid TrainJourneyRequest trainJourneyRequest) {
        TrainJourneyResponse trainJourneyResponse = trainJourneyService.addTrainJourney(trainJourneyRequest);
        return ApiResponse.<TrainJourneyResponse>builder()
                .mess("Success")
                .result(trainJourneyResponse)
                .build() ;
    }
    @PutMapping("/{id}")
    public ApiResponse<TrainJourneyResponse> updateTrainJourney(@Valid @PathVariable int id
            , @RequestBody TrainJourneyUpdateRequest trainJourneyUpdateRequest  ){
        TrainJourneyResponse trainJourneyResponse = trainJourneyService.updateTrainJourney(trainJourneyUpdateRequest , id) ;
        return ApiResponse.<TrainJourneyResponse>builder()
                .mess("Success")
                .result(trainJourneyResponse)
                .build() ;

    }


}
