package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.Request.*;
import com.example.booking_train_backend.DTO.Response.TrainJourneyResponse;
import com.example.booking_train_backend.Entity.*;
import com.example.booking_train_backend.Repo.*;
import com.example.booking_train_backend.Service.ServiceInterface.TrainJourneyService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import com.example.booking_train_backend.mapper.JourneyCarriageMapper;
import com.example.booking_train_backend.mapper.JourneyStationMapper;
import com.example.booking_train_backend.mapper.TrainJourneyMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TrainJourneyServiceImplement implements TrainJourneyService {
    private final TrainJourneyRepo trainJourneyRepo;
    private final TrainJourneyMapper trainJourneyMapper;
    private final JourneyStationMapper journeyStationMapper ;
    private final JourneyCarriageMapper journeyCarriageMapper ;
    private final JourneyStationRepo journeyStationRepo ;
    private final CarriageClassRepo carriageClassRepo ;
    private final JourneyCarriageRepo journeyCarriageRepo ;
    private final TrainStationRepo trainStationRepo ;
    private final ScheduleRepo scheduleRepo ;
    private final TrainTripRepo trainTripRepo ;
    @Autowired
    public TrainJourneyServiceImplement(TrainJourneyRepo trainJourneyRepo,
                                        TrainJourneyMapper trainJourneyMapper,
                                        JourneyStationMapper journeyStationMapper,
                                        JourneyCarriageMapper journeyCarriageMapper,
                                        JourneyStationRepo journeyStationRepo,
                                        CarriageClassRepo carriageClassRepo,
                                        JourneyCarriageRepo journeyCarriageRepo,
                                        TrainStationRepo trainStationRepo,
                                        ScheduleRepo scheduleRepo,
                                        TrainTripRepo trainTripRepo) {
        this.trainJourneyRepo = trainJourneyRepo;
        this.trainJourneyMapper = trainJourneyMapper;
        this.journeyStationMapper = journeyStationMapper;
        this.journeyCarriageMapper = journeyCarriageMapper;
        this.journeyStationRepo = journeyStationRepo;
        this.carriageClassRepo = carriageClassRepo;
        this.journeyCarriageRepo = journeyCarriageRepo;
        this.trainStationRepo = trainStationRepo;
        this.scheduleRepo = scheduleRepo;
        this.trainTripRepo = trainTripRepo;
    }




    private Schedule getScheduleById(Integer id) {
        return scheduleRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_EXISTED));
    }

    private List<JourneyStation> buildJourneyStations(List<JourneyStationRequest> requests, TrainJourney journey) {
        return requests.stream().map(req -> {
            TrainStation station = trainStationRepo.findById(req.getTrainStationId())
                    .orElseThrow(() -> new AppException(ErrorCode.TRAIN_STATION_NOT_EXISTED));

            JourneyStation stationEntity = journeyStationMapper.toEntity(req, journey, station);
            stationEntity.setTrainJourney(journey);
            stationEntity.setTrainStation(station);
            return stationEntity;
        }).collect(Collectors.toList());
    }

    private List<JourneyCarriage> buildJourneyCarriages(List<JourneyCarriageRequest> requests, TrainJourney journey) {
        return requests.stream().map(req -> {
            CarriageClass carriageClass = carriageClassRepo.findById(req.getCarriageClass())
                    .orElseThrow(() -> new AppException(ErrorCode.CARRIAGE_CLASS_NOT_EXISTED));

            JourneyCarriage carriage = journeyCarriageMapper.toEntity(req, journey, carriageClass);
            carriage.setTrainJourney(journey);
            carriage.setCarriageClass(carriageClass);
            return carriage;
        }).collect(Collectors.toList());
    }

    private void validateJourneyStation(List<JourneyStationRequest> requests) {
        Map<Integer, Integer> stopOrderMap = new HashMap<>();
        for (JourneyStationRequest req : requests) {
            TrainStation station = trainStationRepo.findById(req.getTrainStationId())
                    .orElseThrow(() -> new AppException(ErrorCode.TRAIN_STATION_NOT_EXISTED));

            if (stopOrderMap.containsKey(station.getId()) &&
                    stopOrderMap.get(station.getId()).equals(req.getStopOrder())) {
                throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
            }
            stopOrderMap.put(station.getId(), req.getStopOrder());
        }
    }

    private void validateJourneyCarriage(List<JourneyCarriageRequest> requests) {
        Map<Integer, Integer> positionMap = new HashMap<>();
        for (JourneyCarriageRequest req : requests) {
            CarriageClass carriageClass = carriageClassRepo.findById(req.getCarriageClass())
                    .orElseThrow(() -> new AppException(ErrorCode.CARRIAGE_CLASS_NOT_EXISTED));

            if (positionMap.containsKey(carriageClass.getId()) &&
                    positionMap.get(carriageClass.getId()).equals(req.getPosition())) {
                throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
            }
            positionMap.put(carriageClass.getId(), req.getPosition());
        }
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public TrainJourneyResponse addTrainJourney(TrainJourneyRequest request) {
        // 1. Validate input
        Schedule schedule = getScheduleById(request.getSchedule());
        validateJourneyStation(request.getJourneyStationRequests());
        validateJourneyCarriage(request.getJourneyCarriageRequests());

        // 2. Map to TrainJourney entity and save
        TrainJourney trainJourney = trainJourneyMapper.toEntity(request);
        trainJourney.setSchedule(schedule);
        TrainJourney savedJourney = trainJourneyRepo.save(trainJourney);

        // 3. Build and persist child entities
        List<JourneyStation> journeyStations = buildJourneyStations(request.getJourneyStationRequests(), savedJourney);
        List<JourneyCarriage> journeyCarriages = buildJourneyCarriages(request.getJourneyCarriageRequests(), savedJourney);

        savedJourney.getTrainTrips().forEach(trip -> trip.setTrainJourney(savedJourney));

        journeyStations.forEach(journeyStation -> {
            journeyStationRepo.save(journeyStation) ;
        });
        journeyCarriages.forEach(journeyCarriage -> {
            journeyCarriageRepo.save(journeyCarriage) ;
        });

        List<TrainTrip> trainTrips = new ArrayList<>(savedJourney.getTrainTrips());
        trainTrips.forEach(trip -> {
            trip.setTrainJourney(savedJourney);
            trainTripRepo.save(trip);
        });

        TrainJourney trainJourneyAdd =  trainJourneyRepo.save(savedJourney);
        // 4. Return response
        return trainJourneyMapper.toDTO(trainJourneyAdd);

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public TrainJourneyResponse updateTrainJourney(TrainJourneyUpdateRequest request , int id ) {
        // lay TrainJourney can update
        TrainJourney trainJourney = trainJourneyRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.TRAIN_JOURNEY_NOT_EXISTED)) ;
        //----------- KIEM TRA CAC REQUEST HOP LE-------------//

        trainJourney.setName(request.getName());

        // Kiem tra co muon cap nhat Scheduale khong , neu muon thi trong request co gia gi cua Schedule
        if(request.getSchedule() != null) {
            // nguoi dung muon cap nhat schedule
            // kiem tra schedule
            Schedule scheduleUpdate = scheduleRepo.findById(request.getSchedule())
                    .orElseThrow(()-> new AppException(ErrorCode.SCHEDULE_NOT_EXISTED)) ;

            // hop le
            // xoa quan he hai chieu cua trainjourney cu va shedule
            Schedule oldSchedule = trainJourney.getSchedule();
            if (oldSchedule != null) {
                oldSchedule.getTrainJourneys().remove(trainJourney);
            }
            // cap nhat lai moi quan he moi
            trainJourney.setSchedule(scheduleUpdate);
            scheduleUpdate.getTrainJourneys().add(trainJourney) ;

        }
        TrainJourney saved = trainJourneyRepo.saveAndFlush(trainJourney);
        return trainJourneyMapper.toDTO(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<TrainJourneyResponse> findByName(String name) {
        List<TrainJourney> trainJourneys = trainJourneyRepo.findByName(name) ;
        List<TrainJourneyResponse> trainJourneyResponses = new ArrayList<>() ;
        trainJourneys.forEach(trainJourney -> {
            trainJourneyResponses.add(trainJourneyMapper.toDTO(trainJourney)) ;
        });
        return trainJourneyResponses ;

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public TrainJourneyResponse findById(int id) {
        TrainJourney trainJourney = trainJourneyRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.TRAIN_JOURNEY_NOT_EXISTED)) ;
        return trainJourneyMapper.toDTO(trainJourney) ;
    }
}
