package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.Request.BookingRequest;
import com.example.booking_train_backend.DTO.Request.BookingUpdateRequest;
import com.example.booking_train_backend.DTO.Response.BookingResponse;
import com.example.booking_train_backend.Entity.*;
import com.example.booking_train_backend.Repo.*;
import com.example.booking_train_backend.Service.ServiceInterface.BookingService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import com.example.booking_train_backend.mapper.BookingMapper;
import jakarta.transaction.Transactional;
//import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BookingServiceImplement implements BookingService {
    private BookingMapper bookingMapper ;
    private BookingRepo bookingRepo ;
    private UsersRepo usersRepo;
    private BookingStatusRepo bookingStatusRepo ;
    private TrainStationRepo trainStationRepo ;
    private TrainJourneyRepo trainJourneyRepo ;
    private CarriageClassRepo carriageClassRepo ;
    @Autowired
    public BookingServiceImplement(BookingMapper bookingMapper, BookingRepo bookingRepo, UsersRepo usersRepo, BookingStatusRepo bookingStatusRepo, TrainStationRepo trainStationRepo, TrainJourneyRepo trainJourneyRepo, CarriageClassRepo carriageClassRepo) {
        this.bookingMapper = bookingMapper;
        this.bookingRepo = bookingRepo;
        this.usersRepo = usersRepo;
        this.bookingStatusRepo = bookingStatusRepo;
        this.trainStationRepo = trainStationRepo;
        this.trainJourneyRepo = trainJourneyRepo;
        this.carriageClassRepo = carriageClassRepo;
    }

    @Override
    public BookingResponse addBooking(BookingRequest request) {
        Users passenger = usersRepo.findById(request.getPassenger())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)) ;
        TrainStation trainStationStart = trainStationRepo.findByStationName(request.getTrainStationStart()) ;
        TrainStation trainStationEnd = trainStationRepo.findByStationName(request.getTrainStationEnd()) ;
        if(trainStationStart == null || trainStationEnd == null) {
            throw new AppException(ErrorCode.TRAIN_STATION_NOT_EXISTED) ;
        }
        TrainJourney trainJourney = trainJourneyRepo.findById(request.getTrainJourney())
                .orElseThrow(()-> new AppException(ErrorCode.TRAIN_JOURNEY_NOT_EXISTED)) ;
        BookingStatus bookingStatus = bookingStatusRepo.findById(request.getBookingStatus())
                        .orElseThrow(()-> new AppException(ErrorCode.STATUS_NOT_EXISTED) );
        CarriageClass carriageClass = carriageClassRepo.findByName(request.getCarriageClass()) ;

        // kiem tra neu tat ca thoa dieu kien
        Booking booking = bookingMapper.toEntity(request) ;
        // set hai chieu booking status va booking
        booking.setBookingStatus(bookingStatus);
        bookingStatus.getBookings().add(booking) ;
        // set hai chieu passenger va booking
        booking.setUsers(passenger);
        passenger.getBookings().add(booking) ;
        // set hai chieu train station va booking
        booking.setTrainStationStart(trainStationStart);
        trainStationStart.getTrainStationStart().add(booking) ;

        booking.setTrainStationEnd(trainStationEnd);
        trainStationEnd.getTrainStationEnd().add(booking) ;
        // set train journey va booking
        booking.setTrainJourney(trainJourney);
        trainJourney.getBookings().add(booking);

        // set carriage class va booking
        booking.setCarriageClass(carriageClass);
        carriageClass.getBookings().add(booking) ;
        // set cac thong tin con lai cua booking
        booking.setBookingDate(request.getBookingDate());
        booking.setAmountPaid(request.getAmountPaid());
        booking.setTicketNo(request.getTicketNo());
        booking.setSeatNo(request.getSeatNo());

        return bookingMapper.toResponse(bookingRepo.save(booking)) ;

    }

    @Override
    public BookingResponse updateBooking(BookingUpdateRequest request) {
        Booking booking = bookingRepo.findById(request.getId())
                .orElseThrow(()-> new AppException(ErrorCode.BOOKING_NOT_FOUND)) ;
        // booking co ton tai
        // KIEM TRA THONG TIN CAP NHAT
        TrainStation trainStationEnd = trainStationRepo.findByStationName(request.getTrainStationEnd()) ;
        if(trainStationEnd == null) {
            throw new AppException(ErrorCode.TRAIN_STATION_NOT_EXISTED) ;
        }
        TrainJourney trainJourney = trainJourneyRepo.findById(request.getTrainJourney())
                .orElseThrow(()-> new AppException(ErrorCode.TRAIN_JOURNEY_NOT_EXISTED)) ;
        CarriageClass carriageClass = carriageClassRepo.findByName(request.getCarriageClass()) ;
        // CAP NHAT THONG TIN MOI
        // Xoa moi quan he giua booking va trainStation cu
        booking.getTrainStationEnd().getTrainStationEnd().remove(booking) ;
        // cap nhat moi quan he giua booking va trainStation moi
        booking.setTrainStationEnd(trainStationEnd);
        trainStationEnd.getTrainStationEnd().add(booking) ;
        // Xoa moi quan he giua trainJourney va booking
        booking.getTrainJourney().getBookings().remove(booking) ;
        // set trainJourney va booking
        booking.setTrainJourney(trainJourney);
        trainJourney.getBookings().add(booking);
        //Xoa moi quan he giua carriageClass va booking
        booking.getCarriageClass().getBookings().remove(booking) ;
        // set carriageClass va booking
        booking.setCarriageClass(carriageClass);
        carriageClass.getBookings().add(booking) ;
        // CAP NHAT LAI
        return bookingMapper.toResponse(bookingRepo.save(booking)) ;

    }

    @Override
    public BookingResponse findBooking(int id) {
        Booking booking =bookingRepo.findById(id)
                .orElseThrow( () -> new AppException(ErrorCode.BOOKING_NOT_FOUND)) ;
        return bookingMapper.toResponse(booking) ;
    }

    @Override
    public void deleteBooking(int id) {
        Booking booking =bookingRepo.findById(id)
                .orElseThrow( () -> new AppException(ErrorCode.BOOKING_NOT_FOUND)) ;
        // chi xoa booking neu thoi gian dat ve khong qua 10 phut
        // Kiểm tra thời gian đặt và hiện tại
        LocalDateTime now = LocalDateTime.now();
        if (Duration.between(booking.getBookingDate(), now).toMinutes() > 10) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // xoa moi quan he hai chieu
        booking.getBookingStatus().getBookings().remove(booking) ;
        booking.getTrainStationEnd().getTrainStationEnd().remove(booking);
        booking.getTrainStationStart().getTrainStationStart().remove(booking) ;
        booking.getTrainJourney().getBookings().remove(booking) ;
        booking.getCarriageClass().getBookings().remove(booking) ;
        // xoa booking
        bookingRepo.deleteById(id);
    }

    @Override
    public List<BookingResponse> findAll() {
        return bookingRepo.findAll().stream().map(bookingMapper :: toResponse).toList() ;
    }

    private String getCurrentUsername() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken token) {
            Jwt jwt = token.getToken();
            return jwt.getClaimAsString("preferred_username");
        }
        throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    @Override
    public List<BookingResponse> getMyBooking() {
        String username = getCurrentUsername();

        Users passenger = usersRepo.findByUserName(username) ;
        if(passenger == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED) ;
        }

        return passenger.getBookings().stream()
                .map(bookingMapper::toResponse)
                .toList();

    }
}
