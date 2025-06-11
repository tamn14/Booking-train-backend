package com.example.booking_train_backend.Security;

import com.example.booking_train_backend.Entity.Users;
import com.example.booking_train_backend.Repo.UsersRepo;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.security.Principal;

//--CLASS NAY DUNG DE KIEM TRA NGUOI DUNG HIEN TAI CO PHAI LA CHU SO HUU CUA RESOURCE NAO KHONG--//
@Component("authz")
public class AuthorizationHelper {
    @Autowired
    private UsersRepo usersRepo ;
    public boolean isOwner (int  userId) {
        // Lay authentication hien tai tu security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
        if(authentication == null) {
            return false ; // chua ai dang nhap nen tra ve false
        }
        // lay sub keycloak
        var principal = authentication.getPrincipal() ; // lay principal
        if(principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal ;
            // lay sub
            String sub = jwt.getClaimAsString("sub");
            // so sanh  userKeycloakId
            Users user = usersRepo.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)) ;
            return sub.equals(user.getUserKeycloakId());
        }
        return false ;
    }
}
