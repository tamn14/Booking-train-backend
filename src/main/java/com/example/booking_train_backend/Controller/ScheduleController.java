package com.example.booking_train_backend.Controller;

import com.example.booking_train_backend.DTO.Request.BookingUpdateRequest;
import com.example.booking_train_backend.DTO.Request.ScheduleRequest;
import com.example.booking_train_backend.DTO.Response.ApiResponse;
import com.example.booking_train_backend.DTO.Response.BookingResponse;
import com.example.booking_train_backend.DTO.Response.ScheduleResponse;
import com.example.booking_train_backend.Entity.Schedule;
import com.example.booking_train_backend.Service.ServiceInterface.ScheduleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Schedule")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ScheduleController {
    private ScheduleService scheduleService ;
    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/create")
    public ApiResponse<ScheduleResponse> addSchedule (@RequestBody @Valid ScheduleRequest scheduleRequest ) {
        ScheduleResponse scheduleResponse = scheduleService.add(scheduleRequest) ;
        return ApiResponse.<ScheduleResponse>builder()
                .mess("Success")
                .result(scheduleResponse)
                .build() ;
    }
    @PutMapping("/{id}")
    public ApiResponse<ScheduleResponse> updateSchedule(@Valid @PathVariable int id
            , @RequestBody ScheduleRequest scheduleRequest  ){
        ScheduleResponse scheduleResponse = scheduleService.update(scheduleRequest , id) ;
        return ApiResponse.<ScheduleResponse>builder()
                .mess("Success")
                .result(scheduleResponse)
                .build() ;

    }

}
