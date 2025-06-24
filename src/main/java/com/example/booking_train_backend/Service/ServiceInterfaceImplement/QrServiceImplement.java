package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.Service.ServiceInterface.QrService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class QrServiceImplement implements QrService {

    @Override
    public byte[] generateQRCodeToFile(String json, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter() ;
        Map<EncodeHintType , Object> hintTypeObjectMap = new HashMap<>() ;
        hintTypeObjectMap.put(EncodeHintType.CHARACTER_SET , "UTF-8") ;

        BitMatrix bitMatrix = qrCodeWriter.encode(json, BarcodeFormat.QR_CODE, width, height, hintTypeObjectMap);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();

    }
}
