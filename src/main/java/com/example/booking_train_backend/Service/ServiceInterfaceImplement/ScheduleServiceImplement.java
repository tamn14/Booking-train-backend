package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.Request.ScheduleRequest;
import com.example.booking_train_backend.DTO.Response.ScheduleResponse;
import com.example.booking_train_backend.Entity.Schedule;
import com.example.booking_train_backend.Repo.ScheduleRepo;
import com.example.booking_train_backend.Service.ServiceInterface.ScheduleService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import com.example.booking_train_backend.mapper.ScheduleMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service @Transactional
public class ScheduleServiceImplement implements ScheduleService {
    private ScheduleRepo scheduleRepo ;
    private ScheduleMapper scheduleMapper ;
    @Autowired

    public ScheduleServiceImplement(ScheduleRepo scheduleRepo, ScheduleMapper scheduleMapper) {
        this.scheduleRepo = scheduleRepo;
        this.scheduleMapper = scheduleMapper;
    }


    @Override
    public ScheduleResponse add(ScheduleRequest request) {
       // ------------------- KIEM TRA REQUEST ----------------//
        if( scheduleRepo.findByName(request.getName()) != null) {
            throw new AppException(ErrorCode.SCHEDULE_EXISTED) ;
        }
        Schedule scheduleAdd = scheduleRepo.save(scheduleMapper.toEntity(request)) ;
        return scheduleMapper.toDTO(scheduleAdd) ;
    }

    @Override
    public ScheduleResponse update(ScheduleRequest request , int id) {
        Schedule schedule = scheduleRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.SCHEDULE_NOT_EXISTED)) ;
        // cap nhat name schedule
        schedule.setName(request.getName());
        // luu thong tin cap nhat
        scheduleRepo.save(schedule) ;
        return scheduleMapper.toDTO(schedule) ;
    }
}
