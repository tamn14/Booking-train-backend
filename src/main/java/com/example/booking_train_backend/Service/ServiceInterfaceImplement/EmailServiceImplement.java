package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.Entity.Booking;
import com.example.booking_train_backend.Entity.Users;
import com.example.booking_train_backend.Service.ServiceInterface.EmailService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImplement implements EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImplement(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void SendMessage(String from, String to, byte[] qrBytes , Users passenger , Booking booking) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("Booking Train - Vé của bạn");

            String html = """
    <p>Xin chào, %s</p>
    <p>Bạn đã đặt vé thành công với mã: <strong>%s</strong></p>
    <p>Thông tin hành trình: %s → %s</p>
    <p>Ngày đặt: %s</p>
    <p>Dưới đây là mã QR để soát vé:</p>
    <img src='cid:qrCode'/>
    """.formatted(passenger.getFirstName(), booking.getTicketNo(),
                    booking.getTrainStationStart().getStationName(),
                    booking.getTrainStationEnd().getStationName(),
                    booking.getBookingDate());

            helper.setText(html, true);
            helper.addInline("qrCode", new ByteArrayResource(qrBytes), "image/png");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Gửi email thất bại", e);
        }
    }

    @Override
    public void verifyEmail(String from, String to, String numberVerify , String name) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("SmartCard - Xac thuc tai khoan");

            String html = """
    <p>Xin chào, %s</p>
    <p>Dưới đây là mã xac thuc để xac thuc tai khoan cua ban:</p>
    <p>%s</p>
    """.formatted(name,
                    numberVerify);
            helper.setText(html, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.EMAIL_SERVICE_FAILED) ;
        }
    }
}

