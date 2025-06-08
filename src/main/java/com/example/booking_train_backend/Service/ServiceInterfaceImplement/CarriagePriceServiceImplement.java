package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.Request.CarriagePriceRequest;
import com.example.booking_train_backend.DTO.Request.CarriagePriceUpdateRequest;
import com.example.booking_train_backend.DTO.Response.CarriagePriceResponse;
import com.example.booking_train_backend.Entity.CarriageClass;
import com.example.booking_train_backend.Entity.CarriagePrice;
import com.example.booking_train_backend.Entity.CarriagePriceId;
import com.example.booking_train_backend.Entity.Schedule;
import com.example.booking_train_backend.Repo.CarriageClassRepo;
import com.example.booking_train_backend.Repo.CarriagePriceRepo;
import com.example.booking_train_backend.Repo.ScheduleRepo;
import com.example.booking_train_backend.Service.ServiceInterface.CarriagePriceService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import com.example.booking_train_backend.mapper.CarriagePriceMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service @Transactional
public class CarriagePriceServiceImplement implements CarriagePriceService {
    private CarriagePriceRepo carriagePriceRepo ;
    private CarriageClassRepo carriageClassRepo ;
    private ScheduleRepo scheduleRepo ;
    private CarriagePriceMapper carriagePriceMapper ;

    @Autowired
    public CarriagePriceServiceImplement(CarriagePriceRepo carriagePriceRepo,
                                         CarriagePriceMapper carriagePriceMapper) {
        this.carriagePriceRepo = carriagePriceRepo;
        this.carriagePriceMapper = carriagePriceMapper;
    }


    @Override
    public CarriagePriceResponse add(CarriagePriceRequest request) {
        // ----------- KIEM TRA CAC GIA TRI REQUEST ----------------//
        Schedule schedule = scheduleRepo.findByName(request.getScheduleName()) ;
        if(schedule == null) {
            throw new AppException(ErrorCode.SCHEDULE_NOT_EXISTED) ;
        }
        CarriageClass carriageClass = carriageClassRepo.findByName(request.getCarriageClass()) ;
        if( carriageClass == null) {
            throw new AppException(ErrorCode.CARRIAGE_CLASS_NOT_EXISTED) ;
        }

        // chuyen carriage class thanh entity
        CarriagePrice carriagePrice = carriagePriceMapper.toEntity(request) ;
        CarriagePriceId carriagePriceId = carriagePrice.getId() ;
        carriagePriceId.setScheduleId(schedule.getId());
        carriagePriceId.setCarriageClassId(carriageClass.getId());
        carriagePrice.setId(carriagePriceId);
        // Set quan he hai chieu cho Schedule va CarriageClass voi CarriagePrice
        schedule.getCarriagePrices().add(carriagePrice) ;
        carriagePrice.setSchedule(schedule);
        carriageClass.getCarriagePrices().add(carriagePrice);
        carriagePrice.setCarriageClass(carriageClass);

        // luu CarriagePrice xuong ///
        carriagePriceRepo.save(carriagePrice) ;

        return carriagePriceMapper.toDTO(carriagePrice) ;
    }

    @Override
    public CarriagePriceResponse update(CarriagePriceUpdateRequest request , int schedule , int carriageClass) {
        // ----------- KIEM TRA CAC GIA TRI REQUEST ----------------//
        CarriagePriceId carriagePriceId = new CarriagePriceId(schedule , carriageClass);
        CarriagePrice carriagePrice = carriagePriceRepo.findById(carriagePriceId)
                .orElseThrow(()-> new AppException(ErrorCode.CARRIAGE_PRICE_NOT_EXISTED)) ;
        // cap nhat lai price
        carriagePrice.setPrice(request.getPrice());
        // luu cap nhat
        carriagePriceRepo.save(carriagePrice) ;
        return carriagePriceMapper.toDTO(carriagePrice) ;
    }
}
