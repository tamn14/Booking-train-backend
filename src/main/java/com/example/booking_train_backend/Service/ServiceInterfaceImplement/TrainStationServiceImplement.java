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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainStationServiceImplement implements TrainStationService {
    private TrainStationRepo trainStationRepo ;
    private TrainStationMapper trainStationMapper ;
    @Override
    public TrainStationResponse addTrainStation(TrainStationRequest request) {
        // Kiem tra train station da ton tai hay chua
        if(trainStationRepo.findByStationName(request.getName()) != null) {
            throw  new AppException(ErrorCode.TRAIN_STATION_EXISTED) ;
        }
        TrainStation trainStation = trainStationMapper.toEntity(request) ;
        trainStationRepo.save(trainStation) ;
        return trainStationMapper.toDTO(trainStation) ;
    }

    @Override
    public TrainStationResponse findByStationName(String name) {
        TrainStation trainStation = trainStationRepo.findByStationName(name) ;
        if(trainStation == null) {
            throw new AppException(ErrorCode.TRAIN_STATION_NOT_EXISTED) ;
        }
        return trainStationMapper.toDTO(trainStation);
    }

    @Override
    public List<TrainStationResponse> findAll() {
        return trainStationRepo.findAll().stream().map(trainStationMapper :: toDTO).toList() ;
    }


}
