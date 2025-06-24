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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service @Transactional
public class CarriagePriceServiceImplement implements CarriagePriceService {
    private CarriagePriceRepo carriagePriceRepo ;
    private CarriageClassRepo carriageClassRepo ;
    private ScheduleRepo scheduleRepo ;
    private CarriagePriceMapper carriagePriceMapper ;
    @Autowired
    public CarriagePriceServiceImplement(CarriagePriceRepo carriagePriceRepo,
                                         CarriageClassRepo carriageClassRepo,
                                         ScheduleRepo scheduleRepo,
                                         CarriagePriceMapper carriagePriceMapper) {
        this.carriagePriceRepo = carriagePriceRepo;
        this.carriageClassRepo = carriageClassRepo;
        this.scheduleRepo = scheduleRepo;
        this.carriagePriceMapper = carriagePriceMapper;
    }

    private Schedule getScheduleByName(String name) {
        Schedule schedule = scheduleRepo.findByName(name);
        if (schedule == null) {
            throw new AppException(ErrorCode.SCHEDULE_NOT_EXISTED);
        }
        return schedule ;
    }

    private Schedule getScheduleById(int id) {
        return scheduleRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_EXISTED)) ;
    }

    private CarriageClass getCarriageClassById(int id) {
        return carriageClassRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CARRIAGE_CLASS_NOT_EXISTED)) ;
    }

    private CarriagePrice getCarriagePriceById(CarriagePriceId id) {
        return carriagePriceRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CARRIAGE_PRICE_NOT_EXISTED)) ;
    }

    private CarriageClass getCarriageClassByName(String name) {
        CarriageClass carriageClass = carriageClassRepo.findByName(name);
        if (carriageClass == null) {
            throw new AppException(ErrorCode.CARRIAGE_CLASS_NOT_EXISTED);
        }
        return carriageClass ;
    }
    private CarriagePriceId buildCarrigaPriceId(int carriageClassId , int scheduleId) {
        CarriagePriceId id = new CarriagePriceId(scheduleId , carriageClassId);
        if (carriagePriceRepo.existsById(id)) {
            throw new AppException(ErrorCode.CARRIAGE_PRICE_ALREADY_EXISTS);
        }
        return id ;
    }

    private void setRelationShip(CarriagePrice carriagePrice , Schedule schedule , CarriageClass carriageClass) {
        carriagePrice.setSchedule(schedule);
        carriagePrice.setCarriageClass(carriageClass);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CarriagePriceResponse addCarriagePrice(CarriagePriceRequest request) {
        Schedule schedule = getScheduleByName(request.getScheduleName()) ;

        CarriageClass carriageClass = getCarriageClassByName(request.getCarriageClass()) ;

        CarriagePriceId id = buildCarrigaPriceId(carriageClass.getId() , schedule.getId()) ;

        CarriagePrice carriagePrice = carriagePriceMapper.toEntity(request, schedule, carriageClass);

        // KHÔNG gọi add() hai chiều để tránh lỗi Duplicate
        setRelationShip(carriagePrice , schedule , carriageClass );

        carriagePriceRepo.save(carriagePrice);

        return carriagePriceMapper.toDTO(carriagePrice);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CarriagePriceResponse updateCarriagePrice(CarriagePriceUpdateRequest request ,int scheduleId , int carriageClassId ) {
        // ----------- KIEM TRA CAC GIA TRI REQUEST ----------------//
        // kiem tra schedule ton tai
        Schedule schedule = getScheduleById(scheduleId) ;

        // kiem tra carriageClass ton tai
        CarriageClass carriageClass = getCarriageClassById(carriageClassId) ;

        CarriagePriceId carriagePriceId = new CarriagePriceId(scheduleId , carriageClassId);
        CarriagePrice carriagePrice = getCarriagePriceById(carriagePriceId) ;
        // cap nhat lai price
        carriagePrice.setPrice(request.getPrice());
        // luu cap nhat
        carriagePriceRepo.save(carriagePrice) ;
        return carriagePriceMapper.toDTO(carriagePrice) ;
    }
}
