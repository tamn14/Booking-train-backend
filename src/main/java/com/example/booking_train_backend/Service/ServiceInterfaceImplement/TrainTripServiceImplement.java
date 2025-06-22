package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.Request.TrainTripRequest;
import com.example.booking_train_backend.DTO.Request.TrainTripUpdateRequest;
import com.example.booking_train_backend.DTO.Response.TrainJourneyResponse;
import com.example.booking_train_backend.Entity.TrainJourney;
import com.example.booking_train_backend.Entity.TrainTrip;
import com.example.booking_train_backend.Repo.TrainJourneyRepo;
import com.example.booking_train_backend.Repo.TrainTripRepo;
import com.example.booking_train_backend.Service.ServiceInterface.TrainTripService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import com.example.booking_train_backend.mapper.TrainJourneyMapper;
import com.example.booking_train_backend.mapper.TrainTripMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Transactional
public class TrainTripServiceImplement implements TrainTripService {
    private final TrainTripMapper trainTripMapper ;
    private final TrainJourneyMapper trainJourneyMapper  ;
    private final TrainTripRepo trainTripRepo ;
    private final TrainJourneyRepo trainJourneyRepo ;
    @Autowired
    public TrainTripServiceImplement(TrainTripMapper trainTripMapper,
                                     TrainJourneyMapper trainJourneyMapper,
                                     TrainTripRepo trainTripRepo,
                                     TrainJourneyRepo trainJourneyRepo) {
        this.trainTripMapper = trainTripMapper;
        this.trainJourneyMapper = trainJourneyMapper;
        this.trainTripRepo = trainTripRepo;
        this.trainJourneyRepo = trainJourneyRepo;
    }

    private TrainJourney getTrainJourneyById(int id) {
        return trainJourneyRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.TRAIN_JOURNEY_NOT_EXISTED)) ;
    }

    private TrainTrip getTrainTripById(int id) {
        return trainTripRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.TRAIN_TRIP_NOT_EXISTED)) ;
    }

    @Override
    public TrainJourneyResponse addTrainTrip(TrainTripRequest request, int TrainJourneyId) {
        // kiem tra TrainJourney
        TrainJourney trainJourney = getTrainJourneyById(TrainJourneyId) ;
        // tao moi TrainTrip
        TrainTrip trainTrip = trainTripMapper.toEntity(request) ;
        // set quan he hai chieu
        trainTrip.setTrainJourney(trainJourney);
        // save
        trainTripRepo.save(trainTrip) ;
        return trainJourneyMapper.toDTO(getTrainJourneyById(trainJourney.getId())) ;
    }

    @Override
    public TrainJourneyResponse updateTrainTrip(TrainTripUpdateRequest request, int trainTripId , int trainJourneyId) {
        // kiem tra TrainJourney
        TrainJourney oldTrainJourney = getTrainJourneyById(request.getTrainJourneyId()) ;
        // kiem tra TrainTrip
        TrainTrip oldTrainTrip = getTrainTripById(trainTripId) ;
        // truong hop update TrainJourney
        if(request.getTrainJourneyId() != null &&  !Objects.equals(trainJourneyId, request.getTrainJourneyId())){
            // xoa quan he hai chieu cu
            oldTrainTrip.setTrainJourney(null);
            trainTripRepo.deleteById(trainTripId);
            // tao moi
            TrainJourney newTrainJourney = getTrainJourneyById(request.getTrainJourneyId()) ;
            TrainTrip newTrainTrip = TrainTrip.builder()
                    .departureTime((request.getDepartureTime()!=null) ? request.getDepartureTime() : oldTrainTrip.getDepartureTime())
                    .departureDate((request.getDepartureDate()!= null) ? request.getDepartureDate() : oldTrainTrip.getDepartureDate())
                    .trainJourney(newTrainJourney)
                    .build() ;

            // luu lai
            trainTripRepo.save(newTrainTrip) ;

            return trainJourneyMapper.toDTO(getTrainJourneyById(newTrainJourney.getId())) ;
        }
        // khong cap nhat TrainJourney co , luc nay co the chi la cap nhat departureTime hoac departureDate
        if (request.getDepartureTime() != null) {
            oldTrainTrip.setDepartureTime(request.getDepartureTime());
        }
        if (request.getDepartureDate() != null) {
            oldTrainTrip.setDepartureDate(request.getDepartureDate());
        }

        trainTripRepo.save(oldTrainTrip);
        return trainJourneyMapper.toDTO(oldTrainTrip.getTrainJourney());

    }

    @Override
    public void deleteTrainTrip(int TrainJourneyId, int TrainTripId) {
        // kiem tra TrainJourney
        TrainJourney oldTrainJourney = getTrainJourneyById(TrainJourneyId) ;
        // kiem tra TrainTrip
        TrainTrip trainTrip = getTrainTripById(TrainTripId) ;
        trainTripRepo.deleteById(TrainTripId);
    }
}
