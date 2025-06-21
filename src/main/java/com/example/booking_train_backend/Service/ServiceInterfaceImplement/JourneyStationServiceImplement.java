package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.Request.JourneyStationRequest;
import com.example.booking_train_backend.DTO.Request.JourneyStationUpdateRequest;
import com.example.booking_train_backend.DTO.Response.TrainJourneyResponse;
import com.example.booking_train_backend.Entity.JourneyStation;
import com.example.booking_train_backend.Entity.JourneyStationId;
import com.example.booking_train_backend.Entity.TrainJourney;
import com.example.booking_train_backend.Entity.TrainStation;
import com.example.booking_train_backend.Repo.JourneyStationRepo;
import com.example.booking_train_backend.Repo.TrainJourneyRepo;
import com.example.booking_train_backend.Repo.TrainStationRepo;
import com.example.booking_train_backend.Service.ServiceInterface.JourneyStationService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import com.example.booking_train_backend.mapper.JourneyStationMapper;
import com.example.booking_train_backend.mapper.TrainJourneyMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class JourneyStationServiceImplement implements JourneyStationService {
    private final JourneyStationMapper journeyStationMapper ;
    private final JourneyStationRepo journeyStationRepo ;
    private final TrainJourneyRepo trainJourneyRepo ;
    private final TrainStationRepo trainStationRepo ;
    private final TrainJourneyMapper trainJourneyMapper ;

    @Autowired
    public JourneyStationServiceImplement(JourneyStationMapper journeyStationMapper,
                                          JourneyStationRepo journeyStationRepo,
                                          TrainJourneyRepo trainJourneyRepo,
                                          TrainStationRepo trainStationRepo,
                                          TrainJourneyMapper trainJourneyMapper) {
        this.journeyStationMapper = journeyStationMapper;
        this.journeyStationRepo = journeyStationRepo;
        this.trainJourneyRepo = trainJourneyRepo;
        this.trainStationRepo = trainStationRepo;
        this.trainJourneyMapper = trainJourneyMapper ;
    }



    private TrainJourney getTrainJourneyById(int id) {
        return trainJourneyRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.TRAIN_JOURNEY_NOT_EXISTED)) ;
    }
    private JourneyStation getJourneyStationById (JourneyStationId id ){
        return journeyStationRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.JOURNEY_STATION_NOT_EXISTS)) ;
    }

    private TrainStation getTrainStationById (int id ){
        return trainStationRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.TRAIN_STATION_NOT_EXISTED)) ;
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
    @Override
    public TrainJourneyResponse addJourneyStation(JourneyStationRequest request, int TrainJourneyId) {
        // kiem tra trainJourney co ton tai khong
        TrainJourney trainJourney = getTrainJourneyById(TrainJourneyId) ;
        // kiem tra trainStation co ton tai khong
        TrainStation trainStation = getTrainStationById(request.getTrainStationId()) ;

        List<JourneyStationRequest> journeyStationRequests = new ArrayList<>();
        for (JourneyStation js : trainJourney.getJourneyStations()) {
            journeyStationRequests.add(JourneyStationRequest.builder()
                    .trainStationId(js.getTrainStation().getId())
                    .stopOrder(js.getStopOrder())
                    .departureTime(js.getDepartureTime())
                    .build());
        }
        journeyStationRequests.add(request) ;
        //Kiem tra StopOrder
        validateJourneyStation(journeyStationRequests);
        // Lay ra JourneyStation can insert
        JourneyStation journeyStation = journeyStationMapper.toEntity(request, trainJourney , trainStation ) ;
        // set quan he hai chieu
        journeyStation.setTrainJourney(trainJourney);
        journeyStation.setTrainStation(trainStation);
        JourneyStation journeyStationInsert = journeyStationRepo.save(journeyStation) ;

        TrainJourney train = getTrainJourneyById(trainJourney.getId()) ;

        return trainJourneyMapper.toDTO(train) ;


    }

    @Override
    public TrainJourneyResponse updateJourneyStation(JourneyStationUpdateRequest request, int trainJourneyId) {
        TrainJourney oldJourney = getTrainJourneyById(trainJourneyId);
        JourneyStationId oldId = new JourneyStationId(trainJourneyId, request.getTrainStationId());
        JourneyStation oldStation = getJourneyStationById(oldId);

        // Nếu cập nhật sang một hành trình khác (JourneyId khác)
        if (request.getJourneyId() != null && !Objects.equals(request.getJourneyId(), trainJourneyId)) {
            // Xóa quan hệ cũ
            oldStation.setTrainJourney(null);
            journeyStationRepo.deleteById(oldId);

            // Tạo mới cho hành trình mới
            TrainJourney newJourney = getTrainJourneyById(request.getJourneyId());
            TrainStation trainStation = getTrainStationById(request.getTrainStationId());

            JourneyStation newStation = JourneyStation.builder()
                    .id(new JourneyStationId(newJourney.getId(), trainStation.getId()))
                    .departureTime((request.getDepartureTime() != null)? request.getDepartureTime() : oldStation.getDepartureTime())
                    .stopOrder(oldStation.getStopOrder())
                    .trainJourney(newJourney)
                    .trainStation(trainStation)
                    .build();

            journeyStationRepo.save(newStation);
            return trainJourneyMapper.toDTO(newJourney);
        }

        // Nếu chỉ update departureTime
        if (request.getDepartureTime() != null) {
            oldStation.setDepartureTime(request.getDepartureTime());
        }

        journeyStationRepo.save(oldStation);
        return trainJourneyMapper.toDTO(oldJourney);
    }

    @Override
    public void  deleteJourneyStation(int trainJourneyId, int trainStationId) {
        getTrainJourneyById(trainJourneyId); // Đảm bảo tồn tại
        getTrainStationById(trainStationId); // Đảm bảo tồn tại

        JourneyStationId id = new JourneyStationId(trainJourneyId, trainStationId);
        JourneyStation journeyStation = getJourneyStationById(id);

        journeyStationRepo.deleteById(journeyStation.getId());

    }
}
