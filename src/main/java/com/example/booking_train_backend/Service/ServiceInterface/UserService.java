package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Request.ChangePasswordRequest;
import com.example.booking_train_backend.DTO.Request.LoginRequest;
import com.example.booking_train_backend.DTO.Request.UsersRequest;
import com.example.booking_train_backend.DTO.Request.UsersUpdateRequest;
import com.example.booking_train_backend.DTO.Response.LoginResponse;
import com.example.booking_train_backend.DTO.Response.UsersResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

   public String extractUserId(ResponseEntity<?> responseEntity) ; // lay userId tu
   public UsersResponse createUser (UsersRequest request) ;
   public List<UsersResponse> getAllUsers() ;
   public UsersResponse getUserById(int id) ;
   public UsersResponse updateUser (UsersUpdateRequest usersRequest , int id) ;
   public UsersResponse getMyInfor() ;
   public void changePassword (int userId, ChangePasswordRequest request) ;
   public void deleteUser(int id) ;

}
