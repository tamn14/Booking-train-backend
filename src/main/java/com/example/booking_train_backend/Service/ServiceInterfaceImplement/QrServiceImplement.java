package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.Entity.Booking;
import com.example.booking_train_backend.Properties.QrProperties;
import com.example.booking_train_backend.Service.ServiceInterface.QrService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
@Transactional
@RequiredArgsConstructor
public class QrServiceImplement implements QrService {

    private final QrProperties qrProperties ;

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

    @Override
    public String createVnpayPaymentUrl(Booking booking) {
        String vnp_TmnCode = qrProperties.getVnp_TmnCode();
        String vnp_HashSecret = qrProperties.getVnp_HashSecret();
        String vnp_Url = qrProperties.getVnp_Url();
        String returnUrl = qrProperties.getReturnUrl();

        int amount = booking.getAmountPaid() * 100; // VNPAY tính đơn vị là VND * 100

        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", vnp_TmnCode);
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", String.valueOf(booking.getId())); // mã đơn
        params.put("vnp_OrderInfo", "Thanh toan don hang #" + booking.getId());
        params.put("vnp_OrderType", "billpayment");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_IpAddr", "127.0.0.1"); // hoặc IP thật
        params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        // Bước 1: tạo chuỗi dữ liệu để ký
        StringBuilder signData = new StringBuilder();
        StringBuilder queryUrl = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (queryUrl.length() > 0) {
                queryUrl.append("&");
                signData.append("&");
            }
            queryUrl.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII));
            signData.append(entry.getKey()).append("=").append(entry.getValue());
        }

        // Bước 2: ký SHA256
        String secureHash = hmacSHA512(vnp_HashSecret, signData.toString());

        queryUrl.append("&vnp_SecureHash=").append(secureHash);

        return vnp_Url + "?" + queryUrl.toString();
    }

    private String hmacSHA512(String key, String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(secretKeySpec);
            byte[] hashBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Cannot generate HMAC SHA512", e);
        }
    }
}
