package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.Request.CarriageClassRequest;
import com.example.booking_train_backend.DTO.Response.CarriageClassResponse;
import com.example.booking_train_backend.Entity.CarriageClass;
import com.example.booking_train_backend.Repo.CarriageClassRepo;
import com.example.booking_train_backend.Service.ServiceInterface.CarriageClassService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import com.example.booking_train_backend.mapper.CarriageClassMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional

public class CarriageClassServiceImplement implements CarriageClassService {
    private CarriageClassRepo  carriageClassRepo ;
    private CarriageClassMapper carriageClassMapper ;

    @Autowired

    public CarriageClassServiceImplement(CarriageClassRepo carriageClassRepo,
                                         CarriageClassMapper carriageClassMapper) {
        this.carriageClassRepo = carriageClassRepo;
        this.carriageClassMapper = carriageClassMapper;
    }


    @Override
    public CarriageClassResponse add(CarriageClassRequest request) {
        // kiem tra class name da ton tai chua //
        CarriageClass carriageClassExisted = carriageClassRepo.findByName(request.getClassName())  ;
        if(carriageClassExisted != null) {
            throw  new AppException(ErrorCode.CARRIAGE_CLASS_EXISTED) ;

        }
        //  luu CarriageClass ////
        CarriageClass carriageClass = carriageClassRepo.save(carriageClassMapper.toEntity(request)) ;
        return carriageClassMapper.toDTO(carriageClass) ;

    }

    @Override
    public CarriageClassResponse updateCarriageClass(CarriageClassRequest request , int id) {
        CarriageClass carriageClassUpdate = carriageClassRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.CARRIAGE_CLASS_NOT_EXISTED)) ;
        // cap nhat lai thong tin carriage class //
        carriageClassUpdate.setName(request.getClassName());
        carriageClassUpdate.setSeatingCapacity(request.getSeatingCapacity());
        // luu lai carriage class sau khi cap nhat //
        carriageClassRepo.save(carriageClassUpdate);
        return carriageClassMapper.toDTO(carriageClassUpdate) ;
    }

    @Override
    public List<CarriageClassResponse> findAll() {
        return carriageClassRepo.findAll().stream().map(carriageClassMapper :: toDTO).toList() ;
    }


}
