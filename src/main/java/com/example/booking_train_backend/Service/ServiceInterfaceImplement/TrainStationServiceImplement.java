package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.Request.TrainStationRequest;
import com.example.booking_train_backend.DTO.Response.TrainStationResponse;
import com.example.booking_train_backend.Entity.Schedule;
import com.example.booking_train_backend.Entity.TrainStation;
import com.example.booking_train_backend.Repo.TrainStationRepo;
import com.example.booking_train_backend.Service.ServiceInterface.TrainStationService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import com.example.booking_train_backend.mapper.TrainStationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainStationServiceImplement implements TrainStationService {
    private TrainStationRepo trainStationRepo ;
    private TrainStationMapper trainStationMapper ;
    @Autowired
    public TrainStationServiceImplement(TrainStationRepo trainStationRepo,
                                        TrainStationMapper trainStationMapper) {
        this.trainStationRepo = trainStationRepo;
        this.trainStationMapper = trainStationMapper;
    }

    private void checkTrainStationExisted (String name) {
        TrainStation trainStation = trainStationRepo.findByStationName(name) ;
        if(trainStation != null) {
            throw  new AppException(ErrorCode.TRAIN_STATION_EXISTED) ;
        }
    }

    private TrainStation getTrainStationByName(String name) {
        TrainStation trainStation = trainStationRepo.findByStationName(name) ;
        if(trainStation == null) {
            throw new AppException(ErrorCode.TRAIN_STATION_NOT_EXISTED) ;
        }
        return trainStation ;
    }



    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public TrainStationResponse addTrainStation(TrainStationRequest request) {
        // Kiem tra train station da ton tai hay chua
       checkTrainStationExisted(request.getName());
        TrainStation trainStation = trainStationMapper.toEntity(request) ;
        trainStationRepo.save(trainStation) ;
        return trainStationMapper.toDTO(trainStation) ;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public TrainStationResponse findByStationName(String name) {
        TrainStation trainStation = getTrainStationByName(name) ;
        return trainStationMapper.toDTO(trainStation);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<TrainStationResponse> findAll() {
        return trainStationRepo.findAll().stream().map(trainStationMapper :: toDTO).toList() ;
    }


}
