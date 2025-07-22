package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.Entity.Booking;
import com.google.zxing.WriterException;

import java.io.IOException;

public interface QrService {
    public byte[] generateQRCodeToFile(String json , int width, int height)  throws WriterException , IOException;
    public String createVnpayPaymentUrl (Booking booking) ;
}
