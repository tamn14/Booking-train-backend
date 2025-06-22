package com.example.booking_train_backend.Controller;

import com.example.booking_train_backend.DTO.Request.JourneyCarriageRequest;
import com.example.booking_train_backend.DTO.Request.JourneyCarriageUpdateRequest;
import com.example.booking_train_backend.DTO.Request.TrainTripRequest;
import com.example.booking_train_backend.DTO.Request.TrainTripUpdateRequest;
import com.example.booking_train_backend.DTO.Response.ApiResponse;
import com.example.booking_train_backend.DTO.Response.TrainJourneyResponse;
import com.example.booking_train_backend.Service.ServiceInterface.TrainTripService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainTrip")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TrainTripController {
    private final TrainTripService trainTripService ;
    @Autowired
    public TrainTripController(TrainTripService trainTripService) {
        this.trainTripService = trainTripService;
    }
    @PostMapping("create/{id}")
    public ApiResponse<TrainJourneyResponse> addTrainTrip(@PathVariable("id") int id ,
                                                               @RequestBody @Valid TrainTripRequest request) {
        TrainJourneyResponse trainJourneyResponse = trainTripService.addTrainTrip(request , id);
        return ApiResponse.<TrainJourneyResponse>builder()
                .mess("Success")
                .result(trainJourneyResponse)
                .build() ;
    }
    @PutMapping("/{trainJourney}/{trainTrip}")
    public ApiResponse<TrainJourneyResponse> updateTrainTrip(@PathVariable("trainJourney") int trainJourney ,
                                                                  @PathVariable("trainTrip") int trainTrip ,
                                                                  @RequestBody @Valid TrainTripUpdateRequest request){
        TrainJourneyResponse trainJourneyResponse = trainTripService.updateTrainTrip(request , trainTrip , trainJourney) ;
        return ApiResponse.<TrainJourneyResponse>builder()
                .mess("Success")
                .result(trainJourneyResponse)
                .build() ;

    }

    @DeleteMapping("/{trainJourney}/{trainTrip}")
    public ApiResponse<Void> deleteTrainTrip(@PathVariable("trainJourney") int trainJourney ,
                                                  @PathVariable("trainTrip") int trainTrip){
        trainTripService.deleteTrainTrip(trainJourney , trainTrip); ;
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build() ;

    }
}
