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
import org.springframework.security.access.prepost.PreAuthorize;
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

    private void checkCarriageClassExisted (String name) {
        CarriageClass carriageClass = carriageClassRepo.findByName(name) ;
        if(carriageClass != null) {
            throw new AppException(ErrorCode.CARRIAGE_CLASS_EXISTED) ;
        }

    }

    private CarriageClass getCarriageClassById(int id) {
       return carriageClassRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.CARRIAGE_CLASS_NOT_EXISTED)) ;
    }




    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CarriageClassResponse add(CarriageClassRequest request) {
        // kiem tra class name da ton tai chua //
       checkCarriageClassExisted(request.getClassName()); ;

        //  luu CarriageClass ////
        CarriageClass carriageClass = carriageClassRepo.save(carriageClassMapper.toEntity(request)) ;
        return carriageClassMapper.toDTO(carriageClass) ;

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CarriageClassResponse updateCarriageClass(CarriageClassRequest request , int id) {
        CarriageClass carriageClassUpdate = getCarriageClassById(id);
        // cap nhat lai thong tin carriage class //
        carriageClassUpdate.setName(request.getClassName());
        carriageClassUpdate.setSeatingCapacity(request.getSeatingCapacity());
        // luu lai carriage class sau khi cap nhat //
        carriageClassRepo.save(carriageClassUpdate);
        return carriageClassMapper.toDTO(carriageClassUpdate) ;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<CarriageClassResponse> findAll() {
        return carriageClassRepo.findAll().stream().map(carriageClassMapper :: toDTO).toList() ;
    }


}
