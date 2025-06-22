package com.example.booking_train_backend.Controller;

import com.example.booking_train_backend.DTO.Request.JourneyCarriageRequest;
import com.example.booking_train_backend.DTO.Request.JourneyCarriageUpdateRequest;
import com.example.booking_train_backend.DTO.Request.JourneyStationRequest;
import com.example.booking_train_backend.DTO.Request.JourneyStationUpdateRequest;
import com.example.booking_train_backend.DTO.Response.ApiResponse;
import com.example.booking_train_backend.DTO.Response.TrainJourneyResponse;
import com.example.booking_train_backend.Service.ServiceInterface.JourneyCarriageService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/journeyCarriage")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JourneyCarriageController {
    private final JourneyCarriageService journeyCarriageService ;
    @Autowired
    public JourneyCarriageController(JourneyCarriageService journeyCarriageService) {
        this.journeyCarriageService = journeyCarriageService;
    }

    @PostMapping("/create/{id}")
    public ApiResponse<TrainJourneyResponse> addJourneyCarriage(@PathVariable("id") int id ,
                                                               @RequestBody @Valid JourneyCarriageRequest request) {
        TrainJourneyResponse trainJourneyResponse = journeyCarriageService.addJourneyCarriage(request , id);
        return ApiResponse.<TrainJourneyResponse>builder()
                .mess("Success")
                .result(trainJourneyResponse)
                .build() ;
    }
    @PutMapping("/{id}")
    public ApiResponse<TrainJourneyResponse> updateJourneyCarriage(@PathVariable("id") int id ,
                                                                  @RequestBody @Valid JourneyCarriageUpdateRequest request){
        TrainJourneyResponse trainJourneyResponse = journeyCarriageService.updateJourneyCarriage(request , id) ;
        return ApiResponse.<TrainJourneyResponse>builder()
                .mess("Success")
                .result(trainJourneyResponse)
                .build() ;

    }

    @DeleteMapping("/{trainJourney}/{carriageClassId}")
    public ApiResponse<Void> updateJourneyCarriage(@PathVariable("trainJourney") int trainJourney ,
                                                  @PathVariable("carriageClassId") int carriageClassId){
        journeyCarriageService.deleteJourneyCarriage(trainJourney , carriageClassId); ;
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build() ;

    }
}
