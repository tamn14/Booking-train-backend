package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.Request.JourneyCarriageRequest;
import com.example.booking_train_backend.DTO.Request.JourneyCarriageUpdateRequest;
import com.example.booking_train_backend.DTO.Response.TrainJourneyResponse;
import com.example.booking_train_backend.Entity.*;
import com.example.booking_train_backend.Repo.CarriageClassRepo;
import com.example.booking_train_backend.Repo.JourneyCarriageRepo;
import com.example.booking_train_backend.Repo.TrainJourneyRepo;
import com.example.booking_train_backend.Service.ServiceInterface.JourneyCarriageService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import com.example.booking_train_backend.mapper.CarriageClassMapper;
import com.example.booking_train_backend.mapper.JourneyCarriageMapper;
import com.example.booking_train_backend.mapper.TrainJourneyMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class JourneyCarriageServiceImplement implements JourneyCarriageService {
    private final JourneyCarriageMapper journeyCarriageMapper ;
    private final TrainJourneyMapper trainJourneyMapper ;
    private final CarriageClassMapper carriageClassMapper ;
    private final JourneyCarriageRepo journeyCarriageRepo ;
    private final TrainJourneyRepo trainJourneyRepo ;
    private final CarriageClassRepo carriageClassRepo ;
    @Autowired
    public JourneyCarriageServiceImplement(JourneyCarriageMapper journeyCarriageMapper,
                                           TrainJourneyMapper trainJourneyMapper,
                                           CarriageClassMapper carriageClassMapper,
                                           JourneyCarriageRepo journeyCarriageRepo,
                                           TrainJourneyRepo trainJourneyRepo,
                                           CarriageClassRepo carriageClassRepo) {
        this.journeyCarriageMapper = journeyCarriageMapper;
        this.trainJourneyMapper = trainJourneyMapper;
        this.carriageClassMapper = carriageClassMapper;
        this.journeyCarriageRepo = journeyCarriageRepo;
        this.trainJourneyRepo = trainJourneyRepo;
        this.carriageClassRepo = carriageClassRepo;
    }


    private TrainJourney getTrainJourneyById(int id) {
        return trainJourneyRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.TRAIN_JOURNEY_NOT_EXISTED)) ;
    }
    private JourneyCarriage getJourneyCarriage (JourneyCarriageId id ){
        return journeyCarriageRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.JOURNEY_CARRIAGE_NOT_EXISTS)) ;
    }


    private CarriageClass getCarriageClass(int id) {
        return carriageClassRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.CARRIAGE_CLASS_NOT_EXISTED)) ;
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
    public TrainJourneyResponse addJourneyCarriage(JourneyCarriageRequest request, int TrainJourneyId) {
        // kiem tra TrainJourney va CarriageClass
        TrainJourney trainJourney = getTrainJourneyById(TrainJourneyId) ;
        CarriageClass carriageClass = getCarriageClass(request.getCarriageClass()) ;

        List<JourneyCarriageRequest> journeyCarriageRequests = new ArrayList<>() ;
        for(JourneyCarriage js : trainJourney.getJourneyCarriages()) {
            journeyCarriageRequests.add(JourneyCarriageRequest.builder()
                    .carriageClass(js.getCarriageClass().getId())
                    .position(js.getPosition())
                    .build()
            );

        }
        journeyCarriageRequests.add(request) ;

        // kiem tra possition
        validateJourneyCarriage(journeyCarriageRequests);

        // lay journeyCarriage can insert
        JourneyCarriage journeyCarriage = journeyCarriageMapper.toEntity(request , trainJourney , carriageClass) ;

        // set moi quan he hai chieu
        journeyCarriage.setTrainJourney(trainJourney);
        journeyCarriage.setCarriageClass(carriageClass);

        // save
        JourneyCarriage journeyCarriageInsert = journeyCarriageRepo.save(journeyCarriage) ;
        TrainJourney trainJourneyInserted = getTrainJourneyById(trainJourney.getId()) ;
        return trainJourneyMapper.toDTO(trainJourneyInserted) ;

    }

    @Override
    public TrainJourneyResponse updateJourneyCarriage(JourneyCarriageUpdateRequest request, int trainJourneyId) {
        TrainJourney oldJourney = getTrainJourneyById(trainJourneyId);
        JourneyCarriageId oldId = new JourneyCarriageId(trainJourneyId, request.getCarriageClass());
        JourneyCarriage oldJourneyCarriage = getJourneyCarriage(oldId);

        // Nếu đổi sang TrainJourney khác
        if (request.getTrainJourneyId() != null && !Objects.equals(request.getTrainJourneyId(), trainJourneyId)) {
            // Xóa cũ
            oldJourneyCarriage.setTrainJourney(null);
            journeyCarriageRepo.deleteById(oldId);

            // Tạo mới
            TrainJourney newJourney = getTrainJourneyById(request.getTrainJourneyId());
            CarriageClass carriageClass = getCarriageClass(request.getCarriageClass());

            JourneyCarriage newJourneyCarriage = JourneyCarriage.builder()
                    .id(new JourneyCarriageId(newJourney.getId(), carriageClass.getId()))
                    .position(oldJourneyCarriage.getPosition()) // không cho đổi
                    .trainJourney(newJourney)
                    .carriageClass(carriageClass)
                    .build();

            journeyCarriageRepo.save(newJourneyCarriage);
            return trainJourneyMapper.toDTO(newJourney);
        }

        // Nếu không đổi hành trình → không cho update gì cả
        return trainJourneyMapper.toDTO(oldJourney);
    }


    @Override
    public void deleteJourneyCarriage(int trainJourneyId, int carriageClassId) {
        getTrainJourneyById(trainJourneyId); // Đảm bảo tồn tại
        getCarriageClass(carriageClassId); // Đảm bảo tồn tại

        JourneyCarriageId id = new JourneyCarriageId(trainJourneyId, carriageClassId);
        JourneyCarriage journeyCarriage = getJourneyCarriage(id);

        journeyCarriageRepo.deleteById(journeyCarriage.getId());
    }
}
