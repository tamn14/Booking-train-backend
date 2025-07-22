package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.Request.BookingRequest;
import com.example.booking_train_backend.DTO.Response.BookingResponse;
import com.example.booking_train_backend.Entity.*;
import com.example.booking_train_backend.Properties.PaymentProperties;
import com.example.booking_train_backend.Properties.StatusBooking;
import com.example.booking_train_backend.Repo.*;
import com.example.booking_train_backend.Service.ServiceInterface.BookingService;
import com.example.booking_train_backend.Service.ServiceInterface.EmailService;
import com.example.booking_train_backend.Service.ServiceInterface.QrService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import com.example.booking_train_backend.mapper.BookingMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImplement implements BookingService {
    private final BookingMapper bookingMapper ;
    private final BookingRepo bookingRepo ;
    private final UsersRepo usersRepo;
    private final BookingStatusRepo bookingStatusRepo ;
    private final TrainStationRepo trainStationRepo ;
    private final TrainJourneyRepo trainJourneyRepo ;
    private final CarriageClassRepo carriageClassRepo ;
    private final QrService qrCode ;
    private final EmailService emailService ;
    private final ObjectMapper objectMapper;
    private final PaymentProperties paymentProperties ;


    @Value("${spring.mail.from}")
    private String mailForm ;
    @Value("${Qr.width}")
    private int widthQr ;
    @Value("${Qr.height}")
    private int heightQr ;

    private Users getUserById (int userId) {
        return usersRepo.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)) ;
    }

    private Users getUserByName(String name) {
        Users user  =  usersRepo.findByUserName(name) ;
        if(user == null) {
            throw new  AppException(ErrorCode.USER_NOT_EXISTED) ;
        }
        return user ;

    }
    private TrainStation getTrainStationByName (String name ) {
        TrainStation trainStation =   trainStationRepo.findByStationName(name) ;
        if(trainStation == null) {
            throw new AppException(ErrorCode.TRAIN_STATION_NOT_EXISTED) ;
        }
        return trainStation ;
    }
    private TrainJourney getTrainJourneyById (int trainJourneyId) {
        return trainJourneyRepo.findById(trainJourneyId)
                .orElseThrow(()-> new AppException(ErrorCode.TRAIN_JOURNEY_NOT_EXISTED)) ;
    }
    private BookingStatus getBookingStatusByName (String name) {
        BookingStatus bookingStatus =  bookingStatusRepo.findByName(name) ;
        if(bookingStatus == null) {
            throw new AppException(ErrorCode.BOOKING_STATUS_NOT_EXISTED) ;
        }
        return bookingStatus ;

    }

    private CarriageClass getCarriageClassByName (String name) {
        CarriageClass carriageClass = carriageClassRepo.findByName(name) ;
        if(carriageClass == null) {
            throw new AppException(ErrorCode.CARRIAGE_CLASS_NOT_EXISTED) ;
        }
        return carriageClass ;
    }

    private String getCurrentUsername() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken token) {
            Jwt jwt = token.getToken();
            return jwt.getClaimAsString("preferred_username");
        }
        throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    private void setRelationShipForBooking(Booking booking ,
                                           BookingStatus bookingStatus ,
                                           Users users ,
                                           TrainStation trainStationStart,
                                           TrainStation trainStationEnd ,
                                           TrainJourney trainJourney ,
                                           CarriageClass carriageClass) {
        booking.setBookingStatus(bookingStatus);
        booking.setUsers(users);
        booking.setTrainStationStart(trainStationStart);
        booking.setTrainStationEnd(trainStationEnd);
        booking.setTrainJourney(trainJourney);
        booking.setCarriageClass(carriageClass);

    }
    /// Entity khong cau hinh casccade.ALL nen can xoa thu cong
    private void removeRelationShipForBooking(Booking booking ) {
        booking.setBookingStatus(null);
        booking.setUsers(null);
        booking.setTrainStationStart(null);
        booking.setTrainStationEnd(null);
        booking.setTrainJourney(null);
        booking.setCarriageClass(null);

    }

    private Booking getBookingById(int id) {
        return bookingRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.BOOKING_NOT_FOUND)) ;
    }

    private void checkOwnerBooking(Booking booking) {
        String currentUser = getCurrentUsername() ;
        if (!Objects.equals(booking.getUsers().getUserName(), currentUser)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }


    @Override
    @PreAuthorize("hasRole('USER')")
    public BookingResponse addBooking(BookingRequest request) {
        // kiem tra user co ton tai, trong ung dung dung booking thi khi tao (add) user thi user do duoc xem nhu passenger
        String userName = getCurrentUsername() ;
        Users passenger = getUserByName(userName) ;
        // kiem tra train station
        TrainStation trainStationStart = getTrainStationByName(request.getTrainStationStart()) ;
        TrainStation trainStationEnd = getTrainStationByName(request.getTrainStationEnd()) ;

        // kiem tra trainJourney ton tai
        TrainJourney trainJourney = getTrainJourneyById(request.getTrainJourney()) ;

        // kiem tra bookingStatus ton tai
        BookingStatus bookingStatus = getBookingStatusByName(StatusBooking.PENDING.name()) ;

        // kiem tra carriageClass ton tai
        CarriageClass carriageClass = getCarriageClassByName(request.getCarriageClass()) ;

        // kiem tra neu tat ca thoa dieu kien
        Booking booking = bookingMapper.toEntity(request) ;

        // set hai chieu booking
        setRelationShipForBooking(booking ,
                bookingStatus , passenger ,
                trainStationStart , trainStationEnd ,
                trainJourney ,carriageClass);

        // set cac thong tin con lai cua booking
        booking.setBookingDate(request.getBookingDate());
        booking.setAmountPaid(request.getAmountPaid());
        booking.setTicketNo(request.getTicketNo());
        booking.setSeatNo(request.getSeatNo());

        bookingRepo.save(booking) ;
        try {
            String jsonTickit = objectMapper.writeValueAsString(booking) ;
            byte[] qr = qrCode.generateQRCodeToFile(jsonTickit, widthQr , heightQr ) ;
            if (passenger.getEmail() != null && !passenger.getEmail().isEmpty()) {
                emailService.SendMessage(mailForm, passenger.getEmail(), qr , passenger, booking);
            }

        } catch (WriterException | IOException e) {
            throw new AppException(ErrorCode.EMAIL_SERVICE_FAILED);
        }


        return bookingMapper.toResponse(booking) ;

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public BookingResponse findBooking(int id) {
        Booking booking = getBookingById(id) ;
        return bookingMapper.toResponse(booking) ;
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public void deleteBooking(int id) {
        Booking booking = getBookingById(id) ;
        // kiem tra co phai user dang dang nhap khong, user chi duoc delete booking cua chinh user do
        checkOwnerBooking(booking);
        // chi xoa booking neu thoi gian dat ve khong qua 10 phut
        // Kiểm tra thời gian đặt và hiện tại
        LocalDateTime now = LocalDateTime.now();
        if (Duration.between(booking.getBookingDate(), now).toMinutes() > 30) {
            throw new AppException(ErrorCode.BOOKING_TIMEOUT_FOR_CANCEL);
        }

        // xoa moi quan he hai chieu
        removeRelationShipForBooking(booking);
        // xoa booking
        bookingRepo.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponse> findAll() {
        return bookingRepo.findAll().stream().map(bookingMapper :: toResponse).toList() ;
    }



    @Override
    @PreAuthorize("hasRole('USER')")
    public List<BookingResponse> getMyBooking() {
        String username = getCurrentUsername();
        Users passenger = usersRepo.findByUserName(username) ;
        if(passenger == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED) ;
        }

        return passenger.getBookings().stream()
                .sorted((b1, b2) -> b2.getBookingDate().compareTo(b1.getBookingDate()))
                .map(bookingMapper::toResponse)
                .toList();

    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public byte[] QrForPayment(int bookingId) {

        Booking booking = getBookingById(bookingId) ;

        checkOwnerBooking(booking);

        String paymentUrl = qrCode.createVnpayPaymentUrl(booking);
        try {
            return qrCode.generateQRCodeToFile(paymentUrl, 300, 300);
        } catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_CREATE_QR);
        }
    }
}
