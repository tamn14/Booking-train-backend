package com.example.booking_train_backend.Security;

import com.example.booking_train_backend.Service.ServiceInterface.TokenBlacklistService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
//--- class nay tao filter tuy chinh ke thua OncePerRequestFilter, dung de kiem tra JWT co bi thu hoi khong  ---- //
// Neu Jwt da bi thu hoi thi khong cho dang nhap vao he thong
// OncePerRequestFilter la abstract class tu spring dam bao moi request chi qua filter nay duy nhat 1 lan

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenBlacklistService tokenBlacklistService;
    @Autowired
    public TokenAuthenticationFilter(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // lay gia tri cua header Authorization
        // can lay header vi day la noi ma client goi len de chung minh da dang nhap
        String header = request.getHeader("Authorization") ;
        // Kiem tra xem header co ton tai va co bat dau bang Bearer khong
        if(header != null && header.startsWith("Bearer ")) {
            // neu header bat dau bang Bearer thi cat bo 7 ky tu dau de lay chuoi JWT that su
            String token = header.substring(7) ;
            // kiem tra token co nam trong BlackList hay khong
            if(tokenBlacklistService.isBlacklisted(token)) {
                // neu token nam trong blacklist => HTTP Status
                // tra ve loi 401 neu token khong hop le
                throw new AppException(ErrorCode.UNAUTHENTICATED) ;
            }
        }
        // Code chay den day chung to Token chua bi thu hoi , hoac khong co token thi cho request di tiep
        filterChain.doFilter(request, response);

    }
}
