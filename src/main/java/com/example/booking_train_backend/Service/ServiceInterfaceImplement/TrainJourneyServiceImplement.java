package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.Request.*;
import com.example.booking_train_backend.DTO.Response.TrainJourneyResponse;
import com.example.booking_train_backend.Entity.*;
import com.example.booking_train_backend.Repo.*;
import com.example.booking_train_backend.Service.ServiceInterface.TrainJourneyService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import com.example.booking_train_backend.mapper.TrainJourneyMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class TrainJourneyServiceImplement implements TrainJourneyService {
    private TrainJourneyRepo trainJourneyRepo;
    private TrainJourneyMapper trainJourneyMapper;
    private JourneyStationRepo journeyStationRepo ;
    private CarriageClassRepo carriageClassRepo ;
    private JourneyCarriageRepo journeyCarriageRepo ;
    private TrainStationRepo trainStationRepo ;
    private ScheduleRepo scheduleRepo ;
    private TrainTripRepo trainTripRepo ;
    @Autowired
    public TrainJourneyServiceImplement(TrainJourneyRepo trainJourneyRepo,
                                        TrainJourneyMapper trainJourneyMapper,
                                        JourneyStationRepo journeyStationRepo,
                                        CarriageClassRepo carriageClassRepo,
                                        JourneyCarriageRepo journeyCarriageRepo,
                                        TrainStationRepo trainStationRepo,
                                        ScheduleRepo scheduleRepo,
                                        TrainTripRepo trainTripRepo) {
        this.trainJourneyRepo = trainJourneyRepo;
        this.trainJourneyMapper = trainJourneyMapper;
        this.journeyStationRepo = journeyStationRepo;
        this.carriageClassRepo = carriageClassRepo;
        this.journeyCarriageRepo = journeyCarriageRepo;
        this.trainStationRepo = trainStationRepo;
        this.scheduleRepo = scheduleRepo;
        this.trainTripRepo = trainTripRepo;
    }

    @Override
    public TrainJourneyResponse addTrainJourney(TrainJourneyRequest request) {

       //----------- KIEM TRA CAC REQUEST HOP LE-------------//
        // kiem tra Schedule
        Schedule schedule = scheduleRepo.findById(request.getSchedule())
                .orElseThrow(()-> new AppException(ErrorCode.SCHEDULE_NOT_EXISTED)) ;
        // kiem tra TrainStation nam trong JourneyStation co hop le khong
        // tao map luu TrainStation va stopOrder
        Map<Integer , Integer> mapStopOrder = new HashMap<>() ;

        request.getJourneyStationRequests().forEach(
                journeyStationRequest -> {
                   TrainStation trainStation = trainStationRepo.findById(journeyStationRequest.getTrainStationId())
                           .orElseThrow(()-> new AppException(ErrorCode.TRAIN_STATION_NOT_EXISTED)) ;
                   // kiem tra stopOrder
                   if((!mapStopOrder.isEmpty()) && (mapStopOrder.containsKey(trainStation.getId()) && mapStopOrder.get(trainStation.getId()).equals(journeyStationRequest.getStopOrder())) ){
                        throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION) ;
                   }
                   mapStopOrder.put(trainStation.getId() , journeyStationRequest.getStopOrder()) ;
                }
        );
        // kiem tra CarriageClass trong JourneyCarriage
        Map<Integer , Integer> mapPosition = new HashMap<>() ;
        request.getJourneyCarriageRequests().forEach(
                journeyCarriageRequest -> {
                    CarriageClass carriageClass = carriageClassRepo.findById(journeyCarriageRequest.getCarriageClass())
                            .orElseThrow(()-> new AppException(ErrorCode.CARRIAGE_CLASS_NOT_EXISTED)) ;
                    // kiem tra stopOrder
                    if((!mapPosition.isEmpty()) && (mapPosition.containsKey(carriageClass.getId()) && mapPosition.get(carriageClass.getId()).equals(journeyCarriageRequest.getPosition())) ){
                        throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION) ;
                    }
                    mapPosition.put(carriageClass.getId() , journeyCarriageRequest.getPosition()) ;
                }
        );



        // ------------------ CHUYEN REQUEST VE ENTITY DE REPO CO THE GOI DUOC ------------------- //
        TrainJourney trainJourney = trainJourneyMapper.toEntity(request) ;
        TrainJourney savedJourney = trainJourneyRepo.save(trainJourney);

        // set quan he hai chuyeu cho JourneyStation , JourneyCarriage , TrainTrip  , Schedule
        trainJourney.setSchedule(schedule);
        schedule.getTrainJourneys().add(trainJourney)  ;

        trainJourney.getJourneyStations().forEach(journeyStation -> {
            journeyStation.setTrainJourney(savedJourney);
            journeyStation.setId(new JourneyStationId(savedJourney.getId(), journeyStation.getTrainStation().getId()));
        });

        trainJourney.getJourneyCarriages().forEach(journeyCarriage -> {
            journeyCarriage.setTrainJourney(savedJourney);
            journeyCarriage.setId(new JourneyCarriageId(savedJourney.getId(), journeyCarriage.getCarriageClass().getId()));
        });

        trainJourney.getTrainTrips().forEach(trainTrip -> {
            trainTrip.setTrainJourney(savedJourney);
        });

        journeyStationRepo.saveAll(trainJourney.getJourneyStations());
        journeyCarriageRepo.saveAll(trainJourney.getJourneyCarriages());
        trainTripRepo.saveAll(trainJourney.getTrainTrips()) ;
        return trainJourneyMapper.toDTO(savedJourney);

    }

    @Override
    public TrainJourneyResponse updateTrainJourney(TrainJourneyUpdateRequest request , int id ) {
        // lay TrainJourney can update
        TrainJourney trainJourney = trainJourneyRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.TRAIN_JOURNEY_NOT_EXISTED)) ;
        //----------- KIEM TRA CAC REQUEST HOP LE-------------//
        // kiem tra Schedule
        Schedule scheduleUpdate = scheduleRepo.findById(request.getSchedule())
                .orElseThrow(()-> new AppException(ErrorCode.SCHEDULE_NOT_EXISTED)) ;

        // xoa quan he hai chieu cua trainjourney cu va shedule
        Schedule oldSchedule = trainJourney.getSchedule();
        if (oldSchedule != null) {
            oldSchedule.getTrainJourneys().remove(trainJourney);
        }

        // cap nhat lai moi quan he moi
        trainJourney.setName(request.getName());
        trainJourney.setSchedule(scheduleUpdate);
        scheduleUpdate.getTrainJourneys().add(trainJourney) ;


        TrainJourney saved = trainJourneyRepo.saveAndFlush(trainJourney);
        return trainJourneyMapper.toDTO(saved);
    }

    @Override
    public List<TrainJourneyResponse> findByName(String name) {
        List<TrainJourney> trainJourneys = trainJourneyRepo.findByName(name) ;
        List<TrainJourneyResponse> trainJourneyResponses = new ArrayList<>() ;
        trainJourneys.forEach(trainJourney -> {
            trainJourneyResponses.add(trainJourneyMapper.toDTO(trainJourney)) ;
        });
        return trainJourneyResponses ;

    }

    @Override
    public TrainJourneyResponse findById(int id) {
        TrainJourney trainJourney = trainJourneyRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.TRAIN_JOURNEY_NOT_EXISTED)) ;
        return trainJourneyMapper.toDTO(trainJourney) ;
    }
}
