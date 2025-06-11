package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Request.LoginRequest;
import com.example.booking_train_backend.DTO.Request.UsersRequest;
import com.example.booking_train_backend.DTO.Response.LoginResponse;
import com.example.booking_train_backend.DTO.Response.UsersResponse;

import java.util.List;

public interface UserService {
   public LoginResponse login (LoginRequest request) ;
   public UsersResponse createUser (UsersRequest request) ;
   public List<UsersResponse> getAllUsers() ;
   public UsersResponse getUserById(int id) ;
   public UsersResponse updateUser (UsersRequest usersRequest , int id) ;
   public UsersResponse getMyInfor() ;
   public void changePassword () ;
   public void deleteUser(int id) ;

}
