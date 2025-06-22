package com.example.booking_train_backend.Controller;

import com.example.booking_train_backend.DTO.Request.JourneyStationRequest;
import com.example.booking_train_backend.DTO.Request.JourneyStationUpdateRequest;
import com.example.booking_train_backend.DTO.Request.TrainJourneyRequest;
import com.example.booking_train_backend.DTO.Request.TrainJourneyUpdateRequest;
import com.example.booking_train_backend.DTO.Response.ApiResponse;
import com.example.booking_train_backend.DTO.Response.TrainJourneyResponse;
import com.example.booking_train_backend.Service.ServiceInterface.JourneyStationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/journeyStation")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JourneyStationController {
    private final JourneyStationService journeyStationService ;
    @Autowired
    public JourneyStationController(JourneyStationService journeyStationService) {
        this.journeyStationService = journeyStationService;
    }

    @PostMapping("/{id}")
    public ApiResponse<TrainJourneyResponse> addJourneyStation(@PathVariable("id") int id ,
                                                               @RequestBody @Valid JourneyStationRequest request) {
        TrainJourneyResponse trainJourneyResponse = journeyStationService.addJourneyStation(request , id);
        return ApiResponse.<TrainJourneyResponse>builder()
                .mess("Success")
                .result(trainJourneyResponse)
                .build() ;
    }
    @PutMapping("/create/{id}")
    public ApiResponse<TrainJourneyResponse> updateJourneyStation(@PathVariable("id") int id ,
                                                                @RequestBody @Valid JourneyStationUpdateRequest request){
        TrainJourneyResponse trainJourneyResponse = journeyStationService.updateJourneyStation(request , id) ;
        return ApiResponse.<TrainJourneyResponse>builder()
                .mess("Success")
                .result(trainJourneyResponse)
                .build() ;

    }

    @DeleteMapping("/{trainJourney}/{trainStation}")
    public ApiResponse<Void> updateJourneyStation(@PathVariable("trainJourney") int trainJourney ,
                                                      @PathVariable("trainStation") int trainStation){
        journeyStationService.deleteJourneyStation(trainJourney , trainStation); ;
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build() ;

    }

}
